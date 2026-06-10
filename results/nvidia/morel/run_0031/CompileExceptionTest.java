import net.hydromatic.morel.ast.Pos;
import net.hydromatic.morel.compile.CompileException;
import net.hydromatic.morel.util.MorelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompileExceptionTest {

    @Mock
    private Pos mockPos;

    private CompileException compileException;
    private CompileException warningCompileException;

    @BeforeEach
    void setup() {
        when(mockPos.toString()).thenReturn("Mock Position");
        compileException = new CompileException("Test Message", false, mockPos);
        warningCompileException = new CompileException("Test Warning Message", true, mockPos);
    }

    @Test
    void testToString() {
        // Given: a CompileException instance
        // When: toString method is called
        String result = compileException.toString();
        // Then: verify the result
        assertTrue(result.contains("Test Message"));
        assertTrue(result.contains("Mock Position"));
    }

    @Test
    void testToString_Warning() {
        // Given: a CompileException instance with warning
        // When: toString method is called
        String result = warningCompileException.toString();
        // Then: verify the result
        assertTrue(result.contains("Test Warning Message"));
        assertTrue(result.contains("Mock Position"));
    }

    @Test
    void testPos() {
        // Given: a CompileException instance
        // When: pos method is called
        Pos result = compileException.pos();
        // Then: verify the result
        assertEquals(mockPos, result);
    }

    @Test
    void testDescribeTo() {
        // Given: a CompileException instance
        // When: describeTo method is called
        StringBuilder buf = new StringBuilder();
        StringBuilder result = compileException.describeTo(buf);
        // Then: verify the result
        assertNotNull(result);
        assertTrue(result.toString().contains("Test Message"));
        assertTrue(result.toString().contains("Mock Position"));
    }

    @Test
    void testDescribeTo_Warning() {
        // Given: a CompileException instance with warning
        // When: describeTo method is called
        StringBuilder buf = new StringBuilder();
        StringBuilder result = warningCompileException.describeTo(buf);
        // Then: verify the result
        assertNotNull(result);
        assertTrue(result.toString().contains("Test Warning Message"));
        assertTrue(result.toString().contains("Mock Position"));
    }

    @Test
    void testDescribeTo_NullBuffer() {
        // Given: a CompileException instance
        // When: describeTo method is called with null buffer
        assertThrows(NullPointerException.class, () -> compileException.describeTo(null));
    }

    @Test
    void testEquals() {
        // Given: two CompileException instances
        CompileException sameException = new CompileException("Test Message", false, mockPos);
        CompileException differentException = new CompileException("Different Message", false, mockPos);
        // When: equals method is called
        boolean sameResult = compileException.equals(sameException);
        boolean differentResult = compileException.equals(differentException);
        // Then: verify the results
        assertTrue(sameResult);
        assertFalse(differentResult);
    }

    @Test
    void testHashCode() {
        // Given: a CompileException instance
        // When: hashCode method is called
        int result = compileException.hashCode();
        // Then: verify the result
        assertNotNull(result);
    }
}