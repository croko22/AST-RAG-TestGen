import org.apache.commons.collections4.iterators.ObjectArrayIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ObjectArrayIteratorTest {

    private ObjectArrayIterator<String> iterator;

    @BeforeEach
    public void setup() {
        String[] array = {"apple", "banana", "cherry"};
        iterator = new ObjectArrayIterator<>(array);
    }

    @Test
    public void testGetEndIndex() {
        // Given: iterator is created with an array of 3 elements
        // When: getEndIndex is called
        int endIndex = iterator.getEndIndex();
        // Then: endIndex should be equal to the length of the array
        assertEquals(3, endIndex);
    }

    @Test
    public void testGetStartIndex() {
        // Given: iterator is created with an array of 3 elements
        // When: getStartIndex is called
        int startIndex = iterator.getStartIndex();
        // Then: startIndex should be equal to 0
        assertEquals(0, startIndex);
    }

    @Test
    public void testHasNext() {
        // Given: iterator is created with an array of 3 elements
        // When: hasNext is called
        boolean hasNext = iterator.hasNext();
        // Then: hasNext should be true
        assertTrue(hasNext);
    }

    @Test
    public void testNext() {
        // Given: iterator is created with an array of 3 elements
        // When: next is called
        String nextElement = iterator.next();
        // Then: nextElement should be the first element in the array
        assertEquals("apple", nextElement);
    }

    @Test
    public void testNext_MultipleCalls() {
        // Given: iterator is created with an array of 3 elements
        // When: next is called multiple times
        String nextElement1 = iterator.next();
        String nextElement2 = iterator.next();
        String nextElement3 = iterator.next();
        // Then: nextElement1, nextElement2, and nextElement3 should be the first, second, and third elements in the array
        assertEquals("apple", nextElement1);
        assertEquals("banana", nextElement2);
        assertEquals("cherry", nextElement3);
    }

    @Test
    public void testNext_NoMoreElements() {
        // Given: iterator is created with an array of 3 elements
        // When: next is called after all elements have been returned
        iterator.next();
        iterator.next();
        iterator.next();
        // Then: next should throw a NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testRemove() {
        // Given: iterator is created with an array of 3 elements
        // When: remove is called
        // Then: remove should throw an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    @Test
    public void testReset() {
        // Given: iterator is created with an array of 3 elements
        // When: next is called, and then reset is called
        iterator.next();
        iterator.reset();
        // Then: hasNext should be true, and next should return the first element in the array
        assertTrue(iterator.hasNext());
        assertEquals("apple", iterator.next());
    }

    @Test
    public void testConstructor_NullArray() {
        // Given: null array is passed to the constructor
        // When: constructor is called
        // Then: constructor should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> new ObjectArrayIterator<>(null));
    }

    @Test
    public void testConstructor_InvalidStartIndex() {
        // Given: invalid start index is passed to the constructor
        // When: constructor is called
        // Then: constructor should throw an ArrayIndexOutOfBoundsException
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new ObjectArrayIterator<>(new String[3], -1));
    }

    @Test
    public void testConstructor_InvalidEndIndex() {
        // Given: invalid end index is passed to the constructor
        // When: constructor is called
        // Then: constructor should throw an ArrayIndexOutOfBoundsException
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new ObjectArrayIterator<>(new String[3], 0, 4));
    }

    @Test
    public void testConstructor_InvalidStartAndEndIndices() {
        // Given: invalid start and end indices are passed to the constructor
        // When: constructor is called
        // Then: constructor should throw an ArrayIndexOutOfBoundsException
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new ObjectArrayIterator<>(new String[3], 4, 5));
    }

    @Test
    public void testConstructor_EndIndexBeforeStartIndex() {
        // Given: end index is before start index
        // When: constructor is called
        // Then: constructor should throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> new ObjectArrayIterator<>(new String[3], 2, 1));
    }
}