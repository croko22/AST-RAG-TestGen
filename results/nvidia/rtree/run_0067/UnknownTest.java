import com.github.davidmoten.rtree.InternalStructure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InternalStructureTest {

    @Test
    public void testValues() {
        // Then: verifica los valores de la enumeracion
        assertEquals(2, InternalStructure.values().length);
        assertTrue(Enum.contains(InternalStructure.class, InternalStructure.SINGLE_ARRAY));
        assertTrue(Enum.contains(InternalStructure.class, InternalStructure.DEFAULT));
    }

    @Test
    public void testValueOf() {
        // Given: nombres de los valores de la enumeracion
        String singleArrayName = "SINGLE_ARRAY";
        String defaultName = "DEFAULT";

        // When: obtiene los valores de la enumeracion a partir de sus nombres
        InternalStructure singleArrayType = InternalStructure.valueOf(singleArrayName);
        InternalStructure defaultType = InternalStructure.valueOf(defaultName);

        // Then: verifica los valores obtenidos
        assertEquals(InternalStructure.SINGLE_ARRAY, singleArrayType);
        assertEquals(InternalStructure.DEFAULT, defaultType);
    }

    @Test
    public void testValueOf_Unknown() {
        // Given: un nombre desconocido
        String unknownName = "UNKNOWN";

        // When / Then: se espera una excepcion al intentar obtener un valor desconocido
        assertThrows(IllegalArgumentException.class, () -> InternalStructure.valueOf(unknownName));
    }

    @Test
    public void testOrdinal() {
        // Then: verifica los ordinales de los valores de la enumeracion
        assertEquals(0, InternalStructure.SINGLE_ARRAY.ordinal());
        assertEquals(1, InternalStructure.DEFAULT.ordinal());
    }

    @Test
    public void testToString() {
        // Then: verifica las representaciones en cadena de los valores de la enumeracion
        assertEquals("SINGLE_ARRAY", InternalStructure.SINGLE_ARRAY.toString());
        assertEquals("DEFAULT", InternalStructure.DEFAULT.toString());
    }
}