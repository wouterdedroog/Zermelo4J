package nl.mrwouter.zermelo4j.appointments;

import java.util.Comparator;

/**
 * Comparator used to make sure that all events are sorted from start to beginning.
 *
 */
public class AppointmentComparator implements Comparator<Appointment> {

	@Override
	public int compare(Appointment app1, Appointment app2) {
        if (app1.getStart() == app2.getStart()) {
            return 0;
        }
        if (app1.getStart() < app2.getStart()) {
            return -1;
        }
        return 1;
	}	
}
