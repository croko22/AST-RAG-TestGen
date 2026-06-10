import org.ice4j.attribute.ErrorCodeAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorCodeAttributeTest {

    private ErrorCodeAttribute errorCodeAttribute;

    @BeforeEach
    public void setup() {
        errorCodeAttribute = new ErrorCodeAttribute();
    }

    @Test
    public void testSetErrorCode() {
        // Given
        char errorCode = 400;

        // When
        errorCodeAttribute.setErrorCode(errorCode);

        // Then
        assertEquals(errorCode, errorCodeAttribute.getErrorCode());
    }

    @Test
    public void testGetErrorCode() {
        // Given
        char errorCode = 400;
        errorCodeAttribute.setErrorCode(errorCode);

        // When
        char result = errorCodeAttribute.getErrorCode();

        // Then
        assertEquals(errorCode, result);
    }

    @Test
    public void testSetErrorNumber() {
        // Given
        byte errorNumber = 1;

        // When
        errorCodeAttribute.setErrorNumber(errorNumber);

        // Then
        assertEquals(errorNumber, errorCodeAttribute.getErrorNumber());
    }

    @Test
    public void testGetErrorNumber() {
        // Given
        byte errorNumber = 1;
        errorCodeAttribute.setErrorNumber(errorNumber);

        // When
        byte result = errorCodeAttribute.getErrorNumber();

        // Then
        assertEquals(errorNumber, result);
    }

    @Test
    public void testSetErrorClass() {
        // Given
        byte errorClass = 4;

        // When
        errorCodeAttribute.setErrorClass(errorClass);

        // Then
        assertEquals(errorClass, errorCodeAttribute.getErrorClass());
    }

    @Test
    public void testGetErrorClass() {
        // Given
        byte errorClass = 4;
        errorCodeAttribute.setErrorClass(errorClass);

        // When
        byte result = errorCodeAttribute.getErrorClass();

        // Then
        assertEquals(errorClass, result);
    }

    @Test
    public void testGetDefaultReasonPhrase() {
        // Given
        char errorCode = 400;

        // When
        String result = ErrorCodeAttribute.getDefaultReasonPhrase(errorCode);

        // Then
        assertNotNull(result);
        assertEquals("(Bad Request): The request was malformed. The client should not retry the request without modification from the previous attempt.", result);
    }

    @Test
    public void testSetReasonPhrase() {
        // Given
        String reasonPhrase = "Test reason phrase";

        // When
        errorCodeAttribute.setReasonPhrase(reasonPhrase);

        // Then
        assertEquals(reasonPhrase, errorCodeAttribute.getReasonPhrase());
    }

    @Test
    public void testGetReasonPhrase() {
        // Given
        String reasonPhrase = "Test reason phrase";
        errorCodeAttribute.setReasonPhrase(reasonPhrase);

        // When
        String result = errorCodeAttribute.getReasonPhrase();

        // Then
        assertEquals(reasonPhrase, result);
    }

    @Test
    public void testGetName() {
        // When
        String result = errorCodeAttribute.getName();

        // Then
        assertEquals("ERROR-CODE", result);
    }

    @Test
    public void testGetDataLength() {
        // Given
        String reasonPhrase = "Test reason phrase";
        errorCodeAttribute.setReasonPhrase(reasonPhrase);

        // When
        char result = errorCodeAttribute.getDataLength();

        // Then
        assertEquals(4 + reasonPhrase.length(), result);
    }

    @Test
    public void testEquals() {
        // Given
        ErrorCodeAttribute other = new ErrorCodeAttribute();
        other.setErrorCode(400);
        other.setReasonPhrase("Test reason phrase");
        errorCodeAttribute.setErrorCode(400);
        errorCodeAttribute.setReasonPhrase("Test reason phrase");

        // When
        boolean result = errorCodeAttribute.equals(other);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentErrorCodes() {
        // Given
        ErrorCodeAttribute other = new ErrorCodeAttribute();
        other.setErrorCode(401);
        other.setReasonPhrase("Test reason phrase");
        errorCodeAttribute.setErrorCode(400);
        errorCodeAttribute.setReasonPhrase("Test reason phrase");

        // When
        boolean result = errorCodeAttribute.equals(other);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentReasonPhrases() {
        // Given
        ErrorCodeAttribute other = new ErrorCodeAttribute();
        other.setErrorCode(400);
        other.setReasonPhrase("Different reason phrase");
        errorCodeAttribute.setErrorCode(400);
        errorCodeAttribute.setReasonPhrase("Test reason phrase");

        // When
        boolean result = errorCodeAttribute.equals(other);

        // Then
        assertFalse(result);
    }

    @Test
    public void testSetErrorCode_InvalidErrorCode() {
        // Given
        char errorCode = 700;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> errorCodeAttribute.setErrorCode(errorCode));
    }

    @Test
    public void testSetErrorClass_InvalidErrorClass() {
        // Given
        byte errorClass = 7;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> errorCodeAttribute.setErrorClass(errorClass));
    }
}