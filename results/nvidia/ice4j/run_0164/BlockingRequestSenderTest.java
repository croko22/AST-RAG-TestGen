import org.ice4j.stack.StunStack;
import org.ice4j.stunclient.BlockingRequestSender;
import org.ice4j.TransportAddress;
import org.ice4j.message.Request;
import org.ice4j.message.StunResponseEvent;
import org.ice4j.message.StunMessageEvent;
import org.ice4j.message.TransactionID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlockingRequestSenderTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private TransportAddress localAddress;

    @Mock
    private TransportAddress serverAddress;

    @Mock
    private Request request;

    @Mock
    private StunResponseEvent responseEvent;

    @Mock
    private StunMessageEvent messageEvent;

    @Mock
    private TransactionID transactionID;

    private BlockingRequestSender sender;

    @BeforeEach
    public void setup() {
        sender = new BlockingRequestSender(stunStack, localAddress);
    }

    @Test
    public void testGetLocalAddress() {
        // When
        TransportAddress result = sender.getLocalAddress();

        // Then
        assertEquals(localAddress, result);
    }

    @Test
    public void testProcessResponse() {
        // Given
        doNothing().when(stunStack).handleMessageEvent(any());

        // When
        sender.processResponse(responseEvent);

        // Then
        verify(stunStack, never()).handleMessageEvent(any());
    }

    @Test
    public void testSendRequestAndWaitForResponse() throws Exception {
        // Given
        when(stunStack.sendRequest(any(), any(), any(), any())).thenReturn(transactionID);

        // When
        StunMessageEvent result = sender.sendRequestAndWaitForResponse(request, serverAddress);

        // Then
        verify(stunStack).sendRequest(request, serverAddress, localAddress, sender);
        assertNull(result);
    }

    @Test
    public void testSendRequestAndWaitForResponse_WithTransactionID() throws Exception {
        // Given
        when(stunStack.sendRequest(any(), any(), any(), any(), any())).thenReturn(transactionID);

        // When
        StunMessageEvent result = sender.sendRequestAndWaitForResponse(request, serverAddress, transactionID);

        // Then
        verify(stunStack).sendRequest(request, serverAddress, localAddress, sender, transactionID);
        assertNull(result);
    }

    @Test
    public void testSendRequestAndWaitForResponse_WithResponse() throws Exception {
        // Given
        when(stunStack.sendRequest(any(), any(), any(), any())).thenReturn(transactionID);
        sender.processResponse(responseEvent);

        // When
        StunMessageEvent result = sender.sendRequestAndWaitForResponse(request, serverAddress);

        // Then
        verify(stunStack).sendRequest(request, serverAddress, localAddress, sender);
        assertEquals(responseEvent, result);
    }

    @Test
    public void testSendRequestAndWaitForResponse_WithException() throws Exception {
        // Given
        doThrow(new Exception()).when(stunStack).sendRequest(any(), any(), any(), any());

        // When and Then
        assertThrows(Exception.class, () -> sender.sendRequestAndWaitForResponse(request, serverAddress));
    }
}