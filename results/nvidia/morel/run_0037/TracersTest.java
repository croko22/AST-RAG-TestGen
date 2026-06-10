import net.hydromatic.morel.util.Tracers;
import net.hydromatic.morel.util.Unifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TracersTest {

    @Mock
    private Unifier.Term term;

    @Mock
    private Unifier.Sequence sequence;

    @Mock
    private Unifier.Variable variable;

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setup() {
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void testNullTracer() {
        // Given
        Tracers.ConfigurableTracer tracer = Tracers.nullTracer();

        // When
        tracer.onDelete(term, term);
        tracer.onConflict(sequence, sequence);
        tracer.onSequence(sequence, sequence);
        tracer.onSwap(term, term);
        tracer.onCycle(variable, term);
        tracer.onVariable(variable, term);
        tracer.onSubstitute(term, term, term, term);

        // Then
        verify(term, never()).toString();
        verify(sequence, never()).toString();
        verify(variable, never()).toString();
    }

    @Test
    void testPrintTracer() {
        // Given
        PrintWriter writer = new PrintWriter(outputStream);
        Tracers.ConfigurableTracer tracer = Tracers.printTracer(writer);

        // When
        tracer.onDelete(term, term);
        tracer.onConflict(sequence, sequence);
        tracer.onSequence(sequence, sequence);
        tracer.onSwap(term, term);
        tracer.onCycle(variable, term);
        tracer.onVariable(variable, term);
        tracer.onSubstitute(term, term, term, term);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("delete"));
        assertTrue(output.contains("conflict"));
        assertTrue(output.contains("sequence"));
        assertTrue(output.contains("swap"));
        assertTrue(output.contains("cycle"));
        assertTrue(output.contains("variable"));
        assertTrue(output.contains("substitute"));
    }

    @Test
    void testPrintTracerOutputStream() {
        // Given
        Tracers.ConfigurableTracer tracer = Tracers.printTracer(outputStream);

        // When
        tracer.onDelete(term, term);
        tracer.onConflict(sequence, sequence);
        tracer.onSequence(sequence, sequence);
        tracer.onSwap(term, term);
        tracer.onCycle(variable, term);
        tracer.onVariable(variable, term);
        tracer.onSubstitute(term, term, term, term);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("delete"));
        assertTrue(output.contains("conflict"));
        assertTrue(output.contains("sequence"));
        assertTrue(output.contains("swap"));
        assertTrue(output.contains("cycle"));
        assertTrue(output.contains("variable"));
        assertTrue(output.contains("substitute"));
    }

    @Test
    void testWithDeleteHandler() {
        // Given
        BiConsumer<Unifier.Term, Unifier.Term> deleteHandler = mock(BiConsumer.class);
        Tracers.ConfigurableTracer tracer = Tracers.nullTracer().withDeleteHandler(deleteHandler);

        // When
        tracer.onDelete(term, term);

        // Then
        verify(deleteHandler).accept(term, term);
    }

    @Test
    void testWithConflictHandler() {
        // Given
        BiConsumer<Unifier.Sequence, Unifier.Sequence> conflictHandler = mock(BiConsumer.class);
        Tracers.ConfigurableTracer tracer = Tracers.nullTracer().withConflictHandler(conflictHandler);

        // When
        tracer.onConflict(sequence, sequence);

        // Then
        verify(conflictHandler).accept(sequence, sequence);
    }

    @Test
    void testWithSequenceHandler() {
        // Given
        BiConsumer<Unifier.Sequence, Unifier.Sequence> sequenceHandler = mock(BiConsumer.class);
        Tracers.ConfigurableTracer tracer = Tracers.nullTracer().withSequenceHandler(sequenceHandler);

        // When
        tracer.onSequence(sequence, sequence);

        // Then
        verify(sequenceHandler).accept(sequence, sequence);
    }

    @Test
    void testWithSwapHandler() {
        // Given
        BiConsumer<Unifier.Term, Unifier.Term> swapHandler = mock(BiConsumer.class);
        Tracers.ConfigurableTracer tracer = Tracers.nullTracer().withSwapHandler(swapHandler);

        // When
        tracer.onSwap(term, term);

        // Then
        verify(swapHandler).accept(term, term);
    }

    @Test
    void testWithCycleHandler() {
        // Given
        BiConsumer<Unifier.Variable, Unifier.Term> cycleHandler = mock(BiConsumer.class);
        Tracers.ConfigurableTracer tracer = Tracers.nullTracer().withCycleHandler(cycleHandler);

        // When
        tracer.onCycle(variable, term);

        // Then
        verify(cycleHandler).accept(variable, term);
    }

    @Test
    void testWithVariableHandler() {
        // Given
        BiConsumer<Unifier.Variable, Unifier.Term> variableHandler = mock(BiConsumer.class);
        Tracers.ConfigurableTracer tracer = Tracers.nullTracer().withVariableHandler(variableHandler);

        // When
        tracer.onVariable(variable, term);

        // Then
        verify(variableHandler).accept(variable, term);
    }

    @Test
    void testWithSubstituteHandler() {
        // Given
        Tracers.QuadConsumer<Unifier.Term, Unifier.Term, Unifier.Term, Unifier.Term> substituteHandler = mock(Tracers.QuadConsumer.class);
        Tracers.ConfigurableTracer tracer = Tracers.nullTracer().withSubstituteHandler(substituteHandler);

        // When
        tracer.onSubstitute(term, term, term, term);

        // Then
        verify(substituteHandler).accept(term, term, term, term);
    }
}