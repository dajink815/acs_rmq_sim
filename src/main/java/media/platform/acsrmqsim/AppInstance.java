package media.platform.acsrmqsim;

import media.platform.acsrmqsim.common.ServiceType;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.json.HeaderField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author dajin kim
 */
public class AppInstance {
    private static final Logger log = LoggerFactory.getLogger(AppInstance.class);
    private static AppInstance instance = null;

    // Config
    private String configPath = null;
    private Config config = null;

    // Service
    private ServiceType serviceType;

    // RMQ
    private BlockingQueue<String> rmqQueue;
    private long lastMsgTime;
    private boolean amfLogin;
    private boolean awfHb;

    // Xml
    private List<HeaderField> headerList;
    private List<String> bodyList;
    private String keyWord;
    private List<String> keyWordList = new ArrayList<>();

    private AppInstance() {
    }

    public static AppInstance getInstance() {
        if (instance == null)
            instance = new AppInstance();
        return instance;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public boolean setServiceType(ServiceType serviceType){
        if (this.serviceType != null && !this.serviceType.equals(serviceType)) {
            log.error("AppInstance.setServiceType Error - Invalid Service Type");
            return false;
        }

        this.serviceType = serviceType;
        log.warn("ACS SIMULATOR [{}] MODE", serviceType);
        return true;
    }

    /** 현재 서비스 타입과 비교 결과 리턴
     * @param serviceType : 비교하고자 하는 서비스타입
     * @return boolean : 서비스 타입 비교 결과
     * */
    public boolean isEqualService(ServiceType serviceType){
        return this.serviceType != null && this.serviceType.equals(serviceType);
    }

    public BlockingQueue<String> getRmqQueue() {
        return rmqQueue;
    }
    public void setRmqQueue(BlockingQueue<String> rmqQueue) {
        this.rmqQueue = rmqQueue;
    }

    public long getLastMsgTime() {
        return lastMsgTime;
    }
    public void setLastMsgTime() {
        this.lastMsgTime = System.currentTimeMillis();
    }

    public boolean isAmfLogin() {
        return amfLogin;
    }
    public void setAmfLogin(boolean amfLogin) {
        this.amfLogin = amfLogin;
    }

    public boolean isAwfHb() {
        return awfHb;
    }
    public void setAwfHb(boolean awfHb) {
        this.awfHb = awfHb;
    }

    public List<HeaderField> getHeaderList() {
        return headerList;
    }
    public void setHeaderList(List<HeaderField> headerList) {
        this.headerList = headerList;
    }

    public List<String> getBodyList() {
        return bodyList;
    }
    public void setBodyList(List<String> bodyList) {
        this.bodyList = bodyList;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setKeyWordList(List<String> keyWordList) {
        this.keyWordList = keyWordList;
    }

    public void addKeyWord(String keyWord) {
        this.keyWordList.add(keyWord);
    }

    public List<String> getKeyWordList() {
        return keyWordList;
    }
}
