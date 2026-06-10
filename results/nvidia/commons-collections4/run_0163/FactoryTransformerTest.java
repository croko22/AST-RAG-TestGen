import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FactoryTransformerTest {

    @Mock
    private Factory<String> mockFactory;

    private FactoryTransformer<Object, String> factoryTransformer;

    @BeforeEach
    public void setup() {
        factoryTransformer = new FactoryTransformer<>(mockFactory);
    }

    @Test
    public void testFactoryTransformer_StaticMethod() {
        // Given: a mock factory
        when(mockFactory.get()).thenReturn("Mock Result");

        // When: creating a transformer using the static method
        Transformer<Object, String> transformer = FactoryTransformer.factoryTransformer(mockFactory);

        // Then: verify the transformer is created correctly
        assertNotNull(transformer);
        assertEquals("Mock Result", transformer.transform(null));
        verify(mockFactory, times(1)).get();
    }

    @Test
    public void testFactoryTransformer_StaticMethod_NullFactory() {
        // Given: a null factory
        Factory<String> nullFactory = null;

        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> FactoryTransformer.factoryTransformer(nullFactory));
    }

    @Test
    public void testGetFactory() {
        // Given: a factory transformer
        FactoryTransformer<Object, String> transformer = new FactoryTransformer<>(mockFactory);

        // When: getting the factory
        Factory<? extends String> factory = transformer.getFactory();

        // Then: verify the factory is returned correctly
        assertSame(mockFactory, factory);
    }

    @Test
    public void testTransform() {
        // Given: a mock factory
        when(mockFactory.get()).thenReturn("Mock Result");

        // When: transforming an input
        String result = factoryTransformer.transform(null);

        // Then: verify the result is correct
        assertEquals("Mock Result", result);
        verify(mockFactory, times(1)).get();
    }

    @Test
    public void testTransform_MultipleCalls() {
        // Given: a mock factory
        when(mockFactory.get()).thenReturn("Mock Result 1", "Mock Result 2");

        // When: transforming an input multiple times
        String result1 = factoryTransformer.transform(null);
        String result2 = factoryTransformer.transform(null);

        // Then: verify the results are correct
        assertEquals("Mock Result 1", result1);
        assertEquals("Mock Result 2", result2);
        verify(mockFactory, times(2)).get();
    }
}