import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Factory;
import com.github.davidmoten.rtree.Leaf;
import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.geometry.Geometry;
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
public class FactoryDefaultTest {

    @Mock
    private Context<String, Geometry> context;

    @Mock
    private Geometry geometry;

    @Mock
    private Entry<String, Geometry> entry;

    private FactoryDefault<String, Geometry> factory;

    @BeforeEach
    public void setup() {
        factory = FactoryDefault.instance();
    }

    @Test
    public void testInstance() {
        // Given: no setup needed
        // When: get instance of FactoryDefault
        FactoryDefault<String, Geometry> instance = FactoryDefault.instance();
        // Then: verify instance is not null
        assertNotNull(instance);
    }

    @Test
    public void testCreateLeaf() {
        // Given: setup context and entries
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(entry);
        // When: create leaf
        Leaf<String, Geometry> leaf = factory.createLeaf(entries, context);
        // Then: verify leaf is not null
        assertNotNull(leaf);
    }

    @Test
    public void testCreateNonLeaf() {
        // Given: setup context and children
        List<Node<String, Geometry>> children = new ArrayList<>();
        Node<String, Geometry> node = mock(Node.class);
        children.add(node);
        // When: create non-leaf
        NonLeaf<String, Geometry> nonLeaf = factory.createNonLeaf(children, context);
        // Then: verify non-leaf is not null
        assertNotNull(nonLeaf);
    }

    @Test
    public void testCreateEntry() {
        // Given: setup value and geometry
        String value = "test";
        // When: create entry
        Entry<String, Geometry> createdEntry = factory.createEntry(value, geometry);
        // Then: verify entry is not null
        assertNotNull(createdEntry);
    }

    @Test
    public void testCreateEntry_NullValue() {
        // Given: setup null value and geometry
        String value = null;
        // When: create entry
        Entry<String, Geometry> createdEntry = factory.createEntry(value, geometry);
        // Then: verify entry is not null
        assertNotNull(createdEntry);
    }

    @Test
    public void testCreateEntry_NullGeometry() {
        // Given: setup value and null geometry
        String value = "test";
        Geometry geometry = null;
        // When: create entry
        Entry<String, Geometry> createdEntry = factory.createEntry(value, geometry);
        // Then: verify entry is not null
        assertNotNull(createdEntry);
    }
}