package media.platform.acsrmqsim.rmq.types;

import com.google.gson.Gson;

import java.io.Serializable;

public class RmqData<T> implements Serializable {

    private Class<T> classType;

    public RmqData(Class<T> classType) {
        this.classType = classType;
    }

    /** RmqMessage.Body json -> Object */
    public T parse(RmqMessage rmq) {
        Gson gson = new Gson();
        return gson.fromJson(rmq.getBody(), classType);
    }

    /** Object -> json */
    public String build(T data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
