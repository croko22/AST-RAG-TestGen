import org.ice4j.message.Request;
import org.ice4j.message.Response;
import org.ice4j.message.StunResponseEvent;
import org.ice4j.socket.GoogleRelayedCandidate;
import org.ice4j.socket.GoogleRelayedCandidateDelegate;
import org.ice4j.socket.GoogleRelayedCandidateSocket;
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
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoogleRelayedCandidateSocketTest {

    @Mock
    private GoogleRelayedCandidateDelegate socketDelegate;

    @Mock
    private GoogleRelayedCandidate relayedCandidate;

    private GoogleRelayedCandidateSocket googleRelayedCandidateSocket;

    @BeforeEach
    public void setup() throws SocketException {
        googleRelayedCandidateSocket = new GoogleRelayedCandidateSocket(relayedCandidate, null, "username");
        googleRelayedCandidateSocket.socketDelegate = socketDelegate;
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(socketDelegate);
    }

    @Test
    public void testClose() {
        // When
        googleRelayedCandidateSocket.close();

        // Then
        verify(socketDelegate, times(1)).close();
    }

    @Test
    public void testGetLocalAddress() {
        // Given
        InetSocketAddress localSocketAddress = new InetSocketAddress("localhost", 1234);
        when(relayedCandidate.getTransportAddress()).thenReturn(localSocketAddress);

        // When
        InetAddress localAddress = googleRelayedCandidateSocket.getLocalAddress();

        // Then
        assertEquals(localSocketAddress.getAddress(), localAddress);
    }

    @Test
    public void testGetLocalPort() {
        // Given
        InetSocketAddress localSocketAddress = new InetSocketAddress("localhost", 1234);
        when(relayedCandidate.getTransportAddress()).thenReturn(localSocketAddress);

        // When
        int localPort = googleRelayedCandidateSocket.getLocalPort();

        // Then
        assertEquals(localSocketAddress.getPort(), localPort);
    }

    @Test
    public void testGetLocalSocketAddress() {
        // Given
        InetSocketAddress localSocketAddress = new InetSocketAddress("localhost", 1234);
        when(relayedCandidate.getTransportAddress()).thenReturn(localSocketAddress);

        // When
        InetSocketAddress result = googleRelayedCandidateSocket.getLocalSocketAddress();

        // Then
        assertEquals(localSocketAddress, result);
    }

    @Test
    public void testGetRelayedCandidate() {
        // When
        GoogleRelayedCandidate result = googleRelayedCandidateSocket.getRelayedCandidate();

        // Then
        assertEquals(relayedCandidate, result);
    }

    @Test
    public void testProcessSuccess() {
        // Given
        Response response = mock(Response.class);
        Request request = mock(Request.class);

        // When
        googleRelayedCandidateSocket.processSuccess(response, request);

        // Then
        verify(socketDelegate, times(1)).processSuccess(response, request);
    }

    @Test
    public void testProcessResponse() {
        // Given
        StunResponseEvent response = mock(StunResponseEvent.class);

        // When
        googleRelayedCandidateSocket.processResponse(response);

        // Then
        verify(socketDelegate, times(1)).processResponse(response);
    }

    @Test
    public void testReceive() throws IOException {
        // Given
        DatagramPacket packet = mock(DatagramPacket.class);

        // When
        googleRelayedCandidateSocket.receive(packet);

        // Then
        verify(socketDelegate, times(1)).receive(packet);
    }

    @Test
    public void testSend() throws IOException {
        // Given
        DatagramPacket packet = mock(DatagramPacket.class);

        // When
        googleRelayedCandidateSocket.send(packet);

        // Then
        verify(socketDelegate, times(1)).send(packet);
    }

    @Test
    public void testGetOutputStream() throws IOException {
        // Given
        CandidatePair pair = mock(CandidatePair.class);
        when(relayedCandidate.getParentComponent().getSelectedPair()).thenReturn(pair);
        TransportAddress target = mock(TransportAddress.class);
        when(pair.getRemoteCandidate().getTransportAddress()).thenReturn(target);

        // When
        OutputStream outputStream = googleRelayedCandidateSocket.getOutputStream();

        // Then
        assertNotNull(outputStream);
    }

    @Test
    public void testGetOutputStream_ThrowsIOException() throws IOException {
        // Given
        CandidatePair pair = mock(CandidatePair.class);
        when(relayedCandidate.getParentComponent().getSelectedPair()).thenReturn(pair);
        when(pair.getLocalCandidate()).thenReturn(mock(GoogleRelayedCandidate.class));

        // When and Then
        assertThrows(IOException.class, () -> googleRelayedCandidateSocket.getOutputStream());
    }
}