import org.jsoup.UnsupportedMimeTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnsupportedMimeTypeExceptionTest {

    private UnsupportedMimeTypeException exception;
    private String message;
    private String mimeType;
    private String url;

    @BeforeEach
    public void setup() {
        message = "Unsupported mime type";
        mimeType = "text/plain";
        url = "https://example.com";
        exception = new UnsupportedMimeTypeException(message, mimeType, url);
    }

    @Test
    public void testGetMimeType() {
        // When: se obtiene el tipo de mime
        String obtainedMimeType = exception.getMimeType();

        // Then: se verifica el resultado
        assertEquals(mimeType, obtainedMimeType);
    }

    @Test
    public void testGetUrl() {
        // When: se obtiene la URL
        String obtainedUrl = exception.getUrl();

        // Then: se verifica el resultado
        assertEquals(url, obtainedUrl);
    }

    @Test
    public void testToString() {
        // When: se obtiene la representación en cadena
        String toString = exception.toString();

        // Then: se verifica el resultado
        assertTrue(toString.contains(message));
        assertTrue(toString.contains(mimeType));
        assertTrue(toString.contains(url));
    }

    @Test
    public void testConstructor_MessageMimeTypeUrl() {
        // Given: los parámetros para el constructor
        String newMessage = "New message";
        String newMimeType = "application/json";
        String newUrl = "https://new.example.com";

        // When: se crea una nueva instancia de la excepción
        UnsupportedMimeTypeException newException = new UnsupportedMimeTypeException(newMessage, newMimeType, newUrl);

        // Then: se verifica el resultado
        assertEquals(newMessage, newException.getMessage());
        assertEquals(newMimeType, newException.getMimeType());
        assertEquals(newUrl, newException.getUrl());
    }

    @Test
    public void testConstructor_NullMessage() {
        // Given: un mensaje nulo
        String nullMessage = null;

        // When / Then: se espera que no se lance una excepción
        assertDoesNotThrow(() -> new UnsupportedMimeTypeException(nullMessage, mimeType, url));
    }

    @Test
    public void testConstructor_NullMimeType() {
        // Given: un tipo de mime nulo
        String nullMimeType = null;

        // When / Then: se espera que no se lance una excepción
        assertDoesNotThrow(() -> new UnsupportedMimeTypeException(message, nullMimeType, url));
    }

    @Test
    public void testConstructor_NullUrl() {
        // Given: una URL nula
        String nullUrl = null;

        // When / Then: se espera que no se lance una excepción
        assertDoesNotThrow(() -> new UnsupportedMimeTypeException(message, mimeType, nullUrl));
    }
}