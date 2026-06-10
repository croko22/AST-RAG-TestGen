import org.apache.commons.collections4.ListValuedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListValuedMapTest {

    @Mock
    private ListValuedMap<String, String> listValuedMap;

    @BeforeEach
    void setup() {
        // Initialize the mock object
        listValuedMap = mock(ListValuedMap.class);
    }

    @Test
    public void testGet_KeyExists() {
        // Given: a key with associated values
        String key = "key";
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(listValuedMap.get(key)).thenReturn(values);

        // When: get the list of values associated with the key
        List<String> result = listValuedMap.get(key);

        // Then: verify the result and the interaction with the mock
        assertNotNull(result);
        assertEquals(values, result);
        verify(listValuedMap, times(1)).get(key);
    }

    @Test
    public void testGet_KeyDoesNotExist() {
        // Given: a key with no associated values
        String key = "key";
        List<String> values = new ArrayList<>();
        when(listValuedMap.get(key)).thenReturn(values);

        // When: get the list of values associated with the key
        List<String> result = listValuedMap.get(key);

        // Then: verify the result and the interaction with the mock
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(listValuedMap, times(1)).get(key);
    }

    @Test
    public void testGet_NullKey() {
        // Given: a null key
        String key = null;

        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> listValuedMap.get(key));
    }

    @Test
    public void testRemove_KeyExists() {
        // Given: a key with associated values
        String key = "key";
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        when(listValuedMap.remove(key)).thenReturn(values);

        // When: remove all values associated with the key
        List<String> result = listValuedMap.remove(key);

        // Then: verify the result and the interaction with the mock
        assertNotNull(result);
        assertEquals(values, result);
        verify(listValuedMap, times(1)).remove(key);
    }

    @Test
    public void testRemove_KeyDoesNotExist() {
        // Given: a key with no associated values
        String key = "key";
        List<String> values = new ArrayList<>();
        when(listValuedMap.remove(key)).thenReturn(values);

        // When: remove all values associated with the key
        List<String> result = listValuedMap.remove(key);

        // Then: verify the result and the interaction with the mock
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(listValuedMap, times(1)).remove(key);
    }

    @Test
    public void testRemove_NullKey() {
        // Given: a null key
        String key = null;

        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> listValuedMap.remove(key));
    }

    @Test
    public void testRemove_UnmodifiableMap() {
        // Given: an unmodifiable map
        String key = "key";
        when(listValuedMap.remove(key)).thenThrow(UnsupportedOperationException.class);

        // When / Then: expect an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> listValuedMap.remove(key));
    }
}