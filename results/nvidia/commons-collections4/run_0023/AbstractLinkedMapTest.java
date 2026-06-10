import org.apache.commons.collections4.map.AbstractLinkedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractLinkedMapTest {

    @Mock
    private AbstractLinkedMap<String, String> abstractLinkedMap;

    @BeforeEach
    public void setup() {
        abstractLinkedMap = new AbstractLinkedMap<String, String>() {
            @Override
            protected void init() {
                super.init();
            }
        };
    }

    @Test
    public void testClear() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        abstractLinkedMap.clear();

        // Then
        assertTrue(abstractLinkedMap.isEmpty());
    }

    @Test
    public void testContainsValue() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        boolean result = abstractLinkedMap.containsValue("value1");

        // Then
        assertTrue(result);
    }

    @Test
    public void testFirstKey() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.firstKey();

        // Then
        assertEquals("key1", result);
    }

    @Test
    public void testLastKey() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.lastKey();

        // Then
        assertEquals("key2", result);
    }

    @Test
    public void testMapIterator() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        OrderedMapIterator<String, String> result = abstractLinkedMap.mapIterator();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testNextKey() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.nextKey("key1");

        // Then
        assertEquals("key2", result);
    }

    @Test
    public void testPreviousKey() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.previousKey("key2");

        // Then
        assertEquals("key1", result);
    }

    @Test
    public void testHasNext() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        boolean result = abstractLinkedMap.hasNext();

        // Then
        assertTrue(result);
    }

    @Test
    public void testHasPrevious() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        boolean result = abstractLinkedMap.hasPrevious();

        // Then
        assertTrue(result);
    }

    @Test
    public void testNext() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.next();

        // Then
        assertEquals("key1", result);
    }

    @Test
    public void testPrevious() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.previous();

        // Then
        assertEquals("key2", result);
    }

    @Test
    public void testRemove() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        abstractLinkedMap.remove();

        // Then
        assertTrue(abstractLinkedMap.isEmpty());
    }

    @Test
    public void testReset() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        abstractLinkedMap.reset();

        // Then
        assertTrue(abstractLinkedMap.isEmpty());
    }

    @Test
    public void testToString() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testGetKey() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.getKey();

        // Then
        assertEquals("key1", result);
    }

    @Test
    public void testGetValue() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.getValue();

        // Then
        assertEquals("value1", result);
    }

    @Test
    public void testSetValue() {
        // Given
        abstractLinkedMap.put("key1", "value1");
        abstractLinkedMap.put("key2", "value2");

        // When
        String result = abstractLinkedMap.setValue("newValue");

        // Then
        assertEquals("value1", result);
    }
}