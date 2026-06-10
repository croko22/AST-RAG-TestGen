import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.functors.ForClosure;
import org.apache.commons.collections4.functors.NOPClosure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ForClosureTest {

    @Mock
    private Closure<Object> mockClosure;

    private Object input;

    @BeforeEach
    void setup() {
        input = new Object();
    }

    @Test
    public void testForClosure_ZeroCount() {
        // Given: count is zero
        int count = 0;

        // When: forClosure is called
        Closure<Object> result = ForClosure.forClosure(count, mockClosure);

        // Then: NOPClosure is returned
        assertSame(NOPClosure.nopClosure(), result);
    }

    @Test
    public void testForClosure_NullClosure() {
        // Given: closure is null
        Closure<Object> closure = null;

        // When: forClosure is called
        Closure<Object> result = ForClosure.forClosure(1, closure);

        // Then: NOPClosure is returned
        assertSame(NOPClosure.nopClosure(), result);
    }

    @Test
    public void testForClosure_CountOne() {
        // Given: count is one
        int count = 1;

        // When: forClosure is called
        Closure<Object> result = ForClosure.forClosure(count, mockClosure);

        // Then: the original closure is returned
        assertSame(mockClosure, result);
    }

    @Test
    public void testForClosure_MultipleCount() {
        // Given: count is greater than one
        int count = 2;

        // When: forClosure is called
        Closure<Object> result = ForClosure.forClosure(count, mockClosure);

        // Then: a new ForClosure instance is returned
        assertEquals(count, ((ForClosure<Object>) result).getCount());
        assertSame(mockClosure, ((ForClosure<Object>) result).getClosure());
    }

    @Test
    public void testExecute() {
        // Given: a ForClosure instance with a count of 2
        ForClosure<Object> forClosure = new ForClosure<>(2, mockClosure);

        // When: execute is called
        forClosure.execute(input);

        // Then: the closure is executed twice
        verify(mockClosure, times(2)).accept(input);
    }

    @Test
    public void testGetClosure() {
        // Given: a ForClosure instance
        ForClosure<Object> forClosure = new ForClosure<>(1, mockClosure);

        // When: getClosure is called
        Closure<Object> result = forClosure.getClosure();

        // Then: the original closure is returned
        assertSame(mockClosure, result);
    }

    @Test
    public void testGetCount() {
        // Given: a ForClosure instance with a count of 2
        ForClosure<Object> forClosure = new ForClosure<>(2, mockClosure);

        // When: getCount is called
        int result = forClosure.getCount();

        // Then: the count is returned
        assertEquals(2, result);
    }

    @Test
    public void testExecute_NullInput() {
        // Given: a ForClosure instance
        ForClosure<Object> forClosure = new ForClosure<>(1, mockClosure);

        // When: execute is called with a null input
        forClosure.execute(null);

        // Then: the closure is executed with the null input
        verify(mockClosure, times(1)).accept(null);
    }

    @Test
    public void testForClosure_NegativeCount() {
        // Given: a negative count
        int count = -1;

        // When: forClosure is called
        Closure<Object> result = ForClosure.forClosure(count, mockClosure);

        // Then: NOPClosure is returned
        assertSame(NOPClosure.nopClosure(), result);
    }
}