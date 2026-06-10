import org.apache.commons.collections4.bloomfilter.EnhancedDoubleHasher;
import org.apache.commons.collections4.bloomfilter.Hasher;
import org.apache.commons.collections4.bloomfilter.Shape;
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
public class EnhancedDoubleHasherTest {

    @Mock
    private Shape shape;

    private EnhancedDoubleHasher enhancedDoubleHasher;

    @BeforeEach
    void setup() {
        enhancedDoubleHasher = new EnhancedDoubleHasher(1L, 2L);
    }

    @Test
    public void testIndices() {
        // Given
        when(shape.getNumberOfHashFunctions()).thenReturn(5);
        when(shape.getNumberOfBits()).thenReturn(10);

        // When
        var indexExtractor = enhancedDoubleHasher.indices(shape);

        // Then
        assertNotNull(indexExtractor);
        var indices = indexExtractor.asIndexArray();
        assertNotNull(indices);
        assertEquals(5, indices.length);
    }

    @Test
    public void testIndices_ProcessIndices() {
        // Given
        when(shape.getNumberOfHashFunctions()).thenReturn(5);
        when(shape.getNumberOfBits()).thenReturn(10);
        var indexExtractor = enhancedDoubleHasher.indices(shape);
        var consumer = mock(IntPredicate.class);

        // When
        var result = indexExtractor.processIndices(consumer);

        // Then
        assertTrue(result);
        verify(consumer, times(5)).test(anyInt());
    }

    @Test
    public void testIndices_ProcessIndices_ConsumerReturnsFalse() {
        // Given
        when(shape.getNumberOfHashFunctions()).thenReturn(5);
        when(shape.getNumberOfBits()).thenReturn(10);
        var indexExtractor = enhancedDoubleHasher.indices(shape);
        var consumer = mock(IntPredicate.class);
        when(consumer.test(anyInt())).thenReturn(false);

        // When
        var result = indexExtractor.processIndices(consumer);

        // Then
        assertFalse(result);
        verify(consumer, times(1)).test(anyInt());
    }

    @Test
    public void testConstructor_ByteArray() {
        // Given
        var buffer = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

        // When
        var enhancedDoubleHasher = new EnhancedDoubleHasher(buffer);

        // Then
        assertNotNull(enhancedDoubleHasher);
    }

    @Test
    public void testConstructor_ByteArray_Empty() {
        // Given
        var buffer = new byte[]{};

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> new EnhancedDoubleHasher(buffer));
    }

    @Test
    public void testConstructor_LongLong() {
        // Given
        var initial = 1L;
        var increment = 2L;

        // When
        var enhancedDoubleHasher = new EnhancedDoubleHasher(initial, increment);

        // Then
        assertNotNull(enhancedDoubleHasher);
        assertEquals(initial, enhancedDoubleHasher.getInitial());
        assertEquals(increment, enhancedDoubleHasher.getIncrement());
    }
}