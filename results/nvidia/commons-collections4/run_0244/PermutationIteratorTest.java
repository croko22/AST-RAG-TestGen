import org.apache.commons.collections4.iterators.PermutationIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class PermutationIteratorTest {

    private PermutationIterator<String> permutationIterator;

    @BeforeEach
    void setup() {
        List<String> list = Arrays.asList("a", "b", "c");
        permutationIterator = new PermutationIterator<>(list);
    }

    @Test
    void testHasNext() {
        // Given: a permutation iterator with a list of elements
        // When: hasNext is called
        // Then: it should return true
        assertTrue(permutationIterator.hasNext());
    }

    @Test
    void testNext() {
        // Given: a permutation iterator with a list of elements
        // When: next is called
        // Then: it should return a list of elements representing a permutation
        List<String> permutation = permutationIterator.next();
        assertNotNull(permutation);
        assertEquals(3, permutation.size());
    }

    @Test
    void testNext_MultipleCalls() {
        // Given: a permutation iterator with a list of elements
        // When: next is called multiple times
        // Then: it should return different permutations
        Set<List<String>> permutations = new HashSet<>();
        while (permutationIterator.hasNext()) {
            permutations.add(permutationIterator.next());
        }
        assertEquals(6, permutations.size());
    }

    @Test
    void testRemove() {
        // Given: a permutation iterator with a list of elements
        // When: remove is called
        // Then: it should throw an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> permutationIterator.remove());
    }

    @Test
    void testHasNext_EmptyList() {
        // Given: a permutation iterator with an empty list
        PermutationIterator<String> emptyPermutationIterator = new PermutationIterator<>(Collections.emptyList());
        // When: hasNext is called
        // Then: it should return true
        assertTrue(emptyPermutationIterator.hasNext());
    }

    @Test
    void testNext_EmptyList() {
        // Given: a permutation iterator with an empty list
        PermutationIterator<String> emptyPermutationIterator = new PermutationIterator<>(Collections.emptyList());
        // When: next is called
        // Then: it should return an empty list
        List<String> permutation = emptyPermutationIterator.next();
        assertNotNull(permutation);
        assertTrue(permutation.isEmpty());
    }

    @Test
    void testNext_NoMorePermutations() {
        // Given: a permutation iterator with a list of elements
        PermutationIterator<String> permutationIterator = new PermutationIterator<>(Arrays.asList("a", "b", "c"));
        // When: next is called after all permutations have been returned
        // Then: it should throw a NoSuchElementException
        while (permutationIterator.hasNext()) {
            permutationIterator.next();
        }
        assertThrows(NoSuchElementException.class, () -> permutationIterator.next());
    }
}