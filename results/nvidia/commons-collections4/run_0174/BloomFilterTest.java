import org.apache.commons.collections4.bloomfilter.BloomFilter;
import org.apache.commons.collections4.bloomfilter.BitMapExtractor;
import org.apache.commons.collections4.bloomfilter.Hasher;
import org.apache.commons.collections4.bloomfilter.IndexExtractor;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BloomFilterTest {

    @Mock
    private BloomFilter<?> bloomFilter;

    @Mock
    private BitMapExtractor bitMapExtractor;

    @Mock
    private IndexExtractor indexExtractor;

    @Mock
    private Hasher hasher;

    @Mock
    private Shape shape;

    @BeforeEach
    void setup() {
        when(bloomFilter.getShape()).thenReturn(shape);
    }

    @Test
    void testCardinality() {
        // Given
        int cardinality = 10;
        when(bloomFilter.cardinality()).thenReturn(cardinality);

        // When
        int result = bloomFilter.cardinality();

        // Then
        assertEquals(cardinality, result);
        verify(bloomFilter, times(1)).cardinality();
    }

    @Test
    void testCharacteristics() {
        // Given
        int characteristics = 1;
        when(bloomFilter.characteristics()).thenReturn(characteristics);

        // When
        int result = bloomFilter.characteristics();

        // Then
        assertEquals(characteristics, result);
        verify(bloomFilter, times(1)).characteristics();
    }

    @Test
    void testClear() {
        // When
        bloomFilter.clear();

        // Then
        verify(bloomFilter, times(1)).clear();
    }

    @Test
    void testContainsBitMapExtractor() {
        // Given
        boolean result = true;
        when(bloomFilter.contains(bitMapExtractor)).thenReturn(result);

        // When
        boolean contains = bloomFilter.contains(bitMapExtractor);

        // Then
        assertEquals(result, contains);
        verify(bloomFilter, times(1)).contains(bitMapExtractor);
    }

    @Test
    void testContainsBloomFilter() {
        // Given
        BloomFilter<?> other = mock(BloomFilter.class);
        boolean result = true;
        when(bloomFilter.contains(other)).thenReturn(result);

        // When
        boolean contains = bloomFilter.contains(other);

        // Then
        assertEquals(result, contains);
        verify(bloomFilter, times(1)).contains(other);
    }

    @Test
    void testContainsHasher() {
        // Given
        boolean result = true;
        when(bloomFilter.contains(hasher)).thenReturn(result);

        // When
        boolean contains = bloomFilter.contains(hasher);

        // Then
        assertEquals(result, contains);
        verify(bloomFilter, times(1)).contains(hasher);
    }

    @Test
    void testContainsIndexExtractor() {
        // Given
        boolean result = true;
        when(bloomFilter.contains(indexExtractor)).thenReturn(result);

        // When
        boolean contains = bloomFilter.contains(indexExtractor);

        // Then
        assertEquals(result, contains);
        verify(bloomFilter, times(1)).contains(indexExtractor);
    }

    @Test
    void testCopy() {
        // When
        BloomFilter<?> copy = bloomFilter.copy();

        // Then
        assertNotNull(copy);
        verify(bloomFilter, times(1)).copy();
    }

    @Test
    void testEstimateIntersection() {
        // Given
        BloomFilter<?> other = mock(BloomFilter.class);
        int result = 10;
        when(bloomFilter.estimateIntersection(other)).thenReturn(result);

        // When
        int estimate = bloomFilter.estimateIntersection(other);

        // Then
        assertEquals(result, estimate);
        verify(bloomFilter, times(1)).estimateIntersection(other);
    }

    @Test
    void testEstimateN() {
        // Given
        int result = 10;
        when(bloomFilter.estimateN()).thenReturn(result);

        // When
        int estimate = bloomFilter.estimateN();

        // Then
        assertEquals(result, estimate);
        verify(bloomFilter, times(1)).estimateN();
    }

    @Test
    void testEstimateUnion() {
        // Given
        BloomFilter<?> other = mock(BloomFilter.class);
        int result = 10;
        when(bloomFilter.estimateUnion(other)).thenReturn(result);

        // When
        int estimate = bloomFilter.estimateUnion(other);

        // Then
        assertEquals(result, estimate);
        verify(bloomFilter, times(1)).estimateUnion(other);
    }

    @Test
    void testGetShape() {
        // When
        Shape shape = bloomFilter.getShape();

        // Then
        assertNotNull(shape);
        verify(bloomFilter, times(1)).getShape();
    }

    @Test
    void testIsEmpty() {
        // Given
        boolean result = true;
        when(bloomFilter.isEmpty()).thenReturn(result);

        // When
        boolean empty = bloomFilter.isEmpty();

        // Then
        assertEquals(result, empty);
        verify(bloomFilter, times(1)).isEmpty();
    }

    @Test
    void testIsFull() {
        // Given
        boolean result = true;
        when(bloomFilter.isFull()).thenReturn(result);

        // When
        boolean full = bloomFilter.isFull();

        // Then
        assertEquals(result, full);
        verify(bloomFilter, times(1)).isFull();
    }

    @Test
    void testMergeBitMapExtractor() {
        // Given
        boolean result = true;
        when(bloomFilter.merge(bitMapExtractor)).thenReturn(result);

        // When
        boolean merged = bloomFilter.merge(bitMapExtractor);

        // Then
        assertEquals(result, merged);
        verify(bloomFilter, times(1)).merge(bitMapExtractor);
    }

    @Test
    void testMergeBloomFilter() {
        // Given
        BloomFilter<?> other = mock(BloomFilter.class);
        boolean result = true;
        when(bloomFilter.merge(other)).thenReturn(result);

        // When
        boolean merged = bloomFilter.merge(other);

        // Then
        assertEquals(result, merged);
        verify(bloomFilter, times(1)).merge(other);
    }

    @Test
    void testMergeHasher() {
        // Given
        boolean result = true;
        when(bloomFilter.merge(hasher)).thenReturn(result);

        // When
        boolean merged = bloomFilter.merge(hasher);

        // Then
        assertEquals(result, merged);
        verify(bloomFilter, times(1)).merge(hasher);
    }

    @Test
    void testMergeIndexExtractor() {
        // Given
        boolean result = true;
        when(bloomFilter.merge(indexExtractor)).thenReturn(result);

        // When
        boolean merged = bloomFilter.merge(indexExtractor);

        // Then
        assertEquals(result, merged);
        verify(bloomFilter, times(1)).merge(indexExtractor);
    }

    @Test
    void testUniqueIndices() {
        // When
        IndexExtractor uniqueIndices = bloomFilter.uniqueIndices();

        // Then
        assertNotNull(uniqueIndices);
        verify(bloomFilter, times(1)).uniqueIndices();
    }
}