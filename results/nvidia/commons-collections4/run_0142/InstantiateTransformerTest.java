import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstantiateTransformerTest {

    @InjectMocks
    private InstantiateTransformer<Object> instantiateTransformer;

    @Test
    public void testInstantiateTransformer_NoArgInstance() {
        // Given
        Transformer<Class<? extends Object>, Object> transformer = InstantiateTransformer.instantiateTransformer();

        // Then
        assertNotNull(transformer);
    }

    @Test
    public void testInstantiateTransformer_WithParamTypesAndArgs() {
        // Given
        Class<?>[] paramTypes = new Class<?>[]{String.class};
        Object[] args = new Object[]{"test"};

        // When
        Transformer<Class<? extends Object>, Object> transformer = InstantiateTransformer.instantiateTransformer(paramTypes, args);

        // Then
        assertNotNull(transformer);
    }

    @Test
    public void testInstantiateTransformer_WithNullParamTypesAndArgs() {
        // Given
        Class<?>[] paramTypes = null;
        Object[] args = null;

        // When
        Transformer<Class<? extends Object>, Object> transformer = InstantiateTransformer.instantiateTransformer(paramTypes, args);

        // Then
        assertNotNull(transformer);
    }

    @Test
    public void testInstantiateTransformer_WithMismatchedParamTypesAndArgs() {
        // Given
        Class<?>[] paramTypes = new Class<?>[]{String.class};
        Object[] args = new Object[]{};

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> InstantiateTransformer.instantiateTransformer(paramTypes, args));
    }

    @Test
    public void testTransform_NullInput() {
        // Given
        Class<? extends Object> input = null;

        // When and Then
        assertThrows(FunctorException.class, () -> instantiateTransformer.transform(input));
    }

    @Test
    public void testTransform_InvalidConstructor() {
        // Given
        Class<? extends Object> input = String.class;

        // When and Then
        assertThrows(FunctorException.class, () -> instantiateTransformer.transform(input));
    }

    @Test
    public void testTransform_ValidConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Given
        Class<?>[] paramTypes = new Class<?>[]{String.class};
        Object[] args = new Object[]{"test"};
        InstantiateTransformer<Object> transformer = new InstantiateTransformer<>(paramTypes, args);
        Class<? extends Object> input = String.class;
        Constructor<? extends Object> constructor = mock(Constructor.class);
        when(input.getConstructor(paramTypes)).thenReturn(constructor);
        when(constructor.newInstance(args)).thenReturn("test");

        // When
        Object result = transformer.transform(input);

        // Then
        assertNotNull(result);
        assertEquals("test", result);
    }
}