import org.ice4j.attribute.RequestedTransportAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequestedTransportAttributeTest {

    private RequestedTransportAttribute requestedTransportAttribute;

    @BeforeEach
    public void setup() {
        requestedTransportAttribute = new RequestedTransportAttribute();
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        // When
        boolean result = requestedTransportAttribute.equals(requestedTransportAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameValues_ReturnsTrue() {
        // Given
        RequestedTransportAttribute otherAttribute = new RequestedTransportAttribute();
        otherAttribute.setRequestedTransport(requestedTransportAttribute.getRequestedTransport());

        // When
        boolean result = requestedTransportAttribute.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentValues_ReturnsFalse() {
        // Given
        RequestedTransportAttribute otherAttribute = new RequestedTransportAttribute();
        otherAttribute.setRequestedTransport((byte) 6); // Different transport protocol

        // When
        boolean result = requestedTransportAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        // When
        boolean result = requestedTransportAttribute.equals(null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        // Given
        Object differentClass = new Object();

        // When
        boolean result = requestedTransportAttribute.equals(differentClass);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetName_ReturnsExpectedName() {
        // When
        String name = requestedTransportAttribute.getName();

        // Then
        assertEquals(RequestedTransportAttribute.NAME, name);
    }

    @Test
    public void testGetDataLength_ReturnsExpectedLength() {
        // When
        char length = requestedTransportAttribute.getDataLength();

        // Then
        assertEquals(RequestedTransportAttribute.DATA_LENGTH, length);
    }

    @Test
    public void testSetRequestedTransport_SetsExpectedValue() {
        // Given
        byte transportProtocol = 6; // TCP

        // When
        requestedTransportAttribute.setRequestedTransport(transportProtocol);

        // Then
        assertEquals(transportProtocol, requestedTransportAttribute.getRequestedTransport());
    }

    @Test
    public void testGetRequestedTransport_ReturnsExpectedValue() {
        // Given
        byte transportProtocol = 17; // UDP
        requestedTransportAttribute.setRequestedTransport(transportProtocol);

        // When
        int result = requestedTransportAttribute.getRequestedTransport();

        // Then
        assertEquals(transportProtocol, result);
    }
}