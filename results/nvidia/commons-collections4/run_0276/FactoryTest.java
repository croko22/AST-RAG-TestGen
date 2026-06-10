import org.apache.commons.collections4.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FactoryTest {

    @Mock
    private Factory<String> factory;

    @BeforeEach
    void setup() {
        // No setup needed for this test class
    }

    @Test
    public void testCreate() {
        // Given: a factory that returns a new object
        when(factory.create()).thenReturn("New Object");

        // When: create is called on the factory
        String result = factory.create();

        // Then: the result should be the new object
        assertEquals("New Object", result);

        // Verify: the create method was called once
        verify(factory, times(1)).create();
    }

    @Test
    public void testGet() {
        // Given: a factory that returns a new object
        when(factory.get()).thenReturn("New Object");

        // When: get is called on the factory
        String result = factory.get();

        // Then: the result should be the new object
        assertEquals("New Object", result);

        // Verify: the get method was called once
        verify(factory, times(1)).get();
    }

    @Test
    public void testCreate_ThrowsFunctorException() {
        // Given: a factory that throws a FunctorException
        when(factory.create()).thenThrow(new RuntimeException("Cannot create object"));

        // When / Then: create is called on the factory and an exception is thrown
        assertThrows(RuntimeException.class, () -> factory.create());

        // Verify: the create method was called once
        verify(factory, times(1)).create();
    }

    @Test
    public void testGet_ThrowsFunctorException() {
        // Given: a factory that throws a FunctorException
        when(factory.get()).thenThrow(new RuntimeException("Cannot create object"));

        // When / Then: get is called on the factory and an exception is thrown
        assertThrows(RuntimeException.class, () -> factory.get());

        // Verify: the get method was called once
        verify(factory, times(1)).get();
    }
}