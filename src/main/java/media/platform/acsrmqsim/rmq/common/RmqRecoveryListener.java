package media.platform.acsrmqsim.rmq.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class RmqRecoveryListener implements RecoveryListener {
    private Logger log = LoggerFactory.getLogger(RmqRecoveryListener.class);

    @Override
    public void handleRecovery(Recoverable recoverable) {

        if (recoverable instanceof Channel) {
            int channelNumber = ((Channel) recoverable).getChannelNumber();
            log.error("Rmq Connection to channel # {} was recovered.", channelNumber);
        }
    }

    @Override
    public void handleRecoveryStarted(Recoverable recoverable) {
        log.error("Rmq handleRecoveryStarted");
    }
}
