import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BaseDelegatingServerSocketChannelTest {

    @Mock
    private ServerSocketChannel delegate;

    @Mock
    private SocketChannel socketChannel;

    @Mock
    private ServerSocket serverSocket;

    @Mock
    private SocketOption<String> socketOption;

    private BaseDelegatingServerSocketChannel<ServerSocketChannel> baseDelegatingServerSocketChannel;

    @BeforeEach
    public void setup() {
        baseDelegatingServerSocketChannel = new BaseDelegatingServerSocketChannel<>(delegate);
    }

    @Test
    public void testAccept() throws IOException {
        // Given
        when(delegate.accept()).thenReturn(socketChannel);

        // When
        SocketChannel result = baseDelegatingServerSocketChannel.accept();

        // Then
        assertEquals(socketChannel, result);
        verify(delegate, times(1)).accept();
    }

    @Test
    public void testAcceptNull() throws IOException {
        // Given
        when(delegate.accept()).thenReturn(null);

        // When
        SocketChannel result = baseDelegatingServerSocketChannel.accept();

        // Then
        assertNull(result);
        verify(delegate, times(1)).accept();
    }

    @Test
    public void testBind() throws IOException {
        // Given
        SocketAddress local = new InetSocketAddress("localhost", 8080);

        // When
        ServerSocketChannel result = baseDelegatingServerSocketChannel.bind(local, 10);

        // Then
        assertEquals(baseDelegatingServerSocketChannel, result);
        verify(delegate, times(1)).bind(local, 10);
    }

    @Test
    public void testGetLocalAddress() throws IOException {
        // Given
        SocketAddress local = new InetSocketAddress("localhost", 8080);
        when(delegate.getLocalAddress()).thenReturn(local);

        // When
        SocketAddress result = baseDelegatingServerSocketChannel.getLocalAddress();

        // Then
        assertEquals(local, result);
        verify(delegate, times(1)).getLocalAddress();
    }

    @Test
    public void testGetOption() throws IOException {
        // Given
        String value = "value";
        when(delegate.getOption(any(SocketOption.class))).thenReturn(value);

        // When
        String result = baseDelegatingServerSocketChannel.getOption(socketOption);

        // Then
        assertEquals(value, result);
        verify(delegate, times(1)).getOption(any(SocketOption.class));
    }

    @Test
    public void testIsBound() {
        // Given
        when(delegate.getLocalAddress()).thenReturn(new InetSocketAddress("localhost", 8080));

        // When
        boolean result = baseDelegatingServerSocketChannel.isBound();

        // Then
        assertTrue(result);
        verify(delegate, times(1)).getLocalAddress();
    }

    @Test
    public void testIsBoundNot() {
        // Given
        when(delegate.getLocalAddress()).thenThrow(new IOException());

        // When
        boolean result = baseDelegatingServerSocketChannel.isBound();

        // Then
        assertFalse(result);
        verify(delegate, times(1)).getLocalAddress();
    }

    @Test
    public void testSetOption() throws IOException {
        // Given
        String value = "value";

        // When
        ServerSocketChannel result = baseDelegatingServerSocketChannel.setOption(socketOption, value);

        // Then
        assertEquals(baseDelegatingServerSocketChannel, result);
        verify(delegate, times(1)).setOption(any(SocketOption.class), any());
    }

    @Test
    public void testSocket() throws IOException {
        // Given
        when(delegate.socket()).thenReturn(serverSocket);

        // When
        ServerSocket result = baseDelegatingServerSocketChannel.socket();

        // Then
        assertNotNull(result);
        verify(delegate, times(1)).socket();
    }

    @Test
    public void testSupportedOptions() {
        // Given
        Set<SocketOption<?>> options = Set.of(socketOption);
        when(delegate.supportedOptions()).thenReturn(options);

        // When
        Set<SocketOption<?>> result = baseDelegatingServerSocketChannel.supportedOptions();

        // Then
        assertEquals(options, result);
        verify(delegate, times(1)).supportedOptions();
    }
}