import org.ice4j.message.Message;
import org.ice4j.message.MessageEventHandler;
import org.ice4j.message.RequestListener;
import org.ice4j.message.StunMessageEvent;
import org.ice4j.stack.EventDispatcher;
import org.ice4j.stack.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventDispatcherTest {

    @Mock
    private TransportAddress transportAddress;

    @Mock
    private MessageEventHandler messageEventHandler;

    @Mock
    private RequestListener requestListener;

    @Mock
    private StunMessageEvent stunMessageEvent;

    private EventDispatcher eventDispatcher;

    @BeforeEach
    public void setup() {
        eventDispatcher = new EventDispatcher();
    }

    @Test
    public void testAddIndicationListener() {
        // Given
        // When
        eventDispatcher.addIndicationListener(transportAddress, messageEventHandler);
        // Then
        verify(messageEventHandler, never()).handleMessageEvent(any());
    }

    @Test
    public void testAddOldIndicationListener() {
        // Given
        // When
        eventDispatcher.addOldIndicationListener(transportAddress, messageEventHandler);
        // Then
        verify(messageEventHandler, never()).handleMessageEvent(any());
    }

    @Test
    public void testAddRequestListener() {
        // Given
        // When
        eventDispatcher.addRequestListener(requestListener);
        // Then
        verify(requestListener, never()).processRequest(any());
    }

    @Test
    public void testAddRequestListenerWithTransportAddress() {
        // Given
        // When
        eventDispatcher.addRequestListener(transportAddress, requestListener);
        // Then
        verify(requestListener, never()).processRequest(any());
    }

    @Test
    public void testRemoveRequestListener() {
        // Given
        eventDispatcher.addRequestListener(requestListener);
        // When
        eventDispatcher.removeRequestListener(requestListener);
        // Then
        verify(requestListener, never()).processRequest(any());
    }

    @Test
    public void testRemoveRequestListenerWithTransportAddress() {
        // Given
        eventDispatcher.addRequestListener(transportAddress, requestListener);
        // When
        eventDispatcher.removeRequestListener(transportAddress, requestListener);
        // Then
        verify(requestListener, never()).processRequest(any());
    }

    @Test
    public void testFireMessageEvent() {
        // Given
        eventDispatcher.addRequestListener(requestListener);
        // When
        eventDispatcher.fireMessageEvent(stunMessageEvent);
        // Then
        verify(requestListener).processRequest(stunMessageEvent);
    }

    @Test
    public void testHasRequestListeners() {
        // Given
        // When
        boolean result = eventDispatcher.hasRequestListeners(transportAddress);
        // Then
        assertFalse(result);
    }

    @Test
    public void testHasRequestListenersWithListener() {
        // Given
        eventDispatcher.addRequestListener(transportAddress, requestListener);
        // When
        boolean result = eventDispatcher.hasRequestListeners(transportAddress);
        // Then
        assertTrue(result);
    }

    @Test
    public void testRemoveAllListeners() {
        // Given
        eventDispatcher.addRequestListener(requestListener);
        // When
        eventDispatcher.removeAllListeners();
        // Then
        verify(requestListener, never()).processRequest(any());
    }
}