import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DualHashBidiMapTest {

    @Mock
    private Map<String, String> normalMap;

    @Mock
    private Map<String, String> reverseMap;

    @Mock
    private DualHashBidiMap<String, String> inverseBidiMap;

    private DualHashBidiMap<String, String> dualHashBidiMap;

    @BeforeEach
    void setup() {
        dualHashBidiMap = new DualHashBidiMap<>();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(normalMap, reverseMap, inverseBidiMap);
    }

    @Test
    void testConstructor() {
        // Given
        DualHashBidiMap<String, String> dualHashBidiMap = new DualHashBidiMap<>();

        // Then
        assertNotNull(dualHashBidiMap);
    }

    @Test
    void testConstructorWithMap() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        DualHashBidiMap<String, String> dualHashBidiMap = new DualHashBidiMap<>(map);

        // Then
        assertNotNull(dualHashBidiMap);
        assertEquals(2, dualHashBidiMap.size());
    }

    @Test
    void testCreateBidiMap() {
        // Given
        Map<String, String> normalMap = new HashMap<>();
        Map<String, String> reverseMap = new HashMap<>();
        DualHashBidiMap<String, String> inverseBidiMap = new DualHashBidiMap<>();

        // When
        BidiMap<String, String> bidiMap = dualHashBidiMap.createBidiMap(normalMap, reverseMap, inverseBidiMap);

        // Then
        assertNotNull(bidiMap);
    }

    @Test
    void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When
        dualHashBidiMap.put(key, value);

        // Then
        assertEquals(value, dualHashBidiMap.get(key));
    }

    @Test
    void testRemoveValue() {
        // Given
        String key = "key";
        String value = "value";
        dualHashBidiMap.put(key, value);

        // When
        dualHashBidiMap.removeValue(value);

        // Then
        assertNull(dualHashBidiMap.get(key));
    }

    @Test
    void testValues() {
        // Given
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";
        dualHashBidiMap.put(key1, value1);
        dualHashBidiMap.put(key2, value2);

        // When
        assertEquals(2, dualHashBidiMap.values().size());
    }
}