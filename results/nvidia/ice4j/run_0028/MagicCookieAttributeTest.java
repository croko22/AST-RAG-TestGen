import org.ice4j.attribute.MagicCookieAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MagicCookieAttributeTest {

    private MagicCookieAttribute magicCookieAttribute;

    @BeforeEach
    public void setup() {
        magicCookieAttribute = new MagicCookieAttribute();
    }

    @Test
    public void testGetName() {
        // Given: MagicCookieAttribute instance
        // When: getName method is called
        String name = magicCookieAttribute.getName();
        // Then: verify the result
        assertEquals("MAGIC-COOKIE", name);
    }

    @Test
    public void testGetDataLength() {
        // Given: MagicCookieAttribute instance
        // When: getDataLength method is called
        char dataLength = magicCookieAttribute.getDataLength();
        // Then: verify the result
        assertEquals(4, dataLength);
    }

    @Test
    public void testEquals_SameInstance() {
        // Given: MagicCookieAttribute instance
        // When: equals method is called with the same instance
        boolean result = magicCookieAttribute.equals(magicCookieAttribute);
        // Then: verify the result
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_SameValues() {
        // Given: Two MagicCookieAttribute instances with the same values
        MagicCookieAttribute otherAttribute = new MagicCookieAttribute();
        // When: equals method is called
        boolean result = magicCookieAttribute.equals(otherAttribute);
        // Then: verify the result
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_DifferentValues() {
        // Given: Two MagicCookieAttribute instances with different values
        MagicCookieAttribute otherAttribute = new MagicCookieAttribute();
        otherAttribute.decodeAttributeBody(new byte[]{0x12, 0x34, 0x56, 0x78}, 0, 4);
        // When: equals method is called
        boolean result = magicCookieAttribute.equals(otherAttribute);
        // Then: verify the result
        assertFalse(result);
    }

    @Test
    public void testEquals_Null() {
        // Given: MagicCookieAttribute instance
        // When: equals method is called with null
        boolean result = magicCookieAttribute.equals(null);
        // Then: verify the result
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: MagicCookieAttribute instance
        // When: equals method is called with an instance of a different class
        boolean result = magicCookieAttribute.equals("String");
        // Then: verify the result
        assertFalse(result);
    }

    @Test
    public void testEncode() {
        // Given: MagicCookieAttribute instance
        // When: encode method is called
        byte[] encoded = magicCookieAttribute.encode();
        // Then: verify the result
        assertNotNull(encoded);
        assertEquals(8, encoded.length);
    }

    @Test
    public void testDecodeAttributeBody_ValidData() throws Exception {
        // Given: MagicCookieAttribute instance and valid data
        byte[] data = new byte[]{0x12, 0x34, 0x56, 0x78};
        // When: decodeAttributeBody method is called
        magicCookieAttribute.decodeAttributeBody(data, 0, 4);
        // Then: verify the result
        assertEquals(0x12345678, magicCookieAttribute.value);
    }

    @Test
    public void testDecodeAttributeBody_InvalidLength() throws Exception {
        // Given: MagicCookieAttribute instance and invalid data length
        byte[] data = new byte[]{0x12, 0x34, 0x56, 0x78};
        // When: decodeAttributeBody method is called
        assertThrows(Exception.class, () -> magicCookieAttribute.decodeAttributeBody(data, 0, 3));
    }
}