import com.github.davidmoten.rtree.internal.util.BoundedPriorityQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BoundedPriorityQueueTest {

    @Mock
    private Comparator<Integer> comparator;

    private BoundedPriorityQueue<Integer> queue;

    @BeforeEach
    public void setup() {
        queue = BoundedPriorityQueue.create(10, comparator);
    }

    @Test
    public void testCreate() {
        // Given
        int maxSize = 10;
        Comparator<Integer> comparator = (o1, o2) -> o1.compareTo(o2);

        // When
        BoundedPriorityQueue<Integer> queue = BoundedPriorityQueue.create(maxSize, comparator);

        // Then
        assertNotNull(queue);
    }

    @Test
    public void testAdd_ElementAddedToEmptyQueue() {
        // Given
        Integer element = 5;

        // When
        queue.add(element);

        // Then
        assertEquals(1, queue.asList().size());
        assertEquals(element, queue.asList().get(0));
    }

    @Test
    public void testAdd_QueueNotFull_ElementAdded() {
        // Given
        Integer element1 = 5;
        Integer element2 = 10;
        queue.add(element1);

        // When
        queue.add(element2);

        // Then
        assertEquals(2, queue.asList().size());
        assertTrue(queue.asList().contains(element1));
        assertTrue(queue.asList().contains(element2));
    }

    @Test
    public void testAdd_QueueFull_NewElementGreaterThanMaxElement_ElementAdded() {
        // Given
        Integer element1 = 5;
        Integer element2 = 10;
        Integer element3 = 15;
        queue.add(element1);
        queue.add(element2);
        // mock comparator to return element3 > element1
        // when(element1, element3).thenReturn(1);

        // When
        queue.add(element3);

        // Then
        assertEquals(2, queue.asList().size());
        assertTrue(queue.asList().contains(element2));
        assertTrue(queue.asList().contains(element3));
    }

    @Test
    public void testAdd_QueueFull_NewElementLessThanMaxElement_ElementNotAdded() {
        // Given
        Integer element1 = 5;
        Integer element2 = 10;
        Integer element3 = 3;
        queue.add(element1);
        queue.add(element2);
        // mock comparator to return element3 < element1
        // when(element1, element3).thenReturn(-1);

        // When
        queue.add(element3);

        // Then
        assertEquals(2, queue.asList().size());
        assertTrue(queue.asList().contains(element1));
        assertTrue(queue.asList().contains(element2));
    }

    @Test
    public void testAdd_NullElement_ThrowsNullPointerException() {
        // Given
        Integer element = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> queue.add(element));
    }

    @Test
    public void testAsList() {
        // Given
        Integer element1 = 5;
        Integer element2 = 10;
        queue.add(element1);
        queue.add(element2);

        // When
        List<Integer> list = queue.asList();

        // Then
        assertEquals(2, list.size());
        assertTrue(list.contains(element1));
        assertTrue(list.contains(element2));
    }

    @Test
    public void testAsOrderedList() {
        // Given
        Integer element1 = 5;
        Integer element2 = 10;
        queue.add(element1);
        queue.add(element2);

        // When
        List<Integer> list = queue.asOrderedList();

        // Then
        assertEquals(2, list.size());
        assertEquals(element1, list.get(0));
        assertEquals(element2, list.get(1));
    }
}