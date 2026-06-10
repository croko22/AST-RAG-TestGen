import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionPredicateTest {

    @Test
    public void testExceptionPredicate() {
        // Given: 
        Predicate predicate = ExceptionPredicate.exceptionPredicate();

        // When: 
        assertThrows(FunctorException.class, () -> {
            predicate.test("object");
        });
    }

    @Test
    public void testTestMethod() {
        // Given: 
        ExceptionPredicate<String> exceptionPredicate = new ExceptionPredicate<>();

        // When: 
        assertThrows(FunctorException.class, () -> {
            exceptionPredicate.test("object");
        });
    }

    @Test
    public void testExceptionPredicateInstance() {
        // Given: 
        Predicate instance1 = ExceptionPredicate.exceptionPredicate();
        Predicate instance2 = ExceptionPredicate.exceptionPredicate();

        // Then: 
        assertSame(instance1, instance2);
    }
}