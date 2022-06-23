package media.platform.acsrmqsim.scenario;

import media.platform.acsrmqsim.common.FileUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author dajin kim
 */
public class ScenarioParser {
    static final Logger log = LoggerFactory.getLogger(ScenarioParser.class);

    private ScenarioParser() {
        // Do Nothing
    }

    public static JSONArray file2JsonArr(String fileName) {
        JSONArray jsonArray = null;
        String json;
        try {
            json = FileUtil.filepathToString(fileName);
/*            JSONObject jsonObj = (JSONObject)(new JSONParser().parse(json));
            jsonArray = (JSONArray) jsonObj.get(JsonEnum.MSG.getValue());*/
            jsonArray = (JSONArray) new JSONParser().parse(json);
        } catch (IOException e) {
            log.error("ScenarioParser.file2JsonArr - FILE ERROR [{}]", fileName);
        } catch (ParseException e) {
            log.error("ScenarioParser.file2JsonArr - CHECK SCENARIO FORMAT [{}]", fileName);
        }

        return jsonArray;
    }

    public static JSONObject file2JsonObj(String fileName) {
        JSONObject jsonObject = null;
        try {
            String json = FileUtil.filepathToString(fileName);
            jsonObject = (JSONObject)(new JSONParser().parse(json));
        } catch (IOException e) {
            log.error("ScenarioParser.file2JsonObj - FILE ERROR [{}]", fileName);
        } catch (ParseException e) {
            log.error("ScenarioParser.file2JsonObj - CHECK SCENARIO FORMAT [{}]", fileName);
        }

        return jsonObject;
    }
}
