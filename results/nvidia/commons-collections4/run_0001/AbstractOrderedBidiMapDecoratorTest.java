import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractOrderedBidiMapDecoratorTest {

    @Mock
    private OrderedBidiMap<String, String> orderedBidiMap;

    private AbstractOrderedBidiMapDecorator<String, String> abstractOrderedBidiMapDecorator;

    @BeforeEach
    void setup() {
        abstractOrderedBidiMapDecorator = new AbstractOrderedBidiMapDecorator<>(orderedBidiMap) {
        };
    }

    @Test
    public void testFirstKey() {
        // Given
        String expectedKey = "key";
        when(orderedBidiMap.firstKey()).thenReturn(expectedKey);

        // When
        String actualKey = abstractOrderedBidiMapDecorator.firstKey();

        // Then
        assertEquals(expectedKey, actualKey);
        verify(orderedBidiMap, times(1)).firstKey();
    }

    @Test
    public void testInverseBidiMap() {
        // Given
        OrderedBidiMap<String, String> expectedInverseBidiMap = mock(OrderedBidiMap.class);
        when(orderedBidiMap.inverseBidiMap()).thenReturn(expectedInverseBidiMap);

        // When
        OrderedBidiMap<String, String> actualInverseBidiMap = abstractOrderedBidiMapDecorator.inverseBidiMap();

        // Then
        assertEquals(expectedInverseBidiMap, actualInverseBidiMap);
        verify(orderedBidiMap, times(1)).inverseBidiMap();
    }

    @Test
    public void testLastKey() {
        // Given
        String expectedKey = "key";
        when(orderedBidiMap.lastKey()).thenReturn(expectedKey);

        // When
        String actualKey = abstractOrderedBidiMapDecorator.lastKey();

        // Then
        assertEquals(expectedKey, actualKey);
        verify(orderedBidiMap, times(1)).lastKey();
    }

    @Test
    public void testMapIterator() {
        // Given
        OrderedMapIterator<String, String> expectedMapIterator = mock(OrderedMapIterator.class);
        when(orderedBidiMap.mapIterator()).thenReturn(expectedMapIterator);

        // When
        OrderedMapIterator<String, String> actualMapIterator = abstractOrderedBidiMapDecorator.mapIterator();

        // Then
        assertEquals(expectedMapIterator, actualMapIterator);
        verify(orderedBidiMap, times(1)).mapIterator();
    }

    @Test
    public void testNextKey() {
        // Given
        String key = "key";
        String expectedNextKey = "nextKey";
        when(orderedBidiMap.nextKey(key)).thenReturn(expectedNextKey);

        // When
        String actualNextKey = abstractOrderedBidiMapDecorator.nextKey(key);

        // Then
        assertEquals(expectedNextKey, actualNextKey);
        verify(orderedBidiMap, times(1)).nextKey(key);
    }

    @Test
    public void testPreviousKey() {
        // Given
        String key = "key";
        String expectedPreviousKey = "previousKey";
        when(orderedBidiMap.previousKey(key)).thenReturn(expectedPreviousKey);

        // When
        String actualPreviousKey = abstractOrderedBidiMapDecorator.previousKey(key);

        // Then
        assertEquals(expectedPreviousKey, actualPreviousKey);
        verify(orderedBidiMap, times(1)).previousKey(key);
    }

    @Test
    public void testNullOrderedBidiMap() {
        // Given
        assertThrows(NullPointerException.class, () -> new AbstractOrderedBidiMapDecorator<>(null) {
        });
    }
}