package media.platform.acsrmqsim.rmq.transport;

import media.platform.acsrmqsim.common.PrintMsgLog;
import media.platform.acsrmqsim.rmq.module.RmqConsumer;
import media.platform.acsrmqsim.rmq.types.RmqMessage;
import java.nio.charset.StandardCharsets;

public class RmqSender extends RmqTransport {

    public RmqSender(String host, int port, String userName, String password, String queueName) {
        super(host, port, userName, password, queueName);
    }

    public boolean send(String msg){
        // 채널 오픈 여부와 A2S 연결 여부 체크?
        if (!isOpened()){
            log.error("RMQ channel is NOT opened");
            return false;
        }

        boolean result = false;

        try{
            getChannel().basicPublish("", getQueueName(), null, msg.getBytes(StandardCharsets.UTF_8));

            RmqConsumer.RmqParsing rmqParsing = new RmqConsumer.RmqParsing(msg).invoke();
            if (rmqParsing.isParsed()) {
                RmqMessage rmqMsg = rmqParsing.getMsg();

/*                RmqData<SetTaskCall> data = new RmqData<>(SetTaskCall.class);
                SetTaskCall taskCall = data.parse(rmqMsg);

                String callId = taskCall.getCallId();
                String taskId = taskCall.getTaskId();
                if (callId == null) callId = "";
                if (taskId == null) taskId = "";*/

/*                JsonParse jsonParse = JsonParse.getInstance();
                String reasonCode = jsonParse.headerParse(msg, JsonEnum.REASON_CODE.getValue());
                String callId = jsonParse.bodyParse(msg, JsonEnum.CALL_ID.getValue());
                String taskId = jsonParse.bodyParse(msg, JsonEnum.TASK_ID.getValue());*/

                PrintMsgLog.printLog(rmqMsg, msg, true, getQueueName());

                //log.debug("[{}] Send -> [{}] \n'{}'", instance.getServiceType(), getQueueName(), JsonPretty.getFormatJson(msg));

            }

            result = true;
        }catch (Exception e){
            log.error("RmqSender.send",e);
        }
        return result;
    }

    public boolean isOpened() {
        return getChannel().isOpen();
    }
}
