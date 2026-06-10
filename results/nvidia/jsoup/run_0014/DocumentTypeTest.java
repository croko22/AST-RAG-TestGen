import org.jsoup.internal.QuietAppendable;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.LeafNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentTypeTest {

    private DocumentType documentType;

    @BeforeEach
    void setup() {
        documentType = new DocumentType("testName", "testPublicId", "testSystemId");
    }

    @Test
    void testConstructor() {
        // Given
        String name = "testName";
        String publicId = "testPublicId";
        String systemId = "testSystemId";

        // When
        DocumentType documentType = new DocumentType(name, publicId, systemId);

        // Then
        assertEquals(name, documentType.name());
        assertEquals(publicId, documentType.publicId());
        assertEquals(systemId, documentType.systemId());
    }

    @Test
    void testSetPubSysKey() {
        // Given
        String pubSysKey = "testPubSysKey";

        // When
        documentType.setPubSysKey(pubSysKey);

        // Then
        assertEquals(pubSysKey, documentType.attr("pubSysKey"));
    }

    @Test
    void testSetName() {
        // Given
        String name = "testName";

        // When
        documentType.attr("name", name);

        // Then
        assertEquals(name, documentType.name());
    }

    @Test
    void testSetPublicId() {
        // Given
        String publicId = "testPublicId";

        // When
        documentType.attr("publicId", publicId);

        // Then
        assertEquals(publicId, documentType.publicId());
    }

    @Test
    void testSetSystemId() {
        // Given
        String systemId = "testSystemId";

        // When
        documentType.attr("systemId", systemId);

        // Then
        assertEquals(systemId, documentType.systemId());
    }

    @Test
    void testNodeName() {
        // When
        String nodeName = documentType.nodeName();

        // Then
        assertEquals("#doctype", nodeName);
    }

    @Test
    void testOuterHtmlHead() {
        // Given
        QuietAppendable quietAppendable = mock(QuietAppendable.class);

        // When
        documentType.outerHtmlHead(quietAppendable, null);

        // Then
        verify(quietAppendable, times(1)).append("<!DOCTYPE");
    }

    @Test
    void testHasAttribute() {
        // Given
        String attribute = "testAttribute";
        documentType.attr(attribute, "testValue");

        // When
        boolean hasAttribute = documentType.has(attribute);

        // Then
        assertTrue(hasAttribute);
    }

    @Test
    void testHasNoAttribute() {
        // Given
        String attribute = "testAttribute";

        // When
        boolean hasAttribute = documentType.has(attribute);

        // Then
        assertFalse(hasAttribute);
    }

    @Test
    void testUpdatePubSysKey() {
        // Given
        documentType.attr("publicId", null);
        documentType.attr("systemId", "testSystemId");

        // When
        documentType.updatePubSyskey();

        // Then
        assertEquals("SYSTEM", documentType.attr("pubSysKey"));
    }

    @Test
    void testUpdatePubSysKey_PublicId() {
        // Given
        documentType.attr("publicId", "testPublicId");
        documentType.attr("systemId", null);

        // When
        documentType.updatePubSyskey();

        // Then
        assertEquals("PUBLIC", documentType.attr("pubSysKey"));
    }
}