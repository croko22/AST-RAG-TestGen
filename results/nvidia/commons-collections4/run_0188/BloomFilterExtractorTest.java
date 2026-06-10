import org.apache.commons.collections4.bloomfilter.BloomFilter;
import org.apache.commons.collections4.bloomfilter.BloomFilterExtractor;
import org.apache.commons.collections4.bloomfilter.SimpleBloomFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BloomFilterExtractorTest {

    @Mock
    private BloomFilter bloomFilter1;

    @Mock
    private BloomFilter bloomFilter2;

    @InjectMocks
    private BloomFilterExtractor bloomFilterExtractor;

    private BloomFilterExtractor extractor;

    @BeforeEach
    void setup() {
        extractor = BloomFilterExtractor.fromBloomFilterArray(bloomFilter1, bloomFilter2);
    }

    @Test
    void testAsBloomFilterArray() {
        // Given
        BloomFilter[] expectedFilters = {bloomFilter1, bloomFilter2};

        // When
        BloomFilter[] actualFilters = extractor.asBloomFilterArray();

        // Then
        assertArrayEquals(expectedFilters, actualFilters);
    }

    @Test
    void testProcessBloomFilters() {
        // Given
        Predicate<BloomFilter> predicate = mock(Predicate.class);
        when(predicate.test(bloomFilter1)).thenReturn(true);
        when(predicate.test(bloomFilter2)).thenReturn(true);

        // When
        boolean result = extractor.processBloomFilters(predicate);

        // Then
        assertTrue(result);
        verify(predicate, times(1)).test(bloomFilter1);
        verify(predicate, times(1)).test(bloomFilter2);
    }

    @Test
    void testProcessBloomFilters_False() {
        // Given
        Predicate<BloomFilter> predicate = mock(Predicate.class);
        when(predicate.test(bloomFilter1)).thenReturn(true);
        when(predicate.test(bloomFilter2)).thenReturn(false);

        // When
        boolean result = extractor.processBloomFilters(predicate);

        // Then
        assertFalse(result);
        verify(predicate, times(1)).test(bloomFilter1);
        verify(predicate, times(1)).test(bloomFilter2);
    }

    @Test
    void testFlatten() {
        // Given
        SimpleBloomFilter expectedFilter = new SimpleBloomFilter(10);
        when(bloomFilter1.getShape()).thenReturn(10);
        when(bloomFilter2.getShape()).thenReturn(10);

        // When
        BloomFilter actualFilter = extractor.flatten();

        // Then
        assertNotNull(actualFilter);
        assertEquals(expectedFilter.getShape(), actualFilter.getShape());
    }

    @Test
    void testProcessBloomFilterPair() {
        // Given
        BiPredicate<BloomFilter, BloomFilter> func = mock(BiPredicate.class);
        BloomFilterExtractor otherExtractor = mock(BloomFilterExtractor.class);
        when(func.test(bloomFilter1, any())).thenReturn(true);
        when(func.test(bloomFilter2, any())).thenReturn(true);

        // When
        boolean result = extractor.processBloomFilterPair(otherExtractor, func);

        // Then
        assertTrue(result);
        verify(func, times(1)).test(bloomFilter1, any());
        verify(func, times(1)).test(bloomFilter2, any());
    }

    @Test
    void testProcessBloomFilterPair_False() {
        // Given
        BiPredicate<BloomFilter, BloomFilter> func = mock(BiPredicate.class);
        BloomFilterExtractor otherExtractor = mock(BloomFilterExtractor.class);
        when(func.test(bloomFilter1, any())).thenReturn(true);
        when(func.test(bloomFilter2, any())).thenReturn(false);

        // When
        boolean result = extractor.processBloomFilterPair(otherExtractor, func);

        // Then
        assertFalse(result);
        verify(func, times(1)).test(bloomFilter1, any());
        verify(func, times(1)).test(bloomFilter2, any());
    }
}