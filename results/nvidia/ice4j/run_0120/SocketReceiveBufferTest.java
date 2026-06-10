import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SocketReceiveBufferTest {

    @Mock
    private Callable<Integer> receiveBufferSizeSupplier;

    private SocketReceiveBuffer socketReceiveBuffer;

    @BeforeEach
    void setup() {
        socketReceiveBuffer = new SocketReceiveBuffer(receiveBufferSizeSupplier);
    }

    @Test
    void testIsEmpty_InitiallyEmpty() {
        // Given: A newly created SocketReceiveBuffer
        // When: We check if the buffer is empty
        // Then: The buffer should be empty
        assertTrue(socketReceiveBuffer.isEmpty());
    }

    @Test
    void testIsEmpty_AfterAddingDatagramPacket() {
        // Given: A newly created SocketReceiveBuffer
        DatagramPacket datagramPacket = new DatagramPacket(new byte[10], 10);
        // When: We add a datagram packet to the buffer
        socketReceiveBuffer.add(datagramPacket);
        // Then: The buffer should not be empty
        assertFalse(socketReceiveBuffer.isEmpty());
    }

    @Test
    void testAdd_DatagramPacket() {
        // Given: A newly created SocketReceiveBuffer
        DatagramPacket datagramPacket = new DatagramPacket(new byte[10], 10);
        // When: We add a datagram packet to the buffer
        socketReceiveBuffer.add(datagramPacket);
        // Then: The buffer should not be empty and the datagram packet should be added
        assertFalse(socketReceiveBuffer.isEmpty());
        DatagramPacket addedDatagramPacket = socketReceiveBuffer.poll();
        assertNotNull(addedDatagramPacket);
        assertEquals(datagramPacket.getData(), addedDatagramPacket.getData());
    }

    @Test
    void testAdd_MultipleDatagramPackets() {
        // Given: A newly created SocketReceiveBuffer
        DatagramPacket datagramPacket1 = new DatagramPacket(new byte[10], 10);
        DatagramPacket datagramPacket2 = new DatagramPacket(new byte[20], 20);
        // When: We add multiple datagram packets to the buffer
        socketReceiveBuffer.add(datagramPacket1);
        socketReceiveBuffer.add(datagramPacket2);
        // Then: The buffer should not be empty and the datagram packets should be added
        assertFalse(socketReceiveBuffer.isEmpty());
        DatagramPacket addedDatagramPacket1 = socketReceiveBuffer.poll();
        assertNotNull(addedDatagramPacket1);
        assertEquals(datagramPacket1.getData(), addedDatagramPacket1.getData());
        DatagramPacket addedDatagramPacket2 = socketReceiveBuffer.poll();
        assertNotNull(addedDatagramPacket2);
        assertEquals(datagramPacket2.getData(), addedDatagramPacket2.getData());
    }

    @Test
    void testPoll_EmptyBuffer() {
        // Given: A newly created SocketReceiveBuffer
        // When: We poll the buffer
        // Then: The buffer should return null
        assertNull(socketReceiveBuffer.poll());
    }

    @Test
    void testPoll_NonEmptyBuffer() {
        // Given: A newly created SocketReceiveBuffer with a datagram packet
        DatagramPacket datagramPacket = new DatagramPacket(new byte[10], 10);
        socketReceiveBuffer.add(datagramPacket);
        // When: We poll the buffer
        // Then: The buffer should return the datagram packet
        DatagramPacket polledDatagramPacket = socketReceiveBuffer.poll();
        assertNotNull(polledDatagramPacket);
        assertEquals(datagramPacket.getData(), polledDatagramPacket.getData());
    }

    @Test
    void testScan_NoMatchingDatagramPackets() {
        // Given: A newly created SocketReceiveBuffer with a datagram packet
        DatagramPacket datagramPacket = new DatagramPacket(new byte[10], 10);
        socketReceiveBuffer.add(datagramPacket);
        // When: We scan the buffer with a filter that does not match any datagram packets
        List<DatagramPacket> matchedDatagramPackets = socketReceiveBuffer.scan(packet -> false);
        // Then: The buffer should return an empty list
        assertTrue(matchedDatagramPackets.isEmpty());
    }

    @Test
    void testScan_MatchingDatagramPackets() {
        // Given: A newly created SocketReceiveBuffer with a datagram packet
        DatagramPacket datagramPacket = new DatagramPacket(new byte[10], 10);
        socketReceiveBuffer.add(datagramPacket);
        // When: We scan the buffer with a filter that matches the datagram packet
        List<DatagramPacket> matchedDatagramPackets = socketReceiveBuffer.scan(packet -> true);
        // Then: The buffer should return a list with the matched datagram packet
        assertFalse(matchedDatagramPackets.isEmpty());
        assertEquals(1, matchedDatagramPackets.size());
        DatagramPacket matchedDatagramPacket = matchedDatagramPackets.get(0);
        assertNotNull(matchedDatagramPacket);
        assertEquals(datagramPacket.getData(), matchedDatagramPacket.getData());
    }

    @Test
    void testScan_MultipleMatchingDatagramPackets() {
        // Given: A newly created SocketReceiveBuffer with multiple datagram packets
        DatagramPacket datagramPacket1 = new DatagramPacket(new byte[10], 10);
        DatagramPacket datagramPacket2 = new DatagramPacket(new byte[20], 20);
        socketReceiveBuffer.add(datagramPacket1);
        socketReceiveBuffer.add(datagramPacket2);
        // When: We scan the buffer with a filter that matches both datagram packets
        List<DatagramPacket> matchedDatagramPackets = socketReceiveBuffer.scan(packet -> true);
        // Then: The buffer should return a list with both matched datagram packets
        assertFalse(matchedDatagramPackets.isEmpty());
        assertEquals(2, matchedDatagramPackets.size());
        DatagramPacket matchedDatagramPacket1 = matchedDatagramPackets.get(0);
        assertNotNull(matchedDatagramPacket1);
        assertEquals(datagramPacket1.getData(), matchedDatagramPacket1.getData());
        DatagramPacket matchedDatagramPacket2 = matchedDatagramPackets.get(1);
        assertNotNull(matchedDatagramPacket2);
        assertEquals(datagramPacket2.getData(), matchedDatagramPacket2.getData());
    }
}