import org.apache.commons.collections4.bloomfilter.Hasher;
import org.apache.commons.collections4.bloomfilter.IndexExtractor;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HasherTest {

    @Mock
    private Hasher hasher;

    @Mock
    private Shape shape;

    @Mock
    private IndexExtractor indexExtractor;

    @BeforeEach
    public void setup() {
        // No setup needed
    }

    @Test
    public void testIndices_Success() {
        // Given: a valid shape
        when(hasher.indices(any(Shape.class))).thenReturn(indexExtractor);

        // When: indices are requested
        IndexExtractor result = hasher.indices(shape);

        // Then: the result is not null
        assertNotNull(result);

        // Then: the correct method was called
        verify(hasher, times(1)).indices(shape);
    }

    @Test
    public void testIndices_NullShape() {
        // Given: a null shape
        assertThrows(NullPointerException.class, () -> hasher.indices(null));

        // Then: the correct exception was thrown
        verify(hasher, never()).indices(any(Shape.class));
    }

    @Test
    public void testIndices_InvalidShape() {
        // Given: an invalid shape (e.g., not properly initialized)
        when(shape.isValid()).thenReturn(false);

        // When / Then: an exception is expected
        assertThrows(IllegalArgumentException.class, () -> hasher.indices(shape));

        // Then: the correct method was called
        verify(hasher, times(1)).indices(shape);
    }

    @Test
    public void testIndices_Deterministic() {
        // Given: a valid shape
        when(hasher.indices(any(Shape.class))).thenReturn(indexExtractor);

        // When: indices are requested twice with the same shape
        IndexExtractor result1 = hasher.indices(shape);
        IndexExtractor result2 = hasher.indices(shape);

        // Then: the results are the same
        assertEquals(result1, result2);

        // Then: the correct method was called twice
        verify(hasher, times(2)).indices(shape);
    }
}