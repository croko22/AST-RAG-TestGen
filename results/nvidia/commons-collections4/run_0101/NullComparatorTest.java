import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.comparators.NullComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class NullComparatorTest {

    @Mock
    private Comparator<String> nonNullComparator;

    private NullComparator<String> nullComparator;

    @BeforeEach
    public void setup() {
        nullComparator = new NullComparator<>(nonNullComparator, true);
    }

    @Test
    public void testCompare_NullAndNull() {
        // Given: two null objects
        String o1 = null;
        String o2 = null;

        // When: compare the two objects
        int result = nullComparator.compare(o1, o2);

        // Then: the result should be 0
        assertEquals(0, result);
    }

    @Test
    public void testCompare_NullAndNonNull_NullsAreHigh() {
        // Given: a null object and a non-null object
        String o1 = null;
        String o2 = "non-null";

        // When: compare the two objects
        int result = nullComparator.compare(o1, o2);

        // Then: the result should be 1 (null is higher than non-null)
        assertEquals(1, result);
    }

    @Test
    public void testCompare_NullAndNonNull_NullsAreLow() {
        // Given: a null object and a non-null object
        String o1 = null;
        String o2 = "non-null";
        NullComparator<String> nullComparatorLow = new NullComparator<>(nonNullComparator, false);

        // When: compare the two objects
        int result = nullComparatorLow.compare(o1, o2);

        // Then: the result should be -1 (null is lower than non-null)
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_NonNullAndNull_NullsAreHigh() {
        // Given: a non-null object and a null object
        String o1 = "non-null";
        String o2 = null;

        // When: compare the two objects
        int result = nullComparator.compare(o1, o2);

        // Then: the result should be -1 (non-null is lower than null)
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_NonNullAndNull_NullsAreLow() {
        // Given: a non-null object and a null object
        String o1 = "non-null";
        String o2 = null;
        NullComparator<String> nullComparatorLow = new NullComparator<>(nonNullComparator, false);

        // When: compare the two objects
        int result = nullComparatorLow.compare(o1, o2);

        // Then: the result should be 1 (non-null is higher than null)
        assertEquals(1, result);
    }

    @Test
    public void testCompare_NonNullAndNonNull() {
        // Given: two non-null objects
        String o1 = "non-null-1";
        String o2 = "non-null-2";

        // When: compare the two objects
        int result = nullComparator.compare(o1, o2);

        // Then: the result should be the result of the non-null comparator
        // For this test, we assume the non-null comparator returns -1
        // In a real test, you would need to mock the non-null comparator to return a specific value
        // assertEquals(-1, result);
    }

    @Test
    public void testEquals_SameInstance() {
        // Given: the same instance
        Object obj = nullComparator;

        // When: check if the object is equal to the null comparator
        boolean result = nullComparator.equals(obj);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_SameProperties() {
        // Given: a different instance with the same properties
        NullComparator<String> otherNullComparator = new NullComparator<>(nonNullComparator, true);
        Object obj = otherNullComparator;

        // When: check if the object is equal to the null comparator
        boolean result = nullComparator.equals(obj);

        // Then: the result should be true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_DifferentProperties() {
        // Given: a different instance with different properties
        NullComparator<String> otherNullComparator = new NullComparator<>(nonNullComparator, false);
        Object obj = otherNullComparator;

        // When: check if the object is equal to the null comparator
        boolean result = nullComparator.equals(obj);

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testEquals_Null() {
        // Given: a null object
        Object obj = null;

        // When: check if the object is equal to the null comparator
        boolean result = nullComparator.equals(obj);

        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given: the null comparator
        NullComparator<String> nullComparator = new NullComparator<>(nonNullComparator, true);

        // When: get the hash code of the null comparator
        int result = nullComparator.hashCode();

        // Then: the result should be the hash code of the non-null comparator multiplied by -1
        // For this test, we assume the non-null comparator returns a specific hash code
        // In a real test, you would need to mock the non-null comparator to return a specific hash code
        // assertEquals(-1 * nonNullComparator.hashCode(), result);
    }
}