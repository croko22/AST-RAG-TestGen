import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.AttributeFactory;
import org.ice4j.attribute.ChangeRequestAttribute;
import org.ice4j.attribute.MappedAddressAttribute;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Request;
import org.ice4j.message.StunMessageEvent;
import org.ice4j.socket.IceSocketWrapper;
import org.ice4j.stack.StunStack;
import org.ice4j.stunclient.BlockingRequestSender;
import org.ice4j.stunclient.NetworkConfigurationDiscoveryProcess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NetworkConfigurationDiscoveryProcessTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private BlockingRequestSender requestSender;

    @Mock
    private IceSocketWrapper sock;

    private NetworkConfigurationDiscoveryProcess discoveryProcess;

    @BeforeEach
    void setup() throws UnknownHostException {
        discoveryProcess = new NetworkConfigurationDiscoveryProcess(stunStack, new org.ice4j.TransportAddress(InetAddress.getLocalHost(), 5678), new org.ice4j.TransportAddress(InetAddress.getLocalHost(), 3478));
        discoveryProcess.requestSender = requestSender;
        discoveryProcess.sock = sock;
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(stunStack, requestSender, sock);
    }

    @Test
    void testShutDown() {
        // Given
        discoveryProcess.started = true;

        // When
        discoveryProcess.shutDown();

        // Then
        verify(stunStack).removeSocket(any(org.ice4j.TransportAddress.class));
        verify(sock).close();
        assertFalse(discoveryProcess.started);
    }

    @Test
    void testStart() throws IOException, org.ice4j.StunException {
        // Given

        // When
        discoveryProcess.start();

        // Then
        verify(stunStack).addSocket(any(IceSocketWrapper.class));
        assertTrue(discoveryProcess.started);
    }

    @Test
    void testDetermineAddress_NotStarted() {
        // Given

        // When and Then
        assertThrows(org.ice4j.StunException.class, () -> discoveryProcess.determineAddress());
    }

    @Test
    void testDetermineAddress_NoResponse() throws org.ice4j.StunException, IOException {
        // Given
        discoveryProcess.started = true;
        when(requestSender.sendRequestAndWaitForResponse(any(Request.class), any(org.ice4j.TransportAddress.class))).thenReturn(null);

        // When
        StunDiscoveryReport report = discoveryProcess.determineAddress();

        // Then
        assertNotNull(report);
        assertEquals(org.ice4j.StunDiscoveryReport.UDP_BLOCKING_FIREWALL, report.getNatType());
    }

    @Test
    void testDetermineAddress_OpenInternet() throws org.ice4j.StunException, IOException {
        // Given
        discoveryProcess.started = true;
        StunMessageEvent event = mock(StunMessageEvent.class);
        when(requestSender.sendRequestAndWaitForResponse(any(Request.class), any(org.ice4j.TransportAddress.class))).thenReturn(event);
        when(event.getMessage()).thenReturn(MessageFactory.createBindingResponse());
        MappedAddressAttribute mappedAddressAttribute = AttributeFactory.createMappedAddressAttribute();
        mappedAddressAttribute.setAddress(discoveryProcess.localAddress);
        when(event.getMessage().getAttribute(Attribute.MAPPED_ADDRESS)).thenReturn(mappedAddressAttribute);

        // When
        StunDiscoveryReport report = discoveryProcess.determineAddress();

        // Then
        assertNotNull(report);
        assertEquals(org.ice4j.StunDiscoveryReport.OPEN_INTERNET, report.getNatType());
    }

    @Test
    void testDetermineAddress_SymmetricUdpFirewall() throws org.ice4j.StunException, IOException {
        // Given
        discoveryProcess.started = true;
        StunMessageEvent event = mock(StunMessageEvent.class);
        when(requestSender.sendRequestAndWaitForResponse(any(Request.class), any(org.ice4j.TransportAddress.class))).thenReturn(event);
        when(event.getMessage()).thenReturn(MessageFactory.createBindingResponse());
        MappedAddressAttribute mappedAddressAttribute = AttributeFactory.createMappedAddressAttribute();
        mappedAddressAttribute.setAddress(discoveryProcess.localAddress);
        when(event.getMessage().getAttribute(Attribute.MAPPED_ADDRESS)).thenReturn(mappedAddressAttribute);
        when(requestSender.sendRequestAndWaitForResponse(any(Request.class), any(org.ice4j.TransportAddress.class))).thenReturn(null);

        // When
        StunDiscoveryReport report = discoveryProcess.determineAddress();

        // Then
        assertNotNull(report);
        assertEquals(org.ice4j.StunDiscoveryReport.SYMMETRIC_UDP_FIREWALL, report.getNatType());
    }

    @Test
    void testDoTestI() throws org.ice4j.StunException, IOException {
        // Given
        discoveryProcess.started = true;
        StunMessageEvent event = mock(StunMessageEvent.class);
        when(requestSender.sendRequestAndWaitForResponse(any(Request.class), any(org.ice4j.TransportAddress.class))).thenReturn(event);

        // When
        StunMessageEvent result = discoveryProcess.doTestI(new org.ice4j.TransportAddress(InetAddress.getLocalHost(), 3478));

        // Then
        assertEquals(event, result);
    }

    @Test
    void testDoTestII() throws org.ice4j.StunException, IOException {
        // Given
        discoveryProcess.started = true;
        StunMessageEvent event = mock(StunMessageEvent.class);
        when(requestSender.sendRequestAndWaitForResponse(any(Request.class), any(org.ice4j.TransportAddress.class))).thenReturn(event);

        // When
        StunMessageEvent result = discoveryProcess.doTestII(new org.ice4j.TransportAddress(InetAddress.getLocalHost(), 3478));

        // Then
        assertEquals(event, result);
    }

    @Test
    void testDoTestIII() throws org.ice4j.StunException, IOException {
        // Given
        discoveryProcess.started = true;
        StunMessageEvent event = mock(StunMessageEvent.class);
        when(requestSender.sendRequestAndWaitForResponse(any(Request.class), any(org.ice4j.TransportAddress.class))).thenReturn(event);

        // When
        StunMessageEvent result = discoveryProcess.doTestIII(new org.ice4j.TransportAddress(InetAddress.getLocalHost(), 3478));

        // Then
        assertEquals(event, result);
    }
}