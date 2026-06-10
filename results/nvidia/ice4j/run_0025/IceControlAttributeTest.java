import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IceControlAttributeTest {

    @InjectMocks
    private IceControlAttribute iceControlAttribute;

    @Mock
    private org.ice4j.attribute.Attribute attribute;

    @BeforeEach
    void setup() {
        iceControlAttribute = new IceControlAttribute(true) {
            @Override
            public char getAttributeType() {
                return 0;
            }
        };
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(attribute);
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        // When
        boolean result = iceControlAttribute.equals(iceControlAttribute);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        // Given
        Object differentClass = new Object();

        // When
        boolean result = iceControlAttribute.equals(differentClass);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentAttributeType_ReturnsFalse() {
        // Given
        IceControlAttribute differentAttributeType = new IceControlAttribute(false) {
            @Override
            public char getAttributeType() {
                return 1;
            }
        };

        // When
        boolean result = iceControlAttribute.equals(differentAttributeType);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentIsControlling_ReturnsFalse() {
        // Given
        IceControlAttribute differentIsControlling = new IceControlAttribute(false) {
            @Override
            public char getAttributeType() {
                return 0;
            }
        };

        // When
        boolean result = iceControlAttribute.equals(differentIsControlling);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentDataLength_ReturnsFalse() {
        // Given
        IceControlAttribute differentDataLength = new IceControlAttribute(true) {
            @Override
            public char getDataLength() {
                return 1;
            }

            @Override
            public char getAttributeType() {
                return 0;
            }
        };

        // When
        boolean result = iceControlAttribute.equals(differentDataLength);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentTieBreaker_ReturnsFalse() {
        // Given
        IceControlAttribute differentTieBreaker = new IceControlAttribute(true) {
            @Override
            public char getAttributeType() {
                return 0;
            }
        };
        differentTieBreaker.setTieBreaker(1);

        // When
        boolean result = iceControlAttribute.equals(differentTieBreaker);

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetDataLength_ReturnsExpectedValue() {
        // When
        char result = iceControlAttribute.getDataLength();

        // Then
        assertEquals(8, result);
    }

    @Test
    public void testGetName_Controlling_ReturnsIceControlling() {
        // Given
        iceControlAttribute = new IceControlAttribute(true) {
            @Override
            public char getAttributeType() {
                return 0;
            }
        };

        // When
        String result = iceControlAttribute.getName();

        // Then
        assertEquals("ICE-CONTROLLING", result);
    }

    @Test
    public void testGetName_Controlled_ReturnsIceControlled() {
        // Given
        iceControlAttribute = new IceControlAttribute(false) {
            @Override
            public char getAttributeType() {
                return 0;
            }
        };

        // When
        String result = iceControlAttribute.getName();

        // Then
        assertEquals("ICE-CONTROLLED", result);
    }

    @Test
    public void testSetTieBreaker_SetsExpectedValue() {
        // Given
        long tieBreaker = 1;

        // When
        iceControlAttribute.setTieBreaker(tieBreaker);

        // Then
        assertEquals(tieBreaker, iceControlAttribute.getTieBreaker());
    }

    @Test
    public void testGetTieBreaker_ReturnsExpectedValue() {
        // Given
        long tieBreaker = 1;
        iceControlAttribute.setTieBreaker(tieBreaker);

        // When
        long result = iceControlAttribute.getTieBreaker();

        // Then
        assertEquals(tieBreaker, result);
    }
}