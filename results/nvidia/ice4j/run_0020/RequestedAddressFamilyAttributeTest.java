import org.ice4j.attribute.RequestedAddressFamilyAttribute;
import org.ice4j.StunException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RequestedAddressFamilyAttributeTest {

    private RequestedAddressFamilyAttribute requestedAddressFamilyAttribute;

    @BeforeEach
    void setup() {
        requestedAddressFamilyAttribute = new RequestedAddressFamilyAttribute();
    }

    @Test
    void testGetDataLength() {
        // Given
        char expectedDataLength = RequestedAddressFamilyAttribute.DATA_LENGTH;

        // When
        char actualDataLength = requestedAddressFamilyAttribute.getDataLength();

        // Then
        assertEquals(expectedDataLength, actualDataLength);
    }

    @Test
    void testGetName() {
        // Given
        String expectedName = RequestedAddressFamilyAttribute.NAME;

        // When
        String actualName = requestedAddressFamilyAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    void testEquals_SameObject() {
        // Given
        Object obj = requestedAddressFamilyAttribute;

        // When
        boolean result = requestedAddressFamilyAttribute.equals(obj);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentObject_SameAttributes() {
        // Given
        RequestedAddressFamilyAttribute otherAttribute = new RequestedAddressFamilyAttribute();

        // When
        boolean result = requestedAddressFamilyAttribute.equals(otherAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    void testEquals_DifferentObject_DifferentAttributes() {
        // Given
        RequestedAddressFamilyAttribute otherAttribute = new RequestedAddressFamilyAttribute();
        otherAttribute.setFamily(RequestedAddressFamilyAttribute.IPv6);

        // When
        boolean result = requestedAddressFamilyAttribute.equals(otherAttribute);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_NullObject() {
        // Given
        Object obj = null;

        // When
        boolean result = requestedAddressFamilyAttribute.equals(obj);

        // Then
        assertFalse(result);
    }

    @Test
    void testEquals_DifferentClass() {
        // Given
        Object obj = new Object();

        // When
        boolean result = requestedAddressFamilyAttribute.equals(obj);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetFamily() {
        // Given
        char expectedFamily = RequestedAddressFamilyAttribute.IPv4;

        // When
        char actualFamily = requestedAddressFamilyAttribute.getFamily();

        // Then
        assertEquals(expectedFamily, actualFamily);
    }

    @Test
    void testSetFamily_IPv4() {
        // Given
        char family = RequestedAddressFamilyAttribute.IPv4;

        // When
        boolean result = requestedAddressFamilyAttribute.setFamily(family);

        // Then
        assertTrue(result);
        assertEquals(family, requestedAddressFamilyAttribute.getFamily());
    }

    @Test
    void testSetFamily_IPv6() {
        // Given
        char family = RequestedAddressFamilyAttribute.IPv6;

        // When
        boolean result = requestedAddressFamilyAttribute.setFamily(family);

        // Then
        assertTrue(result);
        assertEquals(family, requestedAddressFamilyAttribute.getFamily());
    }

    @Test
    void testSetFamily_InvalidFamily() {
        // Given
        char family = (char) 0x03;

        // When
        boolean result = requestedAddressFamilyAttribute.setFamily(family);

        // Then
        assertFalse(result);
        assertEquals(RequestedAddressFamilyAttribute.IPv4, requestedAddressFamilyAttribute.getFamily());
    }
}