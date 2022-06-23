package media.platform.acsrmqsim.tracing;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapAdapter;
import io.opentracing.tag.Tags;
import media.platform.acsrmqsim.model.info.SessionInfo;
import media.platform.acsrmqsim.rmq.types.RmqMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author dajin kim
 */
public class TraceManager {
    private static final Logger log = LoggerFactory.getLogger(TraceManager.class);
    private static Tracer tracer = null;

    private TraceManager() {
    }

    /**
     * @fn init
     * @brief Trace 생성 함수
     * @param service : Tracer 이름
     * @return Tracer
     * */
    public static Tracer init(String service) {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        Configuration.SenderConfiguration senderConfiguration = Configuration.SenderConfiguration.fromEnv()
                .withAgentHost("192.168.7.33")
                .withAgentPort(6831);

        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true)
                .withSender(senderConfiguration);

        Configuration config = new Configuration(service)
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);

        return config.getTracer();
    }

    public static Tracer getTracer() {
        if (tracer == null)
            tracer = init("ACS_SIM");

        return tracer;
    }

    public static String getUberTraceId(Span span) {
        JaegerSpanContext spanContext = (JaegerSpanContext) span.context();
        String traceId = spanContext.toTraceId();
        String spanId = spanContext.toSpanId();
        byte flag = spanContext.getFlags();

        String uberTraceId = traceId + ":" + spanId + ":" + traceId + ":" + flag;

        //System.out.println("uberTraceId===> " + uberTraceId);

        return uberTraceId;

    }

    public static Span startServerSpan(Tracer tracer, String uberId, String operationName, SessionInfo sessionInfo) {
        // format the headers for extraction
        final HashMap<String, String> headers = new HashMap<>();
        if (!uberId.isEmpty()) {
            headers.put("uber-trace-id", uberId);
        }

        Tracer.SpanBuilder spanBuilder;
        try {
            SpanContext parentSpanCtx = tracer.extract(Format.Builtin.TEXT_MAP, new TextMapAdapter(headers));
            if (parentSpanCtx == null) {
                if (sessionInfo.getRootSpan() != null) {
                    spanBuilder = tracer.buildSpan(operationName).asChildOf(sessionInfo.getRootSpan());
                } else {
                    spanBuilder = tracer.buildSpan(operationName);
                }
            } else {
                spanBuilder = tracer.buildSpan(operationName).asChildOf(parentSpanCtx);
            }
        } catch (IllegalArgumentException e) {
            spanBuilder = tracer.buildSpan(operationName);
        }
        return spanBuilder.withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER).start();
    }

    public static Span startServerSpan(Tracer tracer, RmqMessage rmqMessage, String spanName) {
        // format the headers for extraction
        RmqMessageExtractAdapter carrier = new RmqMessageExtractAdapter(rmqMessage.getBody());
        Iterator<Map.Entry<String, String>> iterator = carrier.iterator();
        log.debug("RmqExtractAdapter");

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            //System.out.println(entry.getKey() + " : " + entry.getValue());
            log.debug("Key: {}, Value: {}", entry.getKey(), entry.getValue());
            log.debug("Entry: {}", entry);
        }

        Tracer.SpanBuilder spanBuilder;

        try {
            SpanContext parentSpanCtx = tracer.extract(Format.Builtin.TEXT_MAP, carrier);
            if (parentSpanCtx == null) {
                spanBuilder = tracer.buildSpan(spanName);
            } else {
                spanBuilder = tracer.buildSpan(spanName).asChildOf(parentSpanCtx);
            }
        } catch (IllegalArgumentException e) {
            spanBuilder = tracer.buildSpan(spanName);
        }
        return spanBuilder.withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER).start();
    }

    private static class RmqMessageExtractAdapter implements TextMap {
        private final Map<String, String> headers = new LinkedHashMap<>();
        private static final String KEY = "uber-trace-id";

/*        RmqMessageExtractAdapter(RmqMessage msg, List<String> jaegerHeader) {
            JsonObject bodyObj = msg.getBody().getAsJsonObject();
            for (String header : jaegerHeader) {
                headers.put(header, bodyObj.get(header).toString());
            }
        }*/

        RmqMessageExtractAdapter(JsonElement body) {
            JsonObject bodyObj = body.getAsJsonObject();
            headers.put(KEY, bodyObj.get(KEY).toString());
        }

        RmqMessageExtractAdapter(JsonElement body, List<String> jaegerHeader) {
            JsonObject bodyObj = body.getAsJsonObject();
            for (String header : jaegerHeader) {
                headers.put(header, bodyObj.get(header).toString());
            }
        }

        RmqMessageExtractAdapter(String rmqMessage) throws ParseException {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(rmqMessage);
            JSONObject bodyObj = (JSONObject) parser.parse(jsonObj.get("body").toString());
            headers.put(KEY, bodyObj.get(KEY).toString());
        }

        RmqMessageExtractAdapter(String rmqMessage, List<String> jaegerHeader) throws ParseException {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(rmqMessage);
            JSONObject bodyObj = (JSONObject) parser.parse(jsonObj.get("body").toString());

            for (String header : jaegerHeader) {
                headers.put(header, bodyObj.get(header).toString());
            }
        }

        @Override
        public Iterator<Map.Entry<String, String>> iterator() {
            return headers.entrySet().iterator();
        }

        @Override
        public void put(String key, String value) {
            throw new UnsupportedOperationException();
        }
    }
}
