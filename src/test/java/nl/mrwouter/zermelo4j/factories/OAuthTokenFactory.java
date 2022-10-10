package nl.mrwouter.zermelo4j.factories;

import com.github.javafaker.Faker;
import com.google.gson.JsonObject;

import java.util.Map;

public class OAuthTokenFactory implements ApiFactory {

    @Override
    public JsonObject getData(Map<String, String> parameters) {
        Faker faker = new Faker();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("access_token", faker.regexify("[a-z0-9]{26}"));
        jsonObject.addProperty("token_type", "bearer");
        jsonObject.addProperty("expires_in", 57600);
        return jsonObject;
    }
}
