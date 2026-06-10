import org.apache.commons.collections4.OrderedIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmptyOrderedIteratorTest {

    private EmptyOrderedIterator<?> emptyIterator;

    @BeforeEach
    void setup() {
        emptyIterator = EmptyOrderedIterator.emptyOrderedIterator();
    }

    @Test
    void testEmptyOrderedIterator() {
        // Given: empty iterator
        // When: checking hasPrevious
        boolean hasPrevious = emptyIterator.hasPrevious();
        // Then: hasPrevious should be false
        assertFalse(hasPrevious);
    }

    @Test
    void testPrevious() {
        // Given: empty iterator
        // When / Then: previous should throw NoSuchElementException
        assertThrows(java.util.NoSuchElementException.class, () -> {
            emptyIterator.previous();
        });
    }

    @Test
    void testEmptyOrderedIteratorSingleton() {
        // Given: two instances of empty iterator
        OrderedIterator<?> instance1 = EmptyOrderedIterator.emptyOrderedIterator();
        OrderedIterator<?> instance2 = EmptyOrderedIterator.emptyOrderedIterator();
        // When: checking instances
        // Then: instances should be the same
        assertSame(instance1, instance2);
    }
}