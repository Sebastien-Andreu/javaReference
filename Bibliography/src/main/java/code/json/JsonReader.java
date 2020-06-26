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
                    map.put("tag", j.get("tag").toString());
                    j.keySet().forEach(e -> {
                        switch (e.toString()){
                            case "name":
                                map.put("name", j.get("name").toString());
                                break;
                            case "typeOfDocument":
                                map.put("typeOfDocument", j.get("typeOfDocument").toString());
                                break;
                            case "ID":
                                map.put("ID", j.get("ID").toString());
                                break;
                        }
                    });
                    JSONObject object = (JSONObject)j.get("container");

                    object.keySet().forEach(e -> {
                        switch (e.toString()){
                            case "availability":
                                map.put("availability", object.get("availability").toString());
                                break;
                            case "title":
                                map.put("title", object.get("title").toString());
                                break;
                            case "author":
                                map.put("author", object.get("author").toString());
                                break;
                            case "theme":
                                map.put("theme", object.get("theme").toString());
                                break;
                            case "keyWord":
                                map.put("keyWord", object.get("keyWord").toString());
                                break;
                            case "quote":
                                map.put("quote", object.get("quote").toString());
                                break;
                            case "note":
                                map.put("note", object.get("note").toString());
                                break;
                            case "date":
                                map.put("date", object.get("date").toString());
                                break;
                            case "confidential":
                                map.put("confidential", object.get("confidential").toString());
                                break;
                            case "read":
                                map.put("read", object.get("read").toString());
                                break;
                        }
                    });
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
