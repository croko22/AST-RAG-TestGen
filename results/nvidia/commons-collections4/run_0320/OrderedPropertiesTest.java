import org.apache.commons.collections4.properties.OrderedProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class OrderedPropertiesTest {

    private OrderedProperties orderedProperties;

    @BeforeEach
    public void setup() {
        orderedProperties = new OrderedProperties();
    }

    @Test
    public void testClear() {
        // Given
        orderedProperties.put("key1", "value1");
        orderedProperties.put("key2", "value2");

        // When
        orderedProperties.clear();

        // Then
        assertTrue(orderedProperties.isEmpty());
    }

    @Test
    public void testCompute() {
        // Given
        orderedProperties.put("key1", "value1");

        // When
        Object result = orderedProperties.compute("key1", (k, v) -> "newValue");

        // Then
        assertEquals("newValue", result);
        assertEquals("newValue", orderedProperties.get("key1"));
    }

    @Test
    public void testComputeIfAbsent() {
        // Given

        // When
        Object result = orderedProperties.computeIfAbsent("key1", k -> "value1");

        // Then
        assertEquals("value1", result);
        assertEquals("value1", orderedProperties.get("key1"));
    }

    @Test
    public void testEntrySet() {
        // Given
        orderedProperties.put("key1", "value1");
        orderedProperties.put("key2", "value2");

        // When
        Set<Map.Entry<Object, Object>> entrySet = orderedProperties.entrySet();

        // Then
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>("key1", "value1")));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>("key2", "value2")));
    }

    @Test
    public void testForEach() {
        // Given
        orderedProperties.put("key1", "value1");
        orderedProperties.put("key2", "value2");
        Map<Object, Object> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        // When
        Map<Object, Object> actual = new HashMap<>();
        orderedProperties.forEach((k, v) -> actual.put(k, v));

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testKeys() {
        // Given
        orderedProperties.put("key1", "value1");
        orderedProperties.put("key2", "value2");

        // When
        Enumeration<Object> keys = orderedProperties.keys();

        // Then
        List<Object> keyList = new ArrayList<>();
        while (keys.hasMoreElements()) {
            keyList.add(keys.nextElement());
        }
        assertEquals(Arrays.asList("key1", "key2"), keyList);
    }

    @Test
    public void testKeySet() {
        // Given
        orderedProperties.put("key1", "value1");
        orderedProperties.put("key2", "value2");

        // When
        Set<Object> keySet = orderedProperties.keySet();

        // Then
        assertEquals(new LinkedHashSet<>(Arrays.asList("key1", "key2")), keySet);
    }

    @Test
    public void testMerge() {
        // Given
        orderedProperties.put("key1", "value1");

        // When
        Object result = orderedProperties.merge("key1", "newValue", (v1, v2) -> v1 + v2);

        // Then
        assertEquals("value1newValue", result);
        assertEquals("value1newValue", orderedProperties.get("key1"));
    }

    @Test
    public void testPropertyNames() {
        // Given
        orderedProperties.put("key1", "value1");
        orderedProperties.put("key2", "value2");

        // When
        Enumeration<?> propertyNames = orderedProperties.propertyNames();

        // Then
        List<Object> propertyNameList = new ArrayList<>();
        while (propertyNames.hasMoreElements()) {
            propertyNameList.add(propertyNames.nextElement());
        }
        assertEquals(Arrays.asList("key1", "key2"), propertyNameList);
    }

    @Test
    public void testPut() {
        // Given

        // When
        Object result = orderedProperties.put("key1", "value1");

        // Then
        assertNull(result);
        assertEquals("value1", orderedProperties.get("key1"));
    }

    @Test
    public void testPutAll() {
        // Given
        Map<Object, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        orderedProperties.putAll(map);

        // Then
        assertEquals("value1", orderedProperties.get("key1"));
        assertEquals("value2", orderedProperties.get("key2"));
    }

    @Test
    public void testPutIfAbsent() {
        // Given

        // When
        Object result = orderedProperties.putIfAbsent("key1", "value1");

        // Then
        assertNull(result);
        assertEquals("value1", orderedProperties.get("key1"));
    }

    @Test
    public void testRemove() {
        // Given
        orderedProperties.put("key1", "value1");

        // When
        Object result = orderedProperties.remove("key1");

        // Then
        assertEquals("value1", result);
        assertNull(orderedProperties.get("key1"));
    }

    @Test
    public void testRemoveWithTwoArgs() {
        // Given
        orderedProperties.put("key1", "value1");

        // When
        boolean result = orderedProperties.remove("key1", "value1");

        // Then
        assertTrue(result);
        assertNull(orderedProperties.get("key1"));
    }

    @Test
    public void testToString() {
        // Given
        orderedProperties.put("key1", "value1");
        orderedProperties.put("key2", "value2");

        // When
        String result = orderedProperties.toString();

        // Then
        assertEquals("{key1=value1, key2=value2}", result);
    }
}