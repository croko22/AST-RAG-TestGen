import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DualLinkedHashBidiMapTest {

    private DualLinkedHashBidiMap<String, String> bidiMap;

    @BeforeEach
    void setup() {
        bidiMap = new DualLinkedHashBidiMap<>();
    }

    @Test
    void testConstructor() {
        // Given: no initial map
        // When: creating a new instance
        DualLinkedHashBidiMap<String, String> newBidiMap = new DualLinkedHashBidiMap<>();
        // Then: the instance is not null
        assertNotNull(newBidiMap);
    }

    @Test
    void testConstructorWithMap() {
        // Given: an initial map
        Map<String, String> initialMap = new HashMap<>();
        initialMap.put("key1", "value1");
        initialMap.put("key2", "value2");
        // When: creating a new instance with the initial map
        DualLinkedHashBidiMap<String, String> newBidiMap = new DualLinkedHashBidiMap<>(initialMap);
        // Then: the instance contains the initial map entries
        assertEquals(initialMap, newBidiMap);
    }

    @Test
    void testPut() {
        // Given: a key-value pair
        String key = "key";
        String value = "value";
        // When: putting the key-value pair into the map
        bidiMap.put(key, value);
        // Then: the key-value pair is in the map
        assertEquals(value, bidiMap.put(key, value));
    }

    @Test
    void testRemoveValue() {
        // Given: a key-value pair in the map
        String key = "key";
        String value = "value";
        bidiMap.put(key, value);
        // When: removing the value from the map
        String removedKey = bidiMap.removeValue(value);
        // Then: the key is removed
        assertEquals(key, removedKey);
        assertNull(bidiMap.put(key, value));
    }

    @Test
    void testValues() {
        // Given: some key-value pairs in the map
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        String value2 = "value2";
        bidiMap.put(key1, value1);
        bidiMap.put(key2, value2);
        // When: getting the values from the map
        assertEquals(2, bidiMap.values().size());
    }

    @Test
    void testGetKey() {
        // Given: a key-value pair in the map
        String key = "key";
        String value = "value";
        bidiMap.put(key, value);
        // When: getting the key for the value
        String retrievedKey = ((BidiMap<String, String>) bidiMap).inverseBidiMap().getKey(value);
        // Then: the key is retrieved
        assertEquals(key, retrievedKey);
    }

    @Test
    void testInverseBidiMap() {
        // Given: a key-value pair in the map
        String key = "key";
        String value = "value";
        bidiMap.put(key, value);
        // When: getting the inverse bidi map
        BidiMap<String, String> inverseMap = ((BidiMap<String, String>) bidiMap).inverseBidiMap();
        // Then: the inverse map contains the reversed key-value pair
        assertEquals(key, inverseMap.put(value, key));
    }

    @Test
    void testCreateBidiMap() {
        // Given: some maps
        Map<String, String> normalMap = new HashMap<>();
        Map<String, String> reverseMap = new HashMap<>();
        BidiMap<String, String> inverseBidiMap = new DualLinkedHashBidiMap<>();
        // When: creating a new bidi map
        BidiMap<String, String> newBidiMap = bidiMap.createBidiMap(normalMap, reverseMap, inverseBidiMap);
        // Then: the new bidi map is not null
        assertNotNull(newBidiMap);
    }

    @Test
    void testReadObject() throws Exception {
        // Given: an object input stream
        ObjectInputStream in = new ObjectInputStream(new java.io.ByteArrayInputStream(new byte[0]));
        // When: reading an object from the stream
        DualLinkedHashBidiMap<String, String> newBidiMap = new DualLinkedHashBidiMap<>();
        newBidiMap.readObject(in);
        // Then: the object is not null
        assertNotNull(newBidiMap);
    }

    @Test
    void testWriteObject() throws Exception {
        // Given: an object output stream
        ObjectOutputStream out = new ObjectOutputStream(new java.io.ByteArrayOutputStream());
        // When: writing an object to the stream
        bidiMap.writeObject(out);
        // Then: no exception is thrown
    }
}