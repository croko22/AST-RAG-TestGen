import net.hydromatic.morel.ast.AstNode;
import net.hydromatic.morel.ast.AstWriter;
import net.hydromatic.morel.ast.Pos;
import net.hydromatic.morel.ast.Shuttle;
import net.hydromatic.morel.ast.Visitor;
import net.hydromatic.morel.type.Op;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AstNodeTest {

    @Mock
    private Pos pos;

    @Mock
    private Op op;

    @Mock
    private AstWriter astWriter;

    @Mock
    private Shuttle shuttle;

    @Mock
    private Visitor visitor;

    private AstNode astNode;

    @BeforeEach
    void setup() {
        astNode = new AstNode(pos, op) {
            @Override
            public AstNode accept(Shuttle shuttle) {
                return null;
            }

            @Override
            public void accept(Visitor visitor) {

            }

            @Override
            AstWriter unparse(AstWriter w, int left, int right) {
                return null;
            }
        };
    }

    @Test
    public void testToString() {
        // Given
        when(astNode.unparse(any(AstWriter.class))).thenReturn(mock(AstWriter.class));

        // When
        String result = astNode.toString();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testUnparseRenumbered() {
        // Given
        when(astNode.unparse(any(AstWriter.class))).thenReturn(mock(AstWriter.class));

        // When
        String result = astNode.unparseRenumbered();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testUnparse() {
        // Given
        when(astNode.unparse(any(AstWriter.class))).thenReturn(mock(AstWriter.class));

        // When
        String result = astNode.unparse(astWriter);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testAcceptShuttle() {
        // Given
        when(shuttle.visit(any(AstNode.class))).thenReturn(mock(AstNode.class));

        // When
        AstNode result = astNode.accept(shuttle);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testAcceptVisitor() {
        // Given

        // When
        astNode.accept(visitor);

        // Then
        verify(visitor, times(1)).visit(any(AstNode.class));
    }
}