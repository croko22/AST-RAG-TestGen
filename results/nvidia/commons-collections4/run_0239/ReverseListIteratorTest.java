import org.apache.commons.collections4.iterators.ReverseListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReverseListIteratorTest {

    @Mock
    private ListIterator<String> iterator;

    @Mock
    private List<String> list;

    private ReverseListIterator<String> reverseListIterator;

    @BeforeEach
    void setup() {
        when(list.listIterator(anyInt())).thenReturn(iterator);
        reverseListIterator = new ReverseListIterator<>(list);
    }

    @Test
    public void testAdd() {
        // Given
        String obj = "Test";
        when(iterator.hasPrevious()).thenReturn(true);
        when(iterator.previous()).thenReturn("Previous");
        reverseListIterator.next(); // To make validForUpdate true

        // When
        reverseListIterator.add(obj);

        // Then
        verify(iterator).add(obj);
        verify(iterator).previous();
    }

    @Test
    public void testAdd_InvalidState() {
        // Given
        String obj = "Test";

        // When / Then
        assertThrows(IllegalStateException.class, () -> reverseListIterator.add(obj));
    }

    @Test
    public void testHasNext() {
        // Given
        when(iterator.hasPrevious()).thenReturn(true);

        // When
        boolean result = reverseListIterator.hasNext();

        // Then
        assertTrue(result);
        verify(iterator).hasPrevious();
    }

    @Test
    public void testHasNext_NoPrevious() {
        // Given
        when(iterator.hasPrevious()).thenReturn(false);

        // When
        boolean result = reverseListIterator.hasNext();

        // Then
        assertFalse(result);
        verify(iterator).hasPrevious();
    }

    @Test
    public void testHasPrevious() {
        // Given
        when(iterator.hasNext()).thenReturn(true);

        // When
        boolean result = reverseListIterator.hasPrevious();

        // Then
        assertTrue(result);
        verify(iterator).hasNext();
    }

    @Test
    public void testHasPrevious_NoNext() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When
        boolean result = reverseListIterator.hasPrevious();

        // Then
        assertFalse(result);
        verify(iterator).hasNext();
    }

    @Test
    public void testNext() {
        // Given
        String obj = "Test";
        when(iterator.hasPrevious()).thenReturn(true);
        when(iterator.previous()).thenReturn(obj);

        // When
        String result = reverseListIterator.next();

        // Then
        assertEquals(obj, result);
        verify(iterator).hasPrevious();
        verify(iterator).previous();
    }

    @Test
    public void testNextIndex() {
        // Given
        int index = 10;
        when(iterator.previousIndex()).thenReturn(index);

        // When
        int result = reverseListIterator.nextIndex();

        // Then
        assertEquals(index, result);
        verify(iterator).previousIndex();
    }

    @Test
    public void testPrevious() {
        // Given
        String obj = "Test";
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(obj);

        // When
        String result = reverseListIterator.previous();

        // Then
        assertEquals(obj, result);
        verify(iterator).hasNext();
        verify(iterator).next();
    }

    @Test
    public void testPreviousIndex() {
        // Given
        int index = 10;
        when(iterator.nextIndex()).thenReturn(index);

        // When
        int result = reverseListIterator.previousIndex();

        // Then
        assertEquals(index, result);
        verify(iterator).nextIndex();
    }

    @Test
    public void testRemove() {
        // Given
        reverseListIterator.next(); // To make validForUpdate true

        // When
        reverseListIterator.remove();

        // Then
        verify(iterator).remove();
    }

    @Test
    public void testRemove_InvalidState() {
        // When / Then
        assertThrows(IllegalStateException.class, () -> reverseListIterator.remove());
    }

    @Test
    public void testReset() {
        // When
        reverseListIterator.reset();

        // Then
        verify(list).listIterator(list.size());
    }

    @Test
    public void testSet() {
        // Given
        String obj = "Test";
        reverseListIterator.next(); // To make validForUpdate true

        // When
        reverseListIterator.set(obj);

        // Then
        verify(iterator).set(obj);
    }

    @Test
    public void testSet_InvalidState() {
        // Given
        String obj = "Test";

        // When / Then
        assertThrows(IllegalStateException.class, () -> reverseListIterator.set(obj));
    }
}