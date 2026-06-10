import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

public class UniquePredicateTest {

    private UniquePredicate<String> uniquePredicate;

    @BeforeEach
    public void setup() {
        uniquePredicate = UniquePredicate.uniquePredicate();
    }

    @Test
    public void testUniquePredicate() {
        // Given: a new UniquePredicate instance
        UniquePredicate<String> predicate = UniquePredicate.uniquePredicate();

        // Then: the predicate is not null
        assertNotNull(predicate);
    }

    @Test
    public void testTest_FirstTime() {
        // Given: a new object
        String object = "object1";

        // When: the test method is called
        boolean result = uniquePredicate.test(object);

        // Then: the result is true
        assertTrue(result);
    }

    @Test
    public void testTest_SecondTime() {
        // Given: an object that has been seen before
        String object = "object1";
        uniquePredicate.test(object);

        // When: the test method is called again
        boolean result = uniquePredicate.test(object);

        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testTest_MultipleObjects() {
        // Given: multiple objects
        String object1 = "object1";
        String object2 = "object2";

        // When: the test method is called for each object
        boolean result1 = uniquePredicate.test(object1);
        boolean result2 = uniquePredicate.test(object2);

        // Then: the results are true for each object
        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    public void testTest_NullObject() {
        // Given: a null object
        String object = null;

        // When: the test method is called
        boolean result = uniquePredicate.test(object);

        // Then: the result is true
        assertTrue(result);
    }

    @Test
    public void testTest_NullObject_SecondTime() {
        // Given: a null object that has been seen before
        String object = null;
        uniquePredicate.test(object);

        // When: the test method is called again
        boolean result = uniquePredicate.test(object);

        // Then: the result is false
        assertFalse(result);
    }
}