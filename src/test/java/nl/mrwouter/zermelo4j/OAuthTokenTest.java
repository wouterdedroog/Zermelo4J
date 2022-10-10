package nl.mrwouter.zermelo4j;

import nl.mrwouter.zermelo4j.api.ZermeloApiException;
import nl.mrwouter.zermelo4j.api.ZermeloHttpClient;
import nl.mrwouter.zermelo4j.mocks.MockZermeloHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OAuthTokenTest {

    ZermeloHttpClient zermeloHttpClient = new MockZermeloHttpClient();

    @Test
    @DisplayName("Can generate an access token")
    void testCanGenerateAccessToken() throws ZermeloApiException {
        String accessToken = ZermeloAPI.getAccessToken("test", "123123123123", zermeloHttpClient);
        assertTrue(accessToken.matches("[a-z0-9]{26}"));
        assertEquals(accessToken.length(), 26);
    }
}
