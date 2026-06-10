import net.hydromatic.morel.util.MapEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MapEntryTest {

    @Test
    public void testToString() {
        // Given: a MapEntry with key and value
        MapEntry<String, String> entry = new MapEntry<>("key", "value");

        // When: toString is called
        String result = entry.toString();

        // Then: the result is as expected
        assertEquals("<key, value>", result);
    }

    @Test
    public void testEquals_SameInstance() {
        // Given: a MapEntry
        MapEntry<String, String> entry = new MapEntry<>("key", "value");

        // When: equals is called with the same instance
        boolean result = entry.equals(entry);

        // Then: the result is true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_SameKeyAndValue() {
        // Given: two MapEntry instances with the same key and value
        MapEntry<String, String> entry1 = new MapEntry<>("key", "value");
        MapEntry<String, String> entry2 = new MapEntry<>("key", "value");

        // When: equals is called
        boolean result = entry1.equals(entry2);

        // Then: the result is true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_DifferentKey() {
        // Given: two MapEntry instances with different keys
        MapEntry<String, String> entry1 = new MapEntry<>("key1", "value");
        MapEntry<String, String> entry2 = new MapEntry<>("key2", "value");

        // When: equals is called
        boolean result = entry1.equals(entry2);

        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentInstance_DifferentValue() {
        // Given: two MapEntry instances with different values
        MapEntry<String, String> entry1 = new MapEntry<>("key", "value1");
        MapEntry<String, String> entry2 = new MapEntry<>("key", "value2");

        // When: equals is called
        boolean result = entry1.equals(entry2);

        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_Null() {
        // Given: a MapEntry and null
        MapEntry<String, String> entry = new MapEntry<>("key", "value");

        // When: equals is called with null
        boolean result = entry.equals(null);

        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: a MapEntry and an instance of a different class
        MapEntry<String, String> entry = new MapEntry<>("key", "value");
        Object obj = new Object();

        // When: equals is called with the different class instance
        boolean result = entry.equals(obj);

        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given: a MapEntry
        MapEntry<String, String> entry = new MapEntry<>("key", "value");

        // When: hashCode is called
        int result = entry.hashCode();

        // Then: the result is not zero
        assertNotEquals(0, result);
    }

    @Test
    public void testGetKey() {
        // Given: a MapEntry
        MapEntry<String, String> entry = new MapEntry<>("key", "value");

        // When: getKey is called
        String result = entry.getKey();

        // Then: the result is as expected
        assertEquals("key", result);
    }

    @Test
    public void testGetValue() {
        // Given: a MapEntry
        MapEntry<String, String> entry = new MapEntry<>("key", "value");

        // When: getValue is called
        String result = entry.getValue();

        // Then: the result is as expected
        assertEquals("value", result);
    }

    @Test
    public void testSetValue() {
        // Given: a MapEntry
        MapEntry<String, String> entry = new MapEntry<>("key", "value");

        // When: setValue is called
        assertThrows(UnsupportedOperationException.class, () -> entry.setValue("new value"));
    }
}