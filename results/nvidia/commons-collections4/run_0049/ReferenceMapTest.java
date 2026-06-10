import org.apache.commons.collections4.map.ReferenceMap;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReferenceMapTest {

    @InjectMocks
    private ReferenceMap<String, String> referenceMap;

    @Mock
    private ObjectInputStream objectInputStream;

    @Mock
    private ObjectOutputStream objectOutputStream;

    @BeforeEach
    public void setup() {
        referenceMap = new ReferenceMap<>();
    }

    @Test
    public void testReferenceMapConstructor() {
        // Given
        ReferenceMap<String, String> map = new ReferenceMap<>();

        // Then
        assertNotNull(map);
    }

    @Test
    public void testReferenceMapConstructorWithKeyAndValueType() {
        // Given
        ReferenceMap<String, String> map = new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.HARD, AbstractReferenceMap.ReferenceStrength.SOFT);

        // Then
        assertNotNull(map);
    }

    @Test
    public void testReferenceMapConstructorWithKeyAndValueTypeAndPurgeValues() {
        // Given
        ReferenceMap<String, String> map = new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.HARD, AbstractReferenceMap.ReferenceStrength.SOFT, true);

        // Then
        assertNotNull(map);
    }

    @Test
    public void testReferenceMapConstructorWithKeyAndValueTypeAndCapacityAndLoadFactor() {
        // Given
        ReferenceMap<String, String> map = new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.HARD, AbstractReferenceMap.ReferenceStrength.SOFT, 10, 0.5f);

        // Then
        assertNotNull(map);
    }

    @Test
    public void testReferenceMapConstructorWithKeyAndValueTypeAndCapacityAndLoadFactorAndPurgeValues() {
        // Given
        ReferenceMap<String, String> map = new ReferenceMap<>(AbstractReferenceMap.ReferenceStrength.HARD, AbstractReferenceMap.ReferenceStrength.SOFT, 10, 0.5f, true);

        // Then
        assertNotNull(map);
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        // Given
        when(objectInputStream.defaultReadObject()).thenReturn(null);

        // When
        try {
            referenceMap.readObject(objectInputStream);
        } catch (IOException | ClassNotFoundException e) {
            // Then
            fail("Unexpected exception");
        }
    }

    @Test
    public void testWriteObject() throws IOException {
        // Given
        doNothing().when(objectOutputStream).defaultWriteObject();

        // When
        try {
            referenceMap.writeObject(objectOutputStream);
        } catch (IOException e) {
            // Then
            fail("Unexpected exception");
        }
    }

    @Test
    public void testPut() {
        // Given
        String key = "key";
        String value = "value";

        // When
        referenceMap.put(key, value);

        // Then
        assertNotNull(referenceMap.get(key));
        assertEquals(value, referenceMap.get(key));
    }

    @Test
    public void testPutAll() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // When
        referenceMap.putAll(map);

        // Then
        assertNotNull(referenceMap.get("key1"));
        assertEquals("value1", referenceMap.get("key1"));
        assertNotNull(referenceMap.get("key2"));
        assertEquals("value2", referenceMap.get("key2"));
    }

    @Test
    public void testGet() {
        // Given
        String key = "key";
        String value = "value";
        referenceMap.put(key, value);

        // When
        String result = referenceMap.get(key);

        // Then
        assertNotNull(result);
        assertEquals(value, result);
    }

    @Test
    public void testRemove() {
        // Given
        String key = "key";
        String value = "value";
        referenceMap.put(key, value);

        // When
        referenceMap.remove(key);

        // Then
        assertNull(referenceMap.get(key));
    }

    @Test
    public void testContainsKey() {
        // Given
        String key = "key";
        String value = "value";
        referenceMap.put(key, value);

        // When
        boolean result = referenceMap.containsKey(key);

        // Then
        assertTrue(result);
    }

    @Test
    public void testContainsValue() {
        // Given
        String key = "key";
        String value = "value";
        referenceMap.put(key, value);

        // When
        boolean result = referenceMap.containsValue(value);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsEmpty() {
        // Given

        // When
        boolean result = referenceMap.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    public void testSize() {
        // Given
        String key = "key";
        String value = "value";
        referenceMap.put(key, value);

        // When
        int result = referenceMap.size();

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testClear() {
        // Given
        String key = "key";
        String value = "value";
        referenceMap.put(key, value);

        // When
        referenceMap.clear();

        // Then
        assertTrue(referenceMap.isEmpty());
    }
}