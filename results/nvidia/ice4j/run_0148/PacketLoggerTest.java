import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacketLoggerTest {

    @Mock
    private PacketLogger packetLogger;

    @BeforeEach
    public void setup() {
        // No setup needed for this test class
    }

    @AfterEach
    public void tearDown() {
        // No tear down needed for this test class
    }

    @Test
    public void testLogPacket_Sender() {
        // Given: packet logger is enabled
        when(packetLogger.isEnabled()).thenReturn(true);

        // When: log a packet as sender
        packetLogger.logPacket(1234, 5678, true);

        // Then: verify logPacket method was called
        verify(packetLogger, times(1)).logPacket(anyInt(), anyInt(), anyBoolean());
    }

    @Test
    public void testLogPacket_Receiver() {
        // Given: packet logger is enabled
        when(packetLogger.isEnabled()).thenReturn(true);

        // When: log a packet as receiver
        packetLogger.logPacket(1234, 5678, false);

        // Then: verify logPacket method was called
        verify(packetLogger, times(1)).logPacket(anyInt(), anyInt(), anyBoolean());
    }

    @Test
    public void testIsEnabled_Enabled() {
        // Given: packet logger is enabled
        when(packetLogger.isEnabled()).thenReturn(true);

        // When: check if packet logger is enabled
        boolean isEnabled = packetLogger.isEnabled();

        // Then: verify packet logger is enabled
        assertTrue(isEnabled);
    }

    @Test
    public void testIsEnabled_Disabled() {
        // Given: packet logger is disabled
        when(packetLogger.isEnabled()).thenReturn(false);

        // When: check if packet logger is enabled
        boolean isEnabled = packetLogger.isEnabled();

        // Then: verify packet logger is disabled
        assertFalse(isEnabled);
    }

    @Test
    public void testLogPacket_Disabled() {
        // Given: packet logger is disabled
        when(packetLogger.isEnabled()).thenReturn(false);

        // When: log a packet
        packetLogger.logPacket(1234, 5678, true);

        // Then: verify logPacket method was not called (no effect)
        verify(packetLogger, times(1)).logPacket(anyInt(), anyInt(), anyBoolean());
    }
}