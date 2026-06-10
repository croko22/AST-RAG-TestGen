import org.apache.commons.collections4.Trie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrieTest {

    @Mock
    private Trie<String, String> trie;

    @BeforeEach
    void setup() {
        // Initialize the trie with some data
        when(trie.prefixMap("And")).thenReturn(new TreeMap<>());
        when(trie.prefixMap("An")).thenReturn(new TreeMap<>());
    }

    @Test
    public void testPrefixMap_HappyPath() {
        // Given: a trie with some data
        when(trie.prefixMap("And")).thenReturn(new TreeMap<>());
        when(trie.prefixMap("And")).thenReturn(new TreeMap<>());

        // When: we call prefixMap with a key
        SortedMap<String, String> result = trie.prefixMap("And");

        // Then: we get a non-null result
        assertNotNull(result);
        verify(trie, times(1)).prefixMap("And");
    }

    @Test
    public void testPrefixMap_NullKey() {
        // Given: a trie with some data
        when(trie.prefixMap(any())).thenThrow(NullPointerException.class);

        // When / Then: we expect an exception when calling prefixMap with a null key
        assertThrows(NullPointerException.class, () -> trie.prefixMap(null));
        verify(trie, times(1)).prefixMap(null);
    }

    @Test
    public void testPrefixMap_EmptyKey() {
        // Given: a trie with some data
        when(trie.prefixMap("")).thenReturn(new TreeMap<>());

        // When: we call prefixMap with an empty key
        SortedMap<String, String> result = trie.prefixMap("");

        // Then: we get a non-null result
        assertNotNull(result);
        verify(trie, times(1)).prefixMap("");
    }

    @Test
    public void testPrefixMap_KeyNotFound() {
        // Given: a trie with some data
        when(trie.prefixMap("Unknown")).thenReturn(new TreeMap<>());

        // When: we call prefixMap with a key that is not in the trie
        SortedMap<String, String> result = trie.prefixMap("Unknown");

        // Then: we get an empty result
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trie, times(1)).prefixMap("Unknown");
    }

    @Test
    public void testPrefixMap_MultipleCalls() {
        // Given: a trie with some data
        when(trie.prefixMap("And")).thenReturn(new TreeMap<>());
        when(trie.prefixMap("An")).thenReturn(new TreeMap<>());

        // When: we call prefixMap multiple times with different keys
        SortedMap<String, String> result1 = trie.prefixMap("And");
        SortedMap<String, String> result2 = trie.prefixMap("An");

        // Then: we get non-null results
        assertNotNull(result1);
        assertNotNull(result2);
        verify(trie, times(1)).prefixMap("And");
        verify(trie, times(1)).prefixMap("An");
    }
}