import net.hydromatic.morel.util.MorelHighlighter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MorelHighlighterTest {

    @Mock
    private MorelHighlighter.Sink sink;

    private MorelHighlighter highlighter;

    @BeforeEach
    void setup() {
        highlighter = new MorelHighlighter(Set.of());
    }

    @Test
    public void testAmendKeywords() {
        // Given
        Function<Set<String>, Iterable<String>> fn = keywords -> Set.of("newKeyword");

        // When
        MorelHighlighter newHighlighter = highlighter.amendKeywords(fn);

        // Then
        assertNotNull(newHighlighter);
        assertEquals(Set.of("newKeyword"), newHighlighter.keywords);
    }

    @Test
    public void testHighlightInput() {
        // Given
        String code = "val x = 5";

        // When
        String highlightedCode = highlighter.highlightInput(code);

        // Then
        assertNotNull(highlightedCode);
        assertEquals("<span class=\"kr\">val</span> <span class=\"nv\">x</span> <span class=\"p\">=</span> <span class=\"mi\">5</span>", highlightedCode);
    }

    @Test
    public void testHighlightRouge() {
        // Given
        String code = "val x = 5";

        // When
        String highlightedCode = highlighter.highlightRouge(code);

        // Then
        assertNotNull(highlightedCode);
        assertEquals("<div class=\"language-sml highlighter-rouge\"><div class=\"highlight\"><pre class=\"highlight\"><code><span class=\"kr\">val</span> <span class=\"nv\">x</span> <span class=\"p\">=</span> <span class=\"mi\">5</span></code></pre></div></div>", highlightedCode);
    }

    @Test
    public void testHighlightRouge2() {
        // Given
        String code = "val x = 5";

        // When
        String highlightedCode = highlighter.highlightRouge2(code);

        // Then
        assertNotNull(highlightedCode);
        assertEquals("kr{val} nv{x} p{=} mi{5}", highlightedCode);
    }

    @Test
    public void testHighlightOutput() {
        // Given
        String text = "<hello> world";

        // When
        String highlightedText = highlighter.highlightOutput(text);

        // Then
        assertNotNull(highlightedText);
        assertEquals("&lt;hello&gt; world", highlightedText);
    }

    @Test
    public void testHighlightCode() {
        // Given
        String code = "val x = 5";

        // When
        highlighter.highlightCode(code, sink);

        // Then
        verify(sink, times(1)).kr(anyInt(), anyInt());
        verify(sink, times(1)).nv(anyInt(), anyInt());
        verify(sink, times(1)).p(anyInt(), anyInt());
        verify(sink, times(1)).mi(anyInt(), anyInt());
    }

    @Test
    public void testPlain() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.plain(start, end);

        // Then
        // No-op
    }

    @Test
    public void testKr() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.kr(start, end);

        // Then
        // No-op
    }

    @Test
    public void testS() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.s(start, end);

        // Then
        // No-op
    }

    @Test
    public void testC() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.c(start, end);

        // Then
        // No-op
    }

    @Test
    public void testCm() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.cm(start, end);

        // Then
        // No-op
    }

    @Test
    public void testCt() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.ct(start, end);

        // Then
        // No-op
    }

    @Test
    public void testN() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.n(start, end);

        // Then
        // No-op
    }

    @Test
    public void testO() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.o(start, end);

        // Then
        // No-op
    }

    @Test
    public void testNv() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.nv(start, end);

        // Then
        // No-op
    }

    @Test
    public void testNf() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.nf(start, end);

        // Then
        // No-op
    }

    @Test
    public void testId() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.id(start, end);

        // Then
        // No-op
    }

    @Test
    public void testP() {
        // Given
        int start = 0;
        int end = 5;

        // When
        highlighter.p(start, end);

        // Then
        // No-op
    }
}