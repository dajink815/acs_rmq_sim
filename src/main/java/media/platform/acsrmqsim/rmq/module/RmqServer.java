package media.platform.acsrmqsim.rmq.module;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.common.ServiceType;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.rmq.common.PasswdDecryptor;
import media.platform.acsrmqsim.rmq.transport.RmqCallback;
import media.platform.acsrmqsim.rmq.transport.RmqReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class RmqServer {
    static final Logger log = LoggerFactory.getLogger(RmqServer.class);

    private final AppInstance instance = AppInstance.getInstance();
    private final Config config = instance.getConfig();
    private final BlockingQueue<String> queue;
    private RmqReceiver receiver = null;

    public RmqServer(){
        queue = instance.getRmqQueue();
    }

    public void start(ServiceType type){
        PasswdDecryptor decryptor=new PasswdDecryptor("skt_acs", "PBEWITHMD5ANDDES");
        String pass;

        RmqReceiver rmqReceiver = null;
        String queueName = config.getRmqServerQueue();
        String host = null;
        try {
            /** 서비스 타입 구분 */
            if(type.equals(ServiceType.A2S) || type.equals(ServiceType.AMF)){
                host = config.getRmqHost();
                pass = decryptor.decrypt0(config.getRmqPass());
                rmqReceiver = new RmqReceiver(host, config.getRmqPort(), config.getRmqUser(), pass, queueName);
            }else if(type.equals(ServiceType.AWF)){
                host = config.getAwfRmqHost();
                pass = decryptor.decrypt0(config.getAwfRmqPass());
                rmqReceiver = new RmqReceiver(host, config.getAwfRmqPort(), config.getAwfRmqUser(), pass, queueName);
            }else{
                log.error("RmqServer.start ServiceType Error : {}", type);
                return;
            }
        } catch (Exception e) {
            log.error("[{}] RMQ Server Password is not available {}",  type, e.getMessage());
            return;
        }

        rmqReceiver.setCallback(new MessageCallback());

        boolean result = false;
        if (rmqReceiver.connect()) {
            setReceiver(rmqReceiver);
            result = rmqReceiver.start();
        }

        log.warn("[{}] RmqServer Start [{}:{}] ({})", type, host, queueName, result);

    }

    public void stop(){
        if (receiver != null) {
            receiver.close();
        }
    }

    public void setReceiver(RmqReceiver receiver) {
        this.receiver = receiver;
    }

    private class MessageCallback implements RmqCallback {

        @Override
        public void onReceived(String msg, Date ts) {

            // A2S & AMF Sim login flag
            if (!instance.isAmfLogin() && (msg.contains("login") || msg.contains("heartbeat"))) {
                instance.setAmfLogin(true);
            }

            // A2S & AWF Sim HB flag
            if (!instance.isAwfHb() && msg.contains("HB")) {
                instance.setAwfHb(true);
            }

            // Log, Update LastMsgTime
/*            if (!msg.contains("heartbeat") && !msg.contains("HB")) {
                instance.setLastMsgTime();
                log.debug("[{}] Recv Message : \n'{}'", instance.getServiceType(), JsonPretty.getFormatJson(msg));
            }*/

            try {
                queue.put(msg);
            } catch (InterruptedException e) {
                log.error("RmqServer.MessageCallback", e);
                Thread.currentThread().interrupt();
            }

        }
    }

}
