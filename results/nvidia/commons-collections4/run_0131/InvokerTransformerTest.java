import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvokerTransformerTest {

    @InjectMocks
    private InvokerTransformer<Object, Object> invokerTransformer;

    @Mock
    private Method method;

    @BeforeEach
    public void setup() {
        invokerTransformer = new InvokerTransformer<>("methodName");
    }

    @Test
    public void testInvokerTransformer_StaticMethod_NoArgs() {
        // Given
        String methodName = "methodName";

        // When
        Transformer<Object, Object> transformer = InvokerTransformer.invokerTransformer(methodName);

        // Then
        assertNotNull(transformer);
    }

    @Test
    public void testInvokerTransformer_StaticMethod_WithArgs() {
        // Given
        String methodName = "methodName";
        Class<?>[] paramTypes = new Class<?>[]{String.class};
        Object[] args = new Object[]{"arg"};

        // When
        Transformer<Object, Object> transformer = InvokerTransformer.invokerTransformer(methodName, paramTypes, args);

        // Then
        assertNotNull(transformer);
    }

    @Test
    public void testInvokerTransformer_StaticMethod_WithNullMethodName() {
        // Given
        String methodName = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> InvokerTransformer.invokerTransformer(methodName));
    }

    @Test
    public void testInvokerTransformer_StaticMethod_WithInvalidArgs() {
        // Given
        String methodName = "methodName";
        Class<?>[] paramTypes = new Class<?>[]{String.class};
        Object[] args = new Object[]{};

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> InvokerTransformer.invokerTransformer(methodName, paramTypes, args));
    }

    @Test
    public void testTransform_NullInput() {
        // Given
        Object input = null;

        // When
        Object result = invokerTransformer.transform(input);

        // Then
        assertNull(result);
    }

    @Test
    public void testTransform_ExistingMethod() throws Exception {
        // Given
        Object input = mock(Object.class);
        Method method = mock(Method.class);
        when(input.getClass().getMethod("methodName")).thenReturn(method);
        when(method.invoke(input)).thenReturn("result");

        // When
        InvokerTransformer<Object, Object> transformer = new InvokerTransformer<>("methodName");
        Object result = transformer.transform(input);

        // Then
        assertEquals("result", result);
    }

    @Test
    public void testTransform_NonExistingMethod() {
        // Given
        Object input = mock(Object.class);
        when(input.getClass().getMethod("methodName")).thenThrow(NoSuchMethodException.class);

        // When and Then
        assertThrows(FunctorException.class, () -> invokerTransformer.transform(input));
    }

    @Test
    public void testTransform_IllegalAccess() {
        // Given
        Object input = mock(Object.class);
        when(input.getClass().getMethod("methodName")).thenThrow(IllegalAccessException.class);

        // When and Then
        assertThrows(FunctorException.class, () -> invokerTransformer.transform(input));
    }

    @Test
    public void testTransform_InvocationTargetException() {
        // Given
        Object input = mock(Object.class);
        Method method = mock(Method.class);
        when(input.getClass().getMethod("methodName")).thenReturn(method);
        when(method.invoke(input)).thenThrow(InvocationTargetException.class);

        // When and Then
        assertThrows(FunctorException.class, () -> invokerTransformer.transform(input));
    }
}