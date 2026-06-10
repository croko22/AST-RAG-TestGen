import org.ice4j.socket.MultiplexingSocket;
import org.ice4j.socket.MultiplexedSocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramPacketFilter;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiplexingSocketTest {

    @Mock
    private DatagramPacketFilter filter;

    @Mock
    private DatagramPacket packet;

    private MultiplexingSocket socket;

    @BeforeEach
    public void setup() {
        socket = new MultiplexingSocket();
    }

    @Test
    public void testGetInputStream() throws IOException {
        // Given
        InputStream inputStream = socket.getInputStream();

        // Then
        assertNotNull(inputStream);
    }

    @Test
    public void testGetOriginalInputStream() throws IOException {
        // Given
        InputStream inputStream = socket.getOriginalInputStream();

        // Then
        assertNotNull(inputStream);
    }

    @Test
    public void testGetOriginalOutputStream() throws IOException {
        // Given
        OutputStream outputStream = socket.getOriginalOutputStream();

        // Then
        assertNotNull(outputStream);
    }

    @Test
    public void testGetOutputStream() throws IOException {
        // Given
        OutputStream outputStream = socket.getOutputStream();

        // Then
        assertNotNull(outputStream);
    }

    @Test
    public void testGetSocket() throws SocketException {
        // Given
        MultiplexedSocket multiplexedSocket = socket.getSocket(filter);

        // Then
        assertNotNull(multiplexedSocket);
    }

    @Test
    public void testGetSoTimeout() {
        // Given
        int soTimeout = socket.getSoTimeout();

        // Then
        assertEquals(0, soTimeout);
    }

    @Test
    public void testReceive() throws IOException {
        // Given
        socket.receive(packet);

        // Then
        verify(packet, times(1)).setData(any());
    }

    @Test
    public void testSetSoTimeout() throws SocketException {
        // Given
        int timeout = 1000;
        socket.setSoTimeout(timeout);

        // Then
        assertEquals(timeout, socket.getSoTimeout());
    }

    @Test
    public void testReceive_MultiplexedSocket() throws IOException {
        // Given
        MultiplexedSocket multiplexedSocket = socket.getSocket(filter);
        socket.receive(multiplexedSocket, packet);

        // Then
        verify(packet, times(1)).setData(any());
    }

    @Test
    public void testGetSocket_ExistingFilter() throws SocketException {
        // Given
        MultiplexedSocket multiplexedSocket1 = socket.getSocket(filter);
        MultiplexedSocket multiplexedSocket2 = socket.getSocket(filter);

        // Then
        assertSame(multiplexedSocket1, multiplexedSocket2);
    }

    @Test
    public void testGetSocket_NewFilter() throws SocketException {
        // Given
        DatagramPacketFilter newFilter = mock(DatagramPacketFilter.class);
        MultiplexedSocket multiplexedSocket1 = socket.getSocket(filter);
        MultiplexedSocket multiplexedSocket2 = socket.getSocket(newFilter);

        // Then
        assertNotSame(multiplexedSocket1, multiplexedSocket2);
    }
}