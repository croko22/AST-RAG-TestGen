import org.apache.commons.collections4.comparators.BooleanComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BooleanComparatorTest {

    private BooleanComparator trueFirstComparator;
    private BooleanComparator falseFirstComparator;

    @BeforeEach
    public void setup() {
        trueFirstComparator = BooleanComparator.getTrueFirstComparator();
        falseFirstComparator = BooleanComparator.getFalseFirstComparator();
    }

    @Test
    public void testBooleanComparator_TrueFirst() {
        // Given
        BooleanComparator comparator = BooleanComparator.booleanComparator(true);

        // Then
        assertNotNull(comparator);
        assertTrue(comparator.sortsTrueFirst());
    }

    @Test
    public void testBooleanComparator_FalseFirst() {
        // Given
        BooleanComparator comparator = BooleanComparator.booleanComparator(false);

        // Then
        assertNotNull(comparator);
        assertFalse(comparator.sortsTrueFirst());
    }

    @Test
    public void testGetTrueFirstComparator() {
        // Given
        BooleanComparator comparator = BooleanComparator.getTrueFirstComparator();

        // Then
        assertNotNull(comparator);
        assertTrue(comparator.sortsTrueFirst());
    }

    @Test
    public void testGetFalseFirstComparator() {
        // Given
        BooleanComparator comparator = BooleanComparator.getFalseFirstComparator();

        // Then
        assertNotNull(comparator);
        assertFalse(comparator.sortsTrueFirst());
    }

    @Test
    public void testCompare_TrueFirst_TrueThenFalse() {
        // Given
        Boolean b1 = Boolean.TRUE;
        Boolean b2 = Boolean.FALSE;

        // When
        int result = trueFirstComparator.compare(b1, b2);

        // Then
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_TrueFirst_FalseThenTrue() {
        // Given
        Boolean b1 = Boolean.FALSE;
        Boolean b2 = Boolean.TRUE;

        // When
        int result = trueFirstComparator.compare(b1, b2);

        // Then
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_TrueFirst_TrueThenTrue() {
        // Given
        Boolean b1 = Boolean.TRUE;
        Boolean b2 = Boolean.TRUE;

        // When
        int result = trueFirstComparator.compare(b1, b2);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testCompare_TrueFirst_FalseThenFalse() {
        // Given
        Boolean b1 = Boolean.FALSE;
        Boolean b2 = Boolean.FALSE;

        // When
        int result = trueFirstComparator.compare(b1, b2);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FalseFirst_TrueThenFalse() {
        // Given
        Boolean b1 = Boolean.TRUE;
        Boolean b2 = Boolean.FALSE;

        // When
        int result = falseFirstComparator.compare(b1, b2);

        // Then
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_FalseFirst_FalseThenTrue() {
        // Given
        Boolean b1 = Boolean.FALSE;
        Boolean b2 = Boolean.TRUE;

        // When
        int result = falseFirstComparator.compare(b1, b2);

        // Then
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_FalseFirst_TrueThenTrue() {
        // Given
        Boolean b1 = Boolean.TRUE;
        Boolean b2 = Boolean.TRUE;

        // When
        int result = falseFirstComparator.compare(b1, b2);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FalseFirst_FalseThenFalse() {
        // Given
        Boolean b1 = Boolean.FALSE;
        Boolean b2 = Boolean.FALSE;

        // When
        int result = falseFirstComparator.compare(b1, b2);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testEquals_SameInstance() {
        // Given
        BooleanComparator comparator1 = BooleanComparator.getTrueFirstComparator();
        BooleanComparator comparator2 = BooleanComparator.getTrueFirstComparator();

        // Then
        assertTrue(comparator1.equals(comparator2));
    }

    @Test
    public void testEquals_DifferentInstancesSameType() {
        // Given
        BooleanComparator comparator1 = BooleanComparator.getTrueFirstComparator();
        BooleanComparator comparator2 = BooleanComparator.getTrueFirstComparator();

        // Then
        assertTrue(comparator1.equals(comparator2));
    }

    @Test
    public void testEquals_DifferentInstancesDifferentType() {
        // Given
        BooleanComparator comparator1 = BooleanComparator.getTrueFirstComparator();
        Object object = new Object();

        // Then
        assertFalse(comparator1.equals(object));
    }

    @Test
    public void testHashCode() {
        // Given
        BooleanComparator comparator1 = BooleanComparator.getTrueFirstComparator();
        BooleanComparator comparator2 = BooleanComparator.getTrueFirstComparator();

        // Then
        assertEquals(comparator1.hashCode(), comparator2.hashCode());
    }

    @Test
    public void testSortsTrueFirst_TrueFirstComparator() {
        // Given
        BooleanComparator comparator = BooleanComparator.getTrueFirstComparator();

        // Then
        assertTrue(comparator.sortsTrueFirst());
    }

    @Test
    public void testSortsTrueFirst_FalseFirstComparator() {
        // Given
        BooleanComparator comparator = BooleanComparator.getFalseFirstComparator();

        // Then
        assertFalse(comparator.sortsTrueFirst());
    }
}