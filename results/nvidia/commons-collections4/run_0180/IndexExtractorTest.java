import org.apache.commons.collections4.bloomfilter.IndexExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.IntPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IndexExtractorTest {

    @Mock
    private IntPredicate intPredicate;

    private IndexExtractor indexExtractor;

    @BeforeEach
    void setup() {
        indexExtractor = IndexExtractor.fromIndexArray(1, 2, 3, 4, 5);
    }

    @Test
    void testAsIndexArray() {
        // Given
        int[] expected = {1, 2, 3, 4, 5};

        // When
        int[] actual = indexExtractor.asIndexArray();

        // Then
        assertArrayEquals(expected, actual);
    }

    @Test
    void testProcessIndices_PredicateReturnsTrue() {
        // Given
        when(intPredicate.test(anyInt())).thenReturn(true);

        // When
        boolean result = indexExtractor.processIndices(intPredicate);

        // Then
        assertTrue(result);
        verify(intPredicate, times(5)).test(anyInt());
    }

    @Test
    void testProcessIndices_PredicateReturnsFalse() {
        // Given
        when(intPredicate.test(anyInt())).thenReturn(false);

        // When
        boolean result = indexExtractor.processIndices(intPredicate);

        // Then
        assertFalse(result);
        verify(intPredicate, times(1)).test(anyInt());
    }

    @Test
    void testUniqueIndices() {
        // Given
        IndexExtractor uniqueIndexExtractor = indexExtractor.uniqueIndices();

        // When
        int[] uniqueIndices = uniqueIndexExtractor.asIndexArray();

        // Then
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, uniqueIndices);
    }

    @Test
    void testUniqueIndices_DuplicateIndices() {
        // Given
        IndexExtractor indexExtractorWithDuplicates = IndexExtractor.fromIndexArray(1, 2, 2, 3, 3, 3, 4, 5);
        IndexExtractor uniqueIndexExtractor = indexExtractorWithDuplicates.uniqueIndices();

        // When
        int[] uniqueIndices = uniqueIndexExtractor.asIndexArray();

        // Then
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, uniqueIndices);
    }

    @Test
    void testUniqueIndices_Empty() {
        // Given
        IndexExtractor emptyIndexExtractor = IndexExtractor.fromIndexArray();
        IndexExtractor uniqueIndexExtractor = emptyIndexExtractor.uniqueIndices();

        // When
        int[] uniqueIndices = uniqueIndexExtractor.asIndexArray();

        // Then
        assertArrayEquals(new int[]{}, uniqueIndices);
    }

    @Test
    void testFromBitMapExtractor() {
        // Given
        // This test is not applicable as BitMapExtractor is not provided
    }

    @Test
    void testFromIndexArray() {
        // Given
        int[] indices = {1, 2, 3, 4, 5};
        IndexExtractor indexExtractorFromArray = IndexExtractor.fromIndexArray(indices);

        // When
        int[] actual = indexExtractorFromArray.asIndexArray();

        // Then
        assertArrayEquals(indices, actual);
    }

    @Test
    void testNullPredicate() {
        // Given
        IntPredicate nullPredicate = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> indexExtractor.processIndices(nullPredicate));
    }
}