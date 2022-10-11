package nl.mrwouter.zermelo4j.factories;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nl.mrwouter.zermelo4j.appointments.AppointmentType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AppointmentFactory extends ApiFactory {

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
        responseObject.add("data", generateAppointments(Long.parseLong(parameters.get("start")), Long.parseLong(parameters.get("end"))));

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("response", responseObject);
        return jsonObject;
    }

    public JsonObject generateAppointment(Faker faker, AppointmentType appointmentType, long startTime, long endTime) {
        JsonObject appointment = new JsonObject();
        appointment.addProperty("id", faker.number().numberBetween(1, 10000));

        appointment.add("groups", this.generateJsonArray(List.of(faker.regexify("[a-z0-9]{3}\\.[a-z0-9]{4}"))));

        long appointmentStartTime = faker.number().numberBetween(startTime, endTime);
        long appointmentEndTime = faker.number().numberBetween(appointmentStartTime, endTime);
        appointment.addProperty("start", appointmentStartTime);
        appointment.addProperty("end", appointmentEndTime);

        appointment.addProperty("type", appointmentType.name().toLowerCase());

        appointment.add("subjects", this.generateJsonArray(List.of(faker.name().username())));

        String timeslot = "u" + faker.number().numberBetween(1, 9);
        appointment.addProperty("startTimeSlotName", timeslot);
        appointment.addProperty("endTimeSlotName", timeslot);

        appointment.add("teachers", this.generateJsonArray(List.of(faker.name().name())));
        appointment.add("locations", this.generateJsonArray(List.of(faker.address().cityName())));

        appointment.addProperty("cancelled", faker.bool().bool());
        appointment.addProperty("changeDescription", faker.bool().bool() ? "" : faker.regexify("[a-zA-Z ]{20}"));
        appointment.addProperty("schedulerRemark", faker.bool().bool() ? "" : faker.regexify("[a-zA-Z ]{20}"));

        appointment.addProperty("valid", faker.bool().bool());
        appointment.addProperty("modified", faker.bool().bool());
        appointment.addProperty("moved", faker.bool().bool());
        appointment.addProperty("new", faker.bool().bool());

        appointment.addProperty("optional", faker.bool().bool());


        return appointment;
    }

    public JsonArray generateAppointments(long startTime, long endTime) {
        JsonArray appointments = new JsonArray();
        Faker faker = new Faker();
        Arrays.stream(AppointmentType.values())
                .forEach(appointmentType -> appointments.add(generateAppointment(faker, appointmentType, startTime, endTime)));

        return appointments;
    }
}
