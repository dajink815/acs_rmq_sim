package media.platform.acsrmqsim.config;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.common.ServiceType;
import media.platform.acsrmqsim.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class Config extends DefaultConfig {
    private final Logger log = LoggerFactory.getLogger(Config.class);

    // COMMON
    private String xmlPath;
    private int callNum;
    private int testTimeout;
    private int scenarioThreadSize;
    private int callRate;

    // RMQ
    private String rmqHost;        //A2S, AMF 큐 접속 정보
    private String rmqUser;
    private int rmqPort;
    private String rmqPass;

    private String awfRmqHost;        //AWF 큐 접속 정보
    private String awfRmqUser;
    private int awfRmqPort;
    private String awfRmqPass;

    private String rmqA2S;         //큐 이름
    private String rmqAmf;
    private String rmqAwf;

    private int rmqAmfId;

    private int rmqQueueSize;
    private int rmqThreadSize;

    private String rmqClientQueue;
    private String rmqServerQueue;

    // JSON
    private String scenarioPath;
    private String jsonFilePath;

    // UDP
    private int serverPort;

    public Config(String configPath){
        boolean result = load(configPath);
        log.info("Load config ... [{}]",  StringUtil.getOkFail(result));

        if (result){
            loadCommonConfig();
            loadRmqConfig();
            loadUdpConfig();
            loadJsonConfig();
        }
    }

    public void loadCommonConfig() {
        this.xmlPath = getStrValue(Section.COMMON, "XML_PATH", "/home/ca2ssim/ca2ssim/xml/jsonField.xml");
        this.callNum = getIntValue(Section.COMMON, "CALL_NUM", 1);
        this.testTimeout = getIntValue(Section.COMMON, "TEST_TIMEOUT", 30);
        this.scenarioThreadSize = getIntValue(Section.COMMON, "SCENARIO_THREAD_SIZE", 10);
        this.callRate = getIntValue(Section.COMMON, "CALL_RATE", 1);
    }

    public void loadRmqConfig(){

        this.rmqHost = getStrValue(Section.RMQ, "HOST", "127.0.0.1");
        this.rmqUser = getStrValue(Section.RMQ, "USER", "admin");
        this.rmqPort = getIntValue(Section.RMQ, "PORT", 5672);
        this.rmqPass = getStrValue(Section.RMQ, "PASS", null);

        this.awfRmqHost = getStrValue(Section.RMQ, "AWF_HOST", "127.0.0.1");
        this.awfRmqUser = getStrValue(Section.RMQ, "AWF_USER", "admin");
        this.awfRmqPort = getIntValue(Section.RMQ, "AWF_PORT", 5672);
        this.awfRmqPass = getStrValue(Section.RMQ, "AWF_PASS", null);

        this.rmqA2S = getStrValue(Section.RMQ, "A2S", "ACS_A2S");
        this.rmqAmf = getStrValue(Section.RMQ, "AMF", "ACS_AMF");
        this.rmqAwf = getStrValue(Section.RMQ, "AWF", "ACS_AWF");

        this.rmqAmfId = getIntValue(Section.RMQ, "AMF_ID", 0);
        this.rmqAmf = this.rmqAmf + "_" + this.rmqAmfId;

        this.rmqThreadSize = getIntValue(Section.RMQ, "THREAD_SIZE", 10);
        this.rmqQueueSize = getIntValue(Section.RMQ, "QUEUE_SIZE", 10);
    }

    public void loadJsonConfig() {
        this.scenarioPath = getStrValue(Section.JSON, "SCENARIO_PATH", "/home/ca2ssim/ca2ssim/scenario");
        this.jsonFilePath = getStrValue(Section.JSON, "JSON_FILE_PATH", "/home/ca2ssim/ca2ssim/rmqsim_json");
    }
    public void loadUdpConfig(){
        this.serverPort = getIntValue(Section.UDP, "SERVER_PORT", 5060);
    }

    /** 서비스 타입에 따라 값이 달라져서 서비스 타입 세팅 후 로딩해야하는 데이터
     * RMQ 큐 세팅 (Sever & Client Queue) */
    public void settingConfig(){
        ServiceType serviceType = AppInstance.getInstance().getServiceType();

        switch(serviceType){

            //AMF 역할
            case AMF:
                //보내는 큐 : ACS_A2S
                setRmqClientQueue(getRmqA2S());
                //받는 큐 : ACS_AMF_#
                setRmqServerQueue(getRmqAmf());
                break;

            //A2S 역할
            case A2S:
                //보내는 큐 : ACS_AMF_#
                setRmqClientQueue(getRmqAmf());
                //받는 큐 : ACS_A2S
                setRmqServerQueue(getRmqA2S());
                break;

            //AWF 역할
            case AWF:
                //보내는 큐 : ACS_AMF_#
                setRmqClientQueue(getRmqAmf());
                //받는 큐 : ACS_AWF
                setRmqServerQueue(getRmqAwf());
                break;

            default:
                log.error("Config.settingQueue Error - Invalid Service Type");
                break;
        }
    }


    public String getXmlPath() {
        return xmlPath;
    }

    public int getCallNum() {
        return callNum;
    }

    public int getTestTimeout() {
        return testTimeout;
    }

    public int getScenarioThreadSize() {
        return scenarioThreadSize;
    }

    public int getCallRate() {
        return callRate;
    }

    public String getRmqHost() {
        return rmqHost;
    }

    public String getRmqUser() {
        return rmqUser;
    }

    public int getRmqPort() {
        return rmqPort;
    }

    public String getRmqPass() {
        return rmqPass;
    }

    public String getAwfRmqHost() {
        return awfRmqHost;
    }

    public String getAwfRmqUser() {
        return awfRmqUser;
    }

    public int getAwfRmqPort() {
        return awfRmqPort;
    }

    public String getAwfRmqPass() {
        return awfRmqPass;
    }

    public String getRmqA2S() {
        return rmqA2S;
    }

    public String getRmqAmf() {
        return rmqAmf;
    }

    public String getRmqAwf() {
        return rmqAwf;
    }

    public int getRmqAmfId() {
        return rmqAmfId;
    }

    public int getRmqQueueSize() {
        return rmqQueueSize;
    }

    public int getRmqThreadSize() {
        return rmqThreadSize;
    }


    public String getRmqClientQueue() {
        return rmqClientQueue;
    }

    public void setRmqClientQueue(String rmqClientQueue) {
        this.rmqClientQueue = rmqClientQueue;
    }

    public String getRmqServerQueue() {
        return rmqServerQueue;
    }

    public void setRmqServerQueue(String rmqServerQueue) {
        this.rmqServerQueue = rmqServerQueue;
    }

    public String getScenarioPath() {
        return scenarioPath;
    }

    public String getJsonFilePath() {
        return jsonFilePath;
    }

    public int getServerPort() {
        return serverPort;
    }
}
