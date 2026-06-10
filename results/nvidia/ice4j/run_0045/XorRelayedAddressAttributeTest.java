import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.attribute.XorRelayedAddressAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class XorRelayedAddressAttributeTest {

    private XorRelayedAddressAttribute xorRelayedAddressAttribute;

    @BeforeEach
    public void setup() {
        xorRelayedAddressAttribute = new XorRelayedAddressAttribute();
    }

    @Test
    public void testConstructor() {
        // Given: default constructor
        // When: create instance
        XorRelayedAddressAttribute attribute = new XorRelayedAddressAttribute();
        // Then: verify instance
        assertNotNull(attribute);
        assertEquals(XorRelayedAddressAttribute.NAME, ((XorMappedAddressAttribute) attribute).getName());
    }

    @Test
    public void testGetName() {
        // Given: instance with default constructor
        // When: get name
        String name = ((XorMappedAddressAttribute) xorRelayedAddressAttribute).getName();
        // Then: verify name
        assertEquals(XorRelayedAddressAttribute.NAME, name);
    }
}