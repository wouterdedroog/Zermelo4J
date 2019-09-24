package nl.mrwouter.zermelo4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nl.mrwouter.zermelo4j.annoucements.Announcement;
import nl.mrwouter.zermelo4j.annoucements.AnnouncementComparator;
import nl.mrwouter.zermelo4j.appointments.Appointment;
import nl.mrwouter.zermelo4j.appointments.AppointmentComparator;
import nl.mrwouter.zermelo4j.appointments.AppointmentType;

/**
 * Main class for Zermelo4J
 */
public class ZermeloAPI {

	/**
	 * Create an instance of the Zermelo API with given school and access token
	 * 
	 * @param school      code of school, for example apidemo when URL is
	 *                    apidemo.zportal.nl
	 * @param accessToken Access token for usage with this API.
	 * @return instance of ZermeloAPI
	 */
	public static ZermeloAPI getAPI(String school, String accessToken) {
		return new ZermeloAPI(school, accessToken);
	}

	/**
	 * Get a valid access token with given 'Koppel App' code.
	 * 
	 * @param school   code of school, for example apidemo when URL is
	 *                 apidemo.zportal.nl
	 * @param authCode Code that can be aquired at the 'Koppel App' page.
	 * @return Valid access token for use with {@link #getAPI}.
	 * @throws IOException When the schoolname or auth code is not valid.
	 */
	public static String getAccessToken(String school, String authCode) throws IOException {
		HttpsURLConnection con = (HttpsURLConnection) new URL("https://" + school + ".zportal.nl/api/v3/oauth/token")
				.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.setDoInput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes("grant_type=authorization_code&code=" + authCode);
		wr.close();

		InputStreamReader reader = new InputStreamReader((InputStream) con.getContent());
		JsonElement root = new JsonParser().parse(reader);
		JsonObject rootobj = root.getAsJsonObject();
		reader.close();

		return rootobj.get("access_token").getAsString();
	}

	private String school, accessToken;

	public ZermeloAPI(String school, String accessToken) {
		this.school = school;
		this.accessToken = accessToken;
	}

	/**
	 * Get the access token used in this API instance
	 * 
	 * @return access token
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Get the school code used in this API instance
	 * 
	 * @return school code
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * Get appointments for own user using the appointmentparticipations endpoint
	 * 
	 * @param year       year
	 * @param weeknumber number of week
	 * @return list of appointments
	 */
	public List<Appointment> getAppointmentParticipations(int year, int weeknumber) {
		return getAppointmentParticipations("~me", year, weeknumber);
	}

	/**
	 * Get appointments for own user using the appointmentparticipations endpoint
	 * 
	 * @param user       user
	 * @param year       year
	 * @param weeknumber number of week
	 * @return list of appointments
	 */
	public List<Appointment> getAppointmentParticipations(String user, int year, int weeknumber) {
		List<Appointment> appointments = new ArrayList<>();

		try {
			// Time gets divided by 1000 because it's epoch time in seconds.
			HttpsURLConnection con = (HttpsURLConnection) new URL("https://" + school
					+ ".zportal.nl/api/v3/appointmentparticipations?student=" + user + "&week=" + year + weeknumber
					+ "&fields=id,appointmentInstance,studentInDepartment,optional,studentEnrolled,attendanceScheduler,attendanceParticipationCoordinator,plannedAttendance,realizedAttendance,publicComment,start,end,subjects,teachers,locations,groups,schedulerRemark,changeDescription,startTimeSlotName,endTimeSlotName,allowedStudentActions,availableSpace,cancelled,appointmentType,content")
							.openConnection();
			con.addRequestProperty("Authorization", "Bearer " + accessToken);
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
			JsonObject rootobj = root.getAsJsonObject();

			streamReader.close();
			reader.close();

			JsonArray data = rootobj.get("response").getAsJsonObject().get("data").getAsJsonArray();
			for (JsonElement appointmentElement : data) {
				JsonObject appointmentObj = appointmentElement.getAsJsonObject();

				long id = appointmentObj.get("id").getAsLong();
				long start = appointmentObj.get("start").getAsLong();
				long end = appointmentObj.get("end").getAsLong();
				String startTimeSlot = appointmentObj.get("startTimeSlotName").isJsonNull() ? "?"
						: appointmentObj.get("startTimeSlotName").getAsString();
				String endTimeSlot = appointmentObj.get("endTimeSlotName").isJsonNull() ? "?"
						: appointmentObj.get("endTimeSlotName").getAsString();

				// I can't call #stream() on a JsonArray, so I'll stick with this for now.
				List<String> subjects = new ArrayList<>();
				for (JsonElement subject : appointmentObj.get("subjects").getAsJsonArray()) {
					subjects.add(subject.getAsString());
				}

				List<String> teachers = new ArrayList<>();
				for (JsonElement teacher : appointmentObj.get("teachers").getAsJsonArray()) {
					teachers.add(teacher.getAsString());
				}

				List<String> groups = new ArrayList<>();
				for (JsonElement group : appointmentObj.get("groups").getAsJsonArray()) {
					groups.add(group.getAsString());
				}

				List<String> locations = new ArrayList<>();
				for (JsonElement location : appointmentObj.get("locations").getAsJsonArray()) {
					locations.add(location.getAsString());
				}

				AppointmentType appointmentType = AppointmentType
						.getEnum(appointmentObj.get("appointmentType").getAsString());

				// schedulerRemark is 'remark' in the appointments endpoint, this is just a
				// guess.
				String remark = appointmentObj.get("schedulerRemark").getAsString();

				boolean cancelled = appointmentObj.get("cancelled").getAsBoolean();

				String changeDescription = appointmentObj.get("changeDescription").getAsString();

				appointments.add(new Appointment(true, id, start, end, startTimeSlot, endTimeSlot, subjects, teachers,
						groups, locations, appointmentType, remark, null, cancelled, null, null, null,
						changeDescription));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Collections.sort(appointments, new AppointmentComparator());
		return appointments;
	}

	/**
	 * Get your own list of appointments
	 * 
	 * @param startDate date to start looking
	 * @param endDate   date to stop looking
	 * @return List of appointments
	 */
	public List<Appointment> getAppointments(Date startDate, Date endDate) {
		return getAppointments("~me", startDate, endDate);
	}

	/**
	 * Get list of appointments of provided user
	 * 
	 * @param user      user
	 * @param startDate date to start looking
	 * @param endDate   date to stop looking
	 * @return List of appointments
	 */
	public List<Appointment> getAppointments(String user, Date startDate, Date endDate) {
		List<Appointment> appointments = new ArrayList<>();

		try {
			// Time gets divided by 1000 because it's epoch time in seconds.
			HttpsURLConnection con = (HttpsURLConnection) new URL("https://" + school
					+ ".zportal.nl/api/v3/appointments?user=" + user + "&start=" + (startDate.getTime() / 1000)
					+ "&end=" + (endDate.getTime() / 1000) + "&access_token=" + accessToken).openConnection();
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
			JsonObject rootobj = root.getAsJsonObject();

			streamReader.close();
			reader.close();

			JsonArray data = rootobj.get("response").getAsJsonObject().get("data").getAsJsonArray();
			for (JsonElement appointmentElement : data) {
				JsonObject appointmentObj = appointmentElement.getAsJsonObject();

				long id = appointmentObj.get("id").getAsLong();
				long start = appointmentObj.get("start").getAsLong();
				long end = appointmentObj.get("end").getAsLong();
				String startTimeSlot = appointmentObj.get("startTimeSlot").isJsonNull() ? "?"
						: appointmentObj.get("startTimeSlot").getAsString();
				String endTimeSlot = appointmentObj.get("startTimeSlot").isJsonNull() ? "?"
						: appointmentObj.get("endTimeSlot").getAsString();

				// I can't call #stream() on a JsonArray, so I'll stick with this for now.
				List<String> subjects = new ArrayList<>();
				for (JsonElement subject : appointmentObj.get("subjects").getAsJsonArray()) {
					subjects.add(subject.getAsString());
				}

				List<String> teachers = new ArrayList<>();
				for (JsonElement teacher : appointmentObj.get("teachers").getAsJsonArray()) {
					teachers.add(teacher.getAsString());
				}

				List<String> groups = new ArrayList<>();
				for (JsonElement group : appointmentObj.get("groups").getAsJsonArray()) {
					groups.add(group.getAsString());
				}

				List<String> locations = new ArrayList<>();
				for (JsonElement location : appointmentObj.get("locations").getAsJsonArray()) {
					locations.add(location.getAsString());
				}

				AppointmentType appointmentType = AppointmentType.getEnum(appointmentObj.get("type").getAsString());
				String remark = appointmentObj.get("remark").getAsString();

				boolean valid = appointmentObj.get("valid").getAsBoolean();
				boolean cancelled = appointmentObj.get("cancelled").getAsBoolean();
				boolean modified = appointmentObj.get("modified").getAsBoolean();
				boolean moved = appointmentObj.get("moved").getAsBoolean();
				boolean isNew = appointmentObj.get("new").getAsBoolean();

				String changeDescription = appointmentObj.get("changeDescription").getAsString();

				appointments.add(new Appointment(false, id, start, end, startTimeSlot, endTimeSlot, subjects, teachers,
						groups, locations, appointmentType, remark, valid, cancelled, modified, moved, isNew,
						changeDescription));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Collections.sort(appointments, new AppointmentComparator());
		return appointments;
	}

	/**
	 * Get a list of currently visible announcements.
	 * 
	 * @return list of announcements
	 */
	public List<Announcement> getAnnouncements() {
		return getAnnouncements("~me");
	}

	/**
	 * Get a list of visible announcements for provided user
	 * 
	 * @param user user
	 * @return list of visible announcements for provided user
	 */
	public List<Announcement> getAnnouncements(String user) {
		List<Announcement> announcements = new ArrayList<>();

		try {
			HttpsURLConnection con = (HttpsURLConnection) new URL(
					"https://" + school + ".zportal.nl/api/v3/announcements?user=" + user + "&current=true"
							+ "&access_token=" + accessToken).openConnection();
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
			JsonObject rootobj = root.getAsJsonObject();

			streamReader.close();
			reader.close();

			JsonArray data = rootobj.get("response").getAsJsonObject().get("data").getAsJsonArray();
			for (JsonElement announcementElement : data) {
				JsonObject announcementObj = announcementElement.getAsJsonObject();
				long id = announcementObj.get("id").getAsLong();
				long start = announcementObj.get("start").getAsLong();
				long end = announcementObj.get("end").getAsLong();
				String title = announcementObj.get("title").getAsString();
				String text = announcementObj.get("text").getAsString();
				announcements.add(new Announcement(id, start, end, title, text));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Collections.sort(announcements, new AnnouncementComparator());
		return announcements;
	}
}
