import org.apache.commons.collections4.bloomfilter.BitMapExtractor;
import org.apache.commons.collections4.bloomfilter.IndexExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.LongPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BitMapExtractorTest {

    @Mock
    private IndexExtractor indexExtractor;

    @Mock
    private LongPredicate longPredicate;

    @Mock
    private LongBiPredicate longBiPredicate;

    private BitMapExtractor bitMapExtractor;

    @BeforeEach
    void setup() {
        bitMapExtractor = BitMapExtractor.fromBitMapArray(1L, 2L, 3L);
    }

    @Test
    void testFromBitMapArray() {
        // Given
        long[] bitMaps = {1L, 2L, 3L};

        // When
        BitMapExtractor extractor = BitMapExtractor.fromBitMapArray(bitMaps);

        // Then
        assertNotNull(extractor);
        assertArrayEquals(bitMaps, extractor.asBitMapArray());
    }

    @Test
    void testFromIndexExtractor() {
        // Given
        int numberOfBits = 10;

        // When
        BitMapExtractor extractor = BitMapExtractor.fromIndexExtractor(indexExtractor, numberOfBits);

        // Then
        assertNotNull(extractor);
    }

    @Test
    void testAsBitMapArray() {
        // Given
        long[] expected = {1L, 2L, 3L};

        // When
        long[] actual = bitMapExtractor.asBitMapArray();

        // Then
        assertArrayEquals(expected, actual);
    }

    @Test
    void testProcessBitMapPairs() {
        // Given
        BitMapExtractor other = BitMapExtractor.fromBitMapArray(4L, 5L, 6L);

        // When
        boolean result = bitMapExtractor.processBitMapPairs(other, longBiPredicate);

        // Then
        assertTrue(result);
        verify(longBiPredicate, times(3)).test(anyLong(), anyLong());
    }

    @Test
    void testProcessBitMaps() {
        // Given
        when(longPredicate.test(anyLong())).thenReturn(true);

        // When
        boolean result = bitMapExtractor.processBitMaps(longPredicate);

        // Then
        assertTrue(result);
        verify(longPredicate, times(3)).test(anyLong());
    }

    @Test
    void testProcessBitMaps_False() {
        // Given
        when(longPredicate.test(anyLong())).thenReturn(false);

        // When
        boolean result = bitMapExtractor.processBitMaps(longPredicate);

        // Then
        assertFalse(result);
        verify(longPredicate, times(1)).test(anyLong());
    }

    @Test
    void testProcessBitMaps_NullPredicate() {
        // When and Then
        assertThrows(NullPointerException.class, () -> bitMapExtractor.processBitMaps(null));
    }
}