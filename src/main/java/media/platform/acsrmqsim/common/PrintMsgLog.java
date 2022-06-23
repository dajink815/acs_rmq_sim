package media.platform.acsrmqsim.common;

import media.platform.qos.manager.RmqTrafficManager;
import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.json.JsonPretty;
import media.platform.acsrmqsim.rmq.module.RmqParser;
import media.platform.acsrmqsim.rmq.types.RmqHeader;
import media.platform.acsrmqsim.rmq.types.RmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class PrintMsgLog {
    static final Logger log = LoggerFactory.getLogger(PrintMsgLog.class);
    private static final AppInstance instance = AppInstance.getInstance();

    private PrintMsgLog() {
        // Do Nothing
    }

    public static boolean printLog(RmqMessage msg, String json, boolean isSend, String targetQueue) {
        String msgType = msg.getMsgType();

        if (MsgTypeChecker.checkLogType(msgType)) {

            // Rmq Traffic Manager
            RmqHeader header = msg.getHeader();
            RmqTrafficManager rmqTrafficManager = RmqTrafficManager.getInstance();

            if (isSend)
                rmqTrafficManager.rmqSendTimeCheck(targetQueue, header.getTransactionId(), msgType);
            else
                rmqTrafficManager.rmqRecvTimeCheck(header.getMsgFrom(), header.getTransactionId(), msgType);

            int reasonCode = header.getReasonCode();
            String queue = isSend? targetQueue : header.getMsgFrom();
            String taskId = RmqParser.parsingTaskId(msg.getBody());
            String callId = RmqParser.parsingCallId(msg.getBody());

            String logMsg = isSend? "Send" : "Recv";
            String dir = isSend? "-->" : "<--";

            log.info("[{}] ({}) ({}) () {} [{}] [{}] {} [{}]", instance.getServiceType(),
                    taskId, callId, logMsg, msgType, reasonCode, dir, queue);

            instance.setLastMsgTime();
            log.debug("[{}] {} {} [{}] \n'{}'", instance.getServiceType(),
                    logMsg, dir, queue, JsonPretty.getFormatJson(json));

            return true;
        } else {
            return false;
        }

    }
}
