import net.hydromatic.morel.type.BaseType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.ast.Op;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class BaseTypeTest {

    @Mock
    private Op op;

    private BaseType baseType;

    @BeforeEach
    void setup() {
        baseType = new BaseType(op) {
            @Override
            public String key() {
                return "TestKey";
            }
        };
    }

    @Test
    void testOp() {
        // When: se obtiene la operacion
        Op result = baseType.op();

        // Then: se verifica el resultado
        assertSame(op, result);
    }

    @Test
    void testToString() {
        // When: se obtiene la representacion como cadena
        String result = baseType.toString();

        // Then: se verifica el resultado
        assertEquals("TestKey", result);
    }

    @Test
    void testEquals_SameInstance() {
        // When: se compara con la misma instancia
        boolean result = baseType.equals(baseType);

        // Then: se verifica el resultado
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentInstance_SameKey() {
        // Given: otra instancia con la misma clave
        BaseType other = new BaseType(op) {
            @Override
            public String key() {
                return "TestKey";
            }
        };

        // When: se compara con la otra instancia
        boolean result = baseType.equals(other);

        // Then: se verifica el resultado
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentInstance_DifferentKey() {
        // Given: otra instancia con una clave diferente
        BaseType other = new BaseType(op) {
            @Override
            public String key() {
                return "DifferentKey";
            }
        };

        // When: se compara con la otra instancia
        boolean result = baseType.equals(other);

        // Then: se verifica el resultado
        assertFalse(result);
    }

    @Test
    void testEquals_Null() {
        // When: se compara con null
        boolean result = baseType.equals(null);

        // Then: se verifica el resultado
        assertFalse(result);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given: un objeto de una clase diferente
        Object other = new Object();

        // When: se compara con el objeto
        boolean result = baseType.equals(other);

        // Then: se verifica el resultado
        assertFalse(result);
    }

    @Test
    void testHashCode() {
        // When: se obtiene el codigo de hash
        int result = baseType.hashCode();

        // Then: se verifica el resultado
        assertNotNull(result);
        assertEquals("TestKey".hashCode(), result);
    }
}