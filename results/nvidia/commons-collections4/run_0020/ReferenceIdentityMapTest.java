import org.apache.commons.collections4.map.ReferenceIdentityMap;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReferenceIdentityMapTest {

    @InjectMocks
    private ReferenceIdentityMap<String, String> referenceIdentityMap;

    @Mock
    private Map<String, String> mockMap;

    @BeforeEach
    void setup() {
        referenceIdentityMap = new ReferenceIdentityMap<>();
    }

    @Test
    void testConstructor() {
        // Given
        ReferenceIdentityMap<String, String> map = new ReferenceIdentityMap<>();

        // Then
        assertNotNull(map);
    }

    @Test
    void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When
        referenceIdentityMap.put(key, value);

        // Then
        assertEquals(value, referenceIdentityMap.get(key));
    }

    @Test
    void testGet() {
        // Given
        String key = "key";
        String value = "value";
        referenceIdentityMap.put(key, value);

        // When
        String result = referenceIdentityMap.get(key);

        // Then
        assertEquals(value, result);
    }

    @Test
    void testRemove() {
        // Given
        String key = "key";
        String value = "value";
        referenceIdentityMap.put(key, value);

        // When
        referenceIdentityMap.remove(key);

        // Then
        assertNull(referenceIdentityMap.get(key));
    }

    @Test
    void testContainsKey() {
        // Given
        String key = "key";
        String value = "value";
        referenceIdentityMap.put(key, value);

        // When
        boolean result = referenceIdentityMap.containsKey(key);

        // Then
        assertTrue(result);
    }

    @Test
    void testContainsValue() {
        // Given
        String key = "key";
        String value = "value";
        referenceIdentityMap.put(key, value);

        // When
        boolean result = referenceIdentityMap.containsValue(value);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsEmpty() {
        // Given
        assertTrue(referenceIdentityMap.isEmpty());

        // When
        referenceIdentityMap.put("key", "value");

        // Then
        assertFalse(referenceIdentityMap.isEmpty());
    }

    @Test
    void testSize() {
        // Given
        assertEquals(0, referenceIdentityMap.size());

        // When
        referenceIdentityMap.put("key", "value");

        // Then
        assertEquals(1, referenceIdentityMap.size());
    }

    @Test
    void testClear() {
        // Given
        referenceIdentityMap.put("key", "value");

        // When
        referenceIdentityMap.clear();

        // Then
        assertTrue(referenceIdentityMap.isEmpty());
    }

    @Test
    void testSerialization() throws Exception {
        // Given
        String key = "key";
        String value = "value";
        referenceIdentityMap.put(key, value);

        // When
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(referenceIdentityMap);
        out.flush();
        byte[] bytes = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bis);
        ReferenceIdentityMap<String, String> deserializedMap = (ReferenceIdentityMap<String, String>) in.readObject();

        // Then
        assertEquals(value, deserializedMap.get(key));
    }
}