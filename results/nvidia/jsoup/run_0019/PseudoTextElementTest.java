import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.PseudoTextElement;
import org.jsoup.parser.Tag;
import org.jsoup.internal.QuietAppendable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PseudoTextElementTest {

    @Mock
    private Tag tag;

    @Mock
    private QuietAppendable accum;

    @Mock
    private Document.OutputSettings out;

    private PseudoTextElement pseudoTextElement;

    @BeforeEach
    public void setup() {
        pseudoTextElement = new PseudoTextElement(tag, "baseUri", null);
    }

    @Test
    public void testConstructor() {
        // Given
        Tag tag = mock(Tag.class);
        String baseUri = "baseUri";
        Attributes attributes = null;

        // When
        PseudoTextElement pseudoTextElement = new PseudoTextElement(tag, baseUri, attributes);

        // Then
        assertNotNull(pseudoTextElement);
    }

    @Test
    public void testOuterHtmlHead() {
        // Given
        when(accum.append(any(CharSequence.class))).thenReturn(accum);

        // When
        pseudoTextElement.outerHtmlHead(accum, out);

        // Then
        verify(accum, never()).append(any(CharSequence.class));
    }

    @Test
    public void testOuterHtmlTail() {
        // Given
        when(accum.append(any(CharSequence.class))).thenReturn(accum);

        // When
        pseudoTextElement.outerHtmlTail(accum, out);

        // Then
        verify(accum, never()).append(any(CharSequence.class));
    }

    @Test
    public void testOuterHtmlHead_Appendable() {
        // Given
        Appendable appendable = mock(Appendable.class);
        QuietAppendable quietAppendable = QuietAppendable.wrap(appendable);

        // When
        pseudoTextElement.outerHtmlHead(quietAppendable, out);

        // Then
        verify(appendable, never()).append(any(CharSequence.class));
    }

    @Test
    public void testOuterHtmlTail_Appendable() {
        // Given
        Appendable appendable = mock(Appendable.class);
        QuietAppendable quietAppendable = QuietAppendable.wrap(appendable);

        // When
        pseudoTextElement.outerHtmlTail(quietAppendable, out);

        // Then
        verify(appendable, never()).append(any(CharSequence.class));
    }
}