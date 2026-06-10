import org.ice4j.ice.Agent;
import org.ice4j.ice.ConnectivityCheckClient;
import org.ice4j.ice.CheckList;
import org.ice4j.message.StunResponseEvent;
import org.ice4j.message.StunTimeoutEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectivityCheckClientTest {

    @Mock
    private Agent parentAgent;

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    @Mock
    private ExecutorService executorService;

    private ConnectivityCheckClient connectivityCheckClient;

    @BeforeEach
    public void setup() {
        connectivityCheckClient = new ConnectivityCheckClient(parentAgent, scheduledExecutorService, executorService);
    }

    @Test
    public void testStartChecks() {
        // Given
        List<IceMediaStream> streamsWithPendingConnectivityEstablishment = mock(List.class);
        when(parentAgent.getStreamsWithPendingConnectivityEstablishment()).thenReturn(streamsWithPendingConnectivityEstablishment);

        // When
        connectivityCheckClient.startChecks();

        // Then
        verify(parentAgent).getStreamsWithPendingConnectivityEstablishment();
    }

    @Test
    public void testStartChecks_CheckList() {
        // Given
        CheckList checkList = mock(CheckList.class);

        // When
        connectivityCheckClient.startChecks(checkList);

        // Then
        verify(checkList).shouldStartPaceMaker();
    }

    @Test
    public void testProcessResponse() {
        // Given
        StunResponseEvent ev = mock(StunResponseEvent.class);

        // When
        connectivityCheckClient.processResponse(ev);

        // Then
        verify(ev).getTransactionID();
    }

    @Test
    public void testProcessTimeout() {
        // Given
        StunTimeoutEvent ev = mock(StunTimeoutEvent.class);

        // When
        connectivityCheckClient.processTimeout(ev);

        // Then
        verify(ev).getTransactionID();
    }

    @Test
    public void testStop() {
        // When
        connectivityCheckClient.stop();

        // Then
        assertTrue(connectivityCheckClient.isStopped());
    }

    @Test
    public void testIsStopped() {
        // Given
        connectivityCheckClient.stop();

        // When
        boolean stopped = connectivityCheckClient.isStopped();

        // Then
        assertTrue(stopped);
    }

    @Test
    public void testIsAlive() {
        // Given
        connectivityCheckClient.processResponse(mock(StunResponseEvent.class));

        // When
        boolean alive = connectivityCheckClient.isAlive();

        // Then
        assertTrue(alive);
    }
}