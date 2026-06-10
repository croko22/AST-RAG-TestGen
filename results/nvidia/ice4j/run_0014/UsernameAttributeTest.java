import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.ice4j.attribute.UsernameAttribute;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class UsernameAttributeTest {

    @Mock
    private UsernameAttribute usernameAttribute;

    @InjectMocks
    private UsernameAttribute usernameAttributeInject;

    @BeforeEach
    void setup() {
        usernameAttributeInject = new UsernameAttribute();
    }

    @AfterEach
    void tearDown() {
        usernameAttributeInject = null;
    }

    @Test
    public void testGetDataLength() {
        // Given
        byte[] username = "test".getBytes();
        usernameAttributeInject.setUsername(username);

        // When
        char dataLength = usernameAttributeInject.getDataLength();

        // Then
        assertEquals(username.length, dataLength);
    }

    @Test
    public void testGetName() {
        // Given

        // When
        String name = usernameAttributeInject.getName();

        // Then
        assertEquals("USERNAME", name);
    }

    @Test
    public void testSetUsername() {
        // Given
        byte[] username = "test".getBytes();

        // When
        usernameAttributeInject.setUsername(username);

        // Then
        assertArrayEquals(username, usernameAttributeInject.getUsername());
    }

    @Test
    public void testEquals_SameObject() {
        // Given

        // When
        boolean result = usernameAttributeInject.equals(usernameAttributeInject);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameValues() {
        // Given
        byte[] username = "test".getBytes();
        usernameAttributeInject.setUsername(username);
        UsernameAttribute otherAttribute = new UsernameAttribute();
        otherAttribute.setUsername(username);

        // When
        boolean result = usernameAttributeInject.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentValues() {
        // Given
        byte[] username = "test".getBytes();
        usernameAttributeInject.setUsername(username);
        UsernameAttribute otherAttribute = new UsernameAttribute();
        otherAttribute.setUsername("different".getBytes());

        // When
        boolean result = usernameAttributeInject.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_NullObject() {
        // Given

        // When
        boolean result = usernameAttributeInject.equals(null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given

        // When
        boolean result = usernameAttributeInject.equals("string");

        // Then
        assertFalse(result);
    }
}