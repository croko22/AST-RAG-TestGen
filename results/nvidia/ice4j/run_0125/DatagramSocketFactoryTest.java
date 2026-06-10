import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.DatagramSocket;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatagramSocketFactoryTest {

    @Mock
    private DatagramSocketFactory datagramSocketFactory;

    @BeforeEach
    void setup() {
        // No setup required for this test class
    }

    @AfterEach
    void tearDown() {
        // No tear down required for this test class
    }

    @Test
    public void testCreateUnboundDatagramSocket_Success() {
        // Given: A mock DatagramSocketFactory
        DatagramSocket expectedDatagramSocket = mock(DatagramSocket.class);
        when(datagramSocketFactory.createUnboundDatagramSocket()).thenReturn(expectedDatagramSocket);

        // When: createUnboundDatagramSocket is called
        DatagramSocket actualDatagramSocket = datagramSocketFactory.createUnboundDatagramSocket();

        // Then: The returned DatagramSocket is not null
        assertNotNull(actualDatagramSocket);

        // Then: The returned DatagramSocket is the expected one
        assertEquals(expectedDatagramSocket, actualDatagramSocket);

        // Then: The createUnboundDatagramSocket method was called once
        verify(datagramSocketFactory, times(1)).createUnboundDatagramSocket();
    }

    @Test
    public void testCreateUnboundDatagramSocket_ThrowsSocketException() {
        // Given: A mock DatagramSocketFactory that throws a SocketException
        when(datagramSocketFactory.createUnboundDatagramSocket()).thenThrow(SocketException.class);

        // When / Then: createUnboundDatagramSocket is called and a SocketException is thrown
        assertThrows(SocketException.class, () -> datagramSocketFactory.createUnboundDatagramSocket());

        // Then: The createUnboundDatagramSocket method was called once
        verify(datagramSocketFactory, times(1)).createUnboundDatagramSocket();
    }

    @Test
    public void testCreateUnboundDatagramSocket_ThrowsNullPointerException() {
        // Given: A null DatagramSocketFactory
        DatagramSocketFactory nullDatagramSocketFactory = null;

        // When / Then: createUnboundDatagramSocket is called and a NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> nullDatagramSocketFactory.createUnboundDatagramSocket());
    }
}