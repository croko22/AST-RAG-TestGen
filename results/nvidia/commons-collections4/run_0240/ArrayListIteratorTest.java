import org.apache.commons.collections4.iterators.ArrayListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArrayListIteratorTest {

    private ArrayListIterator<String> iterator;

    @BeforeEach
    public void setup() {
        String[] array = {"apple", "banana", "cherry"};
        iterator = new ArrayListIterator<>(array);
    }

    @Test
    public void testAdd_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> iterator.add("orange"));
    }

    @Test
    public void testHasPrevious_ReturnsFalse() {
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testHasPrevious_ReturnsTrue() {
        iterator.next();
        assertTrue(iterator.hasPrevious());
    }

    @Test
    public void testNext_ThrowsNoSuchElementException() {
        String[] array = {};
        ArrayListIterator<String> iterator = new ArrayListIterator<>(array);
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testNext_ReturnsNextElement() {
        assertEquals("apple", iterator.next());
    }

    @Test
    public void testNextIndex_ReturnsNextIndex() {
        assertEquals(0, iterator.nextIndex());
    }

    @Test
    public void testPrevious_ThrowsNoSuchElementException() {
        String[] array = {};
        ArrayListIterator<String> iterator = new ArrayListIterator<>(array);
        assertThrows(NoSuchElementException.class, () -> iterator.previous());
    }

    @Test
    public void testPrevious_ReturnsPreviousElement() {
        iterator.next();
        assertEquals("apple", iterator.previous());
    }

    @Test
    public void testPreviousIndex_ReturnsPreviousIndex() {
        iterator.next();
        assertEquals(-1, iterator.previousIndex());
    }

    @Test
    public void testReset_ResetsIterator() {
        iterator.next();
        iterator.reset();
        assertEquals("apple", iterator.next());
    }

    @Test
    public void testSet_ThrowsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> iterator.set("orange"));
    }

    @Test
    public void testSet_SetsElement() {
        iterator.next();
        iterator.set("orange");
        assertEquals("orange", iterator.previous());
    }
}