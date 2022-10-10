package nl.mrwouter.zermelo4j.factories;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public abstract class ApiFactory {

    public abstract JsonObject getData(Map<String, String> parameters);

    protected JsonArray generateJsonArray(List<String> strings) {
        JsonArray jsonArray = new JsonArray();
        strings.forEach(jsonArray::add);

        return jsonArray;
    }
}
