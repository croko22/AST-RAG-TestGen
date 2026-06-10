import net.hydromatic.morel.eval.Applicable;
import net.hydromatic.morel.eval.Code;
import net.hydromatic.morel.eval.Describable;
import net.hydromatic.morel.eval.Describer;
import net.hydromatic.morel.eval.EvalEnv;
import net.hydromatic.morel.eval.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicableTest {

    @Mock
    private Stack stack;

    @Mock
    private Object argValue;

    @Mock
    private EvalEnv env;

    private Applicable applicable;

    @BeforeEach
    void setup() {
        applicable = new Applicable() {
            @Override
            public Object apply(Stack stack, Object argValue) {
                return null;
            }

            @Override
            public void describe(Describer describer) {
                // Empty implementation
            }
        };
    }

    @Test
    void testApply() {
        // Given
        when(stack.push(any())).thenReturn(stack);

        // When
        Object result = applicable.apply(stack, argValue);

        // Then
        verify(stack, times(1)).push(any());
        assertNull(result);
    }

    @Test
    void testApplyDeprecated() {
        // Given
        doThrow(new UnsupportedOperationException()).when(applicable).apply(env, argValue);

        // When
        assertThrows(UnsupportedOperationException.class, () -> applicable.apply(env, argValue));

        // Then
        verify(applicable, times(1)).apply(env, argValue);
    }

    @Test
    void testAsCode() {
        // Given
        Code code = applicable.asCode();

        // When
        Describer describer = mock(Describer.class);
        code.describe(describer);

        // Then
        verify(describer, times(1)).start(anyString(), any());
        assertNotNull(code);
    }

    @Test
    void testDescribe() {
        // Given
        Describer describer = mock(Describer.class);

        // When
        applicable.describe(describer);

        // Then
        verify(describer, times(1)).start(anyString(), any());
    }
}