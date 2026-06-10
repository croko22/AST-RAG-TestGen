import net.hydromatic.morel.datalog.DatalogEvaluator;
import net.hydromatic.morel.eval.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatalogEvaluatorTest {

    @Mock
    private Session session;

    @BeforeEach
    void setup() {
        // Initialize mock session
        when(session.typeSystem).thenReturn(mock(TypeSystem.class));
    }

    @Test
    void testExecute_ValidProgram() {
        // Given: a valid Datalog program
        String program = "example program";

        // When: executing the program
        Variant result = DatalogEvaluator.execute(program, session);

        // Then: verify the result is not null
        assertNotNull(result);
    }

    @Test
    void testExecute_InvalidProgram() {
        // Given: an invalid Datalog program
        String program = "invalid program";

        // When/Then: expect a DatalogException
        assertThrows(DatalogException.class, () -> DatalogEvaluator.execute(program, session));
    }

    @Test
    void testValidate_ValidProgram() {
        // Given: a valid Datalog program
        String program = "example program";

        // When: validating the program
        String result = DatalogEvaluator.validate(program, session);

        // Then: verify the result is not null and does not contain an error message
        assertNotNull(result);
        assertFalse(result.contains("error"));
    }

    @Test
    void testValidate_InvalidProgram() {
        // Given: an invalid Datalog program
        String program = "invalid program";

        // When: validating the program
        String result = DatalogEvaluator.validate(program, session);

        // Then: verify the result contains an error message
        assertNotNull(result);
        assertTrue(result.contains("error"));
    }

    @Test
    void testTranslate_ValidProgram() {
        // Given: a valid Datalog program
        String program = "example program";

        // When: translating the program
        Object result = DatalogEvaluator.translate(program, session);

        // Then: verify the result is not null and is an instance of ImmutableList
        assertNotNull(result);
        assertTrue(result instanceof ImmutableList);
    }

    @Test
    void testTranslate_InvalidProgram() {
        // Given: an invalid Datalog program
        String program = "invalid program";

        // When: translating the program
        Object result = DatalogEvaluator.translate(program, session);

        // Then: verify the result is not null and is an instance of ImmutableList
        assertNotNull(result);
        assertTrue(result instanceof ImmutableList);
    }
}