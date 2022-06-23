package media.platform.acsrmqsim.json;

import media.platform.acsrmqsim.AppInstance;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JsonParse {
    private static final Logger log = LoggerFactory.getLogger(JsonParse.class);
    private static JsonParse jsonParse = null;
    private final AppInstance instance = AppInstance.getInstance();

    // JSONParser 같은 객체로 파싱할 경우 동시에 parser 사용하려고 할 때 파싱에러 발생
    //private static final JSONParser parser = new JSONParser();

    private JsonParse() {
        // Do Nothing
    }

    public static JsonParse getInstance() {
        if (jsonParse == null)
            jsonParse = new JsonParse();
        return jsonParse;
    }

    public String keyWordParse(String jsonStr) {
        return parse(jsonStr, instance.getKeyWord(), JsonEnum.BODY);
    }

    public List<String> keyWordListParse(String jsonStr) {
        List<String> keyWordList = new ArrayList<>();

        for (String keyWord : instance.getKeyWordList()) {
            keyWordList.add(parse(jsonStr, keyWord, JsonEnum.BODY));
        }

        return keyWordList;
    }

    public String headerParse(String jsonStr, String key){
        return parse(jsonStr, key, JsonEnum.HEADER);
    }

    public String bodyParse(String jsonStr, String key){
        return parse(jsonStr, key, JsonEnum.BODY);
    }



    private String parse(String jsonStr, String key, JsonEnum jsonEnum){
        try {
            JSONObject jsonObj = (JSONObject)(new JSONParser().parse(jsonStr));
            JSONObject header = (JSONObject) (jsonObj.get(jsonEnum.getValue()));
            return header.get(key).toString();
        } catch (Exception e){
            String msgType = jsonStr != null? headerParse(jsonStr, HeaderField.TYPE.getValue()) : null;
            log.trace("JsonParser.parse.Exception [{}-{}:{}]", msgType, jsonEnum.getValue(), key);
        }
        return null;
    }

    private String dirParse(JSONObject jsonObj) {
        try {
            return jsonObj.get(JsonEnum.DIRECTION.getValue()).toString();
        } catch (Exception e){
            log.error("JsonParser.dirParse.Exception [{}] ", jsonObj.toJSONString());
        }
        return null;
    }

    public String sendDirParse(JSONObject jsonObj) {
        try {
            JSONArray jsonArray = (JSONArray) jsonObj.get(JsonEnum.DIRECTION.getValue());
            return jsonArray.get(0).toString();
        } catch (Exception e) {
            return dirParse(jsonObj);
        }
    }

    public String sendModuleParse(JSONObject jsonObj) {
        try {
            JSONArray jsonArray = (JSONArray) jsonObj.get(JsonEnum.DIRECTION.getValue());
            return jsonArray.get(1).toString();
        } catch (Exception e) {
            log.error("JsonParser.sendModuleParse.Exception [{}] ", jsonObj.toJSONString());
        }
        return null;
    }

    public String headerParse(JSONObject jsonObj, String key){
        return parse(jsonObj, key, JsonEnum.HEADER);
    }

    public String bodyParse(JSONObject jsonObj, String key){
        return parse(jsonObj, key, JsonEnum.BODY);
    }

    private String parse(JSONObject jsonObj, String key, JsonEnum jsonEnum){
        try {
            JSONObject field = (JSONObject) jsonObj.get(jsonEnum.getValue());
            return field.get(key).toString();
        } catch (Exception e){
            String msgType = jsonObj != null? headerParse(jsonObj, HeaderField.TYPE.getValue()) : null;
            log.trace("JsonParser.parse.Exception [{}-{}:{}]", msgType, jsonEnum.getValue(), key);
        }
        return null;
    }

    public static void main(String[] args) {
        JsonParse jsonParse = JsonParse.getInstance();
        String json = null;
        jsonParse.bodyParse(json, "callId");
    }

}
