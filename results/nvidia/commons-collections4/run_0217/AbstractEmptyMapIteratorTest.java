import org.apache.commons.collections4.iterators.AbstractEmptyMapIterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AbstractEmptyMapIteratorTest {

    @InjectMocks
    private AbstractEmptyMapIterator<String, String> emptyMapIterator;

    @Test
    public void testGetKey() {
        // When: se intenta obtener la clave
        // Then: se espera una excepcion
        assertThrows(IllegalStateException.class, () -> emptyMapIterator.getKey());
    }

    @Test
    public void testGetValue() {
        // When: se intenta obtener el valor
        // Then: se espera una excepcion
        assertThrows(IllegalStateException.class, () -> emptyMapIterator.getValue());
    }

    @Test
    public void testSetValue() {
        // Given: un valor a establecer
        String value = "valor";
        // When: se intenta establecer el valor
        // Then: se espera una excepcion
        assertThrows(IllegalStateException.class, () -> emptyMapIterator.setValue(value));
    }
}