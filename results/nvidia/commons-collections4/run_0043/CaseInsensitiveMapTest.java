import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CaseInsensitiveMapTest {

    @InjectMocks
    private CaseInsensitiveMap<String, String> caseInsensitiveMap;

    @BeforeEach
    void setup() {
        caseInsensitiveMap = new CaseInsensitiveMap<>();
    }

    @Test
    public void testClone() {
        // Given
        caseInsensitiveMap.put("One", "One");
        caseInsensitiveMap.put("Two", "Two");

        // When
        CaseInsensitiveMap<String, String> clonedMap = caseInsensitiveMap.clone();

        // Then
        assertNotNull(clonedMap);
        assertEquals(2, clonedMap.size());
        assertEquals("One", clonedMap.get("one"));
        assertEquals("Two", clonedMap.get("two"));
    }

    @Test
    public void testClone_EmptyMap() {
        // When
        CaseInsensitiveMap<String, String> clonedMap = caseInsensitiveMap.clone();

        // Then
        assertNotNull(clonedMap);
        assertTrue(clonedMap.isEmpty());
    }

    @Test
    public void testPutAndGet_KeyIsConvertedToLowerCase() {
        // Given
        caseInsensitiveMap.put("One", "One");

        // When
        String value = caseInsensitiveMap.get("ONE");

        // Then
        assertEquals("One", value);
    }

    @Test
    public void testPutAndGet_KeyIsNull() {
        // Given
        caseInsensitiveMap.put(null, "NullValue");

        // When
        String value = caseInsensitiveMap.get(null);

        // Then
        assertEquals("NullValue", value);
    }

    @Test
    public void testPutAndGet_MultipleKeysWithSameLowerCaseValue() {
        // Given
        caseInsensitiveMap.put("One", "One");
        caseInsensitiveMap.put("ONE", "NewValue");

        // When
        String value = caseInsensitiveMap.get("one");

        // Then
        assertEquals("NewValue", value);
    }

    @Test
    public void testPutAndGet_MultipleKeysWithDifferentLowerCaseValues() {
        // Given
        caseInsensitiveMap.put("One", "One");
        caseInsensitiveMap.put("Two", "Two");

        // When
        String value1 = caseInsensitiveMap.get("one");
        String value2 = caseInsensitiveMap.get("two");

        // Then
        assertEquals("One", value1);
        assertEquals("Two", value2);
    }

    @Test
    public void testConstructor_Default() {
        // When
        CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();

        // Then
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testConstructor_InitialCapacity() {
        // When
        CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>(10);

        // Then
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testConstructor_InitialCapacityAndLoadFactor() {
        // When
        CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>(10, 0.5f);

        // Then
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testConstructor_Map() {
        // Given
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("One", "One");
        originalMap.put("Two", "Two");

        // When
        CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>(originalMap);

        // Then
        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals("One", map.get("one"));
        assertEquals("Two", map.get("two"));
    }
}