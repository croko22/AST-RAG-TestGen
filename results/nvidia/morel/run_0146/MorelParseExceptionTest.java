import net.hydromatic.morel.ast.Pos;
import net.hydromatic.morel.parse.MorelParseException;
import net.hydromatic.morel.util.MorelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MorelParseExceptionTest {

    @Mock
    private Exception cause;

    @Mock
    private Pos pos;

    private MorelParseException morelParseException;

    @BeforeEach
    void setup() {
        morelParseException = new MorelParseException(cause, pos);
    }

    @Test
    void testConstructor() {
        // Given: cause and pos are not null
        // When: creating a new MorelParseException
        // Then: the exception is created with the given cause and pos
        MorelParseException exception = new MorelParseException(cause, pos);
        assertEquals(cause.getMessage(), exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(pos, exception.pos());
    }

    @Test
    void testPos() {
        // Given: a MorelParseException with a pos
        // When: calling the pos method
        // Then: the pos is returned
        Pos result = morelParseException.pos();
        assertEquals(pos, result);
    }

    @Test
    void testDescribeTo() {
        // Given: a MorelParseException with a cause and a pos
        // When: calling the describeTo method
        // Then: the description is appended to the buffer
        StringBuilder buf = new StringBuilder();
        StringBuilder result = morelParseException.describeTo(buf);
        assertEquals(buf, result);
        assertTrue(result.toString().contains(cause.getMessage()));
    }

    @Test
    void testDescribeToNullBuffer() {
        // Given: a MorelParseException with a cause and a pos
        // When: calling the describeTo method with a null buffer
        // Then: a NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> morelParseException.describeTo(null));
    }

    @Test
    void testEquals() {
        // Given: two MorelParseException with the same cause and pos
        MorelParseException other = new MorelParseException(cause, pos);
        // When: calling the equals method
        // Then: the exceptions are considered equal
        assertTrue(morelParseException.equals(other));
    }

    @Test
    void testEqualsDifferentCause() {
        // Given: two MorelParseException with different causes
        Exception otherCause = mock(Exception.class);
        MorelParseException other = new MorelParseException(otherCause, pos);
        // When: calling the equals method
        // Then: the exceptions are not considered equal
        assertFalse(morelParseException.equals(other));
    }

    @Test
    void testEqualsDifferentPos() {
        // Given: two MorelParseException with different pos
        Pos otherPos = mock(Pos.class);
        MorelParseException other = new MorelParseException(cause, otherPos);
        // When: calling the equals method
        // Then: the exceptions are not considered equal
        assertFalse(morelParseException.equals(other));
    }

    @Test
    void testHashCode() {
        // Given: a MorelParseException with a cause and a pos
        // When: calling the hashCode method
        // Then: the hash code is returned
        int result = morelParseException.hashCode();
        assertNotNull(result);
    }

    @Test
    void testToString() {
        // Given: a MorelParseException with a cause and a pos
        // When: calling the toString method
        // Then: the string representation is returned
        String result = morelParseException.toString();
        assertNotNull(result);
        assertTrue(result.contains(cause.getMessage()));
    }
}