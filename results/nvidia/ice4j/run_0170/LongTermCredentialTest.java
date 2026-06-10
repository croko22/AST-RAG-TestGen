import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.ice4j.security.LongTermCredential;
import java.util.Arrays;

public class LongTermCredentialTest {

    private LongTermCredential longTermCredential;
    private LongTermCredential longTermCredentialWithUsernameAndPassword;

    @BeforeEach
    public void setup() {
        longTermCredential = new LongTermCredential();
        longTermCredentialWithUsernameAndPassword = new LongTermCredential("username", "password");
    }

    @Test
    public void testGetBytes() {
        // Given
        String input = "input";

        // When
        byte[] result = LongTermCredential.getBytes(input);

        // Then
        assertNotNull(result);
        assertEquals(input, new String(result, java.nio.charset.StandardCharsets.UTF_8));
    }

    @Test
    public void testGetBytesNull() {
        // Given
        String input = null;

        // When
        byte[] result = LongTermCredential.getBytes(input);

        // Then
        assertNull(result);
    }

    @Test
    public void testToString() {
        // Given
        byte[] input = "input".getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // When
        String result = LongTermCredential.toString(input);

        // Then
        assertNotNull(result);
        assertEquals("input", result);
    }

    @Test
    public void testToStringNull() {
        // Given
        byte[] input = null;

        // When
        String result = LongTermCredential.toString(input);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetPassword() {
        // Given
        byte[] password = "password".getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // When
        LongTermCredential longTermCredential = new LongTermCredential("username", "password");
        byte[] result = longTermCredential.getPassword();

        // Then
        assertNotNull(result);
        assertTrue(Arrays.equals(password, result));
    }

    @Test
    public void testGetUsername() {
        // Given
        byte[] username = "username".getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // When
        LongTermCredential longTermCredential = new LongTermCredential("username", "password");
        byte[] result = longTermCredential.getUsername();

        // Then
        assertNotNull(result);
        assertTrue(Arrays.equals(username, result));
    }

    @Test
    public void testHashCode() {
        // Given
        LongTermCredential longTermCredential1 = new LongTermCredential("username", "password");
        LongTermCredential longTermCredential2 = new LongTermCredential("username", "password");

        // When
        int hashCode1 = longTermCredential1.hashCode();
        int hashCode2 = longTermCredential2.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        // Given
        LongTermCredential longTermCredential1 = new LongTermCredential("username", "password");
        LongTermCredential longTermCredential2 = new LongTermCredential("username", "password");

        // When
        boolean result = longTermCredential1.equals(longTermCredential2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEqualsDifferentUsername() {
        // Given
        LongTermCredential longTermCredential1 = new LongTermCredential("username1", "password");
        LongTermCredential longTermCredential2 = new LongTermCredential("username2", "password");

        // When
        boolean result = longTermCredential1.equals(longTermCredential2);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEqualsDifferentPassword() {
        // Given
        LongTermCredential longTermCredential1 = new LongTermCredential("username", "password1");
        LongTermCredential longTermCredential2 = new LongTermCredential("username", "password2");

        // When
        boolean result = longTermCredential1.equals(longTermCredential2);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEqualsNull() {
        // Given
        LongTermCredential longTermCredential = new LongTermCredential("username", "password");

        // When
        boolean result = longTermCredential.equals(null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEqualsDifferentClass() {
        // Given
        LongTermCredential longTermCredential = new LongTermCredential("username", "password");
        Object object = new Object();

        // When
        boolean result = longTermCredential.equals(object);

        // Then
        assertFalse(result);
    }
}