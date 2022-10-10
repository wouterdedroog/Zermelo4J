package nl.mrwouter.zermelo4j;

import nl.mrwouter.zermelo4j.api.ZermeloApiException;
import nl.mrwouter.zermelo4j.mocks.MockZermeloHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OAuthTokenTest {

    MockZermeloHttpClient zermeloHttpClient = new MockZermeloHttpClient();

    @Test
    @DisplayName("Can generate an access token")
    void testCanRetrieveAccessToken() throws ZermeloApiException {
        String accessToken = ZermeloAPI.getAccessToken("test", "123123123123", zermeloHttpClient);
        assertEquals(accessToken, zermeloHttpClient.lastResponse().get("access_token").getAsString());
    }
}
