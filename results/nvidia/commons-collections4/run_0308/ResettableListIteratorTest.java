import org.apache.commons.collections4.ResettableListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResettableListIteratorTest {

    @Mock
    private ResettableListIterator<String> resettableListIterator;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        reset(resettableListIterator);
    }

    @Test
    public void testHasNext() {
        // Given: the iterator has next element
        when(resettableListIterator.hasNext()).thenReturn(true);

        // When: check if the iterator has next element
        boolean hasNext = resettableListIterator.hasNext();

        // Then: verify the result
        assertTrue(hasNext);
        verify(resettableListIterator, times(1)).hasNext();
    }

    @Test
    public void testHasNext_NoNextElement() {
        // Given: the iterator does not have next element
        when(resettableListIterator.hasNext()).thenReturn(false);

        // When: check if the iterator has next element
        boolean hasNext = resettableListIterator.hasNext();

        // Then: verify the result
        assertFalse(hasNext);
        verify(resettableListIterator, times(1)).hasNext();
    }

    @Test
    public void testNext() {
        // Given: the iterator has next element
        when(resettableListIterator.hasNext()).thenReturn(true);
        when(resettableListIterator.next()).thenReturn("Element");

        // When: get the next element
        String nextElement = resettableListIterator.next();

        // Then: verify the result
        assertEquals("Element", nextElement);
        verify(resettableListIterator, times(1)).hasNext();
        verify(resettableListIterator, times(1)).next();
    }

    @Test
    public void testNext_NoNextElement() {
        // Given: the iterator does not have next element
        when(resettableListIterator.hasNext()).thenReturn(false);

        // When / Then: expect an exception
        assertThrows(NoSuchElementException.class, () -> resettableListIterator.next());
        verify(resettableListIterator, times(1)).hasNext();
        verify(resettableListIterator, never()).next();
    }

    @Test
    public void testHasPrevious() {
        // Given: the iterator has previous element
        when(resettableListIterator.hasPrevious()).thenReturn(true);

        // When: check if the iterator has previous element
        boolean hasPrevious = resettableListIterator.hasPrevious();

        // Then: verify the result
        assertTrue(hasPrevious);
        verify(resettableListIterator, times(1)).hasPrevious();
    }

    @Test
    public void testHasPrevious_NoPreviousElement() {
        // Given: the iterator does not have previous element
        when(resettableListIterator.hasPrevious()).thenReturn(false);

        // When: check if the iterator has previous element
        boolean hasPrevious = resettableListIterator.hasPrevious();

        // Then: verify the result
        assertFalse(hasPrevious);
        verify(resettableListIterator, times(1)).hasPrevious();
    }

    @Test
    public void testPrevious() {
        // Given: the iterator has previous element
        when(resettableListIterator.hasPrevious()).thenReturn(true);
        when(resettableListIterator.previous()).thenReturn("Element");

        // When: get the previous element
        String previousElement = resettableListIterator.previous();

        // Then: verify the result
        assertEquals("Element", previousElement);
        verify(resettableListIterator, times(1)).hasPrevious();
        verify(resettableListIterator, times(1)).previous();
    }

    @Test
    public void testPrevious_NoPreviousElement() {
        // Given: the iterator does not have previous element
        when(resettableListIterator.hasPrevious()).thenReturn(false);

        // When / Then: expect an exception
        assertThrows(NoSuchElementException.class, () -> resettableListIterator.previous());
        verify(resettableListIterator, times(1)).hasPrevious();
        verify(resettableListIterator, never()).previous();
    }

    @Test
    public void testNextIndex() {
        // Given: the iterator has next index
        when(resettableListIterator.nextIndex()).thenReturn(1);

        // When: get the next index
        int nextIndex = resettableListIterator.nextIndex();

        // Then: verify the result
        assertEquals(1, nextIndex);
        verify(resettableListIterator, times(1)).nextIndex();
    }

    @Test
    public void testPreviousIndex() {
        // Given: the iterator has previous index
        when(resettableListIterator.previousIndex()).thenReturn(0);

        // When: get the previous index
        int previousIndex = resettableListIterator.previousIndex();

        // Then: verify the result
        assertEquals(0, previousIndex);
        verify(resettableListIterator, times(1)).previousIndex();
    }

    @Test
    public void testRemove() {
        // When: remove the current element
        resettableListIterator.remove();

        // Then: verify the interaction
        verify(resettableListIterator, times(1)).remove();
    }

    @Test
    public void testSet() {
        // Given: the element to set
        String element = "New Element";

        // When: set the current element
        resettableListIterator.set(element);

        // Then: verify the interaction
        verify(resettableListIterator, times(1)).set(element);
    }

    @Test
    public void testAdd() {
        // Given: the element to add
        String element = "New Element";

        // When: add the element
        resettableListIterator.add(element);

        // Then: verify the interaction
        verify(resettableListIterator, times(1)).add(element);
    }
}