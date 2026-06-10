import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormElementTest {

    @Mock
    private Element element;

    @Mock
    private Tag tag;

    @Mock
    private Document document;

    private FormElement formElement;

    @BeforeEach
    void setup() {
        formElement = new FormElement(tag, "baseUri", null);
    }

    @Test
    void testElements() {
        // Given
        Elements elements = mock(Elements.class);
        when(formElement.select(any(Evaluator.class))).thenReturn(elements);

        // When
        Elements result = formElement.elements();

        // Then
        assertEquals(elements, result);
        verify(formElement).select(any(Evaluator.class));
    }

    @Test
    void testAddElement() {
        // Given
        Element element = mock(Element.class);

        // When
        FormElement result = formElement.addElement(element);

        // Then
        assertEquals(formElement, result);
        verify(formElement).linkedEls.add(element);
    }

    @Test
    void testSubmit() {
        // Given
        Connection connection = mock(Connection.class);
        when(document.connection()).thenReturn(connection);
        when(formElement.ownerDocument()).thenReturn(document);
        when(formElement.absUrl("action")).thenReturn("actionUrl");
        when(formElement.attr("method")).thenReturn("POST");

        // When
        Connection result = formElement.submit();

        // Then
        assertEquals(connection, result);
        verify(formElement).absUrl("action");
        verify(formElement).attr("method");
        verify(connection).url("actionUrl");
        verify(connection).data(formElement.formData());
        verify(connection).method(Connection.Method.POST);
    }

    @Test
    void testSubmit_InvalidActionUrl() {
        // Given
        when(formElement.absUrl("action")).thenReturn(null);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> formElement.submit());
    }

    @Test
    void testFormData() {
        // Given
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        when(element1.tag()).thenReturn(tag);
        when(element2.tag()).thenReturn(tag);
        when(element1.attr("name")).thenReturn("name1");
        when(element2.attr("name")).thenReturn("name2");
        when(element1.val()).thenReturn("value1");
        when(element2.val()).thenReturn("value2");
        when(formElement.elements()).thenReturn(new Elements(element1, element2));

        // When
        List<Connection.KeyVal> result = formElement.formData();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("name1", result.get(0).key);
        assertEquals("value1", result.get(0).value);
        assertEquals("name2", result.get(1).key);
        assertEquals("value2", result.get(1).value);
    }

    @Test
    void testFormData_IgnoresDisabledElements() {
        // Given
        Element element = mock(Element.class);
        when(element.tag()).thenReturn(tag);
        when(element.attr("name")).thenReturn("name");
        when(element.val()).thenReturn("value");
        when(element.hasAttr("disabled")).thenReturn(true);
        when(formElement.elements()).thenReturn(new Elements(element));

        // When
        List<Connection.KeyVal> result = formElement.formData();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFormData_IgnoresElementsWithoutName() {
        // Given
        Element element = mock(Element.class);
        when(element.tag()).thenReturn(tag);
        when(element.attr("name")).thenReturn("");
        when(element.val()).thenReturn("value");
        when(formElement.elements()).thenReturn(new Elements(element));

        // When
        List<Connection.KeyVal> result = formElement.formData();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFormData_IgnoresButtonAndImageElements() {
        // Given
        Element element = mock(Element.class);
        when(element.tag()).thenReturn(tag);
        when(element.attr("name")).thenReturn("name");
        when(element.attr("type")).thenReturn("button");
        when(element.val()).thenReturn("value");
        when(formElement.elements()).thenReturn(new Elements(element));

        // When
        List<Connection.KeyVal> result = formElement.formData();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFormData_IncludesSelectElements() {
        // Given
        Element element = mock(Element.class);
        when(element.tag()).thenReturn(tag);
        when(element.attr("name")).thenReturn("name");
        when(element.nameIs("select")).thenReturn(true);
        when(formElement.elements()).thenReturn(new Elements(element));

        // When
        List<Connection.KeyVal> result = formElement.formData();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("name", result.get(0).key);
        assertEquals("", result.get(0).value);
    }

    @Test
    void testFormData_IncludesCheckboxAndRadioElements() {
        // Given
        Element element = mock(Element.class);
        when(element.tag()).thenReturn(tag);
        when(element.attr("name")).thenReturn("name");
        when(element.attr("type")).thenReturn("checkbox");
        when(element.hasAttr("checked")).thenReturn(true);
        when(element.val()).thenReturn("value");
        when(formElement.elements()).thenReturn(new Elements(element));

        // When
        List<Connection.KeyVal> result = formElement.formData();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("name", result.get(0).key);
        assertEquals("value", result.get(0).value);
    }

    @Test
    void testClone() {
        // When
        FormElement result = formElement.clone();

        // Then
        assertNotNull(result);
        assertNotSame(formElement, result);
    }
}