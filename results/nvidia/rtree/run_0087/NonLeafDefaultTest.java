import com.github.davidmoten.guavamini.Preconditions;
import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
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
public class NonLeafDefaultTest {

    @Mock
    private Context context;

    @Mock
    private Node node1;

    @Mock
    private Node node2;

    private List<Node> children;

    @BeforeEach
    public void setup() {
        children = new ArrayList<>();
        children.add(node1);
        children.add(node2);
    }

    @Test
    public void testConstructor() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        assertNotNull(nonLeafDefault);
    }

    @Test
    public void testConstructor_EmptyChildrenList() {
        assertThrows(Preconditions.PreconditionException.class, () -> new NonLeafDefault(new ArrayList<>(), context));
    }

    @Test
    public void testGeometry() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        Geometry geometry = nonLeafDefault.geometry();
        assertNotNull(geometry);
    }

    @Test
    public void testSearchWithoutBackpressure() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        Func1 func1 = mock(Func1.class);
        Subscriber subscriber = mock(Subscriber.class);
        nonLeafDefault.searchWithoutBackpressure(func1, subscriber);
        verify(func1, times(1)).call(any());
        verify(subscriber, times(1)).onNext(any());
    }

    @Test
    public void testCount() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        int count = nonLeafDefault.count();
        assertEquals(2, count);
    }

    @Test
    public void testAdd() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        Entry entry = mock(Entry.class);
        List<Node> nodes = nonLeafDefault.add(entry);
        assertNotNull(nodes);
    }

    @Test
    public void testDelete() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        Entry entry = mock(Entry.class);
        NodeAndEntries nodeAndEntries = nonLeafDefault.delete(entry, true);
        assertNotNull(nodeAndEntries);
    }

    @Test
    public void testContext() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        Context context1 = nonLeafDefault.context();
        assertNotNull(context1);
    }

    @Test
    public void testChild() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        Node node = nonLeafDefault.child(0);
        assertNotNull(node);
    }

    @Test
    public void testChildren() {
        NonLeafDefault nonLeafDefault = new NonLeafDefault(children, context);
        List<Node> nodes = nonLeafDefault.children();
        assertNotNull(nodes);
    }
}