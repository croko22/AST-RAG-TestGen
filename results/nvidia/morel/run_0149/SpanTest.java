import net.hydromatic.morel.ast.AstNode;
import net.hydromatic.morel.ast.Pos;
import net.hydromatic.morel.parse.Span;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpanTest {

    @Mock
    private AstNode astNode;

    @Mock
    private Pos pos;

    @Mock
    private MorelParser parser;

    @BeforeEach
    void setup() {
        when(astNode.toString()).thenReturn("Mock AstNode");
        when(pos.toString()).thenReturn("Mock Pos");
        when(parser.pos()).thenReturn(pos);
    }

    @Test
    void testOf() {
        // Given
        Span span = Span.of();

        // Then
        assertNotNull(span);
        assertTrue(span instanceof Span);
    }

    @Test
    void testOfPos() {
        // Given
        Span span = Span.of(pos);

        // Then
        assertNotNull(span);
        assertTrue(span instanceof Span);
    }

    @Test
    void testOfAstNode() {
        // Given
        Span span = Span.of(astNode);

        // Then
        assertNotNull(span);
        assertTrue(span instanceof Span);
    }

    @Test
    void testOfAstNodeAstNode() {
        // Given
        AstNode astNode2 = mock(AstNode.class);
        when(astNode2.toString()).thenReturn("Mock AstNode2");
        Span span = Span.of(astNode, astNode2);

        // Then
        assertNotNull(span);
        assertTrue(span instanceof Span);
    }

    @Test
    void testOfCollection() {
        // Given
        Collection<AstNode> collection = new ArrayList<>();
        collection.add(astNode);
        Span span = Span.of(collection);

        // Then
        assertNotNull(span);
        assertTrue(span instanceof Span);
    }

    @Test
    void testAddAstNode() {
        // Given
        Span span = Span.of();

        // When
        Span result = span.add(astNode);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Span);
        assertSame(span, result);
    }

    @Test
    void testAddIfAstNode() {
        // Given
        Span span = Span.of();

        // When
        Span result = span.addIf(astNode);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Span);
        assertSame(span, result);
    }

    @Test
    void testAddIfNullAstNode() {
        // Given
        Span span = Span.of();

        // When
        Span result = span.addIf(null);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Span);
        assertSame(span, result);
    }

    @Test
    void testAddPos() {
        // Given
        Span span = Span.of();

        // When
        Span result = span.add(pos);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Span);
        assertSame(span, result);
    }

    @Test
    void testAddAll() {
        // Given
        Collection<AstNode> collection = new ArrayList<>();
        collection.add(astNode);
        Span span = Span.of();

        // When
        Span result = span.addAll(collection);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Span);
        assertSame(span, result);
    }

    @Test
    void testAddParser() {
        // Given
        Span span = Span.of();

        // When
        Span result = span.add(parser);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Span);
        assertSame(span, result);
        verify(parser, times(1)).pos();
    }

    @Test
    void testPos() {
        // Given
        Span span = Span.of(pos);

        // When
        Pos result = span.pos();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Pos);
        assertSame(pos, result);
    }

    @Test
    void testPosMultiple() {
        // Given
        Span span = Span.of(pos);
        span.add(pos);

        // When
        Pos result = span.pos();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Pos);
    }

    @Test
    void testEndParser() {
        // Given
        Span span = Span.of();

        // When
        Pos result = span.end(parser);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Pos);
        verify(parser, times(1)).pos();
    }

    @Test
    void testEndAstNode() {
        // Given
        Span span = Span.of();

        // When
        Pos result = span.end(astNode);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Pos);
    }

    @Test
    void testClear() {
        // Given
        Span span = Span.of(pos);

        // When
        Span result = span.clear();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Span);
        assertSame(span, result);
    }

    @Test
    void testPosEmpty() {
        // Given
        Span span = Span.of();

        // When and Then
        assertThrows(AssertionError.class, () -> span.pos());
    }
}