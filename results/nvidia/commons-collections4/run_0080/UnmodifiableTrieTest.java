import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;
import org.apache.commons.collections4.trie.UnmodifiableTrie;
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
public class UnmodifiableTrieTest {

    @Mock
    private Trie<String, String> delegate;

    private UnmodifiableTrie<String, String> unmodifiableTrie;

    @BeforeEach
    void setup() {
        unmodifiableTrie = new UnmodifiableTrie<>(delegate);
    }

    @Test
    void testUnmodifiableTrieFactoryMethod() {
        // Given: a delegate trie
        Trie<String, String> trie = mock(Trie.class);

        // When: creating an unmodifiable trie using the factory method
        Trie<String, String> unmodifiableTrie = UnmodifiableTrie.unmodifiableTrie(trie);

        // Then: the unmodifiable trie is created successfully
        assertNotNull(unmodifiableTrie);
    }

    @Test
    void testClear() {
        // When: calling the clear method
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableTrie.clear());

        // Then: the delegate's clear method is not called
        verify(delegate, never()).clear();
    }

    @Test
    void testComparator() {
        // Given: a comparator
        Comparator<String> comparator = mock(Comparator.class);
        when(delegate.comparator()).thenReturn(comparator);

        // When: getting the comparator
        Comparator<String> result = unmodifiableTrie.comparator();

        // Then: the comparator is returned successfully
        assertEquals(comparator, result);
    }

    @Test
    void testContainsKey() {
        // Given: a key
        String key = "key";

        // When: checking if the key is contained
        boolean result = unmodifiableTrie.containsKey(key);

        // Then: the delegate's containsKey method is called
        verify(delegate).containsKey(key);
    }

    @Test
    void testContainsValue() {
        // Given: a value
        String value = "value";

        // When: checking if the value is contained
        boolean result = unmodifiableTrie.containsValue(value);

        // Then: the delegate's containsValue method is called
        verify(delegate).containsValue(value);
    }

    @Test
    void testEntrySet() {
        // When: getting the entry set
        Set<Map.Entry<String, String>> result = unmodifiableTrie.entrySet();

        // Then: the entry set is returned successfully
        assertNotNull(result);
    }

    @Test
    void testEquals() {
        // Given: an object
        Object obj = new Object();

        // When: checking if the object is equal
        boolean result = unmodifiableTrie.equals(obj);

        // Then: the delegate's equals method is called
        verify(delegate).equals(obj);
    }

    @Test
    void testFirstKey() {
        // When: getting the first key
        String result = unmodifiableTrie.firstKey();

        // Then: the delegate's firstKey method is called
        verify(delegate).firstKey();
    }

    @Test
    void testGet() {
        // Given: a key
        String key = "key";

        // When: getting the value
        String result = unmodifiableTrie.get(key);

        // Then: the delegate's get method is called
        verify(delegate).get(key);
    }

    @Test
    void testHashCode() {
        // When: getting the hash code
        int result = unmodifiableTrie.hashCode();

        // Then: the delegate's hashCode method is called
        verify(delegate).hashCode();
    }

    @Test
    void testHeadMap() {
        // Given: a key
        String key = "key";

        // When: getting the head map
        SortedMap<String, String> result = unmodifiableTrie.headMap(key);

        // Then: the head map is returned successfully
        assertNotNull(result);
    }

    @Test
    void testIsEmpty() {
        // When: checking if the trie is empty
        boolean result = unmodifiableTrie.isEmpty();

        // Then: the delegate's isEmpty method is called
        verify(delegate).isEmpty();
    }

    @Test
    void testKeySet() {
        // When: getting the key set
        Set<String> result = unmodifiableTrie.keySet();

        // Then: the key set is returned successfully
        assertNotNull(result);
    }

    @Test
    void testLastKey() {
        // When: getting the last key
        String result = unmodifiableTrie.lastKey();

        // Then: the delegate's lastKey method is called
        verify(delegate).lastKey();
    }

    @Test
    void testMapIterator() {
        // When: getting the map iterator
        UnmodifiableOrderedMapIterator<String, String> result = (UnmodifiableOrderedMapIterator<String, String>) unmodifiableTrie.mapIterator();

        // Then: the map iterator is returned successfully
        assertNotNull(result);
    }

    @Test
    void testNextKey() {
        // Given: a key
        String key = "key";

        // When: getting the next key
        String result = unmodifiableTrie.nextKey(key);

        // Then: the delegate's nextKey method is called
        verify(delegate).nextKey(key);
    }

    @Test
    void testPrefixMap() {
        // Given: a key
        String key = "key";

        // When: getting the prefix map
        SortedMap<String, String> result = unmodifiableTrie.prefixMap(key);

        // Then: the prefix map is returned successfully
        assertNotNull(result);
    }

    @Test
    void testPreviousKey() {
        // Given: a key
        String key = "key";

        // When: getting the previous key
        String result = unmodifiableTrie.previousKey(key);

        // Then: the delegate's previousKey method is called
        verify(delegate).previousKey(key);
    }

    @Test
    void testPut() {
        // Given: a key and a value
        String key = "key";
        String value = "value";

        // When: putting the key-value pair
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableTrie.put(key, value));

        // Then: the delegate's put method is not called
        verify(delegate, never()).put(key, value);
    }

    @Test
    void testPutAll() {
        // Given: a map
        Map<String, String> map = new HashMap<>();

        // When: putting all the entries
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableTrie.putAll(map));

        // Then: the delegate's putAll method is not called
        verify(delegate, never()).putAll(map);
    }

    @Test
    void testRemove() {
        // Given: a key
        String key = "key";

        // When: removing the key
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableTrie.remove(key));

        // Then: the delegate's remove method is not called
        verify(delegate, never()).remove(key);
    }

    @Test
    void testSize() {
        // When: getting the size
        int result = unmodifiableTrie.size();

        // Then: the delegate's size method is called
        verify(delegate).size();
    }

    @Test
    void testSubMap() {
        // Given: two keys
        String fromKey = "fromKey";
        String toKey = "toKey";

        // When: getting the sub map
        SortedMap<String, String> result = unmodifiableTrie.subMap(fromKey, toKey);

        // Then: the sub map is returned successfully
        assertNotNull(result);
    }

    @Test
    void testTailMap() {
        // Given: a key
        String key = "key";

        // When: getting the tail map
        SortedMap<String, String> result = unmodifiableTrie.tailMap(key);

        // Then: the tail map is returned successfully
        assertNotNull(result);
    }

    @Test
    void testToString() {
        // When: getting the string representation
        String result = unmodifiableTrie.toString();

        // Then: the delegate's toString method is called
        verify(delegate).toString();
    }

    @Test
    void testValues() {
        // When: getting the values
        Collection<String> result = unmodifiableTrie.values();

        // Then: the values are returned successfully
        assertNotNull(result);
    }
}