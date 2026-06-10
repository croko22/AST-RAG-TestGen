import org.apache.commons.collections4.bloomfilter.CountingBloomFilter;
import org.apache.commons.collections4.bloomfilter.CellExtractor;
import org.apache.commons.collections4.bloomfilter.BitMapExtractor;
import org.apache.commons.collections4.bloomfilter.IndexExtractor;
import org.apache.commons.collections4.bloomfilter.BloomFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountingBloomFilterTest {

    @Mock
    private CountingBloomFilter countingBloomFilter;

    @Mock
    private CellExtractor cellExtractor;

    @Mock
    private BitMapExtractor bitMapExtractor;

    @Mock
    private IndexExtractor indexExtractor;

    @Mock
    private BloomFilter<?> bloomFilter;

    @BeforeEach
    public void setup() {
        // Initialize mocks
    }

    @Test
    public void testAdd_CellExtractor() {
        // Given
        when(countingBloomFilter.add(cellExtractor)).thenReturn(true);

        // When
        boolean result = countingBloomFilter.add(cellExtractor);

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).add(cellExtractor);
    }

    @Test
    public void testAdd_CellExtractor_False() {
        // Given
        when(countingBloomFilter.add(cellExtractor)).thenReturn(false);

        // When
        boolean result = countingBloomFilter.add(cellExtractor);

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).add(cellExtractor);
    }

    @Test
    public void testGetMaxCell() {
        // Given
        when(countingBloomFilter.getMaxCell()).thenReturn(10);

        // When
        int result = countingBloomFilter.getMaxCell();

        // Then
        assertEquals(10, result);
        verify(countingBloomFilter, times(1)).getMaxCell();
    }

    @Test
    public void testGetMaxInsert_CellExtractor() {
        // Given
        when(countingBloomFilter.getMaxInsert(cellExtractor)).thenReturn(5);

        // When
        int result = countingBloomFilter.getMaxInsert(cellExtractor);

        // Then
        assertEquals(5, result);
        verify(countingBloomFilter, times(1)).getMaxInsert(cellExtractor);
    }

    @Test
    public void testGetMaxInsert_BitMapExtractor() {
        // Given
        when(countingBloomFilter.getMaxInsert(bitMapExtractor)).thenReturn(3);

        // When
        int result = countingBloomFilter.getMaxInsert(bitMapExtractor);

        // Then
        assertEquals(3, result);
        verify(countingBloomFilter, times(1)).getMaxInsert(bitMapExtractor);
    }

    @Test
    public void testGetMaxInsert_BloomFilter() {
        // Given
        when(countingBloomFilter.getMaxInsert(bloomFilter)).thenReturn(2);

        // When
        int result = countingBloomFilter.getMaxInsert(bloomFilter);

        // Then
        assertEquals(2, result);
        verify(countingBloomFilter, times(1)).getMaxInsert(bloomFilter);
    }

    @Test
    public void testIsValid() {
        // Given
        when(countingBloomFilter.isValid()).thenReturn(true);

        // When
        boolean result = countingBloomFilter.isValid();

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).isValid();
    }

    @Test
    public void testIsValid_False() {
        // Given
        when(countingBloomFilter.isValid()).thenReturn(false);

        // When
        boolean result = countingBloomFilter.isValid();

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).isValid();
    }

    @Test
    public void testMerge_BitMapExtractor() {
        // Given
        when(countingBloomFilter.merge(bitMapExtractor)).thenReturn(true);

        // When
        boolean result = countingBloomFilter.merge(bitMapExtractor);

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).merge(bitMapExtractor);
    }

    @Test
    public void testMerge_BitMapExtractor_False() {
        // Given
        when(countingBloomFilter.merge(bitMapExtractor)).thenReturn(false);

        // When
        boolean result = countingBloomFilter.merge(bitMapExtractor);

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).merge(bitMapExtractor);
    }

    @Test
    public void testMerge_BloomFilter() {
        // Given
        when(countingBloomFilter.merge(bloomFilter)).thenReturn(true);

        // When
        boolean result = countingBloomFilter.merge(bloomFilter);

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).merge(bloomFilter);
    }

    @Test
    public void testMerge_BloomFilter_False() {
        // Given
        when(countingBloomFilter.merge(bloomFilter)).thenReturn(false);

        // When
        boolean result = countingBloomFilter.merge(bloomFilter);

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).merge(bloomFilter);
    }

    @Test
    public void testMerge_IndexExtractor() {
        // Given
        when(countingBloomFilter.merge(indexExtractor)).thenReturn(true);

        // When
        boolean result = countingBloomFilter.merge(indexExtractor);

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).merge(indexExtractor);
    }

    @Test
    public void testMerge_IndexExtractor_False() {
        // Given
        when(countingBloomFilter.merge(indexExtractor)).thenReturn(false);

        // When
        boolean result = countingBloomFilter.merge(indexExtractor);

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).merge(indexExtractor);
    }

    @Test
    public void testRemove_BitMapExtractor() {
        // Given
        when(countingBloomFilter.remove(bitMapExtractor)).thenReturn(true);

        // When
        boolean result = countingBloomFilter.remove(bitMapExtractor);

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).remove(bitMapExtractor);
    }

    @Test
    public void testRemove_BitMapExtractor_False() {
        // Given
        when(countingBloomFilter.remove(bitMapExtractor)).thenReturn(false);

        // When
        boolean result = countingBloomFilter.remove(bitMapExtractor);

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).remove(bitMapExtractor);
    }

    @Test
    public void testRemove_BloomFilter() {
        // Given
        when(countingBloomFilter.remove(bloomFilter)).thenReturn(true);

        // When
        boolean result = countingBloomFilter.remove(bloomFilter);

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).remove(bloomFilter);
    }

    @Test
    public void testRemove_BloomFilter_False() {
        // Given
        when(countingBloomFilter.remove(bloomFilter)).thenReturn(false);

        // When
        boolean result = countingBloomFilter.remove(bloomFilter);

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).remove(bloomFilter);
    }

    @Test
    public void testRemove_IndexExtractor() {
        // Given
        when(countingBloomFilter.remove(indexExtractor)).thenReturn(true);

        // When
        boolean result = countingBloomFilter.remove(indexExtractor);

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).remove(indexExtractor);
    }

    @Test
    public void testRemove_IndexExtractor_False() {
        // Given
        when(countingBloomFilter.remove(indexExtractor)).thenReturn(false);

        // When
        boolean result = countingBloomFilter.remove(indexExtractor);

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).remove(indexExtractor);
    }

    @Test
    public void testSubtract_CellExtractor() {
        // Given
        when(countingBloomFilter.subtract(cellExtractor)).thenReturn(true);

        // When
        boolean result = countingBloomFilter.subtract(cellExtractor);

        // Then
        assertTrue(result);
        verify(countingBloomFilter, times(1)).subtract(cellExtractor);
    }

    @Test
    public void testSubtract_CellExtractor_False() {
        // Given
        when(countingBloomFilter.subtract(cellExtractor)).thenReturn(false);

        // When
        boolean result = countingBloomFilter.subtract(cellExtractor);

        // Then
        assertFalse(result);
        verify(countingBloomFilter, times(1)).subtract(cellExtractor);
    }

    @Test
    public void testUniqueIndices() {
        // Given
        when(countingBloomFilter.uniqueIndices()).thenReturn(indexExtractor);

        // When
        IndexExtractor result = countingBloomFilter.uniqueIndices();

        // Then
        assertEquals(indexExtractor, result);
        verify(countingBloomFilter, times(1)).uniqueIndices();
    }
}