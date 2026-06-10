import net.hydromatic.morel.ast.Op;
import net.hydromatic.morel.type.PrimitiveType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import net.hydromatic.morel.type.TypeVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrimitiveTypeTest {

    @Mock
    private TypeSystem typeSystem;

    @Mock
    private TypeVisitor<String> typeVisitor;

    @Mock
    private UnaryOperator<Type> transform;

    private PrimitiveType primitiveType;

    @BeforeEach
    void setup() {
        primitiveType = PrimitiveType.BOOL;
    }

    @Test
    void testGetMoniker() {
        // Given: primitive type is BOOL
        // When: get moniker
        String moniker = primitiveType.moniker;
        // Then: moniker is "bool"
        assertEquals("bool", moniker);
    }

    @Test
    void testToString() {
        // Given: primitive type is BOOL
        // When: convert to string
        String string = primitiveType.toString();
        // Then: string is "bool"
        assertEquals("bool", string);
    }

    @Test
    void testKey() {
        // Given: primitive type is BOOL
        // When: get key
        // Then: key is not null
        assertNotNull(primitiveType.key());
    }

    @Test
    void testOp() {
        // Given: primitive type is BOOL
        // When: get op
        Op op = primitiveType.op();
        // Then: op is ID
        assertEquals(Op.ID, op);
    }

    @Test
    void testAccept() {
        // Given: primitive type is BOOL and type visitor
        // When: accept type visitor
        String result = primitiveType.accept(typeVisitor);
        // Then: result is not null
        verify(typeVisitor, times(1)).visit(primitiveType);
        assertNotNull(result);
    }

    @Test
    void testIsFinite() {
        // Given: primitive type is BOOL
        // When: check if finite
        boolean finite = primitiveType.isFinite();
        // Then: finite is true
        assertTrue(finite);
    }

    @Test
    void testIsDiscrete() {
        // Given: primitive type is INT and type system
        primitiveType = PrimitiveType.INT;
        // When: check if discrete
        boolean discrete = primitiveType.isDiscrete(typeSystem);
        // Then: discrete is true
        assertTrue(discrete);
    }

    @Test
    void testIsDiscrete_NotDiscrete() {
        // Given: primitive type is REAL and type system
        primitiveType = PrimitiveType.REAL;
        // When: check if discrete
        boolean discrete = primitiveType.isDiscrete(typeSystem);
        // Then: discrete is false
        assertFalse(discrete);
    }

    @Test
    void testCopy() {
        // Given: primitive type is BOOL, type system and transform
        // When: copy
        PrimitiveType copied = primitiveType.copy(typeSystem, transform);
        // Then: copied is same as original
        assertEquals(primitiveType, copied);
    }

    @Test
    void testArgNameTypes() {
        // Given: primitive type is UNIT
        primitiveType = PrimitiveType.UNIT;
        // When: get arg name types
        SortedMap<String, Type> argNameTypes = primitiveType.argNameTypes();
        // Then: arg name types is empty
        assertTrue(argNameTypes.isEmpty());
    }

    @Test
    void testArgNameTypes_NotUnit() {
        // Given: primitive type is not UNIT
        // When: get arg name types
        assertThrows(UnsupportedOperationException.class, () -> primitiveType.argNameTypes());
    }

    @Test
    void testArgType() {
        // Given: primitive type is UNIT
        primitiveType = PrimitiveType.UNIT;
        // When: get arg type
        assertThrows(IndexOutOfBoundsException.class, () -> primitiveType.argType(0));
    }

    @Test
    void testSpecializes() {
        // Given: primitive type is BOOL and same type
        Type type = PrimitiveType.BOOL;
        // When: check if specializes
        boolean specializes = primitiveType.specializes(type);
        // Then: specializes is true
        assertTrue(specializes);
    }

    @Test
    void testSpecializes_TypeVar() {
        // Given: primitive type is BOOL and type var
        Type type = mock(Type.class);
        when(type instanceof TypeVar).thenReturn(true);
        // When: check if specializes
        boolean specializes = primitiveType.specializes(type);
        // Then: specializes is true
        assertTrue(specializes);
    }

    @Test
    void testSpecializes_NotSpecializes() {
        // Given: primitive type is BOOL and different type
        Type type = PrimitiveType.INT;
        // When: check if specializes
        boolean specializes = primitiveType.specializes(type);
        // Then: specializes is false
        assertFalse(specializes);
    }
}