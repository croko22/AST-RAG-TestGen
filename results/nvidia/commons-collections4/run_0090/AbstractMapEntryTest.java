import org.apache.commons.collections4.keyvalue.AbstractMapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class AbstractMapEntryTest {

    private AbstractMapEntry<String, String> entry;

    @BeforeEach
    public void setup() {
        entry = new AbstractMapEntry<>("key", "value") {};
    }

    @Test
    public void testEquals_SameObject() {
        // Given: same object
        Object obj = entry;

        // When: equals is called
        boolean result = entry.equals(obj);

        // Then: result is true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameKeyAndValue() {
        // Given: different object with same key and value
        AbstractMapEntry<String, String> other = new AbstractMapEntry<>("key", "value") {};

        // When: equals is called
        boolean result = entry.equals(other);

        // Then: result is true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentKey() {
        // Given: different object with different key
        AbstractMapEntry<String, String> other = new AbstractMapEntry<>("differentKey", "value") {};

        // When: equals is called
        boolean result = entry.equals(other);

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentValue() {
        // Given: different object with different value
        AbstractMapEntry<String, String> other = new AbstractMapEntry<>("key", "differentValue") {};

        // When: equals is called
        boolean result = entry.equals(other);

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_Null() {
        // Given: null object
        Object obj = null;

        // When: equals is called
        boolean result = entry.equals(obj);

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: different class object
        Object obj = new Object();

        // When: equals is called
        boolean result = entry.equals(obj);

        // Then: result is false
        assertFalse(result);
    }

    @Test
    public void testHashCode_KeyAndValueNotNull() {
        // Given: key and value are not null
        entry = new AbstractMapEntry<>("key", "value") {};

        // When: hashCode is called
        int result = entry.hashCode();

        // Then: result is not zero
        assertNotEquals(0, result);
    }

    @Test
    public void testHashCode_KeyIsNull() {
        // Given: key is null
        entry = new AbstractMapEntry<>(null, "value") {};

        // When: hashCode is called
        int result = entry.hashCode();

        // Then: result is not zero
        assertNotEquals(0, result);
    }

    @Test
    public void testHashCode_ValueIsNull() {
        // Given: value is null
        entry = new AbstractMapEntry<>("key", null) {};

        // When: hashCode is called
        int result = entry.hashCode();

        // Then: result is not zero
        assertNotEquals(0, result);
    }

    @Test
    public void testHashCode_BothNull() {
        // Given: both key and value are null
        entry = new AbstractMapEntry<>(null, null) {};

        // When: hashCode is called
        int result = entry.hashCode();

        // Then: result is zero
        assertEquals(0, result);
    }

    @Test
    public void testSetValue_NewValue() {
        // Given: new value
        String newValue = "newValue";

        // When: setValue is called
        String result = entry.setValue(newValue);

        // Then: result is the old value
        assertEquals("value", result);
        assertEquals(newValue, entry.getValue());
    }

    @Test
    public void testSetValue_NullValue() {
        // Given: null value
        String newValue = null;

        // When: setValue is called
        String result = entry.setValue(newValue);

        // Then: result is the old value
        assertEquals("value", result);
        assertNull(entry.getValue());
    }
}