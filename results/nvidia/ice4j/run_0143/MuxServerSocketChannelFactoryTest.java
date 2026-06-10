import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MuxServerSocketChannelFactoryTest {

    @Mock
    private ServerSocketChannel serverSocketChannel;

    @Mock
    private SocketChannel socketChannel;

    private Map<String, Object> properties;

    @BeforeEach
    void setup() {
        properties = new HashMap<>();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(serverSocketChannel, socketChannel);
    }

    @Test
    public void testOpenAndBindServerSocketChannel_ValidProperties() throws IOException {
        // Given
        SocketAddress endpoint = new InetSocketAddress("localhost", 8080);
        int backlog = 10;
        properties.put(MuxServerSocketChannelFactory.SOCKET_REUSE_ADDRESS_PROPERTY_NAME, true);

        // When
        ServerSocketChannel result = MuxServerSocketChannelFactory.openAndBindServerSocketChannel(properties, endpoint, backlog);

        // Then
        assertNotNull(result);
        assertTrue(result.isOpen());
    }

    @Test
    public void testOpenAndBindServerSocketChannel_InvalidProperties() throws IOException {
        // Given
        SocketAddress endpoint = new InetSocketAddress("localhost", 8080);
        int backlog = 10;
        properties.put(MuxServerSocketChannelFactory.SOCKET_REUSE_ADDRESS_PROPERTY_NAME, "invalid");

        // When
        ServerSocketChannel result = MuxServerSocketChannelFactory.openAndBindServerSocketChannel(properties, endpoint, backlog);

        // Then
        assertNotNull(result);
        assertTrue(result.isOpen());
    }

    @Test
    public void testOpenAndBindServerSocketChannel_NullProperties() throws IOException {
        // Given
        SocketAddress endpoint = new InetSocketAddress("localhost", 8080);
        int backlog = 10;

        // When
        ServerSocketChannel result = MuxServerSocketChannelFactory.openAndBindServerSocketChannel(null, endpoint, backlog);

        // Then
        assertNotNull(result);
        assertTrue(result.isOpen());
    }

    @Test
    public void testOpenAndBindServerSocketChannel_EmptyProperties() throws IOException {
        // Given
        SocketAddress endpoint = new InetSocketAddress("localhost", 8080);
        int backlog = 10;
        properties = new HashMap<>();

        // When
        ServerSocketChannel result = MuxServerSocketChannelFactory.openAndBindServerSocketChannel(properties, endpoint, backlog);

        // Then
        assertNotNull(result);
        assertTrue(result.isOpen());
    }

    @Test
    public void testOpenAndBindServerSocketChannel_InvalidEndpoint() {
        // Given
        SocketAddress endpoint = null;
        int backlog = 10;

        // When and Then
        assertThrows(NullPointerException.class, () -> MuxServerSocketChannelFactory.openAndBindServerSocketChannel(properties, endpoint, backlog));
    }

    @Test
    public void testOpenAndBindServerSocketChannel_InvalidBacklog() throws IOException {
        // Given
        SocketAddress endpoint = new InetSocketAddress("localhost", 8080);
        int backlog = -1;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> MuxServerSocketChannelFactory.openAndBindServerSocketChannel(properties, endpoint, backlog));
    }

    @Test
    public void testOpenAndBindServerSocketChannel_IOError() throws IOException {
        // Given
        SocketAddress endpoint = new InetSocketAddress("localhost", 8080);
        int backlog = 10;
        doThrow(new IOException()).when(serverSocketChannel).socket();

        // When and Then
        assertThrows(IOException.class, () -> MuxServerSocketChannelFactory.openAndBindServerSocketChannel(properties, endpoint, backlog));
    }
}