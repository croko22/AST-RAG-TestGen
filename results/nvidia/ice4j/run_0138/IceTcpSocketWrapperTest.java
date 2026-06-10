import org.ice4j.socket.DelegatingSocket;
import org.ice4j.socket.IceTcpSocketWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IceTcpSocketWrapperTest {

    @Mock
    private Socket socket;

    @Mock
    private DelegatingSocket delegatingSocket;

    @Mock
    private InputStream inputStream;

    @Mock
    private OutputStream outputStream;

    private IceTcpSocketWrapper iceTcpSocketWrapper;

    @BeforeEach
    void setup() throws IOException {
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        iceTcpSocketWrapper = new IceTcpSocketWrapper(socket);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(socket, delegatingSocket, inputStream, outputStream);
    }

    @Test
    void testClose() {
        // When
        iceTcpSocketWrapper.close();

        // Then
        verify(socket).close();
    }

    @Test
    void testGetLocalAddress() {
        // Given
        InetAddress localAddress = mock(InetAddress.class);
        when(socket.getLocalAddress()).thenReturn(localAddress);

        // When
        InetAddress result = iceTcpSocketWrapper.getLocalAddress();

        // Then
        assertEquals(localAddress, result);
        verify(socket).getLocalAddress();
    }

    @Test
    void testGetLocalPort() {
        // Given
        int localPort = 1234;
        when(socket.getLocalPort()).thenReturn(localPort);

        // When
        int result = iceTcpSocketWrapper.getLocalPort();

        // Then
        assertEquals(localPort, result);
        verify(socket).getLocalPort();
    }

    @Test
    void testGetLocalSocketAddress() {
        // Given
        SocketAddress localSocketAddress = mock(SocketAddress.class);
        when(socket.getLocalSocketAddress()).thenReturn(localSocketAddress);

        // When
        SocketAddress result = iceTcpSocketWrapper.getLocalSocketAddress();

        // Then
        assertEquals(localSocketAddress, result);
        verify(socket).getLocalSocketAddress();
    }

    @Test
    void testGetTCPSocket() {
        // When
        Socket result = iceTcpSocketWrapper.getTCPSocket();

        // Then
        assertEquals(socket, result);
    }

    @Test
    void testGetUDPSocket() {
        // When
        DatagramSocket result = iceTcpSocketWrapper.getUDPSocket();

        // Then
        assertNull(result);
    }

    @Test
    void testReceive() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        when(socket instanceof DelegatingSocket).thenReturn(false);

        // When
        iceTcpSocketWrapper.receive(packet);

        // Then
        verifyStatic(DelegatingSocket.class);
        DelegatingSocket.receiveFromInputStream(packet, inputStream, any(InetAddress.class), anyInt());
    }

    @Test
    void testReceiveWithDelegatingSocket() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        when(socket instanceof DelegatingSocket).thenReturn(true);
        iceTcpSocketWrapper = new IceTcpSocketWrapper(delegatingSocket);

        // When
        iceTcpSocketWrapper.receive(packet);

        // Then
        verify(delegatingSocket).receive(packet);
    }

    @Test
    void testSend() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        when(socket instanceof DelegatingSocket).thenReturn(false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(outputStream).thenReturn(byteArrayOutputStream);

        // When
        iceTcpSocketWrapper.send(packet);

        // Then
        verify(outputStream).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    void testSendWithDelegatingSocket() throws IOException {
        // Given
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        when(socket instanceof DelegatingSocket).thenReturn(true);
        iceTcpSocketWrapper = new IceTcpSocketWrapper(delegatingSocket);

        // When
        iceTcpSocketWrapper.send(packet);

        // Then
        verify(delegatingSocket).send(packet);
    }
}