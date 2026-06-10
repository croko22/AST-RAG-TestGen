import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TransportTest {

    @Test
    public void testToString_TCP() {
        // Given: Transport instance of TCP
        org.ice4j.Transport transport = org.ice4j.Transport.TCP;

        // When: toString method is called
        String transportName = transport.toString();

        // Then: transport name is "tcp"
        assertEquals("tcp", transportName);
    }

    @Test
    public void testToString_UDP() {
        // Given: Transport instance of UDP
        org.ice4j.Transport transport = org.ice4j.Transport.UDP;

        // When: toString method is called
        String transportName = transport.toString();

        // Then: transport name is "udp"
        assertEquals("udp", transportName);
    }

    @Test
    public void testToString_TLS() {
        // Given: Transport instance of TLS
        org.ice4j.Transport transport = org.ice4j.Transport.TLS;

        // When: toString method is called
        String transportName = transport.toString();

        // Then: transport name is "tls"
        assertEquals("tls", transportName);
    }

    @Test
    public void testToString_DTLS() {
        // Given: Transport instance of DTLS
        org.ice4j.Transport transport = org.ice4j.Transport.DTLS;

        // When: toString method is called
        String transportName = transport.toString();

        // Then: transport name is "dtls"
        assertEquals("dtls", transportName);
    }

    @Test
    public void testToString_SCTP() {
        // Given: Transport instance of SCTP
        org.ice4j.Transport transport = org.ice4j.Transport.SCTP;

        // When: toString method is called
        String transportName = transport.toString();

        // Then: transport name is "sctp"
        assertEquals("sctp", transportName);
    }

    @Test
    public void testToString_SSLTCP() {
        // Given: Transport instance of SSLTCP
        org.ice4j.Transport transport = org.ice4j.Transport.SSLTCP;

        // When: toString method is called
        String transportName = transport.toString();

        // Then: transport name is "ssltcp"
        assertEquals("ssltcp", transportName);
    }

    @Test
    public void testParse_TCP() {
        // Given: transport name "tcp"
        String transportName = "tcp";

        // When: parse method is called
        org.ice4j.Transport transport = org.ice4j.Transport.parse(transportName);

        // Then: transport instance is TCP
        assertEquals(org.ice4j.Transport.TCP, transport);
    }

    @Test
    public void testParse_UDP() {
        // Given: transport name "udp"
        String transportName = "udp";

        // When: parse method is called
        org.ice4j.Transport transport = org.ice4j.Transport.parse(transportName);

        // Then: transport instance is UDP
        assertEquals(org.ice4j.Transport.UDP, transport);
    }

    @Test
    public void testParse_TLS() {
        // Given: transport name "tls"
        String transportName = "tls";

        // When: parse method is called
        org.ice4j.Transport transport = org.ice4j.Transport.parse(transportName);

        // Then: transport instance is TLS
        assertEquals(org.ice4j.Transport.TLS, transport);
    }

    @Test
    public void testParse_DTLS() {
        // Given: transport name "dtls"
        String transportName = "dtls";

        // When: parse method is called
        org.ice4j.Transport transport = org.ice4j.Transport.parse(transportName);

        // Then: transport instance is DTLS
        assertEquals(org.ice4j.Transport.DTLS, transport);
    }

    @Test
    public void testParse_SCTP() {
        // Given: transport name "sctp"
        String transportName = "sctp";

        // When: parse method is called
        org.ice4j.Transport transport = org.ice4j.Transport.parse(transportName);

        // Then: transport instance is SCTP
        assertEquals(org.ice4j.Transport.SCTP, transport);
    }

    @Test
    public void testParse_SSLTCP() {
        // Given: transport name "ssltcp"
        String transportName = "ssltcp";

        // When: parse method is called
        org.ice4j.Transport transport = org.ice4j.Transport.parse(transportName);

        // Then: transport instance is SSLTCP
        assertEquals(org.ice4j.Transport.SSLTCP, transport);
    }

    @Test
    public void testParse_InvalidTransport() {
        // Given: invalid transport name
        String transportName = "invalid";

        // When: parse method is called
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            org.ice4j.Transport.parse(transportName);
        });

        // Then: exception is thrown with correct message
        assertEquals(transportName + " is not a currently supported Transport", exception.getMessage());
    }

    @Test
    public void testParse_NullTransport() {
        // Given: null transport name
        String transportName = null;

        // When: parse method is called
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            org.ice4j.Transport.parse(transportName);
        });

        // Then: exception is thrown
        assertNotNull(exception);
    }
}