import org.apache.commons.collections4.iterators.AbstractEmptyIterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AbstractEmptyIteratorTest {

    @Test
    public void testAdd_ThrowsUnsupportedOperationException() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When / Then: add() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> iterator.add("ignored"));
    }

    @Test
    public void testHasNext_AlwaysReturnsFalse() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When: hasNext() is called
        boolean result = iterator.hasNext();

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testHasPrevious_AlwaysReturnsFalse() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When: hasPrevious() is called
        boolean result = iterator.hasPrevious();

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testNext_ThrowsNoSuchElementException() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When / Then: next() throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testNextIndex_AlwaysReturns0() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When: nextIndex() is called
        int result = iterator.nextIndex();

        // Then: result is 0
        assertEquals(0, result);
    }

    @Test
    public void testPrevious_ThrowsNoSuchElementException() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When / Then: previous() throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> iterator.previous());
    }

    @Test
    public void testPreviousIndex_AlwaysReturnsMinus1() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When: previousIndex() is called
        int result = iterator.previousIndex();

        // Then: result is -1
        assertEquals(-1, result);
    }

    @Test
    public void testRemove_ThrowsIllegalStateException() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When / Then: remove() throws IllegalStateException
        assertThrows(IllegalStateException.class, () -> iterator.remove());
    }

    @Test
    public void testReset_DoesNothing() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When: reset() is called
        iterator.reset();

        // Then: no exception is thrown
        // This test is more about ensuring that reset() does not throw any exceptions
        // Since reset() does nothing, we can't really test its behavior
    }

    @Test
    public void testSet_ThrowsIllegalStateException() {
        // Given: an instance of AbstractEmptyIterator
        AbstractEmptyIterator<String> iterator = new AbstractEmptyIterator<String>() {};

        // When / Then: set() throws IllegalStateException
        assertThrows(IllegalStateException.class, () -> iterator.set("ignored"));
    }
}