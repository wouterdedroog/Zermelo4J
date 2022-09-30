package nl.mrwouter.zermelo4j.appointments;

/**
 * The appointment types for {@link Appointment#getType()}
 */
public enum AppointmentType {
    UNKNOWN, LESSON, EXAM, ACTIVITY, CHOICE, TALK, OTHER;


    /**
     * Get appointment type for given String (case insensitive)
     *
     * @param name appointment type
     * @return AppointmentType for given string, returns 'UNKNOWN' when not found.
     */
    public static AppointmentType getEnum(String name) {
        for (AppointmentType type : AppointmentType.values()) {
            if (name.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return AppointmentType.UNKNOWN;
    }
}
