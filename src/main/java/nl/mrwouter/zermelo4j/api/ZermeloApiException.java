package nl.mrwouter.zermelo4j.api;

/**
 * Exception thrown when the Zermelo API returns a status code < 200 or > 399
 */
public class ZermeloApiException extends Exception {

    private final int statusCode;
    private final String body;

    public ZermeloApiException(int statusCode, String body) {
        super("Received unexpected status code (" + statusCode + ") from Zermelo API. Body: " + body);
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getResponseBody() {
        return this.body;
    }
}
