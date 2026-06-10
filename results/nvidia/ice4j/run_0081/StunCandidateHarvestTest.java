import org.ice4j.ice.harvest.StunCandidateHarvest;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import org.ice4j.message.Request;
import org.ice4j.message.Response;
import org.ice4j.message.StunResponseEvent;
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
public class StunCandidateHarvestTest {

    @Mock
    private StunCandidateHarvester harvester;

    @Mock
    private StunResponseEvent event;

    private StunCandidateHarvest harvest;

    @BeforeEach
    void setup() {
        harvest = new StunCandidateHarvest(harvester, new HostCandidate());
    }

    @AfterEach
    void tearDown() {
        harvest.close();
    }

    @Test
    void testProcessResponse() {
        // Given
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        when(event.getResponse()).thenReturn(response);
        when(event.getRequest()).thenReturn(request);

        // When
        harvest.processResponse(event);

        // Then
        verify(harvester, times(1)).completedResolvingCandidate(any());
    }

    @Test
    void testClose() {
        // Given
        harvest.setSendKeepAliveMessageInterval(1000);

        // When
        harvest.close();

        // Then
        assertEquals(0, harvest.sendKeepAliveMessageInterval);
    }

    @Test
    void testRun() {
        // Given
        doThrow(new RuntimeException()).when(harvest).runInSendKeepAliveMessageThread();

        // When and Then
        assertDoesNotThrow(() -> new Thread(harvest::run).start());
    }
}