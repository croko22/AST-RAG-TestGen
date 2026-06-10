import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Modifier;
import java.util.function.Predicate as JavaPredicate;

public class InstanceofPredicateTest {

    private InstanceofPredicate instanceofPredicate;
    private Class<?> type;

    @BeforeEach
    public void setup() {
        type = String.class;
        instanceofPredicate = InstanceofPredicate.instanceOfPredicate(type);
    }

    @Test
    public void testInstanceofPredicate() {
        // Given: a class type
        // When: creating an instance of predicate
        InstanceofPredicate predicate = InstanceofPredicate.instanceOfPredicate(type);
        // Then: the predicate is not null
        assertNotNull(predicate);
    }

    @Test
    public void testGetType() {
        // Given: an instance of predicate
        // When: getting the type
        Class<?> result = instanceofPredicate.getType();
        // Then: the result is the expected type
        assertEquals(type, result);
    }

    @Test
    public void testTest_ObjectOfCorrectType() {
        // Given: an object of the correct type
        Object object = "Hello";
        // When: testing the predicate
        boolean result = instanceofPredicate.test(object);
        // Then: the result is true
        assertTrue(result);
    }

    @Test
    public void testTest_ObjectOfIncorrectType() {
        // Given: an object of the incorrect type
        Object object = 123;
        // When: testing the predicate
        boolean result = instanceofPredicate.test(object);
        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testTest_NullObject() {
        // Given: a null object
        Object object = null;
        // When: testing the predicate
        boolean result = instanceofPredicate.test(object);
        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testInstanceofPredicate_NullType() {
        // Given: a null type
        Class<?> nullType = null;
        // When / Then: creating an instance of predicate with a null type throws an exception
        assertThrows(NullPointerException.class, () -> InstanceofPredicate.instanceOfPredicate(nullType));
    }
}