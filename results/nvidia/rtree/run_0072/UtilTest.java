import com.github.davidmoten.guavamini.Preconditions;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UtilTest {

    @Mock
    private HasGeometry hasGeometry;

    @Mock
    private Rectangle rectangle;

    @BeforeEach
    public void setup() {
        when(hasGeometry.geometry()).thenReturn(mock(com.github.davidmoten.rtree.geometry.Geometry.class));
        when(rectangle.x1()).thenReturn(1.0);
        when(rectangle.y1()).thenReturn(2.0);
        when(rectangle.x2()).thenReturn(3.0);
        when(rectangle.y2()).thenReturn(4.0);
        when(rectangle.isDoublePrecision()).thenReturn(true);
    }

    @Test
    public void testMbr_EmptyCollection_ThrowsIllegalArgumentException() {
        // Given
        Collection<HasGeometry> items = new ArrayList<>();

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> com.github.davidmoten.rtree.internal.Util.mbr(items));
    }

    @Test
    public void testMbr_SingleItem_ReturnsMbr() {
        // Given
        Collection<HasGeometry> items = new ArrayList<>();
        items.add(hasGeometry);
        when(hasGeometry.geometry().mbr()).thenReturn(rectangle);

        // When
        Rectangle result = com.github.davidmoten.rtree.internal.Util.mbr(items);

        // Then
        assertNotNull(result);
        assertEquals(1.0, result.x1(), 0.001);
        assertEquals(2.0, result.y1(), 0.001);
        assertEquals(3.0, result.x2(), 0.001);
        assertEquals(4.0, result.y2(), 0.001);
    }

    @Test
    public void testMbr_MultipleItems_ReturnsMbr() {
        // Given
        Collection<HasGeometry> items = new ArrayList<>();
        HasGeometry item1 = mock(HasGeometry.class);
        HasGeometry item2 = mock(HasGeometry.class);
        Rectangle rectangle1 = mock(Rectangle.class);
        Rectangle rectangle2 = mock(Rectangle.class);
        when(item1.geometry()).thenReturn(mock(com.github.davidmoten.rtree.geometry.Geometry.class));
        when(item2.geometry()).thenReturn(mock(com.github.davidmoten.rtree.geometry.Geometry.class));
        when(item1.geometry().mbr()).thenReturn(rectangle1);
        when(item2.geometry().mbr()).thenReturn(rectangle2);
        when(rectangle1.x1()).thenReturn(1.0);
        when(rectangle1.y1()).thenReturn(2.0);
        when(rectangle1.x2()).thenReturn(3.0);
        when(rectangle1.y2()).thenReturn(4.0);
        when(rectangle2.x1()).thenReturn(2.0);
        when(rectangle2.y1()).thenReturn(3.0);
        when(rectangle2.x2()).thenReturn(4.0);
        when(rectangle2.y2()).thenReturn(5.0);
        items.add(item1);
        items.add(item2);

        // When
        Rectangle result = com.github.davidmoten.rtree.internal.Util.mbr(items);

        // Then
        assertNotNull(result);
        assertEquals(1.0, result.x1(), 0.001);
        assertEquals(2.0, result.y1(), 0.001);
        assertEquals(4.0, result.x2(), 0.001);
        assertEquals(5.0, result.y2(), 0.001);
    }

    @Test
    public void testAdd_ListAndElement_ReturnsNewList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        String element = "item3";

        // When
        List<String> result = com.github.davidmoten.rtree.internal.Util.add(list, element);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("item1"));
        assertTrue(result.contains("item2"));
        assertTrue(result.contains("item3"));
    }

    @Test
    public void testRemove_ListAndElements_ReturnsNewList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");
        List<String> elements = new ArrayList<>();
        elements.add("item2");

        // When
        List<String> result = com.github.davidmoten.rtree.internal.Util.remove(list, elements);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("item1"));
        assertTrue(result.contains("item3"));
    }

    @Test
    public void testReplace_ListElementAndReplacements_ReturnsNewList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");
        String element = "item2";
        List<String> replacements = new ArrayList<>();
        replacements.add("replacement1");
        replacements.add("replacement2");

        // When
        List<String> result = com.github.davidmoten.rtree.internal.Util.replace(list, element, replacements);

        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("item1"));
        assertTrue(result.contains("replacement1"));
        assertTrue(result.contains("replacement2"));
        assertTrue(result.contains("item3"));
    }
}