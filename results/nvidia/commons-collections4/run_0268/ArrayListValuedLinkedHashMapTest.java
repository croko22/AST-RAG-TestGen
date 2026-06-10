import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedLinkedHashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArrayListValuedLinkedHashMapTest {

    @Mock
    private Map<String, String> map;

    private ArrayListValuedLinkedHashMap<String, String> arrayListValuedLinkedHashMap;

    @BeforeEach
    void setup() {
        arrayListValuedLinkedHashMap = new ArrayListValuedLinkedHashMap<>();
    }

    @AfterEach
    void tearDown() {
        arrayListValuedLinkedHashMap = null;
    }

    @Test
    void testTrimToSize() {
        // Given
        arrayListValuedLinkedHashMap.put("key1", "value1");
        arrayListValuedLinkedHashMap.put("key1", "value2");

        // When
        arrayListValuedLinkedHashMap.trimToSize();

        // Then
        assertEquals(2, arrayListValuedLinkedHashMap.get("key1").size());
    }

    @Test
    void testTrimToSizeEmptyMap() {
        // Given
        assertTrue(arrayListValuedLinkedHashMap.isEmpty());

        // When
        arrayListValuedLinkedHashMap.trimToSize();

        // Then
        assertTrue(arrayListValuedLinkedHashMap.isEmpty());
    }

    @Test
    void testTrimToSizeNullKey() {
        // Given
        assertThrows(NullPointerException.class, () -> arrayListValuedLinkedHashMap.get(null));

        // When and Then
        assertThrows(NullPointerException.class, () -> arrayListValuedLinkedHashMap.trimToSize());
    }

    @Test
    void testConstructorDefault() {
        // Given
        ArrayListValuedLinkedHashMap<String, String> map = new ArrayListValuedLinkedHashMap<>();

        // When and Then
        assertNotNull(map);
    }

    @Test
    void testConstructorInitialListCapacity() {
        // Given
        ArrayListValuedLinkedHashMap<String, String> map = new ArrayListValuedLinkedHashMap<>(10);

        // When and Then
        assertNotNull(map);
    }

    @Test
    void testConstructorInitialMapAndListCapacity() {
        // Given
        ArrayListValuedLinkedHashMap<String, String> map = new ArrayListValuedLinkedHashMap<>(10, 10);

        // When and Then
        assertNotNull(map);
    }

    @Test
    void testConstructorMap() {
        // Given
        Map<String, String> testMap = new LinkedHashMap<>();
        testMap.put("key1", "value1");
        ArrayListValuedLinkedHashMap<String, String> map = new ArrayListValuedLinkedHashMap<>(testMap);

        // When and Then
        assertNotNull(map);
        assertEquals(1, map.size());
    }

    @Test
    void testConstructorMultiValuedMap() {
        // Given
        MultiValuedMap<String, String> testMap = new ArrayListValuedLinkedHashMap<>();
        testMap.put("key1", "value1");
        ArrayListValuedLinkedHashMap<String, String> map = new ArrayListValuedLinkedHashMap<>(testMap);

        // When and Then
        assertNotNull(map);
        assertEquals(1, map.size());
    }
}