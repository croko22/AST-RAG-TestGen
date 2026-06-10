import org.ice4j.socket.DatagramPacketFilter;
import org.ice4j.socket.MultiplexedDatagramSocket;
import org.ice4j.socket.MultiplexingDatagramSocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiplexedDatagramSocketTest {

    @Mock
    private MultiplexingDatagramSocket multiplexing;

    @Mock
    private DatagramPacketFilter filter;

    private MultiplexedDatagramSocket multiplexedDatagramSocket;

    @BeforeEach
    void setup() throws SocketException {
        multiplexedDatagramSocket = new MultiplexedDatagramSocket(multiplexing, filter);
    }

    @Test
    public void testClose() {
        // When
        multiplexedDatagramSocket.close();

        // Then
        verify(multiplexing, times(1)).close(multiplexedDatagramSocket);
    }

    @Test
    public void testGetFilter() {
        // When
        DatagramPacketFilter result = multiplexedDatagramSocket.getFilter();

        // Then
        assertEquals(filter, result);
    }

    @Test
    public void testReceive() throws IOException {
        // Given
        DatagramPacket packet = mock(DatagramPacket.class);

        // When
        multiplexedDatagramSocket.receive(packet);

        // Then
        verify(multiplexing, times(1)).receive(multiplexedDatagramSocket, packet);
    }

    @Test
    public void testReceiveIOException() throws IOException {
        // Given
        DatagramPacket packet = mock(DatagramPacket.class);
        doThrow(new IOException()).when(multiplexing).receive(any(), any());

        // When and Then
        assertThrows(IOException.class, () -> multiplexedDatagramSocket.receive(packet));
        verify(multiplexing, times(1)).receive(multiplexedDatagramSocket, packet);
    }

    @Test
    public void testConstructorNullMultiplexing() {
        // Given
        MultiplexingDatagramSocket nullMultiplexing = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new MultiplexedDatagramSocket(nullMultiplexing, filter));
    }
}