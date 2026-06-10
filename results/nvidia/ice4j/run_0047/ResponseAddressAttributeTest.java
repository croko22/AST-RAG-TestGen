import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.ResponseAddressAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResponseAddressAttributeTest {

    private ResponseAddressAttribute responseAddressAttribute;

    @BeforeEach
    public void setup() {
        responseAddressAttribute = new ResponseAddressAttribute();
    }

    @Test
    public void testConstructor() {
        // Then: verify the attribute name
        assertEquals(ResponseAddressAttribute.NAME, responseAddressAttribute.getName());
    }

    @Test
    public void testGetName() {
        // Given: a ResponseAddressAttribute instance
        // When: get the attribute name
        String attributeName = responseAddressAttribute.getName();
        // Then: verify the attribute name
        assertEquals(ResponseAddressAttribute.NAME, attributeName);
    }
}