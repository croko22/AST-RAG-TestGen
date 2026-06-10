import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.iterators.AbstractMapIteratorDecorator;
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
public class AbstractMapIteratorDecoratorTest {

    @Mock
    private MapIterator<String, String> mapIterator;

    private AbstractMapIteratorDecorator<String, String> decorator;

    @BeforeEach
    void setup() {
        decorator = new AbstractMapIteratorDecorator<>(mapIterator);
    }

    @Test
    void testConstructor() {
        assertThrows(NullPointerException.class, () -> new AbstractMapIteratorDecorator<>(null));
    }

    @Test
    void testGetKey() {
        // Given
        String key = "key";
        when(mapIterator.getKey()).thenReturn(key);

        // When
        String result = decorator.getKey();

        // Then
        assertEquals(key, result);
        verify(mapIterator, times(1)).getKey();
    }

    @Test
    void testGetValue() {
        // Given
        String value = "value";
        when(mapIterator.getValue()).thenReturn(value);

        // When
        String result = decorator.getValue();

        // Then
        assertEquals(value, result);
        verify(mapIterator, times(1)).getValue();
    }

    @Test
    void testHasNext() {
        // Given
        boolean hasNext = true;
        when(mapIterator.hasNext()).thenReturn(hasNext);

        // When
        boolean result = decorator.hasNext();

        // Then
        assertEquals(hasNext, result);
        verify(mapIterator, times(1)).hasNext();
    }

    @Test
    void testNext() {
        // Given
        String key = "key";
        when(mapIterator.next()).thenReturn(key);

        // When
        String result = decorator.next();

        // Then
        assertEquals(key, result);
        verify(mapIterator, times(1)).next();
    }

    @Test
    void testNext_NoSuchElementException() {
        // Given
        when(mapIterator.hasNext()).thenReturn(false);

        // When / Then
        assertThrows(NoSuchElementException.class, () -> decorator.next());
        verify(mapIterator, times(1)).hasNext();
    }

    @Test
    void testRemove() {
        // When
        decorator.remove();

        // Then
        verify(mapIterator, times(1)).remove();
    }

    @Test
    void testSetValue() {
        // Given
        String value = "value";
        String resultValue = "resultValue";
        when(mapIterator.setValue(any())).thenReturn(resultValue);

        // When
        String actualResult = decorator.setValue(value);

        // Then
        assertEquals(resultValue, actualResult);
        verify(mapIterator, times(1)).setValue(value);
    }
}