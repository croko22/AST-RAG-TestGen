import org.apache.commons.collections4.bloomfilter.LayerManager;
import org.apache.commons.collections4.bloomfilter.BloomFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LayerManagerTest {

    @Mock
    private Supplier<BloomFilter> supplier;

    @Mock
    private Predicate<LayerManager> extendCheck;

    @Mock
    private Consumer<Deque<BloomFilter>> cleanup;

    private LayerManager<BloomFilter> layerManager;

    @BeforeEach
    void setup() {
        layerManager = new LayerManager<>(supplier, extendCheck, cleanup, true);
    }

    @Test
    public void testGetTarget_ExtendCheckReturnsTrue() {
        // Given
        when(extendCheck.test(any())).thenReturn(true);

        // When
        BloomFilter target = layerManager.getTarget();

        // Then
        verify(extendCheck, times(1)).test(any());
        verify(cleanup, times(1)).accept(any());
        assertNotNull(target);
    }

    @Test
    public void testGetTarget_ExtendCheckReturnsFalse() {
        // Given
        when(extendCheck.test(any())).thenReturn(false);

        // When
        BloomFilter target = layerManager.getTarget();

        // Then
        verify(extendCheck, times(1)).test(any());
        verify(cleanup, never()).accept(any());
        assertNotNull(target);
    }

    @Test
    public void testClear() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));

        // When
        layerManager.clear();

        // Then
        verify(supplier, times(1)).get();
        assertEquals(1, layerManager.getDepth());
    }

    @Test
    public void testCopy() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));

        // When
        LayerManager<BloomFilter> copy = layerManager.copy();

        // Then
        assertNotSame(layerManager, copy);
        assertEquals(layerManager.getDepth(), copy.getDepth());
    }

    @Test
    public void testFirst() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));

        // When
        BloomFilter first = layerManager.first();

        // Then
        assertNotNull(first);
    }

    @Test
    public void testGet_DepthInRange() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));
        layerManager = new LayerManager<>(supplier, extendCheck, cleanup, false);
        layerManager.addFilter();

        // When
        BloomFilter filter = layerManager.get(0);

        // Then
        assertNotNull(filter);
    }

    @Test
    public void testGet_DepthOutOfRange() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));
        layerManager = new LayerManager<>(supplier, extendCheck, cleanup, false);

        // When and Then
        assertThrows(NoSuchElementException.class, () -> layerManager.get(0));
    }

    @Test
    public void testGetDepth() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));
        layerManager = new LayerManager<>(supplier, extendCheck, cleanup, false);
        layerManager.addFilter();

        // When
        int depth = layerManager.getDepth();

        // Then
        assertEquals(1, depth);
    }

    @Test
    public void testLast() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));

        // When
        BloomFilter last = layerManager.last();

        // Then
        assertNotNull(last);
    }

    @Test
    public void testNext() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));

        // When
        layerManager.next();

        // Then
        verify(cleanup, times(1)).accept(any());
        assertEquals(2, layerManager.getDepth());
    }

    @Test
    public void testProcessBloomFilters_AllFiltersPass() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));
        layerManager = new LayerManager<>(supplier, extendCheck, cleanup, false);
        layerManager.addFilter();
        Predicate<BloomFilter> predicate = bf -> true;

        // When
        boolean result = layerManager.processBloomFilters(predicate);

        // Then
        assertTrue(result);
    }

    @Test
    public void testProcessBloomFilters_SomeFiltersFail() {
        // Given
        when(supplier.get()).thenReturn(mock(BloomFilter.class));
        layerManager = new LayerManager<>(supplier, extendCheck, cleanup, false);
        layerManager.addFilter();
        Predicate<BloomFilter> predicate = bf -> false;

        // When
        boolean result = layerManager.processBloomFilters(predicate);

        // Then
        assertFalse(result);
    }
}