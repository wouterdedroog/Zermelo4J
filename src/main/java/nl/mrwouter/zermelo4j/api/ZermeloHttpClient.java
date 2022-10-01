package nl.mrwouter.zermelo4j.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ZermeloHttpClient {

    private final String baseUrl;
    private final HttpClient httpClient;

    /**
     * Create a ZermeloHttpClient
     */
    public ZermeloHttpClient() {
        this("https://%s.zportal.nl/api/v3", HttpClient.newHttpClient());
    }

    /**
     * Create a ZermeloHttpClient
     *
     * @param baseUrl    baseUrl of the API, school can be filled in as %s, and will be dynamically filled in per request.
     *                   Example: https://%s.zportal.nl/api/v3
     * @param httpClient instance of HttpClient
     */
    public ZermeloHttpClient(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public HttpResponse<String> get(String endpoint, String school, String accessToken, Map<String, String> parameters)
            throws ZermeloApiException {
        try {
            parameters.put("access_token", accessToken);
        }catch(UnsupportedOperationException exception) {
            // Fix errors when ImmutableMap is supplied
            this.get(endpoint, school, accessToken, new HashMap<>(parameters));
        }

        String url = String.format(this.baseUrl, encode(school)) + endpoint + "?" + this.constructQueryParameters(parameters);

        try {
            HttpRequest httpRequest = prepareRequest(accessToken)
                    .GET()
                    .uri(new URI(url))
                    .build();

            return this.dispatchRequest(httpRequest);
        } catch (URISyntaxException | IOException | InterruptedException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public HttpResponse<String> post(String endpoint, String school, String accessToken, Map<String, String> parameters)
            throws ZermeloApiException {
        String url = String.format(this.baseUrl, encode(school)) + endpoint + "?" + this.constructQueryParameters(parameters);

        try {
            HttpRequest httpRequest = prepareRequest(accessToken)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .uri(new URI(url))
                    .build();

            return this.dispatchRequest(httpRequest);
        } catch (URISyntaxException | IOException | InterruptedException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private String constructQueryParameters(Map<String, String> parameters) {
        return parameters.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private HttpRequest.Builder prepareRequest(String accessToken) {
        return HttpRequest.newBuilder()
                .setHeader("User-Agent", "Zermelo4J")
                .setHeader("Authorization", "Bearer " + accessToken)
                .timeout(Duration.ofSeconds(5));
    }

    private HttpResponse<String> dispatchRequest(HttpRequest httpRequest) throws ZermeloApiException, IOException, InterruptedException {
        HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() > 399) {
            throw new ZermeloApiException(httpResponse.statusCode(), httpResponse.body());
        }
        return httpResponse;
    }
}
