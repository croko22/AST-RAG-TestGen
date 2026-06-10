import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Leaf;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeafDefaultTest {

    @Mock
    private Context<String, Rectangle> context;

    @Mock
    private Entry<String, Rectangle> entry;

    @Mock
    private Rectangle rectangle;

    private List<Entry<String, Rectangle>> entries;

    private LeafDefault<String, Rectangle> leafDefault;

    @BeforeEach
    void setup() {
        entries = new ArrayList<>();
        entries.add(entry);
        leafDefault = new LeafDefault<>(entries, context);
    }

    @Test
    void testGeometry() {
        // Given
        when(Util.mbr(entries)).thenReturn(rectangle);

        // When
        Geometry geometry = leafDefault.geometry();

        // Then
        assertEquals(rectangle, geometry);
    }

    @Test
    void testEntries() {
        // When
        List<Entry<String, Rectangle>> result = leafDefault.entries();

        // Then
        assertEquals(entries, result);
    }

    @Test
    void testSearchWithoutBackpressure() {
        // Given
        Function<Geometry, Boolean> condition = geometry -> true;
        Subscriber<Entry<String, Rectangle>> subscriber = mock(Subscriber.class);

        // When
        leafDefault.searchWithoutBackpressure(condition, subscriber);

        // Then
        verifyStatic(LeafHelper.class);
        LeafHelper.search(any(), any(), any());
    }

    @Test
    void testCount() {
        // When
        int count = leafDefault.count();

        // Then
        assertEquals(entries.size(), count);
    }

    @Test
    void testAdd() {
        // Given
        Entry<String, Rectangle> newEntry = mock(Entry.class);

        // When
        List<Node<String, Rectangle>> result = leafDefault.add(newEntry);

        // Then
        verifyStatic(LeafHelper.class);
        LeafHelper.add(any(), any());
    }

    @Test
    void testDelete() {
        // Given
        Entry<String, Rectangle> entryToDelete = mock(Entry.class);
        boolean all = true;

        // When
        NodeAndEntries<String, Rectangle> result = leafDefault.delete(entryToDelete, all);

        // Then
        verifyStatic(LeafHelper.class);
        LeafHelper.delete(any(), anyBoolean(), any());
    }

    @Test
    void testContext() {
        // When
        Context<String, Rectangle> result = leafDefault.context();

        // Then
        assertEquals(context, result);
    }

    @Test
    void testEntry() {
        // Given
        int index = 0;

        // When
        Entry<String, Rectangle> result = leafDefault.entry(index);

        // Then
        assertEquals(entries.get(index), result);
    }

    @Test
    void testEntryIndexOutOfBoundsException() {
        // Given
        int index = -1;

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> leafDefault.entry(index));
    }
}