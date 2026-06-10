import org.ice4j.ice.Agent;
import org.ice4j.ice.ConnectivityCheckServer;
import org.ice4j.stack.StunStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectivityCheckServerTest {

    @Mock
    private Agent parentAgent;

    @Mock
    private StunStack stunStack;

    private ConnectivityCheckServer connectivityCheckServer;

    @BeforeEach
    public void setup() {
        connectivityCheckServer = new ConnectivityCheckServer(parentAgent);
        when(parentAgent.getStunStack()).thenReturn(stunStack);
    }

    @Test
    public void testStart() {
        // Given
        when(stunStack.addRequestListener(any())).thenReturn(true);

        // When
        connectivityCheckServer.start();

        // Then
        verify(stunStack, times(1)).addRequestListener(any());
        assertTrue(connectivityCheckServer.started);
    }

    @Test
    public void testStop() {
        // Given
        connectivityCheckServer.start();

        // When
        connectivityCheckServer.stop();

        // Then
        verify(stunStack, times(1)).removeRequestListener(any());
        assertFalse(connectivityCheckServer.started);
    }

    @Test
    public void testCheckLocalUserName() {
        // Given
        String localUfrag = "localUfrag";
        when(parentAgent.getLocalUfrag()).thenReturn(localUfrag);

        // When
        boolean result = connectivityCheckServer.checkLocalUserName(localUfrag);

        // Then
        assertTrue(result);
    }

    @Test
    public void testCheckLocalUserName_False() {
        // Given
        String localUfrag = "localUfrag";
        when(parentAgent.getLocalUfrag()).thenReturn("otherUfrag");

        // When
        boolean result = connectivityCheckServer.checkLocalUserName(localUfrag);

        // Then
        assertFalse(result);
    }

    @Test
    public void testProcessRequest() {
        // Given
        StunMessageEvent evt = mock(StunMessageEvent.class);
        Request request = mock(Request.class);
        when(evt.getMessage()).thenReturn(request);
        when(parentAgent.incomingCheckReceived(any(), any(), anyLong(), anyString(), anyString(), anyBoolean())).thenReturn(true);

        // When
        connectivityCheckServer.processRequest(evt);

        // Then
        verify(parentAgent, times(1)).incomingCheckReceived(any(), any(), anyLong(), anyString(), anyString(), anyBoolean());
        verify(stunStack, times(1)).sendResponse(any(), any(), any(), any());
    }

    @Test
    public void testProcessRequest_InvalidRequest() {
        // Given
        StunMessageEvent evt = mock(StunMessageEvent.class);
        Request request = mock(Request.class);
        when(evt.getMessage()).thenReturn(request);
        when(parentAgent.incomingCheckReceived(any(), any(), anyLong(), anyString(), anyString(), anyBoolean())).thenReturn(false);

        // When
        assertThrows(IllegalArgumentException.class, () -> connectivityCheckServer.processRequest(evt));

        // Then
        verify(parentAgent, times(1)).incomingCheckReceived(any(), any(), anyLong(), anyString(), anyString(), anyBoolean());
        verify(stunStack, never()).sendResponse(any(), any(), any(), any());
    }
}