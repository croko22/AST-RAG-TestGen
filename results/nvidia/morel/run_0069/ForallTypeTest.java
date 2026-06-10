import net.hydromatic.morel.type.ForallType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
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
public class ForallTypeTest {

    @Mock
    private Type type;

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypeVisitor typeVisitor;

    @Mock
    private UnaryOperator<Type> transform;

    private ForallType forallType;

    @BeforeEach
    void setup() {
        forallType = new ForallType(1, type);
    }

    @Test
    public void testKey() {
        // Given
        when(type.key()).thenReturn(mock(Key.class));

        // When
        Key key = forallType.key();

        // Then
        assertNotNull(key);
        verify(type, times(1)).key();
    }

    @Test
    public void testAccept() {
        // Given
        when(typeVisitor.visit(any(ForallType.class))).thenReturn(mock(Object.class));

        // When
        Object result = forallType.accept(typeVisitor);

        // Then
        assertNotNull(result);
        verify(typeVisitor, times(1)).visit(forallType);
    }

    @Test
    public void testCopy_NoChange() {
        // Given
        when(type.copy(any(TypeSystem.class), any(UnaryOperator.class))).thenReturn(type);

        // When
        ForallType copiedType = forallType.copy(typeSystem, transform);

        // Then
        assertSame(forallType, copiedType);
        verify(type, times(1)).copy(typeSystem, transform);
    }

    @Test
    public void testCopy_Change() {
        // Given
        Type newType = mock(Type.class);
        when(type.copy(any(TypeSystem.class), any(UnaryOperator.class))).thenReturn(newType);
        when(typeSystem.forallType(anyInt(), any(Type.class))).thenReturn(mock(ForallType.class));

        // When
        ForallType copiedType = forallType.copy(typeSystem, transform);

        // Then
        assertNotNull(copiedType);
        verify(type, times(1)).copy(typeSystem, transform);
        verify(typeSystem, times(1)).forallType(anyInt(), any(Type.class));
    }

    @Test
    public void testCanCallArgOf() {
        // Given
        Type otherType = mock(Type.class);
        when(type.canCallArgOf(any(Type.class))).thenReturn(true);

        // When
        boolean result = forallType.canCallArgOf(otherType);

        // Then
        assertTrue(result);
        verify(type, times(1)).canCallArgOf(otherType);
    }

    @Test
    public void testSubstitute_DataType() {
        // Given
        Type dataType = mock(Type.class);
        when(dataType.op()).thenReturn(Op.DATA_TYPE);
        when(typeSystem.typeFor(any(Key.class))).thenReturn(mock(Type.class));

        // When
        Type substitutedType = forallType.substitute(typeSystem, List.of(dataType));

        // Then
        assertNotNull(substitutedType);
        verify(typeSystem, times(1)).typeFor(any(Key.class));
    }

    @Test
    public void testSubstitute_FunctionType() {
        // Given
        Type functionType = mock(Type.class);
        when(functionType.op()).thenReturn(Op.FUNCTION_TYPE);
        when(functionType.substitute(any(TypeSystem.class), any(List.class))).thenReturn(mock(Type.class));

        // When
        Type substitutedType = forallType.substitute(typeSystem, List.of(functionType));

        // Then
        assertNotNull(substitutedType);
        verify(functionType, times(1)).substitute(typeSystem, List.of(functionType));
    }

    @Test
    public void testSubstitute_UnknownType() {
        // Given
        Type unknownType = mock(Type.class);
        when(unknownType.op()).thenReturn(mock(Op.class));

        // When and Then
        assertThrows(AssertionError.class, () -> forallType.substitute(typeSystem, List.of(unknownType)));
    }
}