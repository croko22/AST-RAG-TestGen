import net.hydromatic.morel.eval.Unit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UnitTest {

    @Test
    public void testToString() {
        // Given: Unit instance
        Unit unit = Unit.INSTANCE;

        // When: toString method is called
        String result = unit.toString();

        // Then: result is "()"
        assertEquals("()", result);
    }

    @Test
    public void testGet_IndexOutOfBoundsException() {
        // Given: Unit instance and index
        Unit unit = Unit.INSTANCE;
        int index = 0;

        // When / Then: get method throws IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () -> unit.get(index));
    }

    @Test
    public void testSize() {
        // Given: Unit instance
        Unit unit = Unit.INSTANCE;

        // When: size method is called
        int result = unit.size();

        // Then: result is 0
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_Equal() {
        // Given: Two Unit instances
        Unit unit1 = Unit.INSTANCE;
        Unit unit2 = Unit.INSTANCE;

        // When: compareTo method is called
        int result = unit1.compareTo(unit2);

        // Then: result is 0 (equal)
        assertEquals(0, result);
    }

    @Test
    public void testCompareTo_SameInstance() {
        // Given: Same Unit instance
        Unit unit = Unit.INSTANCE;

        // When: compareTo method is called with the same instance
        int result = unit.compareTo(unit);

        // Then: result is 0 (equal)
        assertEquals(0, result);
    }
}