import net.hydromatic.morel.eval.Bound;
import net.hydromatic.morel.util.PairList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoundTest {

    @Mock
    private Comparator<Object> comparatorMock;

    @Mock
    private Consumer<Object> consumerMock;

    @BeforeEach
    void setup() {
        // Setup mock behavior
        when(comparatorMock.compare(any(), any())).thenReturn(0);
    }

    @Test
    public void testInclusive() {
        // Given
        Object value = "test";

        // When
        Bound bound = Bound.inclusive(value);

        // Then
        assertNotNull(bound);
        assertEquals(value, bound.value);
        assertTrue(bound.inclusive);
    }

    @Test
    public void testExclusive() {
        // Given
        Object value = "test";

        // When
        Bound bound = Bound.exclusive(value);

        // Then
        assertNotNull(bound);
        assertEquals(value, bound.value);
        assertFalse(bound.inclusive);
    }

    @Test
    public void testToRange_Unbounded() {
        // Given
        Bound lo = Bound.UNBOUNDED;
        Bound hi = Bound.UNBOUNDED;

        // When
        List<Object> range = Bound.toRange(lo, hi);

        // Then
        assertNotNull(range);
        assertEquals(1, range.size());
    }

    @Test
    public void testToRange_LowerUnbounded() {
        // Given
        Bound lo = Bound.UNBOUNDED;
        Bound hi = Bound.inclusive("test");

        // When
        List<Object> range = Bound.toRange(lo, hi);

        // Then
        assertNotNull(range);
        assertEquals(2, range.size());
    }

    @Test
    public void testToRange_UpperUnbounded() {
        // Given
        Bound lo = Bound.inclusive("test");
        Bound hi = Bound.UNBOUNDED;

        // When
        List<Object> range = Bound.toRange(lo, hi);

        // Then
        assertNotNull(range);
        assertEquals(2, range.size());
    }

    @Test
    public void testToRange_Finite() {
        // Given
        Bound lo = Bound.inclusive("test1");
        Bound hi = Bound.inclusive("test2");

        // When
        List<Object> range = Bound.toRange(lo, hi);

        // Then
        assertNotNull(range);
        assertEquals(3, range.size());
    }

    @Test
    public void testMax() {
        // Given
        Bound bound1 = Bound.inclusive("test1");
        Bound bound2 = Bound.inclusive("test2");

        // When
        Bound max = bound1.max(bound2, comparatorMock);

        // Then
        assertNotNull(max);
        assertEquals(bound2, max);
    }

    @Test
    public void testCompareLower() {
        // Given
        Bound bound1 = Bound.inclusive("test1");
        Bound bound2 = Bound.inclusive("test2");

        // When
        int comparison = bound1.compareLower(bound2, comparatorMock);

        // Then
        assertEquals(-1, comparison);
    }

    @Test
    public void testEnumerate_Discrete() {
        // Given
        Bound lo = Bound.inclusive("test1");
        Bound hi = Bound.inclusive("test2");
        Discrete<Object> discreteMock = mock(Discrete.class);
        when(discreteMock.next(any())).thenReturn("test3");

        // When
        Bound.enumerate(discreteMock, lo, hi, consumerMock);

        // Then
        verify(consumerMock, times(1)).accept(any());
    }

    @Test
    public void testEnumerate_NonDiscrete() {
        // Given
        Bound lo = Bound.inclusive("test1");
        Bound hi = Bound.inclusive("test2");

        // When
        Bound.enumerate(null, lo, hi, consumerMock);

        // Then
        verify(consumerMock, never()).accept(any());
    }

    @Test
    public void testRangeContaining() {
        // Given
        PairList<Bound, Bound> ranges = PairList.of(Bound.inclusive("test1"), Bound.inclusive("test2"));
        Object x = "test1";

        // When
        int index = Bound.rangeContaining(ranges, x, comparatorMock);

        // Then
        assertEquals(0, index);
    }

    @Test
    public void testComplement() {
        // Given
        PairList<Bound, Bound> ranges = PairList.of(Bound.inclusive("test1"), Bound.inclusive("test2"));
        Discrete<Object> discreteMock = mock(Discrete.class);

        // When
        PairList<Bound, Bound> complement = Bound.complement(ranges, discreteMock);

        // Then
        assertNotNull(complement);
    }

    @Test
    public void testFromRanges() {
        // Given
        List<List<?>> ranges = new ArrayList<>();
        ranges.add(List.of("test1", "test2"));
        Comparator<Object> comparatorMock = mock(Comparator.class);
        Discrete<Object> discreteMock = mock(Discrete.class);

        // When
        PairList<Bound, Bound> result = Bound.fromRanges(ranges, comparatorMock, discreteMock);

        // Then
        assertNotNull(result);
    }
}