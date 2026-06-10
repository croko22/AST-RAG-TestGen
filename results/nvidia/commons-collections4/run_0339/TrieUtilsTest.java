import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.TrieUtils;
import org.apache.commons.collections4.trie.UnmodifiableTrie;
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
public class TrieUtilsTest {

    @Mock
    private Trie<String, String> trieMock;

    @Mock
    private UnmodifiableTrie<String, String> unmodifiableTrieMock;

    @BeforeEach
    void setup() {
        // Setup mock behavior
        when(UnmodifiableTrie.unmodifiableTrie(any())).thenReturn(unmodifiableTrieMock);
    }

    @Test
    public void testUnmodifiableTrie_Success() {
        // Given: a valid trie instance
        Trie<String, String> trie = new HashMap<>();

        // When: unmodifiableTrie is called
        Trie<String, String> result = TrieUtils.unmodifiableTrie(trie);

        // Then: verify the result and mock interactions
        assertNotNull(result);
        verifyStatic(UnmodifiableTrie.class, times(1));
        UnmodifiableTrie.unmodifiableTrie(trie);
    }

    @Test
    public void testUnmodifiableTrie_NullTrie() {
        // Given: a null trie instance
        Trie<String, String> trie = null;

        // When / Then: expect NullPointerException
        assertThrows(NullPointerException.class, () -> TrieUtils.unmodifiableTrie(trie));
    }

    @Test
    public void testUnmodifiableTrie_MultipleCalls() {
        // Given: a valid trie instance
        Trie<String, String> trie = new HashMap<>();

        // When: unmodifiableTrie is called multiple times
        Trie<String, String> result1 = TrieUtils.unmodifiableTrie(trie);
        Trie<String, String> result2 = TrieUtils.unmodifiableTrie(trie);

        // Then: verify the results and mock interactions
        assertNotNull(result1);
        assertNotNull(result2);
        assertSame(result1, result2);
        verifyStatic(UnmodifiableTrie.class, times(2));
        UnmodifiableTrie.unmodifiableTrie(trie);
    }
}