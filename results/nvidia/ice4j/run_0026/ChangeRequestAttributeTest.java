import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChangeRequestAttributeTest {

    @InjectMocks
    private ChangeRequestAttribute changeRequestAttribute;

    @Test
    public void testGetName() {
        // Given
        String expectedName = "CHANGE-REQUEST";

        // When
        String actualName = changeRequestAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object obj = changeRequestAttribute;

        // When
        boolean result = changeRequestAttribute.equals(obj);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameValues() {
        // Given
        ChangeRequestAttribute otherAttribute = new ChangeRequestAttribute();
        otherAttribute.setChangeIpFlag(changeRequestAttribute.getChangeIpFlag());
        otherAttribute.setChangePortFlag(changeRequestAttribute.getChangePortFlag());

        // When
        boolean result = changeRequestAttribute.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentValues() {
        // Given
        ChangeRequestAttribute otherAttribute = new ChangeRequestAttribute();
        otherAttribute.setChangeIpFlag(!changeRequestAttribute.getChangeIpFlag());
        otherAttribute.setChangePortFlag(changeRequestAttribute.getChangePortFlag());

        // When
        boolean result = changeRequestAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_NullObject() {
        // Given
        Object obj = null;

        // When
        boolean result = changeRequestAttribute.equals(obj);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given
        Object obj = new Object();

        // When
        boolean result = changeRequestAttribute.equals(obj);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetDataLength() {
        // Given
        char expectedLength = 4;

        // When
        char actualLength = changeRequestAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testSetChangeIpFlag() {
        // Given
        boolean expectedValue = true;

        // When
        changeRequestAttribute.setChangeIpFlag(expectedValue);

        // Then
        assertEquals(expectedValue, changeRequestAttribute.getChangeIpFlag());
    }

    @Test
    public void testGetChangeIpFlag() {
        // Given
        boolean expectedValue = true;
        changeRequestAttribute.setChangeIpFlag(expectedValue);

        // When
        boolean actualValue = changeRequestAttribute.getChangeIpFlag();

        // Then
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetChangePortFlag() {
        // Given
        boolean expectedValue = true;

        // When
        changeRequestAttribute.setChangePortFlag(expectedValue);

        // Then
        assertEquals(expectedValue, changeRequestAttribute.getChangePortFlag());
    }

    @Test
    public void testGetChangePortFlag() {
        // Given
        boolean expectedValue = true;
        changeRequestAttribute.setChangePortFlag(expectedValue);

        // When
        boolean actualValue = changeRequestAttribute.getChangePortFlag();

        // Then
        assertEquals(expectedValue, actualValue);
    }
}