import net.hydromatic.morel.ast.Op;
import net.hydromatic.morel.type.Key;
import net.hydromatic.morel.type.Keys;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVar;
import net.hydromatic.morel.type.TypeVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeVarTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypeVisitor<String> typeVisitor;

    private TypeVar typeVar;

    @BeforeEach
    void setup() {
        typeVar = new TypeVar(0);
    }

    @Test
    void testHashCode() {
        // Given
        TypeVar otherTypeVar = new TypeVar(0);

        // When
        int hashCode = typeVar.hashCode();

        // Then
        assertEquals(otherTypeVar.hashCode(), hashCode);
    }

    @Test
    void testEquals_SameObject() {
        // Given
        TypeVar sameTypeVar = typeVar;

        // When
        boolean equals = typeVar.equals(sameTypeVar);

        // Then
        assertTrue(equals);
    }

    @Test
    void testEquals_SameOrdinal() {
        // Given
        TypeVar otherTypeVar = new TypeVar(0);

        // When
        boolean equals = typeVar.equals(otherTypeVar);

        // Then
        assertTrue(equals);
    }

    @Test
    void testEquals_DifferentOrdinal() {
        // Given
        TypeVar otherTypeVar = new TypeVar(1);

        // When
        boolean equals = typeVar.equals(otherTypeVar);

        // Then
        assertFalse(equals);
    }

    @Test
    void testEquals_Null() {
        // Given
        Object nullObject = null;

        // When
        boolean equals = typeVar.equals(nullObject);

        // Then
        assertFalse(equals);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        Object differentClass = new Object();

        // When
        boolean equals = typeVar.equals(differentClass);

        // Then
        assertFalse(equals);
    }

    @Test
    void testToString() {
        // Given
        TypeVar typeVar = new TypeVar(0);

        // When
        String toString = typeVar.toString();

        // Then
        assertEquals("'a", toString);
    }

    @Test
    void testAccept() {
        // Given
        when(typeVisitor.visit(any(TypeVar.class))).thenReturn("visited");

        // When
        String result = typeVar.accept(typeVisitor);

        // Then
        assertEquals("visited", result);
        verify(typeVisitor, times(1)).visit(typeVar);
    }

    @Test
    void testKey() {
        // Given
        Key expectedKey = Keys.ordinal(0);

        // When
        Key key = typeVar.key();

        // Then
        assertEquals(expectedKey, key);
    }

    @Test
    void testOp() {
        // Given
        Op expectedOp = Op.TY_VAR;

        // When
        Op op = typeVar.op();

        // Then
        assertEquals(expectedOp, op);
    }

    @Test
    void testCopy() {
        // Given
        UnaryOperator<Type> transform = type -> type;

        // When
        Type copiedType = typeVar.copy(typeSystem, transform);

        // Then
        assertEquals(typeVar, copiedType);
    }

    @Test
    void testSubstitute() {
        // Given
        List<Type> types = new ArrayList<>();
        types.add(new TypeVar(0));

        // When
        Type substitutedType = typeVar.substitute(typeSystem, types);

        // Then
        assertEquals(types.get(0), substitutedType);
    }

    @Test
    void testSpecializes_TypeVar() {
        // Given
        Type type = new TypeVar(0);

        // When
        boolean specializes = typeVar.specializes(type);

        // Then
        assertTrue(specializes);
    }

    @Test
    void testSpecializes_NotTypeVar() {
        // Given
        Type type = mock(Type.class);

        // When
        boolean specializes = typeVar.specializes(type);

        // Then
        assertFalse(specializes);
    }
}