import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableListIteratorTest {

    @Mock
    private ListIterator<String> listIterator;

    private UnmodifiableListIterator<String> unmodifiableListIterator;

    @BeforeEach
    void setup() {
        unmodifiableListIterator = new UnmodifiableListIterator<>(listIterator);
    }

    @Test
    public void testUnmodifiableListIterator() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("Element1");
        list.add("Element2");
        ListIterator<String> iterator = list.listIterator();

        // When
        UnmodifiableListIterator<String> result = UnmodifiableListIterator.unmodifiableListIterator(iterator);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testUmmodifiableListIterator() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("Element1");
        list.add("Element2");
        ListIterator<String> iterator = list.listIterator();

        // When
        ListIterator<String> result = UnmodifiableListIterator.umodifiableListIterator(iterator);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testAdd() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableListIterator.add("Element"));
    }

    @Test
    public void testHasNext() {
        // Given
        when(listIterator.hasNext()).thenReturn(true);

        // When
        boolean result = unmodifiableListIterator.hasNext();

        // Then
        assertTrue(result);
        verify(listIterator, times(1)).hasNext();
    }

    @Test
    public void testHasPrevious() {
        // Given
        when(listIterator.hasPrevious()).thenReturn(true);

        // When
        boolean result = unmodifiableListIterator.hasPrevious();

        // Then
        assertTrue(result);
        verify(listIterator, times(1)).hasPrevious();
    }

    @Test
    public void testNext() {
        // Given
        when(listIterator.next()).thenReturn("Element");

        // When
        String result = unmodifiableListIterator.next();

        // Then
        assertEquals("Element", result);
        verify(listIterator, times(1)).next();
    }

    @Test
    public void testNextIndex() {
        // Given
        when(listIterator.nextIndex()).thenReturn(1);

        // When
        int result = unmodifiableListIterator.nextIndex();

        // Then
        assertEquals(1, result);
        verify(listIterator, times(1)).nextIndex();
    }

    @Test
    public void testPrevious() {
        // Given
        when(listIterator.previous()).thenReturn("Element");

        // When
        String result = unmodifiableListIterator.previous();

        // Then
        assertEquals("Element", result);
        verify(listIterator, times(1)).previous();
    }

    @Test
    public void testPreviousIndex() {
        // Given
        when(listIterator.previousIndex()).thenReturn(1);

        // When
        int result = unmodifiableListIterator.previousIndex();

        // Then
        assertEquals(1, result);
        verify(listIterator, times(1)).previousIndex();
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableListIterator.remove());
    }

    @Test
    public void testSet() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableListIterator.set("Element"));
    }
}