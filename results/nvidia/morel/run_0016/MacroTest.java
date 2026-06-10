import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Macro;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MacroTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Core.Exp exp;

    @Mock
    private Type argType;

    private Macro macro;

    @BeforeEach
    public void setup() {
        macro = new Macro() {
            @Override
            public Core.Exp expand(TypeSystem typeSystem, Environment env, Type argType) {
                return exp;
            }
        };
    }

    @Test
    public void testExpand() {
        // Given
        Environment env = mock(Environment.class);

        // When
        Core.Exp result = macro.expand(typeSystem, env, argType);

        // Then
        assertEquals(exp, result);
        verify(typeSystem, times(1)).apply(any(Type.class));
    }

    @Test
    public void testExpand_TypeSystemNull() {
        // Given
        Environment env = mock(Environment.class);
        typeSystem = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> macro.expand(typeSystem, env, argType));
    }

    @Test
    public void testExpand_EnvironmentNull() {
        // Given
        Environment env = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> macro.expand(typeSystem, env, argType));
    }

    @Test
    public void testExpand_ArgTypeNull() {
        // Given
        argType = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> macro.expand(typeSystem, mock(Environment.class), argType));
    }
}