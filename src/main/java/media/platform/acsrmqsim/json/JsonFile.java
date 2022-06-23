package media.platform.acsrmqsim.json;

public enum JsonFile {
    LOGIN_REQ("login_req.json"), LOGIN_RES("login_res.json"),
    HB_REQ("hb_req.json"), HB_RES("hb_res.json"),
    HEARTBEAT_REQ("heartbeat_req.json"), HEARTBEAT_RES("heartbeat_res.json");

    final String file;

    public String getFile() {
        return file;
    }

    JsonFile(String file) {
        this.file = file;
    }
}
