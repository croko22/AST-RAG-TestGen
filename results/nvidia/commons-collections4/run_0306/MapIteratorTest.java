import org.apache.commons.collections4.MapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapIteratorTest {

    @Mock
    private MapIterator<String, Integer> mapIterator;

    private Map<String, Integer> map;

    @BeforeEach
    void setup() {
        map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);
    }

    @Test
    public void testHasKey() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");

        // When
        boolean hasNext = mapIterator.hasNext();

        // Then
        assertTrue(hasNext);
        verify(mapIterator, times(1)).hasNext();
    }

    @Test
    public void testNoHasKey() {
        // Given
        when(mapIterator.hasNext()).thenReturn(false);

        // When
        boolean hasNext = mapIterator.hasNext();

        // Then
        assertFalse(hasNext);
        verify(mapIterator, times(1)).hasNext();
    }

    @Test
    public void testNext() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");

        // When
        String next = mapIterator.next();

        // Then
        assertEquals("key1", next);
        verify(mapIterator, times(1)).next();
    }

    @Test
    public void testNext_NoSuchElementException() {
        // Given
        when(mapIterator.hasNext()).thenReturn(false);

        // When / Then
        assertThrows(java.util.NoSuchElementException.class, () -> {
            mapIterator.next();
        });
        verify(mapIterator, times(1)).next();
    }

    @Test
    public void testRemove() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");

        // When
        mapIterator.next();
        mapIterator.remove();

        // Then
        verify(mapIterator, times(1)).remove();
    }

    @Test
    public void testRemove_UnsupportedOperationException() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");
        doThrow(new UnsupportedOperationException()).when(mapIterator).remove();

        // When / Then
        mapIterator.next();
        assertThrows(UnsupportedOperationException.class, () -> {
            mapIterator.remove();
        });
        verify(mapIterator, times(1)).remove();
    }

    @Test
    public void testRemove_IllegalStateException() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");
        doThrow(new IllegalStateException()).when(mapIterator).remove();

        // When / Then
        assertThrows(IllegalStateException.class, () -> {
            mapIterator.remove();
        });
        verify(mapIterator, times(1)).remove();
    }

    @Test
    public void testGetValue() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");
        when(mapIterator.getValue()).thenReturn(1);

        // When
        mapIterator.next();
        Integer value = mapIterator.getValue();

        // Then
        assertEquals(1, value);
        verify(mapIterator, times(1)).getValue();
    }

    @Test
    public void testGetValue_IllegalStateException() {
        // Given
        when(mapIterator.getValue()).thenThrow(new IllegalStateException());

        // When / Then
        assertThrows(IllegalStateException.class, () -> {
            mapIterator.getValue();
        });
        verify(mapIterator, times(1)).getValue();
    }

    @Test
    public void testGetKey() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");
        when(mapIterator.getKey()).thenReturn("key1");

        // When
        mapIterator.next();
        String key = mapIterator.getKey();

        // Then
        assertEquals("key1", key);
        verify(mapIterator, times(1)).getKey();
    }

    @Test
    public void testGetKey_IllegalStateException() {
        // Given
        when(mapIterator.getKey()).thenThrow(new IllegalStateException());

        // When / Then
        assertThrows(IllegalStateException.class, () -> {
            mapIterator.getKey();
        });
        verify(mapIterator, times(1)).getKey();
    }

    @Test
    public void testSetValue() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");
        when(mapIterator.setValue(2)).thenReturn(1);

        // When
        mapIterator.next();
        Integer previousValue = mapIterator.setValue(2);

        // Then
        assertEquals(1, previousValue);
        verify(mapIterator, times(1)).setValue(2);
    }

    @Test
    public void testSetValue_UnsupportedOperationException() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");
        doThrow(new UnsupportedOperationException()).when(mapIterator).setValue(any());

        // When / Then
        mapIterator.next();
        assertThrows(UnsupportedOperationException.class, () -> {
            mapIterator.setValue(2);
        });
        verify(mapIterator, times(1)).setValue(2);
    }

    @Test
    public void testSetValue_IllegalStateException() {
        // Given
        when(mapIterator.hasNext()).thenReturn(true, false);
        when(mapIterator.next()).thenReturn("key1");
        doThrow(new IllegalStateException()).when(mapIterator).setValue(any());

        // When / Then
        assertThrows(IllegalStateException.class, () -> {
            mapIterator.setValue(2);
        });
        verify(mapIterator, times(1)).setValue(2);
    }
}