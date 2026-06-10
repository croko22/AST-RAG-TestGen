import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.SourceAddressAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SourceAddressAttributeTest {

    private SourceAddressAttribute sourceAddressAttribute;

    @BeforeEach
    void setup() {
        sourceAddressAttribute = new SourceAddressAttribute();
    }

    @Test
    void testConstructor() {
        // Then: verify the attribute name
        assertEquals(SourceAddressAttribute.NAME, sourceAddressAttribute.getName());
    }

    @Test
    void testGetName() {
        // When: get the attribute name
        String attributeName = sourceAddressAttribute.getName();

        // Then: verify the attribute name
        assertEquals(SourceAddressAttribute.NAME, attributeName);
    }
}