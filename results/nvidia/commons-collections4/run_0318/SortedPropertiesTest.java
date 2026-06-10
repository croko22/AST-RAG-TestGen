import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.apache.commons.collections4.properties.SortedProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SortedPropertiesTest {

    @InjectMocks
    private SortedProperties sortedProperties;

    @BeforeEach
    void setup() {
        sortedProperties = new SortedProperties();
    }

    @AfterEach
    void tearDown() {
        sortedProperties = null;
    }

    @Test
    public void testEntrySet() {
        // Given
        sortedProperties.put("key1", "value1");
        sortedProperties.put("key3", "value3");
        sortedProperties.put("key2", "value2");

        // When
        Set<Map.Entry<Object, Object>> entrySet = sortedProperties.entrySet();

        // Then
        assertNotNull(entrySet);
        assertEquals(3, entrySet.size());
        List<String> keys = entrySet.stream().map(Map.Entry::getKey).map(Object::toString).collect(Collectors.toList());
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
        assertTrue(keys.contains("key3"));
        List<String> sortedKeys = keys.stream().sorted().collect(Collectors.toList());
        assertEquals(sortedKeys, keys);
    }

    @Test
    public void testKeys() {
        // Given
        sortedProperties.put("key1", "value1");
        sortedProperties.put("key3", "value3");
        sortedProperties.put("key2", "value2");

        // When
        Enumeration<Object> keys = sortedProperties.keys();

        // Then
        assertNotNull(keys);
        List<String> keyList = new ArrayList<>();
        while (keys.hasMoreElements()) {
            keyList.add((String) keys.nextElement());
        }
        assertEquals(3, keyList.size());
        List<String> sortedKeyList = keyList.stream().sorted().collect(Collectors.toList());
        assertEquals(sortedKeyList, keyList);
    }

    @Test
    public void testEntrySet_Empty() {
        // Given
        SortedProperties emptySortedProperties = new SortedProperties();

        // When
        Set<Map.Entry<Object, Object>> entrySet = emptySortedProperties.entrySet();

        // Then
        assertNotNull(entrySet);
        assertTrue(entrySet.isEmpty());
    }

    @Test
    public void testKeys_Empty() {
        // Given
        SortedProperties emptySortedProperties = new SortedProperties();

        // When
        Enumeration<Object> keys = emptySortedProperties.keys();

        // Then
        assertNotNull(keys);
        assertFalse(keys.hasMoreElements());
    }

    @Test
    public void testEntrySet_DuplicateKeys() {
        // Given
        sortedProperties.put("key1", "value1");
        sortedProperties.put("key1", "value2");

        // When
        Set<Map.Entry<Object, Object>> entrySet = sortedProperties.entrySet();

        // Then
        assertNotNull(entrySet);
        assertEquals(1, entrySet.size());
        List<String> keys = entrySet.stream().map(Map.Entry::getKey).map(Object::toString).collect(Collectors.toList());
        assertTrue(keys.contains("key1"));
    }

    @Test
    public void testKeys_DuplicateKeys() {
        // Given
        sortedProperties.put("key1", "value1");
        sortedProperties.put("key1", "value2");

        // When
        Enumeration<Object> keys = sortedProperties.keys();

        // Then
        assertNotNull(keys);
        List<String> keyList = new ArrayList<>();
        while (keys.hasMoreElements()) {
            keyList.add((String) keys.nextElement());
        }
        assertEquals(1, keyList.size());
        assertTrue(keyList.contains("key1"));
    }
}