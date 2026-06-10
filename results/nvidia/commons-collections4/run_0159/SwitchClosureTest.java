import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.SwitchClosure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwitchClosureTest {

    @Mock
    private Predicate<String> predicate1;

    @Mock
    private Predicate<String> predicate2;

    @Mock
    private Closure<String> closure1;

    @Mock
    private Closure<String> closure2;

    @Mock
    private Closure<String> defaultClosure;

    private SwitchClosure<String> switchClosure;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        when(predicate1.test(any())).thenReturn(true);
        when(predicate2.test(any())).thenReturn(false);
        doNothing().when(closure1).execute(any());
        doNothing().when(closure2).execute(any());
        doNothing().when(defaultClosure).execute(any());
    }

    @Test
    public void testSwitchClosure_Map() {
        // Given
        Map<Predicate<String>, Closure<String>> predicatesAndClosures = new HashMap<>();
        predicatesAndClosures.put(predicate1, closure1);
        predicatesAndClosures.put(predicate2, closure2);
        predicatesAndClosures.put(null, defaultClosure);

        // When
        Closure<String> result = SwitchClosure.switchClosure(predicatesAndClosures);

        // Then
        assertNotNull(result);
        result.execute("input");
        verify(predicate1, times(1)).test("input");
        verify(closure1, times(1)).execute("input");
        verify(predicate2, never()).test("input");
        verify(closure2, never()).execute("input");
        verify(defaultClosure, never()).execute("input");
    }

    @Test
    public void testSwitchClosure_Arrays() {
        // Given
        Predicate<String>[] predicates = new Predicate[]{predicate1, predicate2};
        Closure<String>[] closures = new Closure[]{closure1, closure2};

        // When
        Closure<String> result = SwitchClosure.switchClosure(predicates, closures, defaultClosure);

        // Then
        assertNotNull(result);
        result.execute("input");
        verify(predicate1, times(1)).test("input");
        verify(closure1, times(1)).execute("input");
        verify(predicate2, never()).test("input");
        verify(closure2, never()).execute("input");
        verify(defaultClosure, never()).execute("input");
    }

    @Test
    public void testExecute_PredicateMatches() {
        // Given
        switchClosure = new SwitchClosure<>(new Predicate[]{predicate1, predicate2}, new Closure[]{closure1, closure2}, defaultClosure);

        // When
        switchClosure.execute("input");

        // Then
        verify(predicate1, times(1)).test("input");
        verify(closure1, times(1)).execute("input");
        verify(predicate2, never()).test("input");
        verify(closure2, never()).execute("input");
        verify(defaultClosure, never()).execute("input");
    }

    @Test
    public void testExecute_NoPredicateMatches() {
        // Given
        when(predicate1.test(any())).thenReturn(false);
        when(predicate2.test(any())).thenReturn(false);
        switchClosure = new SwitchClosure<>(new Predicate[]{predicate1, predicate2}, new Closure[]{closure1, closure2}, defaultClosure);

        // When
        switchClosure.execute("input");

        // Then
        verify(predicate1, times(1)).test("input");
        verify(predicate2, times(1)).test("input");
        verify(closure1, never()).execute("input");
        verify(closure2, never()).execute("input");
        verify(defaultClosure, times(1)).execute("input");
    }

    @Test
    public void testGetDefaultClosure() {
        // Given
        switchClosure = new SwitchClosure<>(new Predicate[]{predicate1, predicate2}, new Closure[]{closure1, closure2}, defaultClosure);

        // When
        Closure<String> result = switchClosure.getDefaultClosure();

        // Then
        assertNotNull(result);
        assertSame(defaultClosure, result);
    }
}