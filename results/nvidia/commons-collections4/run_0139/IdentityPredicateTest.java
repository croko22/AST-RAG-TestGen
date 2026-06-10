import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IdentityPredicateTest {

    private Object testObject;
    private Object differentObject;

    @BeforeEach
    public void setup() {
        testObject = new Object();
        differentObject = new Object();
    }

    @Test
    public void testIdentityPredicate_NullObject() {
        // Given: null object
        // When: identityPredicate is created with null object
        Predicate<Object> predicate = IdentityPredicate.identityPredicate(null);
        // Then: NullPredicate is returned
        assertNotNull(predicate);
        assertTrue(predicate instanceof org.apache.commons.collections4.functors.NullPredicate);
    }

    @Test
    public void testIdentityPredicate_NonNullObject() {
        // Given: non-null object
        // When: identityPredicate is created with non-null object
        Predicate<Object> predicate = IdentityPredicate.identityPredicate(testObject);
        // Then: IdentityPredicate is returned
        assertNotNull(predicate);
        assertTrue(predicate instanceof IdentityPredicate);
    }

    @Test
    public void testGetValue() {
        // Given: IdentityPredicate with testObject
        IdentityPredicate<Object> predicate = new IdentityPredicate<>(testObject);
        // When: getValue is called
        Object value = predicate.getValue();
        // Then: testObject is returned
        assertSame(testObject, value);
    }

    @Test
    public void testTest_SameObject() {
        // Given: IdentityPredicate with testObject
        IdentityPredicate<Object> predicate = new IdentityPredicate<>(testObject);
        // When: test is called with same object
        boolean result = predicate.test(testObject);
        // Then: true is returned
        assertTrue(result);
    }

    @Test
    public void testTest_DifferentObject() {
        // Given: IdentityPredicate with testObject
        IdentityPredicate<Object> predicate = new IdentityPredicate<>(testObject);
        // When: test is called with different object
        boolean result = predicate.test(differentObject);
        // Then: false is returned
        assertFalse(result);
    }

    @Test
    public void testTest_NullObject() {
        // Given: IdentityPredicate with testObject
        IdentityPredicate<Object> predicate = new IdentityPredicate<>(testObject);
        // When: test is called with null object
        boolean result = predicate.test(null);
        // Then: false is returned
        assertFalse(result);
    }
}