import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableOrderedMapIteratorTest {

    @Mock
    private OrderedMapIterator<String, String> iterator;

    private UnmodifiableOrderedMapIterator<String, String> unmodifiableIterator;

    @BeforeEach
    public void setup() {
        unmodifiableIterator = new UnmodifiableOrderedMapIterator<>(iterator);
    }

    @Test
    public void testUnmodifiableOrderedMapIterator() {
        // Given
        when(iterator.getKey()).thenReturn("key");
        when(iterator.getValue()).thenReturn("value");

        // When
        String key = unmodifiableIterator.getKey();
        String value = unmodifiableIterator.getValue();

        // Then
        assertEquals("key", key);
        assertEquals("value", value);
        verify(iterator, times(1)).getKey();
        verify(iterator, times(1)).getValue();
    }

    @Test
    public void testHasNext() {
        // Given
        when(iterator.hasNext()).thenReturn(true);

        // When
        boolean hasNext = unmodifiableIterator.hasNext();

        // Then
        assertTrue(hasNext);
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testHasPrevious() {
        // Given
        when(iterator.hasPrevious()).thenReturn(true);

        // When
        boolean hasPrevious = unmodifiableIterator.hasPrevious();

        // Then
        assertTrue(hasPrevious);
        verify(iterator, times(1)).hasPrevious();
    }

    @Test
    public void testNext() {
        // Given
        when(iterator.next()).thenReturn("next");

        // When
        String next = unmodifiableIterator.next();

        // Then
        assertEquals("next", next);
        verify(iterator, times(1)).next();
    }

    @Test
    public void testNext_ThrowsNoSuchElementException() {
        // Given
        when(iterator.next()).thenThrow(NoSuchElementException.class);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> unmodifiableIterator.next());
        verify(iterator, times(1)).next();
    }

    @Test
    public void testPrevious() {
        // Given
        when(iterator.previous()).thenReturn("previous");

        // When
        String previous = unmodifiableIterator.previous();

        // Then
        assertEquals("previous", previous);
        verify(iterator, times(1)).previous();
    }

    @Test
    public void testPrevious_ThrowsNoSuchElementException() {
        // Given
        when(iterator.previous()).thenThrow(NoSuchElementException.class);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> unmodifiableIterator.previous());
        verify(iterator, times(1)).previous();
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableIterator.remove());
        verify(iterator, never()).remove();
    }

    @Test
    public void testSetValue() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableIterator.setValue("value"));
        verify(iterator, never()).setValue(any());
    }

    @Test
    public void testUnmodifiableOrderedMapIterator_StaticMethod() {
        // Given
        OrderedMapIterator<String, String> iterator = mock(OrderedMapIterator.class);

        // When
        UnmodifiableOrderedMapIterator<String, String> unmodifiableIterator = UnmodifiableOrderedMapIterator.unmodifiableOrderedMapIterator(iterator);

        // Then
        assertNotNull(unmodifiableIterator);
        assertSame(iterator, ((UnmodifiableOrderedMapIterator) unmodifiableIterator).iterator);
    }

    @Test
    public void testUnmodifiableOrderedMapIterator_StaticMethod_WithUnmodifiableIterator() {
        // Given
        UnmodifiableOrderedMapIterator<String, String> iterator = new UnmodifiableOrderedMapIterator<>(mock(OrderedMapIterator.class));

        // When
        UnmodifiableOrderedMapIterator<String, String> unmodifiableIterator = UnmodifiableOrderedMapIterator.unmodifiableOrderedMapIterator(iterator);

        // Then
        assertSame(iterator, unmodifiableIterator);
    }

    @Test
    public void testUnmodifiableOrderedMapIterator_StaticMethod_NullIterator() {
        // When and Then
        assertThrows(NullPointerException.class, () -> UnmodifiableOrderedMapIterator.unmodifiableOrderedMapIterator(null));
    }
}