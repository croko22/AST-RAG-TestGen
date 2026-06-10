import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChannelDataTest {

    @InjectMocks
    private ChannelData channelData;

    @Test
    public void testSetChannelNumber() {
        // Given
        char channelNumber = 0x4000;

        // When
        channelData.setChannelNumber(channelNumber);

        // Then
        assertEquals(channelNumber, channelData.getChannelNumber());
    }

    @Test
    public void testGetChannelNumber() {
        // Given
        char channelNumber = 0x4000;
        channelData.setChannelNumber(channelNumber);

        // When
        char result = channelData.getChannelNumber();

        // Then
        assertEquals(channelNumber, result);
    }

    @Test
    public void testSetData() {
        // Given
        byte[] data = {1, 2, 3};

        // When
        channelData.setData(data);

        // Then
        assertArrayEquals(data, channelData.getData());
    }

    @Test
    public void testGetDataLength() {
        // Given
        byte[] data = {1, 2, 3};
        channelData.setData(data);

        // When
        char result = channelData.getDataLength();

        // Then
        assertEquals(data.length, result);
    }

    @Test
    public void testDecode() throws Exception {
        // Given
        byte[] binMessage = {0x40, 0x00, 0x00, 0x03, 1, 2, 3};
        char offset = 0;

        // When
        ChannelData result = ChannelData.decode(binMessage, offset);

        // Then
        assertEquals(0x4000, result.getChannelNumber());
        assertArrayEquals(new byte[]{1, 2, 3}, result.getData());
    }

    @Test
    public void testDecode_InvalidChannelNumber() throws Exception {
        // Given
        byte[] binMessage = {0x3F, 0xFF, 0x00, 0x03, 1, 2, 3};
        char offset = 0;

        // When and Then
        assertThrows(Exception.class, () -> ChannelData.decode(binMessage, offset));
    }

    @Test
    public void testDecode_InvalidMessageLength() throws Exception {
        // Given
        byte[] binMessage = {0x40, 0x00, 0x00, 0x04, 1, 2, 3};
        char offset = 0;

        // When and Then
        assertThrows(Exception.class, () -> ChannelData.decode(binMessage, offset));
    }

    @Test
    public void testIsChannelDataMessage() {
        // Given
        byte[] binMessage = {0x40, 0x00, 0x00, 0x03, 1, 2, 3};

        // When
        boolean result = ChannelData.isChannelDataMessage(binMessage);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsChannelDataMessage_False() {
        // Given
        byte[] binMessage = {0x00, 0x00, 0x00, 0x03, 1, 2, 3};

        // When
        boolean result = ChannelData.isChannelDataMessage(binMessage);

        // Then
        assertFalse(result);
    }
}