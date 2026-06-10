import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.FreeFinder;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FreeFinderTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Core.Exp exp;

    private FreeFinder freeFinder;

    @BeforeEach
    void setup() {
        freeFinder = new FreeFinder(typeSystem, Environments.empty(), new ArrayDeque<>(), set -> {
            // Consumer implementation
        });
    }

    @Test
    void testFreePats() {
        // Given
        when(exp.accept(any())).thenReturn(exp);

        // When
        Set<Core.NamedPat> freePats = FreeFinder.freePats(typeSystem, exp);

        // Then
        assertNotNull(freePats);
        verify(exp, times(1)).accept(any());
    }

    @Test
    void testFreePats_EmptySet() {
        // Given
        when(exp.accept(any())).thenReturn(exp);

        // When
        Set<Core.NamedPat> freePats = FreeFinder.freePats(typeSystem, exp);

        // Then
        assertNotNull(freePats);
        assertTrue(freePats.isEmpty());
    }

    @Test
    void testPush() {
        // Given
        Environment env = Environments.empty();

        // When
        FreeFinder newFreeFinder = freeFinder.push(env);

        // Then
        assertNotNull(newFreeFinder);
        assertNotSame(freeFinder, newFreeFinder);
    }

    @Test
    void testVisit() {
        // Given
        Core.Id id = mock(Core.Id.class);
        when(id.idPat).thenReturn(mock(Core.NamedPat.class));

        // When
        freeFinder.visit(id);

        // Then
        verify(id, times(1)).idPat;
    }

    @Test
    void testVisit_IdPatNull() {
        // Given
        Core.Id id = mock(Core.Id.class);
        when(id.idPat).thenReturn(null);

        // When
        freeFinder.visit(id);

        // Then
        verify(id, times(1)).idPat;
    }
}