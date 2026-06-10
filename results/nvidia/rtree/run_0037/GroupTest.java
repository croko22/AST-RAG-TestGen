import com.github.davidmoten.rtree.geometry.Group;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupTest {

    @Mock
    private Util util;

    @Mock
    private Rectangle rectangle;

    private List<HasGeometry> list;

    @BeforeEach
    public void setup() {
        list = new ArrayList<>();
        when(util.mbr(anyCollection())).thenReturn(rectangle);
    }

    @Test
    public void testConstructor() {
        // Given
        Group<HasGeometry> group = new Group<>(list);

        // Then
        assertSame(list, group.list());
        assertSame(rectangle, group.geometry());
        verify(util).mbr(anyCollection());
    }

    @Test
    public void testList() {
        // Given
        Group<HasGeometry> group = new Group<>(list);

        // When
        List<HasGeometry> result = group.list();

        // Then
        assertSame(list, result);
    }

    @Test
    public void testGeometry() {
        // Given
        Group<HasGeometry> group = new Group<>(list);

        // When
        Rectangle result = (Rectangle) group.geometry();

        // Then
        assertSame(rectangle, result);
    }

    @Test
    public void testConstructor_NullList() {
        // Given
        List<HasGeometry> nullList = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> new Group<>(nullList));
    }

    @Test
    public void testConstructor_EmptyList() {
        // Given
        List<HasGeometry> emptyList = new ArrayList<>();

        // When
        Group<HasGeometry> group = new Group<>(emptyList);

        // Then
        assertSame(emptyList, group.list());
        assertSame(rectangle, group.geometry());
        verify(util).mbr(anyCollection());
    }
}