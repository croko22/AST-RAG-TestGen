import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Predicate as JavaPredicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicateTransformerTest {

    @Mock
    private Predicate<Object> predicate;

    private PredicateTransformer<Object> transformer;

    @BeforeEach
    public void setup() {
        transformer = new PredicateTransformer<>(predicate);
    }

    @Test
    public void testPredicateTransformerFactoryMethod() {
        // Given: a predicate
        Predicate<Object> predicate = mock(Predicate.class);

        // When: the factory method is called
        Transformer<Object, Boolean> transformer = PredicateTransformer.predicateTransformer(predicate);

        // Then: the transformer is created
        assertNotNull(transformer);
        assertSame(predicate, ((PredicateTransformer<Object>) transformer).getPredicate());
    }

    @Test
    public void testPredicateTransformerFactoryMethodNullPredicate() {
        // Given: a null predicate
        Predicate<Object> predicate = null;

        // When / Then: an exception is thrown
        assertThrows(IllegalArgumentException.class, () -> PredicateTransformer.predicateTransformer(predicate));
    }

    @Test
    public void testGetPredicate() {
        // Given: a predicate transformer
        PredicateTransformer<Object> transformer = new PredicateTransformer<>(predicate);

        // When: the predicate is retrieved
        Predicate<Object> retrievedPredicate = transformer.getPredicate();

        // Then: the predicate is the same as the one used to create the transformer
        assertSame(predicate, retrievedPredicate);
    }

    @Test
    public void testTransformTrue() {
        // Given: a predicate that returns true
        when(predicate.test(any())).thenReturn(true);

        // When: the transform method is called
        Boolean result = transformer.transform("input");

        // Then: the result is true
        assertTrue(result);
        verify(predicate, times(1)).test("input");
    }

    @Test
    public void testTransformFalse() {
        // Given: a predicate that returns false
        when(predicate.test(any())).thenReturn(false);

        // When: the transform method is called
        Boolean result = transformer.transform("input");

        // Then: the result is false
        assertFalse(result);
        verify(predicate, times(1)).test("input");
    }
}