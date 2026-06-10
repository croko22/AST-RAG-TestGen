import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.ast.Shuttle;
import net.hydromatic.morel.compile.EnvShuttle;
import net.hydromatic.morel.compile.Environment;
import net.hydromatic.morel.type.Binding;
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
public class EnvShuttleTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Environment env;

    private EnvShuttle envShuttle;

    @BeforeEach
    void setup() {
        envShuttle = new EnvShuttle(typeSystem, env) {
            @Override
            protected EnvShuttle push(Environment env) {
                return new EnvShuttle(typeSystem, env) {
                    @Override
                    protected EnvShuttle push(Environment env) {
                        return this;
                    }
                };
            }
        };
    }

    @Test
    void testPush() {
        // Given
        Environment newEnv = mock(Environment.class);

        // When
        EnvShuttle newShuttle = envShuttle.push(newEnv);

        // Then
        assertNotSame(envShuttle, newShuttle);
        assertEquals(newEnv, newShuttle.env);
    }

    @Test
    void testBind() {
        // Given
        Binding binding = mock(Binding.class);

        // When
        EnvShuttle newShuttle = envShuttle.bind(binding);

        // Then
        assertNotSame(envShuttle, newShuttle);
        verify(env).bind(binding);
    }

    @Test
    void testBindList() {
        // Given
        List<Binding> bindings = new ArrayList<>();
        bindings.add(mock(Binding.class));

        // When
        EnvShuttle newShuttle = envShuttle.bind(bindings);

        // Then
        assertNotSame(envShuttle, newShuttle);
        verify(env).bindAll(bindings);
    }

    @Test
    void testVisitFn() {
        // Given
        Core.Fn fn = mock(Core.Fn.class);
        Core.IdPat idPat = mock(Core.IdPat.class);
        Core.Exp exp = mock(Core.Exp.class);

        when(fn.idPat.accept(any(Shuttle.class))).thenReturn(idPat);
        when(fn.exp.accept(any(Shuttle.class))).thenReturn(exp);

        // When
        Core.Fn result = envShuttle.visit(fn);

        // Then
        assertNotSame(fn, result);
        assertEquals(idPat, result.idPat);
        assertEquals(exp, result.exp);
    }

    @Test
    void testVisitMatch() {
        // Given
        Core.Match match = mock(Core.Match.class);
        Core.Pat pat = mock(Core.Pat.class);
        Core.Exp exp = mock(Core.Exp.class);

        when(match.pat.accept(any(Shuttle.class))).thenReturn(pat);
        when(match.exp.accept(any(Shuttle.class))).thenReturn(exp);

        // When
        Core.Match result = envShuttle.visit(match);

        // Then
        assertNotSame(match, result);
        assertEquals(pat, result.pat);
        assertEquals(exp, result.exp);
    }

    @Test
    void testVisitLet() {
        // Given
        Core.Let let = mock(Core.Let.class);
        Core.Decl decl = mock(Core.Decl.class);
        Core.Exp exp = mock(Core.Exp.class);

        when(let.decl.accept(any(Shuttle.class))).thenReturn(decl);
        when(let.exp.accept(any(Shuttle.class))).thenReturn(exp);

        // When
        Core.Exp result = envShuttle.visit(let);

        // Then
        assertNotSame(let, result);
        assertEquals(decl, result.decl);
        assertEquals(exp, result.exp);
    }

    @Test
    void testVisitLocal() {
        // Given
        Core.Local local = mock(Core.Local.class);
        Core.DataType dataType = mock(Core.DataType.class);
        Core.Exp exp = mock(Core.Exp.class);

        when(local.dataType.accept(any(Shuttle.class))).thenReturn(dataType);
        when(local.exp.accept(any(Shuttle.class))).thenReturn(exp);

        // When
        Core.Exp result = envShuttle.visit(local);

        // Then
        assertNotSame(local, result);
        assertEquals(dataType, result.dataType);
        assertEquals(exp, result.exp);
    }

    @Test
    void testVisitRecValDecl() {
        // Given
        Core.RecValDecl recValDecl = mock(Core.RecValDecl.class);
        List<Core.NonRecValDecl> list = new ArrayList<>();
        list.add(mock(Core.NonRecValDecl.class));

        when(recValDecl.list).thenReturn(list);

        // When
        Core.RecValDecl result = envShuttle.visit(recValDecl);

        // Then
        assertNotSame(recValDecl, result);
        assertEquals(list, result.list);
    }

    @Test
    void testVisitFrom() {
        // Given
        Core.From from = mock(Core.From.class);
        List<Core.FromStep> steps = new ArrayList<>();
        steps.add(mock(Core.FromStep.class));

        when(from.steps).thenReturn(steps);

        // When
        Core.From result = envShuttle.visit(from);

        // Then
        assertNotSame(from, result);
        assertEquals(steps, result.steps);
    }
}