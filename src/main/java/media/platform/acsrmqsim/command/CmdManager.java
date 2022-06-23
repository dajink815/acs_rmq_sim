package media.platform.acsrmqsim.command;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.common.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 사용자로부터 명령어를 전송받고 처리 */
public class CmdManager {
    static final Logger log = LoggerFactory.getLogger(CmdManager.class);
    private final UdpServer udpServer;

    public CmdManager(int port) {
        //명령어를 전송받기 위해 UDP 서버 포트 설정
        udpServer = new UdpServer(port);
    }

    /**
     * UdpServer 를 통해 명령어 receive & 규격에 맞추기(객체에 담기)
     * */
    public CmdInfo cmdRecv(){

        while (true) {
            log.warn("[ ] () () () Waiting UDP Message....");
            String udpCmd = udpServer.recv();
            log.warn("[x] () () () Receive UDP Message = {}" , udpCmd);
            CmdInfo cmdInfo = procUserCmd(udpCmd);

            AppInstance instance = AppInstance.getInstance();
            if(cmdInfo != null && (instance.getServiceType() == null
                    || instance.isEqualService(cmdInfo.getServiceType()))){
                return cmdInfo;
            } else if (cmdInfo != null && !instance.isEqualService(cmdInfo.getServiceType())) {
                log.warn("SIMULATOR [{}] MODE, SERVICE_TYPE ERROR", instance.getServiceType());
            } else {
                log.warn("UDP COMMAND ERROR, SEND AGAIN");
            }
        }
    }

    /** 명령어를 CmdInfo 규격에 맞춰 담기
     * @param userCmd : 사용자로부터 입력받은 명령어
     * @return CmdInfo
     * */
    private CmdInfo procUserCmd(String userCmd){
        // 공백을 기준으로 명령어 나누기
        String[] userCmdArr = userCmd.split("[^\\w]+");

        for(String str:userCmdArr){
            log.debug("msg : {}", str);
        }

        if(userCmdArr.length < 1 || userCmdArr.length > 2){
            log.error("UDP Command Length Error");
            return null;
        }

        String type = userCmdArr[0].toLowerCase();
        ServiceType serviceType = ServiceType.getTypeEnum(type);

        if(serviceType == null){
            log.error("UDP Command ServiceType Error");
            return null;
        }

        // 서비스 타입 세팅
        CmdInfo cmdInfo = new CmdInfo();
        cmdInfo.setServiceType(serviceType);

        if (userCmdArr.length == 2) {
            // 파일 이름 세팅
            cmdInfo.setScenarioFile(userCmdArr[1]);
        }
        log.debug("{}", cmdInfo);
        return cmdInfo;
    }
}
