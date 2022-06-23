package media.platform.acsrmqsim.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonPretty {

    private JsonPretty() {
        // Do Nothing
    }

    public static String getFormatJson(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = new JsonParser().parse(json);
        return gson.toJson(je);
    }
}
