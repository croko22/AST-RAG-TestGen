import org.ice4j.stack.StunServerTransaction;
import org.ice4j.stack.StunStack;
import org.ice4j.TransactionID;
import org.ice4j.TransportAddress;
import org.ice4j.message.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StunServerTransactionTest {

    @Mock
    private StunStack stackCallback;

    @Mock
    private TransportAddress localListeningAddress;

    @Mock
    private TransportAddress requestSource;

    @Mock
    private Response response;

    @Mock
    private TransportAddress sendThrough;

    @Mock
    private TransportAddress sendTo;

    private StunServerTransaction transaction;

    @BeforeEach
    public void setup() {
        TransactionID transactionID = mock(TransactionID.class);
        transaction = new StunServerTransaction(stackCallback, transactionID, localListeningAddress, requestSource);
    }

    @Test
    public void testStart() {
        // Given
        when(stackCallback.getNetAccessManager()).thenReturn(mock(StunStack.NetAccessManager.class));

        // When
        transaction.start();

        // Then
        assertFalse(transaction.isExpired());
        assertNotNull(transaction.expirationTime);
    }

    @Test
    public void testStart_AlreadyStarted() {
        // Given
        transaction.start();

        // When and Then
        assertThrows(IllegalStateException.class, () -> transaction.start());
    }

    @Test
    public void testSendResponse() throws IOException {
        // Given
        when(stackCallback.getNetAccessManager()).thenReturn(mock(StunStack.NetAccessManager.class));

        // When
        transaction.sendResponse(response, sendThrough, sendTo);

        // Then
        assertTrue(transaction.isRetransmitting());
        assertEquals(response, transaction.getResponse());
        assertEquals(sendThrough, transaction.getSendingAddress());
        assertEquals(sendTo, transaction.getResponseDestinationAddress());
        verify(stackCallback.getNetAccessManager(), times(1)).sendMessage(any(), any(), any());
    }

    @Test
    public void testExpire() {
        // Given
        transaction.start();

        // When
        transaction.expire();

        // Then
        assertTrue(transaction.isExpired());
    }

    @Test
    public void testIsExpired() {
        // Given
        transaction.start();

        // When and Then
        assertFalse(transaction.isExpired());
    }

    @Test
    public void testIsExpired_Long() {
        // Given
        transaction.start();

        // When and Then
        assertFalse(transaction.isExpired(System.currentTimeMillis() + 1000));
    }

    @Test
    public void testGetTransactionID() {
        // Given
        TransactionID transactionID = mock(TransactionID.class);
        StunServerTransaction transaction = new StunServerTransaction(stackCallback, transactionID, localListeningAddress, requestSource);

        // When and Then
        assertEquals(transactionID, transaction.getTransactionID());
    }

    @Test
    public void testIsRetransmitting() {
        // Given
        transaction.sendResponse(response, sendThrough, sendTo);

        // When and Then
        assertTrue(transaction.isRetransmitting());
    }

    @Test
    public void testGetSendingAddress() {
        // Given
        transaction.sendResponse(response, sendThrough, sendTo);

        // When and Then
        assertEquals(sendThrough, transaction.getSendingAddress());
    }

    @Test
    public void testGetResponseDestinationAddress() {
        // Given
        transaction.sendResponse(response, sendThrough, sendTo);

        // When and Then
        assertEquals(sendTo, transaction.getResponseDestinationAddress());
    }

    @Test
    public void testGetLocalListeningAddress() {
        // When and Then
        assertEquals(localListeningAddress, transaction.getLocalListeningAddress());
    }

    @Test
    public void testGetRequestSourceAddress() {
        // When and Then
        assertEquals(requestSource, transaction.getRequestSourceAddress());
    }

    @Test
    public void testGetResponse() {
        // Given
        transaction.sendResponse(response, sendThrough, sendTo);

        // When and Then
        assertEquals(response, transaction.getResponse());
    }
}