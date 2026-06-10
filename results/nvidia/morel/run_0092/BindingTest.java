import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.type.Binding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BindingTest {

    @Mock
    private Core.NamedPat id;

    @Mock
    private Core.IdPat overloadId;

    @Mock
    private Core.Exp exp;

    private Object value;

    private boolean parameter;

    private Binding.Kind kind;

    @BeforeEach
    void setup() {
        value = new Object();
        parameter = false;
        kind = Binding.Kind.VAL;
    }

    @Test
    void testOf() {
        // Given
        Binding binding = Binding.of(id);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertNull(binding.overloadId);
        assertNull(binding.exp);
        assertNotEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(kind, binding.kind);
    }

    @Test
    void testOver() {
        // Given
        Binding binding = Binding.over(id, value);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertNull(binding.overloadId);
        assertNull(binding.exp);
        assertEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(Binding.Kind.OVER, binding.kind);
    }

    @Test
    void testOverWithoutValue() {
        // Given
        Binding binding = Binding.over(id);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertNull(binding.overloadId);
        assertNull(binding.exp);
        assertNotEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(Binding.Kind.OVER, binding.kind);
    }

    @Test
    void testOfWithExp() {
        // Given
        Binding binding = Binding.of(id, exp);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertNull(binding.overloadId);
        assertEquals(exp, binding.exp);
        assertNotEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(kind, binding.kind);
    }

    @Test
    void testInst() {
        // Given
        Binding binding = Binding.inst(id, overloadId, exp);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertEquals(overloadId, binding.overloadId);
        assertEquals(exp, binding.exp);
        assertNotEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(Binding.Kind.INST, binding.kind);
    }

    @Test
    void testOfWithValue() {
        // Given
        Binding binding = Binding.of(id, value);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertNull(binding.overloadId);
        assertNull(binding.exp);
        assertEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(kind, binding.kind);
    }

    @Test
    void testOfWithExpAndValue() {
        // Given
        Binding binding = Binding.of(id, exp, value);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertNull(binding.overloadId);
        assertEquals(exp, binding.exp);
        assertEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(kind, binding.kind);
    }

    @Test
    void testInstWithValue() {
        // Given
        Binding binding = Binding.inst(id, overloadId, value);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertEquals(overloadId, binding.overloadId);
        assertNull(binding.exp);
        assertEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(Binding.Kind.INST, binding.kind);
    }

    @Test
    void testInstWithExpAndValue() {
        // Given
        Binding binding = Binding.inst(id, overloadId, exp, value);

        // Then
        assertNotNull(binding);
        assertEquals(id, binding.id);
        assertEquals(overloadId, binding.overloadId);
        assertEquals(exp, binding.exp);
        assertEquals(value, binding.value);
        assertEquals(parameter, binding.parameter);
        assertEquals(Binding.Kind.INST, binding.kind);
    }

    @Test
    void testWithFlattenedName() {
        // Given
        Binding binding = Binding.of(id);

        // When
        Binding flattenedBinding = binding.withFlattenedName();

        // Then
        assertNotNull(flattenedBinding);
        assertEquals(id, flattenedBinding.id);
        assertNull(flattenedBinding.overloadId);
        assertNull(flattenedBinding.exp);
        assertNotEquals(value, flattenedBinding.value);
        assertEquals(parameter, flattenedBinding.parameter);
        assertEquals(kind, flattenedBinding.kind);
    }

    @Test
    void testHashCode() {
        // Given
        Binding binding = Binding.of(id);

        // When
        int hashCode = binding.hashCode();

        // Then
        assertNotNull(hashCode);
    }

    @Test
    void testEquals() {
        // Given
        Binding binding1 = Binding.of(id);
        Binding binding2 = Binding.of(id);

        // When
        boolean equals = binding1.equals(binding2);

        // Then
        assertTrue(equals);
    }

    @Test
    void testWithParameter() {
        // Given
        Binding binding = Binding.of(id);

        // When
        Binding parameterBinding = binding.withParameter(true);

        // Then
        assertNotNull(parameterBinding);
        assertEquals(id, parameterBinding.id);
        assertNull(parameterBinding.overloadId);
        assertNull(parameterBinding.exp);
        assertNotEquals(value, parameterBinding.value);
        assertTrue(parameterBinding.parameter);
        assertEquals(kind, parameterBinding.kind);
    }

    @Test
    void testWithKind() {
        // Given
        Binding binding = Binding.of(id);

        // When
        Binding kindBinding = binding.withKind(Binding.Kind.OVER);

        // Then
        assertNotNull(kindBinding);
        assertEquals(id, kindBinding.id);
        assertNull(kindBinding.overloadId);
        assertNull(kindBinding.exp);
        assertNotEquals(value, kindBinding.value);
        assertEquals(parameter, kindBinding.parameter);
        assertEquals(Binding.Kind.OVER, kindBinding.kind);
    }

    @Test
    void testIsInst() {
        // Given
        Binding binding = Binding.inst(id, overloadId, exp);

        // When
        boolean isInst = binding.isInst();

        // Then
        assertTrue(isInst);
    }

    @Test
    void testToString() {
        // Given
        Binding binding = Binding.of(id);

        // When
        String toString = binding.toString();

        // Then
        assertNotNull(toString);
    }
}