import org.ice4j.ice.CheckListState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckListStateTest {

    @Test
    public void testCheckListStateValues() {
        // Given: CheckListState enum values
        CheckListState[] expectedValues = {CheckListState.RUNNING, CheckListState.COMPLETED, CheckListState.FAILED};

        // When: Get CheckListState values
        CheckListState[] actualValues = CheckListState.values();

        // Then: Verify CheckListState values
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testCheckListStateToString() {
        // Given: CheckListState enum values
        CheckListState running = CheckListState.RUNNING;
        CheckListState completed = CheckListState.COMPLETED;
        CheckListState failed = CheckListState.FAILED;

        // When: Get string representation of CheckListState values
        String runningToString = running.toString();
        String completedToString = completed.toString();
        String failedToString = failed.toString();

        // Then: Verify string representation of CheckListState values
        assertEquals("Running", runningToString);
        assertEquals("Completed", completedToString);
        assertEquals("Failed", failedToString);
    }

    @Test
    public void testCheckListStateOrdinal() {
        // Given: CheckListState enum values
        CheckListState running = CheckListState.RUNNING;
        CheckListState completed = CheckListState.COMPLETED;
        CheckListState failed = CheckListState.FAILED;

        // When: Get ordinal of CheckListState values
        int runningOrdinal = running.ordinal();
        int completedOrdinal = completed.ordinal();
        int failedOrdinal = failed.ordinal();

        // Then: Verify ordinal of CheckListState values
        assertEquals(0, runningOrdinal);
        assertEquals(1, completedOrdinal);
        assertEquals(2, failedOrdinal);
    }

    @Test
    public void testCheckListStateName() {
        // Given: CheckListState enum values
        CheckListState running = CheckListState.RUNNING;
        CheckListState completed = CheckListState.COMPLETED;
        CheckListState failed = CheckListState.FAILED;

        // When: Get name of CheckListState values
        String runningName = running.name();
        String completedName = completed.name();
        String failedName = failed.name();

        // Then: Verify name of CheckListState values
        assertEquals("RUNNING", runningName);
        assertEquals("COMPLETED", completedName);
        assertEquals("FAILED", failedName);
    }
}