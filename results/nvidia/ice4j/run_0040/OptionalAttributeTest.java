import org.ice4j.attribute.OptionalAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OptionalAttributeTest {

    private OptionalAttribute optionalAttribute;

    @BeforeEach
    public void setup() {
        optionalAttribute = new OptionalAttribute('a');
    }

    @Test
    public void testGetDataLength_InitialValue() {
        // Given: optionalAttribute has no attributeValue
        // When: getDataLength is called
        char dataLength = optionalAttribute.getDataLength();
        // Then: dataLength should be 0
        assertEquals(0, dataLength);
    }

    @Test
    public void testGetDataLength_AfterSettingBody() {
        // Given: attributeValue is set
        byte[] body = {1, 2, 3};
        optionalAttribute.setBody(body, 0, body.length);
        // When: getDataLength is called
        char dataLength = optionalAttribute.getDataLength();
        // Then: dataLength should be equal to the length of attributeValue
        assertEquals(body.length, dataLength);
    }

    @Test
    public void testGetName() {
        // Given: optionalAttribute is created
        // When: getName is called
        String name = optionalAttribute.getName();
        // Then: name should be "Unknown Attribute"
        assertEquals("Unknown Attribute", name);
    }

    @Test
    public void testSetBody() {
        // Given: body is set
        byte[] body = {1, 2, 3};
        optionalAttribute.setBody(body, 0, body.length);
        // When: getBody is called
        byte[] attributeValue = optionalAttribute.getBody();
        // Then: attributeValue should be equal to body
        assertArrayEquals(body, attributeValue);
    }

    @Test
    public void testEquals_SameObject() {
        // Given: same object
        Object obj = optionalAttribute;
        // When: equals is called
        boolean equals = optionalAttribute.equals(obj);
        // Then: equals should be true
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentObject_SameAttributeValue() {
        // Given: different object with same attributeValue
        OptionalAttribute otherOptionalAttribute = new OptionalAttribute('a');
        byte[] body = {1, 2, 3};
        optionalAttribute.setBody(body, 0, body.length);
        otherOptionalAttribute.setBody(body, 0, body.length);
        // When: equals is called
        boolean equals = optionalAttribute.equals(otherOptionalAttribute);
        // Then: equals should be true
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentObject_DifferentAttributeValue() {
        // Given: different object with different attributeValue
        OptionalAttribute otherOptionalAttribute = new OptionalAttribute('a');
        byte[] body = {1, 2, 3};
        optionalAttribute.setBody(body, 0, body.length);
        byte[] differentBody = {4, 5, 6};
        otherOptionalAttribute.setBody(differentBody, 0, differentBody.length);
        // When: equals is called
        boolean equals = optionalAttribute.equals(otherOptionalAttribute);
        // Then: equals should be false
        assertFalse(equals);
    }

    @Test
    public void testEquals_NullObject() {
        // Given: null object
        Object obj = null;
        // When: equals is called
        boolean equals = optionalAttribute.equals(obj);
        // Then: equals should be false
        assertFalse(equals);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: different class
        Object obj = new Object();
        // When: equals is called
        boolean equals = optionalAttribute.equals(obj);
        // Then: equals should be false
        assertFalse(equals);
    }
}