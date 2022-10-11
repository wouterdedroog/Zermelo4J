package nl.mrwouter.zermelo4j.appointments;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Appointment object
 */
public class Appointment extends AppointmentParticipation {

    private final boolean valid;
    private final boolean modified;
    private final boolean moved;
    private final boolean isNew;
    private final boolean isOptional;

    /**
     * Create an Appointment from a provided JsonObject
     * @param appointmentObject JSON data as returned from the Zermelo API. See {@link nl.mrwouter.zermelo4j.ZermeloAPI#getAppointments(Date, Date)}
     */
    public Appointment(JsonObject appointmentObject) {
        super(appointmentObject);
        this.valid = appointmentObject.get("valid").getAsBoolean();
        this.modified = appointmentObject.get("modified").getAsBoolean();
        this.moved = appointmentObject.get("moved").getAsBoolean();
        this.isNew = appointmentObject.get("new").getAsBoolean();
        this.isOptional = appointmentObject.get("optional").getAsBoolean();
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
     * Check if the appointment is optional
     *
     * @return true if appointment is optional
     */
    public boolean isOptional() {
        return isOptional;
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
        appointment.addProperty("optional", this.isOptional());
        return appointment;
    }
}
