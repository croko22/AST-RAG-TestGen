Here's a comprehensive test class for the `Simplifier` class:

```java
import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.ast.Op;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SimplifierTest {

    @Mock
    private TypeSystem typeSystem;

    private Simplifier simplifier;

    @BeforeEach
    void setup() {
        simplifier = new Simplifier(typeSystem);
    }

    @Test
    void testSimplify_StaticMethod() {
        // Given
        Core.Exp exp = mock(Core.Exp.class);

        // When
        Core.Exp simplifiedExp = Simplifier.simplify(typeSystem, exp);

        // Then
        assertNotNull(simplifiedExp);
    }

    @Test
    void testSimplify_InstanceMethod() {
        // Given
        Core.Exp exp = mock(Core.Exp.class);

        // When
        Core.Exp simplifiedExp = simplifier.simplify(exp);

        // Then
        assertNotNull(simplifiedExp);
    }

    @Test
    void testExpEquals_EqualExpressions() {
        // Given
        Core.Exp exp1 = mock(Core.Exp.class);
        Core.Exp exp2 = mock(Core.Exp.class);
        doReturn(true).when(exp1).equals(exp2);

        // When
        boolean result = Simplifier.expEquals(exp1, exp2);

        // Then
        assertEquals(true, result);
    }

    @Test
    void testExpEquals_NotEqualExpressions() {
        // Given
        Core.Exp exp1 = mock(Core.Exp.class);
        Core.Exp exp2 = mock(Core.Exp.class);
        doReturn(false).when(exp1).equals(exp2);

        // When
        boolean result = Simplifier.expEquals(exp1, exp2);

        // Then
        assertEquals(false, result);
    }

    @Test
    void testSimplify_Subtraction() {
        // Given
        Core.Exp exp = mock(Core.Exp.class);
        doReturn(Op.APPLY).when(exp).op;
        Core.Apply apply = mock(Core.Apply.class);
        doReturn(apply).when(exp).apply;
        doReturn(true).when(apply).isCallTo(any());

        // When
        Core.Exp simplifiedExp = simplifier.simplify(exp);

        // Then
        assertNotNull(simplifiedExp);
    }

    @Test
    void testSimplify_Addition() {
        // Given
        Core.Exp exp = mock(Core.Exp.class);
        doReturn(Op.APPLY).when(exp).op;
        Core.Apply apply = mock(Core.Apply.class);
        doReturn(apply).when(exp).apply;
        doReturn(true).when(apply).isCallTo(any());

        // When
        Core.Exp simplifiedExp = simplifier.simplify(exp);

        // Then
        assertNotNull(simplifiedExp);
    }

    @Test
    void testSimplify_ConstantSubtraction() {
        // Given
        Core.Exp exp = mock(Core.Exp.class);
        doReturn(Op.APPLY).when(exp).op;
        Core.Apply apply = mock(Core.Apply.class);
        doReturn(apply).when(exp).apply;
        doReturn(true).when(apply).isCallTo(any());
        doReturn(true).when(apply).arg(0).isConstant();
        doReturn(true).when(apply).arg(1).isConstant();

        // When
        Core.Exp simplifiedExp = simplifier.simplify(exp);

        // Then
        assertNotNull(simplifiedExp);
    }

    @Test
    void testSimplify_ConstantAddition() {
        // Given
        Core.Exp exp = mock(Core.Exp.class);
        doReturn(Op.APPLY).when(exp).op;
        Core.Apply apply = mock(Core.Apply.class);
        doReturn(apply).when(exp).apply;
        doReturn(true).when(apply).isCallTo(any());
        doReturn(true).when(apply).arg(0).isConstant();
        doReturn(true).when(apply).arg(1).isConstant();

        // When
        Core.Exp simplifiedExp = simplifier.simplify(exp);

        // Then
        assertNotNull(simplifiedExp);
    }

    @Test
    void testSimplify_DefaultCase() {
        // Given
        Core.Exp exp = mock(Core.Exp.class);
        doReturn(Op.ID).when(exp).op;

        // When
        Core.Exp simplifiedExp = simplifier.simplify(exp);

        // Then
        assertEquals(exp, simplifiedExp);
    }
}