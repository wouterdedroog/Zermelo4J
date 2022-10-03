package nl.mrwouter.zermelo4j.appointments;

import java.util.Arrays;

/**
 * The appointment types for {@link Appointment#getType()}
 */
public enum AppointmentType {
    /**
     * AppointmentType ACTIVITY as returned from the Zermelo API.
     */
    ACTIVITY,
    /**
     * AppointmentType CHOICE as returned from the Zermelo API.
     */
    CHOICE,
    /**
     * AppointmentType EXAM as returned from the Zermelo API.
     */
    EXAM,
    /**
     * AppointmentType MEETING as returned from the Zermelo API.
     */
    MEETING,
    /**
     * AppointmentType LESSON as returned from the Zermelo API.
     */
    LESSON,
    /**
     * AppointmentType OTHER as returned from the Zermelo API.
     */
    OTHER,
    /**
     * AppointmentType TALK as returned from the Zermelo API.
     */
    TALK;

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
