import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.apache.commons.collections4.functors.StringValueTransformer;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.functors.Closure;
import org.apache.commons.collections4.functors.Factory;
import org.apache.commons.collections4.functors.Predicate;
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
public class TransformerUtilsTest {

    @Mock
    private Closure<String> closure;

    @Mock
    private Factory<String> factory;

    @Mock
    private Predicate<String> predicate;

    @BeforeEach
    void setup() {
        // Initialize mocks
        when(closure.execute(any())).thenReturn("Executed");
        when(factory.create()).thenReturn("Created");
        when(predicate.evaluate(any())).thenReturn(true);
    }

    @Test
    void testAsTransformerClosure() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.asTransformer(closure);

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Executed", result);
        verify(closure).execute("Input");
    }

    @Test
    void testAsTransformerFactory() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.asTransformer(factory);

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Created", result);
        verify(factory).create();
    }

    @Test
    void testAsTransformerPredicate() {
        // Given
        Transformer<String, Boolean> transformer = TransformerUtils.asTransformer(predicate);

        // When
        Boolean result = transformer.transform("Input");

        // Then
        assertTrue(result);
        verify(predicate).evaluate("Input");
    }

    @Test
    void testChainedTransformerCollection() {
        // Given
        Collection<Transformer<String, String>> transformers = new ArrayList<>();
        transformers.add(TransformerUtils.constantTransformer("First"));
        transformers.add(TransformerUtils.constantTransformer("Second"));
        Transformer<String, String> transformer = TransformerUtils.chainedTransformer(transformers);

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Second", result);
    }

    @Test
    void testChainedTransformerVarargs() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.chainedTransformer(
                TransformerUtils.constantTransformer("First"),
                TransformerUtils.constantTransformer("Second")
        );

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Second", result);
    }

    @Test
    void testCloneTransformer() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.cloneTransformer();

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Input", result);
    }

    @Test
    void testConstantTransformer() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.constantTransformer("Constant");

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Constant", result);
    }

    @Test
    void testExceptionTransformer() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.exceptionTransformer();

        // When and Then
        assertThrows(RuntimeException.class, () -> transformer.transform("Input"));
    }

    @Test
    void testIfTransformer() {
        // Given
        Transformer<String, String> trueTransformer = TransformerUtils.constantTransformer("True");
        Transformer<String, String> falseTransformer = TransformerUtils.constantTransformer("False");
        Transformer<String, String> transformer = TransformerUtils.ifTransformer(predicate, trueTransformer, falseTransformer);

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("True", result);
        verify(predicate).evaluate("Input");
    }

    @Test
    void testIfTransformerSingle() {
        // Given
        Transformer<String, String> trueTransformer = TransformerUtils.constantTransformer("True");
        Transformer<String, String> transformer = TransformerUtils.ifTransformer(predicate, trueTransformer);

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("True", result);
        verify(predicate).evaluate("Input");
    }

    @Test
    void testInstantiateTransformer() {
        // Given
        Transformer<Class<? extends String>, String> transformer = TransformerUtils.instantiateTransformer();

        // When and Then
        assertThrows(InstantiationException.class, () -> transformer.transform(String.class));
    }

    @Test
    void testInvokerTransformer() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.invokerTransformer("toString");

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Input", result);
    }

    @Test
    void testMapTransformer() {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("Input", "Output");
        Transformer<String, String> transformer = TransformerUtils.mapTransformer(map);

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Output", result);
    }

    @Test
    void testNOPTransformer() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.nopTransformer();

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Input", result);
    }

    @Test
    void testNullTransformer() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.nullTransformer();

        // When
        String result = transformer.transform("Input");

        // Then
        assertNull(result);
    }

    @Test
    void testStringValueTransformer() {
        // Given
        Transformer<String, String> transformer = TransformerUtils.stringValueTransformer();

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Input", result);
    }

    @Test
    void testSwitchMapTransformer() {
        // Given
        Map<String, Transformer<String, String>> map = new HashMap<>();
        map.put("Input", TransformerUtils.constantTransformer("Output"));
        Transformer<String, String> transformer = TransformerUtils.switchMapTransformer(map);

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("Output", result);
    }

    @Test
    void testSwitchTransformer() {
        // Given
        Predicate<String> predicate = mock(Predicate.class);
        when(predicate.evaluate(any())).thenReturn(true);
        Transformer<String, String> trueTransformer = TransformerUtils.constantTransformer("True");
        Transformer<String, String> falseTransformer = TransformerUtils.constantTransformer("False");
        Transformer<String, String> transformer = TransformerUtils.switchTransformer(predicate, trueTransformer, falseTransformer);

        // When
        String result = transformer.transform("Input");

        // Then
        assertEquals("True", result);
        verify(predicate).evaluate("Input");
    }
}