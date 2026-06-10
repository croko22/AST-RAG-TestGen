import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.sequence.ReplacementsFinder;
import org.apache.commons.collections4.sequence.ReplacementsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReplacementsFinderTest {

    @Mock
    private ReplacementsHandler<String> handler;

    private ReplacementsFinder<String> replacementsFinder;

    @BeforeEach
    public void setup() {
        replacementsFinder = new ReplacementsFinder<>(handler);
    }

    @Test
    public void testVisitDeleteCommand() {
        // Given
        String object = "object";

        // When
        replacementsFinder.visitDeleteCommand(object);

        // Then
        verify(handler, never()).handleReplacement(anyInt(), any(List.class), any(List.class));
    }

    @Test
    public void testVisitInsertCommand() {
        // Given
        String object = "object";

        // When
        replacementsFinder.visitInsertCommand(object);

        // Then
        verify(handler, never()).handleReplacement(anyInt(), any(List.class), any(List.class));
    }

    @Test
    public void testVisitKeepCommand_NoPendingOperations() {
        // Given
        String object = "object";

        // When
        replacementsFinder.visitKeepCommand(object);

        // Then
        verify(handler, never()).handleReplacement(anyInt(), any(List.class), any(List.class));
        assertEquals(1, replacementsFinder.skipped);
    }

    @Test
    public void testVisitKeepCommand_WithPendingDeletions() {
        // Given
        String object = "object";
        replacementsFinder.visitDeleteCommand("delete");

        // When
        replacementsFinder.visitKeepCommand(object);

        // Then
        verify(handler).handleReplacement(0, argThat(list -> list.size() == 1), argThat(list -> list.isEmpty()));
        assertEquals(1, replacementsFinder.skipped);
    }

    @Test
    public void testVisitKeepCommand_WithPendingInsertions() {
        // Given
        String object = "object";
        replacementsFinder.visitInsertCommand("insert");

        // When
        replacementsFinder.visitKeepCommand(object);

        // Then
        verify(handler).handleReplacement(0, argThat(list -> list.isEmpty()), argThat(list -> list.size() == 1));
        assertEquals(1, replacementsFinder.skipped);
    }

    @Test
    public void testVisitKeepCommand_WithPendingDeletionsAndInsertions() {
        // Given
        String object = "object";
        replacementsFinder.visitDeleteCommand("delete");
        replacementsFinder.visitInsertCommand("insert");

        // When
        replacementsFinder.visitKeepCommand(object);

        // Then
        verify(handler).handleReplacement(0, argThat(list -> list.size() == 1), argThat(list -> list.size() == 1));
        assertEquals(1, replacementsFinder.skipped);
    }

    @Test
    public void testVisitKeepCommand_MultipleTimes() {
        // Given
        String object = "object";
        replacementsFinder.visitDeleteCommand("delete");
        replacementsFinder.visitInsertCommand("insert");

        // When
        replacementsFinder.visitKeepCommand(object);
        replacementsFinder.visitKeepCommand(object);

        // Then
        verify(handler, times(2)).handleReplacement(anyInt(), any(List.class), any(List.class));
        assertEquals(1, replacementsFinder.skipped);
    }
}