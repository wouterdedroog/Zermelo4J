package nl.mrwouter.zermelo4j;

import com.github.javafaker.Faker;
import com.google.gson.JsonObject;
import nl.mrwouter.zermelo4j.annoucements.Announcement;
import nl.mrwouter.zermelo4j.api.ZermeloApiException;
import nl.mrwouter.zermelo4j.mocks.MockZermeloHttpClient;
import nl.mrwouter.zermelo4j.users.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    MockZermeloHttpClient zermeloHttpClient = new MockZermeloHttpClient();

    @Test
    @DisplayName("Can parse user")
    void testCanParseUser() throws ZermeloApiException {
        Faker faker = new Faker();
        ZermeloAPI zermeloAPI = ZermeloAPI.getAPI("test", faker.regexify("[a-z0-9]{26}"), zermeloHttpClient);
        User user = zermeloAPI.getUser();

        JsonObject firstJson = zermeloHttpClient.lastResponse().get("response").getAsJsonObject() // get response object
                .get("data").getAsJsonArray() // get data array
                .get(0).getAsJsonObject(); // get first user


        assertEquals(user.getUser(), firstJson.get("code").getAsString());

        buildNullSafeAssertion(user.getFirstName(), firstJson, "firstName");
        buildNullSafeAssertion(user.getPrefix(), firstJson, "prefix");
        buildNullSafeAssertion(user.getLastName(), firstJson, "lastName");

        assertEquals(user.isApplicationManager(), firstJson.get("isApplicationManager").getAsBoolean());
        assertEquals(user.isArchived(), firstJson.get("archived").getAsBoolean());
        assertEquals(user.isStudent(), firstJson.get("isStudent").getAsBoolean());
        assertEquals(user.isEmployee(), firstJson.get("isEmployee").getAsBoolean());
        assertEquals(user.isFamilyMember(), firstJson.get("isFamilyMember").getAsBoolean());
        assertEquals(user.hasPassword(), firstJson.get("hasPassword").getAsBoolean());
        assertEquals(user.isSchoolScheduler(), firstJson.get("isSchoolScheduler").getAsBoolean());
        assertEquals(user.isSchoolLeader(), firstJson.get("isSchoolLeader").getAsBoolean());
        assertEquals(user.isStudentAdministrator(), firstJson.get("isStudentAdministrator").getAsBoolean());
        assertEquals(user.isTeamLeader(), firstJson.get("isTeamLeader").getAsBoolean());
        assertEquals(user.isSectionLeader(), firstJson.get("isSectionLeader").getAsBoolean());
        assertEquals(user.isMentor(), firstJson.get("isMentor").getAsBoolean());
        assertEquals(user.isParentTeacherNightScheduler(), firstJson.get("isParentTeacherNightScheduler").getAsBoolean());
        assertEquals(user.isDean(), firstJson.get("isDean").getAsBoolean());

    }

    private void buildNullSafeAssertion(String potentiallyNull, JsonObject object, String potentiallyNullPath) {
        if (object.get(potentiallyNullPath).isJsonNull()) {
            assertNull(potentiallyNull);
        }else{
            assertEquals(potentiallyNull, object.get(potentiallyNullPath).getAsString());
        }
    }
}
