package nl.mrwouter.zermelo4j;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nl.mrwouter.zermelo4j.api.ZermeloApiException;
import nl.mrwouter.zermelo4j.appointments.AppointmentParticipation;
import nl.mrwouter.zermelo4j.appointments.AppointmentType;
import nl.mrwouter.zermelo4j.mocks.MockZermeloHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentParticipationTest {

    MockZermeloHttpClient zermeloHttpClient = new MockZermeloHttpClient();

    @Test
    @DisplayName("Can parse appointment participations")
    void testCanParseAppointmentParticipations() throws ZermeloApiException {
        Faker faker = new Faker();
        ZermeloAPI zermeloAPI = ZermeloAPI.getAPI("test", faker.regexify("[a-z0-9]{26}"), zermeloHttpClient);
        List<AppointmentParticipation> appointmentParticipations = zermeloAPI.getAppointmentParticipations(2022, 1);
        assertEquals(appointmentParticipations.size(), Arrays.asList(AppointmentType.values()).size());

        JsonObject firstJson = zermeloHttpClient.lastResponse().get("response").getAsJsonObject() // get response object
                .get("data").getAsJsonArray() // get data array
                .get(0).getAsJsonObject(); // get first appointment participation

        AppointmentParticipation first = appointmentParticipations.stream()
                .filter(appointmentParticipation -> appointmentParticipation.getId() == firstJson.get("id").getAsInt())
                .findFirst()
                .orElse(null);

        assertNotNull(first, "Couldn't find appointment participation object by ID");
        assertEquals(first.getId(), firstJson.get("id").getAsInt());

        assertEquals(String.join(",", first.getGroups()),
                addCommasToJsonArray(firstJson.get("groups").getAsJsonArray()));


        assertEquals(first.getStart(), firstJson.get("start").getAsLong());
        assertEquals(first.getEnd(), firstJson.get("end").getAsLong());

        assertEquals(first.getType().name().toLowerCase(), firstJson.get("appointmentType").getAsString());

        assertEquals(String.join(",", first.getSubjects()),
                addCommasToJsonArray(firstJson.get("subjects").getAsJsonArray()));

        assertEquals(first.getStartTimeSlot(), firstJson.get("startTimeSlotName").getAsString());
        assertEquals(first.getEndTimeSlot(), firstJson.get("endTimeSlotName").getAsString());

        assertEquals(String.join(",", first.getTeachers()),
                addCommasToJsonArray(firstJson.get("teachers").getAsJsonArray()));
        assertEquals(String.join(",", first.getLocations()),
                addCommasToJsonArray(firstJson.get("locations").getAsJsonArray()));

        assertEquals(first.isCancelled(), firstJson.get("cancelled").getAsBoolean());
        assertEquals(first.getChangeDescription(), firstJson.get("changeDescription").getAsString());
        assertEquals(first.getRemark(), firstJson.get("schedulerRemark").getAsString());
    }

    private String addCommasToJsonArray(JsonArray jsonArray) {
        return StreamSupport.stream(jsonArray.spliterator(), true)
                .map(JsonElement::getAsString)
                .collect(Collectors.joining(","));
    }
}
