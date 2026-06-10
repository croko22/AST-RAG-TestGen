import org.ice4j.ice.NominationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NominationStrategyTest {

    private NominationStrategy nominationStrategy;

    @BeforeEach
    public void setup() {
        nominationStrategy = NominationStrategy.NONE;
    }

    @Test
    public void testToString() {
        // Given: a nomination strategy
        NominationStrategy strategy = NominationStrategy.NONE;

        // When: we call toString on the strategy
        String strategyName = strategy.toString();

        // Then: the result should be the name of the strategy
        assertEquals("None", strategyName);
    }

    @Test
    public void testFromString() {
        // Given: a string representing a nomination strategy
        String strategyName = "NominateFirstValid";

        // When: we call fromString on the NominationStrategy class
        NominationStrategy strategy = NominationStrategy.fromString(strategyName);

        // Then: the result should be the corresponding nomination strategy
        assertEquals(NominationStrategy.NOMINATE_FIRST_VALID, strategy);
    }

    @Test
    public void testFromString_UnknownStrategy() {
        // Given: a string representing an unknown nomination strategy
        String strategyName = "UnknownStrategy";

        // When: we call fromString on the NominationStrategy class
        NominationStrategy strategy = NominationStrategy.fromString(strategyName);

        // Then: the result should be null
        assertNull(strategy);
    }

    @Test
    public void testFromString_NullInput() {
        // Given: a null string
        String strategyName = null;

        // When: we call fromString on the NominationStrategy class
        NominationStrategy strategy = NominationStrategy.fromString(strategyName);

        // Then: the result should be null
        assertNull(strategy);
    }

    @Test
    public void testValues() {
        // Given: the NominationStrategy class
        NominationStrategy[] strategies = NominationStrategy.values();

        // Then: the array should contain all nomination strategies
        assertEquals(5, strategies.length);
        assertTrue(java.util.Arrays.asList(strategies).contains(NominationStrategy.NONE));
        assertTrue(java.util.Arrays.asList(strategies).contains(NominationStrategy.NOMINATE_FIRST_VALID));
        assertTrue(java.util.Arrays.asList(strategies).contains(NominationStrategy.NOMINATE_HIGHEST_PRIO));
        assertTrue(java.util.Arrays.asList(strategies).contains(NominationStrategy.NOMINATE_FIRST_HOST_OR_REFLEXIVE_VALID));
        assertTrue(java.util.Arrays.asList(strategies).contains(NominationStrategy.NOMINATE_BEST_RTT));
    }
}