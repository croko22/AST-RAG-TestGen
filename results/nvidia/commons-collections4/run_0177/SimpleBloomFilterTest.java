import org.apache.commons.collections4.bloomfilter.SimpleBloomFilter;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimpleBloomFilterTest {

    @Mock
    private Shape shape;

    @InjectMocks
    private SimpleBloomFilter simpleBloomFilter;

    @BeforeEach
    void setup() {
        when(shape.getNumberOfBits()).thenReturn(100);
        when(shape.getNumberOfHashFunctions()).thenReturn(5);
    }

    @Test
    void testCardinality() {
        // Given
        when(shape.getNumberOfBits()).thenReturn(100);

        // When
        int cardinality = simpleBloomFilter.cardinality();

        // Then
        assertTrue(cardinality >= 0);
    }

    @Test
    void testCharacteristics() {
        // Given

        // When
        int characteristics = simpleBloomFilter.characteristics();

        // Then
        assertEquals(0, characteristics);
    }

    @Test
    void testClear() {
        // Given

        // When
        simpleBloomFilter.clear();

        // Then
        assertTrue(simpleBloomFilter.isEmpty());
    }

    @Test
    void testContains() {
        // Given
        IndexExtractor indexExtractor = mock(IndexExtractor.class);
        when(indexExtractor.processIndices(any())).thenReturn(true);

        // When
        boolean contains = simpleBloomFilter.contains(indexExtractor);

        // Then
        assertTrue(contains);
    }

    @Test
    void testCopy() {
        // Given

        // When
        SimpleBloomFilter copy = simpleBloomFilter.copy();

        // Then
        assertNotNull(copy);
        assertNotSame(simpleBloomFilter, copy);
    }

    @Test
    void testGetShape() {
        // Given

        // When
        Shape shape = simpleBloomFilter.getShape();

        // Then
        assertNotNull(shape);
    }

    @Test
    void testIsEmpty() {
        // Given
        when(shape.getNumberOfBits()).thenReturn(0);

        // When
        boolean isEmpty = simpleBloomFilter.isEmpty();

        // Then
        assertTrue(isEmpty);
    }

    @Test
    void testMergeBitMapExtractor() {
        // Given
        BitMapExtractor bitMapExtractor = mock(BitMapExtractor.class);
        when(bitMapExtractor.processBitMaps(any())).thenReturn(true);

        // When
        boolean merge = simpleBloomFilter.merge(bitMapExtractor);

        // Then
        assertTrue(merge);
    }

    @Test
    void testMergeBloomFilter() {
        // Given
        BloomFilter<?> bloomFilter = mock(BloomFilter.class);
        when(bloomFilter.characteristics()).thenReturn(0);

        // When
        boolean merge = simpleBloomFilter.merge(bloomFilter);

        // Then
        assertTrue(merge);
    }

    @Test
    void testMergeHasher() {
        // Given
        Hasher hasher = mock(Hasher.class);
        when(hasher.indices(any())).thenReturn(mock(IndexExtractor.class));

        // When
        boolean merge = simpleBloomFilter.merge(hasher);

        // Then
        assertTrue(merge);
    }

    @Test
    void testMergeIndexExtractor() {
        // Given
        IndexExtractor indexExtractor = mock(IndexExtractor.class);
        when(indexExtractor.processIndices(any())).thenReturn(true);

        // When
        boolean merge = simpleBloomFilter.merge(indexExtractor);

        // Then
        assertTrue(merge);
    }

    @Test
    void testProcessBitMapPairs() {
        // Given
        BitMapExtractor bitMapExtractor = mock(BitMapExtractor.class);
        LongBiPredicate func = mock(LongBiPredicate.class);
        when(bitMapExtractor.processBitMaps(any())).thenReturn(true);

        // When
        boolean process = simpleBloomFilter.processBitMapPairs(bitMapExtractor, func);

        // Then
        assertTrue(process);
    }

    @Test
    void testProcessBitMaps() {
        // Given
        LongPredicate consumer = mock(LongPredicate.class);
        when(consumer.test(anyLong())).thenReturn(true);

        // When
        boolean process = simpleBloomFilter.processBitMaps(consumer);

        // Then
        assertTrue(process);
    }

    @Test
    void testProcessIndices() {
        // Given
        IntPredicate consumer = mock(IntPredicate.class);
        when(consumer.test(anyInt())).thenReturn(true);

        // When
        boolean process = simpleBloomFilter.processIndices(consumer);

        // Then
        assertTrue(process);
    }
}