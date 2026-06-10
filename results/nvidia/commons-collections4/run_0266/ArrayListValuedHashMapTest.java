import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArrayListValuedHashMapTest {

    @Mock
    private Map<String, String> map;

    private ArrayListValuedHashMap<String, String> arrayListValuedHashMap;

    @BeforeEach
    void setup() {
        arrayListValuedHashMap = new ArrayListValuedHashMap<>();
    }

    @Test
    void testTrimToSize() {
        // Given
        arrayListValuedHashMap.put("key1", "value1");
        arrayListValuedHashMap.put("key1", "value2");
        arrayListValuedHashMap.put("key2", "value3");

        // When
        arrayListValuedHashMap.trimToSize();

        // Then
        assertEquals(2, arrayListValuedHashMap.get("key1").size());
        assertEquals(1, arrayListValuedHashMap.get("key2").size());
    }

    @Test
    void testTrimToSize_EmptyMap() {
        // Given
        assertTrue(arrayListValuedHashMap.isEmpty());

        // When
        arrayListValuedHashMap.trimToSize();

        // Then
        assertTrue(arrayListValuedHashMap.isEmpty());
    }

    @Test
    void testConstructor_DefaultInitialMapCapacity() {
        // Given
        ArrayListValuedHashMap<String, String> arrayListValuedHashMap = new ArrayListValuedHashMap<>();

        // Then
        assertNotNull(arrayListValuedHashMap);
    }

    @Test
    void testConstructor_SpecifiedInitialListCapacity() {
        // Given
        int initialListCapacity = 10;
        ArrayListValuedHashMap<String, String> arrayListValuedHashMap = new ArrayListValuedHashMap<>(initialListCapacity);

        // Then
        assertNotNull(arrayListValuedHashMap);
    }

    @Test
    void testConstructor_SpecifiedInitialMapAndListCapacities() {
        // Given
        int initialMapCapacity = 20;
        int initialListCapacity = 10;
        ArrayListValuedHashMap<String, String> arrayListValuedHashMap = new ArrayListValuedHashMap<>(initialMapCapacity, initialListCapacity);

        // Then
        assertNotNull(arrayListValuedHashMap);
    }

    @Test
    void testConstructor_CopyingMap() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        ArrayListValuedHashMap<String, String> arrayListValuedHashMap = new ArrayListValuedHashMap<>(map);

        // Then
        assertEquals(2, arrayListValuedHashMap.size());
    }

    @Test
    void testConstructor_CopyingMultiValuedMap() {
        // Given
        ArrayListValuedHashMap<String, String> multiValuedMap = new ArrayListValuedHashMap<>();
        multiValuedMap.put("key1", "value1");
        multiValuedMap.put("key1", "value2");
        multiValuedMap.put("key2", "value3");
        ArrayListValuedHashMap<String, String> arrayListValuedHashMap = new ArrayListValuedHashMap<>(multiValuedMap);

        // Then
        assertEquals(2, arrayListValuedHashMap.size());
    }
}