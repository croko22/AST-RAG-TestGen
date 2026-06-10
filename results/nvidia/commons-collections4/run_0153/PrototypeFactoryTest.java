import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.functors.PrototypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Serializable;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrototypeFactoryTest {

    @Mock
    private Method cloneMethod;

    @Mock
    private Method constructorMethod;

    private PrototypeFactory.PrototypeCloneFactory<String> prototypeCloneFactory;

    private PrototypeFactory.PrototypeSerializationFactory<String> prototypeSerializationFactory;

    @BeforeEach
    void setup() {
        prototypeCloneFactory = new PrototypeFactory.PrototypeCloneFactory<>("prototype", cloneMethod);
        prototypeSerializationFactory = new PrototypeFactory.PrototypeSerializationFactory<>("prototype");
    }

    @Test
    void testPrototypeFactory_NullPrototype() {
        // Given: null prototype
        Object prototype = null;

        // When: create prototype factory
        Factory<?> factory = PrototypeFactory.prototypeFactory(prototype);

        // Then: constant factory with null instance
        assertNotNull(factory);
        assertNull(factory.create());
    }

    @Test
    void testPrototypeFactory_CloneMethod() throws Exception {
        // Given: prototype with clone method
        Object prototype = "prototype";
        Method cloneMethod = prototype.getClass().getMethod("toString");

        // When: create prototype factory
        Factory<?> factory = PrototypeFactory.prototypeFactory(prototype);

        // Then: prototype clone factory
        assertNotNull(factory);
        assertNotNull(factory.create());
    }

    @Test
    void testPrototypeFactory_CopyConstructor() throws Exception {
        // Given: prototype with copy constructor
        Object prototype = "prototype";
        Method constructorMethod = prototype.getClass().getConstructor(String.class);

        // When: create prototype factory
        Factory<?> factory = PrototypeFactory.prototypeFactory(prototype);

        // Then: instantiate factory
        assertNotNull(factory);
        assertNotNull(factory.create());
    }

    @Test
    void testPrototypeFactory_Serialization() throws Exception {
        // Given: prototype with serialization
        Object prototype = "prototype";
        when(prototype.getClass().getMethod("clone")).thenThrow(new NoSuchMethodException());

        // When: create prototype factory
        Factory<?> factory = PrototypeFactory.prototypeFactory(prototype);

        // Then: prototype serialization factory
        assertNotNull(factory);
        assertNotNull(factory.create());
    }

    @Test
    void testPrototypeFactory_InvalidPrototype() {
        // Given: invalid prototype
        Object prototype = new Object() {
            @Override
            public Object clone() {
                throw new RuntimeException();
            }
        };

        // When: create prototype factory
        assertThrows(IllegalArgumentException.class, () -> PrototypeFactory.prototypeFactory(prototype));
    }

    @Test
    void testPrototypeCloneFactory_Create() throws Exception {
        // Given: prototype clone factory
        String prototype = "prototype";
        Method cloneMethod = prototype.getClass().getMethod("toString");
        PrototypeFactory.PrototypeCloneFactory<String> factory = new PrototypeFactory.PrototypeCloneFactory<>(prototype, cloneMethod);

        // When: create object
        String createdObject = factory.create();

        // Then: created object is not null
        assertNotNull(createdObject);
        assertEquals(prototype.toString(), createdObject);
    }

    @Test
    void testPrototypeCloneFactory_Create_InvocationTargetException() throws Exception {
        // Given: prototype clone factory
        String prototype = "prototype";
        Method cloneMethod = prototype.getClass().getMethod("toString");
        doThrow(new InvocationTargetException(new RuntimeException())).when(cloneMethod).invoke(any(), any());

        PrototypeFactory.PrototypeCloneFactory<String> factory = new PrototypeFactory.PrototypeCloneFactory<>(prototype, cloneMethod);

        // When: create object
        assertThrows(FunctorException.class, factory::create);
    }

    @Test
    void testPrototypeSerializationFactory_Create() throws Exception {
        // Given: prototype serialization factory
        String prototype = "prototype";
        PrototypeFactory.PrototypeSerializationFactory<String> factory = new PrototypeFactory.PrototypeSerializationFactory<>(prototype);

        // When: create object
        String createdObject = factory.create();

        // Then: created object is not null
        assertNotNull(createdObject);
        assertEquals(prototype, createdObject);
    }

    @Test
    void testPrototypeSerializationFactory_Create_FunctorException() throws Exception {
        // Given: prototype serialization factory
        String prototype = "prototype";
        PrototypeFactory.PrototypeSerializationFactory<String> factory = new PrototypeFactory.PrototypeSerializationFactory<>(prototype);

        // When: create object with exception
        doThrow(new IOException()).when(prototype).writeObject(any());

        // Then: functor exception
        assertThrows(FunctorException.class, factory::create);
    }
}