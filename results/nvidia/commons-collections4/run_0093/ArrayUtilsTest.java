import org.apache.commons.collections4.ArrayUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayUtilsTest {

    @Test
    public void testContains_NullArray() {
        // Given: null array
        Object[] array = null;
        Object objectToFind = "test";

        // When: contains is called
        boolean result = ArrayUtils.contains(array, objectToFind);

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testContains_NullObjectToFind() {
        // Given: null object to find
        Object[] array = new Object[] {"test1", "test2"};
        Object objectToFind = null;

        // When: contains is called
        boolean result = ArrayUtils.contains(array, objectToFind);

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testContains_ObjectFound() {
        // Given: object to find is in the array
        Object[] array = new Object[] {"test1", "test2"};
        Object objectToFind = "test1";

        // When: contains is called
        boolean result = ArrayUtils.contains(array, objectToFind);

        // Then: result is true
        assertTrue(result);
    }

    @Test
    public void testContains_ObjectNotFound() {
        // Given: object to find is not in the array
        Object[] array = new Object[] {"test1", "test2"};
        Object objectToFind = "test3";

        // When: contains is called
        boolean result = ArrayUtils.contains(array, objectToFind);

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testIndexOf_NullArray() {
        // Given: null array
        Object[] array = null;
        Object objectToFind = "test";

        // When: indexOf is called
        int result = ArrayUtils.indexOf(array, objectToFind);

        // Then: result is -1
        assertEquals(-1, result);
    }

    @Test
    public void testIndexOf_NullObjectToFind() {
        // Given: null object to find
        Object[] array = new Object[] {"test1", "test2"};
        Object objectToFind = null;

        // When: indexOf is called
        int result = ArrayUtils.indexOf(array, objectToFind);

        // Then: result is -1 if null is not in the array
        assertEquals(-1, result);
    }

    @Test
    public void testIndexOf_NullObjectToFind_NullInArray() {
        // Given: null object to find and null in the array
        Object[] array = new Object[] {"test1", null, "test2"};
        Object objectToFind = null;

        // When: indexOf is called
        int result = ArrayUtils.indexOf(array, objectToFind);

        // Then: result is the index of null in the array
        assertEquals(1, result);
    }

    @Test
    public void testIndexOf_ObjectFound() {
        // Given: object to find is in the array
        Object[] array = new Object[] {"test1", "test2"};
        Object objectToFind = "test1";

        // When: indexOf is called
        int result = ArrayUtils.indexOf(array, objectToFind);

        // Then: result is the index of the object in the array
        assertEquals(0, result);
    }

    @Test
    public void testIndexOf_ObjectNotFound() {
        // Given: object to find is not in the array
        Object[] array = new Object[] {"test1", "test2"};
        Object objectToFind = "test3";

        // When: indexOf is called
        int result = ArrayUtils.indexOf(array, objectToFind);

        // Then: result is -1
        assertEquals(-1, result);
    }

    @Test
    public void testIndexOf_WithStartIndex() {
        // Given: object to find is in the array and start index is specified
        Object[] array = new Object[] {"test1", "test2", "test3"};
        Object objectToFind = "test3";
        int startIndex = 1;

        // When: indexOf is called with start index
        int result = ArrayUtils.indexOf(array, objectToFind, startIndex);

        // Then: result is the index of the object in the array
        assertEquals(2, result);
    }

    @Test
    public void testIndexOf_WithStartIndex_LessThanZero() {
        // Given: object to find is in the array and start index is less than zero
        Object[] array = new Object[] {"test1", "test2", "test3"};
        Object objectToFind = "test3";
        int startIndex = -1;

        // When: indexOf is called with start index less than zero
        int result = ArrayUtils.indexOf(array, objectToFind, startIndex);

        // Then: result is the index of the object in the array
        assertEquals(2, result);
    }

    @Test
    public void testIndexOf_WithStartIndex_GreaterThanArrayLength() {
        // Given: object to find is in the array and start index is greater than array length
        Object[] array = new Object[] {"test1", "test2", "test3"};
        Object objectToFind = "test3";
        int startIndex = 4;

        // When: indexOf is called with start index greater than array length
        int result = ArrayUtils.indexOf(array, objectToFind, startIndex);

        // Then: result is -1
        assertEquals(-1, result);
    }
}