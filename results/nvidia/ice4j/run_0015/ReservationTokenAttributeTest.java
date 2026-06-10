import org.ice4j.attribute.ReservationTokenAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReservationTokenAttributeTest {

    private ReservationTokenAttribute reservationTokenAttribute;

    @BeforeEach
    public void setup() {
        reservationTokenAttribute = new ReservationTokenAttribute();
    }

    @Test
    public void testGetName() {
        // Given
        String expectedName = "RESERVATION-TOKEN";

        // When
        String actualName = reservationTokenAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetReservationToken() {
        // Given
        byte[] reservationToken = new byte[8];

        // When
        reservationTokenAttribute.setReservationToken(reservationToken);

        // Then
        assertArrayEquals(reservationToken, reservationTokenAttribute.getReservationToken());
    }

    @Test
    public void testGetDataLength() {
        // Given
        char expectedDataLength = 8;

        // When
        char actualDataLength = reservationTokenAttribute.getDataLength();

        // Then
        assertEquals(expectedDataLength, actualDataLength);
    }

    @Test
    public void testCreateNewReservationTokenAttribute() {
        // When
        ReservationTokenAttribute newReservationTokenAttribute = ReservationTokenAttribute.createNewReservationTokenAttribute();

        // Then
        assertNotNull(newReservationTokenAttribute);
        assertNotNull(newReservationTokenAttribute.getReservationToken());
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object obj = reservationTokenAttribute;

        // When
        boolean result = reservationTokenAttribute.equals(obj);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameType() {
        // Given
        ReservationTokenAttribute otherReservationTokenAttribute = new ReservationTokenAttribute();
        otherReservationTokenAttribute.setReservationToken(reservationTokenAttribute.getReservationToken());

        // When
        boolean result = reservationTokenAttribute.equals(otherReservationTokenAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentType() {
        // Given
        Object obj = new Object();

        // When
        boolean result = reservationTokenAttribute.equals(obj);

        // Then
        assertFalse(result);
    }

    @Test
    public void testToString() {
        // Given
        byte[] reservationToken = new byte[8];
        reservationTokenAttribute.setReservationToken(reservationToken);

        // When
        String actualToString = reservationTokenAttribute.toString();

        // Then
        assertNotNull(actualToString);
        assertTrue(actualToString.startsWith("0x"));
    }

    @Test
    public void testToString_StaticMethod() {
        // Given
        byte[] reservationToken = new byte[8];

        // When
        String actualToString = ReservationTokenAttribute.toString(reservationToken);

        // Then
        assertNotNull(actualToString);
        assertTrue(actualToString.startsWith("0x"));
    }

    @Test
    public void testHashCode() {
        // Given
        int expectedHashCode = reservationTokenAttribute.hashCode();

        // When
        int actualHashCode = reservationTokenAttribute.hashCode();

        // Then
        assertEquals(expectedHashCode, actualHashCode);
    }
}