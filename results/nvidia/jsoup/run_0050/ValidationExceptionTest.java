import org.jsoup.helper.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidationExceptionTest {

    private ValidationException validationException;

    @BeforeEach
    void setup() {
        validationException = new ValidationException("Test message");
    }

    @Test
    public void testValidationException_Message() {
        // Given: a message for the exception
        String message = "Test message";

        // When: creating a new ValidationException with the message
        ValidationException exception = new ValidationException(message);

        // Then: the message is set correctly
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testFillInStackTrace() {
        // Given: a ValidationException instance
        ValidationException exception = new ValidationException("Test message");

        // When: filling in the stack trace
        Throwable throwable = exception.fillInStackTrace();

        // Then: the stack trace is filtered correctly
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        boolean validatorFound = false;
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().equals(ValidationException.Validator)) {
                validatorFound = true;
                break;
            }
        }
        assertFalse(validatorFound);
    }

    @Test
    public void testFillInStackTrace_MultipleCalls() {
        // Given: a ValidationException instance
        ValidationException exception = new ValidationException("Test message");

        // When: filling in the stack trace multiple times
        Throwable throwable1 = exception.fillInStackTrace();
        Throwable throwable2 = exception.fillInStackTrace();

        // Then: the stack traces are the same
        assertArrayEquals(throwable1.getStackTrace(), throwable2.getStackTrace());
    }

    @Test
    public void testValidationException_NullMessage() {
        // Given: a null message for the exception
        String message = null;

        // When: creating a new ValidationException with the null message
        assertThrows(NullPointerException.class, () -> new ValidationException(message));
    }

    @Test
    public void testValidationException_EmptyMessage() {
        // Given: an empty message for the exception
        String message = "";

        // When: creating a new ValidationException with the empty message
        ValidationException exception = new ValidationException(message);

        // Then: the message is set correctly
        assertEquals(message, exception.getMessage());
    }
}