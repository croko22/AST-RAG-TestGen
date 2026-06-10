import org.ice4j.message.StunMessageEvent;
import org.ice4j.stack.MessageEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MessageEventHandlerTest {

    @Mock
    private MessageEventHandler messageEventHandler;

    @Mock
    private StunMessageEvent stunMessageEvent;

    @BeforeEach
    public void setup() {
        // No setup required
    }

    @Test
    public void testHandleMessageEvent_NullEvent() {
        // Given: null event
        StunMessageEvent nullEvent = null;

        // When / Then: expect NullPointerException
        assertThrows(NullPointerException.class, () -> messageEventHandler.handleMessageEvent(nullEvent));
        verify(messageEventHandler, never()).handleMessageEvent(any());
    }

    @Test
    public void testHandleMessageEvent_ValidEvent() {
        // Given: valid event
        // When: handle message event
        assertDoesNotThrow(() -> messageEventHandler.handleMessageEvent(stunMessageEvent));

        // Then: verify interaction with mock
        verify(messageEventHandler, org.mockito.Mockito.times(1)).handleMessageEvent(stunMessageEvent);
    }

    @Test
    public void testHandleMessageEvent_ThrowingEvent() {
        // Given: event that throws exception when handled
        doThrow(RuntimeException.class).when(messageEventHandler).handleMessageEvent(stunMessageEvent);

        // When / Then: expect exception
        assertThrows(RuntimeException.class, () -> messageEventHandler.handleMessageEvent(stunMessageEvent));
        verify(messageEventHandler, org.mockito.Mockito.times(1)).handleMessageEvent(stunMessageEvent);
    }
}