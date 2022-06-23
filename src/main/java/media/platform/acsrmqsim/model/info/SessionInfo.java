package media.platform.acsrmqsim.model.info;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import media.platform.acsrmqsim.scenario.DirType;
import media.platform.acsrmqsim.tracing.TraceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author dajin kim
 */
public class SessionInfo {
    private static final Logger log = LoggerFactory.getLogger(SessionInfo.class);

    private final String sessionId;
    private final ConcurrentHashMap<String, String> bodyMap;

    private final ConcurrentHashMap<String, Scope> scopeMap;
    private final ConcurrentHashMap<String, Span> spanMap;
    private Tracer tracer;
    private String rootSpanName;
    private Span rootSpan;

    private long createTime;
    private int msgIndex;
    private long lastSendTime;

    public SessionInfo(String sessionId) {
        this.sessionId = sessionId;
        this.bodyMap = new ConcurrentHashMap<>();
        this.scopeMap = new ConcurrentHashMap<>();
        this.spanMap = new ConcurrentHashMap<>();
        //this.tracer = TraceManager.getTracer();
        //this.rootSpan = tracer.buildSpan(sessionId).start();
        this.rootSpanName = sessionId;
        //activateSpan(rootSpanName, rootSpan);
    }

    public Tracer getTracer() {
        return tracer;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void addValue(String key, String value) {
        synchronized (bodyMap) {
            if (bodyMap.containsKey(key)) return;
            log.debug("({}) Put Body Map -> {}:{}", sessionId, key, value);
            bodyMap.put(key, value);
        }
    }

    public ConcurrentMap<String, String> getBodyMap() {
        return bodyMap;
    }

    public String getValue(String key) {
        synchronized (bodyMap) {
            if (bodyMap.containsKey(key))
                return bodyMap.get(key);
            else
                log.debug("");
        }
        return null;
    }

    public Span getRootSpan() {
        return rootSpan;
    }

    public void setRootSpan(Span rootSpan) {
        this.rootSpan = rootSpan;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(int msgIndex) {
        if (this.msgIndex == msgIndex) return;
        //log.debug("({}) SessionInfo set Index: {}", sessionId, msgIndex);
        this.msgIndex = msgIndex;
    }

    public void increaseMsgIndex() {
        ++msgIndex;
    }

    public long getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(long lastSendTime) {
        this.lastSendTime = lastSendTime;
    }


    public void procSpan(String type, DirType dirType, String parentType) {
        Span span;
        if (parentType != null && !parentType.isEmpty()) {
            span = buildSpan(type, dirType, parentType);
        } else {
            span = buildSpan(type, dirType);
        }


    }

    private Span buildSpan(String type, DirType dirType) {
        log.debug("({}) SpanStart ({}:{})", sessionId, type, dirType.getValue());
        Span span = buildAndStart(type);
        activateSpan(type, span);

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("Dir", dirType.getValue());
        fields.put("MsgType", type);
        fields.put("SessionId", sessionId);
        span.log(fields);
        spanMap.put(type, span);
        return span;
    }

    private Span buildSpan(String type, DirType dirType, String parentType) {
        log.debug("({}) SpanStart ({}:{})", sessionId, type, dirType.getValue());

        // asChildOf
        //Span span = buildSpan(type, parentType);

        Span span = buildAndStart(type);
        activateSpan(type, span);

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("Dir", dirType.getValue());
        fields.put("MsgType", type);
        fields.put("SessionId", sessionId);
        span.log(fields);
        spanMap.put(type, span);
        return span;
    }

    private void activateSpan(String type, Span span) {
        Scope scope = tracer.scopeManager().activate(span);
        if (scope != null) {
            scopeMap.put(type, scope);
        }
    }

    public void finishSpan(String type) {
        if (!type.equals(rootSpanName) && spanMap.containsKey(type)) {
            log.debug("({}) SpanFinished: {}", sessionId, type);

            closeScope(type);
            Span span = spanMap.remove(type);
            span.finish();
        }
    }

    public void finishRootSpan() {
        if (rootSpan != null) {
            log.debug("({}) Finished RootSpan: {}", sessionId, rootSpanName);
            // 남은 span finish
            spanMap.forEach((spanName, span) -> {
                closeScope(spanName);
                span.finish();
            });
            closeScope(rootSpanName);
            rootSpan.finish();
        } else {
            log.debug("RootSpan is Null");
        }
    }

    private void closeScope(String type) {
        if (scopeMap.containsKey(type)) {
            log.debug("({}) Close Scope: {}", sessionId, type);
            Scope scope = scopeMap.remove(type);
            scope.close();
        }
    }

    private Span buildSpan(String methodName, String parentMethod) {
        Span parentSpan = null;
        if (parentMethod != null) {
            parentSpan = spanMap.get(parentMethod);
        }

        if (parentSpan != null) {
            return buildChildSpan(methodName, parentSpan);
        } else {
            return buildAndStart(methodName);
        }
    }

    private Span buildAndStart(String methodName) {
        if (rootSpan != null)
            return buildChildSpan(methodName, rootSpan);
        return tracer.buildSpan(methodName).start();
    }

    private Span buildChildSpan(String methodName, Span parentSpan) {
        return tracer.buildSpan(methodName).asChildOf(parentSpan).start();
    }
}
