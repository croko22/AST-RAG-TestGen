import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
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
public class PredicatedSortedBagTest {

    @Mock
    private SortedBag<String> sortedBag;

    @Mock
    private Predicate<String> predicate;

    private PredicatedSortedBag<String> predicatedSortedBag;

    @BeforeEach
    void setup() {
        predicatedSortedBag = new PredicatedSortedBag<>(sortedBag, predicate);
    }

    @Test
    public void testPredicatedSortedBag() {
        // Given
        when(sortedBag.comparator()).thenReturn(Comparator.naturalOrder());
        when(predicate.evaluate(any())).thenReturn(true);

        // When
        PredicatedSortedBag<String> result = PredicatedSortedBag.predicatedSortedBag(sortedBag, predicate);

        // Then
        assertNotNull(result);
        assertSame(sortedBag, result.decorated());
        assertSame(predicate, result.getPredicate());
    }

    @Test
    public void testPredicatedSortedBag_NullBag() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedSortedBag.predicatedSortedBag(null, predicate));
    }

    @Test
    public void testPredicatedSortedBag_NullPredicate() {
        // Given
        when(sortedBag.comparator()).thenReturn(Comparator.naturalOrder());

        // When / Then
        assertThrows(NullPointerException.class, () -> PredicatedSortedBag.predicatedSortedBag(sortedBag, null));
    }

    @Test
    public void testComparator() {
        // Given
        Comparator<String> comparator = Comparator.naturalOrder();
        when(sortedBag.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = predicatedSortedBag.comparator();

        // Then
        assertSame(comparator, result);
    }

    @Test
    public void testFirst() {
        // Given
        String first = "first";
        when(sortedBag.first()).thenReturn(first);

        // When
        String result = predicatedSortedBag.first();

        // Then
        assertSame(first, result);
    }

    @Test
    public void testLast() {
        // Given
        String last = "last";
        when(sortedBag.last()).thenReturn(last);

        // When
        String result = predicatedSortedBag.last();

        // Then
        assertSame(last, result);
    }
}