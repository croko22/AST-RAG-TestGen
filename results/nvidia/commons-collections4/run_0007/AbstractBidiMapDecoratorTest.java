import org.apache.commons.collections4.bidimap.AbstractBidiMapDecorator;
import org.apache.commons.collections4.bidimap.BidiMap;
import org.apache.commons.collections4.map.AbstractMapDecorator;
import org.apache.commons.collections4.MapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractBidiMapDecoratorTest {

    @Mock
    private BidiMap<String, String> bidiMap;

    private AbstractBidiMapDecorator<String, String> abstractBidiMapDecorator;

    @BeforeEach
    public void setup() {
        abstractBidiMapDecorator = new AbstractBidiMapDecorator<String, String>(bidiMap) {
        };
    }

    @Test
    public void testGetKey() {
        // Given
        String value = "value";
        String key = "key";
        when(bidiMap.getKey(value)).thenReturn(key);

        // When
        String result = abstractBidiMapDecorator.getKey(value);

        // Then
        assertEquals(key, result);
        verify(bidiMap, times(1)).getKey(value);
    }

    @Test
    public void testInverseBidiMap() {
        // Given
        BidiMap<String, String> inverseBidiMap = mock(BidiMap.class);
        when(bidiMap.inverseBidiMap()).thenReturn(inverseBidiMap);

        // When
        BidiMap<String, String> result = abstractBidiMapDecorator.inverseBidiMap();

        // Then
        assertEquals(inverseBidiMap, result);
        verify(bidiMap, times(1)).inverseBidiMap();
    }

    @Test
    public void testMapIterator() {
        // Given
        MapIterator<String, String> mapIterator = mock(MapIterator.class);
        when(bidiMap.mapIterator()).thenReturn(mapIterator);

        // When
        MapIterator<String, String> result = abstractBidiMapDecorator.mapIterator();

        // Then
        assertEquals(mapIterator, result);
        verify(bidiMap, times(1)).mapIterator();
    }

    @Test
    public void testRemoveValue() {
        // Given
        String value = "value";
        String key = "key";
        when(bidiMap.removeValue(value)).thenReturn(key);

        // When
        String result = abstractBidiMapDecorator.removeValue(value);

        // Then
        assertEquals(key, result);
        verify(bidiMap, times(1)).removeValue(value);
    }

    @Test
    public void testValues() {
        // Given
        Set<String> values = new HashSet<>();
        when(bidiMap.values()).thenReturn(values);

        // When
        Set<String> result = abstractBidiMapDecorator.values();

        // Then
        assertEquals(values, result);
        verify(bidiMap, times(1)).values();
    }

    @Test
    public void testGetKey_NullValue() {
        // Given
        String value = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> abstractBidiMapDecorator.getKey(value));
    }

    @Test
    public void testInverseBidiMap_Null() {
        // Given
        when(bidiMap.inverseBidiMap()).thenReturn(null);

        // When and Then
        assertThrows(NullPointerException.class, () -> abstractBidiMapDecorator.inverseBidiMap());
    }

    @Test
    public void testMapIterator_Null() {
        // Given
        when(bidiMap.mapIterator()).thenReturn(null);

        // When and Then
        assertThrows(NullPointerException.class, () -> abstractBidiMapDecorator.mapIterator());
    }

    @Test
    public void testRemoveValue_NullValue() {
        // Given
        String value = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> abstractBidiMapDecorator.removeValue(value));
    }

    @Test
    public void testValues_Null() {
        // Given
        when(bidiMap.values()).thenReturn(null);

        // When and Then
        assertThrows(NullPointerException.class, () -> abstractBidiMapDecorator.values());
    }
}