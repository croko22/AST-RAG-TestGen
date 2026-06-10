Here's a comprehensive test class for the provided RTree class:

```java
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Geometry;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.Line;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
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
public class RTreeTest {

    @Mock
    private Geometry geometry;

    @Mock
    private Node node;

    private RTree<String, Geometry> rTree;

    @BeforeEach
    void setup() {
        rTree = RTree.create();
    }

    @Test
    void testCreate() {
        // Given
        RTree<String, Geometry> rTree = RTree.create();

        // Then
        assertNotNull(rTree);
    }

    @Test
    void testCreateWithEntries() {
        // Given
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(new Entry<>("value", geometry));

        // When
        RTree<String, Geometry> rTree = RTree.create(entries);

        // Then
        assertNotNull(rTree);
    }

    @Test
    void testCalculateDepth() {
        // Given
        rTree = rTree.add(new Entry<>("value", geometry));

        // When
        int depth = rTree.calculateDepth();

        // Then
        assertEquals(1, depth);
    }

    @Test
    void testMinChildren() {
        // Given
        RTree.Builder<String, Geometry> builder = RTree.minChildren(10);

        // When
        RTree<String, Geometry> rTree = builder.create();

        // Then
        assertNotNull(rTree);
    }

    @Test
    void testMaxChildren() {
        // Given
        RTree.Builder<String, Geometry> builder = RTree.maxChildren(10);

        // When
        RTree<String, Geometry> rTree = builder.create();

        // Then
        assertNotNull(rTree);
    }

    @Test
    void testSplitter() {
        // Given
        RTree.Builder<String, Geometry> builder = RTree.splitter(mock(com.github.davidmoten.rtree.Splitter.class));

        // When
        RTree<String, Geometry> rTree = builder.create();

        // Then
        assertNotNull(rTree);
    }

    @Test
    void testSelector() {
        // Given
        RTree.Builder<String, Geometry> builder = RTree.selector(mock(com.github.davidmoten.rtree.Selector.class));

        // When
        RTree<String, Geometry> rTree = builder.create();

        // Then
        assertNotNull(rTree);
    }

    @Test
    void testStar() {
        // Given
        RTree.Builder<String, Geometry> builder = RTree.star();

        // When
        RTree<String, Geometry> rTree = builder.create();

        // Then
        assertNotNull(rTree);
    }

    @Test
    void testAdd() {
        // Given
        Entry<String, Geometry> entry = new Entry<>("value", geometry);

        // When
        RTree<String, Geometry> newRTree = rTree.add(entry);

        // Then
        assertNotNull(newRTree);
    }

    @Test
    void testAddValueAndGeometry() {
        // Given
        String value = "value";
        Geometry geometry = mock(Geometry.class);

        // When
        RTree<String, Geometry> newRTree = rTree.add(value, geometry);

        // Then
        assertNotNull(newRTree);
    }

    @Test
    void testAddEntries() {
        // Given
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(new Entry<>("value", geometry));

        // When
        RTree<String, Geometry> newRTree = rTree.add(entries);

        // Then
        assertNotNull(newRTree);
    }

    @Test
    void testDelete() {
        // Given
        Entry<String, Geometry> entry = new Entry<>("value", geometry);
        rTree = rTree.add(entry);

        // When
        RTree<String, Geometry> newRTree = rTree.delete(entry);

        // Then
        assertNotNull(newRTree);
    }

    @Test
    void testDeleteValueAndGeometry() {
        // Given
        String value = "value";
        Geometry geometry = mock(Geometry.class);
        rTree = rTree.add(value, geometry);

        // When
        RTree<String, Geometry> newRTree = rTree.delete(value, geometry);

        // Then
        assertNotNull(newRTree);
    }

    @Test
    void testSearch() {
        // Given
        Rectangle rectangle = mock(Rectangle.class);

        // When
        rTree.search(rectangle);

        // Then
        verify(geometry, never()).intersects(rectangle);
    }

    @Test
    void testSearchPoint() {
        // Given
        Point point = mock(Point.class);

        // When
        rTree.search(point);

        // Then
        verify(geometry, never()).intersects(point);
    }

    @Test
    void testSearchCircle() {
        // Given
        Circle circle = mock(Circle.class);

        // When
        rTree.search(circle);

        // Then
        verify(geometry, never()).intersects(circle);
    }

    @Test
    void testSearchLine() {
        // Given
        Line line = mock(Line.class);

        // When
        rTree.search(line);

        // Then
        verify(geometry, never()).intersects(line);
    }

    @Test
    void testEntries() {
        // Given

        // When
        rTree.entries();

        // Then
        verify(geometry, never()).intersects(any());
    }
}
```

This test class covers most of the public methods in the RTree class. Note that some methods are not tested because they are not public or are not relevant to the RTree class. Also, some tests are not comprehensive because the RTree class is complex and has many dependencies. You may need to add more tests to cover all the scenarios. 

Please note that you need to have the necessary dependencies in your project to run these tests. The dependencies include JUnit 5, Mockito, and the RTree library. 

Also, remember to replace the mock objects with the actual objects if you are not using a mocking framework. 

This test class should give you a good starting point for testing the RTree class. You can add more tests as needed to cover all the scenarios.