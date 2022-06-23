package media.platform.acsrmqsim.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RmqMessage Json 파일 or Json String 의
 * Header, Body 부분을 수정하는 클래스
 *
 * 공통 매개변수
 * - jsonStr / fileName : 수정하려는 Json String / Json 파일 이름
 * - key : 수정하려는 필드 이름
 * - value : 수정하려는 값
 *
 * return String : 수정된 Json 메시지 결과
 * */
public class JsonModifier {
    private static final Logger log = LoggerFactory.getLogger(JsonModifier.class);

    private static JsonModifier modifier = null;

    public static JsonModifier getInstance() {
        if(modifier == null){
            modifier = new JsonModifier();
        }
        return modifier;
    }

    private JsonModifier() {

    }

    /** Json String 수정 */
    public String delDirectionField(String jsonStr) {
        try {
            JSONObject jsonObj = (JSONObject)(new JSONParser().parse(jsonStr));
            jsonObj.remove(JsonEnum.DIRECTION.getValue());
            return jsonObj.toJSONString();
        } catch (Exception e){
            log.error("JsonModifier.delDirectionField.Exception [{}] ", jsonStr, e);
        }
        return null;
    }

    // Modify String value
    public String headerModifying(String jsonStr, String key, String value) {
        return modifying(jsonStr, key, value, JsonEnum.HEADER);
    }

    public String bodyModifying(String jsonStr, String key, String value) {
        return modifying(jsonStr, key, value, JsonEnum.BODY);
    }

    private String modifying(String jsonStr, String key, String value, JsonEnum jsonEnum) {
        try {
            JSONObject jsonObj = (JSONObject)(new JSONParser().parse(jsonStr));

            JSONObject field = (JSONObject) jsonObj.get(jsonEnum.getValue());
            field.put(key, value);
            jsonObj.put(jsonEnum.getValue(), field);

            return jsonObj.toString();
        } catch (Exception e){
            log.error("JsonModifier.modifying Error [{} ({}:{})]", jsonStr, key, value, e);
        }

        return null;
    }

    // Modify Integer value
    public String headerModifying(String jsonStr, String key, int value) {
        return modifying(jsonStr, key, value, JsonEnum.HEADER);
    }

    public String bodyModifying(String jsonStr, String key, int value) {
        return modifying(jsonStr, key, value, JsonEnum.BODY);
    }

    private String modifying(String jsonStr, String key, int value, JsonEnum jsonEnum) {
        try {
            JSONObject jsonObj = (JSONObject) (new JSONParser().parse(jsonStr));

            JSONObject field = (JSONObject) jsonObj.get(jsonEnum.getValue());
            field.put(key, value);
            jsonObj.put(jsonEnum.getValue(), field);

            return jsonObj.toString();
        } catch (ParseException e) {
            log.error("JsonModifier.modifying Error [{} ({}:{})]", jsonStr, key, value, e);
        }

        return null;
    }

}
