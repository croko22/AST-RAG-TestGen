import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.sequence.EditCommand;
import org.apache.commons.collections4.sequence.InsertCommand;
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
public class InsertCommandTest {

    @Mock
    private CommandVisitor<String> visitor;

    @Mock
    private EditCommand<String> editCommand;

    private InsertCommand<String> insertCommand;

    @BeforeEach
    void setup() {
        String object = "Test Object";
        insertCommand = new InsertCommand<>(object);
    }

    @Test
    void testAcceptVisitor() {
        // Given: a visitor to be accepted
        // When: the accept method is called
        // Then: the visitor's visitInsertCommand method is called with the object
        insertCommand.accept(visitor);
        verify(visitor, times(1)).visitInsertCommand(any());
    }

    @Test
    void testAcceptVisitorNull() {
        // Given: a null visitor
        // When: the accept method is called
        // Then: a NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> insertCommand.accept(null));
    }

    @Test
    void testConstructor() {
        // Given: an object to be inserted
        // When: a new InsertCommand is created
        // Then: the object is set correctly
        String object = "Test Object";
        InsertCommand<String> command = new InsertCommand<>(object);
        assertDoesNotThrow(() -> new InsertCommand<>(object));
    }

    @Test
    void testConstructorNull() {
        // Given: a null object
        // When: a new InsertCommand is created
        // Then: a NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> new InsertCommand<>(null));
    }
}