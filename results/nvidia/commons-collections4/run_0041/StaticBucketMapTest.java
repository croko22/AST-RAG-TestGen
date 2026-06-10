import org.apache.commons.collections4.map.StaticBucketMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StaticBucketMapTest {

    private StaticBucketMap<String, String> staticBucketMap;

    @BeforeEach
    public void setup() {
        staticBucketMap = new StaticBucketMap<>();
    }

    @Test
    public void testClear() {
        // Given
        staticBucketMap.put("key1", "value1");
        staticBucketMap.put("key2", "value2");

        // When
        staticBucketMap.clear();

        // Then
        assertTrue(staticBucketMap.isEmpty());
    }

    @Test
    public void testContainsKey() {
        // Given
        staticBucketMap.put("key1", "value1");

        // When
        boolean result = staticBucketMap.containsKey("key1");

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsValue() {
        // Given
        staticBucketMap.put("key1", "value1");

        // When
        boolean result = staticBucketMap.containsValue("value1");

        // Then
        assertTrue(result);
    }

    @Test
    public void testEntrySet() {
        // Given
        staticBucketMap.put("key1", "value1");
        staticBucketMap.put("key2", "value2");

        // When
        Map<String, String> entrySet = new HashMap<>();
        for (Map.Entry<String, String> entry : staticBucketMap.entrySet()) {
            entrySet.put(entry.getKey(), entry.getValue());
        }

        // Then
        assertEquals(2, entrySet.size());
    }

    @Test
    public void testEquals() {
        // Given
        staticBucketMap.put("key1", "value1");
        staticBucketMap.put("key2", "value2");
        Map<String, String> otherMap = new HashMap<>();
        otherMap.put("key1", "value1");
        otherMap.put("key2", "value2");

        // When
        boolean result = staticBucketMap.equals(otherMap);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGet() {
        // Given
        staticBucketMap.put("key1", "value1");

        // When
        String result = staticBucketMap.get("key1");

        // Then
        assertEquals("value1", result);
    }

    @Test
    public void testHashCode() {
        // Given
        staticBucketMap.put("key1", "value1");

        // When
        int result = staticBucketMap.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        // Given

        // When
        boolean result = staticBucketMap.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testKeySet() {
        // Given
        staticBucketMap.put("key1", "value1");
        staticBucketMap.put("key2", "value2");

        // When
        Set<String> keySet = staticBucketMap.keySet();

        // Then
        assertEquals(2, keySet.size());
    }

    @Test
    public void testPut() {
        // Given

        // When
        String result = staticBucketMap.put("key1", "value1");

        // Then
        assertNull(result);
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        staticBucketMap.putAll(map);

        // Then
        assertEquals(2, staticBucketMap.size());
    }

    @Test
    public void testRemove() {
        // Given
        staticBucketMap.put("key1", "value1");

        // When
        String result = staticBucketMap.remove("key1");

        // Then
        assertEquals("value1", result);
    }

    @Test
    public void testSize() {
        // Given
        staticBucketMap.put("key1", "value1");
        staticBucketMap.put("key2", "value2");

        // When
        int result = staticBucketMap.size();

        // Then
        assertEquals(2, result);
    }

    @Test
    public void testValues() {
        // Given
        staticBucketMap.put("key1", "value1");
        staticBucketMap.put("key2", "value2");

        // When
        Collection<String> values = staticBucketMap.values();

        // Then
        assertEquals(2, values.size());
    }

    @Test
    public void testAtomic() {
        // Given
        Runnable runnable = () -> {
            staticBucketMap.put("key1", "value1");
            staticBucketMap.put("key2", "value2");
        };

        // When
        staticBucketMap.atomic(runnable);

        // Then
        assertEquals(2, staticBucketMap.size());
    }
}