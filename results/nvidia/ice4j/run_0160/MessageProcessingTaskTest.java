import org.ice4j.stack.MessageProcessingTask;
import org.ice4j.stack.NetAccessManager;
import org.ice4j.stack.RawMessage;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransportAddress;
import org.ice4j.stack.MessageEventHandler;
import org.ice4j.stack.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageProcessingTaskTest {

    @Mock
    private NetAccessManager netAccessManager;

    @Mock
    private MessageEventHandler messageEventHandler;

    @Mock
    private ErrorHandler errorHandler;

    @Mock
    private StunStack stunStack;

    private MessageProcessingTask messageProcessingTask;

    @BeforeEach
    void setup() {
        when(netAccessManager.getMessageEventHandler()).thenReturn(messageEventHandler);
        when(netAccessManager.getStunStack()).thenReturn(stunStack);
        messageProcessingTask = new MessageProcessingTask(netAccessManager);
    }

    @Test
    void testConstructor() {
        assertNotNull(messageProcessingTask);
        assertSame(netAccessManager, messageProcessingTask.netAccessManager);
        assertSame(messageEventHandler, messageProcessingTask.messageEventHandler);
        assertSame(errorHandler, messageProcessingTask.errorHandler);
    }

    @Test
    void testConstructor_NullNetAccessManager() {
        assertThrows(NullPointerException.class, () -> new MessageProcessingTask(null));
    }

    @Test
    void testConstructor_NullMessageEventHandler() {
        when(netAccessManager.getMessageEventHandler()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new MessageProcessingTask(netAccessManager));
    }

    @Test
    void testSetMessage() {
        RawMessage rawMessage = RawMessage.build(10, new TransportAddress(), new TransportAddress());
        Consumer<MessageProcessingTask> onProcessed = mock(Consumer.class);
        messageProcessingTask.setMessage(rawMessage, onProcessed);
        assertSame(rawMessage, messageProcessingTask.rawMessage);
        assertSame(onProcessed, messageProcessingTask.rawMessageProcessedHandler);
    }

    @Test
    void testSetMessage_NullMessage() {
        assertThrows(IllegalArgumentException.class, () -> messageProcessingTask.setMessage(null, mock(Consumer.class)));
    }

    @Test
    void testResetState() {
        messageProcessingTask.rawMessage = RawMessage.build(10, new TransportAddress(), new TransportAddress());
        messageProcessingTask.rawMessageProcessedHandler = mock(Consumer.class);
        messageProcessingTask.cancelled.set(true);
        messageProcessingTask.resetState();
        assertNull(messageProcessingTask.rawMessage);
        assertNull(messageProcessingTask.rawMessageProcessedHandler);
        assertFalse(messageProcessingTask.cancelled.get());
    }

    @Test
    void testCancel() {
        messageProcessingTask.cancel();
        assertTrue(messageProcessingTask.cancelled.get());
    }

    @Test
    void testRun_NoMessage() {
        messageProcessingTask.rawMessage = null;
        messageProcessingTask.run();
        verifyNoInteractions(messageEventHandler, errorHandler);
    }

    @Test
    void testRun_Cancelled() {
        messageProcessingTask.rawMessage = RawMessage.build(10, new TransportAddress(), new TransportAddress());
        messageProcessingTask.cancel();
        messageProcessingTask.run();
        verifyNoInteractions(messageEventHandler, errorHandler);
    }

    @Test
    void testRun_DecodeException() {
        messageProcessingTask.rawMessage = RawMessage.build(10, new TransportAddress(), new TransportAddress());
        doThrow(new RuntimeException()).when(stunStack).decode(any(byte[].class), anyInt(), anyInt());
        messageProcessingTask.run();
        verify(errorHandler).handleError(anyString(), any(Throwable.class));
    }

    @Test
    void testRun_Success() {
        messageProcessingTask.rawMessage = RawMessage.build(10, new TransportAddress(), new TransportAddress());
        messageProcessingTask.rawMessageProcessedHandler = mock(Consumer.class);
        messageProcessingTask.run();
        verify(messageEventHandler).handleMessageEvent(any());
        verify(messageProcessingTask.rawMessageProcessedHandler).accept(any(MessageProcessingTask.class));
    }
}