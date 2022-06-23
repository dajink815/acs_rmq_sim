package media.platform.acsrmqsim.service;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.common.CalendarUtil;
import media.platform.acsrmqsim.common.MsgTypeChecker;
import media.platform.acsrmqsim.common.ServiceType;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.json.*;
import media.platform.acsrmqsim.rmq.RmqManager;
import media.platform.acsrmqsim.rmq.module.RmqClient;
import media.platform.acsrmqsim.scenario.ScenarioParser;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.concurrent.*;

import static media.platform.acsrmqsim.json.JsonFile.*;

/** Heartbeat 메시지를 주기적으로 전송 */
public class HeartbeatService {
    private static final Logger log = LoggerFactory.getLogger(HeartbeatService.class);

    private static HeartbeatService heartbeatService = null;
    private ScheduledExecutorService scheduleService;
    private final HeartbeatRunnable heartbeatRunnable = new HeartbeatRunnable();

    private final AppInstance instance = AppInstance.getInstance();
    private final Config config = instance.getConfig();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();
    private final JsonModifier modifier = JsonModifier.getInstance();

    private HeartbeatService() {
        // Do Nothing
    }

    public static HeartbeatService getInstance() {
        if (heartbeatService == null){
            heartbeatService = new HeartbeatService();
        }
        return heartbeatService;
    }

    public void start(){
        if (!instance.isEqualService(ServiceType.A2S) && scheduleService == null) {
            log.warn("START HeartbeatService");

            ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                    .namingPattern("HeartbeatService-%d")
                    .daemon(true)
                    .priority(Thread.MAX_PRIORITY)
                    .build();

            scheduleService = Executors.newScheduledThreadPool(1, threadFactory);
            this.scheduleService.scheduleAtFixedRate(this.heartbeatRunnable, 1000 - CalendarUtil.getMilliSecond(), 1000, TimeUnit.MILLISECONDS);
        }
    }

    public void stop(){
        if (scheduleService != null) {
            log.warn("STOP HeartbeatService");
            scheduleService.shutdown();
        }
    }

    /** Heartbeat & Login Req 전송 */
    public void checkAndSendHeartBeat(){
        // 1. login_req
        if (instance.isEqualService(ServiceType.AMF)
                && !instance.isAmfLogin() && sendReqMsg(LOGIN_REQ.getFile())){
            return;
        }

        // 2. heartbeat_req / HB_REQ
        String jsonFile = instance.isEqualService(ServiceType.AMF)? HEARTBEAT_REQ.getFile() : HB_REQ.getFile();
        sendReqMsg(jsonFile);
    }
    private boolean sendReqMsg(String jsonFile) {
        boolean result = false;

        String jsonPath = config.getJsonFilePath() + File.separator + jsonFile;
        String json = ScenarioParser.file2JsonObj(jsonPath).toJSONString();

        //timestamp, Tid, msgFrom 세팅
        json = jsonMapper.jsonHeaderMapping(null, json);
        if (json != null) {
            // set amfId
            if (instance.isEqualService(ServiceType.AMF)) {
                json = modifier.bodyModifying(json, JsonEnum.AMF_ID.getValue(), config.getRmqAmfId());
            }

            // send msg
            RmqClient a2sClient = RmqManager.getInstance().getRmqClient(config.getRmqA2S());
            a2sClient.send(json);
            result = true;
        } else {
            log.error("HeartbeatService.sendReqMsg {} Json is Null", jsonFile);
        }

        return result;
    }

    /** Heartbeat & Login Res 전송 */
    public void sendResMsg(String msgType, String prevJson) {
        if (!instance.isEqualService(ServiceType.A2S) || !MsgTypeChecker.checkReqType(msgType)) return;

        String jsonFile;
        if (msgType.toLowerCase().contains("heartbeat"))
            jsonFile = HEARTBEAT_RES.getFile();
        else if (msgType.toLowerCase().contains("hb"))
            jsonFile = HB_RES.getFile();
        else if (msgType.toLowerCase().contains("login"))
            jsonFile = LOGIN_RES.getFile();
        else
            return;

        String jsonPath = config.getJsonFilePath() + File.separator + jsonFile;
        String json = ScenarioParser.file2JsonObj(jsonPath).toJSONString();

        //timestamp, Tid, msgFrom 세팅
        json = jsonMapper.jsonHeaderMapping(prevJson, json);

        JsonParse jsonParse = JsonParse.getInstance();
        String target = jsonParse.headerParse(prevJson, HeaderField.MSG_FROM.getValue());

        RmqClient rmqClient = null;
        if (target != null) {
            rmqClient = RmqManager.getInstance().getRmqClient(target);
        }
        if (json != null && rmqClient != null) rmqClient.send(json);
        else log.error("HeartbeatService.sendResMsg {} Json/RmqClient is Null", jsonFile);
    }

    static class HeartbeatRunnable implements Runnable {
        @Override
        public void run() {
            //log.debug("HeartbeatService HeartbeatRunnable");
            HeartbeatService heartbeatService = HeartbeatService.getInstance();
            heartbeatService.checkAndSendHeartBeat();
        }
    }
}
