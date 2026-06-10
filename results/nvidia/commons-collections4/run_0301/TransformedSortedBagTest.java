import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.TransformedSortedBag;
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
public class TransformedSortedBagTest {

    @Mock
    private SortedBag<String> sortedBag;

    @Mock
    private Transformer<String, String> transformer;

    @Mock
    private Comparator<String> comparator;

    private TransformedSortedBag<String> transformedSortedBag;

    @BeforeEach
    void setup() {
        transformedSortedBag = TransformedSortedBag.transformingSortedBag(sortedBag, transformer);
    }

    @Test
    public void testTransformedSortedBag() {
        // Given
        when(sortedBag.isEmpty()).thenReturn(false);
        String[] values = {"value1", "value2"};
        when(sortedBag.toArray()).thenReturn(values);

        // When
        TransformedSortedBag<String> result = TransformedSortedBag.transformedSortedBag(sortedBag, transformer);

        // Then
        verify(sortedBag, times(1)).isEmpty();
        verify(sortedBag, times(1)).toArray();
        verify(sortedBag, times(1)).clear();
        verify(transformer, times(2)).apply(any());
    }

    @Test
    public void testTransformingSortedBag() {
        // Given
        when(sortedBag.isEmpty()).thenReturn(false);

        // When
        TransformedSortedBag<String> result = TransformedSortedBag.transformingSortedBag(sortedBag, transformer);

        // Then
        verify(sortedBag, times(1)).isEmpty();
        verifyNoMoreInteractions(sortedBag);
    }

    @Test
    public void testComparator() {
        // Given
        when(sortedBag.comparator()).thenReturn(comparator);

        // When
        Comparator<String> result = transformedSortedBag.comparator();

        // Then
        assertEquals(comparator, result);
        verify(sortedBag, times(1)).comparator();
    }

    @Test
    public void testFirst() {
        // Given
        String first = "first";
        when(sortedBag.first()).thenReturn(first);

        // When
        String result = transformedSortedBag.first();

        // Then
        assertEquals(first, result);
        verify(sortedBag, times(1)).first();
    }

    @Test
    public void testLast() {
        // Given
        String last = "last";
        when(sortedBag.last()).thenReturn(last);

        // When
        String result = transformedSortedBag.last();

        // Then
        assertEquals(last, result);
        verify(sortedBag, times(1)).last();
    }

    @Test
    public void testTransformedSortedBagNullBag() {
        // Given
        SortedBag<String> nullBag = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedBag.transformedSortedBag(nullBag, transformer));
    }

    @Test
    public void testTransformedSortedBagNullTransformer() {
        // Given
        Transformer<String, String> nullTransformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedBag.transformedSortedBag(sortedBag, nullTransformer));
    }

    @Test
    public void testTransformingSortedBagNullBag() {
        // Given
        SortedBag<String> nullBag = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedBag.transformingSortedBag(nullBag, transformer));
    }

    @Test
    public void testTransformingSortedBagNullTransformer() {
        // Given
        Transformer<String, String> nullTransformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedSortedBag.transformingSortedBag(sortedBag, nullTransformer));
    }
}