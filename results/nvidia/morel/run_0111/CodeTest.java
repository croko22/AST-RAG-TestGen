import net.hydromatic.morel.eval.Code;
import net.hydromatic.morel.eval.Describable;
import net.hydromatic.morel.eval.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CodeTest {

    @Mock
    private Stack stack;

    @Mock
    private Code code;

    @BeforeEach
    void setup() {
        // No setup required
    }

    @Test
    void testEval() {
        // Given
        when(code.eval(stack)).thenCallRealMethod();

        // When
        Object result = code.eval(stack);

        // Then
        verify(code, times(1)).eval(stack);
        assertNotNull(result);
    }

    @Test
    void testEval_ThrowsUnsupportedOperationException() {
        // Given
        Code defaultCode = new Code() {
            @Override
            public String describe() {
                return "Default Code";
            }
        };

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> defaultCode.eval(stack));
    }

    @Test
    void testMaxSlots() {
        // Given
        when(code.maxSlots()).thenCallRealMethod();

        // When
        int maxSlots = code.maxSlots();

        // Then
        verify(code, times(1)).maxSlots();
        assertEquals(0, maxSlots);
    }

    @Test
    void testIsConstant() {
        // Given
        when(code.isConstant()).thenCallRealMethod();

        // When
        boolean isConstant = code.isConstant();

        // Then
        verify(code, times(1)).isConstant();
        assertFalse(isConstant);
    }
}