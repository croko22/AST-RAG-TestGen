import org.ice4j.StunException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StunExceptionTest {

    private StunException stunException;

    @BeforeEach
    public void setup() {
        stunException = new StunException();
    }

    @Test
    public void testSetID() {
        // Given: a StunException instance
        // When: setting the ID
        int id = 1;
        stunException.setID(id);
        // Then: verify the ID is set correctly
        assertEquals(id, stunException.getID());
    }

    @Test
    public void testGetID() {
        // Given: a StunException instance with a set ID
        int id = 1;
        stunException.setID(id);
        // When: getting the ID
        int retrievedId = stunException.getID();
        // Then: verify the ID is retrieved correctly
        assertEquals(id, retrievedId);
    }

    @Test
    public void testDefaultConstructor() {
        // Given: a StunException instance created with the default constructor
        StunException exception = new StunException();
        // Then: verify the ID is set to the default value
        assertEquals(0, exception.getID());
    }

    @Test
    public void testConstructorWithID() {
        // Given: a StunException instance created with an ID
        int id = 1;
        StunException exception = new StunException(id);
        // Then: verify the ID is set correctly
        assertEquals(id, exception.getID());
    }

    @Test
    public void testConstructorWithMessage() {
        // Given: a StunException instance created with a message
        String message = "Test message";
        StunException exception = new StunException(message);
        // Then: verify the message is set correctly
        assertEquals(message, exception.getMessage());
        // and the ID is set to the default value
        assertEquals(0, exception.getID());
    }

    @Test
    public void testConstructorWithIDAndMessage() {
        // Given: a StunException instance created with an ID and a message
        int id = 1;
        String message = "Test message";
        StunException exception = new StunException(id, message);
        // Then: verify the ID and message are set correctly
        assertEquals(id, exception.getID());
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithIDMessageAndCause() {
        // Given: a StunException instance created with an ID, a message, and a cause
        int id = 1;
        String message = "Test message";
        Throwable cause = new Throwable("Test cause");
        StunException exception = new StunException(id, message, cause);
        // Then: verify the ID, message, and cause are set correctly
        assertEquals(id, exception.getID());
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        // Given: a StunException instance created with a message and a cause
        String message = "Test message";
        Throwable cause = new Throwable("Test cause");
        StunException exception = new StunException(message, cause);
        // Then: verify the message and cause are set correctly
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        // and the ID is set to the default value
        assertEquals(0, exception.getID());
    }

    @Test
    public void testConstructorWithCause() {
        // Given: a StunException instance created with a cause
        Throwable cause = new Throwable("Test cause");
        StunException exception = new StunException(cause);
        // Then: verify the cause is set correctly
        assertEquals(cause, exception.getCause());
        // and the ID is set to the default value
        assertEquals(0, exception.getID());
    }
}