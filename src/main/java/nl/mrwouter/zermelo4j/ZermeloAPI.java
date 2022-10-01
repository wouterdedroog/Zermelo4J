package nl.mrwouter.zermelo4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.mrwouter.zermelo4j.annoucements.Announcement;
import nl.mrwouter.zermelo4j.annoucements.AnnouncementComparator;
import nl.mrwouter.zermelo4j.api.ZermeloApiException;
import nl.mrwouter.zermelo4j.api.ZermeloHttpClient;
import nl.mrwouter.zermelo4j.appointments.Appointment;
import nl.mrwouter.zermelo4j.appointments.AppointmentComparator;
import nl.mrwouter.zermelo4j.appointments.AppointmentParticipation;
import nl.mrwouter.zermelo4j.appointments.AppointmentType;
import nl.mrwouter.zermelo4j.users.User;

/**
 * Main class for Zermelo4J
 */
public class ZermeloAPI {

    private final String school;
    private final String accessToken;
    private final ZermeloHttpClient zermeloHttpClient;

    private ZermeloAPI(String school, String accessToken, ZermeloHttpClient zermeloHttpClient) {
        this.school = school;
        this.accessToken = accessToken;
        this.zermeloHttpClient = zermeloHttpClient;
    }

    /**
     * Get a valid access token with given 'Koppel App' code.
     *
     * @param school   code of school, for example apidemo when URL is
     *                 apidemo.zportal.nl
     * @param authCode Code that can be aquired at the 'Koppel App' page.
     * @return Valid access token for use with {@link #getAPI}.
     * @throws ZermeloApiException thrown when authCode is invalid
     */
    public static String getAccessToken(String school, String authCode) throws ZermeloApiException {
        return ZermeloAPI.getAccessToken(school, authCode, new ZermeloHttpClient());
    }

    /**
     * Get a valid access token with given 'Koppel App' code.
     *
     * @param school            code of school, for example apidemo when URL is
     *                          apidemo.zportal.nl
     * @param authCode          Code that can be aquired at the 'Koppel App' page.
     * @param zermeloHttpClient Zermelo HTTP client
     * @return Valid access token for use with {@link #getAPI}.
     * @throws ZermeloApiException thrown when authCode is invalid
     */
    public static String getAccessToken(String school, String authCode, ZermeloHttpClient zermeloHttpClient) throws ZermeloApiException {
        HttpResponse<String> response = zermeloHttpClient.post("/oauth/token", school, "",
                Map.of("grant_type", "authorization_code", "code", authCode));

        JsonElement root = JsonParser.parseString(response.body());
        JsonObject rootObject = root.getAsJsonObject();

        return rootObject.get("access_token").getAsString();
    }

    /**
     * Create an instance of the Zermelo API with given school and access token
     *
     * @param school      code of school, for example apidemo when URL is
     *                    apidemo.zportal.nl
     * @param accessToken Access token for usage with this API.
     * @return instance of ZermeloAPI
     */
    public static ZermeloAPI getAPI(String school, String accessToken) {
        return ZermeloAPI.getAPI(school, accessToken, new ZermeloHttpClient());
    }

    /**
     * Create an instance of the Zermelo API with given school and access token
     *
     * @param school            code of school, for example apidemo when URL is
     *                          apidemo.zportal.nl
     * @param accessToken       Access token for usage with this API.
     * @param zermeloHttpClient Zermelo HTTP client
     * @return instance of ZermeloAPI
     */
    public static ZermeloAPI getAPI(String school, String accessToken, ZermeloHttpClient zermeloHttpClient) {
        return new ZermeloAPI(school, accessToken, zermeloHttpClient);
    }

    /**
     * Get the access token used in this API instance
     *
     * @return access token
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Get the school code used in this API instance
     *
     * @return school code
     */
    public String getSchool() {
        return this.school;
    }

    /**
     * Get the ZermeloHttpClient instance
     *
     * @return ZermeloHttpClient instance
     */
    public ZermeloHttpClient getZermeloHttpClient() {
        return this.zermeloHttpClient;
    }

    /**
     * Get appointments for own user using the appointmentparticipations endpoint
     *
     * @param year       year
     * @param weekNumber number of week
     * @return list of appointments
     */
    public List<AppointmentParticipation> getAppointmentParticipations(int year, int weekNumber) throws ZermeloApiException {
        return this.getAppointmentParticipations("~me", year, weekNumber);
    }

    /**
     * Get appointments for own user using the appointmentparticipations endpoint
     *
     * @param user       user
     * @param year       year
     * @param weekNumber number of week
     * @return list of appointments
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public List<AppointmentParticipation> getAppointmentParticipations(String user, int year, int weekNumber) throws ZermeloApiException {
        List<AppointmentParticipation> appointments = new ArrayList<>();

        // Zermelo requires week format in "<year:4><week:2>"
        String formattedWeekNumber = String.format("%02d", weekNumber);

        HttpResponse<String> response = this.getZermeloHttpClient().get("/appointmentparticipations", school, this.getAccessToken(),
                Map.of("student", user, "week", year + formattedWeekNumber, "fields", "id,start,end,startTimeSlotName,endTimeSlotName,subjects,teachers,groups,locations,appointmentType,schedulerRemark,cancelled,changeDescription"));

        JsonElement root = JsonParser.parseString(response.body());
        JsonObject rootObject = root.getAsJsonObject();

        JsonArray data = rootObject.get("response").getAsJsonObject()
                .get("data").getAsJsonArray();
        for (JsonElement appointmentElement : data) {
            JsonObject appointmentObj = appointmentElement.getAsJsonObject();

            long id = appointmentObj.get("id").getAsLong();
            long start = appointmentObj.get("start").getAsLong();
            long end = appointmentObj.get("end").getAsLong();
            String startTimeSlot = appointmentObj.get("startTimeSlotName").isJsonNull()
                    ? null
                    : appointmentObj.get("startTimeSlotName").getAsString();
            String endTimeSlot = appointmentObj.get("endTimeSlotName").isJsonNull()
                    ? null
                    : appointmentObj.get("endTimeSlotName").getAsString();

            List<String> subjects = StreamSupport.stream(appointmentObj.get("subjects").getAsJsonArray().spliterator(), true)
                    .map(JsonElement::getAsString)
                    .collect(Collectors.toList());

            List<String> teachers = StreamSupport.stream(appointmentObj.get("teachers").getAsJsonArray().spliterator(), true)
                    .map(JsonElement::getAsString)
                    .collect(Collectors.toList());

            List<String> groups = StreamSupport.stream(appointmentObj.get("groups").getAsJsonArray().spliterator(), true)
                    .map(JsonElement::getAsString)
                    .collect(Collectors.toList());

            List<String> locations = StreamSupport.stream(appointmentObj.get("locations").getAsJsonArray().spliterator(), true)
                    .map(JsonElement::getAsString)
                    .collect(Collectors.toList());

            AppointmentType appointmentType = AppointmentType
                    .getEnum(appointmentObj.get("appointmentType").getAsString());

            String remark = appointmentObj.get("schedulerRemark").getAsString();
            boolean cancelled = appointmentObj.get("cancelled").getAsBoolean();
            String changeDescription = appointmentObj.get("changeDescription").getAsString();

            appointments.add(new AppointmentParticipation(id, start, end, startTimeSlot, endTimeSlot, subjects, teachers,
                    groups, locations, appointmentType, remark, cancelled, changeDescription));
        }

        appointments.sort(new AppointmentComparator());
        return appointments;
    }

    /**
     * Get your own list of appointments
     *
     * @param startDate date to start looking
     * @param endDate   date to stop looking
     * @return List of appointments
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public List<Appointment> getAppointments(Date startDate, Date endDate) throws ZermeloApiException {
        return this.getAppointments("~me", startDate, endDate);
    }

    /**
     * Get list of appointments of provided user
     *
     * @param user      user
     * @param startDate date to start looking
     * @param endDate   date to stop looking
     * @return List of appointments
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public List<Appointment> getAppointments(String user, Date startDate, Date endDate) throws ZermeloApiException {
        List<Appointment> appointments = new ArrayList<>();

        HttpResponse<String> httpResponse = this.getZermeloHttpClient().get("/appointments", school, accessToken,
                Map.of("user", user,
                        "start", String.valueOf(startDate.getTime() / 1000),
                        "end", String.valueOf(endDate.getTime() / 1000)));
        JsonElement root = JsonParser.parseString(httpResponse.body());
        JsonObject rootObject = root.getAsJsonObject();

        JsonArray data = rootObject.get("response").getAsJsonObject().get("data").getAsJsonArray();
        for (JsonElement appointmentElement : data) {
            JsonObject appointmentObj = appointmentElement.getAsJsonObject();

            long id = appointmentObj.get("id").getAsLong();
            long start = appointmentObj.get("start").getAsLong();
            long end = appointmentObj.get("end").getAsLong();
            String startTimeSlot = appointmentObj.get("startTimeSlot").isJsonNull() ? null
                    : appointmentObj.get("startTimeSlot").getAsString();
            String endTimeSlot = appointmentObj.get("startTimeSlot").isJsonNull() ? null
                    : appointmentObj.get("endTimeSlot").getAsString();

            List<String> subjects = StreamSupport.stream(appointmentObj.get("subjects").getAsJsonArray().spliterator(), true)
                    .map(JsonElement::getAsString)
                    .collect(Collectors.toList());

            List<String> teachers = StreamSupport.stream(appointmentObj.get("teachers").getAsJsonArray().spliterator(), true)
                    .map(JsonElement::getAsString)
                    .collect(Collectors.toList());

            List<String> groups = StreamSupport.stream(appointmentObj.get("groups").getAsJsonArray().spliterator(), true)
                    .map(JsonElement::getAsString)
                    .collect(Collectors.toList());

            List<String> locations = StreamSupport.stream(appointmentObj.get("locations").getAsJsonArray().spliterator(), true)
                    .map(JsonElement::getAsString)
                    .collect(Collectors.toList());

            AppointmentType appointmentType = AppointmentType.getEnum(appointmentObj.get("type").getAsString());
            String remark = appointmentObj.get("remark").getAsString();

            boolean valid = appointmentObj.get("valid").getAsBoolean();
            boolean cancelled = appointmentObj.get("cancelled").getAsBoolean();
            boolean modified = appointmentObj.get("modified").getAsBoolean();
            boolean moved = appointmentObj.get("moved").getAsBoolean();
            boolean isNew = appointmentObj.get("new").getAsBoolean();

            String changeDescription = appointmentObj.get("changeDescription").getAsString();

            appointments.add(new Appointment(id, start, end, startTimeSlot, endTimeSlot, subjects, teachers,
                    groups, locations, appointmentType, remark, valid, cancelled, modified, moved, isNew,
                    changeDescription));
        }
        appointments.sort(new AppointmentComparator());
        return appointments;
    }

    /**
     * Get a list of currently visible announcements.
     *
     * @return list of announcements
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public List<Announcement> getAnnouncements() throws ZermeloApiException {
        return getAnnouncements("~me");
    }

    /**
     * Get a list of visible announcements for provided user
     *
     * @param user user
     * @return list of visible announcements for provided user
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public List<Announcement> getAnnouncements(String user) throws ZermeloApiException {
        HttpResponse<String> httpResponse = this.getZermeloHttpClient().get("/announcements", this.getSchool(), this.getAccessToken(),
                Map.of("user", user, "current", "true"));
        JsonElement root = JsonParser.parseString(httpResponse.body());
        JsonObject rootObject = root.getAsJsonObject();

        JsonArray data = rootObject.get("response").getAsJsonObject().get("data").getAsJsonArray();
        return StreamSupport.stream(data.spliterator(), true).map(announcementElement -> {
                    JsonObject announcementObj = announcementElement.getAsJsonObject();
                    long id = announcementObj.get("id").getAsLong();
                    long start = announcementObj.get("start").getAsLong();
                    long end = announcementObj.get("end").getAsLong();
                    String title = announcementObj.get("title").getAsString();
                    String text = announcementObj.get("text").getAsString();
                    return new Announcement(id, start, end, title, text);
                })
                .sorted(new AnnouncementComparator())
                .collect(Collectors.toList());
    }

    /**
     * Get the current user
     *
     * @return current user
     */
    public User getUser() {
        return new User(school, accessToken, "~me");
    }

    /**
     * Get a user by userCode
     *
     * @param user user
     * @return current user
     */
    public User getUser(String user) {
        return new User(school, accessToken, user);
    }
}
