import org.ice4j.message.Message;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StunTimeoutEventTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private Message message;

    @Mock
    private TransportAddress localAddress;

    private StunTimeoutEvent stunTimeoutEvent;

    @BeforeEach
    public void setup() {
        // Given: a StunTimeoutEvent instance
        stunTimeoutEvent = new StunTimeoutEvent(stunStack, message, localAddress, new TransactionID());
    }

    @Test
    public void testGetLocalAddress() {
        // When: getLocalAddress is called
        TransportAddress result = stunTimeoutEvent.getLocalAddress();

        // Then: the local address is returned
        assertEquals(localAddress, result);
    }

    @Test
    public void testToString() {
        // Given: a message and local address
        when(message.toString()).thenReturn("MockMessage");
        when(localAddress.toString()).thenReturn("MockLocalAddress");

        // When: toString is called
        String result = stunTimeoutEvent.toString();

        // Then: a string representation of the event is returned
        assertNotNull(result);
        assertEquals("StunTimeoutEvent:\n\tMessage=MockMessage localAddr=MockLocalAddress", result);
    }
}