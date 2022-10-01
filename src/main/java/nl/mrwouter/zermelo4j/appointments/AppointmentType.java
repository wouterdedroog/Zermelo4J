package nl.mrwouter.zermelo4j.appointments;

import java.util.Arrays;

/**
 * The appointment types for {@link Appointment#getType()}
 */
public enum AppointmentType {
    ACTIVITY, CHOICE, EXAM, MEETING, LESSON, OTHER, TALK;

    /**
     * Get appointment type for given String (case insensitive)
     *
     * @param name appointment type
     * @return AppointmentType for given string, or null when not found.
     */
    public static AppointmentType getEnum(String name) {
        return Arrays.stream(AppointmentType.values())
                .filter(appointmentType -> appointmentType.name().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
