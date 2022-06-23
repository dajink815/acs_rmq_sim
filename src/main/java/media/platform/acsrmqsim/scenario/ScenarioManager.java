package media.platform.acsrmqsim.scenario;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.json.*;
import media.platform.acsrmqsim.model.manager.SessionManager;
import media.platform.acsrmqsim.service.ServiceManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dajin kim
 */
public class ScenarioManager {
    static final Logger log = LoggerFactory.getLogger(ScenarioManager.class);

    private static ScenarioManager scenarioManager = null;
    private JSONArray scenario = null;
    private final List<String> msgTypeList = new ArrayList<>();
    private final List<DirType> dirList = new ArrayList<>();
    private final AtomicInteger callNum = new AtomicInteger();
    private final AtomicInteger endCallNum = new AtomicInteger();
    private int configCallNum;

    private ScenarioManager() {
        // Do Nothing
    }

    public static ScenarioManager getInstance() {
        if (scenarioManager == null)
            scenarioManager = new ScenarioManager();
        return scenarioManager;
    }

    public void reset() {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.clearSessionInfoMap();
        scenario = null;
        msgTypeList.clear();
        dirList.clear();
        callNum.set(0);
        endCallNum.set(0);
        log.debug("ScenarioManager Reset callNum:{}, endCall:{}, session:{}", callNum.get(), endCallNum.get(), sessionManager.getSessionInfoSize());
    }

    public boolean init(String scenarioFile) {
        AppInstance.getInstance().setLastMsgTime();
        Config config = AppInstance.getInstance().getConfig();
        String scenarioPath = config.getScenarioPath() + File.separator + scenarioFile + JsonEnum.FILE_EXTENSION.getValue();
        configCallNum = config.getCallNum();
        scenario = ScenarioParser.file2JsonArr(scenarioPath);

        if (scenario == null) {
            endCallNum.set(configCallNum);
            return false;
        }

        JsonParse jsonParse = JsonParse.getInstance();
        for (Object scenarioObj : scenario) {
            String msgType = jsonParse.headerParse((JSONObject) scenarioObj, HeaderField.TYPE.getValue());
            msgTypeList.add(msgType);
            String msgDirection = jsonParse.sendDirParse((JSONObject) scenarioObj);
            dirList.add(DirType.getTypeEnum(msgDirection));
        }

        int maxPauseTime = getMaxPauseTime() + 5;
        int configTimeout = config.getTestTimeout();
        ServiceManager serviceManager = ServiceManager.getInstance();

        if (configTimeout < maxPauseTime)
            serviceManager.setTestTimeout(maxPauseTime);
        else
            serviceManager.setTestTimeout(configTimeout);

        log.warn("\n << INIT SCENARIO, TEST {} CALLS, TIMEOUT {} >>\n => SCENARIO: [{}]\n => MSG     : {}\n => DIR     : {}",
                configCallNum, serviceManager.getTestTimeout(), scenarioPath, msgTypeList, dirList);

        return true;
    }

    // scenario
    public int getScenarioSize() {
        return scenario == null? 0 : this.scenario.size();
    }

    public JSONObject getScenario(int index) {
        return scenario == null? null : (JSONObject) scenario.get(index);
    }

    public JSONArray getScenario() {
        return this.scenario;
    }

    // msgTypeList
    public String getType(int idx) {
        return msgTypeList.isEmpty()? null : msgTypeList.get(idx);
    }

    public List<String> getMsgTypeList() {
        return msgTypeList;
    }

    public boolean hasMsgType(String type) {
        return msgTypeList.contains(type);
    }

    public int getIdxByMsgType(String type) {
        return msgTypeList.indexOf(type);
    }

    public int getLastIdxByMsgType(String type) {
        return msgTypeList.lastIndexOf(type);
    }

    public int getMaxPauseTime() {
        int maxPauseTime = 0;
        for (int i = 0; i < msgTypeList.size(); i++) {
            int pauseTime = getPauseTime(i);
            if (maxPauseTime < pauseTime)
                maxPauseTime = pauseTime;
        }
        return maxPauseTime;
    }

    // dirList
    public DirType getDirType(int idx) {
        return dirList.isEmpty()? null : dirList.get(idx);
    }

    public boolean isSendType(int idx) {
        return DirType.SEND.equals(getDirType(idx));
    }

    public boolean isPauseType(int idx) {
        return DirType.PAUSE.equals(getDirType(idx));
    }

    public boolean isRecvType(int idx) {
        return DirType.RECV.equals(getDirType(idx));
    }

    public int getRecvIdx(int idx) {
        if (idx == 0) return -1;

        while (true) {
            idx--;
            if (getDirType(dirList, idx) == DirType.RECV)
                return idx;
            if (idx <= 0) break;
        }

        return -1;
    }

    // Pause 타입이 아닌 제일 첫 메시지 인덱스
    public int getFirstMsgIndex() {
        int index = -1;
        for (DirType dirType : dirList) {
            index++;
            if (DirType.PAUSE.equals(dirType)) continue;
            return index;
        }

        return -1;
    }

    // CallNum
    public boolean isTestValid() {
        return callNum.get() < configCallNum;
    }

    public int getCallNum() {
        return callNum.get();
    }

    public void increaseCallNum() {
        callNum.incrementAndGet();
    }

    public int getEndCallNum() {
        return endCallNum.get();
    }

    public void increaseEndCallNum() {
        if (scenario != null) {
            endCallNum.incrementAndGet();
        }
    }

    // get Scenario Parsed Info
    public int getPauseTime(int idx) {
        if (isPauseType(idx)) {
            String pauseType = getType(idx);
/*
            if (pauseType != null) return Integer.parseInt(pauseType.substring(5));
*/
            if (pauseType != null) {
                pauseType = pauseType.replaceAll("[^0-9]", "");
                return Integer.parseInt(pauseType);
            }
        }
        return 0;
    }

    public String getSendModule(int idx) {
        JsonParse jsonParse = JsonParse.getInstance();
        JSONObject jsonObject = getScenario(idx);
        return jsonObject == null? null : jsonParse.sendModuleParse(jsonObject);
    }

    public static void main(String[] args) {
/*        parseStr("pause 5");
        parseStr("Pause5");
        parseStr("PAUSE5");
        parseStr("pAuSe5");
        parseStr("pAuSe5.5");*/

        List<DirType> dirList = new ArrayList<>();
        //dirList.add(DirType.RECV);
        dirList.add(DirType.PAUSE);
        dirList.add(DirType.SEND);
        dirList.add(DirType.PAUSE);
        dirList.add(DirType.RECV);
        dirList.add(DirType.SEND);

        System.out.println(getRecvIdx(dirList, 4));

        List<String> list = new ArrayList<>();
        list.add("kim");
        list.add("yoon");
        list.add("park");
        System.out.println(list.contains("kim"));
        System.out.println(list.contains("kimm"));
    }

    public static int getRecvIdx(List<DirType> dirList, int idx) {
        if (idx == 0) return -1;

        while (true) {
            idx--;
            if (getDirType(dirList, idx) == DirType.RECV)
                return idx;
            if (idx <= 0) break;
        }

        return -1;
    }

    public static DirType getDirType(List<DirType> dirList, int idx) {
        return dirList.get(idx);
    }

    public static int parseStr(String pause) {
        try {
            String replacePause = pause.replaceAll("[^0-9]", "");
            int time = Integer.parseInt(replacePause);
            System.out.println(pause + " -> " + replacePause + " ->" + time);
            return time;
        } catch (NumberFormatException e) {
            System.out.println("parseStr.Eception");
        }

        return 0;
    }
}
