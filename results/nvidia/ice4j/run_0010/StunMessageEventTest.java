import org.ice4j.StunMessageEvent;
import org.ice4j.stack.RawMessage;
import org.ice4j.stack.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StunMessageEventTest {

    @Mock
    private RawMessage rawMessage;

    @Mock
    private TransportAddress remoteAddress;

    @Mock
    private TransportAddress localAddress;

    private StunMessageEvent stunMessageEvent;

    @BeforeEach
    public void setup() {
        when(rawMessage.getRemoteAddress()).thenReturn(remoteAddress);
        when(rawMessage.getLocalAddress()).thenReturn(localAddress);
        stunMessageEvent = new StunMessageEvent(null, rawMessage, null);
    }

    @Test
    public void testGetLocalAddress() {
        // When
        TransportAddress localAddressResult = stunMessageEvent.getLocalAddress();

        // Then
        assertEquals(localAddress, localAddressResult);
    }

    @Test
    public void testGetRemoteAddress() {
        // When
        TransportAddress remoteAddressResult = stunMessageEvent.getRemoteAddress();

        // Then
        assertEquals(remoteAddress, remoteAddressResult);
    }

    @Test
    public void testToString() {
        // When
        String toStringResult = stunMessageEvent.toString();

        // Then
        assertNotNull(toStringResult);
        assertEquals("StunMessageEvent:\n\tMessage=null remoteAddr=" + remoteAddress + " localAddr=" + localAddress, toStringResult);
    }

    @Test
    public void testGetRawMessage() {
        // When
        RawMessage rawMessageResult = stunMessageEvent.getRawMessage();

        // Then
        assertEquals(rawMessage, rawMessageResult);
    }

    @Test
    public void testConstructor() {
        // Given
        RawMessage newRawMessage = RawMessage.build(10, remoteAddress, localAddress);

        // When
        StunMessageEvent newStunMessageEvent = new StunMessageEvent(null, newRawMessage, null);

        // Then
        assertNotNull(newStunMessageEvent);
        assertEquals(newRawMessage, newStunMessageEvent.getRawMessage());
    }

    @Test
    public void testGetLocalAddressNullRawMessage() {
        // Given
        StunMessageEvent stunMessageEventWithNullRawMessage = new StunMessageEvent(null, null, null);

        // When / Then
        assertThrows(NullPointerException.class, () -> stunMessageEventWithNullRawMessage.getLocalAddress());
    }

    @Test
    public void testGetRemoteAddressNullRawMessage() {
        // Given
        StunMessageEvent stunMessageEventWithNullRawMessage = new StunMessageEvent(null, null, null);

        // When / Then
        assertThrows(NullPointerException.class, () -> stunMessageEventWithNullRawMessage.getRemoteAddress());
    }
}