import org.ice4j.attribute.DontFragmentAttribute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DontFragmentAttributeTest {

    @InjectMocks
    private DontFragmentAttribute dontFragmentAttribute;

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        // Given: same instance
        Object obj = dontFragmentAttribute;

        // When: equals method is called
        boolean result = dontFragmentAttribute.equals(obj);

        // Then: returns true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_SameClass_ReturnsTrue() {
        // Given: different instance, same class
        Object obj = new DontFragmentAttribute();

        // When: equals method is called
        boolean result = dontFragmentAttribute.equals(obj);

        // Then: returns true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        // Given: different class
        Object obj = new Object();

        // When: equals method is called
        boolean result = dontFragmentAttribute.equals(obj);

        // Then: returns false
        assertFalse(result);
    }

    @Test
    public void testGetName_ReturnsAttributeName() {
        // Given: attribute name
        String expectedName = "DONT-FRAGMENT";

        // When: getName method is called
        String result = dontFragmentAttribute.getName();

        // Then: returns attribute name
        assertEquals(expectedName, result);
    }

    @Test
    public void testGetDataLength_ReturnsDataLength() {
        // Given: data length
        char expectedLength = 0;

        // When: getDataLength method is called
        char result = dontFragmentAttribute.getDataLength();

        // Then: returns data length
        assertEquals(expectedLength, result);
    }
}