import org.apache.commons.collections4.sequence.CommandVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandVisitorTest {

    @Mock
    private CommandVisitor<String> commandVisitorMock;

    @Mock
    private CommandVisitor<Integer> commandVisitorMockInteger;

    @BeforeEach
    public void setup() {
        // No setup needed for this test class
    }

    @Test
    public void testVisitDeleteCommand() {
        // Given
        String objectToDelete = "objectToDelete";

        // When
        commandVisitorMock.visitDeleteCommand(objectToDelete);

        // Then
        verify(commandVisitorMock, times(1)).visitDeleteCommand(objectToDelete);
    }

    @Test
    public void testVisitInsertCommand() {
        // Given
        String objectToInsert = "objectToInsert";

        // When
        commandVisitorMock.visitInsertCommand(objectToInsert);

        // Then
        verify(commandVisitorMock, times(1)).visitInsertCommand(objectToInsert);
    }

    @Test
    public void testVisitKeepCommand() {
        // Given
        String objectToKeep = "objectToKeep";

        // When
        commandVisitorMock.visitKeepCommand(objectToKeep);

        // Then
        verify(commandVisitorMock, times(1)).visitKeepCommand(objectToKeep);
    }

    @Test
    public void testVisitDeleteCommandNull() {
        // Given
        String objectToDelete = null;

        // When
        assertDoesNotThrow(() -> commandVisitorMock.visitDeleteCommand(objectToDelete));

        // Then
        verify(commandVisitorMock, times(1)).visitDeleteCommand(objectToDelete);
    }

    @Test
    public void testVisitInsertCommandNull() {
        // Given
        String objectToInsert = null;

        // When
        assertDoesNotThrow(() -> commandVisitorMock.visitInsertCommand(objectToInsert));

        // Then
        verify(commandVisitorMock, times(1)).visitInsertCommand(objectToInsert);
    }

    @Test
    public void testVisitKeepCommandNull() {
        // Given
        String objectToKeep = null;

        // When
        assertDoesNotThrow(() -> commandVisitorMock.visitKeepCommand(objectToKeep));

        // Then
        verify(commandVisitorMock, times(1)).visitKeepCommand(objectToKeep);
    }

    @Test
    public void testVisitDeleteCommandDifferentType() {
        // Given
        Integer objectToDelete = 1;

        // When
        commandVisitorMockInteger.visitDeleteCommand(objectToDelete);

        // Then
        verify(commandVisitorMockInteger, times(1)).visitDeleteCommand(objectToDelete);
    }

    @Test
    public void testVisitInsertCommandDifferentType() {
        // Given
        Integer objectToInsert = 1;

        // When
        commandVisitorMockInteger.visitInsertCommand(objectToInsert);

        // Then
        verify(commandVisitorMockInteger, times(1)).visitInsertCommand(objectToInsert);
    }

    @Test
    public void testVisitKeepCommandDifferentType() {
        // Given
        Integer objectToKeep = 1;

        // When
        commandVisitorMockInteger.visitKeepCommand(objectToKeep);

        // Then
        verify(commandVisitorMockInteger, times(1)).visitKeepCommand(objectToKeep);
    }
}