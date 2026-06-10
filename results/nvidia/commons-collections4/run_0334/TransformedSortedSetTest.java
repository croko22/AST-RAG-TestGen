import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.set.TransformedSortedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformedSortedSetTest {

    @Mock
    private SortedSet<String> sortedSet;

    @Mock
    private Transformer<String, String> transformer;

    private TransformedSortedSet<String> transformedSortedSet;

    @BeforeEach
    void setup() {
        transformedSortedSet = TransformedSortedSet.transformingSortedSet(sortedSet, transformer);
    }

    @Test
    void testTransformedSortedSet() {
        // Given
        when(transformer.apply(any())).thenReturn("transformed");

        // When
        TransformedSortedSet<String> result = TransformedSortedSet.transformedSortedSet(sortedSet, transformer);

        // Then
        assertNotNull(result);
        assertNotSame(sortedSet, result);
    }

    @Test
    void testTransformingSortedSet() {
        // Given
        when(transformer.apply(any())).thenReturn("transformed");

        // When
        TransformedSortedSet<String> result = TransformedSortedSet.transformingSortedSet(sortedSet, transformer);

        // Then
        assertNotNull(result);
        assertNotSame(sortedSet, result);
    }

    @Test
    void testComparator() {
        // Given
        Comparator<String> comparator = mock(Comparator.class);
        when(sortedSet.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = transformedSortedSet.comparator();

        // Then
        assertNotNull(result);
        assertSame(comparator, result);
    }

    @Test
    void testFirst() {
        // Given
        String first = "first";
        when(sortedSet.first()).thenReturn(first);

        // When
        String result = transformedSortedSet.first();

        // Then
        assertNotNull(result);
        assertEquals(first, result);
    }

    @Test
    void testHeadSet() {
        // Given
        String toElement = "toElement";
        SortedSet<String> headSet = mock(SortedSet.class);
        when(sortedSet.headSet(toElement)).thenReturn(headSet);

        // When
        SortedSet<String> result = transformedSortedSet.headSet(toElement);

        // Then
        assertNotNull(result);
        assertNotSame(headSet, result);
    }

    @Test
    void testLast() {
        // Given
        String last = "last";
        when(sortedSet.last()).thenReturn(last);

        // When
        String result = transformedSortedSet.last();

        // Then
        assertNotNull(result);
        assertEquals(last, result);
    }

    @Test
    void testSubSet() {
        // Given
        String fromElement = "fromElement";
        String toElement = "toElement";
        SortedSet<String> subSet = mock(SortedSet.class);
        when(sortedSet.subSet(fromElement, toElement)).thenReturn(subSet);

        // When
        SortedSet<String> result = transformedSortedSet.subSet(fromElement, toElement);

        // Then
        assertNotNull(result);
        assertNotSame(subSet, result);
    }

    @Test
    void testTailSet() {
        // Given
        String fromElement = "fromElement";
        SortedSet<String> tailSet = mock(SortedSet.class);
        when(sortedSet.tailSet(fromElement)).thenReturn(tailSet);

        // When
        SortedSet<String> result = transformedSortedSet.tailSet(fromElement);

        // Then
        assertNotNull(result);
        assertNotSame(tailSet, result);
    }

    @Test
    void testTransformedSortedSetNullSet() {
        // Given
        SortedSet<String> nullSet = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedSet.transformedSortedSet(nullSet, transformer));
    }

    @Test
    void testTransformedSortedSetNullTransformer() {
        // Given
        Transformer<String, String> nullTransformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedSet.transformedSortedSet(sortedSet, nullTransformer));
    }

    @Test
    void testTransformingSortedSetNullSet() {
        // Given
        SortedSet<String> nullSet = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedSet.transformingSortedSet(nullSet, transformer));
    }

    @Test
    void testTransformingSortedSetNullTransformer() {
        // Given
        Transformer<String, String> nullTransformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedSet.transformingSortedSet(sortedSet, nullTransformer));
    }
}