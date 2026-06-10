import org.ice4j.ice.ServerReflexiveCandidate;
import org.ice4j.ice.harvest.StunCandidateHarvest;
import org.ice4j.message.Request;
import org.ice4j.message.Response;
import org.ice4j.socket.IceSocketWrapper;
import org.ice4j.transport.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServerReflexiveCandidateTest {

    @Mock
    private TransportAddress address;

    @Mock
    private TransportAddress stunSrvrAddr;

    @Mock
    private StunCandidateHarvest stunHarvest;

    @Mock
    private IceSocketWrapper iceSocketWrapper;

    private ServerReflexiveCandidate serverReflexiveCandidate;

    @BeforeEach
    void setup() {
        serverReflexiveCandidate = new ServerReflexiveCandidate(address, mock(HostCandidate.class), stunSrvrAddr, stunHarvest, mock(CandidateExtendedType.class));
    }

    @Test
    void testGetCandidateIceSocketWrapper() {
        // Given
        when(serverReflexiveCandidate.getBase().getCandidateIceSocketWrapper()).thenReturn(iceSocketWrapper);

        // When
        IceSocketWrapper result = serverReflexiveCandidate.getCandidateIceSocketWrapper();

        // Then
        assertEquals(iceSocketWrapper, result);
        verify(serverReflexiveCandidate.getBase()).getCandidateIceSocketWrapper();
    }

    @Test
    void testFree() {
        // Given
        doNothing().when(stunHarvest).close();

        // When
        serverReflexiveCandidate.free();

        // Then
        verify(stunHarvest).close();
        verify(serverReflexiveCandidate).free();
    }

    @Test
    void testFreeWithoutStunHarvest() {
        // Given
        serverReflexiveCandidate = new ServerReflexiveCandidate(address, mock(HostCandidate.class), stunSrvrAddr, null, mock(CandidateExtendedType.class));

        // When
        serverReflexiveCandidate.free();

        // Then
        verifyNoInteractions(stunHarvest);
        verify(serverReflexiveCandidate).free();
    }
}