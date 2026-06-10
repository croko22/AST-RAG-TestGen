import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ChannelNumberAttributeTest {

    @Mock
    private Object obj;

    private ChannelNumberAttribute channelNumberAttribute;

    @BeforeEach
    void setup() {
        channelNumberAttribute = new ChannelNumberAttribute();
    }

    @Test
    void testEquals_SameObject_ReturnsTrue() {
        // When
        boolean result = channelNumberAttribute.equals(channelNumberAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentClass_ReturnsFalse() {
        // Given
        Object differentObj = new Object();

        // When
        boolean result = channelNumberAttribute.equals(differentObj);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_SameTypeDifferentData_ReturnsFalse() {
        // Given
        ChannelNumberAttribute differentAttribute = new ChannelNumberAttribute();
        differentAttribute.setChannelNumber(1);

        // When
        boolean result = channelNumberAttribute.equals(differentAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_SameTypeSameData_ReturnsTrue() {
        // Given
        ChannelNumberAttribute sameAttribute = new ChannelNumberAttribute();

        // When
        boolean result = channelNumberAttribute.equals(sameAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    void testGetName_ReturnsChannelNumber() {
        // When
        String name = channelNumberAttribute.getName();

        // Then
        assertEquals("CHANNEL-NUMBER", name);
    }

    @Test
    void testGetDataLength_ReturnsFour() {
        // When
        char length = channelNumberAttribute.getDataLength();

        // Then
        assertEquals(4, length);
    }

    @Test
    void testSetChannelNumber_SetsChannelNumber() {
        // Given
        char channelNumber = 1;

        // When
        channelNumberAttribute.setChannelNumber(channelNumber);

        // Then
        assertEquals(channelNumber, channelNumberAttribute.getChannelNumber());
    }

    @Test
    void testGetChannelNumber_ReturnsChannelNumber() {
        // Given
        channelNumberAttribute.setChannelNumber(1);

        // When
        char channelNumber = channelNumberAttribute.getChannelNumber();

        // Then
        assertEquals(1, channelNumber);
    }

    @Test
    void testIsValidRange_ChannelNoInRange_ReturnsTrue() {
        // Given
        char channelNo = 0x4000;

        // When
        boolean result = ChannelNumberAttribute.isValidRange(channelNo);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsValidRange_ChannelNoNotInRange_ReturnsFalse() {
        // Given
        char channelNo = 0x3FFF;

        // When
        boolean result = ChannelNumberAttribute.isValidRange(channelNo);

        // Then
        assertFalse(result);
    }
}