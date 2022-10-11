package nl.mrwouter.zermelo4j.factories;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.Map;

public class UserFactory extends ApiFactory {

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
        data.add(generateUser(faker));
        responseObject.add("data", data);

        jsonObject.add("response", responseObject);
        return jsonObject;
    }

    public JsonObject generateUser(Faker faker) {
        JsonObject user = new JsonObject();
        user.addProperty("code", faker.number().numberBetween(1, 10000));

        JsonArray roles = new JsonArray();
        if (faker.bool().bool())
            roles.add(faker.company().name());
        user.add("roles", roles);

        if (faker.bool().bool()) {
            user.add("firstName", JsonNull.INSTANCE);
            user.add("prefix", JsonNull.INSTANCE);
            user.add("lastName", JsonNull.INSTANCE);
        }else{
            user.addProperty("firstName", faker.name().firstName());
            user.addProperty("prefix", faker.name().prefix());
            user.addProperty("lastName", faker.name().lastName());
        }
        user.addProperty("isApplicationManager", faker.bool().bool());
        user.addProperty("archived", faker.bool().bool());
        user.addProperty("isStudent", faker.bool().bool());
        user.addProperty("isEmployee", faker.bool().bool());
        user.addProperty("isFamilyMember", faker.bool().bool());
        user.addProperty("hasPassword", faker.bool().bool());
        user.addProperty("isSchoolScheduler", faker.bool().bool());
        user.addProperty("isSchoolLeader", faker.bool().bool());
        user.addProperty("isStudentAdministrator", faker.bool().bool());
        user.addProperty("isTeamLeader", faker.bool().bool());
        user.addProperty("isSectionLeader", faker.bool().bool());
        user.addProperty("isMentor", faker.bool().bool());
        user.addProperty("isParentTeacherNightScheduler", faker.bool().bool());
        user.addProperty("isDean", faker.bool().bool());
        return user;
    }
}
