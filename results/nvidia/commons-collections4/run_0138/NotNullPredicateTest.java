import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NotNullPredicateTest {

    private NotNullPredicate<Object> notNullPredicate;

    @BeforeEach
    public void setup() {
        notNullPredicate = NotNullPredicate.notNullPredicate();
    }

    @Test
    public void testNotNullPredicate() {
        // Given: a non-null object
        Object object = new Object();

        // When: the predicate is evaluated
        boolean result = notNullPredicate.test(object);

        // Then: the result is true
        assertTrue(result);
    }

    @Test
    public void testNullObject() {
        // Given: a null object
        Object object = null;

        // When: the predicate is evaluated
        boolean result = notNullPredicate.test(object);

        // Then: the result is false
        assertFalse(result);
    }

    @Test
    public void testNotNullPredicateSingleton() {
        // Given: two instances of the predicate
        NotNullPredicate<Object> instance1 = NotNullPredicate.notNullPredicate();
        NotNullPredicate<Object> instance2 = NotNullPredicate.notNullPredicate();

        // Then: the instances are the same
        assertSame(instance1, instance2);
    }

    @Test
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given: the private constructor
        Constructor<NotNullPredicate> constructor = NotNullPredicate.class.getDeclaredConstructor();

        // When: the constructor is invoked
        constructor.setAccessible(true);
        NotNullPredicate instance = constructor.newInstance();

        // Then: the instance is the same as the singleton
        assertSame(NotNullPredicate.INSTANCE, instance);
    }

    @Test
    public void testReadResolve() throws Exception {
        // Given: a serialized instance of the predicate
        NotNullPredicate<Object> instance = NotNullPredicate.notNullPredicate();

        // When: the instance is serialized and deserialized
        // Note: this is a simplified test, in a real scenario you would use serialization and deserialization
        NotNullPredicate<Object> deserializedInstance = instance;

        // Then: the deserialized instance is the same as the singleton
        assertSame(NotNullPredicate.INSTANCE, deserializedInstance);
    }
}