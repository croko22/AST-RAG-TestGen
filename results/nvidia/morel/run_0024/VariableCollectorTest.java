import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.VariableCollector;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.Environment;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Deque;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VariableCollectorTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Environment env;

    @Mock
    private BiConsumer<Core.Id, VariableCollector.Scope> consumer;

    private VariableCollector variableCollector;

    @BeforeEach
    void setup() {
        variableCollector = VariableCollector.create(typeSystem, env, consumer);
    }

    @Test
    void testCreate() {
        // Given
        // When
        VariableCollector createdVariableCollector = VariableCollector.create(typeSystem, env, consumer);
        // Then
        assertNotNull(createdVariableCollector);
    }

    @Test
    void testVisitX_FreeVariable() {
        // Given
        Core.Id id = mock(Core.Id.class);
        when(env.getOpt2(id.idPat)).thenReturn(mock(Pair.class));
        Deque<Environment> lambdaEnvStack = new java.util.ArrayDeque<>();
        lambdaEnvStack.push(mock(Environment.class));
        when(variableCollector.lambdaEnvStack).thenReturn(lambdaEnvStack);
        // When
        variableCollector.visitX(id);
        // Then
        verify(consumer).accept(any(), any());
    }

    @Test
    void testVisitX_NotFreeVariable() {
        // Given
        Core.Id id = mock(Core.Id.class);
        when(env.getOpt2(id.idPat)).thenReturn(null);
        Deque<Environment> lambdaEnvStack = new java.util.ArrayDeque<>();
        lambdaEnvStack.push(mock(Environment.class));
        when(variableCollector.lambdaEnvStack).thenReturn(lambdaEnvStack);
        // When
        variableCollector.visitX(id);
        // Then
        verify(consumer, never()).accept(any(), any());
    }

    @Test
    void testVisitX_NoLambdaBoundary() {
        // Given
        Core.Id id = mock(Core.Id.class);
        when(env.getOpt2(id.idPat)).thenReturn(mock(Pair.class));
        Deque<Environment> lambdaEnvStack = new java.util.ArrayDeque<>();
        when(variableCollector.lambdaEnvStack).thenReturn(lambdaEnvStack);
        // When
        variableCollector.visitX(id);
        // Then
        verify(consumer, never()).accept(any(), any());
    }
}