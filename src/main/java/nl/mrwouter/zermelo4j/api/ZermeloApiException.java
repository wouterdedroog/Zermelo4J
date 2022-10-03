package nl.mrwouter.zermelo4j.api;

/**
 * Exception thrown when the Zermelo API returns a status code &lt; 200 or &gt; 399
 */
public class ZermeloApiException extends Exception {

    /**
     * Status code of request
     */
    private final int statusCode;
    /**
     * Body of request
     */
    private final String body;

    /**
     * Create a ZermeloApiException
     * @param statusCode status code of request
     * @param body body of request
     */
    public ZermeloApiException(int statusCode, String body) {
        super("Received unexpected status code (" + statusCode + ") from Zermelo API. Body: " + body);
        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * Get the HTTP status code for this request
     * @return HTTP status code
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * Get the response body for this request
     * @return response body
     */
    public String getResponseBody() {
        return this.body;
    }
}
