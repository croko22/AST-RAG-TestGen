import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.DataAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.ice.harvest.GoogleTurnCandidateHarvest;
import org.ice4j.message.Message;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Request;
import org.ice4j.message.Response;
import org.ice4j.message.StunMessageEvent;
import org.ice4j.message.StunResponseEvent;
import org.ice4j.message.StunTimeoutEvent;
import org.ice4j.message.TransactionID;
import org.ice4j.socket.GoogleRelayedCandidateDelegate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoogleRelayedCandidateDelegateTest {

    @Mock
    private GoogleTurnCandidateHarvest turnCandidateHarvest;

    @Mock
    private StunMessageEvent stunMessageEvent;

    @Mock
    private Response response;

    @Mock
    private Request request;

    @Mock
    private StunResponseEvent stunResponseEvent;

    @Mock
    private StunTimeoutEvent stunTimeoutEvent;

    private GoogleRelayedCandidateDelegate googleRelayedCandidateDelegate;

    @BeforeEach
    void setup() throws SocketException {
        googleRelayedCandidateDelegate = new GoogleRelayedCandidateDelegate(turnCandidateHarvest, "username");
    }

    @AfterEach
    void tearDown() {
        googleRelayedCandidateDelegate.close();
    }

    @Test
    void testClose() {
        // Given
        AtomicBoolean closed = new AtomicBoolean(false);
        googleRelayedCandidateDelegate = spy(googleRelayedCandidateDelegate);
        doAnswer(invocation -> {
            closed.set(true);
            return null;
        }).when(googleRelayedCandidateDelegate).close();

        // When
        googleRelayedCandidateDelegate.close();

        // Then
        assertTrue(closed.get());
        verify(turnCandidateHarvest).close(any());
    }

    @Test
    void testHandleMessageEvent() {
        // Given
        Message message = mock(Message.class);
        when(stunMessageEvent.getMessage()).thenReturn(message);
        when(message.getMessageType()).thenReturn(Message.OLD_DATA_INDICATION);

        // When
        googleRelayedCandidateDelegate.handleMessageEvent(stunMessageEvent);

        // Then
        verify(googleRelayedCandidateDelegate, times(1)).handleMessageEvent(stunMessageEvent);
    }

    @Test
    void testProcessErrorOrFailure() {
        // Given

        // When
        boolean result = googleRelayedCandidateDelegate.processErrorOrFailure(response, request);

        // Then
        assertFalse(result);
    }

    @Test
    void testProcessSuccess() {
        // Given

        // When
        googleRelayedCandidateDelegate.processSuccess(response, request);

        // Then
        verify(googleRelayedCandidateDelegate, times(1)).processSuccess(response, request);
    }

    @Test
    void testProcessResponse() {
        // Given

        // When
        googleRelayedCandidateDelegate.processResponse(stunResponseEvent);

        // Then
        verify(googleRelayedCandidateDelegate, times(1)).processResponse(stunResponseEvent);
    }

    @Test
    void testProcessTimeout() {
        // Given

        // When
        googleRelayedCandidateDelegate.processTimeout(stunTimeoutEvent);

        // Then
        verify(googleRelayedCandidateDelegate, times(1)).processTimeout(stunTimeoutEvent);
    }

    @Test
    void testReceive() throws IOException {
        // Given
        DatagramPacket packet = mock(DatagramPacket.class);

        // When
        googleRelayedCandidateDelegate.receive(packet);

        // Then
        verify(googleRelayedCandidateDelegate, times(1)).receive(packet);
    }

    @Test
    void testSend() throws IOException {
        // Given
        DatagramPacket packet = mock(DatagramPacket.class);

        // When
        googleRelayedCandidateDelegate.send(packet);

        // Then
        verify(googleRelayedCandidateDelegate, times(1)).send(packet);
    }

    @Test
    void testCreateSendThread() {
        // Given

        // When
        googleRelayedCandidateDelegate.createSendThread();

        // Then
        assertNotNull(googleRelayedCandidateDelegate.sendThread);
    }

    @Test
    void testRunInSendThread() {
        // Given

        // When
        googleRelayedCandidateDelegate.runInSendThread();

        // Then
        verify(googleRelayedCandidateDelegate, times(1)).runInSendThread();
    }
}