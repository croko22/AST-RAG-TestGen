import org.apache.commons.collections4.iterators.ArrayIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayIteratorTest {

    private ArrayIterator<String> arrayIterator;
    private String[] array;

    @BeforeEach
    void setup() {
        array = new String[] {"apple", "banana", "cherry"};
        arrayIterator = new ArrayIterator<>(array);
    }

    @Test
    void testGetArray() {
        // Given: an array iterator
        // When: getArray is called
        Object result = arrayIterator.getArray();
        // Then: the result should be the original array
        assertSame(array, result);
    }

    @Test
    void testGetEndIndex() {
        // Given: an array iterator
        // When: getEndIndex is called
        int result = arrayIterator.getEndIndex();
        // Then: the result should be the length of the array
        assertEquals(array.length, result);
    }

    @Test
    void testGetStartIndex() {
        // Given: an array iterator
        // When: getStartIndex is called
        int result = arrayIterator.getStartIndex();
        // Then: the result should be 0
        assertEquals(0, result);
    }

    @Test
    void testHasNext() {
        // Given: an array iterator
        // When: hasNext is called
        boolean result = arrayIterator.hasNext();
        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    void testHasNext_False() {
        // Given: an array iterator that has iterated over all elements
        ArrayIterator<String> iterator = new ArrayIterator<>(array);
        for (int i = 0; i < array.length; i++) {
            iterator.next();
        }
        // When: hasNext is called
        boolean result = iterator.hasNext();
        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    void testNext() {
        // Given: an array iterator
        // When: next is called
        String result = arrayIterator.next();
        // Then: the result should be the first element of the array
        assertEquals(array[0], result);
    }

    @Test
    void testNext_NoMoreElements() {
        // Given: an array iterator that has iterated over all elements
        ArrayIterator<String> iterator = new ArrayIterator<>(array);
        for (int i = 0; i < array.length; i++) {
            iterator.next();
        }
        // When: next is called
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void testRemove() {
        // Given: an array iterator
        // When: remove is called
        assertThrows(UnsupportedOperationException.class, () -> arrayIterator.remove());
    }

    @Test
    void testReset() {
        // Given: an array iterator that has iterated over some elements
        arrayIterator.next();
        // When: reset is called
        arrayIterator.reset();
        // Then: the iterator should be reset to the start
        assertEquals(array[0], arrayIterator.next());
    }

    @Test
    void testConstructor_InvalidArray() {
        // Given: a non-array object
        Object nonArray = "hello";
        // When: the constructor is called with the non-array object
        assertThrows(IllegalArgumentException.class, () -> new ArrayIterator<>(nonArray));
    }

    @Test
    void testConstructor_NullArray() {
        // Given: a null array
        Object nullArray = null;
        // When: the constructor is called with the null array
        assertThrows(NullPointerException.class, () -> new ArrayIterator<>(nullArray));
    }

    @Test
    void testConstructor_InvalidStartIndex() {
        // Given: an array and an invalid start index
        int invalidStartIndex = -1;
        // When: the constructor is called with the invalid start index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new ArrayIterator<>(array, invalidStartIndex));
    }

    @Test
    void testConstructor_InvalidEndIndex() {
        // Given: an array and an invalid end index
        int invalidEndIndex = array.length + 1;
        // When: the constructor is called with the invalid end index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new ArrayIterator<>(array, 0, invalidEndIndex));
    }

    @Test
    void testConstructor_InvalidStartAndEndIndices() {
        // Given: an array and invalid start and end indices
        int invalidStartIndex = -1;
        int invalidEndIndex = array.length + 1;
        // When: the constructor is called with the invalid start and end indices
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new ArrayIterator<>(array, invalidStartIndex, invalidEndIndex));
    }

    @Test
    void testConstructor_EndIndexLessThanStartIndex() {
        // Given: an array and an end index that is less than the start index
        int startIndex = 1;
        int endIndex = 0;
        // When: the constructor is called with the end index that is less than the start index
        assertThrows(IllegalArgumentException.class, () -> new ArrayIterator<>(array, startIndex, endIndex));
    }
}