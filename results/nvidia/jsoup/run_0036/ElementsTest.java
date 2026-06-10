import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
public class ElementsTest {

    @Mock
    private Element element;

    @Mock
    private Document document;

    private Elements elements;

    @BeforeEach
    public void setup() {
        elements = new Elements();
    }

    @Test
    public void testClone() {
        // Given
        elements.add(element);

        // When
        Elements clonedElements = elements.clone();

        // Then
        assertNotNull(clonedElements);
        assertEquals(1, clonedElements.size());
        assertNotSame(elements, clonedElements);
    }

    @Test
    public void testAsList() {
        // Given
        elements.add(element);

        // When
        List<Element> list = elements.asList();

        // Then
        assertNotNull(list);
        assertEquals(1, list.size());
        assertSame(element, list.get(0));
    }

    @Test
    public void testAttr() {
        // Given
        when(element.attr("key")).thenReturn("value");
        elements.add(element);

        // When
        String attrValue = elements.attr("key");

        // Then
        assertEquals("value", attrValue);
    }

    @Test
    public void testHasAttr() {
        // Given
        when(element.hasAttr("key")).thenReturn(true);
        elements.add(element);

        // When
        boolean hasAttr = elements.hasAttr("key");

        // Then
        assertTrue(hasAttr);
    }

    @Test
    public void testEachAttr() {
        // Given
        when(element.attr("key")).thenReturn("value");
        when(element.hasAttr("key")).thenReturn(true);
        elements.add(element);

        // When
        List<String> attrValues = elements.eachAttr("key");

        // Then
        assertNotNull(attrValues);
        assertEquals(1, attrValues.size());
        assertEquals("value", attrValues.get(0));
    }

    @Test
    public void testAttr_SetAttr() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.attr("key", "value");

        // Then
        assertSame(elements, updatedElements);
        verify(element).attr("key", "value");
    }

    @Test
    public void testRemoveAttr() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.removeAttr("key");

        // Then
        assertSame(elements, updatedElements);
        verify(element).removeAttr("key");
    }

    @Test
    public void testAddClass() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.addClass("class");

        // Then
        assertSame(elements, updatedElements);
        verify(element).addClass("class");
    }

    @Test
    public void testRemoveClass() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.removeClass("class");

        // Then
        assertSame(elements, updatedElements);
        verify(element).removeClass("class");
    }

    @Test
    public void testToggleClass() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.toggleClass("class");

        // Then
        assertSame(elements, updatedElements);
        verify(element).toggleClass("class");
    }

    @Test
    public void testHasClass() {
        // Given
        when(element.hasClass("class")).thenReturn(true);
        elements.add(element);

        // When
        boolean hasClass = elements.hasClass("class");

        // Then
        assertTrue(hasClass);
    }

    @Test
    public void testVal() {
        // Given
        when(element.val()).thenReturn("value");
        elements.add(element);

        // When
        String val = elements.val();

        // Then
        assertEquals("value", val);
    }

    @Test
    public void testVal_SetVal() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.val("value");

        // Then
        assertSame(elements, updatedElements);
        verify(element).val("value");
    }

    @Test
    public void testText() {
        // Given
        when(element.text()).thenReturn("text");
        elements.add(element);

        // When
        String text = elements.text();

        // Then
        assertEquals("text", text);
    }

    @Test
    public void testHasText() {
        // Given
        when(element.hasText()).thenReturn(true);
        elements.add(element);

        // When
        boolean hasText = elements.hasText();

        // Then
        assertTrue(hasText);
    }

    @Test
    public void testEachText() {
        // Given
        when(element.hasText()).thenReturn(true);
        when(element.text()).thenReturn("text");
        elements.add(element);

        // When
        List<String> texts = elements.eachText();

        // Then
        assertNotNull(texts);
        assertEquals(1, texts.size());
        assertEquals("text", texts.get(0));
    }

    @Test
    public void testHtml() {
        // Given
        when(element.html()).thenReturn("html");
        elements.add(element);

        // When
        String html = elements.html();

        // Then
        assertEquals("html", html);
    }

    @Test
    public void testTagName() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.tagName("tag");

        // Then
        assertSame(elements, updatedElements);
        verify(element).tagName("tag");
    }

    @Test
    public void testHtml_SetHtml() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.html("html");

        // Then
        assertSame(elements, updatedElements);
        verify(element).html("html");
    }

    @Test
    public void testPrepend() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.prepend("html");

        // Then
        assertSame(elements, updatedElements);
        verify(element).prepend("html");
    }

    @Test
    public void testAppend() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.append("html");

        // Then
        assertSame(elements, updatedElements);
        verify(element).append("html");
    }

    @Test
    public void testBefore() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.before("html");

        // Then
        assertSame(elements, updatedElements);
        verify(element).before("html");
    }

    @Test
    public void testAfter() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.after("html");

        // Then
        assertSame(elements, updatedElements);
        verify(element).after("html");
    }

    @Test
    public void testWrap() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.wrap("html");

        // Then
        assertSame(elements, updatedElements);
        verify(element).wrap("html");
    }

    @Test
    public void testUnwrap() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.unwrap();

        // Then
        assertSame(elements, updatedElements);
        verify(element).unwrap();
    }

    @Test
    public void testEmpty() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.empty();

        // Then
        assertSame(elements, updatedElements);
        verify(element).empty();
    }

    @Test
    public void testRemove() {
        // Given
        elements.add(element);

        // When
        Elements updatedElements = elements.remove();

        // Then
        assertSame(elements, updatedElements);
        verify(element).remove();
    }

    @Test
    public void testSelect() {
        // Given
        elements.add(element);

        // When
        Elements selectedElements = elements.select("query");

        // Then
        assertNotNull(selectedElements);
    }

    @Test
    public void testSelectFirst() {
        // Given
        elements.add(element);

        // When
        Element selectedElement = elements.selectFirst("query");

        // Then
        assertNotNull(selectedElement);
    }

    @Test
    public void testExpectFirst() {
        // Given
        elements.add(element);

        // When
        Element selectedElement = elements.expectFirst("query");

        // Then
        assertNotNull(selectedElement);
    }

    @Test
    public void testNot() {
        // Given
        elements.add(element);

        // When
        Elements filteredElements = elements.not("query");

        // Then
        assertNotNull(filteredElements);
    }

    @Test
    public void testEq() {
        // Given
        elements.add(element);

        // When
        Elements filteredElements = elements.eq(0);

        // Then
        assertNotNull(filteredElements);
    }

    @Test
    public void testIs() {
        // Given
        when(element.is(any())).thenReturn(true);
        elements.add(element);

        // When
        boolean is = elements.is("query");

        // Then
        assertTrue(is);
    }

    @Test
    public void testNext() {
        // Given
        elements.add(element);

        // When
        Elements nextElements = elements.next();

        // Then
        assertNotNull(nextElements);
    }

    @Test
    public void testNext_Query() {
        // Given
        elements.add(element);

        // When
        Elements nextElements = elements.next("query");

        // Then
        assertNotNull(nextElements);
    }

    @Test
    public void testNextAll() {
        // Given
        elements.add(element);

        // When
        Elements nextElements = elements.nextAll();

        // Then
        assertNotNull(nextElements);
    }

    @Test
    public void testNextAll_Query() {
        // Given
        elements.add(element);

        // When
        Elements nextElements = elements.nextAll("query");

        // Then
        assertNotNull(nextElements);
    }

    @Test
    public void testPrev() {
        // Given
        elements.add(element);

        // When
        Elements prevElements = elements.prev();

        // Then
        assertNotNull(prevElements);
    }

    @Test
    public void testPrev_Query() {
        // Given
        elements.add(element);

        // When
        Elements prevElements = elements.prev("query");

        // Then
        assertNotNull(prevElements);
    }

    @Test
    public void testPrevAll() {
        // Given
        elements.add(element);

        // When
        Elements prevElements = elements.prevAll();

        // Then
        assertNotNull(prevElements);
    }

    @Test
    public void testPrevAll_Query() {
        // Given
        elements.add(element);

        // When
        Elements prevElements = elements.prevAll("query");

        // Then
        assertNotNull(prevElements);
    }

    @Test
    public void testParents() {
        // Given
        elements.add(element);

        // When
        Elements parents = elements.parents();

        // Then
        assertNotNull(parents);
    }

    @Test
    public void testFirst() {
        // Given
        elements.add(element);

        // When
        Element first = elements.first();

        // Then
        assertNotNull(first);
    }

    @Test
    public void testLast() {
        // Given
        elements.add(element);

        // When
        Element last = elements.last();

        // Then
        assertNotNull(last);
    }

    @Test
    public void testTraverse() {
        // Given
        elements.add(element);

        // When
        Elements traversedElements = elements.traverse(mock(NodeVisitor.class));

        // Then
        assertSame(elements, traversedElements);
    }

    @Test
    public void testFilter() {
        // Given
        elements.add(element);

        // When
        Elements filteredElements = elements.filter(mock(NodeFilter.class));

        // Then
        assertSame(elements, filteredElements);
    }

    @Test
    public void testForms() {
        // Given
        elements.add(element);

        // When
        List<FormElement> forms = elements.forms();

        // Then
        assertNotNull(forms);
    }

    @Test
    public void testComments() {
        // Given
        elements.add(element);

        // When
        List<Comment> comments = elements.comments();

        // Then
        assertNotNull(comments);
    }

    @Test
    public void testTextNodes() {
        // Given
        elements.add(element);

        // When
        List<TextNode> textNodes = elements.textNodes();

        // Then
        assertNotNull(textNodes);
    }

    @Test
    public void testDataNodes() {
        // Given
        elements.add(element);

        // When
        List<DataNode> dataNodes = elements.dataNodes();

        // Then
        assertNotNull(dataNodes);
    }

    @Test
    public void testSet() {
        // Given
        elements.add(element);

        // When
        Element replacedElement = elements.set(0, mock(Element.class));

        // Then
        assertNotNull(replacedElement);
    }

    @Test
    public void testRemove() {
        // Given
        elements.add(element);

        // When
        Element removedElement = elements.remove(0);

        // Then
        assertNotNull(removedElement);
    }

    @Test
    public void testDeselect() {
        // Given
        elements.add(element);

        // When
        Element deselectedElement = elements.deselect(0);

        // Then
        assertNotNull(deselectedElement);
    }
}