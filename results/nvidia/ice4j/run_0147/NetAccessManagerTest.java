import org.ice4j.stack.NetAccessManager;
import org.ice4j.stack.StunStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NetAccessManagerTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private PeerUdpMessageEventHandler peerUdpMessageEventHandler;

    @Mock
    private ChannelDataEventHandler channelDataEventHandler;

    private NetAccessManager netAccessManager;

    @BeforeEach
    void setup() {
        netAccessManager = new NetAccessManager(stunStack, peerUdpMessageEventHandler, channelDataEventHandler);
    }

    @AfterEach
    void tearDown() {
        netAccessManager.stop();
    }

    @Test
    void testGetUdpMessageEventHandler() {
        // Given
        // When
        PeerUdpMessageEventHandler udpMessageEventHandler = netAccessManager.getUdpMessageEventHandler();
        // Then
        assertEquals(peerUdpMessageEventHandler, udpMessageEventHandler);
    }

    @Test
    void testGetChannelDataMessageEventHandler() {
        // Given
        // When
        ChannelDataEventHandler channelDataMessageEventHandler = netAccessManager.getChannelDataMessageEventHandler();
        // Then
        assertEquals(channelDataEventHandler, channelDataMessageEventHandler);
    }

    @Test
    void testHandleError() {
        // Given
        String message = "Test error message";
        Throwable error = new RuntimeException("Test error");
        // When
        netAccessManager.handleError(message, error);
        // Then
        verify(stunStack, never()).addSocket(any());
    }

    @Test
    void testHandleFatalError() {
        // Given
        Runnable callingThread = mock(Runnable.class);
        String message = "Test fatal error message";
        Throwable error = new RuntimeException("Test fatal error");
        // When
        netAccessManager.handleFatalError(callingThread, message, error);
        // Then
        verify(stunStack, never()).addSocket(any());
    }

    @Test
    void testStop() {
        // Given
        // When
        netAccessManager.stop();
        // Then
        assertTrue(netAccessManager.isStopped());
    }
}