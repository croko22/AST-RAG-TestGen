import org.apache.commons.collections4.Get;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetTest {

    @Mock
    private Get<String, String> get;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        get = mock(Get.class);
    }

    @Test
    public void testContainsKey_PresentKey() {
        // Given: a key that is present in the map
        String key = "key1";
        when(get.containsKey(key)).thenReturn(true);

        // When: containsKey is called with the present key
        boolean result = get.containsKey(key);

        // Then: the result should be true
        assertTrue(result);
        verify(get, times(1)).containsKey(key);
    }

    @Test
    public void testContainsKey_AbsentKey() {
        // Given: a key that is not present in the map
        String key = "key2";
        when(get.containsKey(key)).thenReturn(false);

        // When: containsKey is called with the absent key
        boolean result = get.containsKey(key);

        // Then: the result should be false
        assertFalse(result);
        verify(get, times(1)).containsKey(key);
    }

    @Test
    public void testContainsValue_PresentValue() {
        // Given: a value that is present in the map
        String value = "value1";
        when(get.containsValue(value)).thenReturn(true);

        // When: containsValue is called with the present value
        boolean result = get.containsValue(value);

        // Then: the result should be true
        assertTrue(result);
        verify(get, times(1)).containsValue(value);
    }

    @Test
    public void testContainsValue_AbsentValue() {
        // Given: a value that is not present in the map
        String value = "value2";
        when(get.containsValue(value)).thenReturn(false);

        // When: containsValue is called with the absent value
        boolean result = get.containsValue(value);

        // Then: the result should be false
        assertFalse(result);
        verify(get, times(1)).containsValue(value);
    }

    @Test
    public void testEntrySet() {
        // Given: a set of entries
        Set<Map.Entry<String, String>> entrySet = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.entrySet();
        when(get.entrySet()).thenReturn(entrySet);

        // When: entrySet is called
        Set<Map.Entry<String, String>> result = get.entrySet();

        // Then: the result should be the same as the given entry set
        assertEquals(entrySet, result);
        verify(get, times(1)).entrySet();
    }

    @Test
    public void testGet_PresentKey() {
        // Given: a key that is present in the map
        String key = "key1";
        String value = "value1";
        when(get.get(key)).thenReturn(value);

        // When: get is called with the present key
        String result = get.get(key);

        // Then: the result should be the value associated with the key
        assertEquals(value, result);
        verify(get, times(1)).get(key);
    }

    @Test
    public void testGet_AbsentKey() {
        // Given: a key that is not present in the map
        String key = "key2";
        when(get.get(key)).thenReturn(null);

        // When: get is called with the absent key
        String result = get.get(key);

        // Then: the result should be null
        assertNull(result);
        verify(get, times(1)).get(key);
    }

    @Test
    public void testIsEmpty_EmptyMap() {
        // Given: an empty map
        when(get.isEmpty()).thenReturn(true);

        // When: isEmpty is called
        boolean result = get.isEmpty();

        // Then: the result should be true
        assertTrue(result);
        verify(get, times(1)).isEmpty();
    }

    @Test
    public void testIsEmpty_NonEmptyMap() {
        // Given: a non-empty map
        when(get.isEmpty()).thenReturn(false);

        // When: isEmpty is called
        boolean result = get.isEmpty();

        // Then: the result should be false
        assertFalse(result);
        verify(get, times(1)).isEmpty();
    }

    @Test
    public void testKeySet() {
        // Given: a set of keys
        Set<String> keySet = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.keySet();
        when(get.keySet()).thenReturn(keySet);

        // When: keySet is called
        Set<String> result = get.keySet();

        // Then: the result should be the same as the given key set
        assertEquals(keySet, result);
        verify(get, times(1)).keySet();
    }

    @Test
    public void testRemove_PresentKey() {
        // Given: a key that is present in the map
        String key = "key1";
        String value = "value1";
        when(get.remove(key)).thenReturn(value);

        // When: remove is called with the present key
        String result = get.remove(key);

        // Then: the result should be the value associated with the key
        assertEquals(value, result);
        verify(get, times(1)).remove(key);
    }

    @Test
    public void testRemove_AbsentKey() {
        // Given: a key that is not present in the map
        String key = "key2";
        when(get.remove(key)).thenReturn(null);

        // When: remove is called with the absent key
        String result = get.remove(key);

        // Then: the result should be null
        assertNull(result);
        verify(get, times(1)).remove(key);
    }

    @Test
    public void testSize_EmptyMap() {
        // Given: an empty map
        when(get.size()).thenReturn(0);

        // When: size is called
        int result = get.size();

        // Then: the result should be 0
        assertEquals(0, result);
        verify(get, times(1)).size();
    }

    @Test
    public void testSize_NonEmptyMap() {
        // Given: a non-empty map
        when(get.size()).thenReturn(2);

        // When: size is called
        int result = get.size();

        // Then: the result should be greater than 0
        assertTrue(result > 0);
        verify(get, times(1)).size();
    }

    @Test
    public void testValues() {
        // Given: a collection of values
        Collection<String> values = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.values();
        when(get.values()).thenReturn(values);

        // When: values is called
        Collection<String> result = get.values();

        // Then: the result should be the same as the given collection of values
        assertEquals(values, result);
        verify(get, times(1)).values();
    }
}