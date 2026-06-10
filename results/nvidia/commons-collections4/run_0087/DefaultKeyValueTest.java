import org.apache.commons.collections4.DefaultKeyValue;
import org.apache.commons.collections4.DefaultMapEntry;
import org.apache.commons.collections4.KeyValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultKeyValueTest {

    @Mock
    private KeyValue<String, String> keyValue;

    private DefaultKeyValue<String, String> defaultKeyValue;

    @BeforeEach
    public void setup() {
        defaultKeyValue = new DefaultKeyValue<>();
    }

    @Test
    public void testEquals_SameObject() {
        // Given: same object
        // When: equals is called
        boolean result = defaultKeyValue.equals(defaultKeyValue);
        // Then: true is returned
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameKeyValue() {
        // Given: different object with same key and value
        DefaultKeyValue<String, String> other = new DefaultKeyValue<>("key", "value");
        defaultKeyValue = new DefaultKeyValue<>("key", "value");
        // When: equals is called
        boolean result = defaultKeyValue.equals(other);
        // Then: true is returned
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentKeyValue() {
        // Given: different object with different key and value
        DefaultKeyValue<String, String> other = new DefaultKeyValue<>("key", "value");
        defaultKeyValue = new DefaultKeyValue<>("differentKey", "differentValue");
        // When: equals is called
        boolean result = defaultKeyValue.equals(other);
        // Then: false is returned
        assertFalse(result);
    }

    @Test
    public void testEquals_NullObject() {
        // Given: null object
        // When: equals is called
        boolean result = defaultKeyValue.equals(null);
        // Then: false is returned
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: different class
        // When: equals is called
        boolean result = defaultKeyValue.equals(new Object());
        // Then: false is returned
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given: key and value
        defaultKeyValue = new DefaultKeyValue<>("key", "value");
        // When: hashCode is called
        int result = defaultKeyValue.hashCode();
        // Then: hash code is calculated correctly
        int expectedHashCode = ("key".hashCode() ^ "value".hashCode());
        assertEquals(expectedHashCode, result);
    }

    @Test
    public void testSetKey() {
        // Given: key
        String key = "newKey";
        // When: setKey is called
        String oldKey = defaultKeyValue.setKey(key);
        // Then: old key is returned and new key is set
        assertNull(oldKey);
        assertEquals(key, defaultKeyValue.getKey());
    }

    @Test
    public void testSetKey_SameObject() {
        // Given: same object as key
        // When: setKey is called
        assertThrows(IllegalArgumentException.class, () -> defaultKeyValue.setKey(defaultKeyValue));
    }

    @Test
    public void testSetValue() {
        // Given: value
        String value = "newValue";
        // When: setValue is called
        String oldValue = defaultKeyValue.setValue(value);
        // Then: old value is returned and new value is set
        assertNull(oldValue);
        assertEquals(value, defaultKeyValue.getValue());
    }

    @Test
    public void testSetValue_SameObject() {
        // Given: same object as value
        // When: setValue is called
        assertThrows(IllegalArgumentException.class, () -> defaultKeyValue.setValue(defaultKeyValue));
    }

    @Test
    public void testToMapEntry() {
        // Given: key and value
        defaultKeyValue = new DefaultKeyValue<>("key", "value");
        // When: toMapEntry is called
        Map.Entry<String, String> mapEntry = defaultKeyValue.toMapEntry();
        // Then: map entry is created correctly
        assertNotNull(mapEntry);
        assertEquals("key", mapEntry.getKey());
        assertEquals("value", mapEntry.getValue());
    }
}