package nl.mrwouter.zermelo4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.AccessDeniedException;
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
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
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
        // Zermelo requires week format in "<year:4><week:2>"
        String formattedWeekNumber = String.format("%02d", weekNumber);

        HttpResponse<String> response = this.getZermeloHttpClient().get("/appointmentparticipations", school, this.getAccessToken(),
                Map.of("student", user, "week", year + formattedWeekNumber, "fields", "id,start,end,startTimeSlotName,endTimeSlotName,subjects,teachers,groups,locations,appointmentType,schedulerRemark,cancelled,changeDescription"));

        JsonElement root = JsonParser.parseString(response.body());
        JsonObject rootObject = root.getAsJsonObject();

        return StreamSupport.stream(rootObject.get("response").getAsJsonObject().get("data").getAsJsonArray().spliterator(), true)
                .map(JsonElement::getAsJsonObject)
                .map(AppointmentParticipation::new)
                .sorted(new AppointmentComparator())
                .collect(Collectors.toList());
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
        HttpResponse<String> httpResponse = this.getZermeloHttpClient().get("/appointments", school, accessToken,
                Map.of("user", user,
                        "start", String.valueOf(startDate.getTime() / 1000),
                        "end", String.valueOf(endDate.getTime() / 1000)));
        JsonElement root = JsonParser.parseString(httpResponse.body());
        JsonObject rootObject = root.getAsJsonObject();

        return StreamSupport.stream(rootObject.get("response").getAsJsonObject().get("data").getAsJsonArray().spliterator(), true)
                .map(JsonElement::getAsJsonObject)
                .map(Appointment::new)
                .sorted(new AppointmentComparator())
                .collect(Collectors.toList());
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
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public User getUser() throws ZermeloApiException {
        return this.getUser("~me");
    }

    /**
     * Get a user by userCode
     *
     * @param user user
     * @return current user
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public User getUser(String user) throws ZermeloApiException {
        HttpResponse<String> httpResponse = this.getZermeloHttpClient().get("/users/" + user, this.getSchool(), this.getAccessToken(), Collections.emptyMap());
        JsonElement root = JsonParser.parseString(httpResponse.body());
        System.out.println(root);
        JsonObject responseObject = root.getAsJsonObject().get("response").getAsJsonObject();
        JsonObject data = responseObject.get("data").getAsJsonArray().get(0).getAsJsonObject();

        String firstName = !data.get("firstName").isJsonNull()
                ? data.get("firstName").getAsString()
                : null;
        String lastName = !data.get("lastName").isJsonNull()
                ? data.get("lastName").getAsString()
                : null;
        String prefix = !data.get("prefix").isJsonNull()
                ? data.get("prefix").getAsString()
                : null;

        return new User(user, firstName, lastName, prefix, data.get("archived").getAsBoolean(), data.get("hasPassword").getAsBoolean(),
                data.get("isApplicationManager").getAsBoolean(), data.get("isStudent").getAsBoolean(), data.get("isEmployee").getAsBoolean(),
                data.get("isFamilyMember").getAsBoolean(), data.get("isSchoolScheduler").getAsBoolean(), data.get("isSchoolLeader").getAsBoolean(),
                data.get("isStudentAdministrator").getAsBoolean(), data.get("isTeamLeader").getAsBoolean(), data.get("isSectionLeader").getAsBoolean(),
                data.get("isMentor").getAsBoolean(), data.get("isDean").getAsBoolean());
    }
}
