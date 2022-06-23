package media.platform.acsrmqsim.rmq.module;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import media.platform.acsrmqsim.common.FileUtil;
import media.platform.acsrmqsim.rmq.types.RmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmqParser {
    static final Logger log = LoggerFactory.getLogger(RmqParser.class);

    private RmqParser() {
        // Do Nothing
    }

    /** json  -> RmqMessage */
    public static RmqMessage parse(String json) {
        Gson gson = getGson();
        return getRmqMessage(json, gson);
    }

    private static RmqMessage getRmqMessage(String json, Gson gson) {
        return gson.fromJson(json, RmqMessage.class);
    }

    private static Gson getGson() {
        return new Gson();
    }

    /** file -> json  -> RmqMessage */
    public static RmqMessage fileParse(String filePath){
        try {
            String json = FileUtil.filepathToString(filePath);
            return parse(json);
        } catch (Exception e) {
            LoggerFactory.getLogger(RmqParser.class).error("UdpServer Error Occurs", e);
        }

        return null;
    }

    public static String parsingCallId(JsonElement json) {
        String callId = "";
        try {
            if (json.getAsJsonObject().get("callId") != null) {
                callId = json.getAsJsonObject().get("callId").getAsString();
            }
        } catch (Exception e) {
            log.error("RmqParser.parsingCallId.Exception ", e);
        }
        return callId;
    }

    public static String parsingTaskId(JsonElement json) {
        String taskId = "";
        try {
            if (json.getAsJsonObject().get("taskId") != null) {
                taskId = json.getAsJsonObject().get("taskId").getAsString();
            }
        } catch (Exception e) {
            log.error("RmqParser.parsingTaskId.Exception ", e);
        }
        return taskId;
    }
}
