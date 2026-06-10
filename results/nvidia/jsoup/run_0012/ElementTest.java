Here is the complete test class for the `Element` class:

```java
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
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
public class ElementTest {

    @Mock
    private Document document;

    private Element element;

    @BeforeEach
    void setup() {
        element = new Element("div", "");
    }

    @Test
    void testAttributes() {
        // Given
        String attributeKey = "test";
        String attributeValue = "value";

        // When
        Element result = element.attr(attributeKey, attributeValue);

        // Then
        assertEquals(element, result);
        assertNotNull(element.attributes());
        assertEquals(attributeValue, element.attributes().get(attributeKey));
    }

    @Test
    void testBaseUri() {
        // Given
        String baseUri = "https://example.com";

        // When
        element.setBaseUri(baseUri);

        // Then
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    void testChildNodeSize() {
        // Given
        Node childNode = new TextNode("text");

        // When
        element.appendChild(childNode);

        // Then
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testNodeName() {
        // Given
        String nodeName = "div";

        // When
        String result = element.nodeName();

        // Then
        assertEquals(nodeName, result);
    }

    @Test
    void testTagName() {
        // Given
        String tagName = "div";

        // When
        String result = element.tagName();

        // Then
        assertEquals(tagName, result);
    }

    @Test
    void testNormalName() {
        // Given
        String normalName = "div";

        // When
        String result = element.normalName();

        // Then
        assertEquals(normalName, result);
    }

    @Test
    void testElementIs() {
        // Given
        String normalName = "div";
        String namespace = "";

        // When
        boolean result = element.elementIs(normalName, namespace);

        // Then
        assertTrue(result);
    }

    @Test
    void testTagNameUpdate() {
        // Given
        String newTagName = "span";

        // When
        Element result = element.tagName(newTagName);

        // Then
        assertEquals(element, result);
        assertEquals(newTagName, element.tagName());
    }

    @Test
    void testTagUpdate() {
        // Given
        org.jsoup.parser.Tag newTag = org.jsoup.parser.Tag.valueOf("span", "", null);

        // When
        Element result = element.tag(newTag);

        // Then
        assertEquals(element, result);
        assertEquals(newTag, element.tag());
    }

    @Test
    void testIsBlock() {
        // Given
        org.jsoup.parser.Tag tag = org.jsoup.parser.Tag.valueOf("div", "", null);

        // When
        boolean result = element.isBlock();

        // Then
        assertTrue(result);
    }

    @Test
    void testId() {
        // Given
        String id = "test-id";

        // When
        element.id(id);

        // Then
        assertEquals(id, element.id());
    }

    @Test
    void testAttribute() {
        // Given
        String key = "test";
        String value = "value";

        // When
        element.attr(key, value);

        // Then
        assertNotNull(element.attribute(key));
    }

    @Test
    void testDataset() {
        // Given
        String key = "data-test";
        String value = "value";

        // When
        element.attr(key, value);

        // Then
        assertNotNull(element.dataset());
        assertEquals(value, element.dataset().get(key.substring(5)));
    }

    @Test
    void testParent() {
        // Given
        Element parent = new Element("div", "");

        // When
        parent.appendChild(element);

        // Then
        assertEquals(parent, element.parent());
    }

    @Test
    void testParents() {
        // Given
        Element parent = new Element("div", "");
        Element grandParent = new Element("div", "");

        // When
        grandParent.appendChild(parent);
        parent.appendChild(element);

        // Then
        assertEquals(2, element.parents().size());
        assertEquals(parent, element.parents().get(0));
        assertEquals(grandParent, element.parents().get(1));
    }

    @Test
    void testChild() {
        // Given
        Element child = new Element("div", "");

        // When
        element.appendChild(child);

        // Then
        assertEquals(child, element.child(0));
    }

    @Test
    void testChildrenSize() {
        // Given
        Element child = new Element("div", "");

        // When
        element.appendChild(child);

        // Then
        assertEquals(1, element.childrenSize());
    }

    @Test
    void testChildren() {
        // Given
        Element child = new Element("div", "");

        // When
        element.appendChild(child);

        // Then
        assertEquals(1, element.children().size());
        assertEquals(child, element.children().get(0));
    }

    @Test
    void testStream() {
        // Given
        Element child = new Element("div", "");

        // When
        element.appendChild(child);

        // Then
        assertEquals(2, element.stream().count());
    }

    @Test
    void testSelect() {
        // Given
        Element child = new Element("div", "");

        // When
        element.appendChild(child);

        // Then
        assertEquals(1, element.select("div").size());
    }

    @Test
    void testSelectFirst() {
        // Given
        Element child = new Element("div", "");

        // When
        element.appendChild(child);

        // Then
        assertEquals(child, element.selectFirst("div"));
    }

    @Test
    void testExpectFirst() {
        // Given
        Element child = new Element("div", "");

        // When
        element.appendChild(child);

        // Then
        assertEquals(child, element.expectFirst("div"));
    }

    @Test
    void testSelectNodes() {
        // Given
        Element child = new Element("div", "");

        // When
        element.appendChild(child);

        // Then
        assertEquals(1, element.selectNodes("div").size());
    }

    @Test
    void testIs() {
        // Given
        String cssQuery = "div";

        // When
        boolean result = element.is(cssQuery);

        // Then
        assertTrue(result);
    }

    @Test
    void testClosest() {
        // Given
        Element parent = new Element("div", "");
        parent.appendChild(element);

        // When
        Element result = element.closest("div");

        // Then
        assertEquals(parent, result);
    }

    @Test
    void testAppendChild() {
        // Given
        Node child = new TextNode("text");

        // When
        element.appendChild(child);

        // Then
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testAppendChildren() {
        // Given
        List<Node> children = new ArrayList<>();
        children.add(new TextNode("text1"));
        children.add(new TextNode("text2"));

        // When
        element.appendChildren(children);

        // Then
        assertEquals(2, element.childNodeSize());
    }

    @Test
    void testPrependChild() {
        // Given
        Node child = new TextNode("text");

        // When
        element.prependChild(child);

        // Then
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testPrependChildren() {
        // Given
        List<Node> children = new ArrayList<>();
        children.add(new TextNode("text1"));
        children.add(new TextNode("text2"));

        // When
        element.prependChildren(children);

        // Then
        assertEquals(2, element.childNodeSize());
    }

    @Test
    void testInsertChildren() {
        // Given
        List<Node> children = new ArrayList<>();
        children.add(new TextNode("text1"));
        children.add(new TextNode("text2"));

        // When
        element.insertChildren(0, children);

        // Then
        assertEquals(2, element.childNodeSize());
    }

    @Test
    void testAppendElement() {
        // Given
        String tagName = "span";

        // When
        Element result = element.appendElement(tagName);

        // Then
        assertEquals(element, result);
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testPrependElement() {
        // Given
        String tagName = "span";

        // When
        Element result = element.prependElement(tagName);

        // Then
        assertEquals(element, result);
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testAppendText() {
        // Given
        String text = "text";

        // When
        Element result = element.appendText(text);

        // Then
        assertEquals(element, result);
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testPrependText() {
        // Given
        String text = "text";

        // When
        Element result = element.prependText(text);

        // Then
        assertEquals(element, result);
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testAppend() {
        // Given
        String html = "<span>text</span>";

        // When
        Element result = element.append(html);

        // Then
        assertEquals(element, result);
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testPrepend() {
        // Given
        String html = "<span>text</span>";

        // When
        Element result = element.prepend(html);

        // Then
        assertEquals(element, result);
        assertEquals(1, element.childNodeSize());
    }

    @Test
    void testBefore() {
        // Given
        String html = "<span>text</span>";

        // When
        Element result = element.before(html);

        // Then
        assertEquals(element, result);
    }

    @Test
    void testAfter() {
        // Given
        String html = "<span>text</span>";

        // When
        Element result = element.after(html);

        // Then
        assertEquals(element, result);
    }

    @Test
    void testEmpty() {
        // Given
        Node child = new TextNode("text");
        element.appendChild(child);

        // When
        Element result = element.empty();

        // Then
        assertEquals(element, result);
        assertEquals(0, element.childNodeSize());
    }

    @Test
    void testWrap() {
        // Given
        String html = "<span>text</span>";

        // When
        Element result = element.wrap(html);

        // Then
        assertEquals(element, result);
    }

    @Test
    void testCssSelector() {
        // Given

        // When
        String result = element.cssSelector();

        // Then
        assertNotNull(result);
    }

    @Test
    void testSiblingElements() {
        // Given
        Element parent = new Element("div", "");
        parent.appendChild(element);
        Element sibling = new Element("span", "");
        parent.appendChild(sibling);

        // When
        Elements result = element.siblingElements();

        // Then
        assertEquals(1, result.size());
        assertEquals(sibling, result.get(0));
    }

    @Test
    void testNextElementSiblings() {
        // Given
        Element parent = new Element("div", "");
        parent.appendChild(element);
        Element sibling = new Element("span", "");
        parent.appendChild(sibling);

        // When
        Elements result = element.nextElementSiblings();

        // Then
        assertEquals(1, result.size());
        assertEquals(sibling, result.get(0));
    }

    @Test
    void testPreviousElementSiblings() {
        // Given
        Element parent = new Element("div", "");
        Element sibling = new Element("span", "");
        parent.appendChild(sibling);
        parent.appendChild(element);

        // When
        Elements result = element.previousElementSiblings();

        // Then
        assertEquals(1, result.size());
        assertEquals(sibling, result.get(0));
    }

    @Test
    void testFirstElementSibling() {
        // Given
        Element parent = new Element("div", "");
        Element sibling = new Element("span", "");
        parent.appendChild(sibling);
        parent.appendChild(element);

        // When
        Element result = element.firstElementSibling();

        // Then
        assertEquals(sibling, result);
    }

    @Test
    void testLastElementSibling() {
        // Given
        Element parent = new Element("div", "");
        Element sibling = new Element("span", "");
        parent.appendChild(element);
        parent.appendChild(sibling);

        // When
        Element result = element.lastElementSibling();

        // Then
        assertEquals(sibling, result);
    }

    @Test
    void testElementSiblingIndex() {
        // Given
        Element parent = new Element("div", "");
        Element sibling = new Element("span", "");
        parent.appendChild(sibling);
        parent.appendChild(element);

        // When
        int result = element.elementSiblingIndex();

        // Then
        assertEquals(1, result);
    }

    @Test
    void testFirstElementChild() {
        // Given
        Element child = new Element("span", "");
        element.appendChild(child);

        // When
        Element result = element.firstElementChild();

        // Then
        assertEquals(child, result);
    }

    @Test
    void testLastElementChild() {
        // Given
        Element child = new Element("span", "");
        element.appendChild(child);

        // When
        Element result = element.lastElementChild();

        // Then
        assertEquals(child, result);
    }

    @Test
    void testGetElementsByTag() {
        // Given
        Element child = new Element("span", "");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByTag("span");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementById() {
        // Given
        Element child = new Element("span", "");
        child.id("test-id");
        element.appendChild(child);

        // When
        Element result = element.getElementById("test-id");

        // Then
        assertEquals(child, result);
    }

    @Test
    void testGetElementsByClass() {
        // Given
        Element child = new Element("span", "");
        child.addClass("test-class");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByClass("test-class");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByAttribute() {
        // Given
        Element child = new Element("span", "");
        child.attr("test-attr", "test-value");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByAttribute("test-attr");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByAttributeStarting() {
        // Given
        Element child = new Element("span", "");
        child.attr("test-attr", "test-value");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByAttributeStarting("test-");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByAttributeValue() {
        // Given
        Element child = new Element("span", "");
        child.attr("test-attr", "test-value");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByAttributeValue("test-attr", "test-value");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByAttributeValueNot() {
        // Given
        Element child = new Element("span", "");
        child.attr("test-attr", "test-value");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByAttributeValueNot("test-attr", "test-value");

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void testGetElementsByAttributeValueStarting() {
        // Given
        Element child = new Element("span", "");
        child.attr("test-attr", "test-value");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByAttributeValueStarting("test-attr", "test-");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByAttributeValueEnding() {
        // Given
        Element child = new Element("span", "");
        child.attr("test-attr", "test-value");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByAttributeValueEnding("test-attr", "value");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByAttributeValueContaining() {
        // Given
        Element child = new Element("span", "");
        child.attr("test-attr", "test-value");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByAttributeValueContaining("test-attr", "test");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByAttributeValueMatching() {
        // Given
        Element child = new Element("span", "");
        child.attr("test-attr", "test-value");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByAttributeValueMatching("test-attr", "test-.*");

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByIndexLessThan() {
        // Given
        Element child = new Element("span", "");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByIndexLessThan(1);

        // Then
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }

    @Test
    void testGetElementsByIndexGreaterThan() {
        // Given
        Element child = new Element("span", "");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByIndexGreaterThan(0);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void testGetElementsByIndexEquals() {
        // Given
        Element child = new Element("span", "");
        element.appendChild(child);

        // When
        Elements result = element.getElementsByIndexEquals(0);

        // Then
        assertEquals(1, result