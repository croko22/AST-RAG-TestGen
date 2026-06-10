import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.iterators.EntrySetMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntrySetMapIteratorTest {

    @Mock
    private Map<String, String> map;

    private EntrySetMapIterator<String, String> iterator;

    @BeforeEach
    public void setup() {
        iterator = new EntrySetMapIterator<>(map);
    }

    @Test
    public void testGetKey_ThrowsExceptionWhenNotCalledAfterNext() {
        // Given: iterator not called after next()
        // When / Then: getKey() throws exception
        assertThrows(IllegalStateException.class, () -> iterator.getKey());
    }

    @Test
    public void testGetValue_ThrowsExceptionWhenNotCalledAfterNext() {
        // Given: iterator not called after next()
        // When / Then: getValue() throws exception
        assertThrows(IllegalStateException.class, () -> iterator.getValue());
    }

    @Test
    public void testHasNext_ReturnsTrueWhenMapHasEntries() {
        // Given: map has entries
        when(map.entrySet()).thenReturn(new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.entrySet());
        // When: hasNext() is called
        boolean result = iterator.hasNext();
        // Then: returns true
        assertTrue(result);
    }

    @Test
    public void testHasNext_ReturnsFalseWhenMapIsEmpty() {
        // Given: map is empty
        when(map.entrySet()).thenReturn(new HashMap<String, String>().entrySet());
        // When: hasNext() is called
        boolean result = iterator.hasNext();
        // Then: returns false
        assertFalse(result);
    }

    @Test
    public void testNext_ReturnsNextKey() {
        // Given: map has entries
        when(map.entrySet()).thenReturn(new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.entrySet());
        // When: next() is called
        String result = iterator.next();
        // Then: returns next key
        assertEquals("key1", result);
    }

    @Test
    public void testNext_ThrowsExceptionWhenNoMoreElements() {
        // Given: map is empty
        when(map.entrySet()).thenReturn(new HashMap<String, String>().entrySet());
        // When / Then: next() throws exception
        assertThrows(java.util.NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testRemove_ThrowsExceptionWhenNotCalledAfterNext() {
        // Given: iterator not called after next()
        // When / Then: remove() throws exception
        assertThrows(IllegalStateException.class, () -> iterator.remove());
    }

    @Test
    public void testRemove_RemovesLastReturnedKey() {
        // Given: map has entries
        when(map.entrySet()).thenReturn(new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.entrySet());
        // When: next() and remove() are called
        iterator.next();
        iterator.remove();
        // Then: last returned key is removed
        verify(map).remove("key1");
    }

    @Test
    public void testReset_ResetsIterator() {
        // Given: iterator has been used
        when(map.entrySet()).thenReturn(new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.entrySet());
        iterator.next();
        // When: reset() is called
        iterator.reset();
        // Then: iterator is reset
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testSetValue_SetsValueForCurrentKey() {
        // Given: map has entries
        when(map.entrySet()).thenReturn(new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.entrySet());
        // When: next() and setValue() are called
        iterator.next();
        String result = iterator.setValue("newValue");
        // Then: value is set for current key
        assertEquals("value1", result);
        verify(map).put("key1", "newValue");
    }

    @Test
    public void testSetValue_ThrowsExceptionWhenNotCalledAfterNext() {
        // Given: iterator not called after next()
        // When / Then: setValue() throws exception
        assertThrows(IllegalStateException.class, () -> iterator.setValue("newValue"));
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        // Given: iterator has been used
        when(map.entrySet()).thenReturn(new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
        }}.entrySet());
        iterator.next();
        // When: toString() is called
        String result = iterator.toString();
        // Then: returns string representation
        assertEquals("MapIterator[key1=value1]", result);
    }
}