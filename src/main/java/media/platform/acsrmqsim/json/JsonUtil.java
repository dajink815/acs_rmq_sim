package media.platform.acsrmqsim.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonUtil {

    /** json 파일에서 하나의 키 값 구해주는 클래스
     * 메소드 공통 매개변수 :
     * 1. 파싱하고자 하는 jsonStr
     * 2. 원하는 값의 요소를 상위 레벨부터 모두 나열
     * 예시 : {header : {type : 예시}}
     * 위의 json 파일 중 type 값이 필요하다면 최 상위 요소인 "header", "type" 모두 매개변수로 넘겨준다.
     * */

    public static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    public boolean isJson(String msg) {
        Gson gson = new Gson();
        try {
            gson.fromJson(msg, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public JsonElement json2Element(String jsonStr, String ... element)throws Exception{
        if (element.length <=0) {
            throw new Exception("Element length must more than 1");
        }

        Gson gson = new Gson();
        JsonObject jo = gson.fromJson(jsonStr, JsonObject.class);
        for (int i = 0; i<element.length-1;i++){
            jo = jo.getAsJsonObject(element[i]);
        }
        return jo.get(element[element.length-1]);
    }


    public int json2Int(String jsonStr, String ... element) {
        int result = -1;

        try {
            result = json2Element(jsonStr, element).getAsInt();
        } catch (Exception e) {
            //log.warn("JsonUtil.json2Int Error - (element : {}) jsonStr : {} ", element, jsonStr);
            log.warn("JsonUtil.json2Int Error - (element : {})", element);

        }

        return result;
    }

    public String json2String(String jsonStr, String ... element) {
        try {
            return json2Element(jsonStr, element).getAsString();
        } catch (Exception e) {
            //log.warn("JsonUtil.json2String Error - (element : {}) jsonStr : {} ", element, jsonStr);
            log.warn("JsonUtil.json2String Error - (element : {}) ", element);
            return null;
        }
    }



    public boolean json2Boolean(String jsonStr, String ... element)throws Exception{
        return json2Element(jsonStr, element).getAsBoolean();
    }

    public float json2Float(String jsonStr, String ... element)throws Exception{
        return json2Element(jsonStr, element).getAsFloat();
    }



    public int[] json2IntArr(String jsonStr, String ... element) throws Exception{
        JsonElement jsonelement = json2Element(jsonStr, element);
        return jsonArr2IntArr(jsonelement.getAsJsonArray());
    }

    public String[] json2StringArr(String jsonStr, String ... element) throws Exception{
        JsonElement jsonelement = json2Element(jsonStr, element);
        return jsonArr2StringArr(jsonelement.getAsJsonArray());
    }

    public float[] json2FloatArr(String jsonStr, String ... element) throws Exception{
        JsonElement jsonelement = json2Element(jsonStr, element);
        return jsonArr2FloatArr(jsonelement.getAsJsonArray());
    }

    public boolean[] json2BooleanArr(String jsonStr, String ... element) throws Exception{
        JsonElement jsonelement = json2Element(jsonStr, element);
        return jsonArr2BooleanArr(jsonelement.getAsJsonArray());
    }


    public int[] jsonArr2IntArr(JsonArray jsonArray){
        int[] ret = new int[jsonArray.size()];
        for (int i =0; i<jsonArray.size();i++){
            ret[i] = jsonArray.get(i).getAsInt();
        }
        return ret;
    }
    public String[] jsonArr2StringArr(JsonArray jsonArray){
        String[] ret = new String[jsonArray.size()];
        for (int i =0; i<jsonArray.size();i++){
            ret[i] = jsonArray.get(i).getAsString();
        }
        return ret;
    }
    public boolean[] jsonArr2BooleanArr(JsonArray jsonArray){
        boolean[] ret = new boolean[jsonArray.size()];
        for (int i =0; i<jsonArray.size();i++){
            ret[i] = jsonArray.get(i).getAsBoolean();
        }
        return ret;
    }
    public float[] jsonArr2FloatArr(JsonArray jsonArray){
        float[] ret = new float[jsonArray.size()];
        for (int i =0; i<jsonArray.size();i++){
            ret[i] = jsonArray.get(i).getAsFloat();
        }
        return ret;
    }
}
