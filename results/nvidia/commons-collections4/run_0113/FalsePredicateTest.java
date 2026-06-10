import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FalsePredicateTest {

    private FalsePredicate<String> falsePredicate;

    @BeforeEach
    public void setup() {
        falsePredicate = (FalsePredicate<String>) FalsePredicate.falsePredicate();
    }

    @Test
    public void testFalsePredicate() {
        // Given: a predicate instance
        // When: the predicate is evaluated
        boolean result = falsePredicate.test("test");
        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testFalsePredicateNullInput() {
        // Given: a null input
        // When: the predicate is evaluated with null input
        boolean result = falsePredicate.test(null);
        // Then: the result should be false
        assertFalse(result);
    }

    @Test
    public void testFalsePredicateInstance() {
        // Given: two instances of FalsePredicate
        FalsePredicate<String> instance1 = (FalsePredicate<String>) FalsePredicate.falsePredicate();
        FalsePredicate<String> instance2 = (FalsePredicate<String>) FalsePredicate.falsePredicate();
        // When: the instances are compared
        // Then: the instances should be the same
        assertSame(instance1, instance2);
    }

    @Test
    public void testFalsePredicateEvaluate() {
        // Given: a predicate instance
        // When: the predicate is evaluated
        boolean result = falsePredicate.test("test");
        // Then: the result should be false
        assertFalse(result);
    }
}