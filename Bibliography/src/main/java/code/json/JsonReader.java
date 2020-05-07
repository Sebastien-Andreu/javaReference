package code.json;

import code.utils.AdditionalInput;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {
    public JsonReader() {
    }

    public Map<String, String> read(String name) {
        JSONParser parser = new JSONParser();
        HashMap map = new HashMap();

        try {
            JSONObject file = (JSONObject)parser.parse(new FileReader("json.json"));
            JSONArray json = (JSONArray)file.get("Biblio");
            for (Object jsonObject : json){
                JSONObject j = (JSONObject)jsonObject;
                if (j.get("tag").equals(name)) {
                    map.put("name", j.get("name").toString());
                    map.put("typeOfDocument", j.get("typeOfDocument").toString());
                    map.put("ID", j.get("ID").toString());
                    map.put("ID", j.get("ID").toString());
                    JSONObject object = (JSONObject)j.get("container");
                    map.put("availability", object.get("availability").toString());
                    map.put("title", object.get("title").toString());
                    map.put("Author", object.get("Author").toString());
                    map.put("theme", object.get("theme").toString());
                    map.put("keyWord", object.get("keyWord").toString());
                    map.put("quote", object.get("quote").toString());
                    map.put("note", object.get("note").toString());
                    map.put("date", object.get("date").toString());
                    break;
                }
            }

            AdditionalInput.setAdditionalInput((String)map.get("typeOfDocument"));
            return map;
        } catch (IOException | ParseException var10) {
            var10.printStackTrace();
            return null;
        }
    }
}
