import org.ice4j.ChannelDataMessageEvent;
import org.ice4j.message.ChannelData;
import org.ice4j.stack.StunStack;
import org.ice4j.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChannelDataMessageEventTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private TransportAddress remoteAddress;

    @Mock
    private TransportAddress localAddress;

    @Mock
    private ChannelData channelDataMessage;

    private ChannelDataMessageEvent channelDataMessageEvent;

    @BeforeEach
    public void setup() {
        channelDataMessageEvent = new ChannelDataMessageEvent(stunStack, remoteAddress, localAddress, channelDataMessage);
    }

    @Test
    public void testGetChannelDataMessage() {
        // When
        ChannelData result = channelDataMessageEvent.getChannelDataMessage();

        // Then
        assertEquals(channelDataMessage, result);
    }

    @Test
    public void testGetRemoteAddress() {
        // When
        TransportAddress result = channelDataMessageEvent.getRemoteAddress();

        // Then
        assertEquals(remoteAddress, result);
    }

    @Test
    public void testGetLocalAddress() {
        // When
        TransportAddress result = channelDataMessageEvent.getLocalAddress();

        // Then
        assertEquals(localAddress, result);
    }

    @Test
    public void testGetStunStack() {
        // When
        StunStack result = channelDataMessageEvent.getStunStack();

        // Then
        assertEquals(stunStack, result);
    }

    @Test
    public void testConstructor() {
        // Given
        StunStack newStunStack = mock(StunStack.class);
        TransportAddress newRemoteAddress = mock(TransportAddress.class);
        TransportAddress newLocalAddress = mock(TransportAddress.class);
        ChannelData newChannelDataMessage = mock(ChannelData.class);

        // When
        ChannelDataMessageEvent newChannelDataMessageEvent = new ChannelDataMessageEvent(newStunStack, newRemoteAddress, newLocalAddress, newChannelDataMessage);

        // Then
        assertNotNull(newChannelDataMessageEvent);
        assertEquals(newChannelDataMessage, newChannelDataMessageEvent.getChannelDataMessage());
        assertEquals(newRemoteAddress, newChannelDataMessageEvent.getRemoteAddress());
        assertEquals(newLocalAddress, newChannelDataMessageEvent.getLocalAddress());
        assertEquals(newStunStack, newChannelDataMessageEvent.getStunStack());
    }

    @Test
    public void testNullStunStack() {
        // Given
        StunStack newStunStack = null;
        TransportAddress newRemoteAddress = mock(TransportAddress.class);
        TransportAddress newLocalAddress = mock(TransportAddress.class);
        ChannelData newChannelDataMessage = mock(ChannelData.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> new ChannelDataMessageEvent(newStunStack, newRemoteAddress, newLocalAddress, newChannelDataMessage));
    }

    @Test
    public void testNullRemoteAddress() {
        // Given
        StunStack newStunStack = mock(StunStack.class);
        TransportAddress newRemoteAddress = null;
        TransportAddress newLocalAddress = mock(TransportAddress.class);
        ChannelData newChannelDataMessage = mock(ChannelData.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> new ChannelDataMessageEvent(newStunStack, newRemoteAddress, newLocalAddress, newChannelDataMessage));
    }

    @Test
    public void testNullLocalAddress() {
        // Given
        StunStack newStunStack = mock(StunStack.class);
        TransportAddress newRemoteAddress = mock(TransportAddress.class);
        TransportAddress newLocalAddress = null;
        ChannelData newChannelDataMessage = mock(ChannelData.class);

        // When and Then
        assertThrows(NullPointerException.class, () -> new ChannelDataMessageEvent(newStunStack, newRemoteAddress, newLocalAddress, newChannelDataMessage));
    }

    @Test
    public void testNullChannelDataMessage() {
        // Given
        StunStack newStunStack = mock(StunStack.class);
        TransportAddress newRemoteAddress = mock(TransportAddress.class);
        TransportAddress newLocalAddress = mock(TransportAddress.class);
        ChannelData newChannelDataMessage = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new ChannelDataMessageEvent(newStunStack, newRemoteAddress, newLocalAddress, newChannelDataMessage));
    }
}