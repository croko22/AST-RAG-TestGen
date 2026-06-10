import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NullPredicateTest {

    private NullPredicate<Object> nullPredicate;

    @BeforeEach
    public void setup() {
        nullPredicate = (NullPredicate<Object>) NullPredicate.nullPredicate();
    }

    @Test
    public void testNullPredicate() {
        // Given: null input
        Object input = null;

        // When: test method is called
        boolean result = nullPredicate.test(input);

        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testNonNullPredicate() {
        // Given: non-null input
        Object input = new Object();

        // When: test method is called
        boolean result = nullPredicate.test(input);

        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testNullPredicateSingleton() {
        // Given: two instances of NullPredicate
        NullPredicate<Object> instance1 = (NullPredicate<Object>) NullPredicate.nullPredicate();
        NullPredicate<Object> instance2 = (NullPredicate<Object>) NullPredicate.nullPredicate();

        // Then: both instances should be the same
        assertSame(instance1, instance2);
    }

    @Test
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given: private constructor of NullPredicate
        Constructor<NullPredicate> constructor = NullPredicate.class.getDeclaredConstructor();

        // When: trying to instantiate NullPredicate using private constructor
        constructor.setAccessible(true);
        NullPredicate instance = constructor.newInstance();

        // Then: instance should be the same as the singleton instance
        assertSame(NullPredicate.nullPredicate(), instance);
    }

    @Test
    public void testReadResolve() throws Exception {
        // Given: serialized instance of NullPredicate
        NullPredicate<Object> instance = (NullPredicate<Object>) NullPredicate.nullPredicate();

        // When: deserializing the instance
        Object deserializedInstance = deserialize(instance);

        // Then: deserialized instance should be the same as the singleton instance
        assertSame(NullPredicate.nullPredicate(), deserializedInstance);
    }

    private Object deserialize(Object instance) throws Exception {
        // Simulating deserialization process
        return instance.getClass().getMethod("readResolve").invoke(instance);
    }
}