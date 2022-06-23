package media.platform.acsrmqsim.service;

import media.platform.acsrmqsim.scenario.ScenarioProc;
import media.platform.qos.manager.RmqTrafficManager;
import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.command.CmdInfo;
import media.platform.acsrmqsim.command.CmdManager;
import media.platform.acsrmqsim.common.SleepUtil;
import media.platform.acsrmqsim.common.XmlParser;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.rmq.RmqManager;
import media.platform.acsrmqsim.rmq.types.RmqMessageType;
import media.platform.acsrmqsim.scenario.ScenarioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class ServiceManager {
    static final Logger log = LoggerFactory.getLogger(ServiceManager.class);
    private static ServiceManager serviceManager = null;
    private boolean isQuit = false;

    private final AppInstance instance = AppInstance.getInstance();
    private RmqManager rmqManager;
    private HeartbeatService heartbeatService;
    private final ScenarioManager scenarioManager = ScenarioManager.getInstance();
    private ScenarioProc scenarioProcess;
    private CmdManager cmdManager;
    private Config config;
    private int testTimeout;

    private ServiceManager() {
        instance.setConfig(new Config(instance.getConfigPath()));

    }

    public static ServiceManager getInstance() {
        if (serviceManager == null) {
            serviceManager = new ServiceManager();
        }
        return serviceManager;
    }

    public void loop() {
        this.startService();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Process is about to quit (Ctrl+C)");
            this.isQuit = true;
            this.stopService();
        }));

        while (!isQuit) {
            //log.debug("ConfigNum:{}, EndCall:{}", config.getCallNum(), scenarioManager.getEndCallNum());
            boolean isTimeout = checkRmqTimeout();
            if (config.getCallNum() <= scenarioManager.getEndCallNum() || isTimeout) {
                if (isTimeout) {
                    log.warn("================RMQ MSG TIMEOUT({})================", testTimeout);
                } else {
                    log.warn("================TEST IS END({})================", scenarioManager.getEndCallNum());
                }
                startNewTest();
            }

            SleepUtil.trySleep(1000);
        }
    }

    private boolean checkRmqTimeout() {
        return instance.getLastMsgTime() + testTimeout * 1000 < System.currentTimeMillis();
    }

    public int getTestTimeout() {
        return testTimeout;
    }

    public void setTestTimeout(int testTimeout) {
        this.testTimeout = testTimeout;
    }

    private void startService() {
        config = instance.getConfig();
        testTimeout = config.getTestTimeout();

        rmqManager = RmqManager.getInstance();
        heartbeatService = HeartbeatService.getInstance();
        scenarioProcess = ScenarioProc.getInstance();
        cmdManager = new CmdManager(config.getServerPort());

        new XmlParser().xmlParsing();

        RmqTrafficManager rmqTrafficManager = RmqTrafficManager.getInstance();
        rmqTrafficManager.start(5000, 2000);
        rmqTrafficManager.addExcludedRmqType(RmqMessageType.RMQ_MSG_STR_HB_REQ);
        rmqTrafficManager.addExcludedRmqType(RmqMessageType.RMQ_MSG_STR_HB_RES);
        rmqTrafficManager.addExcludedRmqType(RmqMessageType.RMQ_MSG_STR_HEARTBEAT_REQ);
        rmqTrafficManager.addExcludedRmqType(RmqMessageType.RMQ_MSG_STR_HEARTBEAT_RES);
        rmqTrafficManager.addExcludedRmqType(RmqMessageType.RMQ_MSG_STR_LOGIN_REQ);
        rmqTrafficManager.addExcludedRmqType(RmqMessageType.RMQ_MSG_STR_LOGIN_RES);
    }

    private void startNewTest() {
        // 시나리오 초기화
        scenarioManager.reset();

        // UDP 로 명령어 수신
        CmdInfo cmdInfo = cmdManager.cmdRecv();

        // ServiceType, client & server 큐 세팅
        if (!instance.setServiceType(cmdInfo.getServiceType())) return;
        config.settingConfig();

        // RMQ
        rmqManager.start();

        // Login, HB
        heartbeatService.start();

        // Scenario 파싱
        String file = cmdInfo.getScenarioFile();
        if (file != null && scenarioManager.init(file)) {
            // Scenario Process
            scenarioProcess.start();
        }
    }

    public void stopService() {
        rmqManager.stop();
        heartbeatService.stop();
        scenarioProcess.stop();
    }
}
