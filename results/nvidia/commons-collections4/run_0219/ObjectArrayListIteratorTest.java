import org.apache.commons.collections4.iterators.ObjectArrayListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ObjectArrayListIteratorTest {

    private ObjectArrayListIterator<String> iterator;

    @BeforeEach
    void setup() {
        String[] array = {"apple", "banana", "cherry"};
        iterator = new ObjectArrayListIterator<>(array);
    }

    @Test
    void testAdd_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> iterator.add("date"));
    }

    @Test
    void testHasPrevious_ReturnsFalseWhenAtStart() {
        assertFalse(iterator.hasPrevious());
    }

    @Test
    void testHasPrevious_ReturnsTrueWhenNotAtStart() {
        iterator.next();
        assertTrue(iterator.hasPrevious());
    }

    @Test
    void testNext_ReturnsNextElement() {
        assertEquals("apple", iterator.next());
    }

    @Test
    void testNext_ThrowsNoSuchElementExceptionWhenAtEnd() {
        iterator.next();
        iterator.next();
        iterator.next();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void testNextIndex_ReturnsNextIndex() {
        assertEquals(0, iterator.nextIndex());
        iterator.next();
        assertEquals(1, iterator.nextIndex());
    }

    @Test
    void testPrevious_ReturnsPreviousElement() {
        iterator.next();
        assertEquals("apple", iterator.previous());
    }

    @Test
    void testPrevious_ThrowsNoSuchElementExceptionWhenAtStart() {
        assertThrows(NoSuchElementException.class, () -> iterator.previous());
    }

    @Test
    void testPreviousIndex_ReturnsPreviousIndex() {
        iterator.next();
        assertEquals(0, iterator.previousIndex());
    }

    @Test
    void testReset_ResetsIterator() {
        iterator.next();
        iterator.reset();
        assertEquals("apple", iterator.next());
    }

    @Test
    void testSet_SetsElement() {
        iterator.next();
        iterator.set("date");
        assertEquals("date", iterator.previous());
    }

    @Test
    void testSet_ThrowsIllegalStateExceptionWhenNotCalledAfterNextOrPrevious() {
        assertThrows(IllegalStateException.class, () -> iterator.set("date"));
    }
}