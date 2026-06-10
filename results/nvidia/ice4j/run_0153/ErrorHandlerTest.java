import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.RuntimeException;
import java.lang.Thread;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTest {

    @Mock
    private ErrorHandler errorHandler;

    @Mock
    private Runnable callingThread;

    @Mock
    private Throwable error;

    @BeforeEach
    void setup() {
        // No setup needed for this test class
    }

    @AfterEach
    void tearDown() {
        // No tear down needed for this test class
    }

    @Test
    public void testHandleError() {
        // Given
        String message = "Test error message";
        Throwable testError = new RuntimeException("Test error");

        // When
        errorHandler.handleError(message, testError);

        // Then
        verify(errorHandler, times(1)).handleError(eq(message), eq(testError));
    }

    @Test
    public void testHandleError_NullMessage() {
        // Given
        String message = null;
        Throwable testError = new RuntimeException("Test error");

        // When
        errorHandler.handleError(message, testError);

        // Then
        verify(errorHandler, times(1)).handleError(eq(message), eq(testError));
    }

    @Test
    public void testHandleError_NullError() {
        // Given
        String message = "Test error message";
        Throwable testError = null;

        // When
        errorHandler.handleError(message, testError);

        // Then
        verify(errorHandler, times(1)).handleError(eq(message), eq(testError));
    }

    @Test
    public void testHandleFatalError() {
        // Given
        String message = "Test fatal error message";
        Throwable testError = new RuntimeException("Test fatal error");

        // When
        errorHandler.handleFatalError(callingThread, message, testError);

        // Then
        verify(errorHandler, times(1)).handleFatalError(eq(callingThread), eq(message), eq(testError));
    }

    @Test
    public void testHandleFatalError_NullCallingThread() {
        // Given
        String message = "Test fatal error message";
        Throwable testError = new RuntimeException("Test fatal error");
        Runnable nullCallingThread = null;

        // When
        errorHandler.handleFatalError(nullCallingThread, message, testError);

        // Then
        verify(errorHandler, times(1)).handleFatalError(eq(nullCallingThread), eq(message), eq(testError));
    }

    @Test
    public void testHandleFatalError_NullMessage() {
        // Given
        String message = null;
        Throwable testError = new RuntimeException("Test fatal error");

        // When
        errorHandler.handleFatalError(callingThread, message, testError);

        // Then
        verify(errorHandler, times(1)).handleFatalError(eq(callingThread), eq(message), eq(testError));
    }

    @Test
    public void testHandleFatalError_NullError() {
        // Given
        String message = "Test fatal error message";
        Throwable testError = null;

        // When
        errorHandler.handleFatalError(callingThread, message, testError);

        // Then
        verify(errorHandler, times(1)).handleFatalError(eq(callingThread), eq(message), eq(testError));
    }
}