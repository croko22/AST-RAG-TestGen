import org.ice4j.attribute.NonceAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NonceAttributeTest {

    private NonceAttribute nonceAttribute;

    @BeforeEach
    public void setup() {
        nonceAttribute = new NonceAttribute();
    }

    @Test
    public void testGetName() {
        // Given: NonceAttribute instance
        // When: getName method is called
        String name = nonceAttribute.getName();
        // Then: verify the result
        assertEquals("NONCE", name);
    }

    @Test
    public void testGetDataLength_WithNoNonce() {
        // Given: NonceAttribute instance with no nonce
        // When: getDataLength method is called
        char length = nonceAttribute.getDataLength();
        // Then: verify the result
        assertEquals(0, length);
    }

    @Test
    public void testGetDataLength_WithNonce() {
        // Given: NonceAttribute instance with a nonce
        byte[] nonce = {1, 2, 3};
        nonceAttribute.setNonce(nonce);
        // When: getDataLength method is called
        char length = nonceAttribute.getDataLength();
        // Then: verify the result
        assertEquals(3, length);
    }

    @Test
    public void testSetNonce_NullNonce() {
        // Given: NonceAttribute instance
        // When: setNonce method is called with a null nonce
        nonceAttribute.setNonce(null);
        // Then: verify the result
        assertNull(nonceAttribute.getNonce());
    }

    @Test
    public void testSetNonce_NonNullNonce() {
        // Given: NonceAttribute instance
        byte[] nonce = {1, 2, 3};
        // When: setNonce method is called with a non-null nonce
        nonceAttribute.setNonce(nonce);
        // Then: verify the result
        assertArrayEquals(nonce, nonceAttribute.getNonce());
    }

    @Test
    public void testEquals_SameInstance() {
        // Given: NonceAttribute instance
        // When: equals method is called with the same instance
        boolean result = nonceAttribute.equals(nonceAttribute);
        // Then: verify the result
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_SameNonce() {
        // Given: Two NonceAttribute instances with the same nonce
        NonceAttribute otherNonceAttribute = new NonceAttribute();
        byte[] nonce = {1, 2, 3};
        nonceAttribute.setNonce(nonce);
        otherNonceAttribute.setNonce(nonce.clone());
        // When: equals method is called
        boolean result = nonceAttribute.equals(otherNonceAttribute);
        // Then: verify the result
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_DifferentNonce() {
        // Given: Two NonceAttribute instances with different nonces
        NonceAttribute otherNonceAttribute = new NonceAttribute();
        byte[] nonce = {1, 2, 3};
        byte[] differentNonce = {4, 5, 6};
        nonceAttribute.setNonce(nonce);
        otherNonceAttribute.setNonce(differentNonce);
        // When: equals method is called
        boolean result = nonceAttribute.equals(otherNonceAttribute);
        // Then: verify the result
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: NonceAttribute instance and an instance of a different class
        Object differentClassInstance = new Object();
        // When: equals method is called
        boolean result = nonceAttribute.equals(differentClassInstance);
        // Then: verify the result
        assertFalse(result);
    }
}