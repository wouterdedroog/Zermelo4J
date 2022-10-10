package nl.mrwouter.zermelo4j.factories;

import com.google.gson.JsonObject;

import java.util.Map;

public interface ApiFactory {

    JsonObject getData(Map<String, String> parameters);
}
