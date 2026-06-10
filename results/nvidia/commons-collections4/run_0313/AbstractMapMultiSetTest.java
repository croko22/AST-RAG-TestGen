import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.AbstractMapMultiSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractMapMultiSetTest {

    @Mock
    private Map<String, AbstractMapMultiSet.MutableInteger> map;

    private AbstractMapMultiSet<String> multiSet;

    @BeforeEach
    void setup() {
        multiSet = new AbstractMapMultiSet<>(map);
    }

    @Test
    void testAdd() {
        // Given
        String object = "Test Object";
        int occurrences = 5;
        when(map.get(object)).thenReturn(null);

        // When
        int oldCount = multiSet.add(object, occurrences);

        // Then
        assertEquals(0, oldCount);
        verify(map).put(object, new AbstractMapMultiSet.MutableInteger(occurrences));
    }

    @Test
    void testAdd_NegativeOccurrences() {
        // Given
        String object = "Test Object";
        int occurrences = -1;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> multiSet.add(object, occurrences));
    }

    @Test
    void testClear() {
        // When
        multiSet.clear();

        // Then
        verify(map).clear();
    }

    @Test
    void testContains() {
        // Given
        String object = "Test Object";
        when(map.containsKey(object)).thenReturn(true);

        // When
        boolean result = multiSet.contains(object);

        // Then
        assertTrue(result);
    }

    @Test
    void testContains_NotPresent() {
        // Given
        String object = "Test Object";
        when(map.containsKey(object)).thenReturn(false);

        // When
        boolean result = multiSet.contains(object);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetCount() {
        // Given
        String object = "Test Object";
        AbstractMapMultiSet.MutableInteger count = new AbstractMapMultiSet.MutableInteger(5);
        when(map.get(object)).thenReturn(count);

        // When
        int result = multiSet.getCount(object);

        // Then
        assertEquals(5, result);
    }

    @Test
    void testGetCount_NotPresent() {
        // Given
        String object = "Test Object";
        when(map.get(object)).thenReturn(null);

        // When
        int result = multiSet.getCount(object);

        // Then
        assertEquals(0, result);
    }

    @Test
    void testIsEmpty() {
        // Given
        when(map.isEmpty()).thenReturn(true);

        // When
        boolean result = multiSet.isEmpty();

        // Then
        assertTrue(result);
    }

    @Test
    void testIsEmpty_NotEmpty() {
        // Given
        when(map.isEmpty()).thenReturn(false);

        // When
        boolean result = multiSet.isEmpty();

        // Then
        assertFalse(result);
    }

    @Test
    void testRemove() {
        // Given
        String object = "Test Object";
        AbstractMapMultiSet.MutableInteger count = new AbstractMapMultiSet.MutableInteger(5);
        when(map.get(object)).thenReturn(count);

        // When
        int oldCount = multiSet.remove(object, 3);

        // Then
        assertEquals(5, oldCount);
        assertEquals(2, count.value);
    }

    @Test
    void testRemove_NegativeOccurrences() {
        // Given
        String object = "Test Object";
        int occurrences = -1;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> multiSet.remove(object, occurrences));
    }

    @Test
    void testRemove_AllOccurrences() {
        // Given
        String object = "Test Object";
        AbstractMapMultiSet.MutableInteger count = new AbstractMapMultiSet.MutableInteger(5);
        when(map.get(object)).thenReturn(count);

        // When
        int oldCount = multiSet.remove(object, 5);

        // Then
        assertEquals(5, oldCount);
        verify(map).remove(object);
    }

    @Test
    void testSize() {
        // Given
        when(map.size()).thenReturn(10);

        // When
        int result = multiSet.size();

        // Then
        assertEquals(10, result);
    }

    @Test
    void testToArray() {
        // Given
        String object1 = "Test Object 1";
        String object2 = "Test Object 2";
        AbstractMapMultiSet.MutableInteger count1 = new AbstractMapMultiSet.MutableInteger(2);
        AbstractMapMultiSet.MutableInteger count2 = new AbstractMapMultiSet.MutableInteger(3);
        when(map.entrySet()).thenReturn(new HashMap<>() {{
            put(object1, count1);
            put(object2, count2);
        }}.entrySet());

        // When
        Object[] result = multiSet.toArray();

        // Then
        assertEquals(5, result.length);
        assertEquals(object1, result[0]);
        assertEquals(object1, result[1]);
        assertEquals(object2, result[2]);
        assertEquals(object2, result[3]);
        assertEquals(object2, result[4]);
    }
}