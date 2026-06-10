import net.hydromatic.morel.compile.BagPrinter;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.TypeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BagPrinterTest {

    @Mock
    private Type elementType;

    @Mock
    private TypeSystem typeSystem;

    private BagPrinter bagPrinter;

    @BeforeEach
    void setup() {
        bagPrinter = BagPrinter.NATURAL;
    }

    @Test
    void testOrder_NaturalOrder() {
        // Given: a list of elements
        List<Object> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        elements.add("Element3");

        // When: order method is called
        List<Object> orderedElements = bagPrinter.order(elements, elementType);

        // Then: elements are returned in the same order
        assertEquals(elements, orderedElements);
    }

    @Test
    void testOrder_EmptyList() {
        // Given: an empty list of elements
        List<Object> elements = new ArrayList<>();

        // When: order method is called
        List<Object> orderedElements = bagPrinter.order(elements, elementType);

        // Then: an empty list is returned
        assertTrue(orderedElements.isEmpty());
    }

    @Test
    void testOrder_NullList() {
        // Given: a null list of elements
        List<Object> elements = null;

        // When: order method is called
        assertThrows(NullPointerException.class, () -> bagPrinter.order(elements, elementType));
    }

    @Test
    void testOrder_NullElementType() {
        // Given: a null element type
        List<Object> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        elements.add("Element3");

        // When: order method is called
        assertThrows(NullPointerException.class, () -> bagPrinter.order(elements, null));
    }
}