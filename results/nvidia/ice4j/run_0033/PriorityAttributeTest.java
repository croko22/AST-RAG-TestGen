import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PriorityAttributeTest {

    private PriorityAttribute priorityAttribute;

    @BeforeEach
    public void setup() {
        priorityAttribute = new PriorityAttribute();
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        // Given: same object
        Object obj = priorityAttribute;

        // When: equals method is called
        boolean result = priorityAttribute.equals(obj);

        // Then: returns true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameAttributes_ReturnsTrue() {
        // Given: different object with same attributes
        PriorityAttribute otherPriorityAttribute = new PriorityAttribute();
        otherPriorityAttribute.setPriority(priorityAttribute.getPriority());

        // When: equals method is called
        boolean result = priorityAttribute.equals(otherPriorityAttribute);

        // Then: returns true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentAttributes_ReturnsFalse() {
        // Given: different object with different attributes
        PriorityAttribute otherPriorityAttribute = new PriorityAttribute();
        otherPriorityAttribute.setPriority(priorityAttribute.getPriority() + 1);

        // When: equals method is called
        boolean result = priorityAttribute.equals(otherPriorityAttribute);

        // Then: returns false
        assertFalse(result);
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        // Given: null object
        Object obj = null;

        // When: equals method is called
        boolean result = priorityAttribute.equals(obj);

        // Then: returns false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        // Given: different class object
        Object obj = new Object();

        // When: equals method is called
        boolean result = priorityAttribute.equals(obj);

        // Then: returns false
        assertFalse(result);
    }

    @Test
    public void testGetDataLength_ReturnsCorrectValue() {
        // Given: priority attribute
        PriorityAttribute attribute = new PriorityAttribute();

        // When: getDataLength method is called
        char result = attribute.getDataLength();

        // Then: returns correct value
        assertEquals(4, result);
    }

    @Test
    public void testGetName_ReturnsCorrectValue() {
        // Given: priority attribute
        PriorityAttribute attribute = new PriorityAttribute();

        // When: getName method is called
        String result = attribute.getName();

        // Then: returns correct value
        assertEquals("PRIORITY", result);
    }

    @Test
    public void testGetPriority_ReturnsCorrectValue() {
        // Given: priority attribute with set priority
        PriorityAttribute attribute = new PriorityAttribute();
        long priority = 12345;
        attribute.setPriority(priority);

        // When: getPriority method is called
        long result = attribute.getPriority();

        // Then: returns correct value
        assertEquals(priority, result);
    }

    @Test
    public void testSetPriority_ValidPriority_ValueIsSet() {
        // Given: priority attribute
        PriorityAttribute attribute = new PriorityAttribute();
        long priority = 12345;

        // When: setPriority method is called
        attribute.setPriority(priority);

        // Then: value is set
        assertEquals(priority, attribute.getPriority());
    }

    @Test
    public void testSetPriority_InvalidPriority_ThrowsIllegalArgumentException() {
        // Given: priority attribute
        PriorityAttribute attribute = new PriorityAttribute();
        long priority = 0;

        // When: setPriority method is called
        assertThrows(IllegalArgumentException.class, () -> attribute.setPriority(priority));
    }

    @Test
    public void testSetPriority_TooLargePriority_ThrowsIllegalArgumentException() {
        // Given: priority attribute
        PriorityAttribute attribute = new PriorityAttribute();
        long priority = 0x7FFFFFFFL + 1;

        // When: setPriority method is called
        assertThrows(IllegalArgumentException.class, () -> attribute.setPriority(priority));
    }
}