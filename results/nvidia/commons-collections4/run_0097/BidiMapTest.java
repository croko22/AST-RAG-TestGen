import org.apache.commons.collections4.BidiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidiMapTest {

    @Mock
    private BidiMap<String, String> bidiMap;

    @InjectMocks
    private BidiMap<String, String> bidiMapToTest;

    @BeforeEach
    void setup() {
        // Initialize the bidiMap with some data
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        when(bidiMap.entrySet()).thenReturn(map.entrySet());
    }

    @Test
    void testGetKey() {
        // Given
        String value = "value1";
        String expectedKey = "key1";
        when(bidiMap.getKey(value)).thenReturn(expectedKey);

        // When
        String key = bidiMap.getKey(value);

        // Then
        assertEquals(expectedKey, key);
        verify(bidiMap, times(1)).getKey(value);
    }

    @Test
    void testGetKey_NotFound() {
        // Given
        String value = "non-existent-value";
        when(bidiMap.getKey(value)).thenReturn(null);

        // When
        String key = bidiMap.getKey(value);

        // Then
        assertNull(key);
        verify(bidiMap, times(1)).getKey(value);
    }

    @Test
    void testInverseBidiMap() {
        // Given
        BidiMap<String, String> expectedInverseMap = mock(BidiMap.class);
        when(bidiMap.inverseBidiMap()).thenReturn(expectedInverseMap);

        // When
        BidiMap<String, String> inverseMap = bidiMap.inverseBidiMap();

        // Then
        assertEquals(expectedInverseMap, inverseMap);
        verify(bidiMap, times(1)).inverseBidiMap();
    }

    @Test
    void testPut() {
        // Given
        String key = "new-key";
        String value = "new-value";
        String expectedPreviousValue = "previous-value";
        when(bidiMap.put(key, value)).thenReturn(expectedPreviousValue);

        // When
        String previousValue = bidiMap.put(key, value);

        // Then
        assertEquals(expectedPreviousValue, previousValue);
        verify(bidiMap, times(1)).put(key, value);
    }

    @Test
    void testPut_ExistingKey() {
        // Given
        String key = "existing-key";
        String value = "new-value";
        String expectedPreviousValue = "previous-value";
        when(bidiMap.put(key, value)).thenReturn(expectedPreviousValue);

        // When
        String previousValue = bidiMap.put(key, value);

        // Then
        assertEquals(expectedPreviousValue, previousValue);
        verify(bidiMap, times(1)).put(key, value);
    }

    @Test
    void testRemoveValue() {
        // Given
        String value = "value-to-remove";
        String expectedRemovedKey = "removed-key";
        when(bidiMap.removeValue(value)).thenReturn(expectedRemovedKey);

        // When
        String removedKey = bidiMap.removeValue(value);

        // Then
        assertEquals(expectedRemovedKey, removedKey);
        verify(bidiMap, times(1)).removeValue(value);
    }

    @Test
    void testRemoveValue_NotFound() {
        // Given
        String value = "non-existent-value";
        when(bidiMap.removeValue(value)).thenReturn(null);

        // When
        String removedKey = bidiMap.removeValue(value);

        // Then
        assertNull(removedKey);
        verify(bidiMap, times(1)).removeValue(value);
    }

    @Test
    void testValues() {
        // Given
        Set<String> expectedValues = Set.of("value1", "value2");
        when(bidiMap.values()).thenReturn(expectedValues);

        // When
        Set<String> values = bidiMap.values();

        // Then
        assertEquals(expectedValues, values);
        verify(bidiMap, times(1)).values();
    }
}