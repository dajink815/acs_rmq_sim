package media.platform.acsrmqsim.rmq.transport;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * RMQ 의 큐에서 메시지를 가져와
 * RmqServer 에서 만든 BlockingQueue 에 메시지 push
 * */
public class RmqReceiver extends RmqTransport {

    private RmqCallback callback = null;

    public RmqReceiver(String host, int port, String userName, String password, String queueName) {
        super(host, port, userName, password, queueName);
    }

    public void setCallback(RmqCallback callback) {
        this.callback = callback;
    }

    private final Consumer consumer = new DefaultConsumer(getChannel()) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            String msg = new String(body, StandardCharsets.UTF_8);
            if (callback != null) {
                try {
                    Date ts = null;
                    Map<String, Object> headers = properties.getHeaders();
                    if (headers != null) {
                        // rmq message log message 도착 시간
                        Long ms =(Long)headers.get("timestamp_in_ms");
                        if (ms != null) {
                            ts = new Date(ms);
                        }
                    }
                    //블록킹 큐에 메시지 넣는 부분
                    callback.onReceived(msg, ts);
                }
                catch (Exception e) {
                    log.error("RmqReceiver.handleDelivery",e);
                }
            }
        }
    };

    public boolean start(){

        if (!getChannel().isOpen()){
            log.error("RMQ channel is NOT opened");
            return false;
        }

        boolean result = false;

        try {
            getChannel().basicConsume(getQueueName(), true, this.consumer);
            result = true;
        } catch (Exception e) {
            log.error("RmqReceiver.start Error",e);
        }

        return result;
    }

}
