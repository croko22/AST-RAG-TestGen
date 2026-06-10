import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.collections4.multimap.TransformedMultiValuedMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MultiMapUtilsTest {

    @Mock
    private MultiValuedMap<String, String> multiValuedMap;

    @BeforeEach
    public void setup() {
        // Initialize mock objects if needed
    }

    @Test
    public void testEmptyIfNull_NullMap_ReturnsEmptyMap() {
        // Given
        MultiValuedMap<String, String> map = null;

        // When
        MultiValuedMap<String, String> result = MultiMapUtils.emptyIfNull(map);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEmptyIfNull_NonNullMap_ReturnsOriginalMap() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();

        // When
        MultiValuedMap<String, String> result = MultiMapUtils.emptyIfNull(map);

        // Then
        assertSame(map, result);
    }

    @Test
    public void testEmptyMultiValuedMap_ReturnsEmptyMap() {
        // When
        MultiValuedMap<String, String> result = MultiMapUtils.emptyMultiValuedMap();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCollection_NullMap_ReturnsNull() {
        // Given
        MultiValuedMap<String, String> map = null;
        String key = "key";

        // When
        Collection<String> result = MultiMapUtils.getCollection(map, key);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetCollection_NonNullMap_ReturnsCollection() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
        String key = "key";
        map.put(key, "value");

        // When
        Collection<String> result = MultiMapUtils.getCollection(map, key);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetValuesAsBag_NullMap_ReturnsNull() {
        // Given
        MultiValuedMap<String, String> map = null;
        String key = "key";

        // When
        HashBag<String> result = (HashBag<String>) MultiMapUtils.getValuesAsBag(map, key);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetValuesAsBag_NonNullMap_ReturnsBag() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
        String key = "key";
        map.put(key, "value");

        // When
        HashBag<String> result = (HashBag<String>) MultiMapUtils.getValuesAsBag(map, key);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetValuesAsList_NullMap_ReturnsNull() {
        // Given
        MultiValuedMap<String, String> map = null;
        String key = "key";

        // When
        List<String> result = (List<String>) MultiMapUtils.getValuesAsList(map, key);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetValuesAsList_NonNullMap_ReturnsList() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
        String key = "key";
        map.put(key, "value");

        // When
        List<String> result = (List<String>) MultiMapUtils.getValuesAsList(map, key);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetValuesAsSet_NullMap_ReturnsNull() {
        // Given
        MultiValuedMap<String, String> map = null;
        String key = "key";

        // When
        Set<String> result = (Set<String>) MultiMapUtils.getValuesAsSet(map, key);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetValuesAsSet_NonNullMap_ReturnsSet() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
        String key = "key";
        map.put(key, "value");

        // When
        Set<String> result = (Set<String>) MultiMapUtils.getValuesAsSet(map, key);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsEmpty_NullMap_ReturnsTrue() {
        // Given
        MultiValuedMap<String, String> map = null;

        // When
        boolean result = MultiMapUtils.isEmpty(map);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_NonNullEmptyMap_ReturnsTrue() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();

        // When
        boolean result = MultiMapUtils.isEmpty(map);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_NonNullNonEmptyMap_ReturnsFalse() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
        map.put("key", "value");

        // When
        boolean result = MultiMapUtils.isEmpty(map);

        // Then
        assertFalse(result);
    }

    @Test
    public void testNewListValuedHashMap_ReturnsNewMap() {
        // When
        ListValuedMap<String, String> result = MultiMapUtils.newListValuedHashMap();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNewSetValuedHashMap_ReturnsNewMap() {
        // When
        SetValuedMap<String, String> result = MultiMapUtils.newSetValuedHashMap();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTransformedMultiValuedMap_NullMap_ThrowsException() {
        // Given
        MultiValuedMap<String, String> map = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> MultiMapUtils.transformedMultiValuedMap(map, null, null));
    }

    @Test
    public void testTransformedMultiValuedMap_NonNullMap_ReturnsTransformedMap() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();

        // When
        MultiValuedMap<String, String> result = MultiMapUtils.transformedMultiValuedMap(map, null, null);

        // Then
        assertNotNull(result);
        assertNotSame(map, result);
    }

    @Test
    public void testUnmodifiableMultiValuedMap_NullMap_ThrowsException() {
        // Given
        MultiValuedMap<String, String> map = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> MultiMapUtils.unmodifiableMultiValuedMap(map));
    }

    @Test
    public void testUnmodifiableMultiValuedMap_NonNullMap_ReturnsUnmodifiableMap() {
        // Given
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();

        // When
        MultiValuedMap<String, String> result = MultiMapUtils.unmodifiableMultiValuedMap(map);

        // Then
        assertNotNull(result);
        assertNotSame(map, result);
    }
}