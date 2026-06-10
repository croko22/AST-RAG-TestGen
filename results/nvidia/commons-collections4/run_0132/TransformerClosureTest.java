import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPClosure;
import org.apache.commons.collections4.functors.TransformerClosure;
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
public class TransformerClosureTest {

    @Mock
    private Transformer<Object, Object> transformerMock;

    private TransformerClosure<Object> transformerClosure;

    @BeforeEach
    void setup() {
        transformerClosure = new TransformerClosure<>(transformerMock);
    }

    @Test
    public void testTransformerClosure_NullTransformer() {
        // Given: null transformer
        Closure<Object> closure = TransformerClosure.transformerClosure(null);

        // Then: NOPClosure is returned
        assertTrue(closure instanceof NOPClosure);
    }

    @Test
    public void testTransformerClosure_NonNullTransformer() {
        // Given: non-null transformer
        Closure<Object> closure = TransformerClosure.transformerClosure(transformerMock);

        // Then: TransformerClosure is returned
        assertTrue(closure instanceof TransformerClosure);
    }

    @Test
    public void testExecute() {
        // Given: input object
        Object input = new Object();

        // When: execute method is called
        transformerClosure.execute(input);

        // Then: transformer's apply method is called with the input object
        verify(transformerMock, times(1)).apply(input);
    }

    @Test
    public void testGetTransformer() {
        // When: getTransformer method is called
        Transformer<Object, Object> transformer = transformerClosure.getTransformer();

        // Then: the transformer is returned
        assertSame(transformerMock, transformer);
    }

    @Test
    public void testTransformerClosure_StaticMethod() {
        // Given: transformer
        Transformer<Object, Object> transformer = transformerMock;

        // When: transformerClosure method is called
        Closure<Object> closure = TransformerClosure.transformerClosure(transformer);

        // Then: TransformerClosure is returned
        assertTrue(closure instanceof TransformerClosure);
    }
}