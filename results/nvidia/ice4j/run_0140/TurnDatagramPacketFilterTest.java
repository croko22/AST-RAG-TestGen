import org.ice4j.message.Message;
import org.ice4j.socket.TurnDatagramPacketFilter;
import org.ice4j.socket.StunDatagramPacketFilter;
import org.ice4j.transport.TransportAddress;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TurnDatagramPacketFilterTest {

    @Mock
    private TransportAddress turnServer;

    @Mock
    private DatagramPacket datagramPacket;

    private TurnDatagramPacketFilter turnDatagramPacketFilter;

    @BeforeEach
    void setup() {
        turnDatagramPacketFilter = new TurnDatagramPacketFilter(turnServer);
    }

    @Test
    void testAccept_TurnMessage_ReturnsTrue() {
        // Given
        when(turnServer.equals(any(TransportAddress.class))).thenReturn(true);
        when(datagramPacket.getAddress()).thenReturn(mock(InetAddress.class));
        when(datagramPacket.getPort()).thenReturn(1234);
        byte[] data = new byte[10];
        when(datagramPacket.getData()).thenReturn(data);
        when(datagramPacket.getLength()).thenReturn(data.length);

        // When
        boolean result = turnDatagramPacketFilter.accept(datagramPacket);

        // Then
        assertTrue(result);
    }

    @Test
    void testAccept_NonTurnMessage_ReturnsFalse() {
        // Given
        when(turnServer.equals(any(TransportAddress.class))).thenReturn(false);

        // When
        boolean result = turnDatagramPacketFilter.accept(datagramPacket);

        // Then
        assertFalse(result);
    }

    @Test
    void testAcceptMethod_TurnMethod_ReturnsTrue() {
        // Given
        char turnMethod = Message.TURN_METHOD_ALLOCATE;

        // When
        boolean result = turnDatagramPacketFilter.acceptMethod(turnMethod);

        // Then
        assertTrue(result);
    }

    @Test
    void testAcceptMethod_NonTurnMethod_ReturnsFalse() {
        // Given
        char nonTurnMethod = 'X';

        // When
        boolean result = turnDatagramPacketFilter.acceptMethod(nonTurnMethod);

        // Then
        assertFalse(result);
    }

    @Test
    void testAcceptMethod_OldTurnDataIndication_ReturnsTrue() {
        // Given
        char oldTurnDataIndication = 0x0005;

        // When
        boolean result = turnDatagramPacketFilter.acceptMethod(oldTurnDataIndication);

        // Then
        assertTrue(result);
    }

    @Test
    void testAcceptMethod_StunMethod_ReturnsTrue() {
        // Given
        char stunMethod = 'S';

        // When
        boolean result = turnDatagramPacketFilter.acceptMethod(stunMethod);

        // Then
        assertTrue(result);
    }

    @Test
    void testAcceptMethod_InvalidMethod_ThrowsNoException() {
        // Given
        char invalidMethod = 0x00FF;

        // When
        boolean result = turnDatagramPacketFilter.acceptMethod(invalidMethod);

        // Then
        assertFalse(result);
    }
}