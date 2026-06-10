import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.sequence.EditCommand;
import org.apache.commons.collections4.sequence.KeepCommand;
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
public class KeepCommandTest {

    @Mock
    private CommandVisitor<String> visitor;

    private KeepCommand<String> keepCommand;

    @BeforeEach
    void setup() {
        String object = "Test Object";
        keepCommand = new KeepCommand<>(object);
    }

    @Test
    void testAcceptVisitor() {
        // When: accept a visitor
        assertDoesNotThrow(() -> keepCommand.accept(visitor));

        // Then: verify the visitor's visitKeepCommand method is called
        verify(visitor, times(1)).visitKeepCommand(any());
    }

    @Test
    void testAcceptNullVisitor() {
        // Given: a null visitor
        CommandVisitor<String> nullVisitor = null;

        // When / Then: expect a NullPointerException
        assertThrows(NullPointerException.class, () -> keepCommand.accept(nullVisitor));
    }
}