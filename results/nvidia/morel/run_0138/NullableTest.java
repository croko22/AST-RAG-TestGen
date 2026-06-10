import net.hydromatic.morel.ast.Op;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

public class OpTest {

    private Op op;

    @BeforeEach
    void setup() {
        op = Op.BOOL_LITERAL;
    }

    @Test
    public void testLowerName() {
        // Given
        Op op = Op.EXISTS;

        // When
        String lowerName = op.lowerName();

        // Then
        assertEquals("exists", lowerName);
    }

    @Test
    public void testToPat_BoolLiteral() {
        // Given
        Op op = Op.BOOL_LITERAL;

        // When
        Op pat = op.toPat();

        // Then
        assertEquals(Op.BOOL_LITERAL_PAT, pat);
    }

    @Test
    public void testToPat_CharLiteral() {
        // Given
        Op op = Op.CHAR_LITERAL;

        // When
        Op pat = op.toPat();

        // Then
        assertEquals(Op.CHAR_LITERAL_PAT, pat);
    }

    @Test
    public void testToPat_IntLiteral() {
        // Given
        Op op = Op.INT_LITERAL;

        // When
        Op pat = op.toPat();

        // Then
        assertEquals(Op.INT_LITERAL_PAT, pat);
    }

    @Test
    public void testToPat_RealLiteral() {
        // Given
        Op op = Op.REAL_LITERAL;

        // When
        Op pat = op.toPat();

        // Then
        assertEquals(Op.REAL_LITERAL_PAT, pat);
    }

    @Test
    public void testToPat_StringLiteral() {
        // Given
        Op op = Op.STRING_LITERAL;

        // When
        Op pat = op.toPat();

        // Then
        assertEquals(Op.STRING_LITERAL_PAT, pat);
    }

    @Test
    public void testToPat_Tuple() {
        // Given
        Op op = Op.TUPLE;

        // When
        Op pat = op.toPat();

        // Then
        assertEquals(Op.TUPLE_PAT, pat);
    }

    @Test
    public void testToPat_UnknownOp() {
        // Given
        Op op = Op.ID;

        // When and Then
        assertThrows(AssertionError.class, () -> op.toPat());
    }

    @Test
    public void testOpName() {
        // Given
        Op op = Op.BOOL_LITERAL;

        // When
        String opName = op.opName;

        // Then
        assertNull(opName);
    }

    @Test
    public void testPadded() {
        // Given
        Op op = Op.BOOL_LITERAL;

        // When
        String padded = op.padded;

        // Then
        assertEquals("", padded);
    }

    @Test
    public void testLeft() {
        // Given
        Op op = Op.BOOL_LITERAL;

        // When
        int left = op.left;

        // Then
        assertEquals(0, left);
    }

    @Test
    public void testRight() {
        // Given
        Op op = Op.BOOL_LITERAL;

        // When
        int right = op.right;

        // Then
        assertEquals(0, right);
    }
}