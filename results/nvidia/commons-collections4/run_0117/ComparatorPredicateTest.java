import org.apache.commons.collections4.functors.ComparatorPredicate;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComparatorPredicateTest {

    @Mock
    private Comparator<Integer> comparatorMock;

    private ComparatorPredicate<Integer> comparatorPredicate;

    @BeforeEach
    void setup() {
        comparatorPredicate = new ComparatorPredicate<>(10, comparatorMock, ComparatorPredicate.Criterion.EQUAL);
    }

    @Test
    public void testComparatorPredicate_Object_Comparator() {
        // Given
        Integer object = 10;
        Comparator<Integer> comparator = (o1, o2) -> o1 - o2;

        // When
        Predicate<Integer> predicate = ComparatorPredicate.comparatorPredicate(object, comparator);

        // Then
        assertNotNull(predicate);
    }

    @Test
    public void testComparatorPredicate_Object_Comparator_Criterion() {
        // Given
        Integer object = 10;
        Comparator<Integer> comparator = (o1, o2) -> o1 - o2;
        ComparatorPredicate.Criterion criterion = ComparatorPredicate.Criterion.EQUAL;

        // When
        Predicate<Integer> predicate = ComparatorPredicate.comparatorPredicate(object, comparator, criterion);

        // Then
        assertNotNull(predicate);
    }

    @Test
    public void testTest_Equal() {
        // Given
        when(comparatorMock.compare(any(), any())).thenReturn(0);

        // When
        boolean result = comparatorPredicate.test(10);

        // Then
        assertTrue(result);
        verify(comparatorMock, times(1)).compare(10, 10);
    }

    @Test
    public void testTest_Greater() {
        // Given
        when(comparatorMock.compare(any(), any())).thenReturn(1);
        comparatorPredicate = new ComparatorPredicate<>(10, comparatorMock, ComparatorPredicate.Criterion.GREATER);

        // When
        boolean result = comparatorPredicate.test(10);

        // Then
        assertTrue(result);
        verify(comparatorMock, times(1)).compare(10, 10);
    }

    @Test
    public void testTest_Less() {
        // Given
        when(comparatorMock.compare(any(), any())).thenReturn(-1);
        comparatorPredicate = new ComparatorPredicate<>(10, comparatorMock, ComparatorPredicate.Criterion.LESS);

        // When
        boolean result = comparatorPredicate.test(10);

        // Then
        assertTrue(result);
        verify(comparatorMock, times(1)).compare(10, 10);
    }

    @Test
    public void testTest_Greater_Or_Equal() {
        // Given
        when(comparatorMock.compare(any(), any())).thenReturn(0);
        comparatorPredicate = new ComparatorPredicate<>(10, comparatorMock, ComparatorPredicate.Criterion.GREATER_OR_EQUAL);

        // When
        boolean result = comparatorPredicate.test(10);

        // Then
        assertTrue(result);
        verify(comparatorMock, times(1)).compare(10, 10);
    }

    @Test
    public void testTest_Less_Or_Equal() {
        // Given
        when(comparatorMock.compare(any(), any())).thenReturn(0);
        comparatorPredicate = new ComparatorPredicate<>(10, comparatorMock, ComparatorPredicate.Criterion.LESS_OR_EQUAL);

        // When
        boolean result = comparatorPredicate.test(10);

        // Then
        assertTrue(result);
        verify(comparatorMock, times(1)).compare(10, 10);
    }

    @Test
    public void testTest_Invalid_Criterion() {
        // Given
        comparatorPredicate = new ComparatorPredicate<>(10, comparatorMock, null);

        // When and Then
        assertThrows(NullPointerException.class, () -> comparatorPredicate.test(10));
    }
}