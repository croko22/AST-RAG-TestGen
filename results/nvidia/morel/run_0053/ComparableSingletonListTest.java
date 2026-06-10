import net.hydromatic.morel.util.ComparableSingletonList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ComparableSingletonListTest {

    @Test
    public void testOf() {
        // Given: an element
        Integer element = 10;

        // When: creating a ComparableSingletonList
        ComparableSingletonList<Integer> list = ComparableSingletonList.of(element);

        // Then: the list is not null and has the correct element
        assertNotNull(list);
        assertEquals(element, list.get(0));
    }

    @Test
    public void testGet() {
        // Given: a ComparableSingletonList
        Integer element = 10;
        ComparableSingletonList<Integer> list = ComparableSingletonList.of(element);

        // When: getting the element at index 0
        Integer result = list.get(0);

        // Then: the result is the same as the element
        assertEquals(element, result);
    }

    @Test
    public void testGet_IndexOutOfBoundsException() {
        // Given: a ComparableSingletonList
        Integer element = 10;
        ComparableSingletonList<Integer> list = ComparableSingletonList.of(element);

        // When / Then: getting the element at index 1 throws an exception
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    public void testSize() {
        // Given: a ComparableSingletonList
        Integer element = 10;
        ComparableSingletonList<Integer> list = ComparableSingletonList.of(element);

        // When: getting the size of the list
        int size = list.size();

        // Then: the size is 1
        assertEquals(1, size);
    }

    @Test
    public void testCompareTo_Equal() {
        // Given: two ComparableSingletonLists with equal elements
        Integer element1 = 10;
        Integer element2 = 10;
        ComparableSingletonList<Integer> list1 = ComparableSingletonList.of(element1);
        ComparableSingletonList<Integer> list2 = ComparableSingletonList.of(element2);

        // When: comparing the lists
        int result = list1.compareTo(list2);

        // Then: the result is 0
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_LessThan() {
        // Given: two ComparableSingletonLists with elements where the first is less than the second
        Integer element1 = 10;
        Integer element2 = 20;
        ComparableSingletonList<Integer> list1 = ComparableSingletonList.of(element1);
        ComparableSingletonList<Integer> list2 = ComparableSingletonList.of(element2);

        // When: comparing the lists
        int result = list1.compareTo(list2);

        // Then: the result is negative
        assertTrue(result < 0);
    }

    @Test
    public void testCompareTo_GreaterThan() {
        // Given: two ComparableSingletonLists with elements where the first is greater than the second
        Integer element1 = 20;
        Integer element2 = 10;
        ComparableSingletonList<Integer> list1 = ComparableSingletonList.of(element1);
        ComparableSingletonList<Integer> list2 = ComparableSingletonList.of(element2);

        // When: comparing the lists
        int result = list1.compareTo(list2);

        // Then: the result is positive
        assertTrue(result > 0);
    }
}