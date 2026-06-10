import net.hydromatic.morel.util.Sat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SatTest {

    @InjectMocks
    private Sat sat;

    private Sat.Term term1;
    private Sat.Term term2;
    private Sat.Term term3;
    private Sat.Variable variable1;
    private Sat.Variable variable2;
    private Sat.Variable variable3;

    @BeforeEach
    public void setup() {
        variable1 = sat.variable("A");
        variable2 = sat.variable("B");
        variable3 = sat.variable("C");
        term1 = variable1;
        term2 = variable2;
        term3 = variable3;
    }

    @Test
    public void testVariable() {
        // Given
        String name = "TestVariable";

        // When
        Sat.Variable variable = sat.variable(name);

        // Then
        assertNotNull(variable);
        assertEquals(name, variable.name);
    }

    @Test
    public void testNot() {
        // Given
        Sat.Term term = term1;

        // When
        Sat.Term notTerm = sat.not(term);

        // Then
        assertNotNull(notTerm);
        assertNotEquals(term, notTerm);
    }

    @Test
    public void testAnd_NoTerms() {
        // Given

        // When
        Sat.Term andTerm = sat.and();

        // Then
        assertNotNull(andTerm);
        assertTrue(andTerm.evaluate(new boolean[0]));
    }

    @Test
    public void testAnd_SingleTerm() {
        // Given
        Sat.Term term = term1;

        // When
        Sat.Term andTerm = sat.and(term);

        // Then
        assertNotNull(andTerm);
        assertEquals(term, andTerm);
    }

    @Test
    public void testAnd_MultipleTerms() {
        // Given
        Sat.Term term1 = this.term1;
        Sat.Term term2 = this.term2;

        // When
        Sat.Term andTerm = sat.and(term1, term2);

        // Then
        assertNotNull(andTerm);
        assertNotEquals(term1, andTerm);
        assertNotEquals(term2, andTerm);
    }

    @Test
    public void testOr_NoTerms() {
        // Given

        // When
        Sat.Term orTerm = sat.or();

        // Then
        assertNotNull(orTerm);
        assertFalse(orTerm.evaluate(new boolean[0]));
    }

    @Test
    public void testOr_SingleTerm() {
        // Given
        Sat.Term term = term1;

        // When
        Sat.Term orTerm = sat.or(term);

        // Then
        assertNotNull(orTerm);
        assertEquals(term, orTerm);
    }

    @Test
    public void testOr_MultipleTerms() {
        // Given
        Sat.Term term1 = this.term1;
        Sat.Term term2 = this.term2;

        // When
        Sat.Term orTerm = sat.or(term1, term2);

        // Then
        assertNotNull(orTerm);
        assertNotEquals(term1, orTerm);
        assertNotEquals(term2, orTerm);
    }

    @Test
    public void testSolve_NoSolution() {
        // Given
        Sat.Term term = sat.not(term1);

        // When
        Map<Sat.Variable, Boolean> solution = sat.solve(term);

        // Then
        assertNull(solution);
    }

    @Test
    public void testSolve_SolutionFound() {
        // Given
        Sat.Term term = term1;

        // When
        Map<Sat.Variable, Boolean> solution = sat.solve(term);

        // Then
        assertNotNull(solution);
        assertEquals(1, solution.size());
        assertTrue(solution.get(term1));
    }

    @Test
    public void testSolve_MultipleVariables() {
        // Given
        Sat.Term term = sat.and(term1, term2);

        // When
        Map<Sat.Variable, Boolean> solution = sat.solve(term);

        // Then
        assertNotNull(solution);
        assertEquals(2, solution.size());
        assertTrue(solution.get(term1));
        assertTrue(solution.get(term2));
    }
}