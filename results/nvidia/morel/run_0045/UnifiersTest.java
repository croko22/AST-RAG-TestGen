import net.hydromatic.morel.util.Unifier;
import net.hydromatic.morel.util.Unifier.Action;
import net.hydromatic.morel.util.Unifier.Sequence;
import net.hydromatic.morel.util.Unifier.Term;
import net.hydromatic.morel.util.Unifier.TermTerm;
import net.hydromatic.morel.util.Unifier.Variable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnifiersTest {

    @Mock
    private BiConsumer<Variable, Action> register;

    @Mock
    private Consumer<List<Term>> termConsumer;

    @Mock
    private PrintStream printStream;

    private ByteArrayOutputStream byteArrayOutputStream;

    @BeforeEach
    void setup() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(byteArrayOutputStream);
    }

    @AfterEach
    void tearDown() {
        byteArrayOutputStream.reset();
    }

    @Test
    void testOnAllVariablesMatched() {
        // Given
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable("T1"));
        variables.add(new Variable("T2"));

        // When
        Unifiers.onAllVariablesMatched(variables, register, termConsumer);

        // Then
        verify(register, times(2)).accept(any(Variable.class), any(Action.class));
    }

    @Test
    void testDump() {
        // Given
        List<TermTerm> pairs = new ArrayList<>();
        pairs.add(new TermTerm(new Variable("T1"), new Variable("T2")));

        // When
        Unifiers.dump(printStream, pairs);

        // Then
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("List<Unifier.TermTerm> pairs = new ArrayList<>();"));
        assertTrue(output.contains("pairs.add(new Unifier.TermTerm("));
    }

    @Test
    void testDump_MultiplePairs() {
        // Given
        List<TermTerm> pairs = new ArrayList<>();
        pairs.add(new TermTerm(new Variable("T1"), new Variable("T2")));
        pairs.add(new TermTerm(new Variable("T3"), new Variable("T4")));

        // When
        Unifiers.dump(printStream, pairs);

        // Then
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("List<Unifier.TermTerm> pairs = new ArrayList<>();"));
        assertTrue(output.contains("pairs.add(new Unifier.TermTerm("));
        assertEquals(2, countOccurrences(output, "pairs.add"));
    }

    @Test
    void testDump_NoPairs() {
        // Given
        List<TermTerm> pairs = new ArrayList<>();

        // When
        Unifiers.dump(printStream, pairs);

        // Then
        String output = byteArrayOutputStream.toString();
        assertTrue(output.contains("List<Unifier.TermTerm> pairs = new ArrayList<>();"));
        assertEquals(0, countOccurrences(output, "pairs.add"));
    }

    private int countOccurrences(String str, String substr) {
        int count = 0;
        int index = str.indexOf(substr);
        while (index != -1) {
            count++;
            index = str.indexOf(substr, index + substr.length());
        }
        return count;
    }
}