package nl.mrwouter.zermelo4j.users;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class User {
    String accessToken;
    String school;
    String userCode;


    public User(String school, String accessToken, String userCode) {
        this.accessToken = accessToken;
        this.school = school;
        this.userCode = userCode;
    }

    public String getFirstName() {
        return (String) getData("firstName");
    }

    public String getLastName() {
        return (String) getData("lastName");
    }

    public String getName() {
        return (String) getData("firstName") + " " + getData("lastName");
    }

    public String getUserCode() {
        return (String) getData("code");
    }

    public String getPrefix() {
        return (String) getData("prefix");
    }

    public boolean isApplicationManager() {
        return (boolean) getData("isApplicationManager");
    }

    public boolean isArchived() {
        return (boolean) getData("archived");
    }

    public boolean hasPassword() {
        return (boolean) getData("hasPassword");
    }

    public boolean isStudent() {
        return (boolean) getData("isStudent");
    }

    public boolean isEmployee() {
        return (boolean) getData("isEmployee");
    }

    public boolean isFamilyMember() {
        return (boolean) getData("isFamilyMember");
    }

    public boolean isSchoolScheduler() {
        return (boolean) getData("isSchoolScheduler");
    }

    public boolean isSchoolLeader() {
        return (boolean) getData("isSchoolLeader");
    }

    public boolean isStudentAdministrator() {
        return (boolean) getData("isStudentAdministrator");
    }

    public boolean isTeamLeader() {
        return (boolean) getData("isTeamLeader");
    }

    public boolean isSectionLeader() {
        return (boolean) getData("isSectionLeader");
    }

    public boolean isMentor() {
        return (boolean) getData("isMentor");
    }

    public boolean isDean() {
        return (boolean) getData("isDean");
    }

    private Object getData(String field) {
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

            return data.get(0).getAsJsonObject().get(field);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
