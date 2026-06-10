import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
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
public class CDataNodeTest {

    @Mock
    private QuietAppendable quietAppendable;

    @Mock
    private Document document;

    private CDataNode cDataNode;

    @BeforeEach
    void setup() {
        cDataNode = new CDataNode("Test CData Node");
    }

    @Test
    void testNodeName() {
        // When: se ejecuta el método nodeName
        String nodeName = cDataNode.nodeName();

        // Then: se verifica el resultado
        assertEquals("#cdata", nodeName);
    }

    @Test
    void testText() {
        // When: se ejecuta el método text
        String text = cDataNode.text();

        // Then: se verifica el resultado
        assertEquals("Test CData Node", text);
    }

    @Test
    void testClone() {
        // When: se ejecuta el método clone
        CDataNode clonedNode = cDataNode.clone();

        // Then: se verifica el resultado
        assertNotNull(clonedNode);
        assertNotSame(cDataNode, clonedNode);
        assertEquals(cDataNode.text(), clonedNode.text());
    }

    @Test
    void testOuterHtmlHead() {
        // Given: se configura el QuietAppendable
        when(quietAppendable.append(any(CharSequence.class))).thenReturn(quietAppendable);

        // When: se ejecuta el método outerHtmlHead
        cDataNode.outerHtmlHead(quietAppendable, document.getOutputSettings());

        // Then: se verifica la interacción con el QuietAppendable
        verify(quietAppendable, times(1)).append("<![CDATA[");
        verify(quietAppendable, times(1)).append("Test CData Node");
        verify(quietAppendable, times(1)).append("]]>");
    }

    @Test
    void testOuterHtmlHeadToString() {
        // Given: se configura el QuietAppendable
        StringWriter stringWriter = new StringWriter();
        QuietAppendable quietAppendable = QuietAppendable.wrap(stringWriter);

        // When: se ejecuta el método outerHtmlHead
        cDataNode.outerHtmlHead(quietAppendable, document.getOutputSettings());

        // Then: se verifica el resultado
        String result = stringWriter.toString();
        assertEquals("<![CDATA[Test CData Node]]>", result);
    }
}