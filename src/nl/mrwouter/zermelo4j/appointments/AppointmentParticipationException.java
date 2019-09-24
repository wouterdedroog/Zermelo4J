package nl.mrwouter.zermelo4j.appointments;

public class AppointmentParticipationException extends Exception {

	// Randomly generated ID.
	private static final long serialVersionUID = 67187906246842591L;

	/**
	 * Appointment participation message
	 * @param message exception message to be thrown
	 */
	public AppointmentParticipationException(String message) {
		super(message);
	}

}
