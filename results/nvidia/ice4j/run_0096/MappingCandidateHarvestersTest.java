import org.ice4j.ice.harvest.MappingCandidateHarvester;
import org.ice4j.message.TransportAddress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MappingCandidateHarvestersTest {

    @Mock
    private MappingCandidateHarvester mockHarvester;

    @BeforeEach
    void setup() {
        // Setup code here
    }

    @AfterEach
    void tearDown() {
        // Teardown code here
    }

    @Test
    public void testGetHarvesters() {
        // Given
        MappingCandidateHarvesters.initialize();

        // When
        MappingCandidateHarvester[] harvesters = MappingCandidateHarvesters.getHarvesters();

        // Then
        assertNotNull(harvesters);
    }

    @Test
    public void testFindHarvesterForAddress_Found() {
        // Given
        TransportAddress publicAddress = new TransportAddress("127.0.0.1", 1234, TransportAddress.TransportProtocol.UDP);
        when(mockHarvester.publicAddressMatches(publicAddress)).thenReturn(true);
        List<MappingCandidateHarvester> harvesters = new ArrayList<>();
        harvesters.add(mockHarvester);
        MappingCandidateHarvesters.harvesters = harvesters.toArray(new MappingCandidateHarvester[0]);

        // When
        MappingCandidateHarvester harvester = MappingCandidateHarvesters.findHarvesterForAddress(publicAddress);

        // Then
        assertNotNull(harvester);
        assertEquals(mockHarvester, harvester);
    }

    @Test
    public void testFindHarvesterForAddress_NotFound() {
        // Given
        TransportAddress publicAddress = new TransportAddress("127.0.0.1", 1234, TransportAddress.TransportProtocol.UDP);
        when(mockHarvester.publicAddressMatches(publicAddress)).thenReturn(false);
        List<MappingCandidateHarvester> harvesters = new ArrayList<>();
        harvesters.add(mockHarvester);
        MappingCandidateHarvesters.harvesters = harvesters.toArray(new MappingCandidateHarvester[0]);

        // When
        MappingCandidateHarvester harvester = MappingCandidateHarvesters.findHarvesterForAddress(publicAddress);

        // Then
        assertNull(harvester);
    }

    @Test
    public void testInitialize() {
        // Given
        MappingCandidateHarvesters.initialized = false;

        // When
        MappingCandidateHarvesters.initialize();

        // Then
        assertTrue(MappingCandidateHarvesters.initialized);
    }

    @Test
    public void testInitialize_AlreadyInitialized() {
        // Given
        MappingCandidateHarvesters.initialized = true;

        // When
        MappingCandidateHarvesters.initialize();

        // Then
        assertTrue(MappingCandidateHarvesters.initialized);
    }
}