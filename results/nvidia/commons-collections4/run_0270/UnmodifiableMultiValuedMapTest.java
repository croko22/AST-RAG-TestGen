import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections4.map.UnmodifiableMap;
import org.apache.commons.collections4.multiset.UnmodifiableMultiSet;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableMultiValuedMapTest {

    @Mock
    private MultiValuedMap<String, String> multiValuedMap;

    private UnmodifiableMultiValuedMap<String, String> unmodifiableMultiValuedMap;

    @BeforeEach
    void setup() {
        unmodifiableMultiValuedMap = UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(multiValuedMap);
    }

    @Test
    void testUnmodifiableMultiValuedMap() {
        // Given
        when(multiValuedMap.asMap()).thenReturn(Map.of("key", Collection.of("value")));

        // When
        Map<String, Collection<String>> asMap = unmodifiableMultiValuedMap.asMap();

        // Then
        assertNotNull(asMap);
        assertEquals(1, asMap.size());
        assertTrue(asMap.containsKey("key"));
        assertEquals(Collection.of("value"), asMap.get("key"));
        verify(multiValuedMap, times(1)).asMap();
    }

    @Test
    void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiValuedMap.clear());
        verify(multiValuedMap, never()).clear();
    }

    @Test
    void testEntries() {
        // Given
        when(multiValuedMap.entries()).thenReturn(Collection.of(Map.entry("key", "value")));

        // When
        Collection<Map.Entry<String, String>> entries = unmodifiableMultiValuedMap.entries();

        // Then
        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertTrue(entries.contains(Map.entry("key", "value")));
        verify(multiValuedMap, times(1)).entries();
    }

    @Test
    void testGet() {
        // Given
        when(multiValuedMap.get(any())).thenReturn(Collection.of("value"));

        // When
        Collection<String> values = unmodifiableMultiValuedMap.get("key");

        // Then
        assertNotNull(values);
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
        verify(multiValuedMap, times(1)).get(any());
    }

    @Test
    void testKeys() {
        // Given
        when(multiValuedMap.keys()).thenReturn(MultiSet.of("key"));

        // When
        MultiSet<String> keys = unmodifiableMultiValuedMap.keys();

        // Then
        assertNotNull(keys);
        assertEquals(1, keys.size());
        assertTrue(keys.contains("key"));
        verify(multiValuedMap, times(1)).keys();
    }

    @Test
    void testKeySet() {
        // Given
        when(multiValuedMap.keySet()).thenReturn(Set.of("key"));

        // When
        Set<String> keySet = unmodifiableMultiValuedMap.keySet();

        // Then
        assertNotNull(keySet);
        assertEquals(1, keySet.size());
        assertTrue(keySet.contains("key"));
        verify(multiValuedMap, times(1)).keySet();
    }

    @Test
    void testMapIterator() {
        // Given
        when(multiValuedMap.mapIterator()).thenReturn(new UnmodifiableMapIterator<String, String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String getValue() {
                return "value";
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public String next() {
                return "key";
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String setValue(String value) {
                throw new UnsupportedOperationException();
            }
        });

        // When
        MapIterator<String, String> mapIterator = unmodifiableMultiValuedMap.mapIterator();

        // Then
        assertNotNull(mapIterator);
        assertEquals("key", mapIterator.getKey());
        assertEquals("value", mapIterator.getValue());
        assertTrue(mapIterator.hasNext());
        assertEquals("key", mapIterator.next());
        assertThrows(UnsupportedOperationException.class, () -> mapIterator.remove());
        assertThrows(UnsupportedOperationException.class, () -> mapIterator.setValue("new value"));
        verify(multiValuedMap, times(1)).mapIterator();
    }

    @Test
    void testPut() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiValuedMap.put("key", "value"));
        verify(multiValuedMap, never()).put(any(), any());
    }

    @Test
    void testPutAll() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiValuedMap.putAll("key", Collection.of("value")));
        verify(multiValuedMap, never()).putAll(any(), any());
    }

    @Test
    void testPutAllMap() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiValuedMap.putAll(Map.of("key", "value")));
        verify(multiValuedMap, never()).putAll(any());
    }

    @Test
    void testPutAllMultiValuedMap() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiValuedMap.putAll(multiValuedMap));
        verify(multiValuedMap, never()).putAll(any());
    }

    @Test
    void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiValuedMap.remove("key"));
        verify(multiValuedMap, never()).remove(any());
    }

    @Test
    void testRemoveMapping() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableMultiValuedMap.removeMapping("key", "value"));
        verify(multiValuedMap, never()).removeMapping(any(), any());
    }

    @Test
    void testValues() {
        // Given
        when(multiValuedMap.values()).thenReturn(Collection.of("value"));

        // When
        Collection<String> values = unmodifiableMultiValuedMap.values();

        // Then
        assertNotNull(values);
        assertEquals(1, values.size());
        assertTrue(values.contains("value"));
        verify(multiValuedMap, times(1)).values();
    }
}