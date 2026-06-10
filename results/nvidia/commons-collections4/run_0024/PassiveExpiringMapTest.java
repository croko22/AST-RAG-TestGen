import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PassiveExpiringMapTest {

    @Mock
    private Map<String, String> map;

    private PassiveExpiringMap<String, String> passiveExpiringMap;

    @BeforeEach
    void setup() {
        passiveExpiringMap = new PassiveExpiringMap<>(map);
    }

    @AfterEach
    void tearDown() {
        passiveExpiringMap.clear();
    }

    @Test
    void testClear() {
        // Given
        passiveExpiringMap.put("key1", "value1");
        passiveExpiringMap.put("key2", "value2");

        // When
        passiveExpiringMap.clear();

        // Then
        assertTrue(passiveExpiringMap.isEmpty());
    }

    @Test
    void testContainsKey() {
        // Given
        passiveExpiringMap.put("key1", "value1");

        // When
        boolean result = passiveExpiringMap.containsKey("key1");

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsValue() {
        // Given
        passiveExpiringMap.put("key1", "value1");

        // When
        boolean result = passiveExpiringMap.containsValue("value1");

        // Then
        assertTrue(result);
    }

    @Test
    void testEntrySet() {
        // Given
        passiveExpiringMap.put("key1", "value1");
        passiveExpiringMap.put("key2", "value2");

        // When
        int size = passiveExpiringMap.entrySet().size();

        // Then
        assertEquals(2, size);
    }

    @Test
    void testGet() {
        // Given
        passiveExpiringMap.put("key1", "value1");

        // When
        String result = passiveExpiringMap.get("key1");

        // Then
        assertEquals("value1", result);
    }

    @Test
    void testIsEmpty() {
        // Given

        // When
        boolean result = passiveExpiringMap.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void testKeySet() {
        // Given
        passiveExpiringMap.put("key1", "value1");
        passiveExpiringMap.put("key2", "value2");

        // When
        int size = passiveExpiringMap.keySet().size();

        // Then
        assertEquals(2, size);
    }

    @Test
    void testPut() {
        // Given

        // When
        String result = passiveExpiringMap.put("key1", "value1");

        // Then
        assertNull(result);
    }

    @Test
    void testPutAll() {
        // Given
        Map<String, String> mapToCopy = new HashMap<>();
        mapToCopy.put("key1", "value1");
        mapToCopy.put("key2", "value2");

        // When
        passiveExpiringMap.putAll(mapToCopy);

        // Then
        assertEquals(2, passiveExpiringMap.size());
    }

    @Test
    void testRemove() {
        // Given
        passiveExpiringMap.put("key1", "value1");

        // When
        String result = passiveExpiringMap.remove("key1");

        // Then
        assertEquals("value1", result);
    }

    @Test
    void testSize() {
        // Given
        passiveExpiringMap.put("key1", "value1");
        passiveExpiringMap.put("key2", "value2");

        // When
        int result = passiveExpiringMap.size();

        // Then
        assertEquals(2, result);
    }

    @Test
    void testValues() {
        // Given
        passiveExpiringMap.put("key1", "value1");
        passiveExpiringMap.put("key2", "value2");

        // When
        int size = passiveExpiringMap.values().size();

        // Then
        assertEquals(2, size);
    }
}