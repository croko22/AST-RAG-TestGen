import org.apache.commons.collections4.map.LRUMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LRUMapTest {

    private LRUMap<String, String> lruMap;

    @BeforeEach
    public void setup() {
        lruMap = new LRUMap<>();
    }

    @Test
    public void testClone() {
        // Given
        lruMap.put("key1", "value1");
        lruMap.put("key2", "value2");

        // When
        LRUMap<String, String> clonedMap = lruMap.clone();

        // Then
        assertEquals(lruMap.size(), clonedMap.size());
        assertEquals(lruMap.get("key1"), clonedMap.get("key1"));
        assertEquals(lruMap.get("key2"), clonedMap.get("key2"));
    }

    @Test
    public void testGet() {
        // Given
        lruMap.put("key1", "value1");
        lruMap.put("key2", "value2");

        // When
        String value = lruMap.get("key1");

        // Then
        assertEquals("value1", value);
    }

    @Test
    public void testGet_UpdateToMRU() {
        // Given
        lruMap.put("key1", "value1");
        lruMap.put("key2", "value2");

        // When
        String value = lruMap.get("key1", true);

        // Then
        assertEquals("value1", value);
    }

    @Test
    public void testIsFull() {
        // Given
        lruMap = new LRUMap<>(1);
        lruMap.put("key1", "value1");

        // When
        boolean isFull = lruMap.isFull();

        // Then
        assertTrue(isFull);
    }

    @Test
    public void testIsScanUntilRemovable() {
        // Given
        lruMap = new LRUMap<>(1, true);

        // When
        boolean isScanUntilRemovable = lruMap.isScanUntilRemovable();

        // Then
        assertTrue(isScanUntilRemovable);
    }

    @Test
    public void testMaxSize() {
        // Given
        lruMap = new LRUMap<>(10);

        // When
        int maxSize = lruMap.maxSize();

        // Then
        assertEquals(10, maxSize);
    }

    @Test
    public void testPut() {
        // Given
        lruMap = new LRUMap<>(1);

        // When
        lruMap.put("key1", "value1");
        lruMap.put("key2", "value2");

        // Then
        assertEquals(1, lruMap.size());
        assertEquals("value2", lruMap.get("key2"));
    }

    @Test
    public void testPut_AllowNullValues() {
        // Given
        lruMap = new LRUMap<>(1);

        // When
        lruMap.put("key1", null);

        // Then
        assertEquals(1, lruMap.size());
        assertNull(lruMap.get("key1"));
    }

    @Test
    public void testPut_WithNullKey() {
        // Given
        lruMap = new LRUMap<>(1);

        // When and Then
        assertThrows(NullPointerException.class, () -> lruMap.put(null, "value1"));
    }

    @Test
    public void testPut_WithNullValue() {
        // Given
        lruMap = new LRUMap<>(1);

        // When
        lruMap.put("key1", null);

        // Then
        assertEquals(1, lruMap.size());
        assertNull(lruMap.get("key1"));
    }

    @Test
    public void testRemove() {
        // Given
        lruMap = new LRUMap<>(1);
        lruMap.put("key1", "value1");

        // When
        lruMap.remove("key1");

        // Then
        assertEquals(0, lruMap.size());
    }

    @Test
    public void testRemove_WithNullKey() {
        // Given
        lruMap = new LRUMap<>(1);

        // When and Then
        assertThrows(NullPointerException.class, () -> lruMap.remove(null));
    }

    @Test
    public void testConstructor_WithMap() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        lruMap = new LRUMap<>(map);

        // Then
        assertEquals(2, lruMap.size());
        assertEquals("value1", lruMap.get("key1"));
        assertEquals("value2", lruMap.get("key2"));
    }

    @Test
    public void testConstructor_WithMaxSize() {
        // Given
        lruMap = new LRUMap<>(1);

        // When and Then
        assertEquals(1, lruMap.maxSize());
    }

    @Test
    public void testConstructor_WithMaxSizeAndLoadFactor() {
        // Given
        lruMap = new LRUMap<>(1, 0.5f);

        // When and Then
        assertEquals(1, lruMap.maxSize());
    }

    @Test
    public void testConstructor_WithMaxSizeAndScanUntilRemovable() {
        // Given
        lruMap = new LRUMap<>(1, true);

        // When and Then
        assertTrue(lruMap.isScanUntilRemovable());
    }
}