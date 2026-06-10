package com.github.davidmoten.rtree;

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
public class LeafFactoryTest {

    @Mock
    private Context<String, Geometry> context;

    @Mock
    private Geometry geometry;

    @Mock
    private Rectangle rectangle;

    private LeafFactory<String, Geometry> leafFactory;

    @BeforeEach
    public void setup() {
        leafFactory = new LeafFactory<String, Geometry>() {
            @Override
            public Leaf<String, Geometry> createLeaf(List<Entry<String, Geometry>> entries, Context<String, Geometry> context) {
                return new Leaf<String, Geometry>() {
                    @Override
                    public List<Entry<String, Geometry>> entries() {
                        return entries;
                    }

                    @Override
                    public Context<String, Geometry> context() {
                        return context;
                    }
                };
            }
        };
    }

    @Test
    public void testCreateLeaf_NullEntries_ThrowsNullPointerException() {
        // Given: entries es null
        List<Entry<String, Geometry>> entries = null;

        // When / Then: se espera una excepcion al intentar crear
        assertThrows(NullPointerException.class, () -> {
            leafFactory.createLeaf(entries, context);
        });
    }

    @Test
    public void testCreateLeaf_NullContext_ThrowsNullPointerException() {
        // Given: context es null
        Context<String, Geometry> context = null;
        List<Entry<String, Geometry>> entries = new ArrayList<>();

        // When / Then: se espera una excepcion al intentar crear
        assertThrows(NullPointerException.class, () -> {
            leafFactory.createLeaf(entries, context);
        });
    }

    @Test
    public void testCreateLeaf_EmptyEntries_ReturnsLeafWithEmptyEntries() {
        // Given: entries esta vacio
        List<Entry<String, Geometry>> entries = new ArrayList<>();

        // When: se ejecuta la creacion del leaf
        Leaf<String, Geometry> leaf = leafFactory.createLeaf(entries, context);

        // Then: se verifica el resultado
        assertNotNull(leaf);
        assertTrue(leaf.entries().isEmpty());
        assertEquals(context, leaf.context());
    }

    @Test
    public void testCreateLeaf_NonEmptyEntries_ReturnsLeafWithEntries() {
        // Given: entries no esta vacio
        List<Entry<String, Geometry>> entries = new ArrayList<>();
        entries.add(new Entry<>("value", geometry));

        // When: se ejecuta la creacion del leaf
        Leaf<String, Geometry> leaf = leafFactory.createLeaf(entries, context);

        // Then: se verifica el resultado
        assertNotNull(leaf);
        assertEquals(entries, leaf.entries());
        assertEquals(context, leaf.context());
    }
}