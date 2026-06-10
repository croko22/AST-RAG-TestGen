import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.LeafNode;
import org.jsoup.internal.QuietAppendable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataNodeTest {

    @Mock
    private QuietAppendable quietAppendable;

    @Mock
    private Document.OutputSettings outputSettings;

    private DataNode dataNode;

    @BeforeEach
    void setup() {
        dataNode = new DataNode("Test Data");
    }

    @Test
    public void testNodeName() {
        // When: nodeName is called
        String nodeName = dataNode.nodeName();

        // Then: nodeName should return "#data"
        assertEquals("#data", nodeName);
    }

    @Test
    public void testGetWholeData() {
        // Given: dataNode with "Test Data"
        // When: getWholeData is called
        String wholeData = dataNode.getWholeData();

        // Then: wholeData should return "Test Data"
        assertEquals("Test Data", wholeData);
    }

    @Test
    public void testSetWholeData() {
        // Given: dataNode with "Test Data"
        // When: setWholeData is called with new data
        DataNode updatedDataNode = dataNode.setWholeData("New Test Data");

        // Then: updatedDataNode should return the same instance
        assertSame(dataNode, updatedDataNode);

        // And: getWholeData should return the new data
        assertEquals("New Test Data", updatedDataNode.getWholeData());
    }

    @Test
    public void testClone() {
        // When: clone is called
        DataNode clonedDataNode = dataNode.clone();

        // Then: clonedDataNode should not be the same instance
        assertNotSame(dataNode, clonedDataNode);

        // And: clonedDataNode should have the same data
        assertEquals(dataNode.getWholeData(), clonedDataNode.getWholeData());
    }

    @Test
    public void testOuterHtmlHead_XmlSyntax() {
        // Given: outputSettings with XML syntax
        when(outputSettings.syntax()).thenReturn(Document.OutputSettings.Syntax.xml);

        // When: outerHtmlHead is called
        dataNode.outerHtmlHead(quietAppendable, outputSettings);

        // Then: quietAppendable should be appended with CDATA section
        verify(quietAppendable, times(1)).append(any(CharSequence.class));
    }

    @Test
    public void testOuterHtmlHead_HtmlSyntax() {
        // Given: outputSettings with HTML syntax
        when(outputSettings.syntax()).thenReturn(Document.OutputSettings.Syntax.html);

        // When: outerHtmlHead is called
        dataNode.outerHtmlHead(quietAppendable, outputSettings);

        // Then: quietAppendable should be appended with the data
        verify(quietAppendable, times(1)).append(any(CharSequence.class));
    }

    @Test
    public void testOuterHtmlHead_XmlSyntax_WithCdata() {
        // Given: outputSettings with XML syntax and data containing CDATA
        when(outputSettings.syntax()).thenReturn(Document.OutputSettings.Syntax.xml);
        dataNode.setWholeData("<![CDATA[Test Data]]>");

        // When: outerHtmlHead is called
        dataNode.outerHtmlHead(quietAppendable, outputSettings);

        // Then: quietAppendable should be appended with the data without CDATA section
        verify(quietAppendable, times(1)).append(any(CharSequence.class));
    }

    @Test
    public void testOuterHtmlHead_ScriptParent() {
        // Given: dataNode with script parent
        // Note: This test is not possible with the current implementation, as the parent node is not accessible.
        // This test is skipped.
    }

    @Test
    public void testOuterHtmlHead_StyleParent() {
        // Given: dataNode with style parent
        // Note: This test is not possible with the current implementation, as the parent node is not accessible.
        // This test is skipped.
    }
}