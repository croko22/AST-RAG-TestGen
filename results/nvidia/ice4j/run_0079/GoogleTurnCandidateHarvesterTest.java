import org.ice4j.ice.harvest.GoogleTurnCandidateHarvester;
import org.ice4j.ice.harvest.StunCandidateHarvester;
import org.ice4j.message.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class GoogleTurnCandidateHarvesterTest {

    @Mock
    private TransportAddress turnServer;

    private GoogleTurnCandidateHarvester harvester;

    @BeforeEach
    public void setup() {
        harvester = new GoogleTurnCandidateHarvester(turnServer);
    }

    @Test
    public void testGetPassword_NoPasswordProvided() {
        // Given: no password provided
        // When: getPassword is called
        String password = harvester.getPassword();
        // Then: password is null
        assertNull(password);
    }

    @Test
    public void testGetPassword_WithPassword() {
        // Given: password provided
        String password = "testPassword";
        harvester = new GoogleTurnCandidateHarvester(turnServer, null, password);
        // When: getPassword is called
        String retrievedPassword = harvester.getPassword();
        // Then: password is returned
        assertEquals(password, retrievedPassword);
    }

    @Test
    public void testCreateHarvest() {
        // Given: host candidate
        HostCandidate hostCandidate = new HostCandidate(turnServer, 1);
        // When: createHarvest is called
        GoogleTurnCandidateHarvest harvest = harvester.createHarvest(hostCandidate);
        // Then: harvest is not null
        assertNotNull(harvest);
    }
}