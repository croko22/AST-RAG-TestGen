import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.map.AbstractOrderedMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractOrderedMapDecoratorTest {

    @Mock
    private OrderedMap<String, String> orderedMap;

    private AbstractOrderedMapDecorator<String, String> decorator;

    @BeforeEach
    void setup() {
        decorator = new AbstractOrderedMapDecorator<>(orderedMap) {
        };
    }

    @Test
    void testFirstKey() {
        // Given
        String firstKey = "firstKey";
        when(orderedMap.firstKey()).thenReturn(firstKey);

        // When
        String result = decorator.firstKey();

        // Then
        assertEquals(firstKey, result);
        verify(orderedMap, times(1)).firstKey();
    }

    @Test
    void testLastKey() {
        // Given
        String lastKey = "lastKey";
        when(orderedMap.lastKey()).thenReturn(lastKey);

        // When
        String result = decorator.lastKey();

        // Then
        assertEquals(lastKey, result);
        verify(orderedMap, times(1)).lastKey();
    }

    @Test
    void testMapIterator() {
        // Given
        OrderedMapIterator<String, String> iterator = mock(OrderedMapIterator.class);
        when(orderedMap.mapIterator()).thenReturn(iterator);

        // When
        OrderedMapIterator<String, String> result = decorator.mapIterator();

        // Then
        assertEquals(iterator, result);
        verify(orderedMap, times(1)).mapIterator();
    }

    @Test
    void testNextKey() {
        // Given
        String key = "key";
        String nextKey = "nextKey";
        when(orderedMap.nextKey(key)).thenReturn(nextKey);

        // When
        String result = decorator.nextKey(key);

        // Then
        assertEquals(nextKey, result);
        verify(orderedMap, times(1)).nextKey(key);
    }

    @Test
    void testPreviousKey() {
        // Given
        String key = "key";
        String previousKey = "previousKey";
        when(orderedMap.previousKey(key)).thenReturn(previousKey);

        // When
        String result = decorator.previousKey(key);

        // Then
        assertEquals(previousKey, result);
        verify(orderedMap, times(1)).previousKey(key);
    }

    @Test
    void testFirstKey_Null() {
        // Given
        when(orderedMap.firstKey()).thenReturn(null);

        // When
        String result = decorator.firstKey();

        // Then
        assertNull(result);
        verify(orderedMap, times(1)).firstKey();
    }

    @Test
    void testLastKey_Null() {
        // Given
        when(orderedMap.lastKey()).thenReturn(null);

        // When
        String result = decorator.lastKey();

        // Then
        assertNull(result);
        verify(orderedMap, times(1)).lastKey();
    }

    @Test
    void testMapIterator_Null() {
        // Given
        when(orderedMap.mapIterator()).thenReturn(null);

        // When
        OrderedMapIterator<String, String> result = decorator.mapIterator();

        // Then
        assertNull(result);
        verify(orderedMap, times(1)).mapIterator();
    }

    @Test
    void testNextKey_Null() {
        // Given
        String key = "key";
        when(orderedMap.nextKey(key)).thenReturn(null);

        // When
        String result = decorator.nextKey(key);

        // Then
        assertNull(result);
        verify(orderedMap, times(1)).nextKey(key);
    }

    @Test
    void testPreviousKey_Null() {
        // Given
        String key = "key";
        when(orderedMap.previousKey(key)).thenReturn(null);

        // When
        String result = decorator.previousKey(key);

        // Then
        assertNull(result);
        verify(orderedMap, times(1)).previousKey(key);
    }
}