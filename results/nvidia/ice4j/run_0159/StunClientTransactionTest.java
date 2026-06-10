import org.ice4j.stack.StunClientTransaction;
import org.ice4j.stack.StunStack;
import org.ice4j.message.Request;
import org.ice4j.message.StunMessageEvent;
import org.ice4j.util.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StunClientTransactionTest {

    @Mock
    private StunStack stackCallback;

    @Mock
    private Request request;

    @Mock
    private TransportAddress requestDestination;

    @Mock
    private TransportAddress localAddress;

    @Mock
    private org.ice4j.ResponseCollector responseCollector;

    @Mock
    private StunMessageEvent evt;

    private StunClientTransaction stunClientTransaction;

    @BeforeEach
    public void setup() {
        stunClientTransaction = new StunClientTransaction(stackCallback, request, requestDestination, localAddress, responseCollector);
    }

    @Test
    public void testHandleResponse() {
        // Given
        when(evt.getMessage()).thenReturn(request);

        // When
        stunClientTransaction.handleResponse(evt);

        // Then
        verify(responseCollector, times(1)).processResponse(any());
        verify(stackCallback, times(1)).removeClientTransaction(stunClientTransaction);
    }

    @Test
    public void testGetLocalAddress() {
        // When
        TransportAddress localAddress = stunClientTransaction.getLocalAddress();

        // Then
        assertEquals(localAddress, localAddress);
    }

    @Test
    public void testGetRemoteAddress() {
        // When
        TransportAddress remoteAddress = stunClientTransaction.getRemoteAddress();

        // Then
        assertEquals(requestDestination, remoteAddress);
    }

    @Test
    public void testCancel() {
        // When
        stunClientTransaction.cancel();

        // Then
        assertTrue(stunClientTransaction.cancelled.get());
    }
}