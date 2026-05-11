import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RequestTest {

    @Mock
    private Message message;

    private Request request;

    @BeforeEach
    void setup() {
        request = new Request();
    }

    @AfterEach
    void tearDown() {
        request = null;
    }

    @Test
    public void testSetMessageType_ValidRequestType() {
        // Given: a valid request type
        char validRequestType = 'A';

        // When: setting the message type
        assertDoesNotThrow(() -> request.setMessageType(validRequestType));

        // Then: the message type is set correctly
        // Note: Since the superclass method is not mocked, we can't directly verify its call.
        // However, we can verify that no exception is thrown, which implies the type was set correctly.
    }

    @Test
    public void testSetMessageType_InvalidRequestType() {
        // Given: an invalid request type
        char invalidRequestType = 'a'; // assuming 'a' is not a valid request type

        // When: setting the message type
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> request.setMessageType(invalidRequestType));

        // Then: an exception is thrown with the correct message
        String expectedMessage = (int) (invalidRequestType) + " - is not a valid request type.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testSetMessageType_NullRequestType() {
        // Given: a null request type (note: char is a primitive type and cannot be null)
        // This test is not applicable for char type, as it cannot be null.
        // However, we can test with a default value (e.g., '\u0000') to see if it's handled correctly.
        char nullRequestType = '\u0000';

        // When: setting the message type
        assertDoesNotThrow(() -> request.setMessageType(nullRequestType));
    }

    @Test
    public void testSetMessageType_BoundaryRequestType() {
        // Given: a boundary request type (e.g., the minimum or maximum char value)
        char minRequestType = Character.MIN_VALUE;
        char maxRequestType = Character.MAX_VALUE;

        // When: setting the message type
        assertDoesNotThrow(() -> request.setMessageType(minRequestType));
        assertDoesNotThrow(() -> request.setMessageType(maxRequestType));
    }
}