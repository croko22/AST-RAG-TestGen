import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class XorOnlyAttributeTest {

    private XorOnlyAttribute xorOnlyAttribute;

    @BeforeEach
    void setup() {
        xorOnlyAttribute = new XorOnlyAttribute();
    }

    @Test
    void testGetDataLength() {
        // Given: XorOnlyAttribute instance
        // When: calling getDataLength method
        char dataLength = xorOnlyAttribute.getDataLength();
        // Then: verify data length is 0
        assertEquals(0, dataLength);
    }

    @Test
    void testGetName() {
        // Given: XorOnlyAttribute instance
        // When: calling getName method
        String attributeName = xorOnlyAttribute.getName();
        // Then: verify attribute name is "XOR-ONLY"
        assertEquals("XOR-ONLY", attributeName);
    }

    @Test
    void testEquals_SameInstance() {
        // Given: same instance of XorOnlyAttribute
        // When: calling equals method
        boolean isEqual = xorOnlyAttribute.equals(xorOnlyAttribute);
        // Then: verify equals method returns true
        assertTrue(isEqual);
    }

    @Test
    void testEquals_DifferentInstance_SameType() {
        // Given: different instance of XorOnlyAttribute
        XorOnlyAttribute otherXorOnlyAttribute = new XorOnlyAttribute();
        // When: calling equals method
        boolean isEqual = xorOnlyAttribute.equals(otherXorOnlyAttribute);
        // Then: verify equals method returns true
        assertTrue(isEqual);
    }

    @Test
    void testEquals_DifferentType() {
        // Given: instance of different class
        Object differentObject = new Object();
        // When: calling equals method
        boolean isEqual = xorOnlyAttribute.equals(differentObject);
        // Then: verify equals method returns false
        assertFalse(isEqual);
    }

    @Test
    void testEquals_Null() {
        // Given: null object
        // When: calling equals method
        boolean isEqual = xorOnlyAttribute.equals(null);
        // Then: verify equals method returns false
        assertFalse(isEqual);
    }
}