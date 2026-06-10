import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SortedBagTest {

    @Mock
    private SortedBag<String> sortedBag;

    @BeforeEach
    void setup() {
        // Initialize the mock object if necessary
    }

    @Test
    public void testComparator() {
        // Given: a comparator is associated with the sorted bag
        Comparator<String> comparator = mock(Comparator.class);
        when(sortedBag.comparator()).thenReturn(comparator);

        // When: the comparator is retrieved
        Comparator<? super String> result = sortedBag.comparator();

        // Then: the result is the expected comparator
        assertEquals(comparator, result);
    }

    @Test
    public void testComparator_Null() {
        // Given: no comparator is associated with the sorted bag
        when(sortedBag.comparator()).thenReturn(null);

        // When: the comparator is retrieved
        Comparator<? super String> result = sortedBag.comparator();

        // Then: the result is null
        assertNull(result);
    }

    @Test
    public void testFirst() {
        // Given: the sorted bag has elements
        String firstElement = "first";
        when(sortedBag.first()).thenReturn(firstElement);

        // When: the first element is retrieved
        String result = sortedBag.first();

        // Then: the result is the expected first element
        assertEquals(firstElement, result);
    }

    @Test
    public void testFirst_ThrowsException() {
        // Given: the sorted bag is empty
        when(sortedBag.first()).thenThrow(new RuntimeException("Sorted bag is empty"));

        // When / Then: an exception is thrown when retrieving the first element
        assertThrows(RuntimeException.class, () -> sortedBag.first());
    }

    @Test
    public void testLast() {
        // Given: the sorted bag has elements
        String lastElement = "last";
        when(sortedBag.last()).thenReturn(lastElement);

        // When: the last element is retrieved
        String result = sortedBag.last();

        // Then: the result is the expected last element
        assertEquals(lastElement, result);
    }

    @Test
    public void testLast_ThrowsException() {
        // Given: the sorted bag is empty
        when(sortedBag.last()).thenThrow(new RuntimeException("Sorted bag is empty"));

        // When / Then: an exception is thrown when retrieving the last element
        assertThrows(RuntimeException.class, () -> sortedBag.last());
    }
}