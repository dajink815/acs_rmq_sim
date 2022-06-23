/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

/**
 * Rabbit MQ Callback
 *
 * @file RmqCallback.java
 * @author Tony Lim
 */

package media.platform.acsrmqsim.rmq.transport;

import java.util.Date;

public interface RmqCallback {
    void onReceived(String msg, Date ts);
}
