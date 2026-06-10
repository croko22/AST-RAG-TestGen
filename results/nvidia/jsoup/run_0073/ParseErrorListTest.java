import org.jsoup.parser.ParseErrorList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParseErrorListTest {

    @Test
    public void testNoTracking() {
        // When: se crea una lista de errores sin seguimiento
        ParseErrorList parseErrorList = ParseErrorList.noTracking();

        // Then: se verifica que la lista tenga una capacidad inicial y tamaño máximo de 0
        assertEquals(0, parseErrorList.getInitialCapacity());
        assertEquals(0, parseErrorList.getMaxSize());
    }

    @Test
    public void testTracking() {
        // Given: un tamaño máximo para la lista de errores
        int maxSize = 10;

        // When: se crea una lista de errores con seguimiento
        ParseErrorList parseErrorList = ParseErrorList.tracking(maxSize);

        // Then: se verifica que la lista tenga la capacidad inicial y tamaño máximo correctos
        assertEquals(16, parseErrorList.getInitialCapacity());
        assertEquals(maxSize, parseErrorList.getMaxSize());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        // Given: una lista de errores
        ParseErrorList parseErrorList = ParseErrorList.tracking(10);

        // When: se clona la lista de errores
        ParseErrorList clonedList = (ParseErrorList) parseErrorList.clone();

        // Then: se verifica que la lista clonada tenga los mismos atributos que la original
        assertEquals(parseErrorList.getInitialCapacity(), clonedList.getInitialCapacity());
        assertEquals(parseErrorList.getMaxSize(), clonedList.getMaxSize());
    }

    @Test
    public void testCanAddError_False() {
        // Given: una lista de errores con tamaño máximo alcanzado
        ParseErrorList parseErrorList = ParseErrorList.tracking(0);

        // When: se verifica si se puede agregar un error a la lista
        boolean canAddError = parseErrorList.canAddError();

        // Then: se verifica que no se pueda agregar un error a la lista
        assertFalse(canAddError);
    }

    @Test
    public void testCanAddError_True() {
        // Given: una lista de errores con tamaño máximo no alcanzado
        ParseErrorList parseErrorList = ParseErrorList.tracking(10);

        // When: se verifica si se puede agregar un error a la lista
        boolean canAddError = parseErrorList.canAddError();

        // Then: se verifica que se pueda agregar un error a la lista
        assertTrue(canAddError);
    }

    @Test
    public void testGetMaxSize() {
        // Given: una lista de errores con tamaño máximo
        int maxSize = 10;
        ParseErrorList parseErrorList = ParseErrorList.tracking(maxSize);

        // When: se obtiene el tamaño máximo de la lista
        int actualMaxSize = parseErrorList.getMaxSize();

        // Then: se verifica que el tamaño máximo sea el correcto
        assertEquals(maxSize, actualMaxSize);
    }
}