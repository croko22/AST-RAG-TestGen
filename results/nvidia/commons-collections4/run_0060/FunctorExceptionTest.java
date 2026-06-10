import org.apache.commons.collections4.FunctorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FunctorExceptionTest {

    @Test
    public void testFunctorException_NoArgs() {
        // When: se crea una excepcion sin argumentos
        FunctorException exception = new FunctorException();

        // Then: se verifica que la excepcion se haya creado correctamente
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testFunctorException_WithMessage() {
        // Given: un mensaje de error
        String message = "Error en el functor";

        // When: se crea una excepcion con el mensaje
        FunctorException exception = new FunctorException(message);

        // Then: se verifica que la excepcion se haya creado correctamente
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testFunctorException_WithMessageAndRootCause() {
        // Given: un mensaje de error y una causa raiz
        String message = "Error en el functor";
        Throwable rootCause = new Throwable("Causa raiz");

        // When: se crea una excepcion con el mensaje y la causa raiz
        FunctorException exception = new FunctorException(message, rootCause);

        // Then: se verifica que la excepcion se haya creado correctamente
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(rootCause, exception.getCause());
    }

    @Test
    public void testFunctorException_WithRootCause() {
        // Given: una causa raiz
        Throwable rootCause = new Throwable("Causa raiz");

        // When: se crea una excepcion con la causa raiz
        FunctorException exception = new FunctorException(rootCause);

        // Then: se verifica que la excepcion se haya creado correctamente
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertEquals(rootCause, exception.getCause());
    }
}