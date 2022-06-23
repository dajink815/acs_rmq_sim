package media.platform.acsrmqsim.rmq.common.handler;

import com.google.gson.annotations.SerializedName;

public class SetTaskCall {
    @SerializedName("taskId")
    private String taskId;
    @SerializedName("callId")
    private String callId;
    @SerializedName("uberId")
    private String uberId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getUberId() {
        return uberId;
    }

    public void setUberId(String uberId) {
        this.uberId = uberId;
    }
}
