import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.EditCommand;
import org.apache.commons.collections4.sequence.KeepCommand;
import org.apache.commons.collections4.sequence.InsertCommand;
import org.apache.commons.collections4.sequence.DeleteCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SequencesComparatorTest {

    @Mock
    private Equator<String> equator;

    @Test
    public void testGetScript_WhenSequencesAreEqual() {
        // Given
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("A");
        sequence1.add("B");
        sequence1.add("C");

        List<String> sequence2 = new ArrayList<>();
        sequence2.add("A");
        sequence2.add("B");
        sequence2.add("C");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        // When
        EditScript<String> script = comparator.getScript();

        // Then
        assertEquals(3, script.size());
        for (int i = 0; i < 3; i++) {
            assertTrue(script.get(i) instanceof KeepCommand);
            assertEquals(sequence1.get(i), ((KeepCommand<String>) script.get(i)).getObject());
        }
    }

    @Test
    public void testGetScript_WhenSequencesAreDifferent() {
        // Given
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("A");
        sequence1.add("B");
        sequence1.add("C");

        List<String> sequence2 = new ArrayList<>();
        sequence2.add("A");
        sequence2.add("D");
        sequence2.add("C");

        when(equator.equate(any(), any())).thenAnswer(invocation -> {
            String o1 = invocation.getArgument(0);
            String o2 = invocation.getArgument(1);
            return o1.equals(o2);
        });

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        // When
        EditScript<String> script = comparator.getScript();

        // Then
        assertEquals(4, script.size());
        assertTrue(script.get(0) instanceof KeepCommand);
        assertEquals("A", ((KeepCommand<String>) script.get(0)).getObject());
        assertTrue(script.get(1) instanceof DeleteCommand);
        assertEquals("B", ((DeleteCommand<String>) script.get(1)).getObject());
        assertTrue(script.get(2) instanceof InsertCommand);
        assertEquals("D", ((InsertCommand<String>) script.get(2)).getObject());
        assertTrue(script.get(3) instanceof KeepCommand);
        assertEquals("C", ((KeepCommand<String>) script.get(3)).getObject());
    }

    @Test
    public void testGetScript_WhenFirstSequenceIsEmpty() {
        // Given
        List<String> sequence1 = new ArrayList<>();

        List<String> sequence2 = new ArrayList<>();
        sequence2.add("A");
        sequence2.add("B");
        sequence2.add("C");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        // When
        EditScript<String> script = comparator.getScript();

        // Then
        assertEquals(3, script.size());
        for (int i = 0; i < 3; i++) {
            assertTrue(script.get(i) instanceof InsertCommand);
            assertEquals(sequence2.get(i), ((InsertCommand<String>) script.get(i)).getObject());
        }
    }

    @Test
    public void testGetScript_WhenSecondSequenceIsEmpty() {
        // Given
        List<String> sequence1 = new ArrayList<>();
        sequence1.add("A");
        sequence1.add("B");
        sequence1.add("C");

        List<String> sequence2 = new ArrayList<>();

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        // When
        EditScript<String> script = comparator.getScript();

        // Then
        assertEquals(3, script.size());
        for (int i = 0; i < 3; i++) {
            assertTrue(script.get(i) instanceof DeleteCommand);
            assertEquals(sequence1.get(i), ((DeleteCommand<String>) script.get(i)).getObject());
        }
    }
}