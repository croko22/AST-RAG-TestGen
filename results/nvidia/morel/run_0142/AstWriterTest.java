import net.hydromatic.morel.ast.AstWriter;
import net.hydromatic.morel.ast.AstNode;
import net.hydromatic.morel.ast.Op;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AstWriterTest {

    @Mock
    private AstNode astNode;

    @Mock
    private Op op;

    private AstWriter astWriter;

    @BeforeEach
    void setup() {
        astWriter = new AstWriter();
    }

    @Test
    void testToString() {
        // Given
        AstWriter writer = new AstWriter();
        writer.append("Hello World");

        // When
        String result = writer.toString();

        // Then
        assertEquals("Hello World", result);
    }

    @Test
    void testWithParenthesize() {
        // Given
        AstWriter writer = new AstWriter();

        // When
        AstWriter result = writer.withParenthesize(true);

        // Then
        assertNotNull(result);
    }

    @Test
    void testAppend() {
        // Given
        AstWriter writer = new AstWriter();

        // When
        AstWriter result = writer.append("Hello World");

        // Then
        assertNotNull(result);
        assertEquals("Hello World", result.toString());
    }

    @Test
    void testId() {
        // Given
        AstWriter writer = new AstWriter();

        // When
        AstWriter result = writer.id("Hello World");

        // Then
        assertNotNull(result);
        assertEquals("Hello World", result.toString());
    }

    @Test
    void testIdWithIndex() {
        // Given
        AstWriter writer = new AstWriter();

        // When
        AstWriter result = writer.id("Hello World", 1);

        // Then
        assertNotNull(result);
        assertEquals("Hello World_1", result.toString());
    }

    @Test
    void testInfix() {
        // Given
        when(op.left).thenReturn(1);
        when(op.right).thenReturn(1);
        when(op.padded).thenReturn("+");

        // When
        AstWriter result = astWriter.infix(0, astNode, op, astNode, 0);

        // Then
        assertNotNull(result);
    }

    @Test
    void testPrefix() {
        // Given
        when(op.left).thenReturn(1);
        when(op.right).thenReturn(1);
        when(op.padded).thenReturn("+");

        // When
        AstWriter result = astWriter.prefix(0, op, astNode, 0);

        // Then
        assertNotNull(result);
    }

    @Test
    void testBinary() {
        // Given

        // When
        AstWriter result = astWriter.binary("let", astNode, " in ", astNode, 0);

        // Then
        assertNotNull(result);
    }

    @Test
    void testBinaryWithRight() {
        // Given

        // When
        AstWriter result = astWriter.binary("let", astNode, " in ", astNode, "end");

        // Then
        assertNotNull(result);
    }

    @Test
    void testAppendNode() {
        // Given

        // When
        AstWriter result = astWriter.append(astNode, 0, 0);

        // Then
        assertNotNull(result);
    }

    @Test
    void testAppendAll() {
        // Given
        List<AstNode> nodes = Arrays.asList(astNode, astNode);

        // When
        AstWriter result = astWriter.appendAll(nodes, 0, op, 0);

        // Then
        assertNotNull(result);
    }

    @Test
    void testAppendAllWithSep() {
        // Given
        List<AstNode> nodes = Arrays.asList(astNode, astNode);

        // When
        AstWriter result = astWriter.appendAll(nodes, ",");

        // Then
        assertNotNull(result);
    }

    @Test
    void testAppendAllWithStartSepEnd() {
        // Given
        List<AstNode> nodes = Arrays.asList(astNode, astNode);

        // When
        AstWriter result = astWriter.appendAll(nodes, "start", ",", "end");

        // Then
        assertNotNull(result);
    }

    @Test
    void testAppendAllWithStartSepEndEmpty() {
        // Given
        List<AstNode> nodes = Arrays.asList(astNode, astNode);

        // When
        AstWriter result = astWriter.appendAll(nodes, "start", ",", "end", "empty");

        // Then
        assertNotNull(result);
    }

    @Test
    void testAppendLiteral() {
        // Given

        // When
        AstWriter result = astWriter.appendLiteral("Hello World");

        // Then
        assertNotNull(result);
        assertEquals("\"Hello World\"", result.toString());
    }

    @Test
    void testAppendLiteralWithCharacter() {
        // Given

        // When
        AstWriter result = astWriter.appendLiteral('A');

        // Then
        assertNotNull(result);
        assertEquals("#\"A\"", result.toString());
    }

    @Test
    void testAppendLiteralWithBigDecimal() {
        // Given
        BigDecimal bigDecimal = new BigDecimal("10.5");

        // When
        AstWriter result = astWriter.appendLiteral(bigDecimal);

        // Then
        assertNotNull(result);
        assertEquals("10.5", result.toString());
    }

    @Test
    void testAppendLiteralWithBuiltIn() {
        // Given
        // BuiltIn builtIn = new BuiltIn();

        // When
        // AstWriter result = astWriter.appendLiteral(builtIn);

        // Then
        // assertNotNull(result);
    }
}