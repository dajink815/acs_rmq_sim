package media.platform.acsrmqsim.rmq.transport;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.DefaultExceptionHandler;
import media.platform.acsrmqsim.rmq.common.RmqRecoveryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RmqTransport {
    static final Logger log = LoggerFactory.getLogger(RmqTransport.class);
    
    private final String host;
    private final String queueName;
    private final String userName;
    private final String password;
    private final int port;

    private Connection connection = null;
    private Channel channel = null;

    public RmqTransport(String host, int port, String userName, String password, String queueName) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean connect() {

        if (!makeConnection()) {
            return false;
        }

        if (!makeChannel()) {
            closeConnection();
            return false;
        }

        return true;
    }

    public void close() {
        closeChannel();
        closeConnection();
    }

    /**
     * RabbitMQ connection try
     * RabbitMQ Local server recovery 및 connection 시도
     *
     * @return RabbitMQ Connection 생성 결과
     */
    private boolean makeConnection() {
        if (this.channel != null && this.channel.isOpen()) {
            return true;
        }

        boolean result = false;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.host);
        factory.setUsername(this.userName);
        factory.setPassword(this.password);
        factory.setPort(this.port);

        // Add Automatic Recovery 기능
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(1000);
        factory.setRequestedHeartbeat(5);
        factory.setConnectionTimeout(2000);
        factory.setExceptionHandler(new DefaultExceptionHandler() {
            @Override
            public void handleUnexpectedConnectionDriverException(Connection con, Throwable exception) {
                log.error("handleUnexpectedConnectionDriverException Rabbit MQ Connect Fail");
            }
            @Override
            public void handleConnectionRecoveryException(Connection conn, Throwable exception) {
                // Connection Recovery 실패시 발생.
                log.error("handleConnectionRecoveryException Rabbit MQ Connect Fail");
            }
            @Override
            public void handleChannelRecoveryException(Channel ch, Throwable exception) {
                // Channel Recovery 실패시 발생.
                log.error("handleChannelRecoveryException Rabbit MQ Connect Fail");
            }
        });

        try {
            //서버가 지원하는 경우 클라이언트가 제공한 ConnectionName 이 관리 UI에 표시된다.
            this.connection = factory.newConnection("AcsSim_" + this.queueName);
            this.connection.addBlockedListener(new BlockedListener() {
                @Override
                public void handleBlocked(String reason) {
                    log.error("handleBlocked Rabbit MQ Blocked, {}", reason);
                }
                @Override
                public void handleUnblocked() {
                    log.error("handleBlocked Rabbit MQ Blocked");
                }
            });

            result = true;

        } catch (Exception e) {
            log.error("RmqTransport.makeConnection.Exception, [{}@{}:{}-{}] ", this.userName, this.host, this.port, this.queueName, e);
        }
        return result;
    }

    private void closeConnection() {
        try {
            this.connection.close();
        } catch (Exception e) {
            log.error("RmqTransport.closeConnection",e);
        }
    }

    private boolean makeChannel() {
        if (this.channel != null && this.channel.isOpen()) {
            return true;
        }

        boolean result = false;
        try {
            this.channel = this.connection.createChannel();
            ((Recoverable) this.channel).addRecoveryListener(new RmqRecoveryListener());
            this.channel.queueDeclare(this.queueName, false, false, false, null);
            result = true;
        } catch (Exception e) {
            log.error("RmqTransport.makeChannel.Exception, [{}@{}:{}-{}] ", this.userName, this.host, this.port, this.queueName, e);
        }

        return result;

    }

    public void deleteQueue(String queueName) throws IOException {
        this.channel.queueDelete(queueName);
    }

    private void closeChannel() {
        try {
            this.channel.close();
        } catch (Exception e) {
            log.error("RmqTransport.closeChannel",e);
        }
    }
}
