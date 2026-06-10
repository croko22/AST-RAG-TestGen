import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import org.ice4j.TransportAddress;
import org.ice4j.stack.RawMessage;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class RawMessageTest {

    @Mock
    private TransportAddress remoteAddress;

    @Mock
    private TransportAddress localAddress;

    private byte[] messageBytes;
    private int messageLength;

    @BeforeEach
    void setup() {
        messageBytes = new byte[]{1, 2, 3, 4, 5};
        messageLength = messageBytes.length;
    }

    @Test
    void testGetBytes() {
        // Given
        RawMessage rawMessage = RawMessage.build(messageBytes, messageLength, remoteAddress, localAddress);

        // When
        byte[] bytes = rawMessage.getBytes();

        // Then
        assertArrayEquals(messageBytes, bytes);
    }

    @Test
    void testGetMessageLength() {
        // Given
        RawMessage rawMessage = RawMessage.build(messageBytes, messageLength, remoteAddress, localAddress);

        // When
        int length = rawMessage.getMessageLength();

        // Then
        assertEquals(messageLength, length);
    }

    @Test
    void testGetRemoteAddress() {
        // Given
        RawMessage rawMessage = RawMessage.build(messageBytes, messageLength, remoteAddress, localAddress);

        // When
        TransportAddress address = rawMessage.getRemoteAddress();

        // Then
        assertEquals(remoteAddress, address);
    }

    @Test
    void testGetLocalAddress() {
        // Given
        RawMessage rawMessage = RawMessage.build(messageBytes, messageLength, remoteAddress, localAddress);

        // When
        TransportAddress address = rawMessage.getLocalAddress();

        // Then
        assertEquals(localAddress, address);
    }

    @Test
    void testBuild() {
        // Given

        // When
        RawMessage rawMessage = RawMessage.build(messageBytes, messageLength, remoteAddress, localAddress);

        // Then
        assertNotNull(rawMessage);
        assertArrayEquals(messageBytes, rawMessage.getBytes());
        assertEquals(messageLength, rawMessage.getMessageLength());
        assertEquals(remoteAddress, rawMessage.getRemoteAddress());
        assertEquals(localAddress, rawMessage.getLocalAddress());
    }

    @Test
    void testBuildNullMessageBytes() {
        // Given
        byte[] nullMessageBytes = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> RawMessage.build(nullMessageBytes, messageLength, remoteAddress, localAddress));
    }

    @Test
    void testBuildNullRemoteAddress() {
        // Given
        TransportAddress nullRemoteAddress = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> RawMessage.build(messageBytes, messageLength, nullRemoteAddress, localAddress));
    }

    @Test
    void testBuildNullLocalAddress() {
        // Given
        TransportAddress nullLocalAddress = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> RawMessage.build(messageBytes, messageLength, remoteAddress, nullLocalAddress));
    }

    @Test
    void testBuildMessageLengthGreaterThanMessageBytesLength() {
        // Given
        int invalidMessageLength = messageBytes.length + 1;

        // When and Then
        assertDoesNotThrow(() -> RawMessage.build(messageBytes, invalidMessageLength, remoteAddress, localAddress));
    }

    @Test
    void testBuildMessageLengthLessThanZero() {
        // Given
        int invalidMessageLength = -1;

        // When and Then
        assertDoesNotThrow(() -> RawMessage.build(messageBytes, invalidMessageLength, remoteAddress, localAddress));
    }
}