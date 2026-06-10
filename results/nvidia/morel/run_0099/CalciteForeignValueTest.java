import net.hydromatic.morel.foreign.CalciteForeignValue;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.apache.calcite.schema.SchemaPlus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CalciteForeignValueTest {

    @Mock
    private net.hydromatic.morel.foreign.Calcite calcite;

    @Mock
    private SchemaPlus schema;

    @Mock
    private CalciteForeignValue.NameConverter nameConverter;

    @Mock
    private TypeSystem typeSystem;

    private CalciteForeignValue calciteForeignValue;

    @BeforeEach
    void setup() {
        calciteForeignValue = new CalciteForeignValue(calcite, schema, nameConverter);
    }

    @Test
    void testType() {
        // Given
        Type type = mock(Type.class);
        doReturn(type).when(typeSystem).recordType(any());

        // When
        Type result = calciteForeignValue.type(typeSystem);

        // Then
        assertNotNull(result);
        verify(typeSystem).recordType(any());
    }

    @Test
    void testValue() {
        // Given
        Object value = mock(Object.class);
        doReturn(value).when(calciteForeignValue).valueFor(schema);

        // When
        Object result = calciteForeignValue.value();

        // Then
        assertNotNull(result);
        assertEquals(value, result);
    }

    @Test
    void testToType() {
        // Given
        Type type = mock(Type.class);
        doReturn(type).when(typeSystem).recordType(any());

        // When
        Type result = calciteForeignValue.toType(schema, typeSystem);

        // Then
        assertNotNull(result);
        verify(typeSystem).recordType(any());
    }

    @Test
    void testToType_Table() {
        // Given
        Type type = mock(Type.class);
        doReturn(type).when(typeSystem).bagType(any());

        // When
        Type result = calciteForeignValue.toType(mock(List.class), mock(net.hydromatic.morel.schema.Table.class), typeSystem);

        // Then
        assertNotNull(result);
        verify(typeSystem).bagType(any());
    }

    @Test
    void testValueFor() {
        // Given
        Object value = mock(Object.class);
        doReturn(value).when(calciteForeignValue).valueFor(schema);

        // When
        Object result = calciteForeignValue.valueFor(schema);

        // Then
        assertNotNull(result);
        assertEquals(value, result);
    }

    @Test
    void testPlus() {
        // Given
        List<String> list = mock(List.class);
        String element = "element";

        // When
        List<String> result = calciteForeignValue.plus(list, element);

        // Then
        assertNotNull(result);
    }

    @Test
    void testConvert() {
        // Given
        String path = "path";
        String name = "name";

        // When
        String result = nameConverter.convert(mock(List.class), name);

        // Then
        assertNotNull(result);
    }
}