import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;
import org.apache.commons.collections4.OrderedMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmptyOrderedMapIteratorTest {

    private EmptyOrderedMapIterator<String, String> iterator;

    @BeforeEach
    void setup() {
        iterator = (EmptyOrderedMapIterator<String, String>) EmptyOrderedMapIterator.emptyOrderedMapIterator();
    }

    @Test
    public void testEmptyOrderedMapIterator() {
        // Given: iterator is empty
        // When: hasPrevious is called
        // Then: hasPrevious returns false
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testPrevious() {
        // Given: iterator is empty
        // When: previous is called
        // Then: previous throws NoSuchElementException
        assertThrows(java.util.NoSuchElementException.class, () -> iterator.previous());
    }

    @Test
    public void testEmptyOrderedMapIteratorSingleton() {
        // Given: two instances of EmptyOrderedMapIterator
        EmptyOrderedMapIterator<String, String> instance1 = (EmptyOrderedMapIterator<String, String>) EmptyOrderedMapIterator.emptyOrderedMapIterator();
        EmptyOrderedMapIterator<String, String> instance2 = (EmptyOrderedMapIterator<String, String>) EmptyOrderedMapIterator.emptyOrderedMapIterator();

        // When: comparing instances
        // Then: instances are the same
        assertSame(instance1, instance2);
    }
}