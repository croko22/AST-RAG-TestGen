import com.github.davidmoten.guavamini.Preconditions;
import com.github.davidmoten.rtree.Context;
import com.github.davidmoten.rtree.Factory;
import com.github.davidmoten.rtree.Splitter;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.Selector;
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
public class ContextTest {

    @Mock
    private Splitter splitter;

    @Mock
    private Selector selector;

    @Mock
    private Factory<String, Geometry> factory;

    private Context<String, Geometry> context;

    @BeforeEach
    void setup() {
        context = new Context<>(1, 3, selector, splitter, factory);
    }

    @Test
    public void testConstructor_MinChildrenLessThanOne_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Context<>(0, 3, selector, splitter, factory));
    }

    @Test
    public void testConstructor_MaxChildrenLessThanThree_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Context<>(1, 2, selector, splitter, factory));
    }

    @Test
    public void testConstructor_MinChildrenGreaterThanMaxChildren_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Context<>(4, 3, selector, splitter, factory));
    }

    @Test
    public void testConstructor_NullSplitter_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Context<>(1, 3, selector, null, factory));
    }

    @Test
    public void testConstructor_NullSelector_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Context<>(1, 3, null, splitter, factory));
    }

    @Test
    public void testConstructor_NullFactory_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Context<>(1, 3, selector, splitter, null));
    }

    @Test
    public void testMaxChildren() {
        assertEquals(3, context.maxChildren());
    }

    @Test
    public void testMinChildren() {
        assertEquals(1, context.minChildren());
    }

    @Test
    public void testSplitter() {
        assertSame(splitter, context.splitter());
    }

    @Test
    public void testSelector() {
        assertSame(selector, context.selector());
    }

    @Test
    public void testFactory() {
        assertSame(factory, context.factory());
    }
}