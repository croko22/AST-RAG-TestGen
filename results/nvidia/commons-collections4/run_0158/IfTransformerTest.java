import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.IfTransformer;
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
public class IfTransformerTest {

    @Mock
    private Predicate<Object> predicate;

    @Mock
    private Transformer<Object, Object> trueTransformer;

    @Mock
    private Transformer<Object, Object> falseTransformer;

    private IfTransformer<Object, Object> ifTransformer;

    @BeforeEach
    public void setup() {
        ifTransformer = new IfTransformer<>(predicate, trueTransformer, falseTransformer);
    }

    @Test
    public void testIfTransformer_PredicateTrue() {
        // Given
        when(predicate.test(any())).thenReturn(true);
        Object input = new Object();
        Object expectedOutput = new Object();
        when(trueTransformer.transform(input)).thenReturn(expectedOutput);

        // When
        Object output = ifTransformer.transform(input);

        // Then
        assertEquals(expectedOutput, output);
        verify(predicate, times(1)).test(input);
        verify(trueTransformer, times(1)).transform(input);
        verify(falseTransformer, never()).transform(input);
    }

    @Test
    public void testIfTransformer_PredicateFalse() {
        // Given
        when(predicate.test(any())).thenReturn(false);
        Object input = new Object();
        Object expectedOutput = new Object();
        when(falseTransformer.transform(input)).thenReturn(expectedOutput);

        // When
        Object output = ifTransformer.transform(input);

        // Then
        assertEquals(expectedOutput, output);
        verify(predicate, times(1)).test(input);
        verify(trueTransformer, never()).transform(input);
        verify(falseTransformer, times(1)).transform(input);
    }

    @Test
    public void testIfTransformer_StaticFactoryMethod() {
        // Given
        Predicate<Object> predicate = mock(Predicate.class);
        Transformer<Object, Object> trueTransformer = mock(Transformer.class);
        Transformer<Object, Object> falseTransformer = mock(Transformer.class);

        // When
        IfTransformer<Object, Object> ifTransformer = IfTransformer.ifTransformer(predicate, trueTransformer, falseTransformer);

        // Then
        assertNotNull(ifTransformer);
        assertEquals(predicate, ifTransformer.getPredicate());
        assertEquals(trueTransformer, ifTransformer.getTrueTransformer());
        assertEquals(falseTransformer, ifTransformer.getFalseTransformer());
    }

    @Test
    public void testIfTransformer_StaticFactoryMethodWithDefaultFalseTransformer() {
        // Given
        Predicate<Object> predicate = mock(Predicate.class);
        Transformer<Object, Object> trueTransformer = mock(Transformer.class);

        // When
        IfTransformer<Object, Object> ifTransformer = IfTransformer.ifTransformer(predicate, trueTransformer);

        // Then
        assertNotNull(ifTransformer);
        assertEquals(predicate, ifTransformer.getPredicate());
        assertEquals(trueTransformer, ifTransformer.getTrueTransformer());
        assertNotNull(ifTransformer.getFalseTransformer());
    }

    @Test
    public void testGetFalseTransformer() {
        // Given
        Transformer<Object, Object> falseTransformer = mock(Transformer.class);
        IfTransformer<Object, Object> ifTransformer = new IfTransformer<>(predicate, trueTransformer, falseTransformer);

        // When
        Transformer<Object, Object> result = ifTransformer.getFalseTransformer();

        // Then
        assertEquals(falseTransformer, result);
    }

    @Test
    public void testGetPredicate() {
        // Given
        Predicate<Object> predicate = mock(Predicate.class);
        IfTransformer<Object, Object> ifTransformer = new IfTransformer<>(predicate, trueTransformer, falseTransformer);

        // When
        Predicate<Object> result = ifTransformer.getPredicate();

        // Then
        assertEquals(predicate, result);
    }

    @Test
    public void testGetTrueTransformer() {
        // Given
        Transformer<Object, Object> trueTransformer = mock(Transformer.class);
        IfTransformer<Object, Object> ifTransformer = new IfTransformer<>(predicate, trueTransformer, falseTransformer);

        // When
        Transformer<Object, Object> result = ifTransformer.getTrueTransformer();

        // Then
        assertEquals(trueTransformer, result);
    }
}