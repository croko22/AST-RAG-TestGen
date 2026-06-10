import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.IfClosure;
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
public class IfClosureTest {

    @Mock
    private Predicate<Object> predicate;

    @Mock
    private Closure<Object> trueClosure;

    @Mock
    private Closure<Object> falseClosure;

    private IfClosure<Object> ifClosure;

    @BeforeEach
    void setup() {
        ifClosure = new IfClosure<>(predicate, trueClosure, falseClosure);
    }

    @Test
    public void testIfClosure_PredicateTrueClosure() {
        // Given
        when(predicate.test(any())).thenReturn(true);

        // When
        IfClosure<Object> result = IfClosure.ifClosure(predicate, trueClosure);

        // Then
        assertNotNull(result);
        assertSame(trueClosure, result.getTrueClosure());
        assertSame(predicate, result.getPredicate());
    }

    @Test
    public void testIfClosure_PredicateTrueClosureFalseClosure() {
        // Given

        // When
        IfClosure<Object> result = IfClosure.ifClosure(predicate, trueClosure, falseClosure);

        // Then
        assertNotNull(result);
        assertSame(trueClosure, result.getTrueClosure());
        assertSame(falseClosure, result.getFalseClosure());
        assertSame(predicate, result.getPredicate());
    }

    @Test
    public void testIfClosure_NullPredicate() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> IfClosure.ifClosure(null, trueClosure));
    }

    @Test
    public void testIfClosure_NullTrueClosure() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> IfClosure.ifClosure(predicate, null));
    }

    @Test
    public void testIfClosure_NullFalseClosure() {
        // Given

        // When / Then
        assertThrows(NullPointerException.class, () -> IfClosure.ifClosure(predicate, trueClosure, null));
    }

    @Test
    public void testExecute_PredicateTrue() {
        // Given
        when(predicate.test(any())).thenReturn(true);
        Object input = new Object();

        // When
        ifClosure.execute(input);

        // Then
        verify(trueClosure, times(1)).accept(input);
        verify(falseClosure, never()).accept(any());
    }

    @Test
    public void testExecute_PredicateFalse() {
        // Given
        when(predicate.test(any())).thenReturn(false);
        Object input = new Object();

        // When
        ifClosure.execute(input);

        // Then
        verify(trueClosure, never()).accept(any());
        verify(falseClosure, times(1)).accept(input);
    }

    @Test
    public void testGetFalseClosure() {
        // Given

        // When
        Closure<Object> result = ifClosure.getFalseClosure();

        // Then
        assertSame(falseClosure, result);
    }

    @Test
    public void testGetPredicate() {
        // Given

        // When
        Predicate<Object> result = ifClosure.getPredicate();

        // Then
        assertSame(predicate, result);
    }

    @Test
    public void testGetTrueClosure() {
        // Given

        // When
        Closure<Object> result = ifClosure.getTrueClosure();

        // Then
        assertSame(trueClosure, result);
    }
}