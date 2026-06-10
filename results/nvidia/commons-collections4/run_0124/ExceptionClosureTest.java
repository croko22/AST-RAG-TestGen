import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.functors.ExceptionClosure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionClosureTest {

    @Test
    public void testExceptionClosure() {
        // Given: 
        Closure<?> closure = ExceptionClosure.exceptionClosure();

        // When: 
        assertThrows(FunctorException.class, () -> closure.execute(null));

        // Then: 
        // No need to verify anything, the exception is the expected result
    }

    @Test
    public void testExceptionClosureExecute() {
        // Given: 
        Closure<?> closure = ExceptionClosure.exceptionClosure();

        // When / Then: 
        assertThrows(FunctorException.class, () -> closure.execute("input"));
    }

    @Test
    public void testExceptionClosureSingleton() {
        // Given: 
        Closure<?> closure1 = ExceptionClosure.exceptionClosure();
        Closure<?> closure2 = ExceptionClosure.exceptionClosure();

        // When / Then: 
        assertSame(closure1, closure2);
    }

    @Test
    public void testReadResolve() throws Exception {
        // Given: 
        ExceptionClosure<?> closure = ExceptionClosure.INSTANCE;

        // When: 
        Object resolvedClosure = closure.readResolve();

        // Then: 
        assertSame(ExceptionClosure.INSTANCE, resolvedClosure);
    }
}