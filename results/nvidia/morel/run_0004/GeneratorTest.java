import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Generator;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneratorTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Core.Exp exp;

    @Mock
    private Core.Pat pat;

    private Generator generator;

    @BeforeEach
    void setup() {
        List<Core.NamedPat> freePats = new ArrayList<>();
        generator = new Generator(exp, freePats, pat, Generator.Cardinality.SINGLE, true, true) {
            @Override
            Core.Exp simplify(TypeSystem typeSystem, Core.Pat pat, Core.Exp exp) {
                return null;
            }
        };
    }

    @Test
    void testSimplify() {
        // Given
        when(typeSystem.apply(any())).thenReturn(exp);

        // When
        Core.Exp result = generator.simplify(typeSystem, pat, exp);

        // Then
        assertNull(result);
        verify(typeSystem, times(1)).apply(any());
    }

    @Test
    void testRangeExp() {
        // When
        assertThrows(UnsupportedOperationException.class, () -> generator.rangeExp(typeSystem));
    }

    @Test
    void testCardinality() {
        // Given
        Generator.Cardinality cardinality = Generator.Cardinality.SINGLE;

        // When
        Generator.Cardinality result = cardinality.max(Generator.Cardinality.FINITE);

        // Then
        assertEquals(Generator.Cardinality.FINITE, result);
    }
}