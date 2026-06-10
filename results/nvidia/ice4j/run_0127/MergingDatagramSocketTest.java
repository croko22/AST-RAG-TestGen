import org.ice4j.message.Request;
import org.ice4j.socket.MergingDatagramSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MergingDatagramSocketTest {

    @Mock
    private DatagramSocket datagramSocket;

    @Mock
    private InetAddress inetAddress;

    private MergingDatagramSocket mergingDatagramSocket;

    @BeforeEach
    void setup() throws SocketException {
        mergingDatagramSocket = new MergingDatagramSocket();
    }

    @AfterEach
    void tearDown() {
        mergingDatagramSocket.close();
    }

    @Test
    void testIsClosed() {
        // Given
        assertFalse(mergingDatagramSocket.isClosed());

        // When
        mergingDatagramSocket.close();

        // Then
        assertTrue(mergingDatagramSocket.isClosed());
    }

    @Test
    void testClose() {
        // Given
        assertFalse(mergingDatagramSocket.isClosed());

        // When
        mergingDatagramSocket.close();

        // Then
        assertTrue(mergingDatagramSocket.isClosed());
    }

    @Test
    void testSetSoTimeout() {
        // Given
        int soTimeout = 1000;

        // When
        mergingDatagramSocket.setSoTimeout(soTimeout);

        // Then
        assertEquals(soTimeout, mergingDatagramSocket.getSoTimeout());
    }

    @Test
    void testGetSoTimeout() {
        // Given
        int soTimeout = 1000;
        mergingDatagramSocket.setSoTimeout(soTimeout);

        // When
        int actualSoTimeout = mergingDatagramSocket.getSoTimeout();

        // Then
        assertEquals(soTimeout, actualSoTimeout);
    }

    @Test
    void testSend() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        mergingDatagramSocket.add(datagramSocket);

        // When
        mergingDatagramSocket.send(packet);

        // Then
        verify(datagramSocket).send(packet);
    }

    @Test
    void testAddDelegatingSocket() {
        // Given
        DelegatingSocket delegatingSocket = mock(DelegatingSocket.class);

        // When
        mergingDatagramSocket.add(delegatingSocket);

        // Then
        assertNotNull(mergingDatagramSocket.getActiveSocket());
    }

    @Test
    void testAddIceSocketWrapper() {
        // Given
        IceSocketWrapper iceSocketWrapper = mock(IceSocketWrapper.class);
        when(iceSocketWrapper.getUDPSocket()).thenReturn(datagramSocket);

        // When
        mergingDatagramSocket.add(iceSocketWrapper);

        // Then
        assertNotNull(mergingDatagramSocket.getActiveSocket());
    }

    @Test
    void testAddDatagramSocket() {
        // Given

        // When
        mergingDatagramSocket.add(datagramSocket);

        // Then
        assertNotNull(mergingDatagramSocket.getActiveSocket());
    }

    @Test
    void testRemoveDatagramSocket() {
        // Given
        mergingDatagramSocket.add(datagramSocket);

        // When
        mergingDatagramSocket.remove(datagramSocket);

        // Then
        assertNull(mergingDatagramSocket.getActiveSocket());
    }

    @Test
    void testRemoveDelegatingSocket() {
        // Given
        DelegatingSocket delegatingSocket = mock(DelegatingSocket.class);
        mergingDatagramSocket.add(delegatingSocket);

        // When
        mergingDatagramSocket.remove(delegatingSocket);

        // Then
        assertNull(mergingDatagramSocket.getActiveSocket());
    }

    @Test
    void testGetLocalAddress() {
        // Given
        when(datagramSocket.getLocalAddress()).thenReturn(inetAddress);
        mergingDatagramSocket.add(datagramSocket);

        // When
        InetAddress actualLocalAddress = mergingDatagramSocket.getLocalAddress();

        // Then
        assertEquals(inetAddress, actualLocalAddress);
    }

    @Test
    void testGetLocalPort() {
        // Given
        when(datagramSocket.getLocalPort()).thenReturn(1234);
        mergingDatagramSocket.add(datagramSocket);

        // When
        int actualLocalPort = mergingDatagramSocket.getLocalPort();

        // Then
        assertEquals(1234, actualLocalPort);
    }

    @Test
    void testGetLocalSocketAddress() {
        // Given
        SocketAddress socketAddress = mock(SocketAddress.class);
        when(datagramSocket.getLocalSocketAddress()).thenReturn(socketAddress);
        mergingDatagramSocket.add(datagramSocket);

        // When
        SocketAddress actualLocalSocketAddress = mergingDatagramSocket.getLocalSocketAddress();

        // Then
        assertEquals(socketAddress, actualLocalSocketAddress);
    }

    @Test
    void testReceive() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        mergingDatagramSocket.add(datagramSocket);

        // When
        mergingDatagramSocket.receive(packet);

        // Then
        verify(datagramSocket).receive(packet);
    }

    @Test
    void testReceiveTimeout() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        mergingDatagramSocket.add(datagramSocket);
        mergingDatagramSocket.setSoTimeout(100);

        // When and Then
        assertThrows(SocketTimeoutException.class, () -> mergingDatagramSocket.receive(packet));
    }
}