import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramPacketFilter;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiplexingXXXSocketSupportTest {

    @Mock
    private DatagramPacketFilter filter;

    @Mock
    private MultiplexedXXXSocketT socket;

    private MultiplexingXXXSocketSupport<MultiplexedXXXSocketT> multiplexingXXXSocketSupport;

    @BeforeEach
    void setup() {
        multiplexingXXXSocketSupport = new MultiplexingXXXSocketSupport<MultiplexedXXXSocketT>() {
            @Override
            protected MultiplexedXXXSocketT createSocket(DatagramPacketFilter filter) throws SocketException {
                return socket;
            }

            @Override
            protected void doReceive(DatagramPacket p) throws IOException {
                // Do nothing
            }

            @Override
            protected void doSetReceiveBufferSize(int receiveBufferSize) throws SocketException {
                // Do nothing
            }

            @Override
            protected SocketReceiveBuffer getReceived() {
                return new SocketReceiveBuffer();
            }

            @Override
            protected SocketReceiveBuffer getReceived(MultiplexedXXXSocketT socket) {
                return new SocketReceiveBuffer();
            }
        };
    }

    @AfterEach
    void tearDown() {
        multiplexingXXXSocketSupport = null;
    }

    @Test
    public void testClone() {
        // Given
        byte[] data = {1, 2, 3};
        DatagramPacket p = new DatagramPacket(data, data.length);
        // When
        DatagramPacket clone = MultiplexingXXXSocketSupport.clone(p);
        // Then
        assertNotNull(clone);
        assertNotSame(p, clone);
        assertEquals(p.getAddress(), clone.getAddress());
        assertEquals(p.getPort(), clone.getPort());
        assertEquals(p.getData(), clone.getData());
        assertEquals(p.getOffset(), clone.getOffset());
        assertEquals(p.getLength(), clone.getLength());
    }

    @Test
    public void testCopy() {
        // Given
        byte[] data = {1, 2, 3};
        DatagramPacket src = new DatagramPacket(data, data.length);
        DatagramPacket dest = new DatagramPacket(new byte[data.length], data.length);
        // When
        MultiplexingXXXSocketSupport.copy(src, dest);
        // Then
        assertNotNull(dest);
        assertEquals(src.getAddress(), dest.getAddress());
        assertEquals(src.getPort(), dest.getPort());
        assertEquals(src.getData(), dest.getData());
        assertEquals(src.getOffset(), dest.getOffset());
        assertEquals(src.getLength(), dest.getLength());
    }

    @Test
    public void testGetSocket() throws SocketException {
        // Given
        when(filter.equals(any())).thenReturn(true);
        // When
        MultiplexedXXXSocketT result = multiplexingXXXSocketSupport.getSocket(filter);
        // Then
        assertNotNull(result);
        assertEquals(socket, result);
    }

    @Test
    public void testGetSocket_CreateFalse() throws SocketException {
        // Given
        when(filter.equals(any())).thenReturn(false);
        // When
        MultiplexedXXXSocketT result = multiplexingXXXSocketSupport.getSocket(filter, false);
        // Then
        assertNull(result);
    }

    @Test
    public void testSetReceiveBufferSize() throws SocketException {
        // Given
        int receiveBufferSize = 1024;
        // When
        multiplexingXXXSocketSupport.setReceiveBufferSize(receiveBufferSize);
        // Then
        verify(multiplexingXXXSocketSupport, times(1)).doSetReceiveBufferSize(receiveBufferSize);
    }

    private static class SocketReceiveBuffer {
        private final List<DatagramPacket> packets = new ArrayList<>();

        public synchronized void add(DatagramPacket packet) {
            packets.add(packet);
        }

        public synchronized DatagramPacket poll() {
            return packets.isEmpty() ? null : packets.remove(0);
        }

        public synchronized boolean isEmpty() {
            return packets.isEmpty();
        }

        public synchronized void notifyAll() {
            notifyAll();
        }

        public synchronized List<DatagramPacket> scan(DatagramPacketFilter filter) {
            List<DatagramPacket> result = new ArrayList<>();
            for (DatagramPacket packet : packets) {
                if (filter.accept(packet)) {
                    result.add(packet);
                }
            }
            return result;
        }

        public synchronized void wait(long timeout) throws InterruptedException {
            wait(timeout);
        }
    }
}