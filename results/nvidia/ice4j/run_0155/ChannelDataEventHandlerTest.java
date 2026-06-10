import org.ice4j.message.ChannelDataMessageEvent;
import org.ice4j.stack.ChannelDataEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChannelDataEventHandlerTest {

    @Mock
    private ChannelDataEventHandler channelDataEventHandler;

    @Mock
    private ChannelDataMessageEvent channelDataMessageEvent;

    @BeforeEach
    public void setup() {
        // No setup needed
    }

    @Test
    public void testHandleMessageEvent() {
        // Given: a valid ChannelDataMessageEvent
        // When: handleMessageEvent is called
        assertDoesNotThrow(() -> channelDataEventHandler.handleMessageEvent(channelDataMessageEvent));
        // Then: verify the method was called
        verify(channelDataEventHandler, org.mockito.Mockito.times(1)).handleMessageEvent(channelDataMessageEvent);
    }

    @Test
    public void testHandleMessageEvent_NullMessageEvent() {
        // Given: a null ChannelDataMessageEvent
        ChannelDataMessageEvent nullMessageEvent = null;
        // When: handleMessageEvent is called
        assertDoesNotThrow(() -> channelDataEventHandler.handleMessageEvent(nullMessageEvent));
        // Then: verify the method was called
        verify(channelDataEventHandler, org.mockito.Mockito.times(1)).handleMessageEvent(nullMessageEvent);
    }

    @Test
    public void testHandleMessageEvent_ThrowingException() {
        // Given: a ChannelDataEventHandler that throws an exception
        ChannelDataEventHandler throwingHandler = new ChannelDataEventHandler() {
            @Override
            public void handleMessageEvent(ChannelDataMessageEvent messageEvent) {
                throw new RuntimeException("Test exception");
            }
        };
        // When: handleMessageEvent is called
        assertThrows(RuntimeException.class, () -> throwingHandler.handleMessageEvent(channelDataMessageEvent));
    }

    @Test
    public void testHandleMessageEvent_MockThrowingException() {
        // Given: a mock ChannelDataEventHandler that throws an exception
        doThrow(new RuntimeException("Mock test exception")).when(channelDataEventHandler).handleMessageEvent(any());
        // When: handleMessageEvent is called
        assertThrows(RuntimeException.class, () -> channelDataEventHandler.handleMessageEvent(channelDataMessageEvent));
        // Then: verify the method was called
        verify(channelDataEventHandler, org.mockito.Mockito.times(1)).handleMessageEvent(channelDataMessageEvent);
    }
}