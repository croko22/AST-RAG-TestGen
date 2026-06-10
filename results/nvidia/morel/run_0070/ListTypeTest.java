import net.hydromatic.morel.type.BaseType;
import net.hydromatic.morel.type.Key;
import net.hydromatic.morel.type.ListType;
import net.hydromatic.morel.type.Op;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListTypeTest {

    @Mock
    private Type elementType;

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypeVisitor<String> typeVisitor;

    @Mock
    private UnaryOperator<Type> transform;

    private ListType listType;

    @BeforeEach
    void setup() {
        listType = new ListType(elementType);
    }

    @Test
    void testKey() {
        // Given
        Key key = mock(Key.class);
        when(elementType.key()).thenReturn(key);

        // When
        Key result = listType.key();

        // Then
        assertEquals(key, result);
        verify(elementType, times(1)).key();
    }

    @Test
    void testArg() {
        // Given
        when(elementType.copy(any(), any())).thenReturn(elementType);

        // When
        Type result = listType.arg(0);

        // Then
        assertEquals(elementType, result);
        verify(elementType, times(1)).copy(any(), any());
    }

    @Test
    void testArg_IndexOutOfBoundsException() {
        // Given
        int index = 1;

        // When / Then
        assertThrows(IndexOutOfBoundsException.class, () -> listType.arg(index));
    }

    @Test
    void testIsCollection() {
        // Given

        // When
        boolean result = listType.isCollection();

        // Then
        assertTrue(result);
    }

    @Test
    void testElementType() {
        // Given

        // When
        Type result = listType.elementType();

        // Then
        assertEquals(elementType, result);
    }

    @Test
    void testAccept() {
        // Given
        String resultString = "result";
        when(typeVisitor.visit(any())).thenReturn(resultString);

        // When
        String result = listType.accept(typeVisitor);

        // Then
        assertEquals(resultString, result);
        verify(typeVisitor, times(1)).visit(any());
    }

    @Test
    void testCopy_NoChange() {
        // Given
        when(elementType.copy(any(), any())).thenReturn(elementType);

        // When
        ListType result = listType.copy(typeSystem, transform);

        // Then
        assertEquals(listType, result);
        verify(elementType, times(1)).copy(any(), any());
    }

    @Test
    void testCopy_Change() {
        // Given
        Type newElementType = mock(Type.class);
        when(elementType.copy(any(), any())).thenReturn(newElementType);

        // When
        ListType result = listType.copy(typeSystem, transform);

        // Then
        assertNotEquals(listType, result);
        assertEquals(newElementType, result.elementType);
        verify(elementType, times(1)).copy(any(), any());
    }

    @Test
    void testSpecializes_ListType() {
        // Given
        Type otherElementType = mock(Type.class);
        ListType otherListType = new ListType(otherElementType);
        when(elementType.specializes(any())).thenReturn(true);

        // When
        boolean result = listType.specializes(otherListType);

        // Then
        assertTrue(result);
        verify(elementType, times(1)).specializes(any());
    }

    @Test
    void testSpecializes_TypeVar() {
        // Given
        Type typeVar = mock(Type.class);
        when(typeVar.getClass()).thenReturn(Type.class);

        // When
        boolean result = listType.specializes(typeVar);

        // Then
        assertTrue(result);
    }

    @Test
    void testSpecializes_OtherType() {
        // Given
        Type otherType = mock(Type.class);
        when(otherType.getClass()).thenReturn(Type.class);

        // When
        boolean result = listType.specializes(otherType);

        // Then
        assertFalse(result);
    }
}