import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.AlternateServerAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlternateServerAttributeTest {

    private AlternateServerAttribute alternateServerAttribute;

    @BeforeEach
    public void setup() {
        alternateServerAttribute = new AlternateServerAttribute();
    }

    @Test
    public void testConstructor() {
        // Given: no parameters
        // When: creating an instance of AlternateServerAttribute
        AlternateServerAttribute attribute = new AlternateServerAttribute();
        // Then: verify the attribute name
        assertEquals(AlternateServerAttribute.NAME, AlternateServerAttribute.NAME);
    }

    @Test
    public void testGetName() {
        // Given: an instance of AlternateServerAttribute
        // When: getting the attribute name
        String name = alternateServerAttribute.getName();
        // Then: verify the attribute name
        assertEquals(AlternateServerAttribute.NAME, name);
    }
}