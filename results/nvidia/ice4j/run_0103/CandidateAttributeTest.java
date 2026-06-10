import org.ice4j.ice.Candidate;
import org.ice4j.ice.Component;
import org.ice4j.ice.Transport;
import org.ice4j.ice.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sdp.SdpException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateAttributeTest {

    @Mock
    private Candidate<?> candidate;

    private CandidateAttribute candidateAttribute;

    @BeforeEach
    public void setup() {
        candidateAttribute = new CandidateAttribute(candidate);
    }

    @Test
    public void testGetName() {
        // Given
        String expectedName = CandidateAttribute.NAME;

        // When
        String actualName = candidateAttribute.getName();

        // Then
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetName() {
        // Given
        String name = "testName";

        // When
        candidateAttribute.setName(name);

        // Then
        // No assertion, as the method does nothing
    }

    @Test
    public void testHasValue() {
        // Given

        // When
        boolean hasValue = candidateAttribute.hasValue();

        // Then
        assertTrue(hasValue);
    }

    @Test
    public void testGetValue() {
        // Given
        String foundation = "foundation";
        int componentId = 1;
        Transport transport = Transport.UDP;
        int priority = 100;
        String hostAddress = "hostAddress";
        int port = 1234;
        String type = "typ";
        String relatedHostAddress = "relatedHostAddress";
        int relatedPort = 5678;

        when(candidate.getFoundation()).thenReturn(foundation);
        when(candidate.getParentComponent()).thenReturn(new Component(componentId));
        when(candidate.getTransport()).thenReturn(transport);
        when(candidate.getPriority()).thenReturn(priority);
        when(candidate.getTransportAddress()).thenReturn(new TransportAddress(hostAddress, port));
        when(candidate.getType()).thenReturn(type);
        when(candidate.getRelatedAddress()).thenReturn(new TransportAddress(relatedHostAddress, relatedPort));

        // When
        String actualValue = candidateAttribute.getValue();

        // Then
        String expectedValue = foundation + " " + componentId + " " + transport + " " + priority + " " + hostAddress + " " + port + " typ " + type + " raddr " + relatedHostAddress + " rport " + relatedPort;
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetValue() {
        // Given
        String value = "value";

        // When and Then
        assertThrows(SdpException.class, () -> candidateAttribute.setValue(value));
    }

    @Test
    public void testGetTypeChar() {
        // Given

        // When
        char typeChar = candidateAttribute.getTypeChar();

        // Then
        assertEquals('a', typeChar);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        // Given

        // When
        CandidateAttribute clone = candidateAttribute.clone();

        // Then
        assertNotSame(candidateAttribute, clone);
        assertEquals(candidateAttribute.candidate, clone.candidate);
    }

    @Test
    public void testEncode() {
        // Given
        String name = CandidateAttribute.NAME;
        String value = "value";

        when(candidateAttribute.getValue()).thenReturn(value);

        // When
        String encoded = candidateAttribute.encode();

        // Then
        String expectedEncoded = "a=" + name + ":" + value + "\r\n";
        assertEquals(expectedEncoded, encoded);
    }

    @Test
    public void testGetAttribute() {
        // Given
        String name = CandidateAttribute.NAME;
        String value = "value";

        when(candidateAttribute.getValue()).thenReturn(value);

        // When
        NameValue attribute = candidateAttribute.getAttribute();

        // Then
        assertEquals(name, attribute.getName());
        assertEquals(value, attribute.getValue());
    }
}