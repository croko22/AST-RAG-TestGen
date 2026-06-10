import org.apache.commons.collections4.SetValuedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SetValuedMapTest {

    @Mock
    private SetValuedMap<String, String> setValuedMap;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        setValuedMap = mock(SetValuedMap.class);
    }

    @Test
    public void testGet_KeyExists() {
        // Given: a key with associated values
        String key = "key";
        Set<String> values = new HashSet<>();
        values.add("value1");
        values.add("value2");
        when(setValuedMap.get(key)).thenReturn(values);

        // When: get the set of values associated with the key
        Set<String> result = setValuedMap.get(key);

        // Then: verify the result and the interaction with the mock
        assertNotNull(result);
        assertEquals(values, result);
        verify(setValuedMap, times(1)).get(key);
    }

    @Test
    public void testGet_KeyDoesNotExist() {
        // Given: a key with no associated values
        String key = "key";
        Set<String> values = new HashSet<>();
        when(setValuedMap.get(key)).thenReturn(values);

        // When: get the set of values associated with the key
        Set<String> result = setValuedMap.get(key);

        // Then: verify the result and the interaction with the mock
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(setValuedMap, times(1)).get(key);
    }

    @Test
    public void testGet_NullKey() {
        // Given: a null key
        String key = null;

        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> setValuedMap.get(key));
    }

    @Test
    public void testRemove_KeyExists() {
        // Given: a key with associated values
        String key = "key";
        Set<String> values = new HashSet<>();
        values.add("value1");
        values.add("value2");
        when(setValuedMap.remove(key)).thenReturn(values);

        // When: remove all values associated with the key
        Set<String> result = setValuedMap.remove(key);

        // Then: verify the result and the interaction with the mock
        assertNotNull(result);
        assertEquals(values, result);
        verify(setValuedMap, times(1)).remove(key);
    }

    @Test
    public void testRemove_KeyDoesNotExist() {
        // Given: a key with no associated values
        String key = "key";
        when(setValuedMap.remove(key)).thenReturn(null);

        // When: remove all values associated with the key
        Set<String> result = setValuedMap.remove(key);

        // Then: verify the result and the interaction with the mock
        assertNull(result);
        verify(setValuedMap, times(1)).remove(key);
    }

    @Test
    public void testRemove_NullKey() {
        // Given: a null key
        String key = null;

        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> setValuedMap.remove(key));
    }

    @Test
    public void testRemove_UnmodifiableMap() {
        // Given: an unmodifiable map
        String key = "key";
        when(setValuedMap.remove(key)).thenThrow(UnsupportedOperationException.class);

        // When / Then: expect an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> setValuedMap.remove(key));
    }
}