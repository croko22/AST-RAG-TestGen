import org.jsoup.SerializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SerializationExceptionTest {

    @Test
    public void testSerializationException_NoMessageNoCause() {
        // Given: no message and no cause
        // When: create a new SerializationException
        SerializationException exception = new SerializationException();
        // Then: verify the exception
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testSerializationException_WithMessageNoCause() {
        // Given: a message and no cause
        String message = "Test message";
        // When: create a new SerializationException
        SerializationException exception = new SerializationException(message);
        // Then: verify the exception
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testSerializationException_NoMessageWithCause() {
        // Given: no message and a cause
        Throwable cause = new Throwable("Test cause");
        // When: create a new SerializationException
        SerializationException exception = new SerializationException(cause);
        // Then: verify the exception
        assertEquals(cause.toString(), exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testSerializationException_WithMessageAndCause() {
        // Given: a message and a cause
        String message = "Test message";
        Throwable cause = new Throwable("Test cause");
        // When: create a new SerializationException
        SerializationException exception = new SerializationException(message, cause);
        // Then: verify the exception
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testSerializationException_NullMessageNoCause() {
        // Given: a null message and no cause
        // When: create a new SerializationException
        SerializationException exception = new SerializationException(null);
        // Then: verify the exception
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testSerializationException_NullMessageWithCause() {
        // Given: a null message and a cause
        Throwable cause = new Throwable("Test cause");
        // When: create a new SerializationException
        SerializationException exception = new SerializationException(null, cause);
        // Then: verify the exception
        assertNull(exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}