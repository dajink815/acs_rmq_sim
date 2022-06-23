package media.platform.acsrmqsim.command;

import media.platform.acsrmqsim.common.ServiceType;

/** User Command 를 담을 포맷 */
public class CmdInfo {

    private ServiceType serviceType;
    private String scenarioFile;

    public CmdInfo() {
    }

    public String getScenarioFile() {
        return scenarioFile;
    }

    public void setScenarioFile(String scenarioFile) {
        this.scenarioFile = scenarioFile;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }


    @Override
    public String toString() {
        return "CmdInfo{" +
                "serviceType=" + serviceType +
                ", scenarioFile='" + scenarioFile + '\'' +
                '}';
    }
}
