package media.platform.acsrmqsim.scenario;

/**
 * @author dajin kim
 */
public enum DirType {
    SEND("send"), RECV("recv"), PAUSE("pause");

    final String value;

    public String getValue() {
        return value;
    }

    DirType(String value) {
        this.value = value;
    }

    public static DirType getTypeEnum(String direction) {
        switch (direction.toLowerCase()) {
            case "send":
            case "s":
                return SEND;
            case "pause":
            case "p":
                return PAUSE;
            case "receive":
            case "recv":
            case "r":
            default:
                return RECV;
        }
    }
}
