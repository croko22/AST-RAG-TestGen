import org.ice4j.attribute.EvenPortAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EvenPortAttributeTest {

    private EvenPortAttribute evenPortAttribute;

    @BeforeEach
    public void setup() {
        evenPortAttribute = new EvenPortAttribute();
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        // When
        boolean result = evenPortAttribute.equals(evenPortAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameValues_ReturnsTrue() {
        // Given
        EvenPortAttribute otherAttribute = new EvenPortAttribute();
        otherAttribute.setRFlag(evenPortAttribute.isRFlag());

        // When
        boolean result = evenPortAttribute.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentValues_ReturnsFalse() {
        // Given
        EvenPortAttribute otherAttribute = new EvenPortAttribute();
        otherAttribute.setRFlag(!evenPortAttribute.isRFlag());

        // When
        boolean result = evenPortAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        // When
        boolean result = evenPortAttribute.equals(null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        // Given
        Object differentClass = new Object();

        // When
        boolean result = evenPortAttribute.equals(differentClass);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetName_ReturnsEvenPort() {
        // When
        String result = evenPortAttribute.getName();

        // Then
        assertEquals("EVEN-PORT", result);
    }

    @Test
    public void testGetDataLength_ReturnsOne() {
        // When
        char result = evenPortAttribute.getDataLength();

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testSetRFlag_SetToTrue() {
        // Given
        boolean rFlag = true;

        // When
        evenPortAttribute.setRFlag(rFlag);

        // Then
        assertTrue(evenPortAttribute.isRFlag());
    }

    @Test
    public void testSetRFlag_SetToFalse() {
        // Given
        boolean rFlag = false;

        // When
        evenPortAttribute.setRFlag(rFlag);

        // Then
        assertFalse(evenPortAttribute.isRFlag());
    }

    @Test
    public void testIsRFlag_ReturnsTrue() {
        // Given
        evenPortAttribute.setRFlag(true);

        // When
        boolean result = evenPortAttribute.isRFlag();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsRFlag_ReturnsFalse() {
        // Given
        evenPortAttribute.setRFlag(false);

        // When
        boolean result = evenPortAttribute.isRFlag();

        // Then
        assertFalse(result);
    }
}