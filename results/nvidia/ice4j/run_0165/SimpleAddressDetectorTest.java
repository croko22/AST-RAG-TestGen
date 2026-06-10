import org.ice4j.stunclient.SimpleAddressDetector;
import org.ice4j.stunclient.BlockingRequestSender;
import org.ice4j.stack.StunStack;
import org.ice4j.IceSocketWrapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Transport;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimpleAddressDetectorTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private BlockingRequestSender requestSender;

    @Mock
    private IceSocketWrapper socket;

    private SimpleAddressDetector simpleAddressDetector;

    @BeforeEach
    public void setup() {
        simpleAddressDetector = new SimpleAddressDetector(new TransportAddress(new InetSocketAddress("localhost", 1234), Transport.UDP));
        simpleAddressDetector.stunStack = stunStack;
        simpleAddressDetector.requestSender = requestSender;
    }

    @AfterEach
    public void tearDown() {
        simpleAddressDetector.shutDown();
    }

    @Test
    public void testGetServerAddress() {
        TransportAddress serverAddress = simpleAddressDetector.getServerAddress();
        assertNotNull(serverAddress);
        assertEquals("localhost", serverAddress.getHostAddress());
        assertEquals(1234, ((InetSocketAddress) serverAddress.getSocketAddress()).getPort());
    }

    @Test
    public void testShutDown() {
        simpleAddressDetector.shutDown();
        assertNull(simpleAddressDetector.stunStack);
        assertNull(simpleAddressDetector.requestSender);
    }

    @Test
    public void testStart() {
        simpleAddressDetector.start();
        assertNotNull(simpleAddressDetector.stunStack);
    }

    @Test
    public void testGetMappingFor_Success() throws IOException, BindException {
        when(stunStack.addSocket(any(IceSocketWrapper.class))).thenReturn(true);
        when(requestSender.sendRequestAndWaitForResponse(any(), any())).thenReturn(new StunMessageEvent(new Response()));
        TransportAddress mappedAddress = simpleAddressDetector.getMappingFor(socket);
        assertNotNull(mappedAddress);
        verify(stunStack, times(1)).addSocket(socket);
        verify(requestSender, times(1)).sendRequestAndWaitForResponse(any(), any());
        verify(stunStack, times(1)).removeSocket(any(TransportAddress.class));
    }

    @Test
    public void testGetMappingFor_Failure() throws IOException, BindException {
        when(stunStack.addSocket(any(IceSocketWrapper.class))).thenReturn(false);
        TransportAddress mappedAddress = simpleAddressDetector.getMappingFor(socket);
        assertNull(mappedAddress);
        verify(stunStack, times(1)).addSocket(socket);
        verify(requestSender, never()).sendRequestAndWaitForResponse(any(), any());
        verify(stunStack, times(1)).removeSocket(any(TransportAddress.class));
    }

    @Test
    public void testGetMappingFor_IOException() throws IOException, BindException {
        when(stunStack.addSocket(any(IceSocketWrapper.class))).thenThrow(new IOException());
        assertThrows(IOException.class, () -> simpleAddressDetector.getMappingFor(socket));
        verify(stunStack, times(1)).addSocket(socket);
        verify(requestSender, never()).sendRequestAndWaitForResponse(any(), any());
        verify(stunStack, times(1)).removeSocket(any(TransportAddress.class));
    }

    @Test
    public void testGetMappingFor_BindException() throws IOException, BindException {
        when(stunStack.addSocket(any(IceSocketWrapper.class))).thenThrow(new BindException());
        assertThrows(BindException.class, () -> simpleAddressDetector.getMappingFor(socket));
        verify(stunStack, times(1)).addSocket(socket);
        verify(requestSender, never()).sendRequestAndWaitForResponse(any(), any());
        verify(stunStack, times(1)).removeSocket(any(TransportAddress.class));
    }
}