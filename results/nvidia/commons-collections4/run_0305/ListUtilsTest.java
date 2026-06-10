import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.list.FixedSizeList;
import org.apache.commons.collections4.list.LazyList;
import org.apache.commons.collections4.list.PredicatedList;
import org.apache.commons.collections4.list.TransformedList;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListUtilsTest {

    @Mock
    private List<String> list;

    @Mock
    private Predicate<String> predicate;

    @Mock
    private Supplier<String> supplier;

    @BeforeEach
    void setup() {
        // Initialize mocks
        when(list.isEmpty()).thenReturn(false);
        when(list.size()).thenReturn(10);
        when(predicate.test(any())).thenReturn(true);
    }

    @Test
    void testDefaultIfNull() {
        // Given
        List<String> defaultList = new ArrayList<>();
        defaultList.add("default");

        // When
        List<String> result = ListUtils.defaultIfNull(null, defaultList);

        // Then
        assertEquals(defaultList, result);
    }

    @Test
    void testEmptyIfNull() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element");

        // When
        List<String> result = ListUtils.emptyIfNull(list);

        // Then
        assertEquals(list, result);
    }

    @Test
    void testFixedSizeList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element");

        // When
        List<String> result = ListUtils.fixedSizeList(list);

        // Then
        assertEquals(list, result);
    }

    @Test
    void testGetFirst() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("second");

        // When
        String result = ListUtils.getFirst(list);

        // Then
        assertEquals("first", result);
    }

    @Test
    void testGetLast() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("last");

        // When
        String result = ListUtils.getLast(list);

        // Then
        assertEquals("last", result);
    }

    @Test
    void testHashCodeForList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element");

        // When
        int result = ListUtils.hashCodeForList(list);

        // Then
        assertTrue(result > 0);
    }

    @Test
    void testIndexOf() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element1");
        list.add("element2");

        // When
        int result = ListUtils.indexOf(list, predicate);

        // Then
        assertTrue(result >= 0);
    }

    @Test
    void testIntersection() {
        // Given
        List<String> list1 = new ArrayList<>();
        list1.add("element1");
        list1.add("element2");

        List<String> list2 = new ArrayList<>();
        list2.add("element2");
        list2.add("element3");

        // When
        List<String> result = ListUtils.intersection(list1, list2);

        // Then
        assertEquals(1, result.size());
        assertEquals("element2", result.get(0));
    }

    @Test
    void testIsEqualList() {
        // Given
        List<String> list1 = new ArrayList<>();
        list1.add("element1");
        list1.add("element2");

        List<String> list2 = new ArrayList<>();
        list2.add("element1");
        list2.add("element2");

        // When
        boolean result = ListUtils.isEqualList(list1, list2);

        // Then
        assertTrue(result);
    }

    @Test
    void testLazyList() {
        // Given
        List<String> list = new ArrayList<>();

        // When
        List<String> result = ListUtils.lazyList(list, supplier);

        // Then
        assertNotNull(result);
    }

    @Test
    void testLongestCommonSubsequence() {
        // Given
        String charSequenceA = "abc";
        String charSequenceB = "abc";

        // When
        String result = ListUtils.longestCommonSubsequence(charSequenceA, charSequenceB);

        // Then
        assertEquals("abc", result);
    }

    @Test
    void testPartition() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element1");
        list.add("element2");
        list.add("element3");

        // When
        List<List<String>> result = ListUtils.partition(list, 2);

        // Then
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).size());
        assertEquals(1, result.get(1).size());
    }

    @Test
    void testPredicatedList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element1");
        list.add("element2");

        // When
        List<String> result = ListUtils.predicatedList(list, predicate);

        // Then
        assertEquals(list, result);
    }

    @Test
    void testRemoveAll() {
        // Given
        List<String> collection = new ArrayList<>();
        collection.add("element1");
        collection.add("element2");

        Collection<String> remove = new ArrayList<>();
        remove.add("element2");

        // When
        List<String> result = ListUtils.removeAll(collection, remove);

        // Then
        assertEquals(1, result.size());
        assertEquals("element1", result.get(0));
    }

    @Test
    void testRetainAll() {
        // Given
        List<String> collection = new ArrayList<>();
        collection.add("element1");
        collection.add("element2");

        Collection<String> retain = new ArrayList<>();
        retain.add("element2");

        // When
        List<String> result = ListUtils.retainAll(collection, retain);

        // Then
        assertEquals(1, result.size());
        assertEquals("element2", result.get(0));
    }

    @Test
    void testSelect() {
        // Given
        List<String> inputCollection = new ArrayList<>();
        inputCollection.add("element1");
        inputCollection.add("element2");

        // When
        List<String> result = ListUtils.select(inputCollection, predicate);

        // Then
        assertEquals(inputCollection, result);
    }

    @Test
    void testSelectRejected() {
        // Given
        List<String> inputCollection = new ArrayList<>();
        inputCollection.add("element1");
        inputCollection.add("element2");

        // When
        List<String> result = ListUtils.selectRejected(inputCollection, predicate);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testSubtract() {
        // Given
        List<String> list1 = new ArrayList<>();
        list1.add("element1");
        list1.add("element2");

        List<String> list2 = new ArrayList<>();
        list2.add("element2");

        // When
        List<String> result = ListUtils.subtract(list1, list2);

        // Then
        assertEquals(1, result.size());
        assertEquals("element1", result.get(0));
    }

    @Test
    void testSum() {
        // Given
        List<String> list1 = new ArrayList<>();
        list1.add("element1");
        list1.add("element2");

        List<String> list2 = new ArrayList<>();
        list2.add("element2");
        list2.add("element3");

        // When
        List<String> result = ListUtils.sum(list1, list2);

        // Then
        assertEquals(3, result.size());
    }

    @Test
    void testSynchronizedList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element1");
        list.add("element2");

        // When
        List<String> result = ListUtils.synchronizedList(list);

        // Then
        assertEquals(list, result);
    }

    @Test
    void testTransformedList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element1");
        list.add("element2");

        // When
        List<String> result = ListUtils.transformedList(list, supplier);

        // Then
        assertNotNull(result);
    }

    @Test
    void testUnion() {
        // Given
        List<String> list1 = new ArrayList<>();
        list1.add("element1");
        list1.add("element2");

        List<String> list2 = new ArrayList<>();
        list2.add("element2");
        list2.add("element3");

        // When
        List<String> result = ListUtils.union(list1, list2);

        // Then
        assertEquals(3, result.size());
    }

    @Test
    void testUnmodifiableList() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("element1");
        list.add("element2");

        // When
        List<String> result = ListUtils.unmodifiableList(list);

        // Then
        assertEquals(list, result);
    }
}