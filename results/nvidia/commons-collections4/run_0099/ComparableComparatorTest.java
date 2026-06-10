import org.apache.commons.collections4.comparators.ComparableComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ComparableComparatorTest {

    @Test
    public void testComparableComparator() {
        // Given: a ComparableComparator instance
        ComparableComparator<String> comparator = ComparableComparator.comparableComparator();

        // When: compare two strings
        int result = comparator.compare("apple", "banana");

        // Then: verify the comparison result
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_Equal() {
        // Given: a ComparableComparator instance
        ComparableComparator<String> comparator = ComparableComparator.comparableComparator();

        // When: compare two equal strings
        int result = comparator.compare("apple", "apple");

        // Then: verify the comparison result
        assertEquals(0, result);
    }

    @Test
    public void testCompare_Null() {
        // Given: a ComparableComparator instance
        ComparableComparator<String> comparator = ComparableComparator.comparableComparator();

        // When / Then: verify that comparing null throws NullPointerException
        assertThrows(NullPointerException.class, () -> comparator.compare(null, "apple"));
    }

    @Test
    public void testEquals_SameInstance() {
        // Given: a ComparableComparator instance
        ComparableComparator<String> comparator = ComparableComparator.comparableComparator();

        // When: compare the instance with itself
        boolean result = comparator.equals(comparator);

        // Then: verify the equality result
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance() {
        // Given: two ComparableComparator instances
        ComparableComparator<String> comparator1 = ComparableComparator.comparableComparator();
        ComparableComparator<String> comparator2 = ComparableComparator.comparableComparator();

        // When: compare the two instances
        boolean result = comparator1.equals(comparator2);

        // Then: verify the equality result
        assertTrue(result);
    }

    @Test
    public void testEquals_Null() {
        // Given: a ComparableComparator instance
        ComparableComparator<String> comparator = ComparableComparator.comparableComparator();

        // When: compare the instance with null
        boolean result = comparator.equals(null);

        // Then: verify the equality result
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: a ComparableComparator instance and an instance of a different class
        ComparableComparator<String> comparator = ComparableComparator.comparableComparator();
        Object differentClass = new Object();

        // When: compare the instance with the different class instance
        boolean result = comparator.equals(differentClass);

        // Then: verify the equality result
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given: a ComparableComparator instance
        ComparableComparator<String> comparator = ComparableComparator.comparableComparator();

        // When: get the hash code of the instance
        int hashCode = comparator.hashCode();

        // Then: verify the hash code
        assertNotNull(hashCode);
    }
}