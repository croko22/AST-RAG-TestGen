import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisualizerTest {

    @Mock
    private RTree<?, Geometry> tree;

    @Mock
    private Rectangle view;

    private Visualizer visualizer;

    @BeforeEach
    void setup() {
        visualizer = new Visualizer(tree, 100, 100, view);
    }

    @Test
    void testCreateImage() {
        // Given
        when(tree.root()).thenReturn(Optional.empty());

        // When
        BufferedImage image = visualizer.createImage();

        // Then
        assertNotNull(image);
        assertEquals(100, image.getWidth());
        assertEquals(100, image.getHeight());
    }

    @Test
    void testCreateImage_WithRoot() {
        // Given
        Geometry geometry = mock(Geometry.class);
        when(geometry.mbr()).thenReturn(mock(Rectangle.class));
        when(tree.root()).thenReturn(Optional.of(mock(com.github.davidmoten.rtree.Node.class)));

        // When
        BufferedImage image = visualizer.createImage();

        // Then
        assertNotNull(image);
        assertEquals(100, image.getWidth());
        assertEquals(100, image.getHeight());
    }

    @Test
    void testSave() throws IOException {
        // Given
        File file = mock(File.class);

        // When
        visualizer.save(file, "PNG");

        // Then
        verifyStatic(ImageSaver.class);
        ImageSaver.save(any(BufferedImage.class), any(File.class), any(String.class));
    }

    @Test
    void testSave_StringFilename() throws IOException {
        // Given
        String filename = "test.png";

        // When
        visualizer.save(filename, "PNG");

        // Then
        verifyStatic(ImageSaver.class);
        ImageSaver.save(any(BufferedImage.class), any(File.class), any(String.class));
    }

    @Test
    void testSave_StringFilename_DefaultFormat() throws IOException {
        // Given
        String filename = "test.png";

        // When
        visualizer.save(filename);

        // Then
        verifyStatic(ImageSaver.class);
        ImageSaver.save(any(BufferedImage.class), any(File.class), eq("PNG"));
    }
}