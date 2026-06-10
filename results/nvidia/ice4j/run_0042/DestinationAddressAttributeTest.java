import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.DestinationAddressAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DestinationAddressAttributeTest {

    private DestinationAddressAttribute destinationAddressAttribute;

    @BeforeEach
    public void setup() {
        destinationAddressAttribute = new DestinationAddressAttribute();
    }

    @Test
    public void testConstructor() {
        // Then: verify the attribute name
        assertEquals(DestinationAddressAttribute.NAME, destinationAddressAttribute.getName());
    }

    @Test
    public void testGetName() {
        // Given: the attribute is created
        // When: get the attribute name
        String attributeName = destinationAddressAttribute.getName();
        // Then: verify the attribute name
        assertEquals(DestinationAddressAttribute.NAME, attributeName);
    }
}