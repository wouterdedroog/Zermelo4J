package nl.mrwouter.zermelo4j;

import java.util.Date;

import nl.mrwouter.zermelo4j.annoucements.Announcement;
import nl.mrwouter.zermelo4j.appointments.Appointment;

public class ZermeloAPITester {

	public static void main(String args[]) {
		// Access token can be created by using ZermeloAPI#getAccessToken("[school]", "[koppel app code]");

		//System.out.println(ZermeloAPI.getAccessToken("stijnvanderkolk", "957851145752"));
		ZermeloAPI api = ZermeloAPI.getAPI("stijnvanderkolk", "q4llre3us0dr2h8e8cuk1pv26t");

		Date endDate = new Date();
		endDate.setTime(endDate.getTime() - 432000000l);
		
		for (Appointment app: api.getAppointments(endDate, new Date())) {
			System.out.println(app.toString());
		}
		for (Announcement ann: api.getAnnouncements()) {
			System.out.println(ann.toString());
		}
	}
}
