import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Replacer;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReplacerTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Core.Exp exp;

    @Mock
    private Core.Id id;

    @Mock
    private Core.NamedPat namedPat;

    private Replacer replacer;

    @BeforeEach
    void setup() {
        Map<Core.NamedPat, Core.Exp> substitution = new HashMap<>();
        substitution.put(namedPat, exp);
        replacer = new Replacer(typeSystem, mock(Environment.class), substitution);
    }

    @Test
    void testSubstitute_WithSubstitution() {
        // Given
        when(id.idPat).thenReturn(namedPat);

        // When
        Core.Exp result = replacer.visit(id);

        // Then
        assertEquals(exp, result);
    }

    @Test
    void testSubstitute_WithoutSubstitution() {
        // Given
        when(id.idPat).thenReturn(mock(Core.NamedPat.class));

        // When
        Core.Exp result = replacer.visit(id);

        // Then
        assertEquals(id, result);
    }

    @Test
    void testPush() {
        // When
        Replacer newReplacer = replacer.push(mock(Environment.class));

        // Then
        assertNotNull(newReplacer);
        assertEquals(replacer.typeSystem, newReplacer.typeSystem);
        assertEquals(replacer.substitution, newReplacer.substitution);
    }

    @Test
    void testVisit() {
        // Given
        when(id.idPat).thenReturn(namedPat);

        // When
        Core.Exp result = replacer.visit(id);

        // Then
        assertEquals(exp, result);
    }

    @Test
    void testSubstitute_StaticMethod_WithSubstitution() {
        // Given
        Map<Core.NamedPat, Core.Exp> substitution = new HashMap<>();
        substitution.put(namedPat, exp);

        // When
        Core.Exp result = Replacer.substitute(typeSystem, mock(Environment.class), substitution, id);

        // Then
        assertEquals(exp, result);
    }

    @Test
    void testSubstitute_StaticMethod_WithoutSubstitution() {
        // Given
        Map<Core.NamedPat, Core.Exp> substitution = new HashMap<>();

        // When
        Core.Exp result = Replacer.substitute(typeSystem, mock(Environment.class), substitution, id);

        // Then
        assertEquals(id, result);
    }

    @Test
    void testSubstituteFromStep_StaticMethod_WithSubstitution() {
        // Given
        Map<Core.NamedPat, Core.Exp> substitution = new HashMap<>();
        substitution.put(namedPat, exp);
        Core.FromStep fromStep = mock(Core.FromStep.class);

        // When
        Core.FromStep result = Replacer.substitute(typeSystem, mock(Environment.class), substitution, fromStep);

        // Then
        assertNotNull(result);
    }

    @Test
    void testSubstituteFromStep_StaticMethod_WithoutSubstitution() {
        // Given
        Map<Core.NamedPat, Core.Exp> substitution = new HashMap<>();
        Core.FromStep fromStep = mock(Core.FromStep.class);

        // When
        Core.FromStep result = Replacer.substitute(typeSystem, mock(Environment.class), substitution, fromStep);

        // Then
        assertEquals(fromStep, result);
    }
}