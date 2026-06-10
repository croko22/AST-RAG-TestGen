package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.ListPair;
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
public class SplitterTest {

    @Mock
    private Splitter splitter;

    private List<HasGeometry> items;

    @BeforeEach
    public void setup() {
        items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HasGeometry item = mock(HasGeometry.class);
            items.add(item);
        }
    }

    @Test
    public void testSplit_MinSizeLessThanHalfOfItems() {
        // Given
        int minSize = 3;
        when(splitter.split(items, minSize)).thenReturn(getListPairMock());

        // When
        ListPair<HasGeometry> result = splitter.split(items, minSize);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
        assertTrue(result.areaSum() >= 0);
        assertTrue(result.marginSum() >= 0);
        verify(splitter, times(1)).split(items, minSize);
    }

    @Test
    public void testSplit_MinSizeGreaterThanHalfOfItems() {
        // Given
        int minSize = 6;
        when(splitter.split(items, minSize)).thenReturn(getListPairMock());

        // When
        ListPair<HasGeometry> result = splitter.split(items, minSize);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
        assertTrue(result.areaSum() >= 0);
        assertTrue(result.marginSum() >= 0);
        verify(splitter, times(1)).split(items, minSize);
    }

    @Test
    public void testSplit_MinSizeEqualsHalfOfItems() {
        // Given
        int minSize = 5;
        when(splitter.split(items, minSize)).thenReturn(getListPairMock());

        // When
        ListPair<HasGeometry> result = splitter.split(items, minSize);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
        assertTrue(result.areaSum() >= 0);
        assertTrue(result.marginSum() >= 0);
        verify(splitter, times(1)).split(items, minSize);
    }

    @Test
    public void testSplit_EmptyList() {
        // Given
        List<HasGeometry> emptyList = new ArrayList<>();
        when(splitter.split(emptyList, 1)).thenReturn(getListPairMock());

        // When
        ListPair<HasGeometry> result = splitter.split(emptyList, 1);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
        assertTrue(result.areaSum() >= 0);
        assertTrue(result.marginSum() >= 0);
        verify(splitter, times(1)).split(emptyList, 1);
    }

    @Test
    public void testSplit_NullList() {
        // Given
        List<HasGeometry> nullList = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> splitter.split(nullList, 1));
        verify(splitter, never()).split(nullList, 1);
    }

    @Test
    public void testSplit_MinSizeLessThanOne() {
        // Given
        int minSize = 0;

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> splitter.split(items, minSize));
        verify(splitter, never()).split(items, minSize);
    }

    private ListPair<HasGeometry> getListPairMock() {
        ListPair<HasGeometry> listPair = mock(ListPair.class);
        when(listPair.group1()).thenReturn(mock(List.class));
        when(listPair.group2()).thenReturn(mock(List.class));
        when(listPair.areaSum()).thenReturn(10.0);
        when(listPair.marginSum()).thenReturn(5.0);
        return listPair;
    }
}