import org.apache.commons.collections4.Unmodifiable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UnmodifiableTest {

    @Test
    public void testUnmodifiableInterface() {
        // Given: Unmodifiable interface
        Unmodifiable unmodifiable = new Unmodifiable() {};

        // When: Checking if the interface is implemented
        boolean isInstance = unmodifiable instanceof Unmodifiable;

        // Then: Verifying the result
        assertTrue(isInstance);
    }

    @Test
    public void testUnmodifiableMarkerInterface() {
        // Given: Unmodifiable marker interface
        Unmodifiable unmodifiable = new Unmodifiable() {};

        // When: Checking if the interface has any methods
        int methodCount = Unmodifiable.class.getMethods().length;

        // Then: Verifying the result
        assertEquals(0, methodCount);
    }

    @Test
    public void testUnmodifiableInstanceCheck() {
        // Given: Unmodifiable instance
        Unmodifiable unmodifiable = new Unmodifiable() {};

        // When: Checking if the instance is an Unmodifiable
        boolean isUnmodifiable = unmodifiable instanceof Unmodifiable;

        // Then: Verifying the result
        assertTrue(isUnmodifiable);
    }

    @Test
    public void testUnmodifiableNullCheck() {
        // Given: Null Unmodifiable instance
        Unmodifiable unmodifiable = null;

        // When: Checking if the instance is an Unmodifiable
        boolean isUnmodifiable = unmodifiable instanceof Unmodifiable;

        // Then: Verifying the result
        assertFalse(isUnmodifiable);
    }
}