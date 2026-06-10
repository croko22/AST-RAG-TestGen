import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;
import org.apache.commons.collections4.map.UnmodifiableOrderedMap;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableOrderedMapTest {

    @Mock
    private OrderedMap<String, String> orderedMap;

    private UnmodifiableOrderedMap<String, String> unmodifiableOrderedMap;

    @BeforeEach
    void setup() {
        unmodifiableOrderedMap = new UnmodifiableOrderedMap<>(orderedMap);
    }

    @Test
    void testClear() {
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedMap.clear());
        verify(orderedMap, never()).clear();
    }

    @Test
    void testEntrySet() {
        Set<Map.Entry<String, String>> entrySet = unmodifiableOrderedMap.entrySet();
        assertNotNull(entrySet);
        assertNotSame(unmodifiableOrderedMap, entrySet);
        verify(orderedMap).entrySet();
    }

    @Test
    void testKeySet() {
        Set<String> keySet = unmodifiableOrderedMap.keySet();
        assertNotNull(keySet);
        assertNotSame(unmodifiableOrderedMap, keySet);
        verify(orderedMap).keySet();
    }

    @Test
    void testMapIterator() {
        UnmodifiableOrderedMapIterator<String, String> mapIterator = (UnmodifiableOrderedMapIterator<String, String>) unmodifiableOrderedMap.mapIterator();
        assertNotNull(mapIterator);
        assertNotSame(unmodifiableOrderedMap, mapIterator);
        verify(orderedMap).mapIterator();
    }

    @Test
    void testPut() {
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedMap.put("key", "value"));
        verify(orderedMap, never()).put(any(), any());
    }

    @Test
    void testPutAll() {
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedMap.putAll(mock(Map.class)));
        verify(orderedMap, never()).putAll(any());
    }

    @Test
    void testRemove() {
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableOrderedMap.remove("key"));
        verify(orderedMap, never()).remove(any());
    }

    @Test
    void testValues() {
        Collection<String> values = unmodifiableOrderedMap.values();
        assertNotNull(values);
        assertNotSame(unmodifiableOrderedMap, values);
        verify(orderedMap).values();
    }

    @Test
    void testUnmodifiableOrderedMapFactoryMethod() {
        OrderedMap<String, String> map = mock(OrderedMap.class);
        UnmodifiableOrderedMap<String, String> unmodifiableMap = UnmodifiableOrderedMap.unmodifiableOrderedMap(map);
        assertNotNull(unmodifiableMap);
        assertNotSame(map, unmodifiableMap);
    }

    @Test
    void testUnmodifiableOrderedMapFactoryMethodWithUnmodifiableMap() {
        OrderedMap<String, String> map = mock(OrderedMap.class);
        when(map instanceof Unmodifiable).thenReturn(true);
        UnmodifiableOrderedMap<String, String> unmodifiableMap = UnmodifiableOrderedMap.unmodifiableOrderedMap(map);
        assertSame(map, unmodifiableMap);
    }
}