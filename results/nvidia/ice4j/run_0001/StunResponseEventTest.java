import org.ice4j.StunResponseEvent;
import org.ice4j.message.Request;
import org.ice4j.message.Response;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransactionID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StunResponseEventTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @Mock
    private TransactionID transactionID;

    private StunResponseEvent stunResponseEvent;

    @BeforeEach
    public void setup() {
        stunResponseEvent = new StunResponseEvent(stunStack, null, response, request, transactionID);
    }

    @Test
    public void testGetRequest() {
        // Given: setup in @BeforeEach

        // When: get the request
        Request result = stunResponseEvent.getRequest();

        // Then: verify the result
        assertEquals(request, result);
    }

    @Test
    public void testGetResponse() {
        // Given: setup in @BeforeEach

        // When: get the response
        Response result = stunResponseEvent.getResponse();

        // Then: verify the result
        assertEquals(response, result);
    }

    @Test
    public void testConstructor() {
        // Given: 
        StunStack newStunStack = new StunStack();
        Request newRequest = new Request();
        Response newResponse = new Response();
        TransactionID newTransactionID = new TransactionID();

        // When: create a new StunResponseEvent
        StunResponseEvent newStunResponseEvent = new StunResponseEvent(newStunStack, null, newResponse, newRequest, newTransactionID);

        // Then: verify the result
        assertNotNull(newStunResponseEvent);
        assertEquals(newRequest, newStunResponseEvent.getRequest());
        assertEquals(newResponse, newStunResponseEvent.getResponse());
    }

    @Test
    public void testNullRequest() {
        // Given: 
        Request newRequest = null;

        // When / Then: create a new StunResponseEvent with null request
        assertThrows(NullPointerException.class, () -> new StunResponseEvent(stunStack, null, response, newRequest, transactionID));
    }

    @Test
    public void testNullResponse() {
        // Given: 
        Response newResponse = null;

        // When / Then: create a new StunResponseEvent with null response
        assertThrows(NullPointerException.class, () -> new StunResponseEvent(stunStack, null, newResponse, request, transactionID));
    }

    @Test
    public void testNullTransactionID() {
        // Given: 
        TransactionID newTransactionID = null;

        // When / Then: create a new StunResponseEvent with null transactionID
        assertThrows(NullPointerException.class, () -> new StunResponseEvent(stunStack, null, response, request, newTransactionID));
    }
}