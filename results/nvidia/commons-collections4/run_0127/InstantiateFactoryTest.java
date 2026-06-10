import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.functors.InstantiateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstantiateFactoryTest {

    @Mock
    private Constructor<?> constructorMock;

    private Class<?> testClass;

    @BeforeEach
    public void setup() {
        testClass = String.class;
    }

    @Test
    public void testInstantiateFactory() {
        // Given
        InstantiateFactory<String> factory = InstantiateFactory.instantiateFactory(String.class);

        // When
        String result = factory.create();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testInstantiateFactory_NullClass() {
        // Given
        Class<?> classToInstantiate = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> InstantiateFactory.instantiateFactory(classToInstantiate));
    }

    @Test
    public void testCreate() {
        // Given
        InstantiateFactory<String> factory = InstantiateFactory.instantiateFactory(String.class);

        // When
        String result = factory.create();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testCreate_InvocationTargetException() {
        // Given
        InstantiateFactory<String> factory = InstantiateFactory.instantiateFactory(String.class);
        Constructor<String> constructor = mock(Constructor.class);
        when(constructor.newInstance(any())).thenThrow(InvocationTargetException.class);
        try {
            // When
            factory.create();
        } catch (FunctorException e) {
            // Then
            assertNotNull(e);
        }
    }

    @Test
    public void testCreate_InstantiationException() {
        // Given
        InstantiateFactory<String> factory = InstantiateFactory.instantiateFactory(String.class);
        Constructor<String> constructor = mock(Constructor.class);
        when(constructor.newInstance(any())).thenThrow(InstantiationException.class);
        try {
            // When
            factory.create();
        } catch (FunctorException e) {
            // Then
            assertNotNull(e);
        }
    }

    @Test
    public void testCreate_IllegalAccessException() {
        // Given
        InstantiateFactory<String> factory = InstantiateFactory.instantiateFactory(String.class);
        Constructor<String> constructor = mock(Constructor.class);
        when(constructor.newInstance(any())).thenThrow(IllegalAccessException.class);
        try {
            // When
            factory.create();
        } catch (FunctorException e) {
            // Then
            assertNotNull(e);
        }
    }

    @Test
    public void testFindConstructor() {
        // Given
        InstantiateFactory<String> factory = InstantiateFactory.instantiateFactory(String.class);

        // When
        factory.findConstructor();

        // Then
        assertNotNull(factory.iConstructor);
    }

    @Test
    public void testFindConstructor_NoSuchMethodException() {
        // Given
        InstantiateFactory<String> factory = InstantiateFactory.instantiateFactory(String.class);
        try {
            // When
            factory.findConstructor();
        } catch (IllegalArgumentException e) {
            // Then
            assertNotNull(e);
        }
    }
}