import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.Relationalizer;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.FnType;
import net.hydromatic.morel.type.RecordLikeType;
import net.hydromatic.morel.type.RecordType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RelationalizerTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Core core;

    private Relationalizer relationalizer;

    @BeforeEach
    void setup() {
        relationalizer = Relationalizer.of(typeSystem, null);
    }

    @Test
    void testOf() {
        // Given
        TypeSystem typeSystem = mock(TypeSystem.class);
        Environment env = null;

        // When
        Relationalizer relationalizer = Relationalizer.of(typeSystem, env);

        // Then
        assertNotNull(relationalizer);
    }

    @Test
    void testPush() {
        // Given
        Environment env = null;

        // When
        Relationalizer relationalizer2 = relationalizer.push(env);

        // Then
        assertNotNull(relationalizer2);
    }

    @Test
    void testVisitApply() {
        // Given
        Core.Apply apply = mock(Core.Apply.class);
        when(apply.fn.op).thenReturn(Core.Op.APPLY);

        // When
        Core.Exp result = relationalizer.visit(apply);

        // Then
        assertNotNull(result);
    }

    @Test
    void testToFrom() {
        // Given
        Core.Exp exp = mock(Core.Exp.class);
        when(exp.type).thenReturn(mock(RecordLikeType.class));

        // When
        Core.From from = relationalizer.toFrom(exp);

        // Then
        assertNotNull(from);
    }

    @Test
    void testVisitFrom() {
        // Given
        Core.From from = mock(Core.From.class);

        // When
        Core.Exp result = relationalizer.visit(from);

        // Then
        assertNotNull(result);
    }
}