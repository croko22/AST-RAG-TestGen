import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KeepAliveStrategyTest {

    private KeepAliveStrategy keepAliveStrategy;

    @BeforeEach
    void setup() {
        // No setup needed for this test class
    }

    @Test
    public void testFromString_SelectedOnly() {
        // Given: the string "selected_only"
        String string = "selected_only";
        
        // When: fromString is called with the given string
        KeepAliveStrategy strategy = KeepAliveStrategy.fromString(string);
        
        // Then: the result should be SELECTED_ONLY
        assertEquals(KeepAliveStrategy.SELECTED_ONLY, strategy);
    }

    @Test
    public void testFromString_SelectedAndTcp() {
        // Given: the string "selected_and_tcp"
        String string = "selected_and_tcp";
        
        // When: fromString is called with the given string
        KeepAliveStrategy strategy = KeepAliveStrategy.fromString(string);
        
        // Then: the result should be SELECTED_AND_TCP
        assertEquals(KeepAliveStrategy.SELECTED_AND_TCP, strategy);
    }

    @Test
    public void testFromString_AllSucceeded() {
        // Given: the string "all_succeeded"
        String string = "all_succeeded";
        
        // When: fromString is called with the given string
        KeepAliveStrategy strategy = KeepAliveStrategy.fromString(string);
        
        // Then: the result should be ALL_SUCCEEDED
        assertEquals(KeepAliveStrategy.ALL_SUCCEEDED, strategy);
    }

    @Test
    public void testFromString_Unknown() {
        // Given: an unknown string
        String string = "unknown";
        
        // When: fromString is called with the given string
        KeepAliveStrategy strategy = KeepAliveStrategy.fromString(string);
        
        // Then: the result should be null
        assertNull(strategy);
    }

    @Test
    public void testToString_SelectedOnly() {
        // Given: the SELECTED_ONLY strategy
        KeepAliveStrategy strategy = KeepAliveStrategy.SELECTED_ONLY;
        
        // When: toString is called on the strategy
        String string = strategy.toString();
        
        // Then: the result should be "selected_only"
        assertEquals("selected_only", string);
    }

    @Test
    public void testToString_SelectedAndTcp() {
        // Given: the SELECTED_AND_TCP strategy
        KeepAliveStrategy strategy = KeepAliveStrategy.SELECTED_AND_TCP;
        
        // When: toString is called on the strategy
        String string = strategy.toString();
        
        // Then: the result should be "selected_and_tcp"
        assertEquals("selected_and_tcp", string);
    }

    @Test
    public void testToString_AllSucceeded() {
        // Given: the ALL_SUCCEEDED strategy
        KeepAliveStrategy strategy = KeepAliveStrategy.ALL_SUCCEEDED;
        
        // When: toString is called on the strategy
        String string = strategy.toString();
        
        // Then: the result should be "all_succeeded"
        assertEquals("all_succeeded", string);
    }
}