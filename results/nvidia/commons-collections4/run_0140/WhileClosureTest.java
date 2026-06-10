import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.WhileClosure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WhileClosureTest {

    @Mock
    private Predicate<Object> predicate;

    @Mock
    private Closure<Object> closure;

    private WhileClosure<Object> whileClosure;

    @BeforeEach
    public void setup() {
        whileClosure = new WhileClosure<>(predicate, closure, true);
    }

    @Test
    public void testWhileClosureFactoryMethod() {
        // Given
        Predicate<Object> predicate = mock(Predicate.class);
        Closure<Object> closure = mock(Closure.class);

        // When
        Closure<Object> result = WhileClosure.whileClosure(predicate, closure, true);

        // Then
        assertNotNull(result);
        assertNotSame(predicate, result);
        assertNotSame(closure, result);
    }

    @Test
    public void testWhileClosureFactoryMethodNullPredicate() {
        // Given
        Closure<Object> closure = mock(Closure.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> WhileClosure.whileClosure(null, closure, true));
    }

    @Test
    public void testWhileClosureFactoryMethodNullClosure() {
        // Given
        Predicate<Object> predicate = mock(Predicate.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> WhileClosure.whileClosure(predicate, null, true));
    }

    @Test
    public void testExecuteDoWhileLoop() {
        // Given
        Object input = new Object();
        when(predicate.test(any())).thenReturn(true, false);

        // When
        whileClosure.execute(input);

        // Then
        verify(closure, times(2)).accept(input);
    }

    @Test
    public void testExecuteWhileLoop() {
        // Given
        Object input = new Object();
        whileClosure = new WhileClosure<>(predicate, closure, false);
        when(predicate.test(any())).thenReturn(true, false);

        // When
        whileClosure.execute(input);

        // Then
        verify(closure, times(1)).accept(input);
    }

    @Test
    public void testExecuteDoWhileLoopPredicateFalse() {
        // Given
        Object input = new Object();
        when(predicate.test(any())).thenReturn(false);

        // When
        whileClosure.execute(input);

        // Then
        verify(closure, times(1)).accept(input);
    }

    @Test
    public void testExecuteWhileLoopPredicateFalse() {
        // Given
        Object input = new Object();
        whileClosure = new WhileClosure<>(predicate, closure, false);
        when(predicate.test(any())).thenReturn(false);

        // When
        whileClosure.execute(input);

        // Then
        verify(closure, never()).accept(input);
    }

    @Test
    public void testGetClosure() {
        // When
        Closure<Object> result = whileClosure.getClosure();

        // Then
        assertSame(closure, result);
    }

    @Test
    public void testGetPredicate() {
        // When
        Predicate<Object> result = whileClosure.getPredicate();

        // Then
        assertSame(predicate, result);
    }

    @Test
    public void testIsDoLoop() {
        // When
        boolean result = whileClosure.isDoLoop();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsWhileLoop() {
        // Given
        whileClosure = new WhileClosure<>(predicate, closure, false);

        // When
        boolean result = whileClosure.isDoLoop();

        // Then
        assertFalse(result);
    }
}