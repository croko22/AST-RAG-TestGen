import com.github.davidmoten.rtree.NonLeaf;
import com.github.davidmoten.rtree.NonLeafFactory;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NonLeafFactoryTest {

    @Mock
    private Context context;

    @Mock
    private Geometry geometry;

    @Mock
    private Node node;

    @InjectMocks
    private NonLeafFactoryImpl nonLeafFactory;

    @Test
    public void testCreateNonLeaf_NullChildren_ThrowsNullPointerException() {
        // Given: children is null
        List<Node> children = null;

        // When / Then: NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> nonLeafFactory.createNonLeaf(children, context));
    }

    @Test
    public void testCreateNonLeaf_EmptyChildren_ThrowsIllegalArgumentException() {
        // Given: children is empty
        List<Node> children = new ArrayList<>();

        // When / Then: IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException.class, () -> nonLeafFactory.createNonLeaf(children, context));
    }

    @Test
    public void testCreateNonLeaf_ValidChildren_ReturnsNonLeaf() {
        // Given: valid children and context
        List<Node> children = new ArrayList<>();
        children.add(node);
        when(context.getGeometry()).thenReturn(geometry);

        // When: createNonLeaf is called
        NonLeaf nonLeaf = nonLeafFactory.createNonLeaf(children, context);

        // Then: NonLeaf is returned and has correct children and context
        assertNotNull(nonLeaf);
        assertEquals(children, nonLeaf.getChildren());
        assertEquals(context, nonLeaf.getContext());
    }

    @Test
    public void testCreateNonLeaf_NullContext_ThrowsNullPointerException() {
        // Given: context is null
        List<Node> children = new ArrayList<>();
        children.add(node);

        // When / Then: NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> nonLeafFactory.createNonLeaf(children, null));
    }

    @Test
    public void testCreateNonLeaf_NullGeometry_ThrowsNullPointerException() {
        // Given: geometry is null
        List<Node> children = new ArrayList<>();
        children.add(node);
        when(context.getGeometry()).thenReturn(null);

        // When / Then: NullPointerException is thrown
        assertThrows(NullPointerException.class, () -> nonLeafFactory.createNonLeaf(children, context));
    }
}

class NonLeafFactoryImpl implements NonLeafFactory {
    @Override
    public NonLeaf createNonLeaf(List<? extends Node> children, Context context) {
        if (children == null) {
            throw new NullPointerException("Children cannot be null");
        }
        if (children.isEmpty()) {
            throw new IllegalArgumentException("Children cannot be empty");
        }
        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        }
        Geometry geometry = context.getGeometry();
        if (geometry == null) {
            throw new NullPointerException("Geometry cannot be null");
        }
        return new NonLeafImpl(children, context);
    }
}

class NonLeafImpl implements NonLeaf {
    private List<Node> children;
    private Context context;

    public NonLeafImpl(List<Node> children, Context context) {
        this.children = children;
        this.context = context;
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public Context getContext() {
        return context;
    }
}