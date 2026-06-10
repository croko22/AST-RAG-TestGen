import org.apache.commons.collections4.map.MultiKeyMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MultiKeyMapTest {

    private MultiKeyMap<String, String> multiKeyMap;

    @BeforeEach
    void setup() {
        multiKeyMap = new MultiKeyMap<>();
    }

    @Test
    void testContainsKey_TwoKeys() {
        multiKeyMap.put("key1", "key2", "value");
        assertTrue(multiKeyMap.containsKey("key1", "key2"));
    }

    @Test
    void testContainsKey_ThreeKeys() {
        multiKeyMap.put("key1", "key2", "key3", "value");
        assertTrue(multiKeyMap.containsKey("key1", "key2", "key3"));
    }

    @Test
    void testContainsKey_FourKeys() {
        multiKeyMap.put("key1", "key2", "key3", "key4", "value");
        assertTrue(multiKeyMap.containsKey("key1", "key2", "key3", "key4"));
    }

    @Test
    void testContainsKey_FiveKeys() {
        multiKeyMap.put("key1", "key2", "key3", "key4", "key5", "value");
        assertTrue(multiKeyMap.containsKey("key1", "key2", "key3", "key4", "key5"));
    }

    @Test
    void testGet_TwoKeys() {
        multiKeyMap.put("key1", "key2", "value");
        assertEquals("value", multiKeyMap.get("key1", "key2"));
    }

    @Test
    void testGet_ThreeKeys() {
        multiKeyMap.put("key1", "key2", "key3", "value");
        assertEquals("value", multiKeyMap.get("key1", "key2", "key3"));
    }

    @Test
    void testGet_FourKeys() {
        multiKeyMap.put("key1", "key2", "key3", "key4", "value");
        assertEquals("value", multiKeyMap.get("key1", "key2", "key3", "key4"));
    }

    @Test
    void testGet_FiveKeys() {
        multiKeyMap.put("key1", "key2", "key3", "key4", "key5", "value");
        assertEquals("value", multiKeyMap.get("key1", "key2", "key3", "key4", "key5"));
    }

    @Test
    void testPut_TwoKeys() {
        String oldValue = multiKeyMap.put("key1", "key2", "value");
        assertNull(oldValue);
        assertEquals("value", multiKeyMap.get("key1", "key2"));
    }

    @Test
    void testPut_ThreeKeys() {
        String oldValue = multiKeyMap.put("key1", "key2", "key3", "value");
        assertNull(oldValue);
        assertEquals("value", multiKeyMap.get("key1", "key2", "key3"));
    }

    @Test
    void testPut_FourKeys() {
        String oldValue = multiKeyMap.put("key1", "key2", "key3", "key4", "value");
        assertNull(oldValue);
        assertEquals("value", multiKeyMap.get("key1", "key2", "key3", "key4"));
    }

    @Test
    void testPut_FiveKeys() {
        String oldValue = multiKeyMap.put("key1", "key2", "key3", "key4", "key5", "value");
        assertNull(oldValue);
        assertEquals("value", multiKeyMap.get("key1", "key2", "key3", "key4", "key5"));
    }

    @Test
    void testRemoveMultiKey_TwoKeys() {
        multiKeyMap.put("key1", "key2", "value");
        String oldValue = multiKeyMap.removeMultiKey("key1", "key2");
        assertEquals("value", oldValue);
        assertNull(multiKeyMap.get("key1", "key2"));
    }

    @Test
    void testRemoveMultiKey_ThreeKeys() {
        multiKeyMap.put("key1", "key2", "key3", "value");
        String oldValue = multiKeyMap.removeMultiKey("key1", "key2", "key3");
        assertEquals("value", oldValue);
        assertNull(multiKeyMap.get("key1", "key2", "key3"));
    }

    @Test
    void testRemoveMultiKey_FourKeys() {
        multiKeyMap.put("key1", "key2", "key3", "key4", "value");
        String oldValue = multiKeyMap.removeMultiKey("key1", "key2", "key3", "key4");
        assertEquals("value", oldValue);
        assertNull(multiKeyMap.get("key1", "key2", "key3", "key4"));
    }

    @Test
    void testRemoveMultiKey_FiveKeys() {
        multiKeyMap.put("key1", "key2", "key3", "key4", "key5", "value");
        String oldValue = multiKeyMap.removeMultiKey("key1", "key2", "key3", "key4", "key5");
        assertEquals("value", oldValue);
        assertNull(multiKeyMap.get("key1", "key2", "key3", "key4", "key5"));
    }

    @Test
    void testRemoveAll_OneKey() {
        multiKeyMap.put("key1", "key2", "value");
        assertTrue(multiKeyMap.removeAll("key1"));
        assertNull(multiKeyMap.get("key1", "key2"));
    }

    @Test
    void testRemoveAll_TwoKeys() {
        multiKeyMap.put("key1", "key2", "value");
        assertTrue(multiKeyMap.removeAll("key1", "key2"));
        assertNull(multiKeyMap.get("key1", "key2"));
    }

    @Test
    void testRemoveAll_ThreeKeys() {
        multiKeyMap.put("key1", "key2", "key3", "value");
        assertTrue(multiKeyMap.removeAll("key1", "key2", "key3"));
        assertNull(multiKeyMap.get("key1", "key2", "key3"));
    }

    @Test
    void testRemoveAll_FourKeys() {
        multiKeyMap.put("key1", "key2", "key3", "key4", "value");
        assertTrue(multiKeyMap.removeAll("key1", "key2", "key3", "key4"));
        assertNull(multiKeyMap.get("key1", "key2", "key3", "key4"));
    }
}