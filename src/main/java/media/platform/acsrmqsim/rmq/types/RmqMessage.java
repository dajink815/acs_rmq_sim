package media.platform.acsrmqsim.rmq.types;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.Serializable;

public class RmqMessage implements Serializable {

    private static final long serialVersionUID = 6671792467428437593L;
    private RmqHeader header;
    private JsonElement body = null;

    public RmqMessage(String type, String transactionId, String msgFrom, String timestamp, int reasonCode, String reason) {
        this.header = new RmqHeader(type, transactionId, msgFrom, timestamp, reasonCode, reason);
    }

    public RmqMessage(RmqHeader header) {
        this.header = new RmqHeader(
                header.getType(),
                header.getTransactionId(),
                header.getMsgFrom(),
                header.getTimestamp(),
                header.getReasonCode(),
                header.getReason()
       );
    }

    public RmqHeader getHeader() {
        return header;
    }

    public void setHeader(RmqHeader header) {
        this.header = header;
    }

    public JsonElement getBody() {
        return body;
    }

    public void setBody(JsonElement body) {
        this.body = body;
    }

    public void setBody(String body) {
        Gson gson = new Gson();
        this.body = gson.toJsonTree(body);
    }

    public String getMsgType() {
        return header.getType();
    }

    @Override
    public String toString() {
        return "RmqMessage{" +
                "header=" + header +
                ", body=" + body + '\'' +
                '}';
    }
}
