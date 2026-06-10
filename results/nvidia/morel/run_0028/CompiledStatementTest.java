import net.hydromatic.morel.compile.CompiledStatement;
import net.hydromatic.morel.eval.Session;
import net.hydromatic.morel.type.Binding;
import net.hydromatic.morel.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompiledStatementTest {

    @Mock
    private Session session;

    @Mock
    private Environment environment;

    @Mock
    private Consumer<String> outLines;

    @Mock
    private Consumer<Binding> outBindings;

    @Mock
    private CompiledStatement compiledStatement;

    @BeforeEach
    void setup() {
        // Initialize the compiledStatement mock
        compiledStatement = mock(CompiledStatement.class);
    }

    @Test
    void testEval() {
        // Given: a session, environment, outLines, and outBindings
        when(session.toString()).thenReturn("Session");

        // When: eval is called on the compiledStatement
        compiledStatement.eval(session, environment, outLines, outBindings);

        // Then: verify the interactions with the mocks
        verify(compiledStatement, times(1)).eval(session, environment, outLines, outBindings);
        verify(outLines, atLeastOnce()).accept(any());
        verify(outBindings, atLeastOnce()).accept(any());
    }

    @Test
    void testGetType() {
        // Given: a compiledStatement
        Type type = mock(Type.class);
        when(compiledStatement.getType()).thenReturn(type);

        // When: getType is called on the compiledStatement
        Type result = compiledStatement.getType();

        // Then: verify the result
        assertEquals(type, result);
    }

    @Test
    void testGetBindings() {
        // Given: a compiledStatement and outBindings
        Binding binding = mock(Binding.class);
        when(compiledStatement.getBindings(outBindings)).thenReturn(void.class);

        // When: getBindings is called on the compiledStatement
        compiledStatement.getBindings(outBindings);

        // Then: verify the interactions with the mocks
        verify(compiledStatement, times(1)).getBindings(outBindings);
        verify(outBindings, atLeastOnce()).accept(any());
    }

    @Test
    void testEval_SessionIsNull() {
        // Given: a null session
        session = null;

        // When: eval is called on the compiledStatement
        assertThrows(NullPointerException.class, () -> compiledStatement.eval(session, environment, outLines, outBindings));
    }

    @Test
    void testEval_EnvironmentIsNull() {
        // Given: a null environment
        environment = null;

        // When: eval is called on the compiledStatement
        assertThrows(NullPointerException.class, () -> compiledStatement.eval(session, environment, outLines, outBindings));
    }

    @Test
    void testEval_OutLinesIsNull() {
        // Given: a null outLines
        outLines = null;

        // When: eval is called on the compiledStatement
        assertThrows(NullPointerException.class, () -> compiledStatement.eval(session, environment, outLines, outBindings));
    }

    @Test
    void testEval_OutBindingsIsNull() {
        // Given: a null outBindings
        outBindings = null;

        // When: eval is called on the compiledStatement
        assertThrows(NullPointerException.class, () -> compiledStatement.eval(session, environment, outLines, outBindings));
    }
}