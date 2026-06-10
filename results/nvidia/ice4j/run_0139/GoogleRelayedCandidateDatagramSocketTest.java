import org.ice4j.ice.GoogleRelayedCandidate;
import org.ice4j.ice.harvest.GoogleTurnCandidateHarvest;
import org.ice4j.message.Request;
import org.ice4j.message.Response;
import org.ice4j.socket.GoogleRelayedCandidateDatagramSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoogleRelayedCandidateDatagramSocketTest {

    @Mock
    private GoogleRelayedCandidate relayedCandidate;

    @Mock
    private GoogleTurnCandidateHarvest turnCandidateHarvest;

    @Mock
    private Response response;

    @Mock
    private Request request;

    @Mock
    private DatagramPacket datagramPacket;

    private GoogleRelayedCandidateDatagramSocket socket;

    @BeforeEach
    public void setup() throws SocketException {
        socket = new GoogleRelayedCandidateDatagramSocket(relayedCandidate, turnCandidateHarvest, "username");
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(relayedCandidate, turnCandidateHarvest, response, request, datagramPacket);
    }

    @Test
    public void testClose() {
        // When
        socket.close();

        // Then
        verify(turnCandidateHarvest, times(1)).close(socket);
    }

    @Test
    public void testGetLocalAddress() {
        // Given
        when(relayedCandidate.getTransportAddress()).thenReturn(new InetSocketAddress("localhost", 1234));

        // When
        InetAddress localAddress = socket.getLocalAddress();

        // Then
        assertEquals(InetAddress.getByName("localhost"), localAddress);
    }

    @Test
    public void testGetLocalPort() {
        // Given
        when(relayedCandidate.getTransportAddress()).thenReturn(new InetSocketAddress("localhost", 1234));

        // When
        int localPort = socket.getLocalPort();

        // Then
        assertEquals(1234, localPort);
    }

    @Test
    public void testGetLocalSocketAddress() {
        // Given
        when(relayedCandidate.getTransportAddress()).thenReturn(new InetSocketAddress("localhost", 1234));

        // When
        SocketAddress localSocketAddress = socket.getLocalSocketAddress();

        // Then
        assertEquals(new InetSocketAddress("localhost", 1234), localSocketAddress);
    }

    @Test
    public void testGetRelayedCandidate() {
        // When
        GoogleRelayedCandidate candidate = socket.getRelayedCandidate();

        // Then
        assertEquals(relayedCandidate, candidate);
    }

    @Test
    public void testProcessSuccess() {
        // When
        socket.processSuccess(response, request);

        // Then
        verify(relayedCandidate, never()).getTransportAddress();
    }

    @Test
    public void testProcessResponse() {
        // When
        socket.processResponse(mock(StunResponseEvent.class));

        // Then
        verify(relayedCandidate, never()).getTransportAddress();
    }

    @Test
    public void testReceive() throws IOException {
        // When
        socket.receive(datagramPacket);

        // Then
        verify(relayedCandidate, never()).getTransportAddress();
    }

    @Test
    public void testSend() throws IOException {
        // When
        socket.send(datagramPacket);

        // Then
        verify(relayedCandidate, never()).getTransportAddress();
    }
}