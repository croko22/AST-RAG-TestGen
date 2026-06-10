import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformerPredicateTest {

    @Mock
    private Transformer<Object, Boolean> transformer;

    private TransformerPredicate<Object> transformerPredicate;

    @BeforeEach
    public void setup() {
        transformerPredicate = new TransformerPredicate<>(transformer);
    }

    @Test
    public void testTransformerPredicate() {
        // Given
        Transformer<Object, Boolean> transformer = mock(Transformer.class);
        when(transformer.apply(any())).thenReturn(true);

        // When
        Predicate<Object> predicate = TransformerPredicate.transformerPredicate(transformer);

        // Then
        assertNotNull(predicate);
        assertTrue(predicate.test(new Object()));
        verify(transformer, times(1)).apply(any());
    }

    @Test
    public void testTransformerPredicate_NullTransformer() {
        // Given
        Transformer<Object, Boolean> transformer = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> TransformerPredicate.transformerPredicate(transformer));
    }

    @Test
    public void testGetTransformer() {
        // Given
        Transformer<Object, Boolean> expectedTransformer = transformer;

        // When
        Transformer<Object, Boolean> actualTransformer = transformerPredicate.getTransformer();

        // Then
        assertSame(expectedTransformer, actualTransformer);
    }

    @Test
    public void testTest() {
        // Given
        Object object = new Object();
        when(transformer.apply(any())).thenReturn(true);

        // When
        boolean result = transformerPredicate.test(object);

        // Then
        assertTrue(result);
        verify(transformer, times(1)).apply(any());
    }

    @Test
    public void testTest_NullResult() {
        // Given
        Object object = new Object();
        when(transformer.apply(any())).thenReturn(null);

        // When / Then
        assertThrows(FunctorException.class, () -> transformerPredicate.test(object));
        verify(transformer, times(1)).apply(any());
    }

    @Test
    public void testTest_FalseResult() {
        // Given
        Object object = new Object();
        when(transformer.apply(any())).thenReturn(false);

        // When
        boolean result = transformerPredicate.test(object);

        // Then
        assertFalse(result);
        verify(transformer, times(1)).apply(any());
    }
}