import org.apache.commons.collections4.iterators.LoopingListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoopingListIteratorTest {

    @Mock
    private List<String> list;

    private LoopingListIterator<String> loopingListIterator;

    @BeforeEach
    void setup() {
        loopingListIterator = new LoopingListIterator<>(list);
    }

    @Test
    void testAdd() {
        // Given
        String obj = "Test Object";
        when(list.listIterator()).thenReturn(mock(ListIterator.class));
        ListIterator<String> listIterator = mock(ListIterator.class);
        when(listIterator.add(obj)).thenReturn(mock(ListIterator.class));
        when(list.listIterator()).thenReturn(listIterator);

        // When
        loopingListIterator.add(obj);

        // Then
        verify(listIterator, times(1)).add(obj);
    }

    @Test
    void testHasNext() {
        // Given
        when(list.isEmpty()).thenReturn(false);

        // When
        boolean result = loopingListIterator.hasNext();

        // Then
        assertTrue(result);
    }

    @Test
    void testHasNext_EmptyList() {
        // Given
        when(list.isEmpty()).thenReturn(true);

        // When
        boolean result = loopingListIterator.hasNext();

        // Then
        assertTrue(result);
    }

    @Test
    void testHasPrevious() {
        // Given
        when(list.isEmpty()).thenReturn(false);

        // When
        boolean result = loopingListIterator.hasPrevious();

        // Then
        assertTrue(result);
    }

    @Test
    void testHasPrevious_EmptyList() {
        // Given
        when(list.isEmpty()).thenReturn(true);

        // When
        boolean result = loopingListIterator.hasPrevious();

        // Then
        assertTrue(result);
    }

    @Test
    void testNext() {
        // Given
        String obj = "Test Object";
        when(list.listIterator()).thenReturn(mock(ListIterator.class));
        ListIterator<String> listIterator = mock(ListIterator.class);
        when(listIterator.hasNext()).thenReturn(true);
        when(listIterator.next()).thenReturn(obj);
        when(list.listIterator()).thenReturn(listIterator);

        // When
        String result = loopingListIterator.next();

        // Then
        assertEquals(obj, result);
    }

    @Test
    void testNext_NoElements() {
        // Given
        when(list.isEmpty()).thenReturn(true);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> loopingListIterator.next());
    }

    @Test
    void testNextIndex() {
        // Given
        when(list.listIterator()).thenReturn(mock(ListIterator.class));
        ListIterator<String> listIterator = mock(ListIterator.class);
        when(listIterator.hasNext()).thenReturn(true);
        when(listIterator.nextIndex()).thenReturn(1);
        when(list.listIterator()).thenReturn(listIterator);

        // When
        int result = loopingListIterator.nextIndex();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testNextIndex_NoElements() {
        // Given
        when(list.isEmpty()).thenReturn(true);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> loopingListIterator.nextIndex());
    }

    @Test
    void testPrevious() {
        // Given
        String obj = "Test Object";
        when(list.listIterator()).thenReturn(mock(ListIterator.class));
        ListIterator<String> listIterator = mock(ListIterator.class);
        when(listIterator.hasPrevious()).thenReturn(true);
        when(listIterator.previous()).thenReturn(obj);
        when(list.listIterator()).thenReturn(listIterator);

        // When
        String result = loopingListIterator.previous();

        // Then
        assertEquals(obj, result);
    }

    @Test
    void testPrevious_NoElements() {
        // Given
        when(list.isEmpty()).thenReturn(true);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> loopingListIterator.previous());
    }

    @Test
    void testPreviousIndex() {
        // Given
        when(list.listIterator()).thenReturn(mock(ListIterator.class));
        ListIterator<String> listIterator = mock(ListIterator.class);
        when(listIterator.hasPrevious()).thenReturn(true);
        when(listIterator.previousIndex()).thenReturn(1);
        when(list.listIterator()).thenReturn(listIterator);

        // When
        int result = loopingListIterator.previousIndex();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testPreviousIndex_NoElements() {
        // Given
        when(list.isEmpty()).thenReturn(true);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> loopingListIterator.previousIndex());
    }

    @Test
    void testRemove() {
        // Given
        when(list.listIterator()).thenReturn(mock(ListIterator.class));
        ListIterator<String> listIterator = mock(ListIterator.class);
        when(listIterator.remove()).thenReturn(mock(ListIterator.class));
        when(list.listIterator()).thenReturn(listIterator);

        // When
        loopingListIterator.remove();

        // Then
        verify(listIterator, times(1)).remove();
    }

    @Test
    void testReset() {
        // Given
        when(list.listIterator()).thenReturn(mock(ListIterator.class));
        ListIterator<String> listIterator = mock(ListIterator.class);
        when(list.listIterator()).thenReturn(listIterator);

        // When
        loopingListIterator.reset();

        // Then
        verify(listIterator, times(1)).hasNext();
    }

    @Test
    void testSet() {
        // Given
        String obj = "Test Object";
        when(list.listIterator()).thenReturn(mock(ListIterator.class));
        ListIterator<String> listIterator = mock(ListIterator.class);
        when(listIterator.set(obj)).thenReturn(mock(ListIterator.class));
        when(list.listIterator()).thenReturn(listIterator);

        // When
        loopingListIterator.set(obj);

        // Then
        verify(listIterator, times(1)).set(obj);
    }

    @Test
    void testSize() {
        // Given
        when(list.size()).thenReturn(10);

        // When
        int result = loopingListIterator.size();

        // Then
        assertEquals(10, result);
    }
}