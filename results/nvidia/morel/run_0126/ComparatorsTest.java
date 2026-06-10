import net.hydromatic.morel.eval.Comparators;
import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.RecordLikeType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComparatorsTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Type type;

    @Mock
    private DataType dataType;

    @Mock
    private RecordLikeType recordLikeType;

    private Comparator comparator;

    @BeforeEach
    void setup() {
        when(typeSystem.lookup()).thenReturn(type);
        when(typeSystem.lookup(any())).thenReturn(type);
        when(typeSystem.typeFor(any())).thenReturn(type);
        when(dataType.typeConstructors(any())).thenReturn(ImmutableMap.of());
        when(recordLikeType.argTypes()).thenReturn(List.of());
    }

    @Test
    void testComparatorFor() {
        // Given
        when(typeSystem.lookup()).thenReturn(type);

        // When
        comparator = Comparators.comparatorFor(typeSystem, type);

        // Then
        assertNotNull(comparator);
        verify(typeSystem, times(1)).lookup();
    }

    @Test
    void testCompare() {
        // Given
        Object o1 = "Hello";
        Object o2 = "World";

        // When
        int result = Comparators.compare(o1, o2);

        // Then
        assertEquals(-1, result);
    }

    @Test
    void testCompareSameObjects() {
        // Given
        Object o1 = "Hello";
        Object o2 = "Hello";

        // When
        int result = Comparators.compare(o1, o2);

        // Then
        assertEquals(0, result);
    }

    @Test
    void testCompareNullObjects() {
        // Given
        Object o1 = null;
        Object o2 = "World";

        // When and Then
        assertThrows(NullPointerException.class, () -> Comparators.compare(o1, o2));
    }

    @Test
    void testComparatorForRecordLikeType() {
        // Given
        when(recordLikeType.argTypes()).thenReturn(List.of(type));

        // When
        comparator = Comparators.comparatorFor(typeSystem, recordLikeType);

        // Then
        assertNotNull(comparator);
        verify(recordLikeType, times(1)).argTypes();
    }

    @Test
    void testComparatorForDataType() {
        // Given
        when(dataType.typeConstructors(any())).thenReturn(ImmutableMap.of("constructor", type));

        // When
        comparator = Comparators.comparatorFor(typeSystem, dataType);

        // Then
        assertNotNull(comparator);
        verify(dataType, times(1)).typeConstructors(any());
    }

    @Test
    void testComparatorForListType() {
        // Given
        when(type.op()).thenReturn(Type.Op.LIST);

        // When
        comparator = Comparators.comparatorFor(typeSystem, type);

        // Then
        assertNotNull(comparator);
        verify(type, times(1)).op();
    }

    @Test
    void testComparatorForTupleType() {
        // Given
        when(type.op()).thenReturn(Type.Op.TUPLE_TYPE);

        // When
        comparator = Comparators.comparatorFor(typeSystem, type);

        // Then
        assertNotNull(comparator);
        verify(type, times(1)).op();
    }
}