import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DelegatingDatagramSocketTest {

    @Mock
    private DatagramSocketFactory factory;

    @Mock
    private DatagramSocket delegate;

    @Mock
    private DatagramPacket packet;

    private DelegatingDatagramSocket socket;

    @BeforeEach
    public void setup() throws SocketException {
        socket = new DelegatingDatagramSocket(delegate);
    }

    @Test
    public void testBind() throws SocketException {
        SocketAddress addr = mock(SocketAddress.class);
        socket.bind(addr);
        verify(delegate).bind(addr);
    }

    @Test
    public void testClose() {
        socket.close();
        verify(delegate).close();
        assertTrue(socket.isClosed());
    }

    @Test
    public void testConnect_InetAddress() throws SocketException {
        InetAddress address = mock(InetAddress.class);
        int port = 1234;
        socket.connect(address, port);
        verify(delegate).connect(address, port);
    }

    @Test
    public void testConnect_SocketAddress() throws SocketException {
        SocketAddress addr = mock(SocketAddress.class);
        socket.connect(addr);
        verify(delegate).connect(addr);
    }

    @Test
    public void testDisconnect() {
        socket.disconnect();
        verify(delegate).disconnect();
    }

    @Test
    public void testGetBroadcast() throws SocketException {
        when(delegate.getBroadcast()).thenReturn(true);
        assertTrue(socket.getBroadcast());
        verify(delegate).getBroadcast();
    }

    @Test
    public void testGetChannel() {
        DatagramChannel channel = mock(DatagramChannel.class);
        when(delegate.getChannel()).thenReturn(channel);
        assertEquals(channel, socket.getChannel());
        verify(delegate).getChannel();
    }

    @Test
    public void testGetInetAddress() {
        InetAddress address = mock(InetAddress.class);
        when(delegate.getInetAddress()).thenReturn(address);
        assertEquals(address, socket.getInetAddress());
        verify(delegate).getInetAddress();
    }

    @Test
    public void testGetLocalAddress() {
        InetAddress address = mock(InetAddress.class);
        when(delegate.getLocalAddress()).thenReturn(address);
        assertEquals(address, socket.getLocalAddress());
        verify(delegate).getLocalAddress();
    }

    @Test
    public void testGetLocalPort() {
        when(delegate.getLocalPort()).thenReturn(1234);
        assertEquals(1234, socket.getLocalPort());
        verify(delegate).getLocalPort();
    }

    @Test
    public void testGetLocalSocketAddress() {
        SocketAddress addr = mock(SocketAddress.class);
        when(delegate.getLocalSocketAddress()).thenReturn(addr);
        assertEquals(addr, socket.getLocalSocketAddress());
        verify(delegate).getLocalSocketAddress();
    }

    @Test
    public void testGetPort() {
        when(delegate.getPort()).thenReturn(1234);
        assertEquals(1234, socket.getPort());
        verify(delegate).getPort();
    }

    @Test
    public void testGetReceiveBufferSize() throws SocketException {
        when(delegate.getReceiveBufferSize()).thenReturn(1024);
        assertEquals(1024, socket.getReceiveBufferSize());
        verify(delegate).getReceiveBufferSize();
    }

    @Test
    public void testGetRemoteSocketAddress() {
        SocketAddress addr = mock(SocketAddress.class);
        when(delegate.getRemoteSocketAddress()).thenReturn(addr);
        assertEquals(addr, socket.getRemoteSocketAddress());
        verify(delegate).getRemoteSocketAddress();
    }

    @Test
    public void testGetReuseAddress() throws SocketException {
        when(delegate.getReuseAddress()).thenReturn(true);
        assertTrue(socket.getReuseAddress());
        verify(delegate).getReuseAddress();
    }

    @Test
    public void testGetSendBufferSize() throws SocketException {
        when(delegate.getSendBufferSize()).thenReturn(1024);
        assertEquals(1024, socket.getSendBufferSize());
        verify(delegate).getSendBufferSize();
    }

    @Test
    public void testGetSoTimeout() throws SocketException {
        when(delegate.getSoTimeout()).thenReturn(1000);
        assertEquals(1000, socket.getSoTimeout());
        verify(delegate).getSoTimeout();
    }

    @Test
    public void testGetTrafficClass() throws SocketException {
        when(delegate.getTrafficClass()).thenReturn(1);
        assertEquals(1, socket.getTrafficClass());
        verify(delegate).getTrafficClass();
    }

    @Test
    public void testIsBound() {
        when(delegate.isBound()).thenReturn(true);
        assertTrue(socket.isBound());
        verify(delegate).isBound();
    }

    @Test
    public void testIsClosed() {
        assertFalse(socket.isClosed());
        socket.close();
        assertTrue(socket.isClosed());
    }

    @Test
    public void testIsConnected() {
        when(delegate.isConnected()).thenReturn(true);
        assertTrue(socket.isConnected());
        verify(delegate).isConnected();
    }

    @Test
    public void testReceive() throws IOException {
        socket.receive(packet);
        verify(delegate).receive(packet);
    }

    @Test
    public void testSend() throws IOException {
        socket.send(packet);
        verify(delegate).send(packet);
    }

    @Test
    public void testSetBroadcast() throws SocketException {
        socket.setBroadcast(true);
        verify(delegate).setBroadcast(true);
    }

    @Test
    public void testSetReceiveBufferSize() throws SocketException {
        socket.setReceiveBufferSize(1024);
        verify(delegate).setReceiveBufferSize(1024);
    }

    @Test
    public void testSetReuseAddress() throws SocketException {
        socket.setReuseAddress(true);
        verify(delegate).setReuseAddress(true);
    }

    @Test
    public void testSetSendBufferSize() throws SocketException {
        socket.setSendBufferSize(1024);
        verify(delegate).setSendBufferSize(1024);
    }

    @Test
    public void testSetSoTimeout() throws SocketException {
        socket.setSoTimeout(1000);
        verify(delegate).setSoTimeout(1000);
    }

    @Test
    public void testSetTrafficClass() throws SocketException {
        socket.setTrafficClass(1);
        verify(delegate).setTrafficClass(1);
    }

    @Test
    public void testSetDefaultDelegateFactory() {
        DelegatingDatagramSocket.setDefaultDelegateFactory(factory);
        assertEquals(factory, DelegatingDatagramSocket.delegateFactory);
    }

    @Test
    public void testSetDefaultReceiveBufferSize() {
        DelegatingDatagramSocket.setDefaultReceiveBufferSize(1024);
        assertEquals(1024, DelegatingDatagramSocket.defaultReceiveBufferSize);
    }
}