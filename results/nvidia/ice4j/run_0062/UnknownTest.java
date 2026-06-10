import org.ice4j.ice.IceProcessingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IceProcessingStateTest {

    private IceProcessingState waitingState;
    private IceProcessingState runningState;
    private IceProcessingState completedState;
    private IceProcessingState failedState;
    private IceProcessingState terminatedState;

    @BeforeEach
    public void setup() {
        waitingState = IceProcessingState.WAITING;
        runningState = IceProcessingState.RUNNING;
        completedState = IceProcessingState.COMPLETED;
        failedState = IceProcessingState.FAILED;
        terminatedState = IceProcessingState.TERMINATED;
    }

    @Test
    public void testToString_Waiting() {
        // Given: waiting state
        // When: toString method is called
        String result = waitingState.toString();
        // Then: result should be "Waiting"
        assertEquals("Waiting", result);
    }

    @Test
    public void testToString_Running() {
        // Given: running state
        // When: toString method is called
        String result = runningState.toString();
        // Then: result should be "Running"
        assertEquals("Running", result);
    }

    @Test
    public void testToString_Completed() {
        // Given: completed state
        // When: toString method is called
        String result = completedState.toString();
        // Then: result should be "Completed"
        assertEquals("Completed", result);
    }

    @Test
    public void testToString_Failed() {
        // Given: failed state
        // When: toString method is called
        String result = failedState.toString();
        // Then: result should be "Failed"
        assertEquals("Failed", result);
    }

    @Test
    public void testToString_Terminated() {
        // Given: terminated state
        // When: toString method is called
        String result = terminatedState.toString();
        // Then: result should be "Terminated"
        assertEquals("Terminated", result);
    }

    @Test
    public void testIsOver_Waiting() {
        // Given: waiting state
        // When: isOver method is called
        boolean result = waitingState.isOver();
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testIsOver_Running() {
        // Given: running state
        // When: isOver method is called
        boolean result = runningState.isOver();
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testIsOver_Completed() {
        // Given: completed state
        // When: isOver method is called
        boolean result = completedState.isOver();
        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testIsOver_Failed() {
        // Given: failed state
        // When: isOver method is called
        boolean result = failedState.isOver();
        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testIsOver_Terminated() {
        // Given: terminated state
        // When: isOver method is called
        boolean result = terminatedState.isOver();
        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testIsEstablished_Waiting() {
        // Given: waiting state
        // When: isEstablished method is called
        boolean result = waitingState.isEstablished();
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testIsEstablished_Running() {
        // Given: running state
        // When: isEstablished method is called
        boolean result = runningState.isEstablished();
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testIsEstablished_Completed() {
        // Given: completed state
        // When: isEstablished method is called
        boolean result = completedState.isEstablished();
        // Then: result should be true
        assertTrue(result);
    }

    @Test
    public void testIsEstablished_Failed() {
        // Given: failed state
        // When: isEstablished method is called
        boolean result = failedState.isEstablished();
        // Then: result should be false
        assertFalse(result);
    }

    @Test
    public void testIsEstablished_Terminated() {
        // Given: terminated state
        // When: isEstablished method is called
        boolean result = terminatedState.isEstablished();
        // Then: result should be true
        assertTrue(result);
    }
}