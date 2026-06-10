import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.SoftwareAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class SoftwareAttributeTest {

    private SoftwareAttribute softwareAttribute;

    @BeforeEach
    public void setup() {
        softwareAttribute = new SoftwareAttribute();
    }

    @Test
    public void testGetName() {
        // Given
        String expectedName = "SOFTWARE";

        // When
        String actualName = softwareAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetDataLength_WithoutSoftware() {
        // Given
        char expectedLength = 0;

        // When
        char actualLength = softwareAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetDataLength_WithSoftware() {
        // Given
        byte[] software = "Test Software".getBytes();
        softwareAttribute.setSoftware(software);
        char expectedLength = (char) software.length;

        // When
        char actualLength = softwareAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testSetSoftware_Null() {
        // Given
        byte[] software = null;

        // When
        softwareAttribute.setSoftware(software);

        // Then
        assertNull(softwareAttribute.getSoftware());
    }

    @Test
    public void testSetSoftware_NotNull() {
        // Given
        byte[] software = "Test Software".getBytes();

        // When
        softwareAttribute.setSoftware(software);

        // Then
        assertNotNull(softwareAttribute.getSoftware());
        assertTrue(Arrays.equals(software, softwareAttribute.getSoftware()));
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object obj = softwareAttribute;

        // When
        boolean result = softwareAttribute.equals(obj);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameType() {
        // Given
        SoftwareAttribute otherAttribute = new SoftwareAttribute();
        otherAttribute.setSoftware("Test Software".getBytes());
        softwareAttribute.setSoftware("Test Software".getBytes();

        // When
        boolean result = softwareAttribute.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentType() {
        // Given
        Object obj = new Attribute(1);

        // When
        boolean result = softwareAttribute.equals(obj);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentSoftware() {
        // Given
        SoftwareAttribute otherAttribute = new SoftwareAttribute();
        otherAttribute.setSoftware("Different Software".getBytes());
        softwareAttribute.setSoftware("Test Software".getBytes());

        // When
        boolean result = softwareAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }
}