import net.hydromatic.morel.datalog.DatalogAst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatalogAstTest {

    @Mock
    private DatalogAst.Declaration declaration;

    @Mock
    private DatalogAst.Input input;

    @Mock
    private DatalogAst.Output output;

    private DatalogAst.Program program;

    @BeforeEach
    void setup() {
        List<DatalogAst.Statement> statements = new ArrayList<>();
        program = new DatalogAst.Program(statements);
    }

    @Test
    void testGetDeclaration() {
        // Given
        DatalogAst.Declaration declaration = new DatalogAst.Declaration("name", new ArrayList<>());
        program.statements.add(declaration);

        // When
        DatalogAst.Declaration result = program.getDeclaration("name");

        // Then
        assertEquals(declaration, result);
    }

    @Test
    void testGetDeclaration_NotFound() {
        // Given
        DatalogAst.Declaration declaration = new DatalogAst.Declaration("name", new ArrayList<>());
        program.statements.add(declaration);

        // When
        DatalogAst.Declaration result = program.getDeclaration("otherName");

        // Then
        assertNull(result);
    }

    @Test
    void testGetInputs() {
        // Given
        DatalogAst.Input input = new DatalogAst.Input("relationName", null);
        program.statements.add(input);

        // When
        List<DatalogAst.Input> result = program.getInputs();

        // Then
        assertEquals(1, result.size());
        assertEquals(input, result.get(0));
    }

    @Test
    void testGetOutputs() {
        // Given
        DatalogAst.Output output = new DatalogAst.Output("relationName");
        program.statements.add(output);

        // When
        List<DatalogAst.Output> result = program.getOutputs();

        // Then
        assertEquals(1, result.size());
        assertEquals(output, result.get(0));
    }

    @Test
    void testHasDeclaration() {
        // Given
        DatalogAst.Declaration declaration = new DatalogAst.Declaration("name", new ArrayList<>());
        program.statements.add(declaration);

        // When
        boolean result = program.hasDeclaration("name");

        // Then
        assertTrue(result);
    }

    @Test
    void testHasDeclaration_NotFound() {
        // Given
        DatalogAst.Declaration declaration = new DatalogAst.Declaration("name", new ArrayList<>());
        program.statements.add(declaration);

        // When
        boolean result = program.hasDeclaration("otherName");

        // Then
        assertFalse(result);
    }

    @Test
    void testGetDeclarations() {
        // Given
        DatalogAst.Declaration declaration1 = new DatalogAst.Declaration("name1", new ArrayList<>());
        DatalogAst.Declaration declaration2 = new DatalogAst.Declaration("name2", new ArrayList<>());
        program.statements.add(declaration1);
        program.statements.add(declaration2);

        // When
        Iterable<DatalogAst.Declaration> result = program.getDeclarations();

        // Then
        int count = 0;
        for (DatalogAst.Declaration declaration : result) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testDeclaration_Arity() {
        // Given
        List<DatalogAst.Param> params = new ArrayList<>();
        params.add(new DatalogAst.Param("param1", "type1"));
        params.add(new DatalogAst.Param("param2", "type2"));
        DatalogAst.Declaration declaration = new DatalogAst.Declaration("name", params);

        // When
        int result = declaration.arity();

        // Then
        assertEquals(2, result);
    }

    @Test
    void testDeclaration_ToString() {
        // Given
        List<DatalogAst.Param> params = new ArrayList<>();
        params.add(new DatalogAst.Param("param1", "type1"));
        params.add(new DatalogAst.Param("param2", "type2"));
        DatalogAst.Declaration declaration = new DatalogAst.Declaration("name", params);

        // When
        String result = declaration.toString();

        // Then
        assertEquals(".decl name(param1:type1, param2:type2)", result);
    }

    @Test
    void testInput_EffectiveFileName() {
        // Given
        DatalogAst.Input input = new DatalogAst.Input("relationName", null);

        // When
        String result = input.effectiveFileName();

        // Then
        assertEquals("relationName.csv", result);
    }

    @Test
    void testInput_EffectiveFileName_WithFileName() {
        // Given
        DatalogAst.Input input = new DatalogAst.Input("relationName", "fileName");

        // When
        String result = input.effectiveFileName();

        // Then
        assertEquals("fileName", result);
    }

    @Test
    void testInput_ToString() {
        // Given
        DatalogAst.Input input = new DatalogAst.Input("relationName", null);

        // When
        String result = input.toString();

        // Then
        assertEquals(".input relationName", result);
    }

    @Test
    void testInput_ToString_WithFileName() {
        // Given
        DatalogAst.Input input = new DatalogAst.Input("relationName", "fileName");

        // When
        String result = input.toString();

        // Then
        assertEquals(".input relationName \"fileName\"", result);
    }

    @Test
    void testOutput_ToString() {
        // Given
        DatalogAst.Output output = new DatalogAst.Output("relationName");

        // When
        String result = output.toString();

        // Then
        assertEquals(".output relationName", result);
    }
}