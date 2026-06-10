import org.ice4j.message.Message;
import org.ice4j.message.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResponseTest {

    @Mock
    private Message message;

    @InjectMocks
    private Response response;

    @BeforeEach
    void setup() {
        // Initialize the response object
        response = new Response();
    }

    @Test
    public void testIsErrorResponse_True() {
        // Given: a valid error response type
        char errorResponseType = 'e';
        when(message.getMessageType()).thenReturn(errorResponseType);

        // When: isErrorResponse is called
        boolean result = response.isErrorResponse();

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsErrorResponse_False() {
        // Given: a valid non-error response type
        char nonErrorResponseType = 's';
        when(message.getMessageType()).thenReturn(nonErrorResponseType);

        // When: isErrorResponse is called
        boolean result = response.isErrorResponse();

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testIsSuccessResponse_True() {
        // Given: a valid success response type
        char successResponseType = 's';
        when(message.getMessageType()).thenReturn(successResponseType);

        // When: isSuccessResponse is called
        boolean result = response.isSuccessResponse();

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testIsSuccessResponse_False() {
        // Given: a valid non-success response type
        char nonSuccessResponseType = 'e';
        when(message.getMessageType()).thenReturn(nonSuccessResponseType);

        // When: isSuccessResponse is called
        boolean result = response.isSuccessResponse();

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testSetMessageType_ValidResponseType() {
        // Given: a valid response type
        char validResponseType = 's';

        // When: setMessageType is called
        response.setMessageType(validResponseType);

        // Then: no exception should be thrown
        verify(message, times(1)).setMessageType(validResponseType);
    }

    @Test
    public void testSetMessageType_InvalidResponseType() {
        // Given: an invalid response type
        char invalidResponseType = 'x';

        // When: setMessageType is called
        assertThrows(IllegalArgumentException.class, () -> response.setMessageType(invalidResponseType));

        // Then: an exception should be thrown
        verify(message, never()).setMessageType(invalidResponseType);
    }
}