import org.ice4j.attribute.ConnectionIdAttribute;
import org.ice4j.StunException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConnectionIdAttributeTest {

    private ConnectionIdAttribute connectionIdAttribute;

    @BeforeEach
    public void setup() {
        connectionIdAttribute = new ConnectionIdAttribute();
    }

    @Test
    public void testGetDataLength() {
        // Given: connectionIdAttribute is initialized
        // When: getDataLength is called
        char dataLength = connectionIdAttribute.getDataLength();
        // Then: data length should be 4
        assertEquals(4, dataLength);
    }

    @Test
    public void testGetName() {
        // Given: connectionIdAttribute is initialized
        // When: getName is called
        String name = connectionIdAttribute.getName();
        // Then: name should be "CONNECTION-ID"
        assertEquals("CONNECTION-ID", name);
    }

    @Test
    public void testEquals_SameObject() {
        // Given: connectionIdAttribute is initialized
        // When: equals is called with the same object
        boolean result = connectionIdAttribute.equals(connectionIdAttribute);
        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameValues() {
        // Given: two connectionIdAttributes with the same values
        ConnectionIdAttribute otherAttribute = new ConnectionIdAttribute();
        otherAttribute.setConnectionIdValue(connectionIdAttribute.getConnectionIdValue());
        // When: equals is called
        boolean result = connectionIdAttribute.equals(otherAttribute);
        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentValues() {
        // Given: two connectionIdAttributes with different values
        ConnectionIdAttribute otherAttribute = new ConnectionIdAttribute();
        otherAttribute.setConnectionIdValue(12345);
        connectionIdAttribute.setConnectionIdValue(67890);
        // When: equals is called
        boolean result = connectionIdAttribute.equals(otherAttribute);
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testEquals_NullObject() {
        // Given: null object
        // When: equals is called
        boolean result = connectionIdAttribute.equals(null);
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: object of a different class
        Object object = new Object();
        // When: equals is called
        boolean result = connectionIdAttribute.equals(object);
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testGetConnectionIdValue() {
        // Given: connectionIdAttribute is initialized
        // When: getConnectionIdValue is called
        int connectionIdValue = connectionIdAttribute.getConnectionIdValue();
        // Then: connectionIdValue should be 0 (default value)
        assertEquals(0, connectionIdValue);
    }

    @Test
    public void testSetConnectionIdValue() {
        // Given: connectionIdAttribute is initialized
        int connectionIdValue = 12345;
        // When: setConnectionIdValue is called
        connectionIdAttribute.setConnectionIdValue(connectionIdValue);
        // Then: getConnectionIdValue should return the set value
        assertEquals(connectionIdValue, connectionIdAttribute.getConnectionIdValue());
    }
}