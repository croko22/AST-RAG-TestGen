import org.ice4j.ice.harvest.HarvestStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HarvestStatisticsTest {

    private HarvestStatistics harvestStatistics;

    @BeforeEach
    public void setup() {
        harvestStatistics = new HarvestStatistics();
        harvestStatistics.setName("Test Harvester");
    }

    @Test
    public void testGetHarvestDuration_NotHarvesting() {
        // Given: harvester is not currently harvesting
        // When: getHarvestDuration is called
        long harvestDuration = harvestStatistics.getHarvestDuration();
        // Then: harvest duration is 0
        assertEquals(0, harvestDuration);
    }

    @Test
    public void testGetHarvestDuration_Harvesting() {
        // Given: harvester is currently harvesting
        harvestStatistics.startHarvestTiming();
        // When: getHarvestDuration is called
        long harvestDuration = harvestStatistics.getHarvestDuration();
        // Then: harvest duration is greater than 0
        assertTrue(harvestDuration > 0);
    }

    @Test
    public void testGetTotalCandidateCount_NoCandidates() {
        // Given: no candidates have been harvested
        // When: getTotalCandidateCount is called
        int totalCandidateCount = harvestStatistics.getTotalCandidateCount();
        // Then: total candidate count is 0
        assertEquals(0, totalCandidateCount);
    }

    @Test
    public void testGetTotalCandidateCount_WithCandidates() {
        // Given: some candidates have been harvested
        harvestStatistics.startHarvestTiming();
        harvestStatistics.stopHarvestTiming(10);
        // When: getTotalCandidateCount is called
        int totalCandidateCount = harvestStatistics.getTotalCandidateCount();
        // Then: total candidate count is greater than 0
        assertEquals(10, totalCandidateCount);
    }

    @Test
    public void testGetHarvestCount_NoHarvests() {
        // Given: no harvests have been completed
        // When: getHarvestCount is called
        int harvestCount = harvestStatistics.getHarvestCount();
        // Then: harvest count is 0
        assertEquals(0, harvestCount);
    }

    @Test
    public void testGetHarvestCount_WithHarvests() {
        // Given: some harvests have been completed
        harvestStatistics.startHarvestTiming();
        harvestStatistics.stopHarvestTiming(10);
        // When: getHarvestCount is called
        int harvestCount = harvestStatistics.getHarvestCount();
        // Then: harvest count is greater than 0
        assertEquals(1, harvestCount);
    }

    @Test
    public void testGetName() {
        // Given: harvester name has been set
        // When: getName is called
        String harvesterName = harvestStatistics.getName();
        // Then: harvester name is correct
        assertEquals("Test Harvester", harvesterName);
    }

    @Test
    public void testToString() {
        // Given: harvester statistics have been updated
        harvestStatistics.startHarvestTiming();
        harvestStatistics.stopHarvestTiming(10);
        // When: toString is called
        String toString = harvestStatistics.toString();
        // Then: string representation is correct
        assertNotNull(toString);
        assertTrue(toString.contains("Test Harvester"));
        assertTrue(toString.contains("time="));
        assertTrue(toString.contains("ms"));
        assertTrue(toString.contains("harvests="));
        assertTrue(toString.contains("candidates="));
    }
}