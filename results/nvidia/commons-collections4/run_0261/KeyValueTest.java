import org.apache.commons.collections4.KeyValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KeyValueTest {

    private KeyValue<String, String> keyValue;

    @BeforeEach
    public void setup() {
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
    }

    @Test
    public void testGetKey() {
        // Given: keyValue instance with a key
        // When: getKey method is called
        String key = keyValue.getKey();
        // Then: key is returned
        assertNotNull(key);
        assertEquals("key", key);
    }

    @Test
    public void testGetValue() {
        // Given: keyValue instance with a value
        // When: getValue method is called
        String value = keyValue.getValue();
        // Then: value is returned
        assertNotNull(value);
        assertEquals("value", value);
    }

    @Test
    public void testGetKey_Null() {
        // Given: keyValue instance with a null key
        KeyValue<String, String> nullKeyKeyValue = new KeyValue<String, String>() {
            @Override
            public String getKey() {
                return null;
            }

            @Override
            public String getValue() {
                return "value";
            }
        };
        // When: getKey method is called
        String key = nullKeyKeyValue.getKey();
        // Then: null key is returned
        assertNull(key);
    }

    @Test
    public void testGetValue_Null() {
        // Given: keyValue instance with a null value
        KeyValue<String, String> nullValueKeyValue = new KeyValue<String, String>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String getValue() {
                return null;
            }
        };
        // When: getValue method is called
        String value = nullValueKeyValue.getValue();
        // Then: null value is returned
        assertNull(value);
    }
}