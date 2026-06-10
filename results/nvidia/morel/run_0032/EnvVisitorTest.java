import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.ast.Visitor;
import net.hydromatic.morel.compile.EnvVisitor;
import net.hydromatic.morel.compile.Environment;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnvVisitorTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Environment env;

    @Mock
    private Deque<EnvVisitor.FromContext> fromStack;

    private EnvVisitor envVisitor;

    @BeforeEach
    void setup() {
        envVisitor = new EnvVisitor(typeSystem, env, fromStack) {
            @Override
            protected EnvVisitor push(Environment env) {
                return null;
            }
        };
    }

    @Test
    void testVisitStep() {
        // Given
        Core.FromStep step = mock(Core.FromStep.class);
        Core.StepEnv stepEnv = mock(Core.StepEnv.class);
        EnvVisitor.FromContext fromContext = new EnvVisitor.FromContext(envVisitor, step, stepEnv);

        // When
        envVisitor.visitStep(step, stepEnv);

        // Then
        verify(fromStack).push(fromContext);
        verify(step).accept(any(EnvVisitor.class));
        verify(fromStack).pop();
    }

    @Test
    void testBind() {
        // Given
        Binding binding = mock(Binding.class);

        // When
        EnvVisitor newEnvVisitor = envVisitor.bind(binding);

        // Then
        assertNotSame(envVisitor, newEnvVisitor);
    }

    @Test
    void testBindBindings() {
        // Given
        List<Binding> bindings = new ArrayList<>();
        Binding binding = mock(Binding.class);
        bindings.add(binding);

        // When
        EnvVisitor newEnvVisitor = envVisitor.bind(bindings);

        // Then
        assertNotSame(envVisitor, newEnvVisitor);
    }

    @Test
    void testVisitFn() {
        // Given
        Core.Fn fn = mock(Core.Fn.class);
        Core.IdPat idPat = mock(Core.IdPat.class);
        when(fn.idPat).thenReturn(idPat);
        Core.Exp exp = mock(Core.Exp.class);
        when(fn.exp).thenReturn(exp);

        // When
        envVisitor.visit(fn);

        // Then
        verify(idPat).accept(any(EnvVisitor.class));
        verify(exp).accept(any(EnvVisitor.class));
    }

    @Test
    void testVisitMatch() {
        // Given
        Core.Match match = mock(Core.Match.class);
        Core.Pat pat = mock(Core.Pat.class);
        when(match.pat).thenReturn(pat);
        Core.Exp exp = mock(Core.Exp.class);
        when(match.exp).thenReturn(exp);

        // When
        envVisitor.visit(match);

        // Then
        verify(pat).accept(any(EnvVisitor.class));
        verify(exp).accept(any(EnvVisitor.class));
    }

    @Test
    void testVisitLet() {
        // Given
        Core.Let let = mock(Core.Let.class);
        Core.Decl decl = mock(Core.Decl.class);
        when(let.decl).thenReturn(decl);
        Core.Exp exp = mock(Core.Exp.class);
        when(let.exp).thenReturn(exp);

        // When
        envVisitor.visit(let);

        // Then
        verify(decl).accept(any(EnvVisitor.class));
        verify(exp).accept(any(EnvVisitor.class));
    }

    @Test
    void testVisitLocal() {
        // Given
        Core.Local local = mock(Core.Local.class);
        Core.DataType dataType = mock(Core.DataType.class);
        when(local.dataType).thenReturn(dataType);
        Core.Exp exp = mock(Core.Exp.class);
        when(local.exp).thenReturn(exp);

        // When
        envVisitor.visit(local);

        // Then
        verify(dataType).accept(any(EnvVisitor.class));
        verify(exp).accept(any(EnvVisitor.class));
    }

    @Test
    void testVisitRecValDecl() {
        // Given
        Core.RecValDecl recValDecl = mock(Core.RecValDecl.class);
        List<Core.NonRecValDecl> list = new ArrayList<>();
        when(recValDecl.list).thenReturn(list);

        // When
        envVisitor.visit(recValDecl);

        // Then
        verify(recValDecl).accept(any(EnvVisitor.class));
    }

    @Test
    void testVisitFrom() {
        // Given
        Core.From from = mock(Core.From.class);
        List<Core.FromStep> steps = new ArrayList<>();
        when(from.steps).thenReturn(steps);

        // When
        envVisitor.visit(from);

        // Then
        verify(from).accept(any(EnvVisitor.class));
    }

    @Test
    void testVisitScan() {
        // Given
        Core.Scan scan = mock(Core.Scan.class);
        Core.Pat pat = mock(Core.Pat.class);
        when(scan.pat).thenReturn(pat);
        Core.Exp exp = mock(Core.Exp.class);
        when(scan.exp).thenReturn(exp);
        Core.Exp condition = mock(Core.Exp.class);
        when(scan.condition).thenReturn(condition);

        // When
        envVisitor.visit(scan);

        // Then
        verify(pat).accept(any(EnvVisitor.class));
        verify(exp).accept(any(EnvVisitor.class));
        verify(condition).accept(any(EnvVisitor.class));
    }

    @Test
    void testVisitAggregate() {
        // Given
        Core.Aggregate aggregate = mock(Core.Aggregate.class);
        Core.Exp aggregateExp = mock(Core.Exp.class);
        when(aggregate.aggregate).thenReturn(aggregateExp);
        Core.Exp argument = mock(Core.Exp.class);
        when(aggregate.argument).thenReturn(argument);

        // When
        envVisitor.visit(aggregate);

        // Then
        verify(aggregateExp).accept(any(EnvVisitor.class));
        verify(argument).accept(any(EnvVisitor.class));
    }
}