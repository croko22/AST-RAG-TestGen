import org.apache.commons.collections4.multimap.AbstractSetValuedMap;
import org.apache.commons.collections4.SetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractSetValuedMapTest {

    @Mock
    private Map<String, Set<String>> map;

    @InjectMocks
    private AbstractSetValuedMap<String, String> abstractSetValuedMap;

    @BeforeEach
    void setup() {
        abstractSetValuedMap = new AbstractSetValuedMap<String, String>(map) {
            @Override
            protected Set<String> createCollection() {
                return new HashSet<>();
            }
        };
    }

    @Test
    void testEquals_SameInstance_ReturnsTrue() {
        // When
        boolean result = abstractSetValuedMap.equals(abstractSetValuedMap);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentInstance_SameValues_ReturnsTrue() {
        // Given
        AbstractSetValuedMap<String, String> otherMap = new AbstractSetValuedMap<String, String>(map) {
            @Override
            protected Set<String> createCollection() {
                return new HashSet<>();
            }
        };

        // When
        boolean result = abstractSetValuedMap.equals(otherMap);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentInstance_DifferentValues_ReturnsFalse() {
        // Given
        Map<String, Set<String>> otherMapInstance = mock(Map.class);
        AbstractSetValuedMap<String, String> otherMap = new AbstractSetValuedMap<String, String>(otherMapInstance) {
            @Override
            protected Set<String> createCollection() {
                return new HashSet<>();
            }
        };

        // When
        boolean result = abstractSetValuedMap.equals(otherMap);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_NullInstance_ReturnsFalse() {
        // When
        boolean result = abstractSetValuedMap.equals(null);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_DifferentClassInstance_ReturnsFalse() {
        // Given
        Object otherInstance = new Object();

        // When
        boolean result = abstractSetValuedMap.equals(otherInstance);

        // Then
        assertFalse(result);
    }

    @Test
    void testHashCode_SameInstance_ReturnsSameHashCode() {
        // When
        int hashCode1 = abstractSetValuedMap.hashCode();
        int hashCode2 = abstractSetValuedMap.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testGet_KeyExists_ReturnsSetValue() {
        // Given
        String key = "key";
        Set<String> value = new HashSet<>();
        value.add("value");
        when(map.get(key)).thenReturn(value);

        // When
        Set<String> result = abstractSetValuedMap.get(key);

        // Then
        assertNotNull(result);
        assertEquals(value, result);
    }

    @Test
    void testGet_KeyDoesNotExist_ReturnsEmptySet() {
        // Given
        String key = "key";
        when(map.get(key)).thenReturn(null);

        // When
        Set<String> result = abstractSetValuedMap.get(key);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testRemove_KeyExists_ReturnsRemovedValue() {
        // Given
        String key = "key";
        Set<String> value = new HashSet<>();
        value.add("value");
        when(map.get(key)).thenReturn(value);
        when(map.remove(key)).thenReturn(value);

        // When
        Set<String> result = abstractSetValuedMap.remove(key);

        // Then
        assertNotNull(result);
        assertEquals(value, result);
    }

    @Test
    void testRemove_KeyDoesNotExist_ReturnsEmptySet() {
        // Given
        String key = "key";
        when(map.get(key)).thenReturn(null);
        when(map.remove(key)).thenReturn(null);

        // When
        Set<String> result = abstractSetValuedMap.remove(key);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}