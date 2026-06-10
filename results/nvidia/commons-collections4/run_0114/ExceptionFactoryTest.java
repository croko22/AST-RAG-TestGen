import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.functors.ExceptionFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExceptionFactoryTest {

    @Test
    public void testExceptionFactory() {
        // Given: 
        // When: se ejecuta la fabrica de excepciones
        Factory<?> factory = ExceptionFactory.exceptionFactory();
        // Then: se verifica que la fabrica de excepciones no sea null
        assertNotNull(factory);
    }

    @Test
    public void testCreate() {
        // Given: 
        Factory<?> factory = ExceptionFactory.exceptionFactory();
        // When / Then: se espera una excepcion al intentar crear
        assertThrows(FunctorException.class, () -> {
            factory.create();
        });
    }

    @Test
    public void testCreateDirect() {
        // Given: 
        ExceptionFactory<?> exceptionFactory = new ExceptionFactory<>();
        // When / Then: se espera una excepcion al intentar crear
        assertThrows(FunctorException.class, () -> {
            exceptionFactory.create();
        });
    }

    @Test
    public void testReadResolve() {
        // Given: 
        ExceptionFactory<?> exceptionFactory = new ExceptionFactory<>();
        // When: 
        Object resolved = exceptionFactory.readResolve();
        // Then: se verifica que el objeto resuelto sea la instancia singleton
        assertSame(ExceptionFactory.INSTANCE, resolved);
    }
}