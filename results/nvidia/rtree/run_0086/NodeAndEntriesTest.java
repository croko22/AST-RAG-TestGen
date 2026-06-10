import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.geometry.Geometry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NodeAndEntriesTest {

    @Mock
    private Node<String, Geometry> node;

    @Mock
    private Entry<String, Geometry> entry;

    @Mock
    private Geometry geometry;

    private NodeAndEntries<String, Geometry> nodeAndEntries;

    @BeforeEach
    void setup() {
        nodeAndEntries = new NodeAndEntries<>(Optional.of(node), new ArrayList<>(), 0);
    }

    @Test
    void testNode_Present() {
        // Given: node is present
        NodeAndEntries<String, Geometry> nodeAndEntries = new NodeAndEntries<>(Optional.of(node), new ArrayList<>(), 0);

        // When: node is retrieved
        Optional<? extends Node<String, Geometry>> retrievedNode = nodeAndEntries.node();

        // Then: node is present
        assertTrue(retrievedNode.isPresent());
        assertEquals(node, retrievedNode.get());
    }

    @Test
    void testNode_Absent() {
        // Given: node is absent
        NodeAndEntries<String, Geometry> nodeAndEntries = new NodeAndEntries<>(Optional.empty(), new ArrayList<>(), 0);

        // When: node is retrieved
        Optional<? extends Node<String, Geometry>> retrievedNode = nodeAndEntries.node();

        // Then: node is absent
        assertTrue(retrievedNode.isEmpty());
    }

    @Test
    void testEntriesToAdd() {
        // Given: entries are added
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(entry);
        NodeAndEntries<String, Geometry> nodeAndEntries = new NodeAndEntries<>(Optional.of(node), entries, 0);

        // When: entries are retrieved
        List<Entry<String, Geometry>> retrievedEntries = nodeAndEntries.entriesToAdd();

        // Then: entries are retrieved correctly
        assertEquals(entries, retrievedEntries);
    }

    @Test
    void testCountDeleted() {
        // Given: count deleted is set
        int countDeleted = 1;
        NodeAndEntries<String, Geometry> nodeAndEntries = new NodeAndEntries<>(Optional.of(node), new ArrayList<>(), countDeleted);

        // When: count deleted is retrieved
        int retrievedCountDeleted = nodeAndEntries.countDeleted();

        // Then: count deleted is retrieved correctly
        assertEquals(countDeleted, retrievedCountDeleted);
    }

    @Test
    void testConstructor_NodePresent() {
        // Given: node is present
        NodeAndEntries<String, Geometry> nodeAndEntries = new NodeAndEntries<>(Optional.of(node), new ArrayList<>(), 0);

        // Then: node is set correctly
        assertNotNull(nodeAndEntries.node());
        assertTrue(nodeAndEntries.node().isPresent());
        assertEquals(node, nodeAndEntries.node().get());
    }

    @Test
    void testConstructor_NodeAbsent() {
        // Given: node is absent
        NodeAndEntries<String, Geometry> nodeAndEntries = new NodeAndEntries<>(Optional.empty(), new ArrayList<>(), 0);

        // Then: node is set correctly
        assertNotNull(nodeAndEntries.node());
        assertTrue(nodeAndEntries.node().isEmpty());
    }

    @Test
    void testConstructor_Entries() {
        // Given: entries are added
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(entry);
        NodeAndEntries<String, Geometry> nodeAndEntries = new NodeAndEntries<>(Optional.of(node), entries, 0);

        // Then: entries are set correctly
        assertNotNull(nodeAndEntries.entriesToAdd());
        assertEquals(entries, nodeAndEntries.entriesToAdd());
    }

    @Test
    void testConstructor_CountDeleted() {
        // Given: count deleted is set
        int countDeleted = 1;
        NodeAndEntries<String, Geometry> nodeAndEntries = new NodeAndEntries<>(Optional.of(node), new ArrayList<>(), countDeleted);

        // Then: count deleted is set correctly
        assertEquals(countDeleted, nodeAndEntries.countDeleted());
    }
}