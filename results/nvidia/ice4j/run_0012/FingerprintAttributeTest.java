import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ice4j.attribute.FingerprintAttribute;
import org.ice4j.attribute.StunException;

import java.util.zip.CRC32;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FingerprintAttributeTest {

    @Mock
    private FingerprintAttribute fingerprintAttribute;

    @InjectMocks
    private FingerprintAttribute fingerprintAttributeInject;

    @BeforeEach
    void setup() {
        fingerprintAttribute = new FingerprintAttribute();
    }

    @AfterEach
    void tearDown() {
        fingerprintAttribute = null;
    }

    @Test
    public void testGetDataLength() {
        // Given
        char expectedLength = 4;

        // When
        char actualLength = fingerprintAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetName() {
        // Given
        String expectedName = "FINGERPRINT";

        // When
        String actualName = fingerprintAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object obj = fingerprintAttribute;

        // When
        boolean result = fingerprintAttribute.equals(obj);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameType() {
        // Given
        FingerprintAttribute otherAttribute = new FingerprintAttribute();

        // When
        boolean result = fingerprintAttribute.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentType() {
        // Given
        Object obj = new Object();

        // When
        boolean result = fingerprintAttribute.equals(obj);

        // Then
        assertFalse(result);
    }

    @Test
    public void testDecodeAttributeBody_ValidLength() throws StunException {
        // Given
        byte[] attributeValue = {0x12, 0x34, 0x56, 0x78};
        char offset = 0;
        char length = 4;

        // When
        fingerprintAttribute.decodeAttributeBody(attributeValue, offset, length);

        // Then
        assertNotNull(fingerprintAttribute.getChecksum());
    }

    @Test
    public void testDecodeAttributeBody_InvalidLength() {
        // Given
        byte[] attributeValue = {0x12, 0x34, 0x56};
        char offset = 0;
        char length = 3;

        // When and Then
        assertThrows(StunException.class, () -> fingerprintAttribute.decodeAttributeBody(attributeValue, offset, length));
    }

    @Test
    public void testCalculateXorCRC32() {
        // Given
        byte[] message = {0x01, 0x02, 0x03, 0x04};
        int offset = 0;
        int len = 4;

        // When
        byte[] result = FingerprintAttribute.calculateXorCRC32(message, offset, len);

        // Then
        assertNotNull(result);
        assertEquals(4, result.length);
    }
}