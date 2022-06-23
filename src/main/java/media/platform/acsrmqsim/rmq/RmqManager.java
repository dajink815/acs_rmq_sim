package media.platform.acsrmqsim.rmq;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.common.ServiceType;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.rmq.module.RmqClient;
import media.platform.acsrmqsim.rmq.module.RmqConsumer;
import media.platform.acsrmqsim.rmq.module.RmqServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

/**
 * @author dajin kim
 */
public class RmqManager {
    static final Logger log = LoggerFactory.getLogger(RmqManager.class);
    private static RmqManager rmqManager = null;
    private final AppInstance instance = AppInstance.getInstance();
    private final Config config = instance.getConfig();
    private ExecutorService executorRmqService;

    private RmqServer rmqServer;
    private RmqServer rmqAmfServer;
    // RmqClient Map
    private final ConcurrentHashMap<String, RmqClient> rmqClientMap = new ConcurrentHashMap<>();

    private RmqManager() {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(config.getRmqQueueSize());
        instance.setRmqQueue(queue);
    }

    public static RmqManager getInstance() {
        if (rmqManager == null) {
            rmqManager = new RmqManager();
        }
        return rmqManager;
    }

    public void start() {
        startRmqConsumer();
        startRmqClient();
        startRmqServer();
    }

    public void stop() {
        stopRmqServer();
        stopRmqClient();
        if (executorRmqService!= null) executorRmqService.shutdown();
    }

    // Start RmqConsumer Thread
    private void startRmqConsumer() {
        // todo ExecutorService 예외처리 수정
        if (executorRmqService == null) {
            executorRmqService = Executors.newFixedThreadPool(config.getRmqThreadSize());
            for (int i = 0; i < config.getRmqThreadSize(); i++) {
                executorRmqService.execute(() -> {
                    Thread rmqConsumerThread = new Thread(new RmqConsumer(instance.getRmqQueue()));
                    rmqConsumerThread.start();
                });
            }
        }
    }

    // Start RMQ Client
    private void startRmqClient() {
        addRmqClient(config.getRmqClientQueue());

        if (instance.isEqualService(ServiceType.AWF)) {
            addRmqClient(config.getRmqA2S());
        } else {
            addRmqClient(config.getRmqAwf());
        }

    }

    private void addRmqClient(String queueName) {
        if (!rmqClientMap.containsKey(queueName)) {
            ServiceType serviceType = queueName.toLowerCase().contains("awf")? ServiceType.AWF : ServiceType.A2S;
            RmqClient rmqClient = new RmqClient(queueName, serviceType);
            rmqClient.start();
            rmqClientMap.put(queueName, rmqClient);
        }
    }

    // Start RMQ Server
    private void startRmqServer() {
        if (rmqServer == null) {
            RmqServer server = new RmqServer();
            server.start(instance.getServiceType());
            setRmqServer(server);
        }

        if (instance.isEqualService(ServiceType.AMF) && rmqAmfServer == null) {
            RmqServer server = new RmqServer();
            server.start(ServiceType.AWF);
            setRmqAmfServer(server);
        }
    }

    private void stopRmqServer() {
        if (rmqServer != null) rmqServer.stop();
        if (rmqAmfServer != null) rmqAmfServer.stop();
    }

    private void stopRmqClient() {
        if (!rmqClientMap.isEmpty()) {
            rmqClientMap.forEach((key, client) -> client.closeSender());
        }
    }

    public ConcurrentMap<String, RmqClient> getRmqClientMap() {
        return rmqClientMap;
    }

    public RmqClient getRmqClient(String queueName) {
        return rmqClientMap.get(queueName);
    }

    private void setRmqServer(RmqServer rmqServer) {
        this.rmqServer = rmqServer;
    }

    private void setRmqAmfServer(RmqServer rmqAmfServer) {
        this.rmqAmfServer = rmqAmfServer;
    }
}
