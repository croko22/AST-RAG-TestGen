import org.ice4j.ice.Candidate;
import org.ice4j.ice.CandidatePrioritizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CandidatePrioritizerTest {

    @Mock
    private Candidate<?> candidate1;

    @Mock
    private Candidate<?> candidate2;

    private CandidatePrioritizer candidatePrioritizer;

    @BeforeEach
    public void setup() {
        candidatePrioritizer = new CandidatePrioritizer();
    }

    @Test
    public void testCompareCandidates_LowerPriority_Returns1() {
        // Given: candidate1 has lower priority than candidate2
        when(candidate1.getPriority()).thenReturn(10);
        when(candidate2.getPriority()).thenReturn(20);

        // When: compare candidates
        int result = CandidatePrioritizer.compareCandidates(candidate1, candidate2);

        // Then: result is 1
        assertEquals(1, result);
    }

    @Test
    public void testCompareCandidates_EqualPriority_Returns0() {
        // Given: candidate1 and candidate2 have equal priority
        when(candidate1.getPriority()).thenReturn(10);
        when(candidate2.getPriority()).thenReturn(10);

        // When: compare candidates
        int result = CandidatePrioritizer.compareCandidates(candidate1, candidate2);

        // Then: result is 0
        assertEquals(0, result);
    }

    @Test
    public void testCompareCandidates_HigherPriority_ReturnsMinus1() {
        // Given: candidate1 has higher priority than candidate2
        when(candidate1.getPriority()).thenReturn(20);
        when(candidate2.getPriority()).thenReturn(10);

        // When: compare candidates
        int result = CandidatePrioritizer.compareCandidates(candidate1, candidate2);

        // Then: result is -1
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_LowerPriority_Returns1() {
        // Given: candidate1 has lower priority than candidate2
        when(candidate1.getPriority()).thenReturn(10);
        when(candidate2.getPriority()).thenReturn(20);

        // When: compare candidates
        int result = candidatePrioritizer.compare(candidate1, candidate2);

        // Then: result is 1
        assertEquals(1, result);
    }

    @Test
    public void testCompare_EqualPriority_Returns0() {
        // Given: candidate1 and candidate2 have equal priority
        when(candidate1.getPriority()).thenReturn(10);
        when(candidate2.getPriority()).thenReturn(10);

        // When: compare candidates
        int result = candidatePrioritizer.compare(candidate1, candidate2);

        // Then: result is 0
        assertEquals(0, result);
    }

    @Test
    public void testCompare_HigherPriority_ReturnsMinus1() {
        // Given: candidate1 has higher priority than candidate2
        when(candidate1.getPriority()).thenReturn(20);
        when(candidate2.getPriority()).thenReturn(10);

        // When: compare candidates
        int result = candidatePrioritizer.compare(candidate1, candidate2);

        // Then: result is -1
        assertEquals(-1, result);
    }

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        // Given: same instance
        CandidatePrioritizer other = candidatePrioritizer;

        // When: check equals
        boolean result = candidatePrioritizer.equals(other);

        // Then: result is true
        assertTrue(result);
    }

    @Test
    public void testEquals_SameClass_ReturnsTrue() {
        // Given: same class
        CandidatePrioritizer other = new CandidatePrioritizer();

        // When: check equals
        boolean result = candidatePrioritizer.equals(other);

        // Then: result is true
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        // Given: different class
        Object other = new Object();

        // When: check equals
        boolean result = candidatePrioritizer.equals(other);

        // Then: result is false
        assertFalse(result);
    }
}