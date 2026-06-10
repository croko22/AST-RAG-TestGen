import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.LeafNode;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.internal.StringUtil;
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
public class XmlDeclarationTest {

    @Mock
    private QuietAppendable quietAppendable;

    @Mock
    private Document.OutputSettings outputSettings;

    private XmlDeclaration xmlDeclaration;

    @BeforeEach
    void setup() {
        xmlDeclaration = new XmlDeclaration("xml", true);
    }

    @Test
    public void testNodeName() {
        // When
        String nodeName = xmlDeclaration.nodeName();

        // Then
        assertEquals("#declaration", nodeName);
    }

    @Test
    public void testName() {
        // When
        String name = xmlDeclaration.name();

        // Then
        assertEquals("xml", name);
    }

    @Test
    public void testGetWholeDeclaration() {
        // Given
        when(StringUtil.borrowBuilder()).thenReturn(new StringBuilder());
        when(StringUtil.releaseBuilder(any(StringBuilder.class))).thenAnswer(invocation -> invocation.getArgument(0).toString());

        // When
        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();

        // Then
        assertNotNull(wholeDeclaration);
    }

    @Test
    public void testToString() {
        // When
        String toString = xmlDeclaration.toString();

        // Then
        assertNotNull(toString);
    }

    @Test
    public void testClone() {
        // When
        XmlDeclaration clone = xmlDeclaration.clone();

        // Then
        assertNotNull(clone);
        assertNotSame(xmlDeclaration, clone);
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        // Given
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("key1", "value1"));
        attributes.add(new Attribute("key2", "value2"));
        xmlDeclaration = new XmlDeclaration("xml", true) {
            @Override
            public List<Attribute> attributes() {
                return attributes;
            }
        };

        // When
        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();

        // Then
        assertNotNull(wholeDeclaration);
        assertTrue(wholeDeclaration.contains("key1=\"value1\""));
        assertTrue(wholeDeclaration.contains("key2=\"value2\""));
    }

    @Test
    public void testGetWholeDeclarationWithEmptyAttributes() {
        // Given
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("key1", ""));
        attributes.add(new Attribute("key2", ""));
        xmlDeclaration = new XmlDeclaration("xml", true) {
            @Override
            public List<Attribute> attributes() {
                return attributes;
            }
        };

        // When
        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();

        // Then
        assertNotNull(wholeDeclaration);
        assertFalse(wholeDeclaration.contains("key1=\"\""));
        assertFalse(wholeDeclaration.contains("key2=\"\""));
    }
}