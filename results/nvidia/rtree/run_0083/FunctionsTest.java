import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import rx.functions.Func1;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FunctionsTest {

    @Test
    public void testIdentity() {
        // Given: un objeto de cualquier tipo
        String objeto = "cadena de prueba";
        // When: se aplica la función identidad
        Func1<String, String> identityFunction = Functions.identity();
        String resultado = identityFunction.call(objeto);
        // Then: se verifica que el resultado sea el mismo objeto
        assertEquals(objeto, resultado);
    }

    @Test
    public void testAlwaysTrue() {
        // Given: un objeto de cualquier tipo
        String objeto = "cadena de prueba";
        // When: se aplica la función que siempre devuelve true
        Func1<String, Boolean> alwaysTrueFunction = Functions.alwaysTrue();
        Boolean resultado = alwaysTrueFunction.call(objeto);
        // Then: se verifica que el resultado sea true
        assertTrue(resultado);
    }

    @Test
    public void testAlwaysTrueConNull() {
        // Given: null
        // When: se aplica la función que siempre devuelve true
        Func1<String, Boolean> alwaysTrueFunction = Functions.alwaysTrue();
        Boolean resultado = alwaysTrueFunction.call(null);
        // Then: se verifica que el resultado sea true
        assertTrue(resultado);
    }

    @Test
    public void testAlwaysFalse() {
        // Given: un objeto de cualquier tipo
        String objeto = "cadena de prueba";
        // When: se aplica la función que siempre devuelve false
        Func1<String, Boolean> alwaysFalseFunction = Functions.alwaysFalse();
        Boolean resultado = alwaysFalseFunction.call(objeto);
        // Then: se verifica que el resultado sea false
        assertFalse(resultado);
    }

    @Test
    public void testAlwaysFalseConNull() {
        // Given: null
        // When: se aplica la función que siempre devuelve false
        Func1<String, Boolean> alwaysFalseFunction = Functions.alwaysFalse();
        Boolean resultado = alwaysFalseFunction.call(null);
        // Then: se verifica que el resultado sea false
        assertFalse(resultado);
    }
}