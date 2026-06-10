import org.apache.commons.collections4.Put;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PutTest {

    @Mock
    private Put<String, String> put;

    @BeforeEach
    public void setup() {
        // Initialize the mock object
        put = mock(Put.class);
    }

    @Test
    public void testClear() {
        // Given: a Put instance
        // When: clear is called
        put.clear();
        // Then: verify the clear method was called
        verify(put, times(1)).clear();
    }

    @Test
    public void testPut_KeyNotPresent() {
        // Given: a key and value
        String key = "key";
        String value = "value";
        // When: put is called with the key and value
        when(put.put(key, value)).thenReturn(null);
        Object result = put.put(key, value);
        // Then: verify the result is null and the put method was called
        assertNull(result);
        verify(put, times(1)).put(key, value);
    }

    @Test
    public void testPut_KeyPresent() {
        // Given: a key, value, and previous value
        String key = "key";
        String value = "value";
        String previousValue = "previousValue";
        // When: put is called with the key and value
        when(put.put(key, value)).thenReturn(previousValue);
        Object result = put.put(key, value);
        // Then: verify the result is the previous value and the put method was called
        assertEquals(previousValue, result);
        verify(put, times(1)).put(key, value);
    }

    @Test
    public void testPutAll() {
        // Given: a map of key-value pairs
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        // When: putAll is called with the map
        put.putAll(map);
        // Then: verify the putAll method was called
        verify(put, times(1)).putAll(map);
    }

    @Test
    public void testPutAll_NullMap() {
        // Given: a null map
        Map<String, String> map = null;
        // When: putAll is called with the null map
        assertThrows(NullPointerException.class, () -> put.putAll(map));
        // Then: verify the putAll method was not called
        verify(put, never()).putAll(any());
    }

    @Test
    public void testPut_NullKey() {
        // Given: a null key and value
        String key = null;
        String value = "value";
        // When: put is called with the null key and value
        assertThrows(NullPointerException.class, () -> put.put(key, value));
        // Then: verify the put method was not called
        verify(put, never()).put(any(), any());
    }

    @Test
    public void testPut_NullValue() {
        // Given: a key and null value
        String key = "key";
        String value = null;
        // When: put is called with the key and null value
        put.put(key, value);
        // Then: verify the put method was called
        verify(put, times(1)).put(key, value);
    }
}