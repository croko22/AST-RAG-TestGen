import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StunDatagramPacketFilterTest {

    @Mock
    private DatagramPacket datagramPacket;

    @Mock
    private TransportAddress stunServer;

    private StunDatagramPacketFilter filter;

    @BeforeEach
    public void setup() {
        filter = new StunDatagramPacketFilter();
    }

    @Test
    public void testAccept NullDatagramPacket() {
        // Given
        DatagramPacket nullDatagramPacket = null;

        // When
        boolean result = filter.accept(nullDatagramPacket);

        // Then
        assertFalse(result);
    }

    @Test
    public void testAccept NonStunPacket() {
        // Given
        byte[] data = new byte[10];
        when(datagramPacket.getData()).thenReturn(data);
        when(datagramPacket.getOffset()).thenReturn(0);
        when(datagramPacket.getLength()).thenReturn(10);

        // When
        boolean result = filter.accept(datagramPacket);

        // Then
        assertFalse(result);
    }

    @Test
    public void testAccept StunPacket() {
        // Given
        byte[] data = new byte[20];
        data[0] = 0x00;
        data[1] = 0x01;
        data[4] = 0x21;
        data[5] = 0x12;
        data[6] = 0xA4;
        data[7] = 0x42;
        when(datagramPacket.getData()).thenReturn(data);
        when(datagramPacket.getOffset()).thenReturn(0);
        when(datagramPacket.getLength()).thenReturn(20);

        // When
        boolean result = filter.accept(datagramPacket);

        // Then
        assertTrue(result);
    }

    @Test
    public void testAccept StunPacketWithSpecificServer() {
        // Given
        filter = new StunDatagramPacketFilter(stunServer);
        byte[] data = new byte[20];
        data[0] = 0x00;
        data[1] = 0x01;
        data[4] = 0x21;
        data[5] = 0x12;
        data[6] = 0xA4;
        data[7] = 0x42;
        when(datagramPacket.getData()).thenReturn(data);
        when(datagramPacket.getOffset()).thenReturn(0);
        when(datagramPacket.getLength()).thenReturn(20);
        when(datagramPacket.getSocketAddress()).thenReturn(stunServer);

        // When
        boolean result = filter.accept(datagramPacket);

        // Then
        assertTrue(result);
    }

    @Test
    public void testAccept StunPacketWithDifferentServer() {
        // Given
        filter = new StunDatagramPacketFilter(stunServer);
        byte[] data = new byte[20];
        data[0] = 0x00;
        data[1] = 0x01;
        data[4] = 0x21;
        data[5] = 0x12;
        data[6] = 0xA4;
        data[7] = 0x42;
        when(datagramPacket.getData()).thenReturn(data);
        when(datagramPacket.getOffset()).thenReturn(0);
        when(datagramPacket.getLength()).thenReturn(20);
        when(datagramPacket.getSocketAddress()).thenReturn(mock(TransportAddress.class));

        // When
        boolean result = filter.accept(datagramPacket);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals SameInstance() {
        // Given
        StunDatagramPacketFilter sameFilter = filter;

        // When
        boolean result = filter.equals(sameFilter);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals DifferentInstanceSameClass() {
        // Given
        StunDatagramPacketFilter differentFilter = new StunDatagramPacketFilter();

        // When
        boolean result = filter.equals(differentFilter);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals DifferentClass() {
        // Given
        Object differentClass = new Object();

        // When
        boolean result = filter.equals(differentClass);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals Null() {
        // Given
        Object nullObject = null;

        // When
        boolean result = filter.equals(nullObject);

        // Then
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given
        int hashCode = filter.hashCode();

        // When
        int expectedHashCode = filter.getClass().hashCode();

        // Then
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testIsStunPacket NullDatagramPacket() {
        // Given
        DatagramPacket nullDatagramPacket = null;

        // When
        boolean result = StunDatagramPacketFilter.isStunPacket(nullDatagramPacket);

        // Then
        assertFalse(result);
    }

    @Test
    public void testIsStunPacket NonStunPacket() {
        // Given
        byte[] data = new byte[10];
        when(datagramPacket.getData()).thenReturn(data);
        when(datagramPacket.getOffset()).thenReturn(0);
        when(datagramPacket.getLength()).thenReturn(10);

        // When
        boolean result = StunDatagramPacketFilter.isStunPacket(datagramPacket);

        // Then
        assertFalse(result);
    }

    @Test
    public void testIsStunPacket StunPacket() {
        // Given
        byte[] data = new byte[20];
        data[0] = 0x00;
        data[1] = 0x01;
        data[4] = 0x21;
        data[5] = 0x12;
        data[6] = 0xA4;
        data[7] = 0x42;
        when(datagramPacket.getData()).thenReturn(data);
        when(datagramPacket.getOffset()).thenReturn(0);
        when(datagramPacket.getLength()).thenReturn(20);

        // When
        boolean result = StunDatagramPacketFilter.isStunPacket(datagramPacket);

        // Then
        assertTrue(result);
    }
}