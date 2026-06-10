import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NetworkUtilsTest {

    @BeforeEach
    public void setup() {
        // No setup needed for this test class
    }

    @Test
    public void testIsValidPortNumber_MinPortNumber() {
        // Given: minimum port number
        int port = org.ice4j.ice.NetworkUtils.MIN_PORT_NUMBER;

        // When: checking if the port number is valid
        boolean isValid = org.ice4j.ice.NetworkUtils.isValidPortNumber(port);

        // Then: the port number should be valid
        assertTrue(isValid);
    }

    @Test
    public void testIsValidPortNumber_MaxPortNumber() {
        // Given: maximum port number
        int port = org.ice4j.ice.NetworkUtils.MAX_PORT_NUMBER;

        // When: checking if the port number is valid
        boolean isValid = org.ice4j.ice.NetworkUtils.isValidPortNumber(port);

        // Then: the port number should be valid
        assertTrue(isValid);
    }

    @Test
    public void testIsValidPortNumber_PortNumberBelowMin() {
        // Given: port number below the minimum
        int port = org.ice4j.ice.NetworkUtils.MIN_PORT_NUMBER - 1;

        // When: checking if the port number is valid
        boolean isValid = org.ice4j.ice.NetworkUtils.isValidPortNumber(port);

        // Then: the port number should not be valid
        assertFalse(isValid);
    }

    @Test
    public void testIsValidPortNumber_PortNumberAboveMax() {
        // Given: port number above the maximum
        int port = org.ice4j.ice.NetworkUtils.MAX_PORT_NUMBER + 1;

        // When: checking if the port number is valid
        boolean isValid = org.ice4j.ice.NetworkUtils.isValidPortNumber(port);

        // Then: the port number should not be valid
        assertFalse(isValid);
    }

    @Test
    public void testStripScopeID_NoScopeID() {
        // Given: IPv6 address without scope ID
        String ipv6Address = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";

        // When: stripping the scope ID
        String strippedAddress = org.ice4j.ice.NetworkUtils.stripScopeID(ipv6Address);

        // Then: the address should remain unchanged
        assertEquals(ipv6Address, strippedAddress);
    }

    @Test
    public void testStripScopeID_WithScopeID() {
        // Given: IPv6 address with scope ID
        String ipv6Address = "2001:0db8:85a3:0000:0000:8a2e:0370:7334%eth0";

        // When: stripping the scope ID
        String strippedAddress = org.ice4j.ice.NetworkUtils.stripScopeID(ipv6Address);

        // Then: the scope ID should be removed
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", strippedAddress);
    }

    @Test
    public void testStripScopeID_WithScopeIDAndSquareBrackets() {
        // Given: IPv6 address with scope ID and square brackets
        String ipv6Address = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334%eth0]";

        // When: stripping the scope ID
        String strippedAddress = org.ice4j.ice.NetworkUtils.stripScopeID(ipv6Address);

        // Then: the scope ID should be removed and the square brackets should remain
        assertEquals("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", strippedAddress);
    }

    @Test
    public void testStripScopeID_WithScopeIDAndSquareBracketsWithoutClosingBracket() {
        // Given: IPv6 address with scope ID and square brackets without closing bracket
        String ipv6Address = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334%eth0";

        // When: stripping the scope ID
        String strippedAddress = org.ice4j.ice.NetworkUtils.stripScopeID(ipv6Address);

        // Then: the scope ID should be removed and the square brackets should be closed
        assertEquals("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", strippedAddress);
    }
}