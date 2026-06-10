import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ExceptionFactory;
import org.apache.commons.collections4.functors.InstantiateFactory;
import org.apache.commons.collections4.functors.PrototypeFactory;
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
public class FactoryUtilsTest {

    @InjectMocks
    private FactoryUtils factoryUtils;

    @Mock
    private ConstantFactory constantFactory;

    @Mock
    private ExceptionFactory exceptionFactory;

    @Mock
    private InstantiateFactory instantiateFactory;

    @Mock
    private PrototypeFactory prototypeFactory;

    @BeforeEach
    void setup() {
        // No setup needed
    }

    @Test
    public void testConstantFactory() {
        // Given
        Object constantToReturn = new Object();

        // When
        Factory<Object> factory = FactoryUtils.constantFactory(constantToReturn);

        // Then
        assertNotNull(factory);
        assertSame(constantToReturn, factory.create());
    }

    @Test
    public void testExceptionFactory() {
        // Given

        // When
        Factory<Object> factory = FactoryUtils.exceptionFactory();

        // Then
        assertNotNull(factory);
        assertThrows(RuntimeException.class, factory::create);
    }

    @Test
    public void testInstantiateFactory() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Given
        Class<?> classToInstantiate = Object.class;
        Constructor<?> constructor = classToInstantiate.getConstructor();

        // When
        Factory<Object> factory = FactoryUtils.instantiateFactory(classToInstantiate);

        // Then
        assertNotNull(factory);
        Object instance = factory.create();
        assertNotNull(instance);
        assertSame(classToInstantiate, instance.getClass());
    }

    @Test
    public void testInstantiateFactory_WithParams() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Given
        Class<?> classToInstantiate = Object.class;
        Class<?>[] paramTypes = new Class<?>[0];
        Object[] args = new Object[0];

        // When
        Factory<Object> factory = FactoryUtils.instantiateFactory(classToInstantiate, paramTypes, args);

        // Then
        assertNotNull(factory);
        Object instance = factory.create();
        assertNotNull(instance);
        assertSame(classToInstantiate, instance.getClass());
    }

    @Test
    public void testNullFactory() {
        // Given

        // When
        Factory<Object> factory = FactoryUtils.nullFactory();

        // Then
        assertNotNull(factory);
        assertNull(factory.create());
    }

    @Test
    public void testPrototypeFactory() {
        // Given
        Object prototype = new Object();

        // When
        Factory<Object> factory = FactoryUtils.prototypeFactory(prototype);

        // Then
        assertNotNull(factory);
        Object instance = factory.create();
        assertNotNull(instance);
        assertNotSame(prototype, instance);
    }

    @Test
    public void testPrototypeFactory_NullPrototype() {
        // Given
        Object prototype = null;

        // When
        Factory<Object> factory = FactoryUtils.prototypeFactory(prototype);

        // Then
        assertNotNull(factory);
        assertNull(factory.create());
    }
}