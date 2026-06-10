import org.apache.commons.collections4.map.CompositeMap;
import org.junit.jupiter.api.AfterEach;
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
public class CompositeMapTest {

    @Mock
    private Map<String, String> map1;

    @Mock
    private Map<String, String> map2;

    private CompositeMap<String, String> compositeMap;

    @BeforeEach
    public void setup() {
        compositeMap = new CompositeMap<>();
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(map1, map2);
    }

    @Test
    public void testAddComposited() {
        // Given
        when(map1.containsKey(any())).thenReturn(false);

        // When
        compositeMap.addComposited(map1);

        // Then
        verify(map1, times(1)).keySet();
    }

    @Test
    public void testAddComposited_KeyCollision() {
        // Given
        when(map1.containsKey(any())).thenReturn(true);
        when(map2.containsKey(any())).thenReturn(true);
        Set<String> intersect = new HashSet<>();
        intersect.add("key");
        when(CollectionUtils.intersection(any(), any())).thenReturn(intersect);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> compositeMap.addComposited(map1));
    }

    @Test
    public void testClear() {
        // Given
        compositeMap.addComposited(map1);
        when(map1.clear()).thenReturn(null);

        // When
        compositeMap.clear();

        // Then
        verify(map1, times(1)).clear();
    }

    @Test
    public void testContainsKey() {
        // Given
        compositeMap.addComposited(map1);
        when(map1.containsKey(any())).thenReturn(true);

        // When
        boolean result = compositeMap.containsKey("key");

        // Then
        assertTrue(result);
        verify(map1, times(1)).containsKey("key");
    }

    @Test
    public void testContainsValue() {
        // Given
        compositeMap.addComposited(map1);
        when(map1.containsValue(any())).thenReturn(true);

        // When
        boolean result = compositeMap.containsValue("value");

        // Then
        assertTrue(result);
        verify(map1, times(1)).containsValue("value");
    }

    @Test
    public void testEntrySet() {
        // Given
        compositeMap.addComposited(map1);

        // When
        Set<Map.Entry<String, String>> entrySet = compositeMap.entrySet();

        // Then
        assertNotNull(entrySet);
    }

    @Test
    public void testEquals() {
        // Given
        CompositeMap<String, String> otherMap = new CompositeMap<>();
        otherMap.addComposited(map1);

        // When
        boolean result = compositeMap.equals(otherMap);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGet() {
        // Given
        compositeMap.addComposited(map1);
        when(map1.get(any())).thenReturn("value");

        // When
        String result = compositeMap.get("key");

        // Then
        assertEquals("value", result);
        verify(map1, times(1)).get("key");
    }

    @Test
    public void testHashCode() {
        // Given
        compositeMap.addComposited(map1);

        // When
        int result = compositeMap.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        // Given
        when(map1.isEmpty()).thenReturn(true);

        // When
        boolean result = compositeMap.isEmpty();

        // Then
        assertTrue(result);
        verify(map1, times(1)).isEmpty();
    }

    @Test
    public void testKeySet() {
        // Given
        compositeMap.addComposited(map1);

        // When
        Set<String> keySet = compositeMap.keySet();

        // Then
        assertNotNull(keySet);
    }

    @Test
    public void testPut() {
        // Given
        compositeMap.setMutator(new CompositeMap.MapMutator<String, String>() {
            @Override
            public String put(CompositeMap<String, String> map, Map<String, String>[] composited, String key, String value) {
                return "oldValue";
            }

            @Override
            public void putAll(CompositeMap<String, String> map, Map<String, String>[] composited, Map<? extends String, ? extends String> mapToAdd) {

            }

            @Override
            public void resolveCollision(CompositeMap<String, String> composite, Map<String, String> existing, Map<String, String> added, Collection<String> intersect) {

            }
        });

        // When
        String result = compositeMap.put("key", "value");

        // Then
        assertEquals("oldValue", result);
    }

    @Test
    public void testPutAll() {
        // Given
        compositeMap.setMutator(new CompositeMap.MapMutator<String, String>() {
            @Override
            public String put(CompositeMap<String, String> map, Map<String, String>[] composited, String key, String value) {
                return null;
            }

            @Override
            public void putAll(CompositeMap<String, String> map, Map<String, String>[] composited, Map<? extends String, ? extends String> mapToAdd) {

            }

            @Override
            public void resolveCollision(CompositeMap<String, String> composite, Map<String, String> existing, Map<String, String> added, Collection<String> intersect) {

            }
        });

        // When
        compositeMap.putAll(map1);

        // Then
        verify(map1, times(1)).entrySet();
    }

    @Test
    public void testRemove() {
        // Given
        compositeMap.addComposited(map1);
        when(map1.remove(any())).thenReturn("value");

        // When
        String result = compositeMap.remove("key");

        // Then
        assertEquals("value", result);
        verify(map1, times(1)).remove("key");
    }

    @Test
    public void testRemoveComposited() {
        // Given
        compositeMap.addComposited(map1);

        // When
        Map<String, String> result = compositeMap.removeComposited(map1);

        // Then
        assertEquals(map1, result);
    }

    @Test
    public void testSetMutator() {
        // Given
        CompositeMap.MapMutator<String, String> mutator = new CompositeMap.MapMutator<String, String>() {
            @Override
            public String put(CompositeMap<String, String> map, Map<String, String>[] composited, String key, String value) {
                return null;
            }

            @Override
            public void putAll(CompositeMap<String, String> map, Map<String, String>[] composited, Map<? extends String, ? extends String> mapToAdd) {

            }

            @Override
            public void resolveCollision(CompositeMap<String, String> composite, Map<String, String> existing, Map<String, String> added, Collection<String> intersect) {

            }
        };

        // When
        compositeMap.setMutator(mutator);

        // Then
        assertNotNull(compositeMap);
    }

    @Test
    public void testSize() {
        // Given
        when(map1.size()).thenReturn(10);

        // When
        int result = compositeMap.size();

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testValues() {
        // Given
        compositeMap.addComposited(map1);

        // When
        Collection<String> values = compositeMap.values();

        // Then
        assertNotNull(values);
    }
}