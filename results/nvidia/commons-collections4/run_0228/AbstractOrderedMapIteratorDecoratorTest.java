import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.iterators.AbstractOrderedMapIteratorDecorator;
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
public class AbstractOrderedMapIteratorDecoratorTest {

    @Mock
    private OrderedMapIterator<String, String> iterator;

    private AbstractOrderedMapIteratorDecorator<String, String> decorator;

    @BeforeEach
    public void setup() {
        decorator = new AbstractOrderedMapIteratorDecorator<>(iterator);
    }

    @Test
    public void testGetKey() {
        // Given
        String key = "key";
        when(iterator.getKey()).thenReturn(key);

        // When
        String result = decorator.getKey();

        // Then
        assertEquals(key, result);
        verify(iterator, times(1)).getKey();
    }

    @Test
    public void testGetValue() {
        // Given
        String value = "value";
        when(iterator.getValue()).thenReturn(value);

        // When
        String result = decorator.getValue();

        // Then
        assertEquals(value, result);
        verify(iterator, times(1)).getValue();
    }

    @Test
    public void testHasNext() {
        // Given
        boolean hasNext = true;
        when(iterator.hasNext()).thenReturn(hasNext);

        // When
        boolean result = decorator.hasNext();

        // Then
        assertEquals(hasNext, result);
        verify(iterator, times(1)).hasNext();
    }

    @Test
    public void testHasPrevious() {
        // Given
        boolean hasPrevious = true;
        when(iterator.hasPrevious()).thenReturn(hasPrevious);

        // When
        boolean result = decorator.hasPrevious();

        // Then
        assertEquals(hasPrevious, result);
        verify(iterator, times(1)).hasPrevious();
    }

    @Test
    public void testNext() {
        // Given
        String next = "next";
        when(iterator.next()).thenReturn(next);

        // When
        String result = decorator.next();

        // Then
        assertEquals(next, result);
        verify(iterator, times(1)).next();
    }

    @Test
    public void testNext_NoSuchElementException() {
        // Given
        when(iterator.hasNext()).thenReturn(false);

        // When / Then
        assertThrows(NoSuchElementException.class, () -> decorator.next());
        verify(iterator, times(1)).hasNext();
        verify(iterator, never()).next();
    }

    @Test
    public void testPrevious() {
        // Given
        String previous = "previous";
        when(iterator.previous()).thenReturn(previous);

        // When
        String result = decorator.previous();

        // Then
        assertEquals(previous, result);
        verify(iterator, times(1)).previous();
    }

    @Test
    public void testPrevious_NoSuchElementException() {
        // Given
        when(iterator.hasPrevious()).thenReturn(false);

        // When / Then
        assertThrows(NoSuchElementException.class, () -> decorator.previous());
        verify(iterator, times(1)).hasPrevious();
        verify(iterator, never()).previous();
    }

    @Test
    public void testRemove() {
        // When
        decorator.remove();

        // Then
        verify(iterator, times(1)).remove();
    }

    @Test
    public void testSetValue() {
        // Given
        String value = "value";
        String resultValue = "resultValue";
        when(iterator.setValue(any())).thenReturn(resultValue);

        // When
        String actualResult = decorator.setValue(value);

        // Then
        assertEquals(resultValue, actualResult);
        verify(iterator, times(1)).setValue(value);
    }
}