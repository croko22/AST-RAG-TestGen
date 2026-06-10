import org.apache.commons.collections4.iterators.SingletonListIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SingletonListIteratorTest {

    private SingletonListIterator<String> iterator;

    @BeforeEach
    public void setup() {
        iterator = new SingletonListIterator<>("test");
    }

    @Test
    public void testAdd() {
        assertThrows(UnsupportedOperationException.class, () -> iterator.add("test"));
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasPrevious() {
        assertFalse(iterator.hasPrevious());
        iterator.next();
        assertTrue(iterator.hasPrevious());
    }

    @Test
    public void testNext() {
        assertEquals("test", iterator.next());
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testNextIndex() {
        assertEquals(0, iterator.nextIndex());
        iterator.next();
        assertEquals(1, iterator.nextIndex());
    }

    @Test
    public void testPrevious() {
        iterator.next();
        assertEquals("test", iterator.previous());
        assertThrows(NoSuchElementException.class, () -> iterator.previous());
    }

    @Test
    public void testPreviousIndex() {
        assertEquals(-1, iterator.previousIndex());
        iterator.next();
        assertEquals(0, iterator.previousIndex());
    }

    @Test
    public void testRemove() {
        iterator.next();
        iterator.remove();
        assertThrows(IllegalStateException.class, () -> iterator.remove());
    }

    @Test
    public void testRemoveBeforeNext() {
        assertThrows(IllegalStateException.class, () -> iterator.remove());
    }

    @Test
    public void testReset() {
        iterator.next();
        iterator.reset();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testSet() {
        iterator.next();
        iterator.set("newTest");
        assertEquals("newTest", iterator.previous());
        assertThrows(IllegalStateException.class, () -> iterator.set("test"));
    }

    @Test
    public void testSetBeforeNext() {
        assertThrows(IllegalStateException.class, () -> iterator.set("test"));
    }
}