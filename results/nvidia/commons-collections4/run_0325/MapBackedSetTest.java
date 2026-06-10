import org.apache.commons.collections4.set.MapBackedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapBackedSetTest {

    @Mock
    private Map<String, Object> map;

    private MapBackedSet<String, Object> mapBackedSet;

    @BeforeEach
    public void setup() {
        mapBackedSet = MapBackedSet.mapBackedSet(map);
    }

    @Test
    public void testMapBackedSet() {
        // Given
        Map<String, Object> map = new HashMap<>();
        MapBackedSet<String, Object> mapBackedSet = MapBackedSet.mapBackedSet(map);

        // Then
        assertNotNull(mapBackedSet);
    }

    @Test
    public void testMapBackedSet_DummyValue() {
        // Given
        Map<String, Object> map = new HashMap<>();
        Object dummyValue = new Object();
        MapBackedSet<String, Object> mapBackedSet = MapBackedSet.mapBackedSet(map, dummyValue);

        // Then
        assertNotNull(mapBackedSet);
    }

    @Test
    public void testAdd() {
        // Given
        String obj = "obj";
        when(map.containsKey(obj)).thenReturn(false);
        when(map.put(obj, any())).thenReturn(null);

        // When
        boolean result = mapBackedSet.add(obj);

        // Then
        assertTrue(result);
        verify(map, times(1)).put(obj, any());
    }

    @Test
    public void testAdd_Existing() {
        // Given
        String obj = "obj";
        when(map.containsKey(obj)).thenReturn(true);

        // When
        boolean result = mapBackedSet.add(obj);

        // Then
        assertFalse(result);
        verify(map, times(1)).put(obj, any());
    }

    @Test
    public void testAddAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("obj1");
        coll.add("obj2");
        when(map.containsKey("obj1")).thenReturn(false);
        when(map.containsKey("obj2")).thenReturn(false);
        when(map.put("obj1", any())).thenReturn(null);
        when(map.put("obj2", any())).thenReturn(null);

        // When
        boolean result = mapBackedSet.addAll(coll);

        // Then
        assertTrue(result);
        verify(map, times(1)).put("obj1", any());
        verify(map, times(1)).put("obj2", any());
    }

    @Test
    public void testAddAll_NoChange() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("obj1");
        coll.add("obj2");
        when(map.containsKey("obj1")).thenReturn(true);
        when(map.containsKey("obj2")).thenReturn(true);

        // When
        boolean result = mapBackedSet.addAll(coll);

        // Then
        assertFalse(result);
        verify(map, times(1)).put("obj1", any());
        verify(map, times(1)).put("obj2", any());
    }

    @Test
    public void testClear() {
        // Given
        when(map.isEmpty()).thenReturn(false);

        // When
        mapBackedSet.clear();

        // Then
        verify(map, times(1)).clear();
    }

    @Test
    public void testContains() {
        // Given
        String obj = "obj";
        when(map.containsKey(obj)).thenReturn(true);

        // When
        boolean result = mapBackedSet.contains(obj);

        // Then
        assertTrue(result);
    }

    @Test
    public void testContains_Not() {
        // Given
        String obj = "obj";
        when(map.containsKey(obj)).thenReturn(false);

        // When
        boolean result = mapBackedSet.contains(obj);

        // Then
        assertFalse(result);
    }

    @Test
    public void testContainsAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("obj1");
        coll.add("obj2");
        when(map.containsKey("obj1")).thenReturn(true);
        when(map.containsKey("obj2")).thenReturn(true);

        // When
        boolean result = mapBackedSet.containsAll(coll);

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsAll_Not() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("obj1");
        coll.add("obj2");
        when(map.containsKey("obj1")).thenReturn(true);
        when(map.containsKey("obj2")).thenReturn(false);

        // When
        boolean result = mapBackedSet.containsAll(coll);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals() {
        // Given
        MapBackedSet<String, Object> other = MapBackedSet.mapBackedSet(map);
        when(map.keySet()).thenReturn(map.keySet());

        // When
        boolean result = mapBackedSet.equals(other);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_Not() {
        // Given
        MapBackedSet<String, Object> other = MapBackedSet.mapBackedSet(new HashMap<>());
        when(map.keySet()).thenReturn(map.keySet());

        // When
        boolean result = mapBackedSet.equals(other);

        // Then
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given
        when(map.keySet()).thenReturn(map.keySet());

        // When
        int result = mapBackedSet.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        // Given
        when(map.isEmpty()).thenReturn(true);

        // When
        boolean result = mapBackedSet.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_Not() {
        // Given
        when(map.isEmpty()).thenReturn(false);

        // When
        boolean result = mapBackedSet.isEmpty();

        // Then
        assertFalse(result);
    }

    @Test
    public void testIterator() {
        // Given
        when(map.keySet()).thenReturn(map.keySet());

        // When
        Iterator<String> result = mapBackedSet.iterator();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testRemove() {
        // Given
        String obj = "obj";
        when(map.containsKey(obj)).thenReturn(true);
        when(map.remove(obj)).thenReturn(any());

        // When
        boolean result = mapBackedSet.remove(obj);

        // Then
        assertTrue(result);
        verify(map, times(1)).remove(obj);
    }

    @Test
    public void testRemove_Not() {
        // Given
        String obj = "obj";
        when(map.containsKey(obj)).thenReturn(false);

        // When
        boolean result = mapBackedSet.remove(obj);

        // Then
        assertFalse(result);
        verify(map, times(1)).remove(obj);
    }

    @Test
    public void testRemoveAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("obj1");
        coll.add("obj2");
        when(map.containsKey("obj1")).thenReturn(true);
        when(map.containsKey("obj2")).thenReturn(true);

        // When
        boolean result = mapBackedSet.removeAll(coll);

        // Then
        assertTrue(result);
        verify(map, times(1)).keySet();
    }

    @Test
    public void testRemoveAll_Not() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("obj1");
        coll.add("obj2");
        when(map.containsKey("obj1")).thenReturn(false);
        when(map.containsKey("obj2")).thenReturn(false);

        // When
        boolean result = mapBackedSet.removeAll(coll);

        // Then
        assertFalse(result);
        verify(map, times(1)).keySet();
    }

    @Test
    public void testRemoveIf() {
        // Given
        Predicate<String> filter = obj -> true;
        when(map.keySet()).thenReturn(map.keySet());

        // When
        boolean result = mapBackedSet.removeIf(filter);

        // Then
        assertTrue(result);
        verify(map, times(1)).keySet();
    }

    @Test
    public void testRetainAll() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("obj1");
        coll.add("obj2");
        when(map.containsKey("obj1")).thenReturn(true);
        when(map.containsKey("obj2")).thenReturn(true);

        // When
        boolean result = mapBackedSet.retainAll(coll);

        // Then
        assertTrue(result);
        verify(map, times(1)).keySet();
    }

    @Test
    public void testRetainAll_Not() {
        // Given
        Collection<String> coll = new ArrayList<>();
        coll.add("obj1");
        coll.add("obj2");
        when(map.containsKey("obj1")).thenReturn(false);
        when(map.containsKey("obj2")).thenReturn(false);

        // When
        boolean result = mapBackedSet.retainAll(coll);

        // Then
        assertFalse(result);
        verify(map, times(1)).keySet();
    }

    @Test
    public void testSize() {
        // Given
        when(map.size()).thenReturn(10);

        // When
        int result = mapBackedSet.size();

        // Then
        assertEquals(10, result);
    }

    @Test
    public void testToArray() {
        // Given
        when(map.keySet()).thenReturn(map.keySet());

        // When
        Object[] result = mapBackedSet.toArray();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testToArray_Type() {
        // Given
        when(map.keySet()).thenReturn(map.keySet());

        // When
        String[] result = mapBackedSet.toArray(new String[0]);

        // Then
        assertNotNull(result);
    }
}