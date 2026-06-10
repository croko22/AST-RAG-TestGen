import org.jsoup.SerializationException;
import org.jsoup.internal.QuietAppendable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.StringWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuietAppendableTest {

    @Mock
    private Appendable appendable;

    @Mock
    private StringWriter stringWriter;

    @InjectMocks
    private QuietAppendable quietAppendable;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        quietAppendable = QuietAppendable.wrap(stringWriter);
    }

    @AfterEach
    public void tearDown() {
        // Reset mocks
        reset(appendable, stringWriter);
    }

    @Test
    public void testAppend_CharSequence() {
        // Given: a CharSequence to append
        CharSequence charSequence = "Hello, World!";
        
        // When: append the CharSequence
        QuietAppendable result = quietAppendable.append(charSequence);
        
        // Then: verify the result and interactions
        assertNotNull(result);
        verify(stringWriter, times(1)).append(charSequence);
    }

    @Test
    public void testAppend_Char() {
        // Given: a character to append
        char character = 'A';
        
        // When: append the character
        QuietAppendable result = quietAppendable.append(character);
        
        // Then: verify the result and interactions
        assertNotNull(result);
        verify(stringWriter, times(1)).append(character);
    }

    @Test
    public void testAppend_CharArray() {
        // Given: a character array to append
        char[] charArray = "Hello, World!".toCharArray();
        int offset = 0;
        int length = charArray.length;
        
        // When: append the character array
        QuietAppendable result = quietAppendable.append(charArray, offset, length);
        
        // Then: verify the result and interactions
        assertNotNull(result);
        verify(stringWriter, times(1)).append(new String(charArray, offset, length));
    }

    @Test
    public void testWrap_Appendable() {
        // Given: an Appendable to wrap
        Appendable appendableToWrap = new StringWriter();
        
        // When: wrap the Appendable
        QuietAppendable wrappedAppendable = QuietAppendable.wrap(appendableToWrap);
        
        // Then: verify the result
        assertNotNull(wrappedAppendable);
    }

    @Test
    public void testWrap_StringBuilder() {
        // Given: a StringBuilder to wrap
        StringBuilder stringBuilder = new StringBuilder();
        
        // When: wrap the StringBuilder
        QuietAppendable wrappedStringBuilder = QuietAppendable.wrap(stringBuilder);
        
        // Then: verify the result
        assertNotNull(wrappedStringBuilder);
    }

    @Test
    public void testAppend_IOException() {
        // Given: an Appendable that throws an IOException
        doThrow(new IOException()).when(stringWriter).append(any());
        
        // When / Then: append a CharSequence and expect a SerializationException
        assertThrows(SerializationException.class, () -> quietAppendable.append("Hello, World!"));
    }
}