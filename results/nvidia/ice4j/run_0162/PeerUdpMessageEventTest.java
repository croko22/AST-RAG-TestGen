import org.ice4j.PeerUdpMessageEvent;
import org.ice4j.stack.RawMessage;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PeerUdpMessageEventTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private RawMessage udpMessage;

    private PeerUdpMessageEvent peerUdpMessageEvent;

    @BeforeEach
    public void setup() {
        peerUdpMessageEvent = new PeerUdpMessageEvent(stunStack, udpMessage);
    }

    @Test
    public void testGetBytes() {
        // Given
        byte[] bytes = new byte[]{1, 2, 3};
        when(udpMessage.getBytes()).thenReturn(bytes);

        // When
        byte[] result = peerUdpMessageEvent.getBytes();

        // Then
        assertNotNull(result);
        assertEquals(bytes, result);
    }

    @Test
    public void testGetMessageLength() {
        // Given
        int messageLength = 10;
        when(udpMessage.getMessageLength()).thenReturn(messageLength);

        // When
        int result = peerUdpMessageEvent.getMessageLength();

        // Then
        assertEquals(messageLength, result);
    }

    @Test
    public void testGetRemoteAddress() {
        // Given
        TransportAddress remoteAddress = new TransportAddress("localhost", 1234);
        when(udpMessage.getRemoteAddress()).thenReturn(remoteAddress);

        // When
        TransportAddress result = peerUdpMessageEvent.getRemoteAddress();

        // Then
        assertNotNull(result);
        assertEquals(remoteAddress, result);
    }

    @Test
    public void testGetLocalAddress() {
        // Given
        TransportAddress localAddress = new TransportAddress("localhost", 5678);
        when(udpMessage.getLocalAddress()).thenReturn(localAddress);

        // When
        TransportAddress result = peerUdpMessageEvent.getLocalAddress();

        // Then
        assertNotNull(result);
        assertEquals(localAddress, result);
    }

    @Test
    public void testGetStunStack() {
        // Given
        StunStack stunStack = this.stunStack;

        // When
        StunStack result = peerUdpMessageEvent.getStunStack();

        // Then
        assertNotNull(result);
        assertEquals(stunStack, result);
    }

    @Test
    public void testConstructor() {
        // Given
        StunStack stunStack = this.stunStack;
        RawMessage udpMessage = this.udpMessage;

        // When
        PeerUdpMessageEvent peerUdpMessageEvent = new PeerUdpMessageEvent(stunStack, udpMessage);

        // Then
        assertNotNull(peerUdpMessageEvent);
        assertEquals(stunStack, peerUdpMessageEvent.getStunStack());
        assertEquals(udpMessage, peerUdpMessageEvent.udpMessage);
    }
}