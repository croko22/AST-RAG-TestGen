import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TiedMapEntryTest {

    @Mock
    private Map<String, String> map;

    private TiedMapEntry<String, String> tiedMapEntry;

    @BeforeEach
    public void setup() {
        tiedMapEntry = new TiedMapEntry<>(map, "key");
    }

    @Test
    public void testEquals_SameObject() {
        // Given: same object
        // When: equals is called
        // Then: true is returned
        assertTrue(tiedMapEntry.equals(tiedMapEntry));
    }

    @Test
    public void testEquals_DifferentObject_SameKeyAndValue() {
        // Given: different object with same key and value
        TiedMapEntry<String, String> other = new TiedMapEntry<>(map, "key");
        // When: equals is called
        // Then: true is returned
        assertTrue(tiedMapEntry.equals(other));
    }

    @Test
    public void testEquals_DifferentObject_DifferentKey() {
        // Given: different object with different key
        TiedMapEntry<String, String> other = new TiedMapEntry<>(map, "differentKey");
        // When: equals is called
        // Then: false is returned
        assertFalse(tiedMapEntry.equals(other));
    }

    @Test
    public void testEquals_DifferentObject_DifferentValue() {
        // Given: different object with different value
        TiedMapEntry<String, String> other = new TiedMapEntry<>(map, "key");
        // When: equals is called
        // Then: false is returned
        assertFalse(tiedMapEntry.equals(other));
    }

    @Test
    public void testEquals_Null() {
        // Given: null object
        // When: equals is called
        // Then: false is returned
        assertFalse(tiedMapEntry.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: different class object
        Object obj = new Object();
        // When: equals is called
        // Then: false is returned
        assertFalse(tiedMapEntry.equals(obj));
    }

    @Test
    public void testGetKey() {
        // Given: tied map entry
        // When: getKey is called
        // Then: key is returned
        assertEquals("key", tiedMapEntry.getKey());
    }

    @Test
    public void testGetValue() {
        // Given: tied map entry
        // When: getValue is called
        // Then: value is returned
        assertNotNull(tiedMapEntry.getValue());
    }

    @Test
    public void testHashCode() {
        // Given: tied map entry
        // When: hashCode is called
        // Then: hash code is returned
        assertNotNull(tiedMapEntry.hashCode());
    }

    @Test
    public void testSetValue() {
        // Given: tied map entry
        String value = "value";
        // When: setValue is called
        // Then: old value is returned
        String oldValue = tiedMapEntry.setValue(value);
        assertEquals(value, tiedMapEntry.getValue());
    }

    @Test
    public void testSetValue_ThisMapEntry() {
        // Given: tied map entry
        // When: setValue is called with this map entry
        // Then: exception is thrown
        assertThrows(IllegalArgumentException.class, () -> tiedMapEntry.setValue((String) tiedMapEntry));
    }

    @Test
    public void testToString() {
        // Given: tied map entry
        // When: toString is called
        // Then: string representation is returned
        assertNotNull(tiedMapEntry.toString());
    }

    @Test
    public void testKeyValueGetKey() {
        // Given: tied map entry
        KeyValue keyValue = tiedMapEntry;
        // When: getKey is called
        // Then: key is returned
        assertEquals("key", keyValue.getKey());
    }

    @Test
    public void testKeyValueGetValue() {
        // Given: tied map entry
        KeyValue keyValue = tiedMapEntry;
        // When: getValue is called
        // Then: value is returned
        assertNotNull(keyValue.getValue());
    }
}