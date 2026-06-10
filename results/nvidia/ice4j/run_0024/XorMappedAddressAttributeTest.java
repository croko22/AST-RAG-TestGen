import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.message.Message;
import org.ice4j.message.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class XorMappedAddressAttributeTest {

    @InjectMocks
    private XorMappedAddressAttribute xorMappedAddressAttribute;

    @Mock
    private TransportAddress transportAddress;

    private byte[] transactionID;

    @BeforeEach
    public void setup() {
        transactionID = new byte[12];
        for (int i = 0; i < 12; i++) {
            transactionID[i] = (byte) i;
        }
    }

    @Test
    public void testApplyXor() throws UnknownHostException {
        // Given
        TransportAddress address = new TransportAddress("192.168.1.1", 1234, Transport.UDP);
        byte[] addressBytes = address.getAddressBytes();
        char port = (char) address.getPort();

        // When
        TransportAddress xoredAddress = XorMappedAddressAttribute.applyXor(address, transactionID);

        // Then
        assertNotNull(xoredAddress);
        byte[] xoredAddressBytes = xoredAddress.getAddressBytes();
        for (int i = 0; i < addressBytes.length; i++) {
            assertEquals(addressBytes[i] ^ transactionID[i], xoredAddressBytes[i]);
        }
        assertEquals(port ^ (transactionID[0] << 8 | transactionID[1]), (char) xoredAddress.getPort());
    }

    @Test
    public void testGetAddress() throws UnknownHostException {
        // Given
        TransportAddress address = new TransportAddress("192.168.1.1", 1234, Transport.UDP);
        xorMappedAddressAttribute = new XorMappedAddressAttribute();
        ((AddressAttribute) xorMappedAddressAttribute).setAddress(address);

        // When
        TransportAddress xoredAddress = xorMappedAddressAttribute.getAddress(transactionID);

        // Then
        assertNotNull(xoredAddress);
    }

    @Test
    public void testApplyXorWithXorMask() throws UnknownHostException {
        // Given
        TransportAddress address = new TransportAddress("192.168.1.1", 1234, Transport.UDP);
        xorMappedAddressAttribute = new XorMappedAddressAttribute();
        ((AddressAttribute) xorMappedAddressAttribute).setAddress(address);
        byte[] xorMask = new byte[16];
        System.arraycopy(Message.MAGIC_COOKIE, 0, xorMask, 0, 4);
        System.arraycopy(transactionID, 0, xorMask, 4, 12);

        // When
        TransportAddress xoredAddress = xorMappedAddressAttribute.applyXor(xorMask);

        // Then
        assertNotNull(xoredAddress);
    }

    @Test
    public void testSetAddress() throws UnknownHostException {
        // Given
        TransportAddress address = new TransportAddress("192.168.1.1", 1234, Transport.UDP);

        // When
        xorMappedAddressAttribute.setAddress(address, transactionID);

        // Then
        TransportAddress xoredAddress = (TransportAddress) ((AddressAttribute) xorMappedAddressAttribute).getAddress();
        assertNotNull(xoredAddress);
    }

    @Test
    public void testApplyXorWithNullAddress() {
        // Given
        TransportAddress address = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> XorMappedAddressAttribute.applyXor(address, transactionID));
    }

    @Test
    public void testApplyXorWithNullTransactionID() throws UnknownHostException {
        // Given
        TransportAddress address = new TransportAddress("192.168.1.1", 1234, Transport.UDP);
        byte[] transactionID = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> XorMappedAddressAttribute.applyXor(address, transactionID));
    }
}