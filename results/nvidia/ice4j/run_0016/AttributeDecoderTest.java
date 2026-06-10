import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.AttributeDecoder;
import org.ice4j.attribute.ChangeRequestAttribute;
import org.ice4j.attribute.ChangedAddressAttribute;
import org.ice4j.attribute.MappedAddressAttribute;
import org.ice4j.attribute.ErrorCodeAttribute;
import org.ice4j.attribute.MessageIntegrityAttribute;
import org.ice4j.attribute.ReflectedFromAttribute;
import org.ice4j.attribute.ResponseAddressAttribute;
import org.ice4j.attribute.SourceAddressAttribute;
import org.ice4j.attribute.UnknownAttributesAttribute;
import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.attribute.XorOnlyAttribute;
import org.ice4j.attribute.SoftwareAttribute;
import org.ice4j.attribute.UsernameAttribute;
import org.ice4j.attribute.RealmAttribute;
import org.ice4j.attribute.NonceAttribute;
import org.ice4j.attribute.FingerprintAttribute;
import org.ice4j.attribute.AlternateServerAttribute;
import org.ice4j.attribute.ChannelNumberAttribute;
import org.ice4j.attribute.LifetimeAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.attribute.DataAttribute;
import org.ice4j.attribute.XorRelayedAddressAttribute;
import org.ice4j.attribute.EvenPortAttribute;
import org.ice4j.attribute.RequestedTransportAttribute;
import org.ice4j.attribute.DontFragmentAttribute;
import org.ice4j.attribute.ReservationTokenAttribute;
import org.ice4j.attribute.PriorityAttribute;
import org.ice4j.attribute.IceControllingAttribute;
import org.ice4j.attribute.IceControlledAttribute;
import org.ice4j.attribute.UseCandidateAttribute;
import org.ice4j.attribute.RequestedAddressFamilyAttribute;
import org.ice4j.attribute.ConnectionIdAttribute;
import org.ice4j.attribute.OptionalAttribute;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttributeDecoderTest {

    @Test
    public void testDecode_ChangeRequestAttribute() {
        // Given
        byte[] bytes = new byte[] {0x00, 0x01, 0x00, 0x08};
        int offset = 0;
        int length = bytes.length;

        // When
        Attribute attribute = AttributeDecoder.decode(bytes, offset, length);

        // Then
        assertNotNull(attribute);
        assertTrue(attribute instanceof ChangeRequestAttribute);
    }

    @Test
    public void testDecode_ChangedAddressAttribute() {
        // Given
        byte[] bytes = new byte[] {0x00, 0x02, 0x00, 0x08};
        int offset = 0;
        int length = bytes.length;

        // When
        Attribute attribute = AttributeDecoder.decode(bytes, offset, length);

        // Then
        assertNotNull(attribute);
        assertTrue(attribute instanceof ChangedAddressAttribute);
    }

    @Test
    public void testDecode_MappedAddressAttribute() {
        // Given
        byte[] bytes = new byte[] {0x00, 0x03, 0x00, 0x08};
        int offset = 0;
        int length = bytes.length;

        // When
        Attribute attribute = AttributeDecoder.decode(bytes, offset, length);

        // Then
        assertNotNull(attribute);
        assertTrue(attribute instanceof MappedAddressAttribute);
    }

    @Test
    public void testDecode_UnknownAttribute() {
        // Given
        byte[] bytes = new byte[] {0x00, 0x10, 0x00, 0x08};
        int offset = 0;
        int length = bytes.length;

        // When
        Attribute attribute = AttributeDecoder.decode(bytes, offset, length);

        // Then
        assertNotNull(attribute);
        assertTrue(attribute instanceof OptionalAttribute);
    }

    @Test
    public void testDecode_NullBytes() {
        // Given
        byte[] bytes = null;
        int offset = 0;
        int length = 0;

        // When and Then
        assertThrows(StunException.class, () -> AttributeDecoder.decode(bytes, offset, length));
    }

    @Test
    public void testDecode_InvalidBytes() {
        // Given
        byte[] bytes = new byte[] {0x00};
        int offset = 0;
        int length = bytes.length;

        // When and Then
        assertThrows(StunException.class, () -> AttributeDecoder.decode(bytes, offset, length));
    }

    @Test
    public void testDecode_AttributeLengthExceedsBytesLength() {
        // Given
        byte[] bytes = new byte[] {0x00, 0x01, 0x00, 0x10};
        int offset = 0;
        int length = bytes.length;

        // When and Then
        assertThrows(StunException.class, () -> AttributeDecoder.decode(bytes, offset, length));
    }
}