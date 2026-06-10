import org.apache.commons.collections4.list.GrowthList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrowthListTest {

    @Mock
    private List<String> mockList;

    private GrowthList<String> growthList;

    @BeforeEach
    void setup() {
        growthList = new GrowthList<>(mockList);
    }

    @Test
    void testGrowthList() {
        // Given
        when(mockList.size()).thenReturn(0);

        // When
        growthList.add(5, "element");

        // Then
        verify(mockList).addAll(any());
        verify(mockList).add(5, "element");
    }

    @Test
    void testGrowthList_AddAll() {
        // Given
        when(mockList.size()).thenReturn(0);

        // When
        growthList.addAll(5, Arrays.asList("element1", "element2"));

        // Then
        verify(mockList).addAll(any());
        verify(mockList).addAll(5, Arrays.asList("element1", "element2"));
    }

    @Test
    void testGrowthList_Set() {
        // Given
        when(mockList.size()).thenReturn(0);

        // When
        growthList.set(5, "element");

        // Then
        verify(mockList).addAll(any());
        verify(mockList).set(5, "element");
    }

    @Test
    void testGrowthList_StaticMethod() {
        // Given
        List<String> list = new ArrayList<>();

        // When
        GrowthList<String> growthList = GrowthList.growthList(list);

        // Then
        assertNotNull(growthList);
    }

    @Test
    void testGrowthList_Add_IndexLessThanZero() {
        // Given
        when(mockList.size()).thenReturn(10);

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> growthList.add(-1, "element"));
    }

    @Test
    void testGrowthList_AddAll_IndexLessThanZero() {
        // Given
        when(mockList.size()).thenReturn(10);

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> growthList.addAll(-1, Arrays.asList("element1", "element2")));
    }

    @Test
    void testGrowthList_Set_IndexLessThanZero() {
        // Given
        when(mockList.size()).thenReturn(10);

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> growthList.set(-1, "element"));
    }

    @Test
    void testGrowthList_Add_IndexEqualToSize() {
        // Given
        when(mockList.size()).thenReturn(10);

        // When
        growthList.add(10, "element");

        // Then
        verify(mockList).add(10, "element");
    }

    @Test
    void testGrowthList_AddAll_IndexEqualToSize() {
        // Given
        when(mockList.size()).thenReturn(10);

        // When
        growthList.addAll(10, Arrays.asList("element1", "element2"));

        // Then
        verify(mockList).addAll(10, Arrays.asList("element1", "element2"));
    }

    @Test
    void testGrowthList_Set_IndexEqualToSize() {
        // Given
        when(mockList.size()).thenReturn(10);

        // When
        growthList.set(10, "element");

        // Then
        verify(mockList).addAll(any());
        verify(mockList).set(10, "element");
    }
}