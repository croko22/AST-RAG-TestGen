import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class DefaultMapEntryTest {

    private DefaultMapEntry<String, String> defaultMapEntry;
    private KeyValue<String, String> keyValue;
    private Map.Entry<String, String> mapEntry;

    @BeforeEach
    public void setup() {
        defaultMapEntry = new DefaultMapEntry<>("key", "value");
        keyValue = new KeyValue<String, String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String getValue() {
                return "value";
            }
        };
        mapEntry = new java.util.AbstractMap.SimpleEntry<>("key", "value");
    }

    @Test
    public void testConstructor_KeyValue() {
        // Given: a KeyValue object
        // When: creating a new DefaultMapEntry with the KeyValue object
        DefaultMapEntry<String, String> entry = new DefaultMapEntry<>(keyValue);
        // Then: the entry should have the same key and value as the KeyValue object
        assertEquals(keyValue.getKey(), entry.getKey());
        assertEquals(keyValue.getValue(), entry.getValue());
    }

    @Test
    public void testConstructor_MapEntry() {
        // Given: a Map.Entry object
        // When: creating a new DefaultMapEntry with the Map.Entry object
        DefaultMapEntry<String, String> entry = new DefaultMapEntry<>(mapEntry);
        // Then: the entry should have the same key and value as the Map.Entry object
        assertEquals(mapEntry.getKey(), entry.getKey());
        assertEquals(mapEntry.getValue(), entry.getValue());
    }

    @Test
    public void testConstructor_KeyValue_Null() {
        // Given: a null KeyValue object
        // When: creating a new DefaultMapEntry with the null KeyValue object
        assertThrows(NullPointerException.class, () -> new DefaultMapEntry<>(null));
    }

    @Test
    public void testConstructor_MapEntry_Null() {
        // Given: a null Map.Entry object
        // When: creating a new DefaultMapEntry with the null Map.Entry object
        assertThrows(NullPointerException.class, () -> new DefaultMapEntry<>(null));
    }

    @Test
    public void testConstructor_Key_Value() {
        // Given: a key and a value
        // When: creating a new DefaultMapEntry with the key and value
        DefaultMapEntry<String, String> entry = new DefaultMapEntry<>("key", "value");
        // Then: the entry should have the same key and value
        assertEquals("key", entry.getKey());
        assertEquals("value", entry.getValue());
    }

    @Test
    public void testConstructor_Key_Value_Null() {
        // Given: a null key and a null value
        // When: creating a new DefaultMapEntry with the null key and null value
        DefaultMapEntry<String, String> entry = new DefaultMapEntry<>(null, null);
        // Then: the entry should have the null key and null value
        assertNull(entry.getKey());
        assertNull(entry.getValue());
    }
}