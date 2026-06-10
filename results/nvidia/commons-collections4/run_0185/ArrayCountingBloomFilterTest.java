import org.apache.commons.collections4.bloomfilter.ArrayCountingBloomFilter;
import org.apache.commons.collections4.bloomfilter.CellExtractor;
import org.apache.commons.collections4.bloomfilter.CellPredicate;
import org.apache.commons.collections4.bloomfilter.IndexExtractor;
import org.apache.commons.collections4.bloomfilter.LongPredicate;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.IntPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArrayCountingBloomFilterTest {

    @Mock
    private CellExtractor cellExtractor;

    @Mock
    private IndexExtractor indexExtractor;

    @Mock
    private CellPredicate cellPredicate;

    @Mock
    private LongPredicate longPredicate;

    @Mock
    private IntPredicate intPredicate;

    @InjectMocks
    private Shape shape;

    private ArrayCountingBloomFilter arrayCountingBloomFilter;

    @BeforeEach
    void setup() {
        arrayCountingBloomFilter = new ArrayCountingBloomFilter(shape);
    }

    @Test
    void testAdd() {
        // Given
        when(cellExtractor.processCells(any())).thenReturn(true);

        // When
        boolean result = arrayCountingBloomFilter.add(cellExtractor);

        // Then
        assertTrue(result);
        verify(cellExtractor, times(1)).processCells(any());
    }

    @Test
    void testAdd_InvalidState() {
        // Given
        when(cellExtractor.processCells(any())).thenReturn(false);

        // When
        boolean result = arrayCountingBloomFilter.add(cellExtractor);

        // Then
        assertFalse(result);
        verify(cellExtractor, times(1)).processCells(any());
    }

    @Test
    void testCardinality() {
        // Given
        int[] cells = {1, 2, 3, 0, 0};
        arrayCountingBloomFilter = new ArrayCountingBloomFilter(shape) {
            @Override
            public int[] getCells() {
                return cells;
            }
        };

        // When
        int result = arrayCountingBloomFilter.cardinality();

        // Then
        assertEquals(3, result);
    }

    @Test
    void testCharacteristics() {
        // Given

        // When
        int result = arrayCountingBloomFilter.characteristics();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testClear() {
        // Given
        int[] cells = {1, 2, 3, 0, 0};
        arrayCountingBloomFilter = new ArrayCountingBloomFilter(shape) {
            @Override
            public int[] getCells() {
                return cells;
            }
        };

        // When
        arrayCountingBloomFilter.clear();

        // Then
        assertArrayEquals(new int[]{0, 0, 0, 0, 0}, arrayCountingBloomFilter.getCells());
    }

    @Test
    void testContains_BitMapExtractor() {
        // Given
        when(indexExtractor.processIndices(any())).thenReturn(true);

        // When
        boolean result = arrayCountingBloomFilter.contains(indexExtractor);

        // Then
        assertTrue(result);
        verify(indexExtractor, times(1)).processIndices(any());
    }

    @Test
    void testContains_IndexExtractor() {
        // Given
        when(indexExtractor.processIndices(any())).thenReturn(true);

        // When
        boolean result = arrayCountingBloomFilter.contains(indexExtractor);

        // Then
        assertTrue(result);
        verify(indexExtractor, times(1)).processIndices(any());
    }

    @Test
    void testCopy() {
        // Given

        // When
        ArrayCountingBloomFilter result = arrayCountingBloomFilter.copy();

        // Then
        assertNotNull(result);
        assertNotSame(arrayCountingBloomFilter, result);
    }

    @Test
    void testGetMaxCell() {
        // Given

        // When
        int result = arrayCountingBloomFilter.getMaxCell();

        // Then
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    void testGetMaxInsert() {
        // Given
        when(cellExtractor.processCells(any())).thenReturn(true);

        // When
        int result = arrayCountingBloomFilter.getMaxInsert(cellExtractor);

        // Then
        assertEquals(Integer.MAX_VALUE, result);
        verify(cellExtractor, times(1)).processCells(any());
    }

    @Test
    void testGetShape() {
        // Given

        // When
        Shape result = arrayCountingBloomFilter.getShape();

        // Then
        assertNotNull(result);
        assertSame(shape, result);
    }

    @Test
    void testIsValid() {
        // Given

        // When
        boolean result = arrayCountingBloomFilter.isValid();

        // Then
        assertTrue(result);
    }

    @Test
    void testProcessBitMaps() {
        // Given
        when(longPredicate.test(anyLong())).thenReturn(true);

        // When
        boolean result = arrayCountingBloomFilter.processBitMaps(longPredicate);

        // Then
        assertTrue(result);
        verify(longPredicate, times(1)).test(anyLong());
    }

    @Test
    void testProcessCells() {
        // Given
        when(cellPredicate.test(anyInt(), anyInt())).thenReturn(true);

        // When
        boolean result = arrayCountingBloomFilter.processCells(cellPredicate);

        // Then
        assertTrue(result);
        verify(cellPredicate, times(1)).test(anyInt(), anyInt());
    }

    @Test
    void testProcessIndices() {
        // Given
        when(intPredicate.test(anyInt())).thenReturn(true);

        // When
        boolean result = arrayCountingBloomFilter.processIndices(intPredicate);

        // Then
        assertTrue(result);
        verify(intPredicate, times(1)).test(anyInt());
    }

    @Test
    void testSubtract() {
        // Given
        when(cellExtractor.processCells(any())).thenReturn(true);

        // When
        boolean result = arrayCountingBloomFilter.subtract(cellExtractor);

        // Then
        assertTrue(result);
        verify(cellExtractor, times(1)).processCells(any());
    }

    @Test
    void testSubtract_InvalidState() {
        // Given
        when(cellExtractor.processCells(any())).thenReturn(false);

        // When
        boolean result = arrayCountingBloomFilter.subtract(cellExtractor);

        // Then
        assertFalse(result);
        verify(cellExtractor, times(1)).processCells(any());
    }
}