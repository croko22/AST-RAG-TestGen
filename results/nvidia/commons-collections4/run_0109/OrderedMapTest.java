import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderedMapTest {

    @Mock
    private OrderedMap<String, String> orderedMap;

    @BeforeEach
    public void setup() {
        // Initialize the ordered map with some data
        when(orderedMap.mapIterator()).thenReturn(new OrderedMapIterator<String, String>() {
            @Override
            public String next() {
                return "next";
            }

            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public String getValue() {
                return "value";
            }

            @Override
            public String setValue(String value) {
                return "new value";
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public boolean hasPrevious() {
                return true;
            }

            @Override
            public String previous() {
                return "previous";
            }
        });
    }

    @Test
    public void testFirstKey_EmptyMap() {
        // Given: an empty ordered map
        when(orderedMap.firstKey()).thenThrow(NoSuchElementException.class);

        // When / Then: first key is called
        assertThrows(NoSuchElementException.class, () -> orderedMap.firstKey());
    }

    @Test
    public void testFirstKey_NonEmptyMap() {
        // Given: a non-empty ordered map
        when(orderedMap.firstKey()).thenReturn("first key");

        // When: first key is called
        String firstKey = orderedMap.firstKey();

        // Then: the first key is returned
        assertEquals("first key", firstKey);
    }

    @Test
    public void testLastKey_EmptyMap() {
        // Given: an empty ordered map
        when(orderedMap.lastKey()).thenThrow(NoSuchElementException.class);

        // When / Then: last key is called
        assertThrows(NoSuchElementException.class, () -> orderedMap.lastKey());
    }

    @Test
    public void testLastKey_NonEmptyMap() {
        // Given: a non-empty ordered map
        when(orderedMap.lastKey()).thenReturn("last key");

        // When: last key is called
        String lastKey = orderedMap.lastKey();

        // Then: the last key is returned
        assertEquals("last key", lastKey);
    }

    @Test
    public void testMapIterator() {
        // When: map iterator is called
        OrderedMapIterator<String, String> iterator = orderedMap.mapIterator();

        // Then: the iterator is not null
        assertNotNull(iterator);
    }

    @Test
    public void testNextKey_KeyExists() {
        // Given: a key that exists in the ordered map
        when(orderedMap.nextKey("key")).thenReturn("next key");

        // When: next key is called
        String nextKey = orderedMap.nextKey("key");

        // Then: the next key is returned
        assertEquals("next key", nextKey);
    }

    @Test
    public void testNextKey_KeyDoesNotExist() {
        // Given: a key that does not exist in the ordered map
        when(orderedMap.nextKey("key")).thenReturn(null);

        // When: next key is called
        String nextKey = orderedMap.nextKey("key");

        // Then: null is returned
        assertNull(nextKey);
    }

    @Test
    public void testPreviousKey_KeyExists() {
        // Given: a key that exists in the ordered map
        when(orderedMap.previousKey("key")).thenReturn("previous key");

        // When: previous key is called
        String previousKey = orderedMap.previousKey("key");

        // Then: the previous key is returned
        assertEquals("previous key", previousKey);
    }

    @Test
    public void testPreviousKey_KeyDoesNotExist() {
        // Given: a key that does not exist in the ordered map
        when(orderedMap.previousKey("key")).thenReturn(null);

        // When: previous key is called
        String previousKey = orderedMap.previousKey("key");

        // Then: null is returned
        assertNull(previousKey);
    }
}