import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BaseDelegatingSocketChannelTest {

    @Mock
    private SocketChannel delegate;

    private BaseDelegatingSocketChannel<SocketChannel> baseDelegatingSocketChannel;

    @BeforeEach
    public void setup() {
        baseDelegatingSocketChannel = new BaseDelegatingSocketChannel<>(delegate);
    }

    @Test
    public void testBind() throws IOException {
        // Given
        SocketAddress local = mock(SocketAddress.class);
        // When
        SocketChannel result = baseDelegatingSocketChannel.bind(local);
        // Then
        verify(delegate).bind(local);
        assertEquals(baseDelegatingSocketChannel, result);
    }

    @Test
    public void testConnect() throws IOException {
        // Given
        SocketAddress remote = mock(SocketAddress.class);
        // When
        boolean result = baseDelegatingSocketChannel.connect(remote);
        // Then
        verify(delegate).connect(remote);
        assertEquals(result, delegate.connect(remote));
    }

    @Test
    public void testFinishConnect() throws IOException {
        // When
        boolean result = baseDelegatingSocketChannel.finishConnect();
        // Then
        verify(delegate).finishConnect();
        assertEquals(result, delegate.finishConnect());
    }

    @Test
    public void testGetLocalAddress() throws IOException {
        // When
        SocketAddress result = baseDelegatingSocketChannel.getLocalAddress();
        // Then
        verify(delegate).getLocalAddress();
        assertEquals(result, delegate.getLocalAddress());
    }

    @Test
    public void testGetOption() throws IOException {
        // Given
        SocketOption<String> option = mock(SocketOption.class);
        // When
        String result = (String) baseDelegatingSocketChannel.getOption(option);
        // Then
        verify(delegate).getOption(option);
        assertEquals(result, delegate.getOption(option));
    }

    @Test
    public void testGetRemoteAddress() throws IOException {
        // When
        SocketAddress result = baseDelegatingSocketChannel.getRemoteAddress();
        // Then
        verify(delegate).getRemoteAddress();
        assertEquals(result, delegate.getRemoteAddress());
    }

    @Test
    public void testIsConnected() {
        // When
        boolean result = baseDelegatingSocketChannel.isConnected();
        // Then
        verify(delegate).isConnected();
        assertEquals(result, delegate.isConnected());
    }

    @Test
    public void testIsConnectionPending() {
        // When
        boolean result = baseDelegatingSocketChannel.isConnectionPending();
        // Then
        verify(delegate).isConnectionPending();
        assertEquals(result, delegate.isConnectionPending());
    }

    @Test
    public void testRead() throws IOException {
        // Given
        ByteBuffer dst = mock(ByteBuffer.class);
        // When
        int result = baseDelegatingSocketChannel.read(dst);
        // Then
        verify(delegate).read(dst);
        assertEquals(result, delegate.read(dst));
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        // Given
        int offset = 1;
        int length = 2;
        // When
        long result = baseDelegatingSocketChannel.read(new ByteBuffer[length], offset, length);
        // Then
        verify(delegate).read(any(ByteBuffer[].class), eq(offset), eq(length));
        assertEquals(result, delegate.read(new ByteBuffer[length], offset, length));
    }

    @Test
    public void testSetOption() throws IOException {
        // Given
        SocketOption<String> option = mock(SocketOption.class);
        String value = "value";
        // When
        SocketChannel result = baseDelegatingSocketChannel.setOption(option, value);
        // Then
        verify(delegate).setOption(option, value);
        assertEquals(result, baseDelegatingSocketChannel);
    }

    @Test
    public void testShutdownInput() throws IOException {
        // When
        SocketChannel result = baseDelegatingSocketChannel.shutdownInput();
        // Then
        verify(delegate).shutdownInput();
        assertEquals(result, baseDelegatingSocketChannel);
    }

    @Test
    public void testShutdownOutput() throws IOException {
        // When
        SocketChannel result = baseDelegatingSocketChannel.shutdownOutput();
        // Then
        verify(delegate).shutdownOutput();
        assertEquals(result, baseDelegatingSocketChannel);
    }

    @Test
    public void testSocket() {
        // Given
        Socket socket = mock(Socket.class);
        when(delegate.socket()).thenReturn(socket);
        // When
        Socket result = baseDelegatingSocketChannel.socket();
        // Then
        verify(delegate).socket();
        assertNotNull(result);
    }

    @Test
    public void testSupportedOptions() {
        // When
        Set<SocketOption<?>> result = baseDelegatingSocketChannel.supportedOptions();
        // Then
        verify(delegate).supportedOptions();
        assertEquals(result, delegate.supportedOptions());
    }

    @Test
    public void testWrite() throws IOException {
        // Given
        ByteBuffer src = mock(ByteBuffer.class);
        // When
        int result = baseDelegatingSocketChannel.write(src);
        // Then
        verify(delegate).write(src);
        assertEquals(result, delegate.write(src));
    }

    @Test
    public void testWriteWithOffsetAndLength() throws IOException {
        // Given
        int offset = 1;
        int length = 2;
        // When
        long result = baseDelegatingSocketChannel.write(new ByteBuffer[length], offset, length);
        // Then
        verify(delegate).write(any(ByteBuffer[].class), eq(offset), eq(length));
        assertEquals(result, delegate.write(new ByteBuffer[length], offset, length));
    }
}