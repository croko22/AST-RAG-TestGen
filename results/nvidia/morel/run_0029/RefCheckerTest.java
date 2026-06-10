import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.EnvVisitor;
import net.hydromatic.morel.compile.RefChecker;
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
public class RefCheckerTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private net.hydromatic.morel.compile.Environment env;

    private RefChecker refChecker;

    @BeforeEach
    public void setup() {
        refChecker = RefChecker.of(typeSystem, env);
    }

    @Test
    public void testOf() {
        // Given
        TypeSystem typeSystem = mock(TypeSystem.class);
        net.hydromatic.morel.compile.Environment env = mock(net.hydromatic.morel.compile.Environment.class);

        // When
        RefChecker refChecker = RefChecker.of(typeSystem, env);

        // Then
        assertNotNull(refChecker);
        assertSame(typeSystem, refChecker.typeSystem);
        assertSame(env, refChecker.env);
    }

    @Test
    public void testPush() {
        // Given
        net.hydromatic.morel.compile.Environment newEnv = mock(net.hydromatic.morel.compile.Environment.class);

        // When
        RefChecker newRefChecker = refChecker.push(newEnv);

        // Then
        assertNotNull(newRefChecker);
        assertSame(typeSystem, newRefChecker.typeSystem);
        assertSame(newEnv, newRefChecker.env);
    }

    @Test
    public void testVisit_Core_Id() {
        // Given
        Core.Id id = mock(Core.Id.class);
        when(env.getOpt(any())).thenReturn(mock(net.hydromatic.morel.compile.Binding.class));

        // When
        refChecker.visit(id);

        // Then
        verify(env).getOpt(any());
    }

    @Test
    public void testVisit_Core_Id_NotFound() {
        // Given
        Core.Id id = mock(Core.Id.class);
        when(env.getOpt(any())).thenReturn(null);

        // When and Then
        assertThrows(NullPointerException.class, () -> refChecker.visit(id));
    }
}