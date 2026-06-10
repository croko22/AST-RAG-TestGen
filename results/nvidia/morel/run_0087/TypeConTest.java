import net.hydromatic.morel.type.DataType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeCon;
import net.hydromatic.morel.type.TypeVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeConTest {

    @Mock
    private DataType dataType;

    @Mock
    private Type.Key typeKey;

    @Mock
    private TypeSystem typeSystem;

    private TypeCon typeCon;

    @BeforeEach
    void setup() {
        typeCon = TypeCon.of(dataType, "testName", typeKey);
    }

    @Test
    void testOf() {
        // Given
        String name = "testName";
        DataType dataType = mock(DataType.class);
        Type.Key typeKey = mock(Type.Key.class);

        // When
        TypeCon typeCon = TypeCon.of(dataType, name, typeKey);

        // Then
        assertNotNull(typeCon);
        assertEquals(dataType, typeCon.dataType);
        assertEquals(name, typeCon.name);
        assertEquals(typeKey, typeCon.argTypeKey);
    }

    @Test
    void testGetDataType() {
        // Given
        DataType expectedDataType = dataType;

        // When
        DataType actualDataType = typeCon.dataType;

        // Then
        assertEquals(expectedDataType, actualDataType);
    }

    @Test
    void testGetName() {
        // Given
        String expectedName = "testName";

        // When
        String actualName = typeCon.name;

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    void testGetArgTypeKey() {
        // Given
        Type.Key expectedTypeKey = typeKey;

        // When
        Type.Key actualTypeKey = typeCon.argTypeKey;

        // Then
        assertEquals(expectedTypeKey, actualTypeKey);
    }

    @Test
    void testEquals_SameObject() {
        // Given
        TypeCon sameTypeCon = typeCon;

        // When
        boolean isEqual = typeCon.equals(sameTypeCon);

        // Then
        assertTrue(isEqual);
    }

    @Test
    void testEquals_DifferentObjects_SameValues() {
        // Given
        TypeCon differentTypeCon = TypeCon.of(dataType, "testName", typeKey);

        // When
        boolean isEqual = typeCon.equals(differentTypeCon);

        // Then
        assertTrue(isEqual);
    }

    @Test
    void testEquals_DifferentObjects_DifferentValues() {
        // Given
        DataType differentDataType = mock(DataType.class);
        TypeCon differentTypeCon = TypeCon.of(differentDataType, "differentName", typeKey);

        // When
        boolean isEqual = typeCon.equals(differentTypeCon);

        // Then
        assertFalse(isEqual);
    }

    @Test
    void testHashCode() {
        // Given
        int expectedHashCode = typeCon.hashCode();

        // When
        int actualHashCode = typeCon.hashCode();

        // Then
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    void testToString() {
        // Given
        String expectedToString = "TypeCon{" +
                "dataType=" + dataType +
                ", name='" + "testName" + '\'' +
                ", argTypeKey=" + typeKey +
                '}';

        // When
        String actualToString = typeCon.toString();

        // Then
        assertEquals(expectedToString, actualToString);
    }
}