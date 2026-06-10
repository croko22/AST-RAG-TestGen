import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableMapIteratorTest {

    @Mock
    private MapIterator<String, String> iterator;

    private UnmodifiableMapIterator<String, String> unmodifiableMapIterator;

    @BeforeEach
    public void setup() {
        unmodifiableMapIterator = new UnmodifiableMapIterator<>(iterator);
    }

    @Test
    public void testUnmodifiableMapIterator() {
        // Given
        when(iterator.getKey()).thenReturn("key");
        when(iterator.getValue()).thenReturn("value");
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn("next");

        // When
        String key = unmodifiableMapIterator.getKey();
        String value = unmodifiableMapIterator.getValue();
        boolean hasNext = unmodifiableMapIterator.hasNext();
        String next = unmodifiableMapIterator.next();

        // Then
        assertEquals("key", key);
        assertEquals("value", value);
        assertTrue(hasNext);
        assertEquals("next", next);
        verify(iterator, times(1)).getKey();
        verify(iterator, times(1)).getValue();
        verify(iterator, times(1)).hasNext();
        verify(iterator, times(1)).next();
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMapIterator.remove());
        verify(iterator, never()).remove();
    }

    @Test
    public void testSetValue() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMapIterator.setValue("value"));
        verify(iterator, never()).setValue(any());
    }

    @Test
    public void testNext_NoSuchElementException() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> unmodifiableMapIterator.next());
        verify(iterator, times(1)).hasNext();
        verify(iterator, never()).next();
    }

    @Test
    public void testUnmodifiableMapIterator_StaticMethod() {
        // Given
        MapIterator<String, String> mapIterator = mock(MapIterator.class);

        // When
        UnmodifiableMapIterator<String, String> result = UnmodifiableMapIterator.unmodifiableMapIterator(mapIterator);

        // Then
        assertNotNull(result);
        assertSame(mapIterator, result.iterator);
    }

    @Test
    public void testUnmodifiableMapIterator_StaticMethod_Unmodifiable() {
        // Given
        UnmodifiableMapIterator<String, String> unmodifiableMapIterator = mock(UnmodifiableMapIterator.class);

        // When
        MapIterator<String, String> result = UnmodifiableMapIterator.unmodifiableMapIterator(unmodifiableMapIterator);

        // Then
        assertSame(unmodifiableMapIterator, result);
    }

    @Test
    public void testUnmodifiableMapIterator_StaticMethod_Null() {
        // When and Then
        assertThrows(NullPointerException.class, () -> UnmodifiableMapIterator.unmodifiableMapIterator(null));
    }
}