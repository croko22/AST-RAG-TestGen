import com.github.davidmoten.rtree.SplitterRStar;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SplitterRStarTest {

    @Mock
    private HasGeometry hasGeometry1;

    @Mock
    private HasGeometry hasGeometry2;

    @Mock
    private HasGeometry hasGeometry3;

    private SplitterRStar splitterRStar;

    @BeforeEach
    void setup() {
        splitterRStar = new SplitterRStar();
    }

    @Test
    public void testSplit_EmptyList_ThrowsException() {
        // Given
        List<HasGeometry> items = new ArrayList<>();

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> splitterRStar.split(items, 1));
    }

    @Test
    public void testSplit_ListWithOneElement_ThrowsException() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        items.add(hasGeometry1);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> splitterRStar.split(items, 1));
    }

    @Test
    public void testSplit_ListWithTwoElements_ReturnsListPair() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        items.add(hasGeometry1);
        items.add(hasGeometry2);

        // When
        ListPair<HasGeometry> result = splitterRStar.split(items, 1);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
    }

    @Test
    public void testSplit_ListWithThreeElements_ReturnsListPair() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        items.add(hasGeometry1);
        items.add(hasGeometry2);
        items.add(hasGeometry3);

        // When
        ListPair<HasGeometry> result = splitterRStar.split(items, 1);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
    }

    @Test
    public void testSplit_ListWithMultipleElements_ReturnsListPair() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(mock(HasGeometry.class));
        }

        // When
        ListPair<HasGeometry> result = splitterRStar.split(items, 1);

        // Then
        assertNotNull(result);
        assertNotNull(result.group1());
        assertNotNull(result.group2());
    }

    @Test
    public void testSplit_MinSizeGreaterThanListSize_ThrowsException() {
        // Given
        List<HasGeometry> items = new ArrayList<>();
        items.add(hasGeometry1);
        items.add(hasGeometry2);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> splitterRStar.split(items, 3));
    }
}