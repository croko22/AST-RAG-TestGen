import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DelegatingServerSocketTest {

    @Mock
    private ServerSocket delegate;

    @Mock
    private ServerSocketChannel channel;

    @Mock
    private Socket socket;

    private DelegatingServerSocket delegatingServerSocket;

    @BeforeEach
    void setup() throws IOException {
        delegatingServerSocket = new DelegatingServerSocket(delegate, channel);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(delegate, channel);
    }

    @Test
    void testAccept() throws IOException {
        // Given
        when(delegate.accept()).thenReturn(socket);

        // When
        Socket result = delegatingServerSocket.accept();

        // Then
        assertEquals(socket, result);
        verify(delegate, times(1)).accept();
    }

    @Test
    void testBindSocketAddress() throws IOException {
        // Given
        SocketAddress endpoint = mock(SocketAddress.class);

        // When
        delegatingServerSocket.bind(endpoint);

        // Then
        verify(delegate, times(1)).bind(endpoint);
    }

    @Test
    void testBindSocketAddressInt() throws IOException {
        // Given
        SocketAddress endpoint = mock(SocketAddress.class);
        int backlog = 10;

        // When
        delegatingServerSocket.bind(endpoint, backlog);

        // Then
        verify(delegate, times(1)).bind(endpoint, backlog);
    }

    @Test
    void testClose() throws IOException {
        // When
        delegatingServerSocket.close();

        // Then
        verify(delegate, times(1)).close();
    }

    @Test
    void testGetChannel() {
        // When
        ServerSocketChannel result = delegatingServerSocket.getChannel();

        // Then
        assertEquals(channel, result);
    }

    @Test
    void testGetChannelDelegate() {
        // Given
        delegatingServerSocket = new DelegatingServerSocket(delegate, null);

        // When
        ServerSocketChannel result = delegatingServerSocket.getChannel();

        // Then
        assertEquals(delegate.getChannel(), result);
    }

    @Test
    void testGetInetAddress() {
        // Given
        InetAddress inetAddress = mock(InetAddress.class);
        when(delegate.getInetAddress()).thenReturn(inetAddress);

        // When
        InetAddress result = delegatingServerSocket.getInetAddress();

        // Then
        assertEquals(inetAddress, result);
        verify(delegate, times(1)).getInetAddress();
    }

    @Test
    void testGetLocalPort() {
        // Given
        int localPort = 8080;
        when(delegate.getLocalPort()).thenReturn(localPort);

        // When
        int result = delegatingServerSocket.getLocalPort();

        // Then
        assertEquals(localPort, result);
        verify(delegate, times(1)).getLocalPort();
    }

    @Test
    void testGetLocalSocketAddress() {
        // Given
        SocketAddress socketAddress = mock(SocketAddress.class);
        when(delegate.getLocalSocketAddress()).thenReturn(socketAddress);

        // When
        SocketAddress result = delegatingServerSocket.getLocalSocketAddress();

        // Then
        assertEquals(socketAddress, result);
        verify(delegate, times(1)).getLocalSocketAddress();
    }

    @Test
    void testGetReceiveBufferSize() throws SocketException {
        // Given
        int receiveBufferSize = 1024;
        when(delegate.getReceiveBufferSize()).thenReturn(receiveBufferSize);

        // When
        int result = delegatingServerSocket.getReceiveBufferSize();

        // Then
        assertEquals(receiveBufferSize, result);
        verify(delegate, times(1)).getReceiveBufferSize();
    }

    @Test
    void testGetReuseAddress() throws SocketException {
        // Given
        boolean reuseAddress = true;
        when(delegate.getReuseAddress()).thenReturn(reuseAddress);

        // When
        boolean result = delegatingServerSocket.getReuseAddress();

        // Then
        assertEquals(reuseAddress, result);
        verify(delegate, times(1)).getReuseAddress();
    }

    @Test
    void testGetSoTimeout() throws SocketException {
        // Given
        int soTimeout = 1000;
        when(delegate.getSoTimeout()).thenReturn(soTimeout);

        // When
        int result = delegatingServerSocket.getSoTimeout();

        // Then
        assertEquals(soTimeout, result);
        verify(delegate, times(1)).getSoTimeout();
    }

    @Test
    void testIsBound() {
        // Given
        boolean bound = true;
        when(delegate.isBound()).thenReturn(bound);

        // When
        boolean result = delegatingServerSocket.isBound();

        // Then
        assertEquals(bound, result);
        verify(delegate, times(1)).isBound();
    }

    @Test
    void testIsClosed() {
        // Given
        boolean closed = true;
        when(delegate.isClosed()).thenReturn(closed);

        // When
        boolean result = delegatingServerSocket.isClosed();

        // Then
        assertEquals(closed, result);
        verify(delegate, times(1)).isClosed();
    }

    @Test
    void testSetPerformancePreferences() {
        // Given
        int connectionTime = 1;
        int latency = 2;
        int bandwidth = 3;

        // When
        delegatingServerSocket.setPerformancePreferences(connectionTime, latency, bandwidth);

        // Then
        verify(delegate, times(1)).setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    @Test
    void testSetReceiveBufferSize() throws SocketException {
        // Given
        int size = 1024;

        // When
        delegatingServerSocket.setReceiveBufferSize(size);

        // Then
        verify(delegate, times(1)).setReceiveBufferSize(size);
    }

    @Test
    void testSetReuseAddress() throws SocketException {
        // Given
        boolean on = true;

        // When
        delegatingServerSocket.setReuseAddress(on);

        // Then
        verify(delegate, times(1)).setReuseAddress(on);
    }

    @Test
    void testSetSoTimeout() throws SocketException {
        // Given
        int timeout = 1000;

        // When
        delegatingServerSocket.setSoTimeout(timeout);

        // Then
        verify(delegate, times(1)).setSoTimeout(timeout);
    }

    @Test
    void testToString() {
        // Given
        String toString = "toString";
        when(delegate.toString()).thenReturn(toString);

        // When
        String result = delegatingServerSocket.toString();

        // Then
        assertEquals(toString, result);
        verify(delegate, times(1)).toString();
    }
}