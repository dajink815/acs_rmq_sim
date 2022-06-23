package media.platform.acsrmqsim.rmq.types;

public class RmqHeader {
    String type;
    String transactionId;
    String msgFrom;
    String timestamp;
    int reasonCode;
    String reason;

    public RmqHeader(){

    }

    public RmqHeader(String type, String transactionId, String msgFrom, String timestamp, int reasonCode, String reason) {
        this.type = type;
        this.transactionId = transactionId;
        this.msgFrom = msgFrom;
        this.timestamp = timestamp;
        this.reasonCode = reasonCode;
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(int reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "RmqHeader{" +
                "type='" + type + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", msgFrom='" + msgFrom + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", reasonCode='" + reasonCode + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
