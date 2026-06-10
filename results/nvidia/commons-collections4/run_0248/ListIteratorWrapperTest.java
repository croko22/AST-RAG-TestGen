import org.apache.commons.collections4.iterators.ListIteratorWrapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UnsupportedOperationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListIteratorWrapperTest {

    @Mock
    private Iterator<String> iterator;

    private ListIteratorWrapper<String> listIteratorWrapper;

    @BeforeEach
    void setup() {
        listIteratorWrapper = new ListIteratorWrapper<>(iterator);
    }

    @Test
    void testAdd_ThrowsUnsupportedOperationException() {
        // Given
        String obj = "Test Object";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> listIteratorWrapper.add(obj));
    }

    @Test
    void testAdd_ThrowsUnsupportedOperationException_WhenIteratorIsListIterator() {
        // Given
        Iterator<String> listIterator = mock(ListIterator.class);
        ListIteratorWrapper<String> wrapper = new ListIteratorWrapper<>(listIterator);
        String obj = "Test Object";

        // When
        wrapper.add(obj);

        // Then
        verify(listIterator).add(obj);
    }

    @Test
    void testHasNext_ReturnsTrue_WhenIteratorHasNext() {
        // Given
        when(iterator.hasNext()).thenReturn(true);

        // When
        boolean result = listIteratorWrapper.hasNext();

        // Then
        assertTrue(result);
    }

    @Test
    void testHasNext_ReturnsFalse_WhenIteratorDoesNotHaveNext() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When
        boolean result = listIteratorWrapper.hasNext();

        // Then
        assertFalse(result);
    }

    @Test
    void testHasPrevious_ReturnsTrue_WhenIteratorIsListIteratorAndHasPrevious() {
        // Given
        Iterator<String> listIterator = mock(ListIterator.class);
        ListIteratorWrapper<String> wrapper = new ListIteratorWrapper<>(listIterator);
        when(listIterator.hasPrevious()).thenReturn(true);

        // When
        boolean result = wrapper.hasPrevious();

        // Then
        assertTrue(result);
    }

    @Test
    void testHasPrevious_ReturnsTrue_WhenIndexIsGreaterThanZero() {
        // Given
        listIteratorWrapper.next();

        // When
        boolean result = listIteratorWrapper.hasPrevious();

        // Then
        assertTrue(result);
    }

    @Test
    void testHasPrevious_ReturnsFalse_WhenIndexIsZero() {
        // When
        boolean result = listIteratorWrapper.hasPrevious();

        // Then
        assertFalse(result);
    }

    @Test
    void testNext_ThrowsNoSuchElementException_WhenIteratorDoesNotHaveNext() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> listIteratorWrapper.next());
    }

    @Test
    void testNext_ReturnsNextElement_WhenIteratorHasNext() {
        // Given
        String nextElement = "Next Element";
        when(iterator.next()).thenReturn(nextElement);

        // When
        String result = listIteratorWrapper.next();

        // Then
        assertEquals(nextElement, result);
    }

    @Test
    void testNextIndex_ReturnsNextIndex_WhenIteratorIsListIterator() {
        // Given
        Iterator<String> listIterator = mock(ListIterator.class);
        ListIteratorWrapper<String> wrapper = new ListIteratorWrapper<>(listIterator);
        when(listIterator.nextIndex()).thenReturn(1);

        // When
        int result = wrapper.nextIndex();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testNextIndex_ReturnsCurrentIndex_WhenIteratorIsNotListIterator() {
        // When
        int result = listIteratorWrapper.nextIndex();

        // Then
        assertEquals(0, result);
    }

    @Test
    void testPrevious_ThrowsNoSuchElementException_WhenIndexIsZero() {
        // When and Then
        assertThrows(NoSuchElementException.class, () -> listIteratorWrapper.previous());
    }

    @Test
    void testPrevious_ReturnsPreviousElement_WhenIndexIsGreaterThanZero() {
        // Given
        String nextElement = "Next Element";
        when(iterator.next()).thenReturn(nextElement);
        listIteratorWrapper.next();

        // When
        String result = listIteratorWrapper.previous();

        // Then
        assertEquals(nextElement, result);
    }

    @Test
    void testPreviousIndex_ReturnsPreviousIndex_WhenIteratorIsListIterator() {
        // Given
        Iterator<String> listIterator = mock(ListIterator.class);
        ListIteratorWrapper<String> wrapper = new ListIteratorWrapper<>(listIterator);
        when(listIterator.previousIndex()).thenReturn(1);

        // When
        int result = wrapper.previousIndex();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testPreviousIndex_ReturnsCurrentIndexMinusOne_WhenIteratorIsNotListIterator() {
        // When
        int result = listIteratorWrapper.previousIndex();

        // Then
        assertEquals(-1, result);
    }

    @Test
    void testRemove_ThrowsIllegalStateException_WhenRemoveStateIsFalse() {
        // When and Then
        assertThrows(IllegalStateException.class, () -> listIteratorWrapper.remove());
    }

    @Test
    void testRemove_RemovesLastReturnedElement_WhenRemoveStateIsTrue() {
        // Given
        String nextElement = "Next Element";
        when(iterator.next()).thenReturn(nextElement);
        listIteratorWrapper.next();

        // When
        listIteratorWrapper.remove();

        // Then
        verify(iterator).remove();
    }

    @Test
    void testReset_ResetsIteratorToInitialPosition() {
        // Given
        listIteratorWrapper.next();

        // When
        listIteratorWrapper.reset();

        // Then
        assertEquals(0, listIteratorWrapper.nextIndex());
    }

    @Test
    void testSet_ThrowsUnsupportedOperationException() {
        // Given
        String obj = "Test Object";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> listIteratorWrapper.set(obj));
    }

    @Test
    void testSet_SetsLastReturnedElement_WhenIteratorIsListIterator() {
        // Given
        Iterator<String> listIterator = mock(ListIterator.class);
        ListIteratorWrapper<String> wrapper = new ListIteratorWrapper<>(listIterator);
        String obj = "Test Object";

        // When
        wrapper.set(obj);

        // Then
        verify(listIterator).set(obj);
    }
}