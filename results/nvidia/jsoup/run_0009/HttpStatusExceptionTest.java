import org.jsoup.HttpStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HttpStatusExceptionTest {

    private HttpStatusException exception;

    @BeforeEach
    public void setup() {
        String message = "HTTP request failed";
        int statusCode = 404;
        String url = "https://example.com";
        exception = new HttpStatusException(message, statusCode, url);
    }

    @Test
    public void testGetStatusCode() {
        // Given: exception is created with a status code
        // When: getStatusCode method is called
        int actualStatusCode = exception.getStatusCode();
        // Then: status code is returned correctly
        assertEquals(404, actualStatusCode);
    }

    @Test
    public void testGetUrl() {
        // Given: exception is created with a URL
        // When: getUrl method is called
        String actualUrl = exception.getUrl();
        // Then: URL is returned correctly
        assertEquals("https://example.com", actualUrl);
    }

    @Test
    public void testConstructor_Message() {
        // Given: exception is created with a message, status code, and URL
        // When: exception message is retrieved
        String actualMessage = exception.getMessage();
        // Then: message contains the expected information
        assertTrue(actualMessage.contains("HTTP request failed"));
        assertTrue(actualMessage.contains("Status=404"));
        assertTrue(actualMessage.contains("URL=[https://example.com]"));
    }

    @Test
    public void testConstructor_StatusCode() {
        // Given: exception is created with a status code
        // When: status code is retrieved
        int actualStatusCode = exception.getStatusCode();
        // Then: status code is correct
        assertEquals(404, actualStatusCode);
    }

    @Test
    public void testConstructor_Url() {
        // Given: exception is created with a URL
        // When: URL is retrieved
        String actualUrl = exception.getUrl();
        // Then: URL is correct
        assertEquals("https://example.com", actualUrl);
    }

    @Test
    public void testConstructor_NullMessage() {
        // Given: exception is created with a null message
        assertThrows(NullPointerException.class, () -> new HttpStatusException(null, 404, "https://example.com"));
    }

    @Test
    public void testConstructor_NullUrl() {
        // Given: exception is created with a null URL
        assertThrows(NullPointerException.class, () -> new HttpStatusException("HTTP request failed", 404, null));
    }
}