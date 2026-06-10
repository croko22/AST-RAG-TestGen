import org.ice4j.message.Message;
import org.ice4j.message.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RequestTest {

    @InjectMocks
    private Request request;

    @Test
    public void testSetMessageType_ValidRequestType() {
        // Given: a valid request type
        char validRequestType = 'A'; // assuming 'A' is a valid request type

        // When: setting the message type
        assertDoesNotThrow(() -> request.setMessageType(validRequestType));

        // Then: the message type is set correctly
        assertEquals(validRequestType, request.getMessageType());
    }

    @Test
    public void testSetMessageType_InvalidRequestType() {
        // Given: an invalid request type
        char invalidRequestType = 'Z'; // assuming 'Z' is not a valid request type

        // When / Then: setting the message type throws an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> request.setMessageType(invalidRequestType));
        String expectedMessage = (int) invalidRequestType + " - is not a valid request type.";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testSetMessageType_BoundaryRequestType() {
        // Given: a boundary request type (e.g., the first or last valid type)
        char boundaryRequestType = 'A'; // assuming 'A' is the first valid request type

        // When: setting the message type
        assertDoesNotThrow(() -> request.setMessageType(boundaryRequestType));

        // Then: the message type is set correctly
        assertEquals(boundaryRequestType, request.getMessageType());
    }
}