import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Leaf;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.ListPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeafHelperTest {

    @Mock
    private Leaf<String, Geometry> leaf;

    @Mock
    private Entry<String, Geometry> entry;

    @Mock
    private Context<String, Geometry> context;

    @Mock
    private Geometry geometry;

    @BeforeEach
    void setup() {
        when(leaf.context()).thenReturn(context);
        when(entry.geometry()).thenReturn(geometry);
        when(context.maxChildren()).thenReturn(5);
        when(context.minChildren()).thenReturn(2);
    }

    @Test
    public void testDelete_EntryNotFound() {
        // Given
        when(leaf.entries()).thenReturn(new ArrayList<>());
        // When
        NodeAndEntries<String, Geometry> result = LeafHelper.delete(entry, false, leaf);
        // Then
        assertNotNull(result);
        assertTrue(result.node().isPresent());
        assertEquals(leaf, result.node().get());
        assertTrue(result.entries().isEmpty());
        assertEquals(0, result.numDeleted());
    }

    @Test
    public void testDelete_EntryFound() {
        // Given
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(entry);
        when(leaf.entries()).thenReturn(entries);
        // When
        NodeAndEntries<String, Geometry> result = LeafHelper.delete(entry, false, leaf);
        // Then
        assertNotNull(result);
        assertTrue(result.node().isPresent());
        assertEquals(leaf, result.node().get());
        assertTrue(result.entries().isEmpty());
        assertEquals(1, result.numDeleted());
    }

    @Test
    public void testDelete_AllEntries() {
        // Given
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(entry);
        when(leaf.entries()).thenReturn(entries);
        // When
        NodeAndEntries<String, Geometry> result = LeafHelper.delete(entry, true, leaf);
        // Then
        assertNotNull(result);
        assertTrue(result.node().isPresent());
        assertEquals(leaf, result.node().get());
        assertTrue(result.entries().isEmpty());
        assertEquals(1, result.numDeleted());
    }

    @Test
    public void testAdd_Entry() {
        // Given
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        when(leaf.entries()).thenReturn(entries);
        // When
        List<Node<String, Geometry>> result = LeafHelper.add(entry, leaf);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        Node<String, Geometry> node = result.get(0);
        assertEquals(1, node.count());
    }

    @Test
    public void testAdd_EntrySplit() {
        // Given
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entries.add(entry);
        }
        when(leaf.entries()).thenReturn(entries);
        // When
        List<Node<String, Geometry>> result = LeafHelper.add(entry, leaf);
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testSearch_NoMatch() {
        // Given
        Func1<Geometry, Boolean> condition = geometry1 -> false;
        Subscriber<Entry<String, Geometry>> subscriber = mock(Subscriber.class);
        // When
        LeafHelper.search(condition, subscriber, leaf);
        // Then
        verify(subscriber, never()).onNext(any());
    }

    @Test
    public void testSearch_Match() {
        // Given
        Func1<Geometry, Boolean> condition = geometry1 -> true;
        Subscriber<Entry<String, Geometry>> subscriber = mock(Subscriber.class);
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(entry);
        when(leaf.entries()).thenReturn(entries);
        // When
        LeafHelper.search(condition, subscriber, leaf);
        // Then
        verify(subscriber).onNext(entry);
    }
}