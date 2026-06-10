import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class XmlTreeBuilderTest {

    @InjectMocks
    private XmlTreeBuilder xmlTreeBuilder;

    @Mock
    private Parser parser;

    @BeforeEach
    public void setup() {
        // Initialize the parser with the XmlTreeBuilder
        when(parser.getBuilder()).thenReturn(xmlTreeBuilder);
    }

    @Test
    public void testDefaultNamespace() {
        // Given: XmlTreeBuilder instance
        // When: defaultNamespace method is called
        String defaultNamespace = xmlTreeBuilder.defaultNamespace();
        // Then: default namespace should be "http://www.w3.org/XML/1998/namespace"
        assertEquals("http://www.w3.org/XML/1998/namespace", defaultNamespace);
    }

    @Test
    public void testParse() {
        // Given: XML string and base URI
        String xmlString = "<root><person><name>John</name><age>30</age></person></root>";
        String baseUri = "https://example.com";
        // When: parse method is called
        Document document = Jsoup.parse(xmlString, baseUri, Parser.xmlParser());
        // Then: document should not be null
        assertNotNull(document);
        // And: document should have the expected structure
        Element root = document.select("root").first();
        assertNotNull(root);
        Element person = root.select("person").first();
        assertNotNull(person);
        Element name = person.select("name").first();
        assertNotNull(name);
        assertEquals("John", name.text());
        Element age = person.select("age").first();
        assertNotNull(age);
        assertEquals("30", age.text());
    }

    @Test
    public void testParseFragment() {
        // Given: XML string and base URI
        String xmlString = "<person><name>John</name><age>30</age></person>";
        String baseUri = "https://example.com";
        // When: parse method is called
        Document document = Jsoup.parse(xmlString, baseUri, Parser.xmlParser());
        // Then: document should not be null
        assertNotNull(document);
        // And: document should have the expected structure
        Element person = document.select("person").first();
        assertNotNull(person);
        Element name = person.select("name").first();
        assertNotNull(name);
        assertEquals("John", name.text());
        Element age = person.select("age").first();
        assertNotNull(age);
        assertEquals("30", age.text());
    }

    @Test
    public void testInvalidXml() {
        // Given: invalid XML string
        String xmlString = "<root><person><name>John</name><age>30</age>";
        String baseUri = "https://example.com";
        // When: parse method is called
        assertThrows(org.jsoup.parser.ParseError.class, () -> Jsoup.parse(xmlString, baseUri, Parser.xmlParser()));
    }
}