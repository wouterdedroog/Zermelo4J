package nl.mrwouter.zermelo4j;

import nl.mrwouter.zermelo4j.factories.*;

public enum ApiEndpoint {
    OAUTH_TOKEN("/oauth/token", new OAuthTokenFactory()),
    APPOINTMENT_PARTICIPATIONS("/appointmentparticipations", new AppointmentParticipationFactory()),
    APPOINTMENTS("/appointments", new AppointmentFactory()),
    ANNOUNCEMENTS("/announcements", new AnnouncementFactory()),
    USER("/users/", null);

    private final String endpoint;
    private final ApiFactory apiFactory;
    ApiEndpoint(String endpoint, ApiFactory apiFactory) {
        this.endpoint = endpoint;
        this.apiFactory = apiFactory;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public ApiFactory getFactory() {
        return this.apiFactory;
    }
}
