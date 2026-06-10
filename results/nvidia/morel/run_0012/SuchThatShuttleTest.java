import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.ast.Visitor;
import net.hydromatic.morel.compile.SuchThatShuttle;
import net.hydromatic.morel.type.Binding;
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
public class SuchThatShuttleTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Core.Decl decl;

    @Mock
    private Core.Exp exp;

    @Mock
    private Core.From from;

    @Mock
    private Core.RecValDecl recValDecl;

    private SuchThatShuttle suchThatShuttle;

    @BeforeEach
    void setup() {
        suchThatShuttle = new SuchThatShuttle(typeSystem, null);
    }

    @Test
    void testContainsUnbounded() {
        // Given
        Holder<Boolean> found = Holder.of(false);
        when(decl.accept(any(Visitor.class))).thenAnswer(invocation -> {
            Visitor visitor = invocation.getArgument(0);
            visitor.visit(any(Core.Scan.class));
            return null;
        });

        // When
        boolean result = SuchThatShuttle.containsUnbounded(decl);

        // Then
        assertTrue(result);
    }

    @Test
    void testVisit() {
        // Given
        when(typeSystem.bindTyCon(any(), any())).thenReturn(null);

        // When
        Core.Exp result = suchThatShuttle.visit(exp);

        // Then
        assertEquals(exp, result);
    }

    @Test
    void testVisitFrom() {
        // Given
        when(typeSystem.bindTyCon(any(), any())).thenReturn(null);
        when(from.accept(any(SuchThatShuttle.class))).thenReturn(from);

        // When
        Core.Exp result = suchThatShuttle.visit(from);

        // Then
        assertEquals(from, result);
    }

    @Test
    void testVisitRecValDecl() {
        // Given
        List<Binding> bindings = new ArrayList<>();
        when(typeSystem.bindTyCon(any(), any())).thenReturn(null);
        when(recValDecl.accept(any(SuchThatShuttle.class))).thenReturn(recValDecl);

        // When
        Core.RecValDecl result = suchThatShuttle.visit(recValDecl);

        // Then
        assertEquals(recValDecl, result);
    }

    @Test
    void testPush() {
        // Given

        // When
        SuchThatShuttle result = suchThatShuttle.push(null);

        // Then
        assertNotNull(result);
    }
}