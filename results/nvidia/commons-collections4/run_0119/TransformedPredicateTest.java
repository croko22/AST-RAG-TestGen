import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.TransformedPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformedPredicateTest {

    @Mock
    private Transformer<String, String> transformer;

    @Mock
    private Predicate<String> predicate;

    private TransformedPredicate<String> transformedPredicate;

    @BeforeEach
    public void setup() {
        transformedPredicate = new TransformedPredicate<>(transformer, predicate);
    }

    @Test
    public void testTransformedPredicate_StaticMethod() {
        // Given
        Transformer<String, String> transformer = mock(Transformer.class);
        Predicate<String> predicate = mock(Predicate.class);

        // When
        TransformedPredicate<String> result = TransformedPredicate.transformedPredicate(transformer, predicate);

        // Then
        assertNotNull(result);
        assertSame(transformer, result.getTransformer());
        assertSame(predicate, result.getPredicates()[0]);
    }

    @Test
    public void testGetTransformer() {
        // When
        Transformer<String, String> result = transformedPredicate.getTransformer();

        // Then
        assertSame(transformer, result);
    }

    @Test
    public void testTest_PredicateReturnsTrue() {
        // Given
        String input = "input";
        String transformedInput = "transformedInput";
        when(transformer.apply(any())).thenReturn(transformedInput);
        when(predicate.test(any())).thenReturn(true);

        // When
        boolean result = transformedPredicate.test(input);

        // Then
        assertTrue(result);
        verify(transformer, times(1)).apply(input);
        verify(predicate, times(1)).test(transformedInput);
    }

    @Test
    public void testTest_PredicateReturnsFalse() {
        // Given
        String input = "input";
        String transformedInput = "transformedInput";
        when(transformer.apply(any())).thenReturn(transformedInput);
        when(predicate.test(any())).thenReturn(false);

        // When
        boolean result = transformedPredicate.test(input);

        // Then
        assertFalse(result);
        verify(transformer, times(1)).apply(input);
        verify(predicate, times(1)).test(transformedInput);
    }

    @Test
    public void testTest_TransformerThrowsException() {
        // Given
        String input = "input";
        when(transformer.apply(any())).thenThrow(new RuntimeException("Transformer exception"));

        // When
        assertThrows(RuntimeException.class, () -> transformedPredicate.test(input));

        // Then
        verify(transformer, times(1)).apply(input);
        verify(predicate, never()).test(any());
    }

    @Test
    public void testTest_PredicateThrowsException() {
        // Given
        String input = "input";
        String transformedInput = "transformedInput";
        when(transformer.apply(any())).thenReturn(transformedInput);
        when(predicate.test(any())).thenThrow(new RuntimeException("Predicate exception"));

        // When
        assertThrows(RuntimeException.class, () -> transformedPredicate.test(input));

        // Then
        verify(transformer, times(1)).apply(input);
        verify(predicate, times(1)).test(transformedInput);
    }
}