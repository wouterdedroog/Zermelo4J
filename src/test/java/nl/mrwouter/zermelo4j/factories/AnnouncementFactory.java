package nl.mrwouter.zermelo4j.factories;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;

public class AnnouncementFactory extends ApiFactory {

    @Override
    public JsonObject getData(Map<String, String> parameters) {
        Faker faker = new Faker();
        JsonObject jsonObject = new JsonObject();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", 200);
        responseObject.addProperty("message", "");
        responseObject.addProperty("details", "");
        responseObject.addProperty("eventId", 0);
        responseObject.addProperty("startRow", 0);
        responseObject.addProperty("endRow", 1);
        responseObject.addProperty("totalRows", 1);
        JsonArray data = new JsonArray();
        data.add(generateAnnouncement(faker));
        responseObject.add("data", data);

        jsonObject.add("response", responseObject);
        return jsonObject;
    }

    public JsonObject generateAnnouncement(Faker faker) {
        JsonObject announcement = new JsonObject();
        announcement.addProperty("id", faker.number().numberBetween(1, 10000));
        long startTime = faker.number().numberBetween(System.currentTimeMillis() / 1000, System.currentTimeMillis() / 1000 + 864000);
        announcement.addProperty("start", startTime);
        announcement.addProperty("end", startTime + faker.number().numberBetween(864000, 864000 * 3));

        announcement.addProperty("title", "Test");
        announcement.addProperty("text", faker.backToTheFuture().quote());

        announcement.addProperty("read", faker.bool().bool());
        return announcement;
    }
}
