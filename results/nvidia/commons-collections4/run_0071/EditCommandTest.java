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
public class EditCommandTest {

    @Mock
    private CommandVisitor<String> visitor;

    @Mock
    private EditCommand<String> editCommand;

    @BeforeEach
    void setup() {
        editCommand = new EditCommand<String>("Test Object") {
            @Override
            public void accept(CommandVisitor<String> visitor) {
                visitor.visit(this);
            }
        };
    }

    @Test
    void testAccept() {
        // Given: a mock visitor
        // When: accept is called with the visitor
        editCommand.accept(visitor);
        // Then: the visitor's visit method is called with the edit command
        verify(visitor, times(1)).visit(any(EditCommand.class));
    }

    @Test
    void testAccept_NullVisitor() {
        // Given: a null visitor
        CommandVisitor<String> nullVisitor = null;
        // When / Then: NullPointerException is thrown when accept is called with the null visitor
        assertThrows(NullPointerException.class, () -> editCommand.accept(nullVisitor));
    }
}