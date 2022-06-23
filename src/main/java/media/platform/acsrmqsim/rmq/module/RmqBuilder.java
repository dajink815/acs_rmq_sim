/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

/**
 * Rabbit MQ Builder
 *
 * @file RmqBuilder.java
 * @author Tony Lim
 */

package media.platform.acsrmqsim.rmq.module;

import com.google.gson.Gson;
import media.platform.acsrmqsim.rmq.types.RmqMessage;

public class RmqBuilder{
    /**
     * RmqMessage class -> json
     * */
    private RmqBuilder() {
    }

    public static String build(RmqMessage msg) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(msg);
        return json;
    }

}
