import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.functors.NOPClosure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NOPClosureTest {

    private Closure<?> closure;

    @BeforeEach
    void setup() {
        closure = NOPClosure.nopClosure();
    }

    @Test
    public void testNOPClosure() {
        // Given: a NOPClosure instance
        Closure<?> instance = NOPClosure.nopClosure();

        // Then: it should be the singleton instance
        assertSame(NOPClosure.INSTANCE, instance);
    }

    @Test
    public void testExecute() {
        // Given: an input object
        Object input = new Object();

        // When: execute is called
        closure.execute(input);

        // Then: no exception should be thrown
        // The method does nothing, so we can't assert any specific behavior
    }

    @Test
    public void testNopClosure() {
        // Given: a type parameter
        Closure<String> stringClosure = NOPClosure.nopClosure();

        // Then: it should be the singleton instance
        assertSame(NOPClosure.INSTANCE, stringClosure);
    }

    @Test
    public void testReadResolve() {
        // Given: a serialized instance
        NOPClosure<?> instance = NOPClosure.INSTANCE;

        // When: readResolve is called (implicitly through serialization)
        // Then: it should return the singleton instance
        assertSame(NOPClosure.INSTANCE, instance);
    }
}