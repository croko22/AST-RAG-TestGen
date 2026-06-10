import net.hydromatic.morel.util.RobinsonUnifier;
import net.hydromatic.morel.util.Result;
import net.hydromatic.morel.util.Substitution;
import net.hydromatic.morel.util.SubstitutionResult;
import net.hydromatic.morel.util.Term;
import net.hydromatic.morel.util.TermTerm;
import net.hydromatic.morel.util.Variable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RobinsonUnifierTest {

    @Mock
    private Term mockTerm;

    @Mock
    private Variable mockVariable;

    @Mock
    private Sequence mockSequence;

    private RobinsonUnifier unifier;

    @BeforeEach
    void setup() {
        unifier = new RobinsonUnifier();
    }

    @Test
    void testUnify_TermPairs_SizeOne() {
        // Given
        TermTerm termPair = new TermTerm(mockTerm, mockTerm);
        List<TermTerm> termPairs = new ArrayList<>();
        termPairs.add(termPair);
        Map<Variable, Action> termActions = new HashMap<>();
        List<Constraint> constraints = new ArrayList<>();
        Tracer tracer = mock(Tracer.class);

        // When
        Result result = unifier.unify(termPairs, termActions, constraints, tracer);

        // Then
        assertNotNull(result);
    }

    @Test
    void testUnify_TermPairs_SizeNotOne() {
        // Given
        TermTerm termPair1 = new TermTerm(mockTerm, mockTerm);
        TermTerm termPair2 = new TermTerm(mockTerm, mockTerm);
        List<TermTerm> termPairs = new ArrayList<>();
        termPairs.add(termPair1);
        termPairs.add(termPair2);
        Map<Variable, Action> termActions = new HashMap<>();
        List<Constraint> constraints = new ArrayList<>();
        Tracer tracer = mock(Tracer.class);

        // When and Then
        assertThrows(AssertionError.class, () -> unifier.unify(termPairs, termActions, constraints, tracer));
    }

    @Test
    void testUnify_TermActions_NotEmpty() {
        // Given
        TermTerm termPair = new TermTerm(mockTerm, mockTerm);
        List<TermTerm> termPairs = new ArrayList<>();
        termPairs.add(termPair);
        Map<Variable, Action> termActions = new HashMap<>();
        termActions.put(mockVariable, mock(Action.class));
        List<Constraint> constraints = new ArrayList<>();
        Tracer tracer = mock(Tracer.class);

        // When and Then
        assertThrows(AssertionError.class, () -> unifier.unify(termPairs, termActions, constraints, tracer));
    }

    @Test
    void testUnify_Constraints_NotEmpty() {
        // Given
        TermTerm termPair = new TermTerm(mockTerm, mockTerm);
        List<TermTerm> termPairs = new ArrayList<>();
        termPairs.add(termPair);
        Map<Variable, Action> termActions = new HashMap<>();
        List<Constraint> constraints = new ArrayList<>();
        constraints.add(mock(Constraint.class));
        Tracer tracer = mock(Tracer.class);

        // When and Then
        assertThrows(AssertionError.class, () -> unifier.unify(termPairs, termActions, constraints, tracer));
    }

    @Test
    void testUnify_LhsVariable() {
        // Given
        Variable variable = mock(Variable.class);
        Term term = mockTerm;

        // When
        Result result = unifier.unify(variable, term);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof SubstitutionResult);
        SubstitutionResult substitutionResult = (SubstitutionResult) result;
        assertEquals(variable, substitutionResult.getVariable());
        assertEquals(term, substitutionResult.getTerm());
    }

    @Test
    void testUnify_RhsVariable() {
        // Given
        Term term = mockTerm;
        Variable variable = mock(Variable.class);

        // When
        Result result = unifier.unify(term, variable);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof SubstitutionResult);
        SubstitutionResult substitutionResult = (SubstitutionResult) result;
        assertEquals(variable, substitutionResult.getVariable());
        assertEquals(term, substitutionResult.getTerm());
    }

    @Test
    void testUnify_LhsSequence_RhsSequence() {
        // Given
        Sequence lhsSequence = mockSequence;
        Sequence rhsSequence = mockSequence;

        // When
        Result result = unifier.unify(lhsSequence, rhsSequence);

        // Then
        assertNotNull(result);
    }

    @Test
    void testUnify_LhsNotSequence_RhsNotSequence() {
        // Given
        Term lhsTerm = mockTerm;
        Term rhsTerm = mockTerm;

        // When
        Result result = unifier.unify(lhsTerm, rhsTerm);

        // Then
        assertNotNull(result);
    }

    @Test
    void testSequenceUnify_SequencesHaveDifferentLength() {
        // Given
        Sequence lhsSequence = mockSequence;
        Sequence rhsSequence = mockSequence;
        when(lhsSequence.terms.size()).thenReturn(1);
        when(rhsSequence.terms.size()).thenReturn(2);

        // When
        Result result = unifier.sequenceUnify(lhsSequence, rhsSequence);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Result);
    }

    @Test
    void testSequenceUnify_SequencesHaveDifferentOperator() {
        // Given
        Sequence lhsSequence = mockSequence;
        Sequence rhsSequence = mockSequence;
        when(lhsSequence.terms.size()).thenReturn(1);
        when(rhsSequence.terms.size()).thenReturn(1);
        when(lhsSequence.operator).thenReturn("operator1");
        when(rhsSequence.operator).thenReturn("operator2");

        // When
        Result result = unifier.sequenceUnify(lhsSequence, rhsSequence);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Result);
    }

    @Test
    void testSequenceUnify_SequencesEmpty() {
        // Given
        Sequence lhsSequence = mockSequence;
        Sequence rhsSequence = mockSequence;
        when(lhsSequence.terms.size()).thenReturn(0);
        when(rhsSequence.terms.size()).thenReturn(0);

        // When
        Result result = unifier.sequenceUnify(lhsSequence, rhsSequence);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof SubstitutionResult);
    }
}