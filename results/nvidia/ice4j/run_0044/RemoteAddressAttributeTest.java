import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.RemoteAddressAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RemoteAddressAttributeTest {

    private RemoteAddressAttribute remoteAddressAttribute;

    @BeforeEach
    public void setup() {
        remoteAddressAttribute = new RemoteAddressAttribute();
    }

    @Test
    public void testConstructor() {
        // Given: no arguments
        // When: creating a new instance
        RemoteAddressAttribute attribute = new RemoteAddressAttribute();
        // Then: verify the attribute name
        assertEquals(RemoteAddressAttribute.NAME, attribute.getName());
    }

    @Test
    public void testGetName() {
        // Given: a RemoteAddressAttribute instance
        // When: getting the attribute name
        String name = remoteAddressAttribute.getName();
        // Then: verify the attribute name
        assertEquals(RemoteAddressAttribute.NAME, name);
    }
}