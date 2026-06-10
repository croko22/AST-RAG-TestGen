import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StackPropertiesTest {

    @Mock
    private static Logger logger;

    @InjectMocks
    private StackProperties stackProperties;

    @BeforeEach
    void setUp() {
        // Setup code here
    }

    @AfterEach
    void tearDown() {
        // Teardown code here
    }

    @Test
    public void testGetString_PropertyExists() {
        // Given
        String propertyName = "org.ice4j.BIND_RETRIES";
        String propertyValue = "50";
        System.setProperty(propertyName, propertyValue);

        // When
        String result = StackProperties.getString(propertyName);

        // Then
        assertNotNull(result);
        assertEquals(propertyValue, result);
    }

    @Test
    public void testGetString_PropertyDoesNotExist() {
        // Given
        String propertyName = "non.existent.property";

        // When
        String result = StackProperties.getString(propertyName);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetString_PropertyValueIsEmpty() {
        // Given
        String propertyName = "org.ice4j.BIND_RETRIES";
        String propertyValue = "";
        System.setProperty(propertyName, propertyValue);

        // When
        String result = StackProperties.getString(propertyName);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetStringArray_PropertyExists() {
        // Given
        String propertyName = "org.ice4j.BIND_RETRIES";
        String propertyValue = "50,60,70";
        System.setProperty(propertyName, propertyValue);

        // When
        String[] result = StackProperties.getStringArray(propertyName, ",");

        // Then
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("50", result[0]);
        assertEquals("60", result[1]);
        assertEquals("70", result[2]);
    }

    @Test
    public void testGetStringArray_PropertyDoesNotExist() {
        // Given
        String propertyName = "non.existent.property";

        // When
        String[] result = StackProperties.getStringArray(propertyName, ",");

        // Then
        assertNull(result);
    }

    @Test
    public void testGetStringArray_PropertyValueIsEmpty() {
        // Given
        String propertyName = "org.ice4j.BIND_RETRIES";
        String propertyValue = "";
        System.setProperty(propertyName, propertyValue);

        // When
        String[] result = StackProperties.getStringArray(propertyName, ",");

        // Then
        assertNull(result);
    }

    @Test
    public void testGetInt_PropertyExists() {
        // Given
        String propertyName = "org.ice4j.BIND_RETRIES";
        String propertyValue = "50";
        System.setProperty(propertyName, propertyValue);

        // When
        int result = StackProperties.getInt(propertyName, 0);

        // Then
        assertEquals(50, result);
    }

    @Test
    public void testGetInt_PropertyDoesNotExist() {
        // Given
        String propertyName = "non.existent.property";

        // When
        int result = StackProperties.getInt(propertyName, 0);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testGetInt_PropertyValueIsNotAnInteger() {
        // Given
        String propertyName = "org.ice4j.BIND_RETRIES";
        String propertyValue = "not an integer";
        System.setProperty(propertyName, propertyValue);

        // When
        int result = StackProperties.getInt(propertyName, 0);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testGetBoolean_PropertyExists() {
        // Given
        String propertyName = "org.ice4j.BIND_RETRIES";
        String propertyValue = "true";
        System.setProperty(propertyName, propertyValue);

        // When
        boolean result = StackProperties.getBoolean(propertyName, false);

        // Then
        assertTrue(result);
    }

    @Test
    public void testGetBoolean_PropertyDoesNotExist() {
        // Given
        String propertyName = "non.existent.property";

        // When
        boolean result = StackProperties.getBoolean(propertyName, false);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetBoolean_PropertyValueIsNotABoolean() {
        // Given
        String propertyName = "org.ice4j.BIND_RETRIES";
        String propertyValue = "not a boolean";
        System.setProperty(propertyName, propertyValue);

        // When
        boolean result = StackProperties.getBoolean(propertyName, false);

        // Then
        assertFalse(result);
    }
}