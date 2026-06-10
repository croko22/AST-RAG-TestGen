import net.hydromatic.morel.compile.BuiltIn;
import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.Keys;
import net.hydromatic.morel.type.PrimitiveType;
import net.hydromatic.morel.type.RecordLikeType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiscretesTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private Type type;

    @Mock
    private RecordLikeType recordLikeType;

    @Mock
    private DataType dataType;

    private Discretes discretes;

    @BeforeEach
    void setup() {
        discretes = new Discretes();
    }

    @Test
    void testDiscreteFor_PrimitiveType_Int() {
        // Given
        when(type instanceof PrimitiveType).thenReturn(true);
        when(((PrimitiveType) type).ordinal()).thenReturn(PrimitiveType.INT.ordinal());

        // When
        Discrete<Object> discrete = Discretes.discreteFor(typeSystem, type);

        // Then
        assertNotNull(discrete);
        assertEquals(Discretes.INT, discrete);
    }

    @Test
    void testDiscreteFor_PrimitiveType_Char() {
        // Given
        when(type instanceof PrimitiveType).thenReturn(true);
        when(((PrimitiveType) type).ordinal()).thenReturn(PrimitiveType.CHAR.ordinal());

        // When
        Discrete<Object> discrete = Discretes.discreteFor(typeSystem, type);

        // Then
        assertNotNull(discrete);
        assertEquals(Discretes.CHAR, discrete);
    }

    @Test
    void testDiscreteFor_PrimitiveType_Bool() {
        // Given
        when(type instanceof PrimitiveType).thenReturn(true);
        when(((PrimitiveType) type).ordinal()).thenReturn(PrimitiveType.BOOL.ordinal());

        // When
        Discrete<Object> discrete = Discretes.discreteFor(typeSystem, type);

        // Then
        assertNotNull(discrete);
        assertEquals(Discretes.BOOL, discrete);
    }

    @Test
    void testDiscreteFor_PrimitiveType_Unit() {
        // Given
        when(type instanceof PrimitiveType).thenReturn(true);
        when(((PrimitiveType) type).ordinal()).thenReturn(PrimitiveType.UNIT.ordinal());

        // When
        Discrete<Object> discrete = Discretes.discreteFor(typeSystem, type);

        // Then
        assertNotNull(discrete);
        assertEquals(Discretes.UNIT, discrete);
    }

    @Test
    void testDiscreteFor_PrimitiveType_Unknown() {
        // Given
        when(type instanceof PrimitiveType).thenReturn(true);
        when(((PrimitiveType) type).ordinal()).thenReturn(PrimitiveType.UNKNOWN.ordinal());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> Discretes.discreteFor(typeSystem, type));
    }

    @Test
    void testDiscreteFor_RecordLikeType() {
        // Given
        when(type instanceof RecordLikeType).thenReturn(true);
        when(recordLikeType.argTypes()).thenReturn(new ArrayList<>());

        // When
        Discrete<Object> discrete = Discretes.discreteFor(typeSystem, recordLikeType);

        // Then
        assertNotNull(discrete);
    }

    @Test
    void testDiscreteFor_DataType() {
        // Given
        when(type instanceof DataType).thenReturn(true);
        when(dataType.name).thenReturn(BuiltIn.Datatype.DESCENDING.mlName());

        // When
        Discrete<Object> discrete = Discretes.discreteFor(typeSystem, dataType);

        // Then
        assertNotNull(discrete);
    }

    @Test
    void testDiscreteFor_UnknownType() {
        // Given
        when(type instanceof PrimitiveType).thenReturn(false);
        when(type instanceof RecordLikeType).thenReturn(false);
        when(type instanceof DataType).thenReturn(false);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> Discretes.discreteFor(typeSystem, type));
    }

    @Test
    void testTupleDiscrete() {
        // Given
        when(recordLikeType.argTypes()).thenReturn(new ArrayList<>());

        // When
        Discrete<Object> discrete = Discretes.tupleDiscrete(typeSystem, recordLikeType);

        // Then
        assertNotNull(discrete);
    }

    @Test
    void testDataTypeDiscrete_Descending() {
        // Given
        when(dataType.name).thenReturn(BuiltIn.Datatype.DESCENDING.mlName());

        // When
        Discrete<Object> discrete = Discretes.dataTypeDiscrete(typeSystem, dataType);

        // Then
        assertNotNull(discrete);
    }

    @Test
    void testDataTypeDiscrete_Sum() {
        // Given
        when(dataType.name).thenReturn("Sum");

        // When
        Discrete<Object> discrete = Discretes.dataTypeDiscrete(typeSystem, dataType);

        // Then
        assertNotNull(discrete);
    }

    @Test
    void testStepTuple_Forward() {
        // Given
        List<Object> values = new ArrayList<>();
        List<Discrete<Object>> components = new ArrayList<>();

        // When
        Object result = Discretes.stepTuple(values, components, true);

        // Then
        assertNull(result);
    }

    @Test
    void testStepTuple_Backward() {
        // Given
        List<Object> values = new ArrayList<>();
        List<Discrete<Object>> components = new ArrayList<>();

        // When
        Object result = Discretes.stepTuple(values, components, false);

        // Then
        assertNull(result);
    }

    @Test
    void testTupleExtreme_Min() {
        // Given
        List<Discrete<Object>> components = new ArrayList<>();

        // When
        Object result = Discretes.tupleExtreme(components, true);

        // Then
        assertNull(result);
    }

    @Test
    void testTupleExtreme_Max() {
        // Given
        List<Discrete<Object>> components = new ArrayList<>();

        // When
        Object result = Discretes.tupleExtreme(components, false);

        // Then
        assertNull(result);
    }
}