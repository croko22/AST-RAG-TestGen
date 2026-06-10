import org.ice4j.message.Message;
import org.ice4j.stack.StunStack;
import org.ice4j.transport.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StunFailureEventTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private Message message;

    @Mock
    private TransportAddress localAddress;

    private StunFailureEvent eventWithCause;
    private StunFailureEvent eventWithoutCause;

    @BeforeEach
    void setup() {
        eventWithCause = new StunFailureEvent(stunStack, message, localAddress, new Exception("Test exception"));
        eventWithoutCause = new StunFailureEvent(stunStack, message, localAddress, null);
    }

    @Test
    void testGetLocalAddress() {
        // Given: eventWithCause and eventWithoutCause are created in setup
        // When: getLocalAddress is called
        TransportAddress localAddressWithCause = eventWithCause.getLocalAddress();
        TransportAddress localAddressWithoutCause = eventWithoutCause.getLocalAddress();
        // Then: local addresses are returned
        assertEquals(localAddress, localAddressWithCause);
        assertEquals(localAddress, localAddressWithoutCause);
    }

    @Test
    void testGetCause() {
        // Given: eventWithCause and eventWithoutCause are created in setup
        // When: getCause is called
        Throwable causeWithCause = eventWithCause.getCause();
        Throwable causeWithoutCause = eventWithoutCause.getCause();
        // Then: causes are returned
        assertNotNull(causeWithCause);
        assertNull(causeWithoutCause);
    }

    @Test
    void testToString() {
        // Given: eventWithCause and eventWithoutCause are created in setup
        // When: toString is called
        String toStringWithCause = eventWithCause.toString();
        String toStringWithoutCause = eventWithoutCause.toString();
        // Then: string representations are returned
        assertNotNull(toStringWithCause);
        assertNotNull(toStringWithoutCause);
        assertEquals("StunFailureEvent:\n\tMessage=" + message + " localAddr=" + localAddress, toStringWithoutCause);
    }

    @Test
    void testConstructorNullStunStack() {
        // Given: null stunStack
        // When: constructor is called with null stunStack
        assertThrows(NullPointerException.class, () -> new StunFailureEvent(null, message, localAddress, new Exception("Test exception")));
    }

    @Test
    void testConstructorNullMessage() {
        // Given: null message
        // When: constructor is called with null message
        assertThrows(NullPointerException.class, () -> new StunFailureEvent(stunStack, null, localAddress, new Exception("Test exception")));
    }

    @Test
    void testConstructorNullLocalAddress() {
        // Given: null localAddress
        // When: constructor is called with null localAddress
        assertThrows(NullPointerException.class, () -> new StunFailureEvent(stunStack, message, null, new Exception("Test exception")));
    }
}