import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Predicates;
import org.apache.commons.collections4.collection.PredicatedCollection;
import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicatedListTest {

    @Mock
    private List<String> mockList;

    @Mock
    private Predicate<String> mockPredicate;

    private PredicatedList<String> predicatedList;

    @BeforeEach
    void setup() {
        predicatedList = new PredicatedList<>(mockList, mockPredicate);
    }

    @Test
    void testAdd() {
        // Given
        String object = "Test Object";
        when(mockPredicate.evaluate(any())).thenReturn(true);

        // When
        predicatedList.add(object);

        // Then
        verify(mockList).add(object);
    }

    @Test
    void testAdd_ThrowsIllegalArgumentException_WhenPredicateFails() {
        // Given
        String object = "Test Object";
        when(mockPredicate.evaluate(any())).thenReturn(false);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> predicatedList.add(object));
        verify(mockList, never()).add(object);
    }

    @Test
    void testSet() {
        // Given
        String object = "Test Object";
        when(mockPredicate.evaluate(any())).thenReturn(true);

        // When
        predicatedList.set(object);

        // Then
        verify(mockList).set(0, object);
    }

    @Test
    void testSet_ThrowsIllegalArgumentException_WhenPredicateFails() {
        // Given
        String object = "Test Object";
        when(mockPredicate.evaluate(any())).thenReturn(false);

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> predicatedList.set(object));
        verify(mockList, never()).set(0, object);
    }

    @Test
    void testPredicatedList() {
        // Given
        List<String> list = new ArrayList<>();
        Predicate<String> predicate = Predicates.notNullPredicate();

        // When
        PredicatedList<String> predicatedList = PredicatedList.predicatedList(list, predicate);

        // Then
        assertNotNull(predicatedList);
    }

    @Test
    void testAddAtIndex() {
        // Given
        String object = "Test Object";
        int index = 0;
        when(mockPredicate.evaluate(any())).thenReturn(true);

        // When
        predicatedList.add(index, object);

        // Then
        verify(mockList).add(index, object);
    }

    @Test
    void testAddAll() {
        // Given
        Collection<String> collection = new ArrayList<>();
        int index = 0;
        when(mockPredicate.evaluate(any())).thenReturn(true);

        // When
        predicatedList.addAll(index, collection);

        // Then
        verify(mockList).addAll(index, collection);
    }

    @Test
    void testEquals() {
        // Given
        Object object = new Object();

        // When
        boolean result = predicatedList.equals(object);

        // Then
        assertEquals(false, result);
    }

    @Test
    void testGet() {
        // Given
        int index = 0;
        String object = "Test Object";
        when(mockList.get(index)).thenReturn(object);

        // When
        String result = predicatedList.get(index);

        // Then
        assertEquals(object, result);
    }

    @Test
    void testHashCode() {
        // Given
        int hashCode = 123;

        // When
        when(mockList.hashCode()).thenReturn(hashCode);
        int result = predicatedList.hashCode();

        // Then
        assertEquals(hashCode, result);
    }

    @Test
    void testIndexOf() {
        // Given
        Object object = new Object();
        int index = 0;
        when(mockList.indexOf(object)).thenReturn(index);

        // When
        int result = predicatedList.indexOf(object);

        // Then
        assertEquals(index, result);
    }

    @Test
    void testLastIndexOf() {
        // Given
        Object object = new Object();
        int index = 0;
        when(mockList.lastIndexOf(object)).thenReturn(index);

        // When
        int result = predicatedList.lastIndexOf(object);

        // Then
        assertEquals(index, result);
    }

    @Test
    void testListIterator() {
        // Given
        ListIterator<String> listIterator = mock(ListIterator.class);

        // When
        when(mockList.listIterator()).thenReturn(listIterator);
        ListIterator<String> result = predicatedList.listIterator();

        // Then
        assertEquals(listIterator, result);
    }

    @Test
    void testRemove() {
        // Given
        int index = 0;
        String object = "Test Object";
        when(mockList.remove(index)).thenReturn(object);

        // When
        String result = predicatedList.remove(index);

        // Then
        assertEquals(object, result);
    }

    @Test
    void testSetAtIndex() {
        // Given
        int index = 0;
        String object = "Test Object";
        when(mockPredicate.evaluate(any())).thenReturn(true);

        // When
        predicatedList.set(index, object);

        // Then
        verify(mockList).set(index, object);
    }

    @Test
    void testSubList() {
        // Given
        int fromIndex = 0;
        int toIndex = 1;
        List<String> subList = new ArrayList<>();

        // When
        when(mockList.subList(fromIndex, toIndex)).thenReturn(subList);
        List<String> result = predicatedList.subList(fromIndex, toIndex);

        // Then
        assertEquals(subList, result);
    }
}