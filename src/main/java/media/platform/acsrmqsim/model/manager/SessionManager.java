package media.platform.acsrmqsim.model.manager;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.model.info.SessionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dajin kim
 */
public class SessionManager {
    static final Logger log = LoggerFactory.getLogger(SessionManager.class);
    private static final ConcurrentHashMap<String, SessionInfo> sessionInfoMap = new ConcurrentHashMap<>();
    private static SessionManager sessionManager = null;
    private final Config config = AppInstance.getInstance().getConfig();

    public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public SessionInfo createSessionInfo(String sessionId) {
        if (sessionId == null) return null;

        int callSize = config.getCallNum();
        if (sessionInfoMap.containsKey(sessionId)) {
            log.error("SessionInfo [{}] already exist", sessionId);
            return null;
        } else if (sessionInfoMap.size() > callSize) {
            log.error("SessionInfoHashMap Size is Full, CurrentSize [{}], MaxSize [{}]", sessionInfoMap.size(), callSize);
            return null;
        }

        SessionInfo sessionInfo = new SessionInfo(sessionId);
        sessionInfo.setCreateTime(System.currentTimeMillis());
        sessionInfo.setMsgIndex(0);
        sessionInfoMap.put(sessionId, sessionInfo);
        log.warn("() () () SessionInfo [{}] is created", sessionId);
        return sessionInfo;
    }

    public SessionInfo getSessionInfo(String sessionId) {
        if(sessionId == null) return null;
        return sessionInfoMap.get(sessionId);
    }

    public SessionInfo getSessionInfo(List<String> keyWordList) {
        //log.debug("SessionManager.getSessionInfo by keyWordList : {}", keyWordList);
        for (String keyWord : keyWordList) {
            SessionInfo sessionInfo = getSessionInfo(keyWord);
            if (sessionInfo == null) continue;
            //log.debug("Found SessionInfo");
            return sessionInfo;
        }
        return null;
    }

    public ConcurrentMap<String, SessionInfo> getSessionInfoMap() {
        return sessionInfoMap;
    }

    public void clearSessionInfoMap() {
        sessionInfoMap.clear();
    }

    public int getSessionInfoSize() {
        return sessionInfoMap.size();
    }

    public List<String> getSessionIds() {
        synchronized (sessionInfoMap){
            return new ArrayList<>(sessionInfoMap.keySet());
        }
    }

    private void finishTrace(String sessionId) {
        SessionInfo sessionInfo = getSessionInfo(sessionId);
        if (sessionInfo != null) {
            log.debug("({}) finishTrace SessionInfo", sessionId);
            //sessionInfo.finishRootSpan();
        } else {
            log.debug("({}) finishTrace SessionInfo is Null", sessionId);

        }
    }

    public void deleteSessionInfo(String sessionId) {
        if (sessionId == null) return;
        SessionInfo deleteSession = sessionInfoMap.remove(sessionId);
        if (deleteSession != null) {
            //finishTrace(sessionId);
            //deleteSession.finishRootSpan();
            log.warn("() () () SessionInfo [{}] is removed", sessionId);
        }
    }
}
