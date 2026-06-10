import org.apache.commons.collections4.Closure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClosureTest {

    @Mock
    private Closure<String> closure;

    @BeforeEach
    void setup() {
        // No setup needed
    }

    @Test
    public void testAccept() {
        // Given
        String input = "testInput";

        // When
        closure.accept(input);

        // Then
        verify(closure, times(1)).execute(input);
    }

    @Test
    public void testExecute() {
        // Given
        String input = "testInput";

        // When
        closure.execute(input);

        // Then
        verify(closure, times(1)).execute(input);
    }

    @Test
    public void testExecute_ClassCastException() {
        // Given
        Closure<Object> objectClosure = mock(Closure.class);
        Object input = new Object();

        // When and Then
        assertThrows(ClassCastException.class, () -> objectClosure.execute(input));
    }

    @Test
    public void testExecute_IllegalArgumentException() {
        // Given
        Closure<Object> objectClosure = mock(Closure.class);
        Object input = new Object();

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> objectClosure.execute(input));
    }

    @Test
    public void testExecute_FunctorException() {
        // Given
        Closure<Object> objectClosure = mock(Closure.class);
        Object input = new Object();

        // When and Then
        assertThrows(RuntimeException.class, () -> objectClosure.execute(input));
    }

    @Test
    public void testAndThen() {
        // Given
        Closure<String> firstClosure = mock(Closure.class);
        Closure<String> secondClosure = mock(Closure.class);
        String input = "testInput";

        // When
        Consumer<String> andThen = firstClosure.andThen(secondClosure);
        andThen.accept(input);

        // Then
        verify(firstClosure, times(1)).accept(input);
        verify(secondClosure, times(1)).accept(input);
    }
}