import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SocketClosedExceptionTest {

    @Test
    public void testSocketClosedException_DefaultConstructor() {
        // Given: no parameters
        // When: create a new SocketClosedException
        SocketClosedException exception = new SocketClosedException();
        
        // Then: verify the exception message
        assertEquals("Socket closed", exception.getMessage());
    }

    @Test
    public void testSocketClosedException_SuperClass() {
        // Given: a new SocketClosedException
        SocketClosedException exception = new SocketClosedException();
        
        // Then: verify the exception is an instance of SocketException
        assertTrue(exception instanceof SocketException);
    }

    @Test
    public void testSocketClosedException_GetMessage() {
        // Given: a new SocketClosedException
        SocketClosedException exception = new SocketClosedException();
        
        // When: get the exception message
        String message = exception.getMessage();
        
        // Then: verify the exception message
        assertEquals("Socket closed", message);
    }
}