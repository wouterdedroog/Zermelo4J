package nl.mrwouter.zermelo4j.appointments;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

/**
 * Appointment object
 */
public class Appointment extends AppointmentParticipation {

    private final boolean valid;
    private final boolean modified;
    private final boolean moved;
    private final boolean isNew;

    public Appointment(long id, long start, long end, String startTimeSlot, String endTimeSlot,
                       List<String> subjects, List<String> teachers, List<String> groups, List<String> locations,
                       AppointmentType appointmentType, String remark, boolean valid, boolean cancelled, boolean modified,
                       boolean moved, boolean isNew, String changeDescription) {
        super(id, start, end, startTimeSlot, endTimeSlot, subjects, teachers, groups, locations, appointmentType, remark, cancelled, changeDescription);
        this.valid = valid;
        this.modified = modified;
        this.moved = moved;
        this.isNew = isNew;
    }

    /**
     * From Zermelo API docs: 'True if this appointment is part of the most
     * up-to-date schedule. Note that both valid and cancelled can be true, in which
     * case the appointment has been cancelled and attendance is not required nor
     * possible.'
     *
     * @return true if appointment is valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Check if appointment is modified.
     *
     * @return true if appointment is modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Check if the appointment has moved
     *
     * @return true if appointment has moved
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * From Zermelo API docs: 'True if this appointment has been added and was not
     * originally scheduled.'
     *
     * @return true if appointment is new
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonObject toJson() {
        JsonObject appointment = super.toJson();
        appointment.addProperty("valid", this.isValid());
        appointment.addProperty("modified", this.isModified());
        appointment.addProperty("moved", this.isMoved());
        appointment.addProperty("new", this.isNew());
        return appointment;
    }
}
