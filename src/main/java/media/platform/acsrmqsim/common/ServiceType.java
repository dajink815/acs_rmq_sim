package media.platform.acsrmqsim.common;

public enum ServiceType {
    A2S, AMF, AWF;

    public static ServiceType getTypeEnum(String type) {

        if(type.equalsIgnoreCase(ServiceType.A2S.toString())){
            return A2S;
        } else if(type.equalsIgnoreCase(ServiceType.AMF.toString())){
            return AMF;
        } else if(type.equalsIgnoreCase(ServiceType.AWF.toString())){
            return AWF;
        } else {
            return null;
        }
    }

}
