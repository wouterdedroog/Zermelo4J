package nl.mrwouter.zermelo4j.factories;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nl.mrwouter.zermelo4j.appointments.AppointmentType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AppointmentParticipationFactory extends ApiFactory {

    @Override
    public JsonObject getData(Map<String, String> parameters) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", 200);
        responseObject.addProperty("message", "");
        responseObject.addProperty("details", "");
        responseObject.addProperty("eventId", 0);
        responseObject.addProperty("startRow", 0);
        responseObject.addProperty("endRow", Arrays.stream(AppointmentType.values()).count());
        responseObject.addProperty("totalRows", Arrays.stream(AppointmentType.values()).count());
        responseObject.add("data", generateAppointmentParticipations());

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("response", responseObject);
        return jsonObject;
    }

    public JsonObject generateAppointmentParticipation(Faker faker, AppointmentType appointmentType) {
        JsonObject appointmentParticipation = new JsonObject();
        appointmentParticipation.addProperty("id", faker.number().numberBetween(1, 10000));

        appointmentParticipation.add("groups", this.generateJsonArray(List.of(faker.regexify("[a-z0-9]{3}\\.[a-z0-9]{4}"))));

        long startTime = faker.number().numberBetween(System.currentTimeMillis() / 1000, System.currentTimeMillis() / 1000 + 864000);
        appointmentParticipation.addProperty("start", startTime);
        appointmentParticipation.addProperty("end", startTime + faker.number().numberBetween(1800, 3600));

        appointmentParticipation.addProperty("appointmentType", appointmentType.name().toLowerCase());

        appointmentParticipation.add("subjects", this.generateJsonArray(List.of(faker.name().username())));

        String timeslot = "u" + faker.number().numberBetween(1, 9);
        appointmentParticipation.addProperty("startTimeSlotName", timeslot);
        appointmentParticipation.addProperty("endTimeSlotName", timeslot);

        appointmentParticipation.add("teachers", this.generateJsonArray(List.of(faker.name().name())));
        appointmentParticipation.add("locations", this.generateJsonArray(List.of(faker.address().cityName())));

        appointmentParticipation.addProperty("cancelled", faker.bool().bool());
        appointmentParticipation.addProperty("changeDescription", faker.bool().bool() ? "" : faker.regexify("[a-zA-Z ]{20}"));
        appointmentParticipation.addProperty("schedulerRemark", faker.bool().bool() ? "" : faker.regexify("[a-zA-Z ]{20}"));

        return appointmentParticipation;
    }

    public JsonArray generateAppointmentParticipations() {
        JsonArray appointmentParticipations = new JsonArray();
        Faker faker = new Faker();
        Arrays.stream(AppointmentType.values())
                .forEach(appointmentType -> appointmentParticipations.add(generateAppointmentParticipation(faker, appointmentType)));

        return appointmentParticipations;
    }
}
