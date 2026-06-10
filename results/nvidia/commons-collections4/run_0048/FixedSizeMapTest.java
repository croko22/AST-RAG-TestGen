import org.apache.commons.collections4.map.FixedSizeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FixedSizeMapTest {

    @Mock
    private Map<String, String> map;

    @InjectMocks
    private FixedSizeMap<String, String> fixedSizeMap;

    @BeforeEach
    public void setup() {
        fixedSizeMap = FixedSizeMap.fixedSizeMap(new HashMap<>());
    }

    @Test
    public void testFixedSizeMap() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");

        // When
        FixedSizeMap<String, String> fixedSizeMap = FixedSizeMap.fixedSizeMap(originalMap);

        // Then
        assertEquals(originalMap, fixedSizeMap);
    }

    @Test
    public void testClear() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeMap.clear());
    }

    @Test
    public void testEntrySet() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        fixedSizeMap = FixedSizeMap.fixedSizeMap(originalMap);

        // When
        Set<Map.Entry<String, String>> entrySet = fixedSizeMap.entrySet();

        // Then
        assertEquals(2, entrySet.size());
    }

    @Test
    public void testIsFull() {
        // When
        boolean isFull = fixedSizeMap.isFull();

        // Then
        assertTrue(isFull);
    }

    @Test
    public void testKeySet() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        fixedSizeMap = FixedSizeMap.fixedSizeMap(originalMap);

        // When
        Set<String> keySet = fixedSizeMap.keySet();

        // Then
        assertEquals(2, keySet.size());
    }

    @Test
    public void testMaxSize() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        fixedSizeMap = FixedSizeMap.fixedSizeMap(originalMap);

        // When
        int maxSize = fixedSizeMap.maxSize();

        // Then
        assertEquals(2, maxSize);
    }

    @Test
    public void testPutExistingKey() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        fixedSizeMap = FixedSizeMap.fixedSizeMap(originalMap);

        // When
        String result = fixedSizeMap.put("key1", "newValue");

        // Then
        assertEquals("value1", result);
    }

    @Test
    public void testPutNewKey() {
        // When and Then
        assertThrows(IllegalArgumentException.class, () -> fixedSizeMap.put("newKey", "newValue"));
    }

    @Test
    public void testPutAllExistingKeys() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        Map<String, String> mapToPut = new HashMap<>();
        mapToPut.put("key1", "newValue1");
        mapToPut.put("key2", "newValue2");
        fixedSizeMap = FixedSizeMap.fixedSizeMap(originalMap);

        // When
        fixedSizeMap.putAll(mapToPut);

        // Then
        assertEquals(mapToPut, fixedSizeMap);
    }

    @Test
    public void testPutAllNewKeys() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        Map<String, String> mapToPut = new HashMap<>();
        mapToPut.put("newKey1", "newValue1");
        mapToPut.put("newKey2", "newValue2");
        fixedSizeMap = FixedSizeMap.fixedSizeMap(originalMap);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> fixedSizeMap.putAll(mapToPut));
    }

    @Test
    public void testRemove() {
        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> fixedSizeMap.remove("key"));
    }

    @Test
    public void testValues() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        fixedSizeMap = FixedSizeMap.fixedSizeMap(originalMap);

        // When
        Collection<String> values = fixedSizeMap.values();

        // Then
        assertEquals(2, values.size());
    }
}