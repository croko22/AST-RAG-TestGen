import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.comparators.ReverseComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReverseComparatorTest {

    @Mock
    private Comparator<String> comparatorMock;

    private ReverseComparator<String> reverseComparator;

    @BeforeEach
    public void setup() {
        reverseComparator = new ReverseComparator<>(comparatorMock);
    }

    @Test
    public void testCompare() {
        // Given
        when(comparatorMock.compare(any(), any())).thenReturn(1);

        // When
        int result = reverseComparator.compare("obj1", "obj2");

        // Then
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_Equal() {
        // Given
        when(comparatorMock.compare(any(), any())).thenReturn(0);

        // When
        int result = reverseComparator.compare("obj1", "obj2");

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testCompare_Negative() {
        // Given
        when(comparatorMock.compare(any(), any())).thenReturn(-1);

        // When
        int result = reverseComparator.compare("obj1", "obj2");

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testEquals_SameInstance() {
        // When
        boolean result = reverseComparator.equals(reverseComparator);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_Null() {
        // When
        boolean result = reverseComparator.equals(null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given
        Object differentClass = new Object();

        // When
        boolean result = reverseComparator.equals(differentClass);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_SameClass_DifferentComparator() {
        // Given
        Comparator<String> differentComparator = mock(Comparator.class);
        ReverseComparator<String> reverseComparatorDifferent = new ReverseComparator<>(differentComparator);

        // When
        boolean result = reverseComparator.equals(reverseComparatorDifferent);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_SameClass_SameComparator() {
        // Given
        ReverseComparator<String> reverseComparatorSame = new ReverseComparator<>(comparatorMock);

        // When
        boolean result = reverseComparator.equals(reverseComparatorSame);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        // Given
        int expectedHashCode = "ReverseComparator".hashCode() ^ comparatorMock.hashCode();

        // When
        int result = reverseComparator.hashCode();

        // Then
        assertEquals(expectedHashCode, result);
    }

    @Test
    public void testConstructor_NoComparator() {
        // When
        ReverseComparator<String> reverseComparatorNoComparator = new ReverseComparator<>();

        // Then
        assertEquals(ComparatorUtils.naturalComparator(), reverseComparatorNoComparator.comparator);
    }

    @Test
    public void testConstructor_WithComparator() {
        // When
        ReverseComparator<String> reverseComparatorWithComparator = new ReverseComparator<>(comparatorMock);

        // Then
        assertEquals(comparatorMock, reverseComparatorWithComparator.comparator);
    }
}