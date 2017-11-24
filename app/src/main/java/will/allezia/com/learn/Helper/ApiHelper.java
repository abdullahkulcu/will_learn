package will.allezia.com.learn.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abdullah on 20.10.2017.
 */

public class ApiHelper {
    final static String URL = "https://translation.googleapis.com/language/translate/v2?";
    final static String API_KEY = "***************************";
    final static String FORMAT = "text";

    public static String getFullUrl( String text) {
        return (URL + "q=" + text + "&target=" + "tr" + "&format=" + FORMAT + "&key=" + API_KEY);
    }

    public static String getTranslatedWord(String responseString) {
        JSONObject response = null;
        try {
            response = new JSONObject(responseString).getJSONObject("data");
            JSONArray texts = response.getJSONArray("translations");
            JSONObject word = texts.getJSONObject(0);
            return String.valueOf(word.get("translatedText"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
