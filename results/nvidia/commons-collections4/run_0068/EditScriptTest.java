import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.sequence.DeleteCommand;
import org.apache.commons.collections4.sequence.KeepCommand;
import org.apache.commons.collections4.sequence.InsertCommand;
import org.apache.commons.collections4.sequence.EditCommand;
import org.apache.commons.collections4.sequence.EditScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditScriptTest {

    @Mock
    private CommandVisitor<String> commandVisitor;

    @Mock
    private DeleteCommand<String> deleteCommand;

    @Mock
    private InsertCommand<String> insertCommand;

    @Mock
    private KeepCommand<String> keepCommand;

    private EditScript<String> editScript;

    @BeforeEach
    public void setup() {
        editScript = new EditScript<>();
    }

    @Test
    public void testAppendDeleteCommand() {
        // When: append a delete command to the script
        editScript.append(deleteCommand);

        // Then: verify the command was added and modifications incremented
        assertEquals(1, editScript.getModifications());
        assertEquals(0, editScript.getLCSLength());
    }

    @Test
    public void testAppendInsertCommand() {
        // When: append an insert command to the script
        editScript.append(insertCommand);

        // Then: verify the command was added and modifications incremented
        assertEquals(1, editScript.getModifications());
        assertEquals(0, editScript.getLCSLength());
    }

    @Test
    public void testAppendKeepCommand() {
        // When: append a keep command to the script
        editScript.append(keepCommand);

        // Then: verify the command was added and LCS length incremented
        assertEquals(0, editScript.getModifications());
        assertEquals(1, editScript.getLCSLength());
    }

    @Test
    public void testGetLCSLength() {
        // Given: append multiple keep commands to the script
        editScript.append(keepCommand);
        editScript.append(keepCommand);

        // When: get the LCS length
        int lcsLength = editScript.getLCSLength();

        // Then: verify the LCS length is correct
        assertEquals(2, lcsLength);
    }

    @Test
    public void testGetModifications() {
        // Given: append multiple delete and insert commands to the script
        editScript.append(deleteCommand);
        editScript.append(insertCommand);
        editScript.append(deleteCommand);

        // When: get the number of modifications
        int modifications = editScript.getModifications();

        // Then: verify the number of modifications is correct
        assertEquals(3, modifications);
    }

    @Test
    public void testVisit() {
        // Given: append multiple commands to the script
        editScript.append(deleteCommand);
        editScript.append(insertCommand);
        editScript.append(keepCommand);

        // When: visit the script with a command visitor
        editScript.visit(commandVisitor);

        // Then: verify the visitor was called for each command
        verify(deleteCommand, times(1)).accept(any(CommandVisitor.class));
        verify(insertCommand, times(1)).accept(any(CommandVisitor.class));
        verify(keepCommand, times(1)).accept(any(CommandVisitor.class));
    }

    @Test
    public void testVisit_MultipleCommands() {
        // Given: append multiple commands to the script
        editScript.append(deleteCommand);
        editScript.append(insertCommand);
        editScript.append(keepCommand);
        editScript.append(deleteCommand);
        editScript.append(insertCommand);
        editScript.append(keepCommand);

        // When: visit the script with a command visitor
        editScript.visit(commandVisitor);

        // Then: verify the visitor was called for each command
        verify(deleteCommand, times(2)).accept(any(CommandVisitor.class));
        verify(insertCommand, times(2)).accept(any(CommandVisitor.class));
        verify(keepCommand, times(2)).accept(any(CommandVisitor.class));
    }
}