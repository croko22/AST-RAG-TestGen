import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeShuttle;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVar;
import net.hydromatic.morel.type.ListType;
import net.hydromatic.morel.type.FnType;
import net.hydromatic.morel.type.TupleType;
import net.hydromatic.morel.type.RecordType;
import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.PrimitiveType;
import net.hydromatic.morel.type.ForallType;
import net.hydromatic.morel.type.DummyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeShuttleTest {

    @Mock
    private TypeSystem typeSystem;

    private TypeShuttle typeShuttle;

    @BeforeEach
    public void setup() {
        typeShuttle = new TypeShuttle(typeSystem);
    }

    @Test
    public void testVisit_TypeVar() {
        // Given
        TypeVar typeVar = mock(TypeVar.class);
        when(typeVar.copy(typeSystem, any())).thenReturn(typeVar);

        // When
        Type result = typeShuttle.visit(typeVar);

        // Then
        assertEquals(typeVar, result);
        verify(typeVar, times(1)).copy(typeSystem, any());
    }

    @Test
    public void testVisit_ListType() {
        // Given
        ListType listType = mock(ListType.class);
        when(listType.copy(typeSystem, any())).thenReturn(listType);

        // When
        ListType result = typeShuttle.visit(listType);

        // Then
        assertEquals(listType, result);
        verify(listType, times(1)).copy(typeSystem, any());
    }

    @Test
    public void testVisit_FnType() {
        // Given
        FnType fnType = mock(FnType.class);
        when(fnType.copy(typeSystem, any())).thenReturn(fnType);

        // When
        FnType result = typeShuttle.visit(fnType);

        // Then
        assertEquals(fnType, result);
        verify(fnType, times(1)).copy(typeSystem, any());
    }

    @Test
    public void testVisit_TupleType() {
        // Given
        TupleType tupleType = mock(TupleType.class);
        when(tupleType.copy(typeSystem, any())).thenReturn(tupleType);

        // When
        TupleType result = typeShuttle.visit(tupleType);

        // Then
        assertEquals(tupleType, result);
        verify(tupleType, times(1)).copy(typeSystem, any());
    }

    @Test
    public void testVisit_RecordType() {
        // Given
        RecordType recordType = mock(RecordType.class);
        when(recordType.copy(typeSystem, any())).thenReturn(recordType);

        // When
        RecordType result = typeShuttle.visit(recordType);

        // Then
        assertEquals(recordType, result);
        verify(recordType, times(1)).copy(typeSystem, any());
    }

    @Test
    public void testVisit_DataType() {
        // Given
        DataType dataType = mock(DataType.class);
        when(dataType.copy(typeSystem, any())).thenReturn(dataType);

        // When
        Type result = typeShuttle.visit(dataType);

        // Then
        assertEquals(dataType, result);
        verify(dataType, times(1)).copy(typeSystem, any());
    }

    @Test
    public void testVisit_PrimitiveType() {
        // Given
        PrimitiveType primitiveType = mock(PrimitiveType.class);
        when(primitiveType.copy(typeSystem, any())).thenReturn(primitiveType);

        // When
        PrimitiveType result = typeShuttle.visit(primitiveType);

        // Then
        assertEquals(primitiveType, result);
        verify(primitiveType, times(1)).copy(typeSystem, any());
    }

    @Test
    public void testVisit_ForallType() {
        // Given
        ForallType forallType = mock(ForallType.class);
        when(forallType.copy(typeSystem, any())).thenReturn(forallType);

        // When
        ForallType result = typeShuttle.visit(forallType);

        // Then
        assertEquals(forallType, result);
        verify(forallType, times(1)).copy(typeSystem, any());
    }

    @Test
    public void testVisit_DummyType() {
        // Given
        DummyType dummyType = mock(DummyType.class);
        when(dummyType.copy(typeSystem, any())).thenReturn(dummyType);

        // When
        DummyType result = typeShuttle.visit(dummyType);

        // Then
        assertEquals(dummyType, result);
        verify(dummyType, times(1)).copy(typeSystem, any());
    }
}