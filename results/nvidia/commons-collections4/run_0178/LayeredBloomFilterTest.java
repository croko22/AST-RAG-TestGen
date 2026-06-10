import org.apache.commons.collections4.bloomfilter.LayeredBloomFilter;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LayeredBloomFilterTest {

    @Mock
    private Shape shape;

    @Mock
    private LayerManager layerManager;

    private LayeredBloomFilter layeredBloomFilter;

    @BeforeEach
    void setup() {
        layeredBloomFilter = new LayeredBloomFilter(shape, layerManager);
    }

    @Test
    void testCardinality() {
        // Given
        when(layerManager.processBloomFilters(any())).thenReturn(true);

        // When
        int cardinality = layeredBloomFilter.cardinality();

        // Then
        verify(layerManager, times(1)).processBloomFilters(any());
    }

    @Test
    void testCharacteristics() {
        // When
        int characteristics = layeredBloomFilter.characteristics();

        // Then
        assertEquals(0, characteristics);
    }

    @Test
    void testCleanup() {
        // When
        layeredBloomFilter.cleanup();

        // Then
        verify(layerManager, times(1)).cleanup();
    }

    @Test
    void testClear() {
        // When
        layeredBloomFilter.clear();

        // Then
        verify(layerManager, times(1)).clear();
    }

    @Test
    void testContainsBitMapExtractor() {
        // Given
        BitMapExtractor bitMapExtractor = mock(BitMapExtractor.class);

        // When
        boolean contains = layeredBloomFilter.contains(bitMapExtractor);

        // Then
        verify(layerManager, times(1)).processBloomFilters(any());
    }

    @Test
    void testContainsBloomFilter() {
        // Given
        BloomFilter bloomFilter = mock(BloomFilter.class);

        // When
        boolean contains = layeredBloomFilter.contains(bloomFilter);

        // Then
        verify(layerManager, times(1)).processBloomFilters(any());
    }

    @Test
    void testContainsBloomFilterExtractor() {
        // Given
        BloomFilterExtractor bloomFilterExtractor = mock(BloomFilterExtractor.class);

        // When
        boolean contains = layeredBloomFilter.contains(bloomFilterExtractor);

        // Then
        verify(bloomFilterExtractor, times(1)).processBloomFilters(any());
    }

    @Test
    void testContainsHasher() {
        // Given
        Hasher hasher = mock(Hasher.class);

        // When
        boolean contains = layeredBloomFilter.contains(hasher);

        // Then
        verify(layerManager, times(1)).processBloomFilters(any());
    }

    @Test
    void testContainsIndexExtractor() {
        // Given
        IndexExtractor indexExtractor = mock(IndexExtractor.class);

        // When
        boolean contains = layeredBloomFilter.contains(indexExtractor);

        // Then
        verify(layerManager, times(1)).processBloomFilters(any());
    }

    @Test
    void testCopy() {
        // When
        LayeredBloomFilter copy = layeredBloomFilter.copy();

        // Then
        assertNotNull(copy);
    }

    @Test
    void testEstimateN() {
        // Given
        SimpleBloomFilter simpleBloomFilter = mock(SimpleBloomFilter.class);
        when(layerManager.flatten()).thenReturn(simpleBloomFilter);

        // When
        int estimateN = layeredBloomFilter.estimateN();

        // Then
        verify(simpleBloomFilter, times(1)).estimateN();
    }

    @Test
    void testEstimateUnion() {
        // Given
        BloomFilter bloomFilter = mock(BloomFilter.class);
        SimpleBloomFilter simpleBloomFilter = mock(SimpleBloomFilter.class);
        when(layerManager.flatten()).thenReturn(simpleBloomFilter);

        // When
        int estimateUnion = layeredBloomFilter.estimateUnion(bloomFilter);

        // Then
        verify(simpleBloomFilter, times(1)).merge(bloomFilter);
        verify(simpleBloomFilter, times(1)).estimateN();
    }

    @Test
    void testFlatten() {
        // When
        SimpleBloomFilter simpleBloomFilter = layeredBloomFilter.flatten();

        // Then
        assertNotNull(simpleBloomFilter);
    }

    @Test
    void testGet() {
        // Given
        int depth = 1;

        // When
        BloomFilter bloomFilter = layeredBloomFilter.get(depth);

        // Then
        verify(layerManager, times(1)).get(depth);
    }

    @Test
    void testGetDepth() {
        // When
        int depth = layeredBloomFilter.getDepth();

        // Then
        verify(layerManager, times(1)).getDepth();
    }

    @Test
    void testGetShape() {
        // When
        Shape shape = layeredBloomFilter.getShape();

        // Then
        assertSame(this.shape, shape);
    }

    @Test
    void testIsEmpty() {
        // When
        boolean isEmpty = layeredBloomFilter.isEmpty();

        // Then
        verify(layerManager, times(1)).processBloomFilters(any());
    }

    @Test
    void testMergeBitMapExtractor() {
        // Given
        BitMapExtractor bitMapExtractor = mock(BitMapExtractor.class);

        // When
        boolean merge = layeredBloomFilter.merge(bitMapExtractor);

        // Then
        verify(layerManager, times(1)).getTarget();
    }

    @Test
    void testMergeBloomFilter() {
        // Given
        BloomFilter bloomFilter = mock(BloomFilter.class);

        // When
        boolean merge = layeredBloomFilter.merge(bloomFilter);

        // Then
        verify(layerManager, times(1)).getTarget();
    }

    @Test
    void testMergeIndexExtractor() {
        // Given
        IndexExtractor indexExtractor = mock(IndexExtractor.class);

        // When
        boolean merge = layeredBloomFilter.merge(indexExtractor);

        // Then
        verify(layerManager, times(1)).getTarget();
    }

    @Test
    void testNext() {
        // When
        layeredBloomFilter.next();

        // Then
        verify(layerManager, times(1)).next();
    }

    @Test
    void testProcessBitMaps() {
        // Given
        LongPredicate predicate = mock(LongPredicate.class);
        SimpleBloomFilter simpleBloomFilter = mock(SimpleBloomFilter.class);
        when(layerManager.flatten()).thenReturn(simpleBloomFilter);

        // When
        boolean processBitMaps = layeredBloomFilter.processBitMaps(predicate);

        // Then
        verify(simpleBloomFilter, times(1)).processBitMaps(predicate);
    }

    @Test
    void testProcessBloomFilters() {
        // Given
        Predicate predicate = mock(Predicate.class);

        // When
        boolean processBloomFilters = layeredBloomFilter.processBloomFilters(predicate);

        // Then
        verify(layerManager, times(1)).processBloomFilters(predicate);
    }

    @Test
    void testProcessIndices() {
        // Given
        IntPredicate predicate = mock(IntPredicate.class);

        // When
        boolean processIndices = layeredBloomFilter.processIndices(predicate);

        // Then
        verify(layerManager, times(1)).processBloomFilters(any());
    }
}