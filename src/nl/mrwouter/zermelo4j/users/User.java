package nl.mrwouter.zermelo4j.users;

import com.google.gson.*;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class User {
    private static String accessToken = null;
    private static String school = null;

    private String userCode;
    private String firstName;
    private String lastName;
    private String prefix;
    private boolean isArchived;
    private boolean hasPassword;
    private boolean isApplicationManager;
    private boolean isStudent;
    private boolean isEmployee;
    private boolean isFamilyMember;
    private boolean isSchoolScheduler;
    private boolean isSchoolLeader;
    private boolean isStudentAdministrator;
    private boolean isTeamLeader;
    private boolean isSectionLeader;
    private boolean isMentor;
    private boolean isDean;


    public static void setup(String school, String accessToken) {
        User.accessToken = accessToken;
        User.school = school;
    }

    public User(String userCode) {
        if (accessToken == null || school == null)
            throw new RuntimeException("ZermeloAPI has to be constructed before you can use the User class.");

        JsonObject data = getData(userCode);
        this.userCode               = userCode;
        this.firstName              = getField(data, "firstName");
        this.lastName               = getField(data, "lastName");
        this.prefix                 = getField(data, "prefix");
        this.isArchived             = data.get("archived").getAsBoolean();
        this.hasPassword            = data.get("hasPassword").getAsBoolean();
        this.isApplicationManager   = data.get("isApplicationManager").getAsBoolean();
        this.isStudent              = data.get("isStudent").getAsBoolean();
        this.isEmployee             = data.get("isEmployee").getAsBoolean();
        this.isFamilyMember         = data.get("isFamilyMember").getAsBoolean();
        this.isSchoolScheduler      = data.get("isSchoolScheduler").getAsBoolean();
        this.isSchoolLeader         = data.get("isSchoolLeader").getAsBoolean();
        this.isStudentAdministrator = data.get("isStudentAdministrator").getAsBoolean();
        this.isTeamLeader           = data.get("isTeamLeader").getAsBoolean();
        this.isSectionLeader        = data.get("isSectionLeader").getAsBoolean();
        this.isMentor               = data.get("isMentor").getAsBoolean();
        this.isDean                 = data.get("isDean").getAsBoolean();
    }

    public String getUserCode() {
        return userCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public boolean isApplicationManager() {
        return isApplicationManager;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public boolean isEmployee() {
        return isEmployee;
    }

    public boolean isFamilyMember() {
        return isFamilyMember;
    }

    public boolean isSchoolScheduler() {
        return isSchoolScheduler;
    }

    public boolean isSchoolLeader() {
        return isSchoolLeader;
    }

    public boolean isStudentAdministrator() {
        return isStudentAdministrator;
    }

    public boolean isTeamLeader() {
        return isTeamLeader;
    }

    public boolean isSectionLeader() {
        return isSectionLeader;
    }

    public boolean isMentor() {
        return isMentor;
    }

    public boolean isDean() {
        return isDean;
    }

    private JsonObject getData(String userCode) {
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(
                    "https://" + school + ".zportal.nl/api/v3/users/" + userCode + "?access_token=" + accessToken
            ).openConnection();
            con.setRequestMethod("GET");
            InputStream inputStream = null;

            try {
                inputStream = con.getInputStream();
            } catch (IOException exception) {
                inputStream = con.getErrorStream();
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader streamReader = new BufferedReader(reader);

            JsonElement root = new JsonParser().parse(streamReader);
            JsonArray data = root.getAsJsonObject().get("response").getAsJsonObject().get("data").getAsJsonArray();

            streamReader.close();
            reader.close();
            return data.get(0).getAsJsonObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getField(JsonObject object, String field) {
        if (object.get(field).isJsonNull()) return null;
        return object.get(field).getAsString();
    }
}
