import net.hydromatic.morel.type.BaseType;
import net.hydromatic.morel.type.Key;
import net.hydromatic.morel.type.Op;
import net.hydromatic.morel.type.RecordLikeType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVisitor;
import net.hydromatic.morel.type.TupleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TupleTypeTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypeVisitor<?> typeVisitor;

    private TupleType tupleType;

    @BeforeEach
    void setup() {
        List<Type> argTypes = List.of(mock(Type.class), mock(Type.class));
        tupleType = new TupleType(argTypes);
    }

    @Test
    void testArgNameTypes() {
        // Given
        SortedMap<String, Type> expectedArgNameTypes = TupleType.recordMap(List.of(mock(Type.class), mock(Type.class)));

        // When
        SortedMap<String, Type> actualArgNameTypes = tupleType.argNameTypes();

        // Then
        assertEquals(expectedArgNameTypes.size(), actualArgNameTypes.size());
    }

    @Test
    void testArgNames() {
        // Given
        List<String> expectedArgNames = TupleType.ordinalNames(2);

        // When
        List<String> actualArgNames = tupleType.argNames();

        // Then
        assertEquals(expectedArgNames, actualArgNames);
    }

    @Test
    void testArgTypes() {
        // Given
        List<Type> expectedArgTypes = List.of(mock(Type.class), mock(Type.class));

        // When
        List<Type> actualArgTypes = tupleType.argTypes();

        // Then
        assertEquals(expectedArgTypes.size(), actualArgTypes.size());
    }

    @Test
    void testArgType() {
        // Given
        Type expectedArgType = mock(Type.class);

        // When
        Type actualArgType = tupleType.argType(0);

        // Then
        assertNotNull(actualArgType);
    }

    @Test
    void testAccept() {
        // Given
        Object expectedResult = new Object();

        // When
        when(typeVisitor.visit(any(TupleType.class))).thenReturn(expectedResult);
        Object actualResult = tupleType.accept(typeVisitor);

        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void testKey() {
        // Given
        Key expectedKey = mock(Key.class);

        // When
        Key actualKey = tupleType.key();

        // Then
        assertNotNull(actualKey);
    }

    @Test
    void testCopy_NoChanges() {
        // Given
        UnaryOperator<Type> transform = type -> type;

        // When
        TupleType actualTupleType = tupleType.copy(typeSystem, transform);

        // Then
        assertSame(tupleType, actualTupleType);
    }

    @Test
    void testCopy_WithChanges() {
        // Given
        Type newType = mock(Type.class);
        UnaryOperator<Type> transform = type -> newType;

        // When
        TupleType actualTupleType = tupleType.copy(typeSystem, transform);

        // Then
        assertNotSame(tupleType, actualTupleType);
    }

    @Test
    void testSpecializes_SameTupleType() {
        // Given
        TupleType otherTupleType = new TupleType(List.of(mock(Type.class), mock(Type.class)));

        // When
        boolean actualResult = tupleType.specializes(otherTupleType);

        // Then
        assertTrue(actualResult);
    }

    @Test
    void testSpecializes_DifferentTupleType() {
        // Given
        TupleType otherTupleType = new TupleType(List.of(mock(Type.class)));

        // When
        boolean actualResult = tupleType.specializes(otherTupleType);

        // Then
        assertFalse(actualResult);
    }

    @Test
    void testSpecializes_TypeVar() {
        // Given
        Type typeVar = mock(Type.class);

        // When
        boolean actualResult = tupleType.specializes(typeVar);

        // Then
        assertTrue(actualResult);
    }

    @Test
    void testOrdinalNames() {
        // Given
        int size = 2;

        // When
        List<String> actualOrdinalNames = TupleType.ordinalNames(size);

        // Then
        assertEquals(size, actualOrdinalNames.size());
    }
}