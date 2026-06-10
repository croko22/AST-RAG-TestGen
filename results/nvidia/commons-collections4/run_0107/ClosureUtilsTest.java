import org.apache.commons.collections4.ClosureUtils;
import org.apache.commons.collections4.functors.ChainedClosure;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionClosure;
import org.apache.commons.collections4.functors.ForClosure;
import org.apache.commons.collections4.functors.IfClosure;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.NOPClosure;
import org.apache.commons.collections4.functors.SwitchClosure;
import org.apache.commons.collections4.functors.TransformerClosure;
import org.apache.commons.collections4.functors.WhileClosure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClosureUtilsTest {

    @Mock
    private org.apache.commons.collections4.Transformer transformer;

    @Mock
    private org.apache.commons.collections4.Closure closure;

    @Mock
    private org.apache.commons.collections4.Predicate predicate;

    @BeforeEach
    void setup() {
        // Setup mocks
    }

    @Test
    void testAsClosure() {
        // Given
        when(transformer.transform(any())).thenReturn("transformed");

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.asClosure(transformer);

        // Then
        assertNotNull(result);
        verify(transformer, times(1)).transform(any());
    }

    @Test
    void testChainedClosure() {
        // Given
        Collection<org.apache.commons.collections4.Closure> closures = new ArrayList<>();
        closures.add(closure);

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.chainedClosure(closures);

        // Then
        assertNotNull(result);
        verify(closure, times(1)).execute(any());
    }

    @Test
    void testDoWhileClosure() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.doWhileClosure(closure, predicate);

        // Then
        assertNotNull(result);
        verify(closure, times(1)).execute(any());
        verify(predicate, times(1)).evaluate(any());
    }

    @Test
    void testExceptionClosure() {
        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.exceptionClosure();

        // Then
        assertNotNull(result);
        assertThrows(RuntimeException.class, () -> result.execute(null));
    }

    @Test
    void testForClosure() {
        // Given
        when(closure.execute(any())).thenReturn(null);

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.forClosure(1, closure);

        // Then
        assertNotNull(result);
        verify(closure, times(1)).execute(any());
    }

    @Test
    void testIfClosure() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.ifClosure(predicate, closure);

        // Then
        assertNotNull(result);
        verify(closure, times(1)).execute(any());
        verify(predicate, times(1)).evaluate(any());
    }

    @Test
    void testInvokerClosure() {
        // Given
        when(transformer.transform(any())).thenReturn("invoked");

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.invokerClosure("methodName");

        // Then
        assertNotNull(result);
        verify(transformer, times(1)).transform(any());
    }

    @Test
    void testNopClosure() {
        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.nopClosure();

        // Then
        assertNotNull(result);
        result.execute(null);
        verifyNoInteractions(closure);
    }

    @Test
    void testSwitchClosure() {
        // Given
        Map<org.apache.commons.collections4.Predicate, org.apache.commons.collections4.Closure> predicatesAndClosures = new HashMap<>();
        predicatesAndClosures.put(predicate, closure);

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.switchClosure(predicatesAndClosures);

        // Then
        assertNotNull(result);
        verify(closure, times(1)).execute(any());
        verify(predicate, times(1)).evaluate(any());
    }

    @Test
    void testSwitchMapClosure() {
        // Given
        Map<String, org.apache.commons.collections4.Closure> objectsAndClosures = new HashMap<>();
        objectsAndClosures.put("key", closure);

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.switchMapClosure(objectsAndClosures);

        // Then
        assertNotNull(result);
        verify(closure, times(1)).execute(any());
    }

    @Test
    void testWhileClosure() {
        // Given
        when(predicate.evaluate(any())).thenReturn(true);

        // When
        org.apache.commons.collections4.Closure result = ClosureUtils.whileClosure(predicate, closure);

        // Then
        assertNotNull(result);
        verify(closure, times(1)).execute(any());
        verify(predicate, times(1)).evaluate(any());
    }
}