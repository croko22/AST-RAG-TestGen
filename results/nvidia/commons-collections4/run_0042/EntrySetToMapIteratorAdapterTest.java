import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.map.EntrySetToMapIteratorAdapter;
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
public class EntrySetToMapIteratorAdapterTest {

    @Mock
    private Set<Map.Entry<String, String>> entrySet;

    @Mock
    private Iterator<Map.Entry<String, String>> iterator;

    @Mock
    private Map.Entry<String, String> entry;

    private EntrySetToMapIteratorAdapter<String, String> adapter;

    @BeforeEach
    public void setup() {
        when(entrySet.iterator()).thenReturn(iterator);
        adapter = new EntrySetToMapIteratorAdapter<>(entrySet);
    }

    @Test
    public void testConstructor() {
        // Given: entrySet is not null
        // When: creating a new EntrySetToMapIteratorAdapter
        // Then: iterator is initialized
        verify(entrySet, times(1)).iterator();
    }

    @Test
    public void testGetKey() {
        // Given: entry is not null
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(entry);
        adapter.next();
        // When: calling getKey
        // Then: key is returned
        String key = adapter.getKey();
        verify(entry, times(1)).getKey();
        assertEquals(entry.getKey(), key);
    }

    @Test
    public void testGetValue() {
        // Given: entry is not null
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(entry);
        adapter.next();
        // When: calling getValue
        // Then: value is returned
        String value = adapter.getValue();
        verify(entry, times(1)).getValue();
        assertEquals(entry.getValue(), value);
    }

    @Test
    public void testHasNext() {
        // Given: iterator has next element
        when(iterator.hasNext()).thenReturn(true);
        // When: calling hasNext
        // Then: true is returned
        boolean hasNext = adapter.hasNext();
        verify(iterator, times(1)).hasNext();
        assertTrue(hasNext);
    }

    @Test
    public void testNext() {
        // Given: iterator has next element
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(entry);
        // When: calling next
        // Then: key is returned
        String key = adapter.next();
        verify(iterator, times(1)).next();
        assertEquals(entry.getKey(), key);
    }

    @Test
    public void testRemove() {
        // Given: iterator has next element
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(entry);
        adapter.next();
        // When: calling remove
        // Then: entry is removed
        adapter.remove();
        verify(iterator, times(1)).remove();
        assertNull(adapter.current());
    }

    @Test
    public void testReset() {
        // Given: iterator is not null
        // When: calling reset
        // Then: iterator is reset
        adapter.reset();
        verify(entrySet, times(1)).iterator();
    }

    @Test
    public void testSetValue() {
        // Given: entry is not null
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(entry);
        adapter.next();
        // When: calling setValue
        // Then: value is set
        String newValue = "new value";
        String oldValue = adapter.setValue(newValue);
        verify(entry, times(1)).setValue(newValue);
        assertEquals(entry.getValue(), newValue);
    }

    @Test
    public void testGetKey_ThrowsException() {
        // Given: entry is null
        // When: calling getKey
        // Then: exception is thrown
        assertThrows(IllegalStateException.class, () -> adapter.getKey());
    }

    @Test
    public void testGetValue_ThrowsException() {
        // Given: entry is null
        // When: calling getValue
        // Then: exception is thrown
        assertThrows(IllegalStateException.class, () -> adapter.getValue());
    }

    @Test
    public void testSetValue_ThrowsException() {
        // Given: entry is null
        // When: calling setValue
        // Then: exception is thrown
        assertThrows(IllegalStateException.class, () -> adapter.setValue("new value"));
    }
}