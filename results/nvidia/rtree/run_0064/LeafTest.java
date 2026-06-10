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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeafTest {

    @Mock
    private Geometry geometry;

    @Mock
    private Entry<String, Geometry> entry;

    private Leaf<String, Geometry> leaf;

    @BeforeEach
    public void setup() {
        leaf = new Leaf<String, Geometry>() {
            @Override
            public List<Entry<String, Geometry>> entries() {
                List<Entry<String, Geometry>> entries = new ArrayList<>();
                entries.add(entry);
                return entries;
            }

            @Override
            public Entry<String, Geometry> entry(int i) {
                return entries().get(i);
            }
        };
    }

    @Test
    public void testEntries() {
        // Given
        List<Entry<String, Geometry>> expectedEntries = new ArrayList<>();
        expectedEntries.add(entry);

        // When
        List<Entry<String, Geometry>> actualEntries = leaf.entries();

        // Then
        assertEquals(expectedEntries, actualEntries);
        verifyNoInteractions(geometry);
    }

    @Test
    public void testEntry_IndexWithinBounds() {
        // Given
        int index = 0;

        // When
        Entry<String, Geometry> actualEntry = leaf.entry(index);

        // Then
        assertEquals(entry, actualEntry);
        verifyNoInteractions(geometry);
    }

    @Test
    public void testEntry_IndexOutOfBounds() {
        // Given
        int index = 1;

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> leaf.entry(index));
        verifyNoInteractions(geometry);
    }

    @Test
    public void testEntry_NegativeIndex() {
        // Given
        int index = -1;

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> leaf.entry(index));
        verifyNoInteractions(geometry);
    }
}