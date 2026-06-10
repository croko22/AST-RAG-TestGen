import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.comparators.BooleanComparator;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.collections4.comparators.NullComparator;
import org.apache.commons.collections4.comparators.ReverseComparator;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ComparatorUtilsTest {

    @Mock
    private Comparator<String> comparatorMock;

    @BeforeEach
    public void setup() {
        // Setup mock behavior
        doReturn(0).when(comparatorMock).compare(any(), any());
    }

    @Test
    public void testBooleanComparator_TrueFirst() {
        // Given
        boolean trueFirst = true;

        // When
        Comparator<Boolean> booleanComparator = ComparatorUtils.booleanComparator(trueFirst);

        // Then
        assertNotNull(booleanComparator);
        assertEquals(-1, booleanComparator.compare(true, false));
    }

    @Test
    public void testBooleanComparator_FalseFirst() {
        // Given
        boolean trueFirst = false;

        // When
        Comparator<Boolean> booleanComparator = ComparatorUtils.booleanComparator(trueFirst);

        // Then
        assertNotNull(booleanComparator);
        assertEquals(1, booleanComparator.compare(true, false));
    }

    @Test
    public void testChainedComparator_Collection() {
        // Given
        Comparator<String> comparator1 = mock(Comparator.class);
        Comparator<String> comparator2 = mock(Comparator.class);
        Comparator<String>[] comparators = new Comparator[]{comparator1, comparator2};

        // When
        Comparator<String> chainedComparator = ComparatorUtils.chainedComparator(Arrays.asList(comparators));

        // Then
        assertNotNull(chainedComparator);
        assertTrue(chainedComparator instanceof ComparatorChain);
    }

    @Test
    public void testChainedComparator_Varargs() {
        // Given
        Comparator<String> comparator1 = mock(Comparator.class);
        Comparator<String> comparator2 = mock(Comparator.class);

        // When
        Comparator<String> chainedComparator = ComparatorUtils.chainedComparator(comparator1, comparator2);

        // Then
        assertNotNull(chainedComparator);
        assertTrue(chainedComparator instanceof ComparatorChain);
    }

    @Test
    public void testMax() {
        // Given
        String o1 = "apple";
        String o2 = "banana";
        Comparator<String> comparator = mock(Comparator.class);
        doReturn(1).when(comparator).compare(o1, o2);

        // When
        String max = ComparatorUtils.max(o1, o2, comparator);

        // Then
        assertEquals(o1, max);
    }

    @Test
    public void testMin() {
        // Given
        String o1 = "apple";
        String o2 = "banana";
        Comparator<String> comparator = mock(Comparator.class);
        doReturn(-1).when(comparator).compare(o1, o2);

        // When
        String min = ComparatorUtils.min(o1, o2, comparator);

        // Then
        assertEquals(o1, min);
    }

    @Test
    public void testNaturalComparator() {
        // When
        Comparator<String> naturalComparator = ComparatorUtils.naturalComparator();

        // Then
        assertNotNull(naturalComparator);
        assertTrue(naturalComparator instanceof ComparableComparator);
    }

    @Test
    public void testNullHighComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);

        // When
        Comparator<String> nullHighComparator = ComparatorUtils.nullHighComparator(comparator);

        // Then
        assertNotNull(nullHighComparator);
        assertTrue(nullHighComparator instanceof NullComparator);
    }

    @Test
    public void testNullLowComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);

        // When
        Comparator<String> nullLowComparator = ComparatorUtils.nullLowComparator(comparator);

        // Then
        assertNotNull(nullLowComparator);
        assertTrue(nullLowComparator instanceof NullComparator);
    }

    @Test
    public void testReversedComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);

        // When
        Comparator<String> reversedComparator = ComparatorUtils.reversedComparator(comparator);

        // Then
        assertNotNull(reversedComparator);
        assertTrue(reversedComparator instanceof ReverseComparator);
    }

    @Test
    public void testTransformedComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);

        // When
        Comparator<String> transformedComparator = ComparatorUtils.transformedComparator(comparator, o -> o);

        // Then
        assertNotNull(transformedComparator);
        assertTrue(transformedComparator instanceof TransformingComparator);
    }

    @Test
    public void testChainedComparator_NullCollection() {
        // Given
        Collection<Comparator<String>> collection = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> ComparatorUtils.chainedComparator(collection));
    }

    @Test
    public void testChainedComparator_NullVarargs() {
        // Given
        Comparator<String> comparator = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> ComparatorUtils.chainedComparator(comparator));
    }

    @Test
    public void testNullHighComparator_NullComparator() {
        // Given
        Comparator<String> comparator = null;

        // When
        Comparator<String> nullHighComparator = ComparatorUtils.nullHighComparator(comparator);

        // Then
        assertNotNull(nullHighComparator);
        assertTrue(nullHighComparator instanceof NullComparator);
    }

    @Test
    public void testNullLowComparator_NullComparator() {
        // Given
        Comparator<String> comparator = null;

        // When
        Comparator<String> nullLowComparator = ComparatorUtils.nullLowComparator(comparator);

        // Then
        assertNotNull(nullLowComparator);
        assertTrue(nullLowComparator instanceof NullComparator);
    }

    @Test
    public void testReversedComparator_NullComparator() {
        // Given
        Comparator<String> comparator = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> ComparatorUtils.reversedComparator(comparator));
    }

    @Test
    public void testTransformedComparator_NullComparator() {
        // Given
        Comparator<String> comparator = null;

        // When
        Comparator<String> transformedComparator = ComparatorUtils.transformedComparator(comparator, o -> o);

        // Then
        assertNotNull(transformedComparator);
        assertTrue(transformedComparator instanceof TransformingComparator);
    }
}