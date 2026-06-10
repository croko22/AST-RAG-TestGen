import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.GlobalFreeVarCollector;
import net.hydromatic.morel.eval.Unit;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GlobalFreeVarCollectorTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Core.Exp exp;

    @Mock
    private Binding binding;

    private List<String> freeVars;

    @BeforeEach
    void setup() {
        freeVars = new ArrayList<>();
    }

    @Test
    void testCollect_FreeGlobalVars() {
        // Given
        when(typeSystem.lookup(any())).thenReturn(null);
        when(binding.value).thenReturn("someValue");
        when(binding instanceof Binding).thenReturn(true);
        when(exp.accept(any())).thenReturn(exp);
        Consumer<String> consumer = freeVars::add;

        // When
        GlobalFreeVarCollector.collect(typeSystem, mock(Environment.class), exp, consumer);

        // Then
        verify(typeSystem, times(1)).lookup(any());
        verify(binding, times(1)).value;
        assertEquals(1, freeVars.size());
    }

    @Test
    void testCollect_NoFreeGlobalVars() {
        // Given
        when(typeSystem.lookup(any())).thenReturn(null);
        when(binding.value).thenReturn(Unit.INSTANCE);
        when(binding instanceof Binding).thenReturn(true);
        when(exp.accept(any())).thenReturn(exp);
        Consumer<String> consumer = freeVars::add;

        // When
        GlobalFreeVarCollector.collect(typeSystem, mock(Environment.class), exp, consumer);

        // Then
        verify(typeSystem, times(1)).lookup(any());
        verify(binding, times(1)).value;
        assertEquals(0, freeVars.size());
    }

    @Test
    void testCollect_CodePlaceholder() {
        // Given
        when(typeSystem.lookup(any())).thenReturn(null);
        when(binding.value).thenReturn(new Code());
        when(binding instanceof Binding).thenReturn(true);
        when(exp.accept(any())).thenReturn(exp);
        Consumer<String> consumer = freeVars::add;

        // When
        GlobalFreeVarCollector.collect(typeSystem, mock(Environment.class), exp, consumer);

        // Then
        verify(typeSystem, times(1)).lookup(any());
        verify(binding, times(1)).value;
        assertEquals(0, freeVars.size());
    }

    @Test
    void testPush_NewEnvVisitor() {
        // Given
        GlobalFreeVarCollector collector = new GlobalFreeVarCollector(typeSystem, mock(Environment.class), new ArrayDeque<>(), freeVars::add);

        // When
        EnvVisitor newCollector = collector.push(mock(Environment.class));

        // Then
        assertNotNull(newCollector);
        assertNotSame(collector, newCollector);
    }

    @Test
    void testVisit_Id() {
        // Given
        Core.Id id = mock(Core.Id.class);
        when(id.idPat).thenReturn(mock(Core.IdPat.class));
        when(id.accept(any())).thenReturn(id);
        when(binding.value).thenReturn("someValue");
        when(binding instanceof Binding).thenReturn(true);
        Consumer<String> consumer = freeVars::add;

        // When
        GlobalFreeVarCollector collector = new GlobalFreeVarCollector(typeSystem, mock(Environment.class), new ArrayDeque<>(), consumer);
        collector.visit(id);

        // Then
        verify(id, times(1)).accept(any());
        assertEquals(1, freeVars.size());
    }

    @Test
    void testVisit_Fn() {
        // Given
        Core.Fn fn = mock(Core.Fn.class);
        when(fn.accept(any())).thenReturn(fn);

        // When
        GlobalFreeVarCollector collector = new GlobalFreeVarCollector(typeSystem, mock(Environment.class), new ArrayDeque<>(), freeVars::add);
        collector.visit(fn);

        // Then
        verify(fn, times(1)).accept(any());
        assertEquals(0, freeVars.size());
    }
}