import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UnknownAttributesAttributeTest {

    @InjectMocks
    private UnknownAttributesAttribute unknownAttributesAttribute;

    @Test
    public void testGetName() {
        // Given
        String expectedName = "UNKNOWN-ATTRIBUTES";

        // When
        String actualName = unknownAttributesAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetDataLength_NoAttributes() {
        // Given
        char expectedLength = 0;

        // When
        char actualLength = unknownAttributesAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetDataLength_OneAttribute() {
        // Given
        char attributeID = 1;
        unknownAttributesAttribute.addAttributeID(attributeID);
        char expectedLength = 4;

        // When
        char actualLength = unknownAttributesAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetDataLength_TwoAttributes() {
        // Given
        char attributeID1 = 1;
        char attributeID2 = 2;
        unknownAttributesAttribute.addAttributeID(attributeID1);
        unknownAttributesAttribute.addAttributeID(attributeID2);
        char expectedLength = 4;

        // When
        char actualLength = unknownAttributesAttribute.getDataLength();

        // Then
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testAddAttributeID_NewAttribute() {
        // Given
        char attributeID = 1;
        int expectedSize = 1;

        // When
        unknownAttributesAttribute.addAttributeID(attributeID);

        // Then
        assertEquals(expectedSize, unknownAttributesAttribute.getAttributeCount());
    }

    @Test
    public void testAddAttributeID_ExistingAttribute() {
        // Given
        char attributeID = 1;
        unknownAttributesAttribute.addAttributeID(attributeID);
        int expectedSize = 1;

        // When
        unknownAttributesAttribute.addAttributeID(attributeID);

        // Then
        assertEquals(expectedSize, unknownAttributesAttribute.getAttributeCount());
    }

    @Test
    public void testContains_ExistingAttribute() {
        // Given
        char attributeID = 1;
        unknownAttributesAttribute.addAttributeID(attributeID);
        boolean expectedResult = true;

        // When
        boolean actualResult = unknownAttributesAttribute.contains(attributeID);

        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testContains_NonExistingAttribute() {
        // Given
        char attributeID = 1;
        boolean expectedResult = false;

        // When
        boolean actualResult = unknownAttributesAttribute.contains(attributeID);

        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetAttributes() {
        // Given
        char attributeID1 = 1;
        char attributeID2 = 2;
        unknownAttributesAttribute.addAttributeID(attributeID1);
        unknownAttributesAttribute.addAttributeID(attributeID2);
        Iterator<Character> expectedIterator = new ArrayList<Character>() {{
            add(attributeID1);
            add(attributeID2);
        }}.iterator();

        // When
        Iterator<Character> actualIterator = unknownAttributesAttribute.getAttributes();

        // Then
        while (expectedIterator.hasNext()) {
            char expectedAttribute = expectedIterator.next();
            char actualAttribute = actualIterator.next();
            assertEquals(expectedAttribute, actualAttribute);
        }
    }

    @Test
    public void testGetAttributeCount_NoAttributes() {
        // Given
        int expectedCount = 0;

        // When
        int actualCount = unknownAttributesAttribute.getAttributeCount();

        // Then
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetAttributeCount_OneAttribute() {
        // Given
        char attributeID = 1;
        unknownAttributesAttribute.addAttributeID(attributeID);
        int expectedCount = 1;

        // When
        int actualCount = unknownAttributesAttribute.getAttributeCount();

        // Then
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetAttribute() {
        // Given
        char attributeID = 1;
        unknownAttributesAttribute.addAttributeID(attributeID);
        int index = 0;
        char expectedAttribute = attributeID;

        // When
        char actualAttribute = unknownAttributesAttribute.getAttribute(index);

        // Then
        assertEquals(expectedAttribute, actualAttribute);
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        Object obj = unknownAttributesAttribute;
        boolean expectedResult = true;

        // When
        boolean actualResult = unknownAttributesAttribute.equals(obj);

        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEquals_DifferentObject_SameAttributes() {
        // Given
        UnknownAttributesAttribute otherAttribute = new UnknownAttributesAttribute();
        char attributeID = 1;
        unknownAttributesAttribute.addAttributeID(attributeID);
        otherAttribute.addAttributeID(attributeID);
        Object obj = otherAttribute;
        boolean expectedResult = true;

        // When
        boolean actualResult = unknownAttributesAttribute.equals(obj);

        // Then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEquals_DifferentObject_DifferentAttributes() {
        // Given
        UnknownAttributesAttribute otherAttribute = new UnknownAttributesAttribute();
        char attributeID1 = 1;
        char attributeID2 = 2;
        unknownAttributesAttribute.addAttributeID(attributeID1);
        otherAttribute.addAttributeID(attributeID2);
        Object obj = otherAttribute;
        boolean expectedResult = false;

        // When
        boolean actualResult = unknownAttributesAttribute.equals(obj);

        // Then
        assertEquals(expectedResult, actualResult);
    }
}