import org.ice4j.stack.Connector;
import org.ice4j.stack.ErrorHandler;
import org.ice4j.socket.IceSocketWrapper;
import org.ice4j.TransportAddress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectorTest {

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    @Mock
    private ErrorHandler errorHandler;

    @Mock
    private Consumer<org.ice4j.message.RawMessage> messageConsumer;

    private Connector connector;

    @BeforeEach
    void setup() {
        connector = new Connector(iceSocketWrapper, null, messageConsumer, errorHandler);
    }

    @AfterEach
    void tearDown() {
        connector.stop();
    }

    @Test
    void testRun() {
        // Given
        when(iceSocketWrapper.receive(any(DatagramPacket.class))).thenReturn();
        when(iceSocketWrapper.getUDPSocket()).thenReturn(mock(java.net.DatagramSocket.class));

        // When
        connector.start();

        // Then
        verify(iceSocketWrapper, atLeastOnce()).receive(any(DatagramPacket.class));
    }

    @Test
    void testRunSocketException() {
        // Given
        when(iceSocketWrapper.receive(any(DatagramPacket.class))).thenThrow(new SocketException());

        // When
        connector.start();

        // Then
        verify(errorHandler).handleFatalError(any(Runnable.class), anyString(), any(Throwable.class));
    }

    @Test
    void testRunClosedChannelException() {
        // Given
        when(iceSocketWrapper.receive(any(DatagramPacket.class))).thenThrow(new java.nio.channels.ClosedChannelException());

        // When
        connector.start();

        // Then
        verify(errorHandler).handleFatalError(any(Runnable.class), anyString(), any(Throwable.class));
    }

    @Test
    void testRunIOException() {
        // Given
        when(iceSocketWrapper.receive(any(DatagramPacket.class))).thenThrow(new IOException());

        // When
        connector.start();

        // Then
        verify(errorHandler).handleError(anyString(), any(Throwable.class));
    }

    @Test
    void testRunThrowable() {
        // Given
        when(iceSocketWrapper.receive(any(DatagramPacket.class))).thenThrow(new Throwable());

        // When
        connector.start();

        // Then
        verify(errorHandler).handleFatalError(any(Runnable.class), anyString(), any(Throwable.class));
    }

    @Test
    void testToString() {
        // Given

        // When
        String result = connector.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    void testSendMessage() throws IOException {
        // Given
        byte[] message = new byte[]{1, 2, 3};
        TransportAddress address = new TransportAddress(InetAddress.getLocalHost(), 1234, org.ice4j.Transport.UDP);

        // When
        connector.sendMessage(message, address);

        // Then
        verify(iceSocketWrapper).send(any(DatagramPacket.class));
    }

    @Test
    void testSendMessageIOException() throws IOException {
        // Given
        byte[] message = new byte[]{1, 2, 3};
        TransportAddress address = new TransportAddress(InetAddress.getLocalHost(), 1234, org.ice4j.Transport.UDP);
        doThrow(new IOException()).when(iceSocketWrapper).send(any(DatagramPacket.class));

        // When and Then
        assertThrows(IOException.class, () -> connector.sendMessage(message, address));
    }

    @Test
    void testGetListenAddress() {
        // Given

        // When
        TransportAddress result = connector.getListenAddress();

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetRemoteAddress() {
        // Given

        // When
        TransportAddress result = connector.getRemoteAddress();

        // Then
        assertNull(result);
    }
}