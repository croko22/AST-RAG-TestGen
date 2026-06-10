import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.AttributeFactory;
import org.ice4j.attribute.FingerprintAttribute;
import org.ice4j.attribute.MessageIntegrityAttribute;
import org.ice4j.attribute.UsernameAttribute;
import org.ice4j.message.Message;
import org.ice4j.message.Request;
import org.ice4j.message.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageTest {

    @Mock
    private Attribute attribute;

    private Message message;

    @BeforeEach
    public void setup() {
        message = new Request();
    }

    @Test
    public void testGetDataLength() {
        // Given
        Attribute attribute1 = AttributeFactory.createMappedAddressAttribute("192.168.1.1", 8080);
        Attribute attribute2 = AttributeFactory.createUsernameAttribute("username");

        // When
        message.putAttribute(attribute1);
        message.putAttribute(attribute2);

        // Then
        char dataLength = message.getDataLength();
        assertTrue(dataLength > 0);
    }

    @Test
    public void testGetDataLengthWithoutPadding() {
        // Given
        Attribute attribute1 = AttributeFactory.createMappedAddressAttribute("192.168.1.1", 8080);
        Attribute attribute2 = AttributeFactory.createUsernameAttribute("username");

        // When
        message.putAttribute(attribute1);
        message.putAttribute(attribute2);

        // Then
        char dataLength = message.getDataLengthWithoutPadding();
        assertTrue(dataLength > 0);
    }

    @Test
    public void testPutAttribute() {
        // Given
        Attribute attribute = AttributeFactory.createMappedAddressAttribute("192.168.1.1", 8080);

        // When
        message.putAttribute(attribute);

        // Then
        assertTrue(message.containsAttribute(Attribute.MAPPED_ADDRESS));
    }

    @Test
    public void testContainsAttribute() {
        // Given
        Attribute attribute = AttributeFactory.createMappedAddressAttribute("192.168.1.1", 8080);
        message.putAttribute(attribute);

        // When
        boolean containsAttribute = message.containsAttribute(Attribute.MAPPED_ADDRESS);

        // Then
        assertTrue(containsAttribute);
    }

    @Test
    public void testGetAttribute() {
        // Given
        Attribute attribute = AttributeFactory.createMappedAddressAttribute("192.168.1.1", 8080);
        message.putAttribute(attribute);

        // When
        Attribute retrievedAttribute = message.getAttribute(Attribute.MAPPED_ADDRESS);

        // Then
        assertNotNull(retrievedAttribute);
        assertEquals(attribute, retrievedAttribute);
    }

    @Test
    public void testGetAttributes() {
        // Given
        Attribute attribute1 = AttributeFactory.createMappedAddressAttribute("192.168.1.1", 8080);
        Attribute attribute2 = AttributeFactory.createUsernameAttribute("username");
        message.putAttribute(attribute1);
        message.putAttribute(attribute2);

        // When
        List<Attribute> attributes = message.getAttributes();

        // Then
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertTrue(attributes.contains(attribute1));
        assertTrue(attributes.contains(attribute2));
    }

    @Test
    public void testRemoveAttribute() {
        // Given
        Attribute attribute = AttributeFactory.createMappedAddressAttribute("192.168.1.1", 8080);
        message.putAttribute(attribute);

        // When
        Attribute removedAttribute = message.removeAttribute(Attribute.MAPPED_ADDRESS);

        // Then
        assertNotNull(removedAttribute);
        assertEquals(attribute, removedAttribute);
        assertFalse(message.containsAttribute(Attribute.MAPPED_ADDRESS));
    }

    @Test
    public void testGetAttributeCount() {
        // Given
        Attribute attribute1 = AttributeFactory.createMappedAddressAttribute("192.168.1.1", 8080);
        Attribute attribute2 = AttributeFactory.createUsernameAttribute("username");
        message.putAttribute(attribute1);
        message.putAttribute(attribute2);

        // When
        int attributeCount = message.getAttributeCount();

        // Then
        assertEquals(2, attributeCount);
    }

    @Test
    public void testGetMessageType() {
        // Given
        message.setMessageType(Message.BINDING_REQUEST);

        // When
        char messageType = message.getMessageType();

        // Then
        assertEquals(Message.BINDING_REQUEST, messageType);
    }

    @Test
    public void testSetTransactionID() {
        // Given
        byte[] transactionID = new byte[12];

        // When
        message.setTransactionID(transactionID);

        // Then
        assertNotNull(message.getTransactionID());
        assertArrayEquals(transactionID, message.getTransactionID());
    }

    @Test
    public void testGetName() {
        // Given
        message.setMessageType(Message.BINDING_REQUEST);

        // When
        String name = message.getName();

        // Then
        assertNotNull(name);
        assertEquals("BINDING-REQUEST", name);
    }

    @Test
    public void testEquals() {
        // Given
        Message message1 = new Request();
        Message message2 = new Request();
        message1.setMessageType(Message.BINDING_REQUEST);
        message2.setMessageType(Message.BINDING_REQUEST);

        // When
        boolean equals = message1.equals(message2);

        // Then
        assertTrue(equals);
    }

    @Test
    public void testDecode() {
        // Given
        byte[] binMessage = new byte[20];

        // When
        Message message = Message.decode(binMessage, 0, binMessage.length);

        // Then
        assertNotNull(message);
    }

    @Test
    public void testIsErrorResponseType() {
        // Given
        char type = Message.BINDING_ERROR_RESPONSE;

        // When
        boolean isErrorResponseType = Message.isErrorResponseType(type);

        // Then
        assertTrue(isErrorResponseType);
    }

    @Test
    public void testIsSuccessResponseType() {
        // Given
        char type = Message.BINDING_SUCCESS_RESPONSE;

        // When
        boolean isSuccessResponseType = Message.isSuccessResponseType(type);

        // Then
        assertTrue(isSuccessResponseType);
    }

    @Test
    public void testIsResponseType() {
        // Given
        char type = Message.BINDING_SUCCESS_RESPONSE;

        // When
        boolean isResponseType = Message.isResponseType(type);

        // Then
        assertTrue(isResponseType);
    }

    @Test
    public void testIsIndicationType() {
        // Given
        char type = Message.SEND_INDICATION;

        // When
        boolean isIndicationType = Message.isIndicationType(type);

        // Then
        assertTrue(isIndicationType);
    }

    @Test
    public void testIsRequestType() {
        // Given
        char type = Message.BINDING_REQUEST;

        // When
        boolean isRequestType = Message.isRequestType(type);

        // Then
        assertTrue(isRequestType);
    }

    @Test
    public void testToString() {
        // Given
        message.setMessageType(Message.BINDING_REQUEST);

        // When
        String toString = message.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("BINDING-REQUEST"));
    }
}