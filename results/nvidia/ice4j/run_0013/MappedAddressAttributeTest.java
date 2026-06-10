import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.MappedAddressAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MappedAddressAttributeTest {

    private MappedAddressAttribute mappedAddressAttribute;

    @BeforeEach
    public void setup() {
        mappedAddressAttribute = new MappedAddressAttribute();
    }

    @Test
    public void testConstructor() {
        // Given: a new instance of MappedAddressAttribute
        // When: the constructor is called
        // Then: the attribute name is set to "MAPPED-ADDRESS"
        assertEquals(MappedAddressAttribute.NAME, mappedAddressAttribute.getName());
    }

    @Test
    public void testGetName() {
        // Given: an instance of MappedAddressAttribute
        // When: the getName method is called
        // Then: the attribute name is returned
        assertEquals(MappedAddressAttribute.NAME, mappedAddressAttribute.getName());
    }
}