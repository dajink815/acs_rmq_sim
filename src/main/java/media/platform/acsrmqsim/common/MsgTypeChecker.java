package media.platform.acsrmqsim.common;

/**
 * @author dajin kim
 */
public class MsgTypeChecker {

    private MsgTypeChecker() {
        // Do Nothing
    }

    public static boolean checkLogType(String msgType) {
        return msgType != null && !msgType.toLowerCase().contains("heartbeat")
                && !msgType.toLowerCase().contains("hb")
                && !msgType.toLowerCase().contains("login");
    }

    public static boolean checkReqType(String msgType) {
        return msgType != null && msgType.toLowerCase().contains("req");
    }

    public static boolean checkResType(String msgType) {
        return msgType != null && msgType.toLowerCase().contains("res");
    }
}
