import net.hydromatic.morel.datalog.DatalogException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DatalogExceptionTest {

    @Test
    public void testDatalogException_Message() {
        // Given: un mensaje de error
        String message = "Error parsing Datalog program";

        // When: se crea una excepción DatalogException con el mensaje
        DatalogException exception = new DatalogException(message);

        // Then: se verifica el mensaje de la excepción
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testDatalogException_MessageAndCause() {
        // Given: un mensaje de error y una causa
        String message = "Error parsing Datalog program";
        Throwable cause = new Throwable("Underlying error");

        // When: se crea una excepción DatalogException con el mensaje y la causa
        DatalogException exception = new DatalogException(message, cause);

        // Then: se verifica el mensaje y la causa de la excepción
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testDatalogException_NullMessage() {
        // Given: un mensaje de error nulo
        String message = null;

        // When: se crea una excepción DatalogException con el mensaje nulo
        DatalogException exception = new DatalogException(message);

        // Then: se verifica el mensaje de la excepción
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testDatalogException_NullCause() {
        // Given: un mensaje de error y una causa nula
        String message = "Error parsing Datalog program";
        Throwable cause = null;

        // When: se crea una excepción DatalogException con el mensaje y la causa nula
        DatalogException exception = new DatalogException(message, cause);

        // Then: se verifica el mensaje y la causa de la excepción
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
}