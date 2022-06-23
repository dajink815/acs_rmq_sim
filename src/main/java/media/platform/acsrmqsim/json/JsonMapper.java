package media.platform.acsrmqsim.json;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.common.DateFormatUtil;
import media.platform.acsrmqsim.common.MsgTypeChecker;
import media.platform.acsrmqsim.model.info.SessionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

/**
 * @author dajin kim
 */
public class JsonMapper {
    private static final Logger log = LoggerFactory.getLogger(JsonMapper.class);
    private static JsonMapper jsonMapper = null;

    private final AppInstance instance = AppInstance.getInstance();
    private final JsonParse jsonParse = JsonParse.getInstance();
    private final JsonModifier jsonModifier = JsonModifier.getInstance();

    private JsonMapper() {
        // Do Nothing
    }

    public static JsonMapper getInstance() {
        if (jsonMapper == null)
            jsonMapper = new JsonMapper();
        return jsonMapper;
    }

    public void setBodyValue(String recvScenario, String recvMsg, SessionInfo sessionInfo) {
        if (sessionInfo == null) return;

        for (String bodyKey : instance.getBodyList()) {
            // 시나리오에 키워드 존재할 때만 값 저장
            String scenarioValue = jsonParse.bodyParse(recvScenario, bodyKey);
            if (scenarioValue == null) continue;
            if (scenarioValue.isEmpty()) scenarioValue = bodyKey;

            String bodyValue = jsonParse.bodyParse(recvMsg, bodyKey);
            if (bodyValue != null) {
                sessionInfo.addValue(scenarioValue, bodyValue);
            }
/*            log.debug("setBodyValue - key:{}, scenarioValue:{}, bodyValue: {} (bodyMap:{})",
                    bodyKey, scenarioValue, bodyValue, sessionInfo.getBodyMap());*/

        }
    }

    public String jsonMsgMapping(String prevMsg, String sendMsg, SessionInfo sessionInfo) {
        // header
        sendMsg = jsonHeaderMapping(prevMsg, sendMsg);
        // body
        sendMsg = jsonBodyMapping(prevMsg, sendMsg, sessionInfo);

        return sendMsg;
    }

    public String jsonHeaderMapping(String prevMsg, String sendMsg) {
        // header
        for (HeaderField headerKey : instance.getHeaderList()) {
            sendMsg = jsonHeaderProc(prevMsg, sendMsg, headerKey);
        }
        return sendMsg;
    }

    private String jsonBodyMapping(String prevMsg, String sendMsg, SessionInfo sessionInfo) {
        for (String bodyKey : instance.getBodyList()) {
            sendMsg = jsonBodyProc(prevMsg, sendMsg, bodyKey, sessionInfo);
            if (sendMsg == null) return null;
        }
        return sendMsg;
    }

    private String jsonHeaderProc(String prevMsg, String sendMsg, HeaderField headerKey) {
        String value = null;
        switch (headerKey) {
            case TRANSACTION_ID:
                value = MsgTypeChecker.checkReqType(sendMsg)?
                        UUID.randomUUID().toString() : jsonParse.headerParse(prevMsg, headerKey.getValue());
                break;
            case TIMESTAMP:
                value = DateFormatUtil.formatYmdHmsS(System.currentTimeMillis());
                break;
            case MSG_FROM:
                value = AppInstance.getInstance().getConfig().getRmqServerQueue();
                break;
            default:
                log.trace("JsonMapper.jsonHeaderProc - {} case not defined", headerKey);
                break;
        }
        if (value != null) {
            sendMsg = jsonModifier.headerModifying(sendMsg, headerKey.getValue(), value);
        }

        return sendMsg;
    }

    private String jsonBodyProc(String prevMsg, String sendMsg, String bodyKey, SessionInfo sessionInfo) {
        // 1. sendMsg body 에 bodyKey 없으면 skip
        String sendBodyValue = jsonParse.bodyParse(sendMsg, bodyKey);
        if (sendBodyValue == null) return sendMsg;

        String setValue;
        if (!sendBodyValue.isEmpty()) {
            // 2. sendMsg bodyKey "new" 일때 새로운 값 생성
            if (sendBodyValue.equalsIgnoreCase("new")) {
                setValue = UUID.randomUUID().toString();
                log.debug("Create {}: {}", bodyKey, setValue);

            } else {
                // 3. keyWord 가 SessionInfo 에 저장돼있을 경우, 저장된 값 호출
                setValue = getBodyValue(sendBodyValue, sessionInfo);
                if (setValue != null) {
                    log.debug("getValue From SessionInfo {}: {} (BodyKey:{})", sendBodyValue, setValue, bodyKey);
                }
            }

            if (setValue != null) {
                sendMsg = setBodyValue(sendMsg, bodyKey, setValue);
            }

            // 4. 저장된 keyWord 값이 없을 경우 그대로 전송

        }
        // 5. sendMsg bodyKey 존재하지만 ""(공란)일때, 이전 메시지에서 복사
        else {
            // prevMsg.bodyKey ---(copy)---> sendMsg.bodyKey
            String prevBodyValue = jsonParse.bodyParse(prevMsg, bodyKey);
            if (prevBodyValue != null && !prevBodyValue.isEmpty()) {
                log.debug("Copy From PrevMsg {}: {}", bodyKey, prevBodyValue);
                sendMsg = setBodyValue(sendMsg, bodyKey, prevBodyValue);
            }
            // 이전 메시지에 복사할 값이 없을 경우 시나리오 오류, 해당 호 에러로 종료
            else {
                log.warn("CheckScenario - Not Exist PrevMsg, Need to Add 'new' keyWord [{}:{}] \n'{}'", bodyKey, sendBodyValue, sendMsg);
                return null;
            }
        }

        // sendMsg bodyKey 값 new 외의 다른 문구 있을시, 사용자가 입력한 문구 그대로 전송
        return sendMsg;
    }

    private String getBodyValue(String sendBodyValue, SessionInfo sessionInfo) {
        if (sessionInfo != null) return sessionInfo.getValue(sendBodyValue);
        return  null;
    }

    private String setBodyValue(String sendMsg, String bodyKey, String bodyValue) {
        if (bodyValue != null) {
            try {
                // Integer Value
                int intValue = Integer.parseInt(bodyValue);
                sendMsg = jsonModifier.bodyModifying(sendMsg, bodyKey, intValue);
            } catch (NumberFormatException e) {
                // String Value
                log.trace("JsonMapper.jsonBodyProc.ParseInt [{}]", bodyValue);
                sendMsg = jsonModifier.bodyModifying(sendMsg, bodyKey, bodyValue);
            }

        }
        return sendMsg;
    }

}

