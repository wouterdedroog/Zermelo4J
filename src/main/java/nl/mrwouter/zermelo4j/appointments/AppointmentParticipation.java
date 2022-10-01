package nl.mrwouter.zermelo4j.appointments;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

/**
 * AppointmentParticipation object
 */
public class AppointmentParticipation {

    private final long id;
    private final long start;
    private final long end;
    private final List<String> subjects;
    private final List<String> teachers;
    private final List<String> groups;
    private final List<String> locations;
    private final String remark;
    private final String changeDescription;
    private final String startTimeSlot;
    private final String endTimeSlot;
    private final Boolean cancelled;
    private final AppointmentType appointmentType;

    public AppointmentParticipation(long id, long start, long end, String startTimeSlot, String endTimeSlot,
                       List<String> subjects, List<String> teachers, List<String> groups, List<String> locations,
                       AppointmentType appointmentType, String remark, Boolean cancelled, String changeDescription) {
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
        this.cancelled = cancelled;
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
     * Get the starting timeslot of this appointment. Returns null when not provided.
     *
     * @return start time slot
     */
    public String getStartTimeSlot() {
        return startTimeSlot;
    }

    /**
     * Get the ending timeslot of this appointment. Returns null when not provided.
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
     * Check if appointment is cancelled
     *
     * @return true if appointment is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
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
     * Return this AppointmentParticipation object as a JsonObject
     * @return JsonObject
     */
    public JsonObject toJson() {
        JsonObject appointment = new JsonObject();
        appointment.addProperty("id", id);
        appointment.addProperty("start", start);
        appointment.addProperty("end", end);
        appointment.addProperty("startTimeSlot", startTimeSlot);

        appointment.addProperty("endTimeSlot", endTimeSlot);
        appointment.addProperty("teachers", String.join(",", teachers));
        appointment.addProperty("subjects", String.join(",", subjects));
        appointment.addProperty("groups", String.join(",", groups));
        appointment.addProperty("locations", String.join(",", locations));
        appointment.addProperty("appointmentType", appointmentType.name());
        appointment.addProperty("remark", remark);
        appointment.addProperty("cancelled", cancelled);
        appointment.addProperty("changeDescription", changeDescription);
        return appointment;
    }

    /**
     * Added custom #toString() method for easy debugging.
     */
    @Override
    public String toString() {
        return this.toJson().toString();
    }
}
