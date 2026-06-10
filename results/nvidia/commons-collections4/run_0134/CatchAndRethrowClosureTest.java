import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.functors.CatchAndRethrowClosure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatchAndRethrowClosureTest {

    @Mock
    private Closure<String> mockClosure;

    private CatchAndRethrowClosure<String> catchAndRethrowClosure;

    @BeforeEach
    public void setup() {
        catchAndRethrowClosure = new CatchAndRethrowClosure<String>() {
            @Override
            protected void executeAndThrow(String input) throws Throwable {
                // empty implementation
            }
        };
    }

    @Test
    public void testExecute_NoException() {
        // Given: a valid input
        String input = "test input";

        // When: execute the closure
        catchAndRethrowClosure.execute(input);

        // Then: no exception is thrown
        verifyNoInteractions(mockClosure);
    }

    @Test
    public void testExecute_RuntimeException() {
        // Given: a closure that throws a RuntimeException
        CatchAndRethrowClosure<String> testClosure = new CatchAndRethrowClosure<String>() {
            @Override
            protected void executeAndThrow(String input) throws Throwable {
                throw new RuntimeException("test exception");
            }
        };

        // When: execute the closure
        assertThrows(RuntimeException.class, () -> testClosure.execute("test input"));
    }

    @Test
    public void testExecute_CheckedException() {
        // Given: a closure that throws a checked exception
        CatchAndRethrowClosure<String> testClosure = new CatchAndRethrowClosure<String>() {
            @Override
            protected void executeAndThrow(String input) throws Throwable {
                throw new Exception("test exception");
            }
        };

        // When: execute the closure
        FunctorException exception = assertThrows(FunctorException.class, () -> testClosure.execute("test input"));

        // Then: the cause of the FunctorException is the original checked exception
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof Exception);
    }

    @Test
    public void testExecute_Throwable() {
        // Given: a closure that throws a Throwable
        CatchAndRethrowClosure<String> testClosure = new CatchAndRethrowClosure<String>() {
            @Override
            protected void executeAndThrow(String input) throws Throwable {
                throw new Throwable("test throwable");
            }
        };

        // When: execute the closure
        FunctorException exception = assertThrows(FunctorException.class, () -> testClosure.execute("test input"));

        // Then: the cause of the FunctorException is the original Throwable
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof Throwable);
    }
}