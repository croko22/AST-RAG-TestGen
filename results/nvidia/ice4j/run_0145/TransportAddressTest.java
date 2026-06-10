import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransportAddressTest {

    @InjectMocks
    private TransportAddress transportAddress;

    @Mock
    private InetAddress inetAddress;

    @Mock
    private InetSocketAddress inetSocketAddress;

    @BeforeEach
    void setup() throws UnknownHostException {
        inetAddress = InetAddress.getByName("127.0.0.1");
        inetSocketAddress = new InetSocketAddress(inetAddress, 8080);
        transportAddress = new TransportAddress(inetSocketAddress, Transport.UDP);
    }

    @AfterEach
    void tearDown() {
        // No need to clean up after each test
    }

    @Test
    public void testToString() {
        // Given
        String expected = "[" + inetAddress.getHostAddress() + "]:8080/UDP";

        // When
        String result = transportAddress.toString();

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testToRedactedString() {
        // Given
        String expected = "[" + "xx.xx.xx.xx" + "]:8080/UDP";

        // When
        String result = transportAddress.toRedactedString();

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testGetHostAddress() {
        // Given
        String expected = inetAddress.getHostAddress();

        // When
        String result = transportAddress.getHostAddress();

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testGetRedactedAddress() {
        // Given
        String expected = "xx.xx.xx.xx";

        // When
        String result = transportAddress.getRedactedAddress();

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testGetTransport() {
        // Given
        Transport expected = Transport.UDP;

        // When
        Transport result = transportAddress.getTransport();

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        // Given
        TransportAddress other = new TransportAddress(inetSocketAddress, Transport.UDP);

        // When
        boolean result = transportAddress.equals(other);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEqualsTransportAddress() {
        // Given
        TransportAddress other = new TransportAddress(inetSocketAddress, Transport.UDP);

        // When
        boolean result = transportAddress.equalsTransportAddress(other);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsIPv6() {
        // Given
        boolean expected = false;

        // When
        boolean result = transportAddress.isIPv6();

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testCanReach() {
        // Given
        TransportAddress other = new TransportAddress(inetSocketAddress, Transport.UDP);

        // When
        boolean result = transportAddress.canReach(other);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRedactInetAddress() {
        // Given
        String expected = "xx.xx.xx.xx";

        // When
        String result = TransportAddress.redact(inetAddress);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testRedactSocketAddress() {
        // Given
        String expected = "xx.xx.xx.xx:8080";

        // When
        String result = TransportAddress.redact(inetSocketAddress);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testToRedactedStringInetAddress() {
        // Given
        String expected = "xx.xx.xx.xx";

        // When
        String result = TransportAddress.toRedactedString(inetAddress);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testTransportAddressConstructorWithInetSocketAddress() {
        // Given
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        Transport transport = Transport.UDP;

        // When
        TransportAddress result = new TransportAddress(address, transport);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testTransportAddressConstructorWithInetAddress() {
        // Given
        InetAddress address = InetAddress.getByName("127.0.0.1");
        int port = 8080;
        Transport transport = Transport.UDP;

        // When
        TransportAddress result = new TransportAddress(address, port, transport);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testTransportAddressConstructorWithHostname() {
        // Given
        String hostname = "localhost";
        int port = 8080;
        Transport transport = Transport.UDP;

        // When
        TransportAddress result = new TransportAddress(hostname, port, transport);

        // Then
        assertNotNull(result);
    }

    @Test
    public void testTransportAddressConstructorWithByteArray() throws UnknownHostException {
        // Given
        byte[] ipAddress = InetAddress.getByName("127.0.0.1").getAddress();
        int port = 8080;
        Transport transport = Transport.UDP;

        // When
        TransportAddress result = new TransportAddress(ipAddress, port, transport);

        // Then
        assertNotNull(result);
    }
}