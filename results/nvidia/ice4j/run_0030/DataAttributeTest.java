import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class DataAttributeTest {

    @Mock
    private byte[] data;

    private DataAttribute dataAttribute;

    @BeforeEach
    void setup() {
        dataAttribute = new DataAttribute();
    }

    @Test
    void testGetName() {
        // Given
        String expectedName = "DATA";

        // When
        String actualName = dataAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    void testGetDataLength_NoData() {
        // Given
        char expectedLength = 0;

        // When
        char actualLength = dataAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    void testGetDataLength_WithData() {
        // Given
        byte[] data = {1, 2, 3, 4};
        dataAttribute.setData(data);
        char expectedLength = (char) data.length;

        // When
        char actualLength = dataAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    void testEquals_SameObject() {
        // Given
        Object obj = dataAttribute;

        // When
        boolean result = dataAttribute.equals(obj);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentObject_SameType() {
        // Given
        DataAttribute otherAttribute = new DataAttribute();
        otherAttribute.setData(dataAttribute.getData());

        // When
        boolean result = dataAttribute.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentObject_DifferentType() {
        // Given
        Object obj = new Object();

        // When
        boolean result = dataAttribute.equals(obj);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_DifferentObject_SameType_DifferentData() {
        // Given
        DataAttribute otherAttribute = new DataAttribute();
        otherAttribute.setData(new byte[]{1, 2, 3, 4});

        // When
        boolean result = dataAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_DifferentObject_SameType_DifferentLength() {
        // Given
        DataAttribute otherAttribute = new DataAttribute();
        otherAttribute.setData(new byte[]{1, 2, 3});

        // When
        boolean result = dataAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    void testSetData_NullData() {
        // Given
        byte[] data = null;

        // When
        dataAttribute.setData(data);

        // Then
        assertNull(dataAttribute.getData());
    }

    @Test
    void testSetData_WithData() {
        // Given
        byte[] data = {1, 2, 3, 4};

        // When
        dataAttribute.setData(data);

        // Then
        assertArrayEquals(data, dataAttribute.getData());
    }
}