import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.sequence.DeleteCommand;
import org.apache.commons.collections4.sequence.EditCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCommandTest {

    @Mock
    private CommandVisitor<String> visitor;

    private DeleteCommand<String> deleteCommand;

    @BeforeEach
    void setup() {
        String object = "testObject";
        deleteCommand = new DeleteCommand<>(object);
    }

    @Test
    void testAcceptVisitor() {
        // When: accept visitor
        deleteCommand.accept(visitor);

        // Then: verify visitor method call
        verify(visitor, times(1)).visitDeleteCommand(any());
    }

    @Test
    void testAcceptVisitorNull() {
        // Given: null visitor
        CommandVisitor<String> nullVisitor = null;

        // When / Then: expect NullPointerException
        assertThrows(NullPointerException.class, () -> deleteCommand.accept(nullVisitor));
    }

    @Test
    void testDeleteCommandConstructor() {
        // Given: object to delete
        String object = "testObject";

        // When: create DeleteCommand instance
        DeleteCommand<String> command = new DeleteCommand<>(object);

        // Then: verify object is set
        assertEquals(object, command.getObject());
    }

    @Test
    void testDeleteCommandConstructorNull() {
        // Given: null object
        String nullObject = null;

        // When / Then: expect NullPointerException
        assertThrows(NullPointerException.class, () -> new DeleteCommand<>(nullObject));
    }
}