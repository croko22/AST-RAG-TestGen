package com.github.davidmoten.rtree.geometry;

import com.github.davidmoten.rtree.geometry.Group;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListPairTest {

    @Mock
    private HasGeometry geometry1;

    @Mock
    private HasGeometry geometry2;

    @Mock
    private Group<HasGeometry> group1;

    @Mock
    private Group<HasGeometry> group2;

    private List<HasGeometry> list1;
    private List<HasGeometry> list2;

    @BeforeEach
    public void setup() {
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list1.add(geometry1);
        list2.add(geometry2);
    }

    @Test
    public void testConstructor() {
        // Given
        when(geometry1.geometry().mbr().perimeter()).thenReturn(10.0);
        when(geometry2.geometry().mbr().perimeter()).thenReturn(20.0);

        // When
        ListPair<HasGeometry> listPair = new ListPair<>(list1, list2);

        // Then
        assertNotNull(listPair);
        assertNotNull(listPair.group1());
        assertNotNull(listPair.group2());
    }

    @Test
    public void testGroup1() {
        // Given
        when(geometry1.geometry().mbr().perimeter()).thenReturn(10.0);
        when(geometry2.geometry().mbr().perimeter()).thenReturn(20.0);
        ListPair<HasGeometry> listPair = new ListPair<>(list1, list2);

        // When
        Group<HasGeometry> result = listPair.group1();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testGroup2() {
        // Given
        when(geometry1.geometry().mbr().perimeter()).thenReturn(10.0);
        when(geometry2.geometry().mbr().perimeter()).thenReturn(20.0);
        ListPair<HasGeometry> listPair = new ListPair<>(list1, list2);

        // When
        Group<HasGeometry> result = listPair.group2();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testAreaSum() {
        // Given
        when(geometry1.geometry().mbr().area()).thenReturn(10.0);
        when(geometry2.geometry().mbr().area()).thenReturn(20.0);
        ListPair<HasGeometry> listPair = new ListPair<>(list1, list2);

        // When
        double result = listPair.areaSum();

        // Then
        assertEquals(30.0, result);
    }

    @Test
    public void testAreaSum_Cached() {
        // Given
        when(geometry1.geometry().mbr().area()).thenReturn(10.0);
        when(geometry2.geometry().mbr().area()).thenReturn(20.0);
        ListPair<HasGeometry> listPair = new ListPair<>(list1, list2);
        listPair.areaSum(); // cache the result

        // When
        double result = listPair.areaSum();

        // Then
        assertEquals(30.0, result);
    }

    @Test
    public void testMarginSum() {
        // Given
        when(geometry1.geometry().mbr().perimeter()).thenReturn(10.0);
        when(geometry2.geometry().mbr().perimeter()).thenReturn(20.0);
        ListPair<HasGeometry> listPair = new ListPair<>(list1, list2);

        // When
        double result = listPair.marginSum();

        // Then
        assertEquals(30.0, result);
    }

    @Test
    public void testConstructor_NullList1() {
        // Given
        list1 = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> new ListPair<>(list1, list2));
    }

    @Test
    public void testConstructor_NullList2() {
        // Given
        list2 = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> new ListPair<>(list1, list2));
    }

    @Test
    public void testConstructor_NullGeometry1() {
        // Given
        geometry1 = null;
        list1 = new ArrayList<>();
        list1.add(geometry1);

        // When / Then
        assertThrows(NullPointerException.class, () -> new ListPair<>(list1, list2));
    }

    @Test
    public void testConstructor_NullGeometry2() {
        // Given
        geometry2 = null;
        list2 = new ArrayList<>();
        list2.add(geometry2);

        // When / Then
        assertThrows(NullPointerException.class, () -> new ListPair<>(list1, list2));
    }
}