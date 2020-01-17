package nl.mrwouter.zermelo4j.users;

import com.google.gson.*;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.AccessDeniedException;

public class User {
    private String school, accessToken;

    private String user, firstName, lastName, prefix;
    private boolean isArchived, hasPassword, isApplicationManager, isStudent, isEmployee, isFamilyMember, isSchoolScheduler, isSchoolLeader, isStudentAdministrator, isTeamLeader, isSectionLeader, isMentor, isDean;

    public User(String school, String accessToken, String user) {
        this.school = school;
        this.accessToken = accessToken;

        JsonObject data = getData(user);
        this.user = user;
        this.firstName = getField(data, "firstName");
        this.lastName = getField(data, "lastName");
        this.prefix = getField(data, "prefix");
        this.isArchived = data.get("archived").getAsBoolean();
        this.hasPassword = data.get("hasPassword").getAsBoolean();
        this.isApplicationManager = data.get("isApplicationManager").getAsBoolean();
        this.isStudent = data.get("isStudent").getAsBoolean();
        this.isEmployee = data.get("isEmployee").getAsBoolean();
        this.isFamilyMember = data.get("isFamilyMember").getAsBoolean();
        this.isSchoolScheduler = data.get("isSchoolScheduler").getAsBoolean();
        this.isSchoolLeader = data.get("isSchoolLeader").getAsBoolean();
        this.isStudentAdministrator = data.get("isStudentAdministrator").getAsBoolean();
        this.isTeamLeader = data.get("isTeamLeader").getAsBoolean();
        this.isSectionLeader = data.get("isSectionLeader").getAsBoolean();
        this.isMentor = data.get("isMentor").getAsBoolean();
        this.isDean = data.get("isDean").getAsBoolean();
    }

    /**
     * Get the user identifier
     *
     * @return identifier of user
     */
    public String getUser() {
        return user;
    }

    /**
     * Get the first name of the user
     *
     * @return first name of user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name of the user
     *
     * @return last name of user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the full name of the user
     *
     * @return full name of user
     */
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * Get the prefix of the user
     *
     * @return prefix of user
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get whether the user is archived
     *
     * @return whether the user is archived
     */
    public boolean isArchived() {
        return isArchived;
    }

    /**
     * Get whether the user has a password to login
     *
     * @return whether the user has a password
     */
    public boolean isHasPassword() {
        return hasPassword;
    }

    /**
     * Get whether the user is an application manager
     *
     * @return whether the user is an application manager
     */
    public boolean isApplicationManager() {
        return isApplicationManager;
    }

    /**
     * Get whether the user is a student
     *
     * @return whether the user is a student
     */
    public boolean isStudent() {
        return isStudent;
    }

    /**
     * Get whether the user is an employee
     *
     * @return whether the user is an employee
     */
    public boolean isEmployee() {
        return isEmployee;
    }

    /**
     * Get whether the user is a family member
     *
     * @return whether the user is a family member
     */
    public boolean isFamilyMember() {
        return isFamilyMember;
    }

    /**
     * Get whether the user is a school scheduler
     *
     * @return whether the user is a school scheduler
     */
    public boolean isSchoolScheduler() {
        return isSchoolScheduler;
    }

    /**
     * Get whether the user is a school leader
     *
     * @return whether the user is a school leader
     */
    public boolean isSchoolLeader() {
        return isSchoolLeader;
    }

    /**
     * Get whether the user is a student administrator
     *
     * @return whether the user is a student administrator
     */
    public boolean isStudentAdministrator() {
        return isStudentAdministrator;
    }

    /**
     * Get whether the user is a team leader
     *
     * @return whether the user is a team leader
     */
    public boolean isTeamLeader() {
        return isTeamLeader;
    }

    /**
     * Get whether the user is a section leader
     *
     * @return whether the user is a section leader
     */
    public boolean isSectionLeader() {
        return isSectionLeader;
    }

    /**
     * Get whether the user is a mentor
     *
     * @return whether the user is a mentor
     */
    public boolean isMentor() {
        return isMentor;
    }

    /**
     * Get whether the user is a dean
     *
     * @return whether the user is a dean
     */
    public boolean isDean() {
        return isDean;
    }

    private JsonObject getData(String user) {
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(
                    "https://" + school + ".zportal.nl/api/v3/users/" + user + "?access_token=" + accessToken
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

            JsonElement response = (new JsonParser().parse(streamReader)).getAsJsonObject().get("response");
            if (response.getAsJsonObject().get("status").getAsInt() == 403) throw new AccessDeniedException(
                    "You don't have enough permissions to view user '" + user + "' or this user doesn't exist.");

            JsonArray data = response.getAsJsonObject().get("data").getAsJsonArray();

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
