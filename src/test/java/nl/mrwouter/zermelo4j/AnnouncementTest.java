package nl.mrwouter.zermelo4j;

import com.github.javafaker.Faker;
import com.google.gson.JsonObject;
import nl.mrwouter.zermelo4j.annoucements.Announcement;
import nl.mrwouter.zermelo4j.api.ZermeloApiException;
import nl.mrwouter.zermelo4j.mocks.MockZermeloHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnnouncementTest {

    MockZermeloHttpClient zermeloHttpClient = new MockZermeloHttpClient();

    @Test
    @DisplayName("Can parse announcements")
    void testCanParseAnnouncements() throws ZermeloApiException {
        Faker faker = new Faker();
        ZermeloAPI zermeloAPI = ZermeloAPI.getAPI("test", faker.regexify("[a-z0-9]{26}"), zermeloHttpClient);
        List<Announcement> announcements = zermeloAPI.getAnnouncements();
        assertEquals(announcements.size(), 1);

        JsonObject firstJson = zermeloHttpClient.lastResponse().get("response").getAsJsonObject() // get response object
                .get("data").getAsJsonArray() // get data array
                .get(0).getAsJsonObject(); // get first announcement

        Announcement first = announcements.stream()
                .findFirst()
                .orElse(null);

        assertNotNull(first, "Couldn't find announcement object by ID");
        assertEquals(first.getId(), firstJson.get("id").getAsInt());

        assertEquals(first.getStart(), firstJson.get("start").getAsLong());
        assertEquals(first.getEnd(), firstJson.get("end").getAsLong());

        assertEquals(first.getTitle(), firstJson.get("title").getAsString());
        assertEquals(first.getText(), firstJson.get("text").getAsString());
    }
}
