package media.platform.acsrmqsim.json;

public enum JsonEnum {
    FILE_EXTENSION(".json"), DIRECTION("direction"),
    HEADER("header"), BODY("body"), AMF_ID("amf_id");

    final String value;

    public String getValue() {
        return value;
    }

    JsonEnum(String value) {
        this.value = value;
    }
}
