import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.ReflectedFromAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReflectedFromAttributeTest {

    private ReflectedFromAttribute reflectedFromAttribute;

    @BeforeEach
    public void setup() {
        reflectedFromAttribute = new ReflectedFromAttribute();
    }

    @Test
    public void testGetName() {
        // Given: A ReflectedFromAttribute instance
        // When: We call the getName method
        String name = reflectedFromAttribute.getName();
        // Then: The name should be "REFLECTED-FROM"
        assertEquals(ReflectedFromAttribute.NAME, name);
    }

    @Test
    public void testGetAttributeName() {
        // Given: A ReflectedFromAttribute instance
        // When: We call the getAttributeName method
        String attributeName = reflectedFromAttribute.getAttributeName();
        // Then: The attribute name should be "REFLECTED-FROM"
        assertEquals(ReflectedFromAttribute.NAME, attributeName);
    }

    @Test
    public void testReflectedFromAttributeConstructor() {
        // Given: A new ReflectedFromAttribute instance
        ReflectedFromAttribute newReflectedFromAttribute = new ReflectedFromAttribute();
        // Then: The new instance should not be null
        assertNotNull(newReflectedFromAttribute);
    }
}