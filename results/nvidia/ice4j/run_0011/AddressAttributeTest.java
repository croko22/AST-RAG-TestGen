import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.MappedAddressAttribute;
import org.ice4j.attribute.ResponseAddressAttribute;
import org.ice4j.attribute.SourceAddressAttribute;
import org.ice4j.attribute.ChangedAddressAttribute;
import org.ice4j.attribute.ReflectedFromAttribute;
import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.attribute.AlternateServerAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.attribute.XorRelayedAddressAttribute;
import org.ice4j.attribute.TransportAddress;
import org.ice4j.StunException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AddressAttributeTest {

    @Mock
    private TransportAddress transportAddress;

    private AddressAttribute addressAttribute;

    @BeforeEach
    public void setup() {
        addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS) {
            @Override
            public String getName() {
                return super.getName();
            }

            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }

            @Override
            public char getDataLength() {
                return super.getDataLength();
            }

            @Override
            public void setAddress(TransportAddress address) {
                super.setAddress(address);
            }

            @Override
            public TransportAddress getAddress() {
                return super.getAddress();
            }

            @Override
            public byte getFamily() {
                return super.getFamily();
            }

            @Override
            public int getPort() {
                return super.getPort();
            }
        };
    }

    @Test
    public void testGetName() {
        // Given
        AddressAttribute addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);

        // When
        String name = addressAttribute.getName();

        // Then
        assertEquals(MappedAddressAttribute.NAME, name);
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        AddressAttribute addressAttribute1 = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        AddressAttribute addressAttribute2 = addressAttribute1;

        // When
        boolean equals = addressAttribute1.equals(addressAttribute2);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentObject_SameAttributes() throws UnknownHostException {
        // Given
        AddressAttribute addressAttribute1 = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        addressAttribute1.setAddress(new TransportAddress(InetAddress.getByName("192.168.1.1"), 1234, Transport.UDP));
        AddressAttribute addressAttribute2 = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        addressAttribute2.setAddress(new TransportAddress(InetAddress.getByName("192.168.1.1"), 1234, Transport.UDP));

        // When
        boolean equals = addressAttribute1.equals(addressAttribute2);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentObject_DifferentAttributes() throws UnknownHostException {
        // Given
        AddressAttribute addressAttribute1 = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        addressAttribute1.setAddress(new TransportAddress(InetAddress.getByName("192.168.1.1"), 1234, Transport.UDP));
        AddressAttribute addressAttribute2 = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        addressAttribute2.setAddress(new TransportAddress(InetAddress.getByName("192.168.1.2"), 1234, Transport.UDP));

        // When
        boolean equals = addressAttribute1.equals(addressAttribute2);

        // Then
        assertFalse(equals);
    }

    @Test
    public void testGetDataLength_IPv4() {
        // Given
        AddressAttribute addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);

        // When
        char dataLength = addressAttribute.getDataLength();

        // Then
        assertEquals(8, dataLength);
    }

    @Test
    public void testGetDataLength_IPv6() {
        // Given
        AddressAttribute addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        addressAttribute.setAddress(new TransportAddress(InetAddress.getByName("2001:0db8:85a3:0000:0000:8a2e:0370:7334"), 1234, Transport.UDP));

        // When
        char dataLength = addressAttribute.getDataLength();

        // Then
        assertEquals(20, dataLength);
    }

    @Test
    public void testSetAddress() throws UnknownHostException {
        // Given
        AddressAttribute addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("192.168.1.1"), 1234, Transport.UDP);

        // When
        addressAttribute.setAddress(transportAddress);

        // Then
        assertEquals(transportAddress, addressAttribute.getAddress());
    }

    @Test
    public void testGetAddress() throws UnknownHostException {
        // Given
        AddressAttribute addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("192.168.1.1"), 1234, Transport.UDP);
        addressAttribute.setAddress(transportAddress);

        // When
        TransportAddress address = addressAttribute.getAddress();

        // Then
        assertEquals(transportAddress, address);
    }

    @Test
    public void testGetFamily_IPv4() throws UnknownHostException {
        // Given
        AddressAttribute addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("192.168.1.1"), 1234, Transport.UDP);
        addressAttribute.setAddress(transportAddress);

        // When
        byte family = addressAttribute.getFamily();

        // Then
        assertEquals(1, family);
    }

    @Test
    public void testGetFamily_IPv6() throws UnknownHostException {
        // Given
        AddressAttribute addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("2001:0db8:85a3:0000:0000:8a2e:0370:7334"), 1234, Transport.UDP);
        addressAttribute.setAddress(transportAddress);

        // When
        byte family = addressAttribute.getFamily();

        // Then
        assertEquals(2, family);
    }

    @Test
    public void testGetPort() throws UnknownHostException {
        // Given
        AddressAttribute addressAttribute = new AddressAttribute(MappedAddressAttribute.MAPPED_ADDRESS);
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("192.168.1.1"), 1234, Transport.UDP);
        addressAttribute.setAddress(transportAddress);

        // When
        int port = addressAttribute.getPort();

        // Then
        assertEquals(1234, port);
    }
}