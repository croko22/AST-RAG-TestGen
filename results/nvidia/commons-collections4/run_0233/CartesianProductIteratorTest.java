import org.apache.commons.collections4.iterators.CartesianProductIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class CartesianProductIteratorTest {

    private CartesianProductIterator<Character> iterator;

    @BeforeEach
    public void setup() {
        List<Character> iterable1 = Arrays.asList('A', 'B', 'C');
        List<Character> iterable2 = Arrays.asList('1', '2', '3');
        iterator = new CartesianProductIterator<>(iterable1, iterable2);
    }

    @Test
    public void testHasNext() {
        // Given: the iterator has been initialized
        // When: hasNext is called
        // Then: it should return true
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testNext() {
        // Given: the iterator has been initialized
        // When: next is called
        // Then: it should return the next tuple
        List<Character> tuple = iterator.next();
        assertEquals(Arrays.asList('A', '1'), tuple);
    }

    @Test
    public void testNext_MultipleCalls() {
        // Given: the iterator has been initialized
        // When: next is called multiple times
        // Then: it should return the next tuple each time
        List<Character> tuple1 = iterator.next();
        assertEquals(Arrays.asList('A', '1'), tuple1);

        List<Character> tuple2 = iterator.next();
        assertEquals(Arrays.asList('A', '2'), tuple2);

        List<Character> tuple3 = iterator.next();
        assertEquals(Arrays.asList('A', '3'), tuple3);

        List<Character> tuple4 = iterator.next();
        assertEquals(Arrays.asList('B', '1'), tuple4);
    }

    @Test
    public void testNext_NoMoreElements() {
        // Given: the iterator has been initialized and all elements have been exhausted
        // When: next is called
        // Then: it should throw a NoSuchElementException
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(Arrays.asList('A'), Arrays.asList('1'));
        iterator.next();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testRemove() {
        // Given: the iterator has been initialized
        // When: remove is called
        // Then: it should throw an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    @Test
    public void testEmptyIterables() {
        // Given: the iterator has been initialized with empty iterables
        // When: hasNext is called
        // Then: it should return false
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(Arrays.asList(), Arrays.asList('1', '2', '3'));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNullIterables() {
        // Given: the iterator has been initialized with null iterables
        // When: the constructor is called
        // Then: it should throw a NullPointerException
        assertThrows(NullPointerException.class, () -> new CartesianProductIterator<>((Iterable<Character>) null, Arrays.asList('1', '2', '3')));
    }
}