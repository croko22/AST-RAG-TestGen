import org.ice4j.message.StunMessageEvent;
import org.ice4j.stack.RequestListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestListenerTest {

    @Mock
    private RequestListener requestListener;

    @Mock
    private StunMessageEvent stunMessageEvent;

    @Test
    public void testProcessRequest() {
        // Given: a valid StunMessageEvent
        // When: processRequest is called
        requestListener.processRequest(stunMessageEvent);
        // Then: verify the method was called
        verify(requestListener, times(1)).processRequest(stunMessageEvent);
    }

    @Test
    public void testProcessRequest_ThrowsIllegalArgumentException() {
        // Given: a RequestListener that throws an IllegalArgumentException
        RequestListener requestListener = new RequestListener() {
            @Override
            public void processRequest(StunMessageEvent evt) throws IllegalArgumentException {
                throw new IllegalArgumentException("Test exception");
            }
        };
        // When: processRequest is called
        // Then: an IllegalArgumentException is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            requestListener.processRequest(stunMessageEvent);
        });
        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    public void testProcessRequest_ThrowsOtherException() {
        // Given: a RequestListener that throws an exception other than IllegalArgumentException
        RequestListener requestListener = new RequestListener() {
            @Override
            public void processRequest(StunMessageEvent evt) throws IllegalArgumentException {
                throw new RuntimeException("Test exception");
            }
        };
        // When: processRequest is called
        // Then: the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            requestListener.processRequest(stunMessageEvent);
        });
        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    public void testProcessRequest_NullStunMessageEvent() {
        // Given: a null StunMessageEvent
        StunMessageEvent nullStunMessageEvent = null;
        // When: processRequest is called
        // Then: a NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> {
            requestListener.processRequest(nullStunMessageEvent);
        });
    }
}