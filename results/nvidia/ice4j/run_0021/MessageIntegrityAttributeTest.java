import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ice4j.attribute.MessageIntegrityAttribute;
import org.ice4j.stack.StunStack;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageIntegrityAttributeTest {

    @Mock
    private StunStack stunStack;

    private MessageIntegrityAttribute messageIntegrityAttribute;

    @BeforeEach
    void setup() {
        messageIntegrityAttribute = new MessageIntegrityAttribute();
    }

    @Test
    void testSetUsername() {
        // Given
        String username = "testUsername";

        // When
        messageIntegrityAttribute.setUsername(username);

        // Then
        assertEquals(username, messageIntegrityAttribute.getUsername());
    }

    @Test
    void testSetMedia() {
        // Given
        String media = "testMedia";

        // When
        messageIntegrityAttribute.setMedia(media);

        // Then
        assertEquals(media, messageIntegrityAttribute.getMedia());
    }

    @Test
    void testDecodeAttributeBody() {
        // Given
        byte[] attributeValue = new byte[20];
        char offset = 0;
        char length = 20;

        // When
        messageIntegrityAttribute.decodeAttributeBody(attributeValue, offset, length);

        // Then
        assertArrayEquals(attributeValue, messageIntegrityAttribute.getHmacSha1Content());
    }

    @Test
    void testGetDataLength() {
        // Given
        char expectedLength = 20;

        // When
        char actualLength = messageIntegrityAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    void testGetName() {
        // Given
        String expectedName = "MESSAGE_INTEGRITY";

        // When
        String actualName = messageIntegrityAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    void testEquals() {
        // Given
        MessageIntegrityAttribute otherAttribute = new MessageIntegrityAttribute();
        otherAttribute.setUsername("testUsername");
        otherAttribute.setMedia("testMedia");
        byte[] attributeValue = new byte[20];
        otherAttribute.decodeAttributeBody(attributeValue, 0, 20);

        // When
        boolean result = messageIntegrityAttribute.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentUsername() {
        // Given
        MessageIntegrityAttribute otherAttribute = new MessageIntegrityAttribute();
        otherAttribute.setUsername("differentUsername");
        otherAttribute.setMedia("testMedia");
        byte[] attributeValue = new byte[20];
        otherAttribute.decodeAttributeBody(attributeValue, 0, 20);

        // When
        boolean result = messageIntegrityAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_DifferentMedia() {
        // Given
        MessageIntegrityAttribute otherAttribute = new MessageIntegrityAttribute();
        otherAttribute.setUsername("testUsername");
        otherAttribute.setMedia("differentMedia");
        byte[] attributeValue = new byte[20];
        otherAttribute.decodeAttributeBody(attributeValue, 0, 20);

        // When
        boolean result = messageIntegrityAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_DifferentHmacSha1Content() {
        // Given
        MessageIntegrityAttribute otherAttribute = new MessageIntegrityAttribute();
        otherAttribute.setUsername("testUsername");
        otherAttribute.setMedia("testMedia");
        byte[] differentAttributeValue = new byte[20];
        otherAttribute.decodeAttributeBody(differentAttributeValue, 0, 20);

        // When
        boolean result = messageIntegrityAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }
}