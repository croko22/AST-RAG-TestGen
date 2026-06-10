import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
public class ExceptionTransformerTest {

    @Test
    public void testExceptionTransformer() {
        // Given: 
        Transformer<?, ?> transformer = ExceptionTransformer.exceptionTransformer();

        // Then: 
        assertSame(ExceptionTransformer.INSTANCE, transformer);
    }

    @Test
    public void testTransform() {
        // Given: 
        Transformer<?, ?> transformer = ExceptionTransformer.exceptionTransformer();

        // When / Then: 
        FunctorException exception = assertThrows(FunctorException.class, () -> transformer.transform(null));
        assertEquals("ExceptionTransformer invoked", exception.getMessage());
    }

    @Test
    public void testReadResolve() {
        // Given: 
        ExceptionTransformer<?, ?> transformer = new ExceptionTransformer<>();

        // When: 
        Object resolvedTransformer = transformer.readResolve();

        // Then: 
        assertSame(ExceptionTransformer.INSTANCE, resolvedTransformer);
    }
}