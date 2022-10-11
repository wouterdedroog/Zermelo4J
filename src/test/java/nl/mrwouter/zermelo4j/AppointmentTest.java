package nl.mrwouter.zermelo4j;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nl.mrwouter.zermelo4j.api.ZermeloApiException;
import nl.mrwouter.zermelo4j.appointments.Appointment;
import nl.mrwouter.zermelo4j.appointments.AppointmentType;
import nl.mrwouter.zermelo4j.mocks.MockZermeloHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppointmentTest {

    MockZermeloHttpClient zermeloHttpClient = new MockZermeloHttpClient();

    @Test
    @DisplayName("Can parse appointments")
    void testCanParseAppointment() throws ZermeloApiException {
        Faker faker = new Faker();
        ZermeloAPI zermeloAPI = ZermeloAPI.getAPI("test", faker.regexify("[a-z0-9]{26}"), zermeloHttpClient);
        Date today = new Date();
        List<Appointment> appointments = zermeloAPI.getAppointments(today, addWeek(today));
        assertEquals(appointments.size(), Arrays.asList(AppointmentType.values()).size());

        JsonObject firstJson = zermeloHttpClient.lastResponse().get("response").getAsJsonObject() // get response object
                .get("data").getAsJsonArray() // get data array
                .get(0).getAsJsonObject(); // get first appointment

        Appointment first = appointments.stream()
                .filter(appointment -> appointment.getId() == firstJson.get("id").getAsInt())
                .findFirst()
                .orElse(null);

        assertNotNull(first, "Couldn't find appointment object by ID");
        assertEquals(first.getId(), firstJson.get("id").getAsInt());

        assertEquals(String.join(",", first.getGroups()),
                addCommasToJsonArray(firstJson.get("groups").getAsJsonArray()));


        assertEquals(first.getStart(), firstJson.get("start").getAsLong());
        assertEquals(first.getEnd(), firstJson.get("end").getAsLong());

        assertEquals(first.getType().name().toLowerCase(), firstJson.get("type").getAsString());

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

        assertEquals(first.isValid(), firstJson.get("valid").getAsBoolean());
        assertEquals(first.isModified(), firstJson.get("modified").getAsBoolean());
        assertEquals(first.isMoved(), firstJson.get("moved").getAsBoolean());
        assertEquals(first.isNew(), firstJson.get("new").getAsBoolean());
        assertEquals(first.isOptional(), firstJson.get("optional").getAsBoolean());
    }

    private Date addWeek(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    private String addCommasToJsonArray(JsonArray jsonArray) {
        return StreamSupport.stream(jsonArray.spliterator(), true)
                .map(JsonElement::getAsString)
                .collect(Collectors.joining(","));
    }
}
