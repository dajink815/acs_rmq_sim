package media.platform.acsrmqsim.rmq.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import media.platform.acsrmqsim.common.MsgTypeChecker;
import media.platform.acsrmqsim.common.PrintMsgLog;
import media.platform.acsrmqsim.json.JsonParse;
import media.platform.acsrmqsim.model.info.SessionInfo;
import media.platform.acsrmqsim.model.manager.SessionManager;
import media.platform.acsrmqsim.scenario.DirType;
import media.platform.acsrmqsim.scenario.ScenarioProc;
import media.platform.acsrmqsim.service.HeartbeatService;
import media.platform.acsrmqsim.tracing.TraceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import media.platform.acsrmqsim.common.SleepUtil;
import media.platform.acsrmqsim.rmq.types.RmqMessage;
import media.platform.acsrmqsim.scenario.ScenarioManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * RmqReceiver 가 BlockingQueue 에 넣어놓은 메시지 처리
 * */
public class RmqConsumer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RmqConsumer.class);

    private final ScenarioManager scenarioManager = ScenarioManager.getInstance();
    private final JsonParse jsonParse = JsonParse.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final ScenarioProc scenarioProc = ScenarioProc.getInstance();

    private final BlockingQueue<String> queue;
    private boolean isQuit = false;

    public RmqConsumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        queueProcessing();
    }

    private void queueProcessing(){
        while (!isQuit){
            try {
                String msg = queue.poll(10, TimeUnit.MILLISECONDS);

                if (msg == null){
                    SleepUtil.trySleep(10);
                    continue;
                }
                parseRmqMsg(msg);

            }catch (InterruptedException e){
                log.error("RmqConsumer.queueProcessing",e);
                isQuit = true;
                Thread.currentThread().interrupt();
            }
        }
    }

    // JSON -> RmqMessage
    private void parseRmqMsg(String json){
        RmqParsing rmqParsing = new RmqParsing(json).invoke();
        if (!rmqParsing.isParsed()) return;

        // Parse RmqMessage
        RmqMessage rmqMessage = rmqParsing.getMsg();
        msgProcessing(rmqMessage, json);
    }

    private void msgProcessing(RmqMessage rmqMsg, String json){
        String msgType = rmqMsg.getMsgType();
        int scenarioIdx = scenarioManager.getIdxByMsgType(msgType);

        // 시나리오에 존재하지 않는 메시지는 처리불가
        if (scenarioIdx < 0) {
            if (MsgTypeChecker.checkLogType(msgType)) {
                log.warn("INVALID MSG RECV [{}] <-- [{}]", msgType, rmqMsg.getHeader().getMsgFrom());
            } else {
                HeartbeatService heartbeatService = HeartbeatService.getInstance();
                heartbeatService.sendResMsg(msgType, json);
            }
            return;
        }

        PrintMsgLog.printLog(rmqMsg, json, false, null);

        String sessionId = jsonParse.keyWordParse(json);

        //SessionInfo sessionInfo = sessionManager.getSessionInfo(sessionId);
        SessionInfo sessionInfo = sessionManager.getSessionInfo(jsonParse.keyWordListParse(json));

        if (sessionInfo != null) {
            /** JaegerTrace */
            //sessionInfo.procSpan(msgType, DirType.RECV, null);
            //TraceManager.startServerSpan(sessionInfo.getTracer(), "", "Recv_" + msgType, sessionInfo);

            int callIdx = sessionInfo.getMsgIndex() + 1;
            log.debug("RmqConsumer SessionInfo Index: {}", callIdx);

            // 시나리오의 다른 메시지를 먼저 수신했을 경우
            if (scenarioIdx != callIdx) {
                // 타입이 같은 메시지가 시나리오 내에 또 존재할때, SessionInfo Index 대로 진행
                int lastScenarioIdx = scenarioManager.getLastIdxByMsgType(msgType);
                if (scenarioIdx != lastScenarioIdx) {
                    scenarioIdx = callIdx;
                } else {
                    // 먼저 온 메시지 먼저 처리
                    log.warn("RmqConsumer Check order of the Scenario (SessionInfo Idx: {}, Scenario Idx: {})", callIdx, scenarioIdx);
                }
            }

        } else {
            log.debug("RmqConsumer Scenario Index: {}", scenarioIdx);
        }

        // todo reasonCode 비교?
        // 수신 메시지 처리
        scenarioProc.procRecv(scenarioIdx, json, sessionInfo);
    }

    public static class RmqParsing {
        /**
         * RmqParser 이용해서
         * json  -> RmqMessage class 변환 후, 결과 저장
         * */
        private boolean parseResult;
        private final String json;
        private RmqMessage msg = null;

        public RmqParsing(String json){
            this.json = json;
        }

        public boolean isParsed(){
            return parseResult;
        }

        public RmqMessage getMsg(){
            return msg;
        }

        public RmqParsing invoke(){
            try {
                msg = RmqParser.parse(json);
                if (msg == null){
                    parseResult = false;
                    return this;
                }
            } catch (Exception e) {
                log.error("RmqParsing invoke Error ", e);
            }
            parseResult = true;
            return this;
        }
    }

}
