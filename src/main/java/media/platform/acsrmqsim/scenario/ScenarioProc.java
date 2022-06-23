package media.platform.acsrmqsim.scenario;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.common.CalendarUtil;
import media.platform.acsrmqsim.common.MsgTypeChecker;
import media.platform.acsrmqsim.common.ServiceType;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.json.*;
import media.platform.acsrmqsim.model.info.SessionInfo;
import media.platform.acsrmqsim.model.manager.SessionManager;
import media.platform.acsrmqsim.rmq.RmqManager;
import media.platform.acsrmqsim.rmq.module.RmqClient;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author dajin kim
 */
public class ScenarioProc {
    static final Logger log = LoggerFactory.getLogger(ScenarioProc.class);

    private static ScenarioProc scenarioProc = null;
    private ScheduledExecutorService scheduleService;

    private final AppInstance instance = AppInstance.getInstance();
    private final Config config = instance.getConfig();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();
    private final JsonModifier modifier = JsonModifier.getInstance();
    private final ScenarioManager scenarioManager = ScenarioManager.getInstance();
    private final RmqManager rmqManager = RmqManager.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final JsonParse jsonParse = JsonParse.getInstance();

    public ScenarioProc() {
        // Do Nothing
    }

    public static ScenarioProc getInstance() {
        if (scenarioProc == null)
            scenarioProc = new ScenarioProc();
        return scenarioProc;
    }

    public void start() {
        if (scheduleService == null) {
            log.warn("START ScenarioPro");

            ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                    .namingPattern("ScenarioProc" + "-%d")
                    .daemon(true)
                    .priority(Thread.MAX_PRIORITY)
                    .build();

            scheduleService = Executors.newScheduledThreadPool(config.getScenarioThreadSize(), threadFactory);
            if (scheduleService == null)
                log.debug("scheduleService Null");
            else {
                log.debug("scheduleService Not Null");

            scheduleService.scheduleAtFixedRate(() -> {
                for (int i = 1; i <= config.getCallRate(); i++) {
                    this.newScenario();
                }
            }, 1000 - CalendarUtil.getMilliSecond(), 1000, TimeUnit.MILLISECONDS);
            }
        }
    }

    private void newScenario() {
        if (!scenarioManager.isTestValid()) return;

        ServiceType serviceType = instance.getServiceType();

        // send, pause 로 시작하는 시나리오 처리
        try {
            if (scenarioManager.isPauseType(0) || scenarioManager.isSendType(0)) {
                switch (serviceType) {
                    case A2S:
                    case AMF:
                        if (instance.isAmfLogin()) {
                            process(0, null, null);
                        }
                        break;

                    case AWF:

                        if (instance.isAwfHb()) {
                            process(0, null, null);
                        }
                        break;

                    default:

                        break;
                }
            }
        } catch (Exception e) {
            log.error("ScenarioProcess.newScenario.Exception ", e);
        }
    }

    public void stop() {
        if (scheduleService != null) {
            log.error("STOP ScenarioProcess");
            scheduleService.shutdown();
        }
    }

    private void process(int msgIdx, String prevMsg, SessionInfo sessionInfo) {
        // 시나리오 마지막 인덱스까지 진행된 메시지 체크
        if (scenarioManager.getScenarioSize() <= msgIdx) {
            scenarioManager.increaseEndCallNum();
            String sessionId = sessionInfo != null? sessionInfo.getSessionId() : jsonParse.keyWordParse(prevMsg);
            sessionManager.deleteSessionInfo(sessionId);
            log.warn("({}) End of Scenario (EndCall:{}, ConfigCall:{}, ScenarioSize:{}, Idx:{}, Session:{})",
                    sessionId, scenarioManager.getEndCallNum(), config.getCallNum(), scenarioManager.getScenarioSize(), msgIdx, sessionManager.getSessionInfoSize());
            return;
        }

        // Send, Pause 메시지 처리
        if (scenarioManager.isSendType(msgIdx)) {
            procSend(msgIdx, prevMsg, sessionInfo);
        } else if (scenarioManager.isPauseType(msgIdx)) {
            procPause(msgIdx, prevMsg, sessionInfo);
        }
    }


    private void procPause(int msgIdx, String prevMsg, SessionInfo sessionInfo) {
        // 시나리오 첫번째 인덱스 체크
        if (!checkCallNum(msgIdx)) return;
        int pauseTime = scenarioManager.getPauseTime(msgIdx);
        log.debug("ScenarioProcess [{}] Pause ({} Sec)", msgIdx, pauseTime);

        // 다음 시나리오 메시지 처리
        if (this.scheduleService == null) {
            log.error("scheduleService is Null");
            ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                    .namingPattern("ScenarioProc2" + "-%d")
                    .daemon(true)
                    .priority(Thread.MAX_PRIORITY)
                    .build();
            scheduleService = Executors.newScheduledThreadPool(config.getScenarioThreadSize(), threadFactory);
        }

        this.scheduleService.schedule(() -> process(msgIdx + 1, prevMsg, sessionInfo), pauseTime, TimeUnit.SECONDS);
    }

    public void procRecv(int msgIdx, String recvMsg, SessionInfo sessionInfo) {
        // 시나리오 첫번째 인덱스 체크
        if (!checkCallNum(msgIdx)) return;
        log.debug("ScenarioProcess [{}:{}] Recv", msgIdx, scenarioManager.getType(msgIdx));

        sessionInfo = setMsgIndex(recvMsg, sessionInfo);

        if (sessionInfo != null) {
            sessionInfo.setMsgIndex(msgIdx);
            String recvScenario = scenarioManager.getScenario(msgIdx).toJSONString();
            jsonMapper.setBodyValue(recvScenario, recvMsg, sessionInfo);

            /** JaegerTrace */
            String msgType = JsonParse.getInstance().headerParse(recvMsg, "type");
            //sessionInfo.finishSpan(msgType);
        }

        // 다음 시나리오 메시지 처리
        process(msgIdx + 1, recvMsg, sessionInfo);
    }

    private void procSend(int msgIdx, String prevMsg, SessionInfo sessionInfo) {
        // 시나리오 첫번째 인덱스 체크
        if (!checkCallNum(msgIdx)) return;
        log.debug("ScenarioProcess [{}:{}] Send", msgIdx, scenarioManager.getType(msgIdx));

        JSONObject jsonObject = scenarioManager.getScenario(msgIdx);

        if (jsonObject == null) {
            log.warn("ScenarioProcess.procSend {}-Scenario is Null", msgIdx);
            return;
        }

        // 'direction' field 삭제 전 target 먼저 파싱
        String target = getSendQueue(msgIdx, prevMsg);
        String sendMsg = modifier.delDirectionField(jsonObject.toJSONString());

        /** JaegerTrace */
        String prevMsgType = JsonParse.getInstance().headerParse(prevMsg, "type");
        String msgType = JsonParse.getInstance().headerParse(sendMsg, "type");

        // 새로운 keyWord Value 생성
        sendMsg = jsonMapper.jsonMsgMapping(prevMsg, sendMsg, sessionInfo);

        SessionInfo prevInfo = sessionInfo;
        sessionInfo = setMsgIndex(sendMsg, sessionInfo);

        if (sessionInfo != null) {
            /** JaegerTrace */
            //sessionInfo.procSpan(msgType, DirType.SEND, prevMsgType);

            sessionInfo.setMsgIndex(msgIdx);
            // 메시지 수신시 SessionInfo 가 존재하지 않아 송신하면서 생성한 경우
            // 송신 전 수신받았던 메시지 중 body KeyWord 값 저장
            int prevRecvIdx = scenarioManager.getRecvIdx(msgIdx);
            if (prevInfo == null && prevRecvIdx != -1) {
                String recvScenario = scenarioManager.getScenario(prevRecvIdx).toJSONString();
                jsonMapper.setBodyValue(recvScenario, prevMsg, sessionInfo);

                // 위에서 저장한 keyWord 다시 세팅
                sendMsg = jsonMapper.jsonMsgMapping(prevMsg, sendMsg, sessionInfo);
            }
        }

        if (sendMsg == null) {
            scenarioManager.increaseEndCallNum();
            log.warn("ScenarioProcess.procSend - Fail JsonMsgMapping (EndCall:{}, ConfigCall:{})",
                    scenarioManager.getEndCallNum(), config.getCallNum());
            return;
        }

        // rmqClient 이용해 메시지 전송
        RmqClient rmqClient = rmqManager.getRmqClient(target);
        rmqClient.send(sendMsg);

        /** JaegerTrace */
        //if (sessionInfo != null) sessionInfo.finishSpan(msgType);

        // 다음 시나리오 메시지 처리
        process(msgIdx + 1, sendMsg, sessionInfo);
    }

    private String getSendQueue(int idx, String prevMsg) {
        // 현재 idx 보내려는 메시지 타입이 Res 라면 이전 Req 메시지에서 msgFrom 파싱
        if (prevMsg != null) {
            String msgType = scenarioManager.getType(idx);
            String prevMsgType = jsonParse.headerParse(prevMsg, HeaderField.TYPE.getValue());

            if (MsgTypeChecker.checkResType(msgType) &&
                    MsgTypeChecker.checkReqType(prevMsgType)) {
                //log.debug("get targetQueue from prevMsg msg_from");
                return jsonParse.headerParse(prevMsg, HeaderField.MSG_FROM.getValue());
            }
        }

        // 시나리오 파일 direction 설정값 통해 getTargetQueue
        String sendModule  = scenarioManager.getSendModule(idx);
        if (sendModule != null) {
            sendModule = sendModule.toLowerCase();

            if (sendModule.contains("a2s")) {
                return config.getRmqA2S();
            } else if (sendModule.contains("amf")) {
                return config.getRmqAmf();
            } else if (sendModule.contains("awf")) {
                return config.getRmqAwf();
            }
        }
        return config.getRmqClientQueue();
    }

    private SessionInfo setMsgIndex(String json, SessionInfo sessionInfo) {
        String sessionId = jsonParse.keyWordParse(json);

        List<String> keyWordList = jsonParse.keyWordListParse(json);
        //log.debug("ScenarioProc setMsgIndex: sessionId [{}], keyWordList{}", sessionId, keyWordList);

        if (sessionId == null) {
            for (String keyWord : keyWordList) {
                if (keyWord == null) continue;
                sessionId = keyWord;
                log.debug("ScenarioProc setMsgIndex: sessionId is Null -> set [{}]", sessionId);
            }
        }

        if (sessionInfo == null) {
            sessionInfo = sessionManager.getSessionInfo(keyWordList);
        }

        if (sessionInfo == null) {
            sessionInfo = sessionManager.createSessionInfo(sessionId);
        }

        return sessionInfo;
    }

    private boolean checkCallNum(int msgIdx) {
        // 시나리오 첫번째 메시지를 처리하는 경우 totalCallNum 확인 후 CallNum 중가
        boolean result = true;
        //log.debug("checkCallNum index:{}, callNum:{}, configCallNum:{}", msgIdx, scenarioManager.getTestCallNum(), scenarioManager.getConfigCallNum());
        if (msgIdx == 0) {
            if (scenarioManager.isTestValid()) {
                scenarioManager.increaseCallNum();
            } else {
                result = false;
            }
            // todo log level?
            log.warn("SCENARIO START RESULT [{}], (CallNum: {}, ConfigCall: {})",
                    result, scenarioManager.getCallNum(), config.getCallNum());
        }
        return result;
    }


}
