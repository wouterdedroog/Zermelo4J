package nl.mrwouter.zermelo4j.appointments;

import java.util.List;

import com.google.gson.JsonObject;

/**
 * Appointment object for easy overview
 */
public class Appointment {

	private long id, start, end;
	private List<String> subjects, teachers, groups, locations;
	private String remark, changeDescription, startTimeSlot, endTimeSlot;
	private Boolean isParticipation, valid, cancelled, modified, moved, isNew;
	private AppointmentType appointmentType;

	public Appointment(boolean isParticipation, long id, long start, long end, String startTimeSlot, String endTimeSlot,
			List<String> subjects, List<String> teachers, List<String> groups, List<String> locations,
			AppointmentType appointmentType, String remark, Boolean valid, Boolean cancelled, Boolean modified,
			Boolean moved, Boolean isNew, String changeDescription) {
		this.isParticipation = isParticipation;
		this.id = id;
		this.start = start;
		this.end = end;
		this.startTimeSlot = startTimeSlot;
		this.endTimeSlot = endTimeSlot;
		this.subjects = subjects;
		this.teachers = teachers;
		this.groups = groups;
		this.locations = locations;
		this.appointmentType = appointmentType;
		this.remark = remark;
		this.valid = valid;
		this.cancelled = cancelled;
		this.modified = modified;
		this.moved = moved;
		this.isNew = isNew;
		this.changeDescription = changeDescription;
	}

	/**
	 * Get the ID of this appointment
	 * 
	 * @return id of this appointment
	 */
	public long getId() {
		return id;
	}

	/**
	 * Get the appointment type
	 * 
	 * @return appointment type
	 */
	public AppointmentType getType() {
		return appointmentType;
	}

	/**
	 * Get the starting timeslot of this appointment
	 * 
	 * @return start time slot
	 */
	public String getStartTimeSlot() {
		return startTimeSlot;
	}

	/**
	 * Get the ending timeslot of this appointment
	 * 
	 * @return end time slot
	 */
	public String getEndTimeSlot() {
		return endTimeSlot;
	}

	/**
	 * Get the time when this lesson starts (Seconds since epoch)
	 * 
	 * @return time when this appointment starts
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Get the time when this appointment starts (Seconds since epoch)
	 * 
	 * @return time when this appointment ends
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * Get a list of subjects for this appointment
	 * 
	 * @return array of subjects
	 */
	public List<String> getSubjects() {
		return subjects;
	}

	/**
	 * Get a list of teachers for this appointment
	 * 
	 * @return list of teachers
	 */
	public List<String> getTeachers() {
		return teachers;
	}

	/**
	 * Get a list of groups for this appointment
	 * 
	 * @return list of groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * Get a list of locations for this appointment
	 * 
	 * @return list of locations
	 */
	public List<String> getLocations() {
		return locations;
	}

	/**
	 * Remark for this appointment (For example: "Don't forget your books")
	 * 
	 * @return remark for this appointment
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * From Zermelo API docs: 'True if this appointment is part of the most
	 * up-to-date schedule. Note that both valid and cancelled can be true, in which
	 * case the appointment has been cancelled and attendance is not required nor
	 * possible.'
	 * 
	 * @throws AppointmentParticipationException when using
	 *                                           getAppointmentParticipations
	 * @return true if appointment is valid
	 */
	public boolean isValid() throws AppointmentParticipationException {
		if (isParticipation) {
			throw new AppointmentParticipationException("Can't invoke isValid when using getAppointmentParticipations");
		}
		return valid;
	}

	/**
	 * Check if appointment is cancelled
	 * 
	 * @return true if appointment is cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Check if appointment is modified. When using the
	 * {@link nl.mrwouter.zermelo4j.ZermeloAPI#getAppointmentParticipations(String, int, int)}
	 * method, isModified is only a check if {@link #getChangeDescription()} is
	 * empty.
	 * 
	 * @return true if appointment is modified
	 */
	public boolean isModified() {
		return isParticipation ? !changeDescription.isEmpty() : modified;
	}

	/**
	 * Check if the appointment has moved
	 * 
	 * @throws AppointmentParticipationException when using
	 *                                           getAppointmentParticipations
	 * @return true if appointment has moved
	 */
	public boolean isMoved() throws AppointmentParticipationException {
		if (isParticipation) {
			throw new AppointmentParticipationException("Can't invoke isValid when using getAppointmentParticipations");
		}
		return moved;
	}

	/**
	 * From Zermelo API docs: 'True if this appointment has been added and was not
	 * originally scheduled.'
	 * 
	 * @throws AppointmentParticipationException when using
	 *                                           getAppointmentParticipations
	 * 
	 * @return true if appointment is new
	 */
	public boolean isNew() throws AppointmentParticipationException {
		if (isParticipation) {
			throw new AppointmentParticipationException("Can't invoke isValid when using getAppointmentParticipations");
		}
		return isNew;
	}

	/**
	 * Get the description of the change.
	 * 
	 * @return textual description of the change.
	 */
	public String getChangeDescription() {
		return changeDescription;
	}

	/**
	 * Added custom {@link #toString()} method because easy debugging.
	 */
	@Override
	public String toString() {
		JsonObject appointment = new JsonObject();
		appointment.addProperty("id", id);
		appointment.addProperty("start", start);
		appointment.addProperty("end", end);
		appointment.addProperty("startTimeSlot", startTimeSlot);

		appointment.addProperty("endTimeSlot", endTimeSlot);
		appointment.addProperty("teachers", teachers.toString().replace("[", "").replace("]", ""));
		appointment.addProperty("subjects", subjects.toString().replace("[", "").replace("]", ""));
		appointment.addProperty("groups", groups.toString().replace("[", "").replace("]", ""));
		appointment.addProperty("locations", locations.toString().replace("[", "").replace("]", ""));
		appointment.addProperty("appointmentType", appointmentType.toString().replace("[", "").replace("]", ""));
		appointment.addProperty("remark", remark);
		appointment.addProperty("valid", (isParticipation ? "?" : "" + valid));
		appointment.addProperty("cancelled", cancelled);
		appointment.addProperty("modified", (isParticipation ? "?" : "" + modified));
		appointment.addProperty("moved", (isParticipation ? "?" : "" + moved));
		appointment.addProperty("new", (isParticipation ? "?" : "" + isNew));
		appointment.addProperty("changeDescription", changeDescription);

		return appointment.toString();
	}
}
