import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentTest {

    @Mock
    private Parser parser;

    private Comment comment;

    @BeforeEach
    public void setup() {
        comment = new Comment("Test comment");
    }

    @Test
    public void testNodeName() {
        // When: get node name
        String nodeName = comment.nodeName();

        // Then: verify node name
        assertEquals("#comment", nodeName);
    }

    @Test
    public void testGetData() {
        // When: get comment data
        String data = comment.getData();

        // Then: verify comment data
        assertEquals("Test comment", data);
    }

    @Test
    public void testSetData() {
        // Given: new comment data
        String newData = "New comment";

        // When: set comment data
        Comment updatedComment = comment.setData(newData);

        // Then: verify comment data and updated comment
        assertEquals(newData, updatedComment.getData());
        assertSame(comment, updatedComment);
    }

    @Test
    public void testClone() {
        // When: clone comment
        Comment clonedComment = comment.clone();

        // Then: verify cloned comment
        assertEquals(comment.getData(), clonedComment.getData());
        assertNotSame(comment, clonedComment);
    }

    @Test
    public void testIsXmlDeclaration_False() {
        // Given: comment data that is not an XML declaration
        comment = new Comment("Not an XML declaration");

        // When: check if comment is an XML declaration
        boolean isXmlDeclaration = comment.isXmlDeclaration();

        // Then: verify result
        assertFalse(isXmlDeclaration);
    }

    @Test
    public void testIsXmlDeclaration_True() {
        // Given: comment data that is an XML declaration
        comment = new Comment("<?xml version=\"1.0\"?>");

        // When: check if comment is an XML declaration
        boolean isXmlDeclaration = comment.isXmlDeclaration();

        // Then: verify result
        assertTrue(isXmlDeclaration);
    }

    @Test
    public void testAsXmlDeclaration_Null() {
        // Given: comment data that is not an XML declaration
        comment = new Comment("Not an XML declaration");

        // When: attempt to cast comment to XML declaration
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        // Then: verify result
        assertNull(xmlDeclaration);
    }

    @Test
    public void testAsXmlDeclaration_NotNull() {
        // Given: comment data that is an XML declaration and parser that can parse it
        comment = new Comment("<?xml version=\"1.0\"?>");
        when(parser.parseFragmentInput("<" + comment.getData() + ">", null, "")).thenReturn(List.of(new XmlDeclaration()));

        // When: attempt to cast comment to XML declaration
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        // Then: verify result
        assertNotNull(xmlDeclaration);
    }
}