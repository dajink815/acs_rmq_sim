package media.platform.acsrmqsim;

import media.platform.acsrmqsim.service.ServiceManager;
import org.slf4j.LoggerFactory;

/**
 * @author dajin kim
 */
public class AcsRmqSimMain {

    public static void main(String[] args){

        LoggerFactory.getLogger(AcsRmqSimMain.class).info("RmqMsg Simulator Process Start");

        AppInstance instance = AppInstance.getInstance();
        instance.setConfigPath(args[0]);
        LoggerFactory.getLogger(AcsRmqSimMain.class).info("configPath:{}", args[0]);

        ServiceManager serviceManager = ServiceManager.getInstance();
        serviceManager.loop();

    }
}
