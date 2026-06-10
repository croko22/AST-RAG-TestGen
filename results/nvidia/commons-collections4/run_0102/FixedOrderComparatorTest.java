import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FixedOrderComparatorTest {

    private FixedOrderComparator<String> comparator;

    @BeforeEach
    void setup() {
        comparator = new FixedOrderComparator<>();
    }

    @Test
    void testAdd() {
        // Given
        String obj = "Test";

        // When
        boolean result = comparator.add(obj);

        // Then
        assertTrue(result);
        assertEquals(1, comparator.compare("Test", "Test2"));
    }

    @Test
    void testAdd_Duplicate() {
        // Given
        String obj = "Test";
        comparator.add(obj);

        // When
        boolean result = comparator.add(obj);

        // Then
        assertFalse(result);
    }

    @Test
    void testAddAsEqual() {
        // Given
        String existingObj = "Test";
        String newObj = "Test2";
        comparator.add(existingObj);

        // When
        boolean result = comparator.addAsEqual(existingObj, newObj);

        // Then
        assertTrue(result);
        assertEquals(0, comparator.compare(existingObj, newObj));
    }

    @Test
    void testAddAsEqual_ExistingObjNotKnown() {
        // Given
        String existingObj = "Test";
        String newObj = "Test2";

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> comparator.addAsEqual(existingObj, newObj));
    }

    @Test
    void testCompare() {
        // Given
        String obj1 = "Test";
        String obj2 = "Test2";
        comparator.add(obj1);
        comparator.add(obj2);

        // When
        int result = comparator.compare(obj1, obj2);

        // Then
        assertTrue(result < 0);
    }

    @Test
    void testCompare_UnknownObj() {
        // Given
        String obj1 = "Test";
        String obj2 = "Test2";

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> comparator.compare(obj1, obj2));
    }

    @Test
    void testCompare_UnknownObj_Before() {
        // Given
        String obj1 = "Test";
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        comparator.add(obj1);

        // When
        int result = comparator.compare(obj1, "Test2");

        // Then
        assertTrue(result < 0);
    }

    @Test
    void testCompare_UnknownObj_After() {
        // Given
        String obj1 = "Test";
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        comparator.add(obj1);

        // When
        int result = comparator.compare(obj1, "Test2");

        // Then
        assertTrue(result > 0);
    }

    @Test
    void testEquals() {
        // Given
        FixedOrderComparator<String> other = new FixedOrderComparator<>();
        String obj = "Test";
        comparator.add(obj);
        other.add(obj);

        // When
        boolean result = comparator.equals(other);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_NotEqual() {
        // Given
        FixedOrderComparator<String> other = new FixedOrderComparator<>();
        String obj = "Test";
        comparator.add(obj);

        // When
        boolean result = comparator.equals(other);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetUnknownObjectBehavior() {
        // Given
        FixedOrderComparator.UnknownObjectBehavior expected = FixedOrderComparator.UnknownObjectBehavior.EXCEPTION;

        // When
        FixedOrderComparator.UnknownObjectBehavior result = comparator.getUnknownObjectBehavior();

        // Then
        assertEquals(expected, result);
    }

    @Test
    void testHashCode() {
        // Given
        int expected = comparator.hashCode();

        // When
        int result = comparator.hashCode();

        // Then
        assertEquals(expected, result);
    }

    @Test
    void testIsLocked() {
        // Given
        assertFalse(comparator.isLocked());

        // When
        comparator.compare("Test", "Test2");

        // Then
        assertTrue(comparator.isLocked());
    }

    @Test
    void testSetUnknownObjectBehavior() {
        // Given
        FixedOrderComparator.UnknownObjectBehavior expected = FixedOrderComparator.UnknownObjectBehavior.BEFORE;

        // When
        comparator.setUnknownObjectBehavior(expected);

        // Then
        assertEquals(expected, comparator.getUnknownObjectBehavior());
    }

    @Test
    void testSetUnknownObjectBehavior_Null() {
        // Given

        // When and Then
        assertThrows(NullPointerException.class, () -> comparator.setUnknownObjectBehavior(null));
    }

    @Test
    void testSetUnknownObjectBehavior_Locked() {
        // Given
        comparator.compare("Test", "Test2");

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE));
    }
}