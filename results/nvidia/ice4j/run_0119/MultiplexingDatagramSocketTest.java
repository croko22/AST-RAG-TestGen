import org.ice4j.message.DatagramPacket;
import org.ice4j.message.DatagramPacketFilter;
import org.ice4j.socket.MultiplexedDatagramSocket;
import org.ice4j.socket.MultiplexingDatagramSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiplexingDatagramSocketTest {

    @Mock
    private DatagramPacketFilter filter;

    @Mock
    private DatagramPacket packet;

    private MultiplexingDatagramSocket socket;

    @BeforeEach
    public void setup() throws SocketException {
        socket = new MultiplexingDatagramSocket();
    }

    @AfterEach
    public void tearDown() {
        // No-op
    }

    @Test
    public void testGetSocket() throws SocketException {
        // Given
        when(filter.accept(any(DatagramPacket.class))).thenReturn(true);

        // When
        MultiplexedDatagramSocket multiplexedSocket = socket.getSocket(filter);

        // Then
        assertNotNull(multiplexedSocket);
        verify(filter, times(1)).accept(any(DatagramPacket.class));
    }

    @Test
    public void testGetSocket_CreateFalse() throws SocketException {
        // Given
        when(filter.accept(any(DatagramPacket.class))).thenReturn(true);

        // When
        MultiplexedDatagramSocket multiplexedSocket = socket.getSocket(filter, false);

        // Then
        assertNotNull(multiplexedSocket);
        verify(filter, times(1)).accept(any(DatagramPacket.class));
    }

    @Test
    public void testGetSoTimeout() {
        // Given
        int timeout = 1000;
        socket.setSoTimeout(timeout);

        // When
        int soTimeout = socket.getSoTimeout();

        // Then
        assertEquals(timeout, soTimeout);
    }

    @Test
    public void testReceive() throws IOException {
        // Given
        when(packet.getData()).thenReturn(new byte[10]);

        // When
        socket.receive(packet);

        // Then
        verify(packet, times(1)).getData();
    }

    @Test
    public void testSetReceiveBufferSize() throws SocketException {
        // Given
        int receiveBufferSize = 1024;

        // When
        socket.setReceiveBufferSize(receiveBufferSize);

        // Then
        // No-op, as the method does not throw any exceptions
    }

    @Test
    public void testSetSoTimeout() throws SocketException {
        // Given
        int timeout = 1000;

        // When
        socket.setSoTimeout(timeout);

        // Then
        assertEquals(timeout, socket.getSoTimeout());
    }

    @Test
    public void testSetSoTimeout_NegativeValue() {
        // Given
        int timeout = -1;

        // When and Then
        assertThrows(SocketException.class, () -> socket.setSoTimeout(timeout));
    }

    @Test
    public void testReceive_Timeout() throws SocketException {
        // Given
        socket.setSoTimeout(100);
        when(packet.getData()).thenReturn(new byte[10]);

        // When and Then
        assertThrows(SocketTimeoutException.class, () -> socket.receive(packet));
    }
}