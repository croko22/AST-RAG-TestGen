import org.ice4j.attribute.AttributeFactory;
import org.ice4j.attribute.ChangeRequestAttribute;
import org.ice4j.attribute.ChangedAddressAttribute;
import org.ice4j.attribute.ConnectionIdAttribute;
import org.ice4j.attribute.DataAttribute;
import org.ice4j.attribute.DestinationAddressAttribute;
import org.ice4j.attribute.EvenPortAttribute;
import org.ice4j.attribute.ErrorCodeAttribute;
import org.ice4j.attribute.FingerprintAttribute;
import org.ice4j.attribute.IceControlledAttribute;
import org.ice4j.attribute.IceControllingAttribute;
import org.ice4j.attribute.LifetimeAttribute;
import org.ice4j.attribute.MagicCookieAttribute;
import org.ice4j.attribute.MappedAddressAttribute;
import org.ice4j.attribute.MessageIntegrityAttribute;
import org.ice4j.attribute.NonceAttribute;
import org.ice4j.attribute.PriorityAttribute;
import org.ice4j.attribute.RealmAttribute;
import org.ice4j.attribute.ReflectedFromAttribute;
import org.ice4j.attribute.RequestedAddressFamilyAttribute;
import org.ice4j.attribute.RequestedTransportAttribute;
import org.ice4j.attribute.ReservationTokenAttribute;
import org.ice4j.attribute.ResponseAddressAttribute;
import org.ice4j.attribute.SourceAddressAttribute;
import org.ice4j.attribute.TransportAddress;
import org.ice4j.attribute.UnknownAttributesAttribute;
import org.ice4j.attribute.UseCandidateAttribute;
import org.ice4j.attribute.UsernameAttribute;
import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.attribute.XorRelayedAddressAttribute;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttributeFactoryTest {

    @Test
    public void testCreateChangeRequestAttribute() {
        ChangeRequestAttribute attribute = AttributeFactory.createChangeRequestAttribute();
        assertNotNull(attribute);
    }

    @Test
    public void testCreateChangeRequestAttributeWithFlags() {
        ChangeRequestAttribute attribute = AttributeFactory.createChangeRequestAttribute(true, true);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateChangedAddressAttribute() {
        TransportAddress address = new TransportAddress();
        ChangedAddressAttribute attribute = AttributeFactory.createChangedAddressAttribute(address);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateErrorCodeAttribute() {
        ErrorCodeAttribute attribute = AttributeFactory.createErrorCodeAttribute((byte) 1, (byte) 1);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateErrorCodeAttributeWithReasonPhrase() {
        ErrorCodeAttribute attribute = AttributeFactory.createErrorCodeAttribute((byte) 1, (byte) 1, "reason");
        assertNotNull(attribute);
    }

    @Test
    public void testCreateErrorCodeAttributeWithChar() {
        ErrorCodeAttribute attribute = AttributeFactory.createErrorCodeAttribute('a');
        assertNotNull(attribute);
    }

    @Test
    public void testCreateErrorCodeAttributeWithCharAndReasonPhrase() {
        ErrorCodeAttribute attribute = AttributeFactory.createErrorCodeAttribute('a', "reason");
        assertNotNull(attribute);
    }

    @Test
    public void testCreateMappedAddressAttribute() {
        TransportAddress address = new TransportAddress();
        MappedAddressAttribute attribute = AttributeFactory.createMappedAddressAttribute(address);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateReflectedFromAttribute() {
        TransportAddress address = new TransportAddress();
        ReflectedFromAttribute attribute = AttributeFactory.createReflectedFromAttribute(address);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateResponseAddressAttribute() {
        TransportAddress address = new TransportAddress();
        ResponseAddressAttribute attribute = AttributeFactory.createResponseAddressAttribute(address);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateSourceAddressAttribute() {
        TransportAddress address = new TransportAddress();
        SourceAddressAttribute attribute = AttributeFactory.createSourceAddressAttribute(address);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateUnknownAttributesAttribute() {
        UnknownAttributesAttribute attribute = AttributeFactory.createUnknownAttributesAttribute();
        assertNotNull(attribute);
    }

    @Test
    public void testCreateXorRelayedAddressAttribute() {
        TransportAddress address = new TransportAddress();
        XorRelayedAddressAttribute attribute = AttributeFactory.createXorRelayedAddressAttribute(address, new byte[0]);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateXorPeerAddressAttribute() {
        TransportAddress address = new TransportAddress();
        XorPeerAddressAttribute attribute = AttributeFactory.createXorPeerAddressAttribute(address, new byte[0]);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateXorMappedAddressAttribute() {
        TransportAddress address = new TransportAddress();
        XorMappedAddressAttribute attribute = AttributeFactory.createXorMappedAddressAttribute(address, new byte[0]);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateUsernameAttribute() {
        UsernameAttribute attribute = AttributeFactory.createUsernameAttribute("username".getBytes());
        assertNotNull(attribute);
    }

    @Test
    public void testCreateUsernameAttributeWithString() {
        UsernameAttribute attribute = AttributeFactory.createUsernameAttribute("username");
        assertNotNull(attribute);
    }

    @Test
    public void testCreateMessageIntegrityAttribute() {
        MessageIntegrityAttribute attribute = AttributeFactory.createMessageIntegrityAttribute("username");
        assertNotNull(attribute);
    }

    @Test
    public void testCreateFingerprintAttribute() {
        FingerprintAttribute attribute = AttributeFactory.createFingerprintAttribute();
        assertNotNull(attribute);
    }

    @Test
    public void testCreateChannelNumberAttribute() {
        ChannelNumberAttribute attribute = AttributeFactory.createChannelNumberAttribute('a');
        assertNotNull(attribute);
    }

    @Test
    public void testCreateRealmAttribute() {
        RealmAttribute attribute = AttributeFactory.createRealmAttribute("realm".getBytes());
        assertNotNull(attribute);
    }

    @Test
    public void testCreateNonceAttribute() {
        NonceAttribute attribute = AttributeFactory.createNonceAttribute("nonce".getBytes());
        assertNotNull(attribute);
    }

    @Test
    public void testCreateSoftwareAttribute() {
        SoftwareAttribute attribute = AttributeFactory.createSoftwareAttribute("software".getBytes());
        assertNotNull(attribute);
    }

    @Test
    public void testCreateEvenPortAttribute() {
        EvenPortAttribute attribute = AttributeFactory.createEvenPortAttribute(true);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateLifetimeAttribute() {
        LifetimeAttribute attribute = AttributeFactory.createLifetimeAttribute(100);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateRequestedTransportAttribute() {
        RequestedTransportAttribute attribute = AttributeFactory.createRequestedTransportAttribute((byte) 1);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateReservationTokenAttribute() {
        ReservationTokenAttribute attribute = AttributeFactory.createReservationTokenAttribute("token".getBytes());
        assertNotNull(attribute);
    }

    @Test
    public void testCreateDataAttribute() {
        DataAttribute attribute = AttributeFactory.createDataAttribute("data".getBytes());
        assertNotNull(attribute);
    }

    @Test
    public void testCreateDataAttributeWithoutPadding() {
        DataAttribute attribute = AttributeFactory.createDataAttributeWithoutPadding("data".getBytes());
        assertNotNull(attribute);
    }

    @Test
    public void testCreateIceControlledAttribute() {
        IceControlledAttribute attribute = AttributeFactory.createIceControlledAttribute(100);
        assertNotNull(attribute);
    }

    @Test
    public void testCreatePriorityAttribute() {
        PriorityAttribute attribute = AttributeFactory.createPriorityAttribute(100);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateUseCandidateAttribute() {
        UseCandidateAttribute attribute = AttributeFactory.createUseCandidateAttribute();
        assertNotNull(attribute);
    }

    @Test
    public void testCreateIceControllingAttribute() {
        IceControllingAttribute attribute = AttributeFactory.createIceControllingAttribute(100);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateMagicCookieAttribute() {
        MagicCookieAttribute attribute = AttributeFactory.createMagicCookieAttribute();
        assertNotNull(attribute);
    }

    @Test
    public void testCreateDestinationAddressAttribute() {
        TransportAddress address = new TransportAddress();
        DestinationAddressAttribute attribute = AttributeFactory.createDestinationAddressAttribute(address);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateRequestedAddressFamilyAttribute() {
        RequestedAddressFamilyAttribute attribute = AttributeFactory.createRequestedAddressFamilyAttribute('a');
        assertNotNull(attribute);
    }

    @Test
    public void testCreateConnectionIdAttribute() {
        ConnectionIdAttribute attribute = AttributeFactory.createConnectionIdAttribute(100);
        assertNotNull(attribute);
    }

    @Test
    public void testCreateConnectionIdAttributeWithoutValue() {
        ConnectionIdAttribute attribute = AttributeFactory.createConnectionIdAttribute();
        assertNotNull(attribute);
    }
}