import org.apache.commons.collections4.comparators.ComparatorChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class ComparatorChainTest {

    @Mock
    private Comparator<String> comparatorMock;

    private ComparatorChain<String> comparatorChain;

    @BeforeEach
    public void setup() {
        comparatorChain = new ComparatorChain<>();
    }

    @Test
    public void testAddComparator() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);

        // When
        comparatorChain.addComparator(comparator);

        // Then
        assertEquals(1, comparatorChain.size());
    }

    @Test
    public void testAddComparator_Reverse() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);

        // When
        comparatorChain.addComparator(comparator, true);

        // Then
        assertEquals(1, comparatorChain.size());
    }

    @Test
    public void testCompare() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);
        String o1 = "apple";
        String o2 = "banana";

        // When
        int result = comparatorChain.compare(o1, o2);

        // Then
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_Equal() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);
        String o1 = "apple";
        String o2 = "apple";

        // When
        int result = comparatorChain.compare(o1, o2);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testCompare_Locked() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);
        comparatorChain.compare("apple", "banana");
        Comparator<String> newComparator = (o1, o2) -> o2.compareTo(o1);

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> comparatorChain.addComparator(newComparator));
    }

    @Test
    public void testEquals() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);
        ComparatorChain<String> otherComparatorChain = new ComparatorChain<>();
        otherComparatorChain.addComparator(comparator);

        // When
        boolean result = comparatorChain.equals(otherComparatorChain);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentComparator() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);
        ComparatorChain<String> otherComparatorChain = new ComparatorChain<>();
        otherComparatorChain.addComparator((o1, o2) -> o2.compareTo(o1));

        // When
        boolean result = comparatorChain.equals(otherComparatorChain);

        // Then
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);

        // When
        int result = comparatorChain.hashCode();

        // Then
        assertEquals(comparatorChain.comparatorChain.hashCode() ^ comparatorChain.orderingBits.hashCode(), result);
    }

    @Test
    public void testIsLocked() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);

        // When
        boolean result = comparatorChain.isLocked();

        // Then
        assertFalse(result);
    }

    @Test
    public void testIsLocked_AfterCompare() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);
        comparatorChain.compare("apple", "banana");

        // When
        boolean result = comparatorChain.isLocked();

        // Then
        assertTrue(result);
    }

    @Test
    public void testSetComparator() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);
        Comparator<String> newComparator = (o1, o2) -> o2.compareTo(o1);

        // When
        comparatorChain.setComparator(0, newComparator);

        // Then
        assertEquals(1, comparatorChain.size());
    }

    @Test
    public void testSetComparator_Reverse() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);
        Comparator<String> newComparator = (o1, o2) -> o2.compareTo(o1);

        // When
        comparatorChain.setComparator(0, newComparator, true);

        // Then
        assertEquals(1, comparatorChain.size());
    }

    @Test
    public void testSetForwardSort() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator, true);

        // When
        comparatorChain.setForwardSort(0);

        // Then
        assertEquals(1, comparatorChain.size());
    }

    @Test
    public void testSetReverseSort() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);

        // When
        comparatorChain.setReverseSort(0);

        // Then
        assertEquals(1, comparatorChain.size());
    }

    @Test
    public void testSize() {
        // Given
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        comparatorChain.addComparator(comparator);

        // When
        int result = comparatorChain.size();

        // Then
        assertEquals(1, result);
    }
}