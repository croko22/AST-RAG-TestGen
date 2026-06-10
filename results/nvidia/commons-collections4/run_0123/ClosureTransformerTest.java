import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClosureTransformerTest {

    @Mock
    private Closure<Object> mockClosure;

    @Test
    public void testClosureTransformer() {
        // Given: a valid closure
        Closure<Object> closure = mockClosure;

        // When: creating a ClosureTransformer using the factory method
        Transformer<Object, Object> transformer = ClosureTransformer.closureTransformer(closure);

        // Then: the transformer is not null and the closure is correctly set
        assertNotNull(transformer);
        assertEquals(closure, ((ClosureTransformer<Object>) transformer).getClosure());
    }

    @Test
    public void testClosureTransformer_NullClosure() {
        // Given: a null closure
        Closure<Object> closure = null;

        // When / Then: an exception is thrown when creating a ClosureTransformer
        assertThrows(NullPointerException.class, () -> ClosureTransformer.closureTransformer(closure));
    }

    @Test
    public void testGetClosure() {
        // Given: a valid closure and a ClosureTransformer
        Closure<Object> closure = mockClosure;
        ClosureTransformer<Object> transformer = new ClosureTransformer<>(closure);

        // When: getting the closure from the transformer
        Closure<Object> result = transformer.getClosure();

        // Then: the result is the same as the original closure
        assertEquals(closure, result);
    }

    @Test
    public void testTransform() {
        // Given: a valid closure, a ClosureTransformer, and an input object
        Closure<Object> closure = mockClosure;
        ClosureTransformer<Object> transformer = new ClosureTransformer<>(closure);
        Object input = new Object();

        // When: transforming the input object using the transformer
        Object result = transformer.transform(input);

        // Then: the result is the same as the input object and the closure was executed
        assertEquals(input, result);
        verify(closure, times(1)).accept(input);
    }

    @Test
    public void testTransform_NullInput() {
        // Given: a valid closure, a ClosureTransformer, and a null input object
        Closure<Object> closure = mockClosure;
        ClosureTransformer<Object> transformer = new ClosureTransformer<>(closure);
        Object input = null;

        // When: transforming the input object using the transformer
        Object result = transformer.transform(input);

        // Then: the result is the same as the input object and the closure was executed
        assertEquals(input, result);
        verify(closure, times(1)).accept(input);
    }
}