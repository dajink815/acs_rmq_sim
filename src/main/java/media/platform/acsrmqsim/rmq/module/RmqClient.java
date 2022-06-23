package media.platform.acsrmqsim.rmq.module;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.common.ServiceType;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.rmq.common.PasswdDecryptor;
import media.platform.acsrmqsim.rmq.transport.RmqSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmqClient {
    static final Logger log = LoggerFactory.getLogger(RmqClient.class);

    private final Config config = AppInstance.getInstance().getConfig();
    private RmqSender sender = null;
    private final String queueName;
    private final ServiceType type;

    public RmqClient(String queueName, ServiceType type) {
        this.type = type;
        this.queueName = queueName;
    }

    public void start() {
        RmqSender rmqSender = createSender();
        if (rmqSender != null) {
            setSender(rmqSender);
        }
    }

    private RmqSender createSender(){
        PasswdDecryptor decryptor = new PasswdDecryptor("skt_acs", "PBEWITHMD5ANDDES");
        String password;

        RmqSender rmqSender = null;
        String host = null;
        try {
            /** 서비스 타입 구분 */
            if(type.equals(ServiceType.A2S)){
                host = config.getRmqHost();
                password = decryptor.decrypt0(config.getRmqPass());
                rmqSender = new RmqSender(config.getRmqHost(), config.getRmqPort() , config.getRmqUser(), password,  queueName);
            }else if(type.equals(ServiceType.AWF)){
                host = config.getAwfRmqHost();
                password = decryptor.decrypt0(config.getAwfRmqPass());
                rmqSender = new RmqSender(config.getAwfRmqHost(), config.getAwfRmqPort() , config.getAwfRmqUser(), password,  queueName);
            }else{
                log.error("RmqClient.createSender ServiceType Error : {}", type);
                return null;
            }
        }
        catch (Exception e) {
            log.error("[{}] RMQ Client Password is not available {}", type, e.getMessage());
            return null;
        }

        boolean result = true;
        if (!rmqSender.connect()) {
            rmqSender = null;
            result = false;
        }
        log.warn("[{}] RmqClient Start [{}:{}] ({})", type, host, queueName, result);
        return rmqSender;

    }

    public void closeSender(){
        if (sender != null){
            sender.close();
            sender = null;
        }
    }

    public boolean send(String msg){
        RmqSender rmqSender = getSender();
        if (rmqSender == null){
            rmqSender = createSender();
            if (rmqSender == null)
                return false;

            setSender(rmqSender);

        }

        if (!rmqSender.isOpened() && !rmqSender.connect())
            return false;

        return rmqSender.send(msg);
    }

    public RmqSender getSender() {
        return sender;
    }

    public void setSender(RmqSender sender) {
        this.sender = sender;
    }
}
