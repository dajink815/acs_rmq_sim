package media.platform.acsrmqsim.json;

/**
 * @author dajin kim
 */
public enum HeaderField {
    TYPE("type"), REASON_CODE("reasonCode"), TRANSACTION_ID("transactionId"),
    MSG_FROM("msgFrom"), TIMESTAMP("timestamp");

    final String value;

    public String getValue() {
        return value;
    }

    HeaderField(String value) {
        this.value = value;
    }

    public static HeaderField getTypeEnum(String keyName) {
        switch (keyName.toLowerCase()) {
            case "reasoncode":
                return REASON_CODE;
            case "transactionid":
                return TRANSACTION_ID;
            case "msgfrom":
                return MSG_FROM;
            case "timestamp":
                return TIMESTAMP;
            case "type":
            default:
                return TYPE;
        }
    }
}
