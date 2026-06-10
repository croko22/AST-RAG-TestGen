import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.ice4j.socket.RtcpDemuxPacketFilter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

@ExtendWith(MockitoExtension.class)
public class RtcpDemuxPacketFilterTest {

    @Test
    public void testIsRtcpPacket_RtcpPacket_ReturnsTrue() throws Exception {
        // Given: a valid RTCP packet
        byte[] data = new byte[] {(byte) 0x80, (byte) 0x200, 0, 0};
        DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 1234);

        // When: we call isRtcpPacket
        boolean result = RtcpDemuxPacketFilter.isRtcpPacket(p);

        // Then: it returns true
        assertTrue(result);
    }

    @Test
    public void testIsRtcpPacket_RtpPacket_ReturnsFalse() throws Exception {
        // Given: a valid RTP packet
        byte[] data = new byte[] {(byte) 0x80, (byte) 0x60, 0, 0};
        DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 1234);

        // When: we call isRtcpPacket
        boolean result = RtcpDemuxPacketFilter.isRtcpPacket(p);

        // Then: it returns false
        assertFalse(result);
    }

    @Test
    public void testIsRtcpPacket_InvalidPacket_ReturnsFalse() throws Exception {
        // Given: an invalid packet (less than 4 bytes)
        byte[] data = new byte[] {(byte) 0x80};
        DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 1234);

        // When: we call isRtcpPacket
        boolean result = RtcpDemuxPacketFilter.isRtcpPacket(p);

        // Then: it returns false
        assertFalse(result);
    }

    @Test
    public void testIsRtcpPacket_NullPacket_ThrowsNullPointerException() {
        // Given: a null packet
        DatagramPacket p = null;

        // When: we call isRtcpPacket
        assertThrows(NullPointerException.class, () -> RtcpDemuxPacketFilter.isRtcpPacket(p));
    }

    @Test
    public void testAccept_RtcpPacket_ReturnsTrue() throws Exception {
        // Given: a valid RTCP packet
        byte[] data = new byte[] {(byte) 0x80, (byte) 0x200, 0, 0};
        DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 1234);

        // When: we call accept
        RtcpDemuxPacketFilter filter = new RtcpDemuxPacketFilter();
        boolean result = filter.accept(p);

        // Then: it returns true
        assertTrue(result);
    }

    @Test
    public void testAccept_RtpPacket_ReturnsFalse() throws Exception {
        // Given: a valid RTP packet
        byte[] data = new byte[] {(byte) 0x80, (byte) 0x60, 0, 0};
        DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 1234);

        // When: we call accept
        RtcpDemuxPacketFilter filter = new RtcpDemuxPacketFilter();
        boolean result = filter.accept(p);

        // Then: it returns false
        assertFalse(result);
    }

    @Test
    public void testAccept_InvalidPacket_ReturnsFalse() throws Exception {
        // Given: an invalid packet (less than 4 bytes)
        byte[] data = new byte[] {(byte) 0x80};
        DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 1234);

        // When: we call accept
        RtcpDemuxPacketFilter filter = new RtcpDemuxPacketFilter();
        boolean result = filter.accept(p);

        // Then: it returns false
        assertFalse(result);
    }

    @Test
    public void testAccept_NullPacket_ThrowsNullPointerException() {
        // Given: a null packet
        DatagramPacket p = null;

        // When: we call accept
        RtcpDemuxPacketFilter filter = new RtcpDemuxPacketFilter();
        assertThrows(NullPointerException.class, () -> filter.accept(p));
    }
}