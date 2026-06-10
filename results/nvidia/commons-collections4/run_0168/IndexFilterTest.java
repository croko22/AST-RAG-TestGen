import org.apache.commons.collections4.bloomfilter.IndexFilter;
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
public class IndexFilterTest {

    @Mock
    private Shape shape;

    @Mock
    private IntPredicate consumer;

    private IndexFilter indexFilter;

    @BeforeEach
    void setup() {
        when(shape.getNumberOfHashFunctions()).thenReturn(10);
        when(shape.getNumberOfBits()).thenReturn(100);
        indexFilter = new IndexFilter(shape, consumer);
    }

    @Test
    void testCreate() {
        // Given
        Shape shape = mock(Shape.class);
        IntPredicate consumer = mock(IntPredicate.class);

        // When
        IntPredicate indexFilter = IndexFilter.create(shape, consumer);

        // Then
        assertNotNull(indexFilter);
    }

    @Test
    void testTest_NumberNotSeen() {
        // Given
        when(consumer.test(anyInt())).thenReturn(true);
        int number = 50;

        // When
        boolean result = indexFilter.test(number);

        // Then
        assertTrue(result);
        verify(consumer, times(1)).test(number);
    }

    @Test
    void testTest_NumberSeen() {
        // Given
        int number = 50;
        indexFilter.test(number); // mark as seen

        // When
        boolean result = indexFilter.test(number);

        // Then
        assertTrue(result);
        verify(consumer, times(1)).test(number);
    }

    @Test
    void testTest_NumberOutOfRange() {
        // Given
        int number = 150;

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> indexFilter.test(number));
    }

    @Test
    void testTest_ConsumerReturnsFalse() {
        // Given
        when(consumer.test(anyInt())).thenReturn(false);
        int number = 50;

        // When
        boolean result = indexFilter.test(number);

        // Then
        assertFalse(result);
        verify(consumer, times(1)).test(number);
    }

    @Test
    void testTest_NullConsumer() {
        // Given
        Shape shape = mock(Shape.class);
        when(shape.getNumberOfHashFunctions()).thenReturn(10);
        when(shape.getNumberOfBits()).thenReturn(100);
        IndexFilter indexFilter = new IndexFilter(shape, null);

        // When and Then
        assertThrows(NullPointerException.class, () -> indexFilter.test(50));
    }

    @Test
    void testTest_NullShape() {
        // Given
        Shape shape = null;
        IntPredicate consumer = mock(IntPredicate.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> new IndexFilter(shape, consumer));
    }
}