import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HashedMapTest {

    @Mock
    private Map<String, String> map;

    private HashedMap<String, String> hashedMap;

    @BeforeEach
    void setup() {
        hashedMap = new HashedMap<>();
    }

    @Test
    void testConstructor() {
        // Given: default constructor
        HashedMap<String, String> defaultHashedMap = new HashedMap<>();

        // Then: map is not null
        assertNotNull(defaultHashedMap);
    }

    @Test
    void testConstructorWithInitialCapacity() {
        // Given: initial capacity
        int initialCapacity = 10;
        HashedMap<String, String> hashedMapWithCapacity = new HashedMap<>(initialCapacity);

        // Then: map is not null
        assertNotNull(hashedMapWithCapacity);
    }

    @Test
    void testConstructorWithInitialCapacityAndLoadFactor() {
        // Given: initial capacity and load factor
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        HashedMap<String, String> hashedMapWithCapacityAndLoadFactor = new HashedMap<>(initialCapacity, loadFactor);

        // Then: map is not null
        assertNotNull(hashedMapWithCapacityAndLoadFactor);
    }

    @Test
    void testConstructorWithMap() {
        // Given: map to copy
        Map<String, String> mapToCopy = new HashMap<>();
        mapToCopy.put("key1", "value1");
        mapToCopy.put("key2", "value2");
        HashedMap<String, String> hashedMapWithMap = new HashedMap<>(mapToCopy);

        // Then: map is not null and contains all entries from the original map
        assertNotNull(hashedMapWithMap);
        assertEquals(2, hashedMapWithMap.size());
        assertTrue(hashedMapWithMap.containsKey("key1"));
        assertTrue(hashedMapWithMap.containsKey("key2"));
    }

    @Test
    void testClone() {
        // Given: map with some entries
        hashedMap.put("key1", "value1");
        hashedMap.put("key2", "value2");

        // When: clone the map
        HashedMap<String, String> clonedMap = hashedMap.clone();

        // Then: cloned map is not null and contains all entries from the original map
        assertNotNull(clonedMap);
        assertEquals(2, clonedMap.size());
        assertTrue(clonedMap.containsKey("key1"));
        assertTrue(clonedMap.containsKey("key2"));
    }

    @Test
    void testSerialization() throws Exception {
        // Given: map with some entries
        hashedMap.put("key1", "value1");
        hashedMap.put("key2", "value2");

        // When: serialize the map
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(hashedMap);
        oos.close();

        // Then: deserialize the map
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        HashedMap<String, String> deserializedMap = (HashedMap<String, String>) ois.readObject();
        ois.close();

        // Then: deserialized map is not null and contains all entries from the original map
        assertNotNull(deserializedMap);
        assertEquals(2, deserializedMap.size());
        assertTrue(deserializedMap.containsKey("key1"));
        assertTrue(deserializedMap.containsKey("key2"));
    }
}