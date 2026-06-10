import org.ice4j.ResponseCollector;
import org.ice4j.StunResponseEvent;
import org.ice4j.StunTimeoutEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResponseCollectorTest {

    @Mock
    private ResponseCollector responseCollector;

    @Mock
    private StunResponseEvent stunResponseEvent;

    @Mock
    private StunTimeoutEvent stunTimeoutEvent;

    @BeforeEach
    public void setup() {
        // No setup required
    }

    @Test
    public void testProcessResponse() {
        // Given: a valid StunResponseEvent
        // When: processResponse is called
        assertDoesNotThrow(() -> responseCollector.processResponse(stunResponseEvent));
        // Then: verify the method was called
        verify(responseCollector, times(1)).processResponse(any(StunResponseEvent.class));
    }

    @Test
    public void testProcessResponse_NullEvent() {
        // Given: a null StunResponseEvent
        StunResponseEvent nullEvent = null;
        // When: processResponse is called
        assertDoesNotThrow(() -> responseCollector.processResponse(nullEvent));
        // Then: verify the method was not called with a null argument
        verify(responseCollector, never()).processResponse(null);
    }

    @Test
    public void testProcessTimeout() {
        // Given: a valid StunTimeoutEvent
        // When: processTimeout is called
        assertDoesNotThrow(() -> responseCollector.processTimeout(stunTimeoutEvent));
        // Then: verify the method was called
        verify(responseCollector, times(1)).processTimeout(any(StunTimeoutEvent.class));
    }

    @Test
    public void testProcessTimeout_NullEvent() {
        // Given: a null StunTimeoutEvent
        StunTimeoutEvent nullEvent = null;
        // When: processTimeout is called
        assertDoesNotThrow(() -> responseCollector.processTimeout(nullEvent));
        // Then: verify the method was not called with a null argument
        verify(responseCollector, never()).processTimeout(null);
    }
}