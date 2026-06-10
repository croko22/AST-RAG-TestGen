import org.ice4j.ice.Component;
import org.ice4j.ice.harvest.HarvestStatistics;
import org.ice4j.ice.harvest.SinglePortUdpHarvester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SinglePortUdpHarvesterTest {

    @Mock
    private Component component;

    private SinglePortUdpHarvester harvester;

    @BeforeEach
    public void setup() throws IOException {
        harvester = new SinglePortUdpHarvester(new InetSocketAddress("localhost", 0));
    }

    @AfterEach
    public void tearDown() {
        harvester.free();
    }

    @Test
    public void testCreateHarvesters() {
        List<SinglePortUdpHarvester> harvesters = SinglePortUdpHarvester.createHarvesters(1234);
        assertNotNull(harvesters);
        assertFalse(harvesters.isEmpty());
    }

    @Test
    public void testGetHarvestStatistics() {
        HarvestStatistics statistics = harvester.getHarvestStatistics();
        assertNotNull(statistics);
    }

    @Test
    public void testHarvest() {
        Collection<LocalCandidate> candidates = harvester.harvest(component);
        assertNotNull(candidates);
        assertFalse(candidates.isEmpty());
    }

    @Test
    public void testIsHostHarvester() {
        boolean isHostHarvester = harvester.isHostHarvester();
        assertTrue(isHostHarvester);
    }

    @Test
    public void testFree() {
        harvester.free();
        // No exception expected
    }

    @Test
    public void testMaybeAcceptNewSession_UnknownUfrag() {
        InetSocketAddress remoteAddress = new InetSocketAddress("localhost", 1234);
        String ufrag = "unknown";
        MySocket socket = harvester.maybeAcceptNewSession(null, remoteAddress, ufrag);
        assertNull(socket);
    }

    @Test
    public void testMaybeAcceptNewSession_KnownUfrag() throws SocketException, IOException {
        InetSocketAddress remoteAddress = new InetSocketAddress("localhost", 1234);
        String ufrag = "known";
        MyCandidate candidate = new SinglePortUdpHarvester.MyCandidate(component, ufrag);
        harvester.candidates.put(ufrag, candidate);
        MySocket socket = harvester.maybeAcceptNewSession(null, remoteAddress, ufrag);
        assertNotNull(socket);
    }

    @Test
    public void testMaybeAcceptNewSession_SocketException() throws SocketException, IOException {
        InetSocketAddress remoteAddress = new InetSocketAddress("localhost", 1234);
        String ufrag = "known";
        MyCandidate candidate = new SinglePortUdpHarvester.MyCandidate(component, ufrag);
        harvester.candidates.put(ufrag, candidate);
        doThrow(new SocketException()).when(harvester).addSocket(any(), any(), any());
        MySocket socket = harvester.maybeAcceptNewSession(null, remoteAddress, ufrag);
        assertNull(socket);
    }
}