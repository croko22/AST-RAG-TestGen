import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BoundsType_Test {

    @Test
    public void test_name_BoundsFloat() {
        // Given: entrada válida para BoundsFloat
        int entrada = 0;
        
        // When: se ejecuta el método name
        String resultado = com.github.davidmoten.rtree.fbs.generated.BoundsType_.name(entrada);
        
        // Then: se verifica el resultado
        assertEquals("BoundsFloat", resultado);
    }

    @Test
    public void test_name_BoundsDouble() {
        // Given: entrada válida para BoundsDouble
        int entrada = 1;
        
        // When: se ejecuta el método name
        String resultado = com.github.davidmoten.rtree.fbs.generated.BoundsType_.name(entrada);
        
        // Then: se verifica el resultado
        assertEquals("BoundsDouble", resultado);
    }

    @Test
    public void test_name_ValorNoValido() {
        // Given: entrada no válida
        int entrada = 2;
        
        // When: se ejecuta el método name
        String resultado = com.github.davidmoten.rtree.fbs.generated.BoundsType_.name(entrada);
        
        // Then: se verifica el resultado
        assertNull(resultado);
    }

    @Test
    public void test_name_ValorNegativo() {
        // Given: entrada negativa
        int entrada = -1;
        
        // When: se ejecuta el método name
        String resultado = com.github.davidmoten.rtree.fbs.generated.BoundsType_.name(entrada);
        
        // Then: se verifica el resultado
        assertNull(resultado);
    }
}