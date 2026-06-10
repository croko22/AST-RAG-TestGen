import net.hydromatic.morel.Darn;
import net.hydromatic.morel.Darn.Command;
import net.hydromatic.morel.Darn.Attrs;
import net.hydromatic.morel.Darn.Segment;
import net.hydromatic.morel.Darn.ProcessResult;
import net.hydromatic.morel.Darn.ProbeResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DarnTest {

    @Mock
    private Supplier<Darn.Kernel> kernelSupplier;

    @Mock
    private Darn.Kernel kernel;

    private File tempFile;

    @BeforeEach
    void setup() throws IOException {
        tempFile = Files.createTempFile("darn-test", ".md").toFile();
        when(kernelSupplier.get()).thenReturn(kernel);
    }

    @Test
    void testProcess_FileDoesNotExist_ThrowsIOException() {
        // Given
        File nonExistingFile = new File("non-existing-file.md");

        // When and Then
        assertThrows(IOException.class, () -> Darn.process(nonExistingFile, false, kernelSupplier, false));
    }

    @Test
    void testProcess_EmptyFile_ReturnsFalse() throws IOException {
        // Given
        Files.write(tempFile.toPath(), "".getBytes());

        // When
        boolean result = Darn.process(tempFile, false, kernelSupplier, false);

        // Then
        assertFalse(result);
    }

    @Test
    void testProcess_FileWithNoCells_ReturnsFalse() throws IOException {
        // Given
        Files.write(tempFile.toPath(), "Hello World!".getBytes());

        // When
        boolean result = Darn.process(tempFile, false, kernelSupplier, false);

        // Then
        assertFalse(result);
    }

    @Test
    void testProcess_FileWithCells_ProcessesCells() throws IOException {
        // Given
        String content = "<!-- morel run -->\n" +
                "println(\"Hello World!\")\n" +
                "-->\n";
        Files.write(tempFile.toPath(), content.getBytes());

        // When
        boolean result = Darn.process(tempFile, false, kernelSupplier, false);

        // Then
        assertTrue(result);
        verify(kernel, times(1)).execute(any());
    }

    @Test
    void testProbe_FileDoesNotExist_ThrowsIOException() {
        // Given
        File nonExistingFile = new File("non-existing-file.md");

        // When and Then
        assertThrows(IOException.class, () -> Darn.probe(nonExistingFile, System.out, kernelSupplier));
    }

    @Test
    void testProbe_EmptyFile_DoesNothing() throws IOException {
        // Given
        Files.write(tempFile.toPath(), "".getBytes());

        // When
        Darn.probe(tempFile, System.out, kernelSupplier);

        // Then
        verifyNoInteractions(kernel);
    }

    @Test
    void testProbe_FileWithNoCells_DoesNothing() throws IOException {
        // Given
        Files.write(tempFile.toPath(), "Hello World!".getBytes());

        // When
        Darn.probe(tempFile, System.out, kernelSupplier);

        // Then
        verifyNoInteractions(kernel);
    }

    @Test
    void testProbe_FileWithCells_ProbesCells() throws IOException {
        // Given
        String content = "<!-- morel skip -->\n" +
                "println(\"Hello World!\")\n" +
                "-->\n";
        Files.write(tempFile.toPath(), content.getBytes());

        // When
        Darn.probe(tempFile, System.out, kernelSupplier);

        // Then
        verify(kernel, times(1)).execute(any());
    }

    @Test
    void testParseAttrs_RunCommand() {
        // Given
        String openingLine = "<!-- morel run -->";

        // When
        Attrs attrs = Darn.parseAttrs(openingLine);

        // Then
        assertEquals(Command.RUN, attrs.command);
        assertFalse(attrs.noOutput);
        assertFalse(attrs.fail);
        assertEquals("default", attrs.env);
    }

    @Test
    void testParseAttrs_SilentCommand() {
        // Given
        String openingLine = "<!-- morel silent -->";

        // When
        Attrs attrs = Darn.parseAttrs(openingLine);

        // Then
        assertEquals(Command.SILENT, attrs.command);
        assertFalse(attrs.noOutput);
        assertFalse(attrs.fail);
        assertEquals("default", attrs.env);
    }

    @Test
    void testParseAttrs_SkipCommand() {
        // Given
        String openingLine = "<!-- morel skip -->";

        // When
        Attrs attrs = Darn.parseAttrs(openingLine);

        // Then
        assertEquals(Command.SKIP, attrs.command);
        assertFalse(attrs.noOutput);
        assertFalse(attrs.fail);
        assertEquals("default", attrs.env);
    }

    @Test
    void testParseAttrs_NoOutputFlag() {
        // Given
        String openingLine = "<!-- morel run no-output -->";

        // When
        Attrs attrs = Darn.parseAttrs(openingLine);

        // Then
        assertEquals(Command.RUN, attrs.command);
        assertTrue(attrs.noOutput);
        assertFalse(attrs.fail);
        assertEquals("default", attrs.env);
    }

    @Test
    void testParseAttrs_FailFlag() {
        // Given
        String openingLine = "<!-- morel run fail -->";

        // When
        Attrs attrs = Darn.parseAttrs(openingLine);

        // Then
        assertEquals(Command.RUN, attrs.command);
        assertFalse(attrs.noOutput);
        assertTrue(attrs.fail);
        assertEquals("default", attrs.env);
    }

    @Test
    void testParseAttrs_EnvFlag() {
        // Given
        String openingLine = "<!-- morel run env=my-env -->";

        // When
        Attrs attrs = Darn.parseAttrs(openingLine);

        // Then
        assertEquals(Command.RUN, attrs.command);
        assertFalse(attrs.noOutput);
        assertFalse(attrs.fail);
        assertEquals("my-env", attrs.env);
    }

    @Test
    void testParseSegments_SingleSegment() {
        // Given
        List<String> contentLines = Arrays.asList(
                "println(\"Hello World!\")",
                "> Hello World!"
        );

        // When
        List<Segment> segments = Darn.parseSegments(contentLines);

        // Then
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(Arrays.asList("println(\"Hello World!\")"), segment.input);
        assertEquals(Arrays.asList("Hello World!"), segment.output);
    }

    @Test
    void testParseSegments_MultipleSegments() {
        // Given
        List<String> contentLines = Arrays.asList(
                "println(\"Hello World!\")",
                "> Hello World!",
                "println(\"Goodbye World!\")",
                "> Goodbye World!"
        );

        // When
        List<Segment> segments = Darn.parseSegments(contentLines);

        // Then
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(Arrays.asList("println(\"Hello World!\")"), segment1.input);
        assertEquals(Arrays.asList("Hello World!"), segment1.output);
        Segment segment2 = segments.get(1);
        assertEquals(Arrays.asList("println(\"Goodbye World!\")"), segment2.input);
        assertEquals(Arrays.asList("Goodbye World!"), segment2.output);
    }
}