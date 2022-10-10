package nl.mrwouter.zermelo4j.mocks;

import com.google.gson.JsonObject;
import nl.mrwouter.zermelo4j.ApiEndpoint;
import nl.mrwouter.zermelo4j.api.ZermeloApiException;
import nl.mrwouter.zermelo4j.api.ZermeloHttpClient;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;

public class MockZermeloHttpClient extends ZermeloHttpClient {

    /**
     * Make a GET request to the Zermelo API
     * @param endpoint API endpoint (i.e. /appointments)
     * @param school subdomain used for the current school
     * @param accessToken access token as retrieved from {@link nl.mrwouter.zermelo4j.ZermeloAPI#getAccessToken(String, String)}
     * @param parameters route parameters
     * @return HttpResponse containing the body as a string
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public HttpResponse<String> get(String endpoint, String school, String accessToken, Map<String, String> parameters)
            throws ZermeloApiException {
        JsonObject response = generateDataForEndpoint(endpoint, parameters);
        return new MockHttpResponse(200, response.getAsString());
    }

    /**
     * Make a POST request to the Zermelo API
     * @param endpoint API endpoint (i.e. /appointments)
     * @param school subdomain used for the current school
     * @param accessToken access token as retrieved from {@link nl.mrwouter.zermelo4j.ZermeloAPI#getAccessToken(String, String)}
     * @param parameters route parameters
     * @return HttpResponse containing the body as a string
     * @throws ZermeloApiException thrown when Zermelo returns a non-successful status code
     */
    public HttpResponse<String> post(String endpoint, String school, String accessToken, Map<String, String> parameters)
            throws ZermeloApiException {
        JsonObject response = generateDataForEndpoint(endpoint, parameters);
        return new MockHttpResponse(200, response.toString());
    }

    public JsonObject generateDataForEndpoint(String endpoint, Map<String, String> parameters) {
        return Arrays.stream(ApiEndpoint.values())
                .filter(apiEndpoint -> endpoint.startsWith(apiEndpoint.getEndpoint()))
                .map(apiEndpoint -> apiEndpoint.getFactory().getData(parameters))
                .findFirst()
                .orElseThrow();
    }
}
