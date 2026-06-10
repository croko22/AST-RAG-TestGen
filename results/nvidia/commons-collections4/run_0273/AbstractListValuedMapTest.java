import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractListValuedMapTest {

    @Mock
    private Map<String, List<String>> map;

    private AbstractListValuedMap<String, String> abstractListValuedMap;

    @BeforeEach
    void setup() {
        abstractListValuedMap = new AbstractListValuedMap<String, String>(map) {
            @Override
            protected List<String> createCollection() {
                return new ArrayList<>();
            }
        };
    }

    @Test
    void testGet() {
        // Given
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(map.get("key")).thenReturn(values);

        // When
        List<String> result = abstractListValuedMap.get("key");

        // Then
        assertEquals(values, result);
        verify(map, times(1)).get("key");
    }

    @Test
    void testGetEmpty() {
        // Given
        when(map.get("key")).thenReturn(null);

        // When
        List<String> result = abstractListValuedMap.get("key");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(map, times(1)).get("key");
    }

    @Test
    void testRemove() {
        // Given
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(map.get("key")).thenReturn(values);

        // When
        List<String> result = abstractListValuedMap.remove("key");

        // Then
        assertEquals(values, result);
        verify(map, times(1)).remove("key");
    }

    @Test
    void testRemoveEmpty() {
        // Given
        when(map.get("key")).thenReturn(null);

        // When
        List<String> result = abstractListValuedMap.remove("key");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(map, times(1)).remove("key");
    }

    @Test
    void testListIterator() {
        // Given
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(map.get("key")).thenReturn(values);

        // When
        ListIterator<String> iterator = abstractListValuedMap.get("key").listIterator();

        // Then
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
        verify(map, times(1)).get("key");
    }

    @Test
    void testListIteratorEmpty() {
        // Given
        when(map.get("key")).thenReturn(null);

        // When
        ListIterator<String> iterator = abstractListValuedMap.get("key").listIterator();

        // Then
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
        verify(map, times(1)).get("key");
    }

    @Test
    void testAdd() {
        // Given
        List<String> values = new ArrayList<>();
        when(map.get("key")).thenReturn(values);

        // When
        abstractListValuedMap.get("key").add("value");

        // Then
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
        verify(map, times(1)).get("key");
    }

    @Test
    void testAddAll() {
        // Given
        List<String> values = new ArrayList<>();
        when(map.get("key")).thenReturn(values);
        Collection<String> collection = new ArrayList<>();
        collection.add("value1");
        collection.add("value2");

        // When
        abstractListValuedMap.get("key").addAll(collection);

        // Then
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        verify(map, times(1)).get("key");
    }

    @Test
    void testRemoveIndex() {
        // Given
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(map.get("key")).thenReturn(values);

        // When
        String result = abstractListValuedMap.get("key").remove(0);

        // Then
        assertEquals("value1", result);
        assertEquals(1, values.size());
        assertEquals("value2", values.get(0));
        verify(map, times(1)).get("key");
    }

    @Test
    void testSet() {
        // Given
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(map.get("key")).thenReturn(values);

        // When
        String result = abstractListValuedMap.get("key").set(0, "newValue");

        // Then
        assertEquals("value1", result);
        assertEquals(2, values.size());
        assertEquals("newValue", values.get(0));
        assertEquals("value2", values.get(1));
        verify(map, times(1)).get("key");
    }

    @Test
    void testSubList() {
        // Given
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        values.add("value3");
        when(map.get("key")).thenReturn(values);

        // When
        List<String> result = abstractListValuedMap.get("key").subList(1, 3);

        // Then
        assertEquals(2, result.size());
        assertEquals("value2", result.get(0));
        assertEquals("value3", result.get(1));
        verify(map, times(1)).get("key");
    }
}