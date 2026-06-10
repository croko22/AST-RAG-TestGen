import org.apache.commons.collections4.iterators.SingletonIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SingletonIteratorTest {

    @Mock
    private Object object;

    private SingletonIterator<Object> iterator;
    private SingletonIterator<Object> iteratorRemoveNotAllowed;

    @BeforeEach
    void setup() {
        iterator = new SingletonIterator<>(object);
        iteratorRemoveNotAllowed = new SingletonIterator<>(object, false);
    }

    @Test
    void testHasNext_ReturnsTrue_BeforeFirst() {
        // Given: iterator is before the first element
        // When: hasNext is called
        boolean result = iterator.hasNext();
        // Then: returns true
        assertTrue(result);
    }

    @Test
    void testHasNext_ReturnsFalse_AfterNext() {
        // Given: iterator is after the first element
        iterator.next();
        // When: hasNext is called
        boolean result = iterator.hasNext();
        // Then: returns false
        assertFalse(result);
    }

    @Test
    void testHasNext_ReturnsFalse_AfterRemove() {
        // Given: iterator is after the first element and remove is called
        iterator.next();
        iterator.remove();
        // When: hasNext is called
        boolean result = iterator.hasNext();
        // Then: returns false
        assertFalse(result);
    }

    @Test
    void testNext_ReturnsObject_BeforeFirst() {
        // Given: iterator is before the first element
        // When: next is called
        Object result = iterator.next();
        // Then: returns the object
        assertEquals(object, result);
    }

    @Test
    void testNext_ThrowsNoSuchElementException_AfterNext() {
        // Given: iterator is after the first element
        iterator.next();
        // When: next is called
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void testNext_ThrowsNoSuchElementException_AfterRemove() {
        // Given: iterator is after the first element and remove is called
        iterator.next();
        iterator.remove();
        // When: next is called
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void testRemove_Allowed() {
        // Given: iterator is after the first element and remove is allowed
        iterator.next();
        // When: remove is called
        iterator.remove();
        // Then: no exception is thrown
        assertDoesNotThrow(() -> iterator.remove());
    }

    @Test
    void testRemove_NotAllowed() {
        // Given: iterator is after the first element and remove is not allowed
        iteratorRemoveNotAllowed.next();
        // When: remove is called
        assertThrows(UnsupportedOperationException.class, () -> iteratorRemoveNotAllowed.remove());
    }

    @Test
    void testRemove_ThrowsIllegalStateException_BeforeNext() {
        // Given: iterator is before the first element
        // When: remove is called
        assertThrows(IllegalStateException.class, () -> iterator.remove());
    }

    @Test
    void testRemove_ThrowsIllegalStateException_AfterRemove() {
        // Given: iterator is after the first element and remove is called
        iterator.next();
        iterator.remove();
        // When: remove is called again
        assertThrows(IllegalStateException.class, () -> iterator.remove());
    }

    @Test
    void testReset() {
        // Given: iterator is after the first element
        iterator.next();
        // When: reset is called
        iterator.reset();
        // Then: hasNext returns true
        assertTrue(iterator.hasNext());
    }
}