import org.ice4j.ice.CandidatePairState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CandidatePairStateTest {

    @Test
    public void testValues() {
        // Given: CandidatePairState enum
        // When: We retrieve all values
        CandidatePairState[] values = CandidatePairState.values();
        // Then: We verify the number of values and their names
        assertEquals(5, values.length);
        assertEquals("Waiting", values[0].toString());
        assertEquals("In-Progress", values[1].toString());
        assertEquals("Succeeded", values[2].toString());
        assertEquals("Failed", values[3].toString());
        assertEquals("Frozen", values[4].toString());
    }

    @Test
    public void testValueOf() {
        // Given: CandidatePairState enum and a valid name
        String name = "Waiting";
        // When: We retrieve the enum value by name
        CandidatePairState value = CandidatePairState.valueOf(name);
        // Then: We verify the retrieved value
        assertEquals(CandidatePairState.WAITING, value);
    }

    @Test
    public void testValueOf_InvalidName() {
        // Given: CandidatePairState enum and an invalid name
        String name = "Invalid";
        // When and Then: We expect an exception
        assertThrows(IllegalArgumentException.class, () -> CandidatePairState.valueOf(name));
    }

    @Test
    public void testToString() {
        // Given: CandidatePairState enum value
        CandidatePairState value = CandidatePairState.WAITING;
        // When: We retrieve the string representation
        String stringValue = value.toString();
        // Then: We verify the string representation
        assertEquals("Waiting", stringValue);
    }
}