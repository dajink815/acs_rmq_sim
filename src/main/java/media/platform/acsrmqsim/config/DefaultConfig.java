package media.platform.acsrmqsim.config;

import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.event.Event;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.concurrent.TimeUnit;

/** Config 값 동적 로딩을 위한 클래스 */
public class DefaultConfig {
    static final Logger log = LoggerFactory.getLogger(DefaultConfig.class);

    private ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder;

    protected boolean load(String configPath){
        //log.debug("Config Path is {} ", configPath);

        boolean result = false;
        try {
            Parameters params = new Parameters();
            File f = new File(configPath);
            builder = new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(INIConfiguration.class).configure(params.fileBased().setFile(f).setReloadingRefreshDelay(0L));

            builder.addEventListener(ConfigurationBuilderEvent.RESET, new EventListener<Event>() {
                Configuration config1 = builder.getConfiguration();

                //Config 변화 이벤트가 발생하면 로그 출력, 새로운 Config 값 불러오기
                public void onEvent(Event event) {
                    try
                    {
                        Configuration config2 = builder.getConfiguration();
                        ConfigurationComparator comparator = new StrictConfigurationComparator();
                        if (!comparator.compare(config1, config2))
                        {
                            //config 1, 2 비교 -> 바뀌었으면 로그 출력, config1 에 새로운 config 저장
                        } else
                        {
                            log.debug("Default.config not changed");
                        }
                    } catch (ConfigurationException e)
                    {
                        log.error("DefaultConfig.load",e);
                    }
                }
            });

            PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(builder.getReloadingController(),null, 1, TimeUnit.SECONDS);
            trigger.start();
            result = true;
        } catch (Exception e) {
            log.error("DefaultConfig.load",e);
        }

        return result;
    }

    public Configuration getConfig() {
        CompositeConfiguration config = new CompositeConfiguration();
        try {
            config.addConfiguration(builder.getConfiguration());
        } catch (ConfigurationException e) {
            log.error("DefaultConfig.getConfig",e);
        }

        return config;
    }

    public void save() throws ConfigurationException {
        builder.save();
    }

    public Configuration getWritableConfig() {
        try {
            return builder.getConfiguration();
        } catch (ConfigurationException e) {
            log.error("DefaultConfig.getWritableConfig",e);
        }
        return null;
    }

    /**
     * Config 의 String 값 불러오기
     * @param section 값을 불러오고 싶은 Config 의 section
     * @param key Config 의 key 값
     * @param defaultValue section 이 존재하지 않을 경우 반환 할 default 값
     * */
    public String getStrValue(Section section, String key, String defaultValue) {
        String mkey = section + "." + key;
        String value = null;

        if (section == null) {
            return defaultValue;
        }
        try {
            value = getConfig().getString(mkey);
        } catch (Exception e) {
            log.error("DefaultConfig.getStrValue",e);
        }


        return value;
    }

    /** Config 의 int 값 불러오기 */
    public int getIntValue(Section section, String key, int defaultValue) {

        String mkey = section + "." + key;
        int rvalue = 0;

        if (section == null) {
            return defaultValue;
        }

        try {
            rvalue = getConfig().getInt(mkey);
        } catch (Exception e) {
            log.error("DefaultConfig.getIntValue",e);
        }

        return rvalue;
    }

    public int getIntValueException(Section section, String key, int defaultValue, int max, int min) {
        String mkey = section + "." + key;

        int rvalue = 0;

        if (section == null) {
            return defaultValue;
        }

        try {
            rvalue = getConfig().getInt(mkey);
            if (max != 0) {
                if (rvalue > max || rvalue < min) {
                    log.error(" Config [{}:{}] is not invalid. Max Value is [{}], Min Value is [{}]",  section, key, max, min);
                    Runtime.getRuntime().exit(0);
                }
            } else {
                if (rvalue < min) {
                    log.error(" Config [{}:{}] is not invalid. Min Value is [{}]",  section, key, min);
                    Runtime.getRuntime().exit(0);
                }
            }
            return rvalue;
        } catch (Exception e) {
            log.error("DefaultConfig.getIntValueException",e);
        }

        return rvalue;

    }

    /** Config 의 float 값 불러오기 */
    public float getFloatValue(Section section, String key, float defaultValue) {

        float result;
        String mkey = section + "." + key;
        String value = null;

        if (section == null) {
            return defaultValue;
        }
        try {
            value = getConfig().getString(mkey);
            result = Float.valueOf(value);
        } catch (Exception e) {
            log.error("DefaultConfig.getFloatValue",e);
            result = defaultValue;
        }

        return result;
    }
}
