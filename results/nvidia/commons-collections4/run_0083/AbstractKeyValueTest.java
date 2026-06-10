import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.AbstractKeyValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AbstractKeyValueTest {

    @Mock
    private Object key;

    @Mock
    private Object value;

    private AbstractKeyValue<Object, Object> abstractKeyValue;

    @BeforeEach
    void setup() {
        abstractKeyValue = new AbstractKeyValue<>(key, value) {};
    }

    @Test
    void testGetKey() {
        // Given: abstractKeyValue with key and value
        // When: getKey is called
        Object result = abstractKeyValue.getKey();
        // Then: result should be equal to key
        assertEquals(key, result);
    }

    @Test
    void testGetValue() {
        // Given: abstractKeyValue with key and value
        // When: getValue is called
        Object result = abstractKeyValue.getValue();
        // Then: result should be equal to value
        assertEquals(value, result);
    }

    @Test
    void testToString() {
        // Given: abstractKeyValue with key and value
        // When: toString is called
        String result = abstractKeyValue.toString();
        // Then: result should be in the format "key=value"
        assertEquals(key + "=" + value, result);
    }

    @Test
    void testGetKey_NullKey() {
        // Given: abstractKeyValue with null key and value
        abstractKeyValue = new AbstractKeyValue<>(null, value) {};
        // When: getKey is called
        Object result = abstractKeyValue.getKey();
        // Then: result should be null
        assertEquals(null, result);
    }

    @Test
    void testGetValue_NullValue() {
        // Given: abstractKeyValue with key and null value
        abstractKeyValue = new AbstractKeyValue<>(key, null) {};
        // When: getValue is called
        Object result = abstractKeyValue.getValue();
        // Then: result should be null
        assertEquals(null, result);
    }

    @Test
    void testToString_NullKey() {
        // Given: abstractKeyValue with null key and value
        abstractKeyValue = new AbstractKeyValue<>(null, value) {};
        // When: toString is called
        String result = abstractKeyValue.toString();
        // Then: result should be in the format "null=value"
        assertEquals("null=" + value, result);
    }

    @Test
    void testToString_NullValue() {
        // Given: abstractKeyValue with key and null value
        abstractKeyValue = new AbstractKeyValue<>(key, null) {};
        // When: toString is called
        String result = abstractKeyValue.toString();
        // Then: result should be in the format "key=null"
        assertEquals(key + "=null", result);
    }
}