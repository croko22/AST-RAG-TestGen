import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.PredicatedCollection;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.list.PredicatedList;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.collections4.multiset.PredicatedMultiSet;
import org.apache.commons.collections4.queue.PredicatedQueue;
import org.apache.commons.collections4.set.PredicatedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PredicatedCollectionTest {

    @Mock
    private Predicate<String> predicate;

    private PredicatedCollection<String> predicatedCollection;

    @BeforeEach
    void setup() {
        predicatedCollection = new PredicatedCollection<>(new ArrayList<>(), predicate);
    }

    @Test
    void testAdd() {
        // Given
        String item = "item";
        when(predicate.test(item)).thenReturn(true);

        // When
        boolean result = predicatedCollection.add(item);

        // Then
        assertTrue(result);
        verify(predicate, times(1)).test(item);
    }

    @Test
    void testAdd_InvalidItem() {
        // Given
        String item = "item";
        when(predicate.test(item)).thenReturn(false);

        // When
        assertThrows(IllegalArgumentException.class, () -> predicatedCollection.add(item));

        // Then
        verify(predicate, times(1)).test(item);
    }

    @Test
    void testAddAll() {
        // Given
        List<String> items = new ArrayList<>();
        items.add("item1");
        items.add("item2");
        when(predicate.test(any())).thenReturn(true);

        // When
        boolean result = predicatedCollection.addAll(items);

        // Then
        assertTrue(result);
        verify(predicate, times(2)).test(any());
    }

    @Test
    void testAddAll_InvalidItem() {
        // Given
        List<String> items = new ArrayList<>();
        items.add("item1");
        items.add("item2");
        when(predicate.test(any())).thenReturn(false);

        // When
        assertThrows(IllegalArgumentException.class, () -> predicatedCollection.addAll(items));

        // Then
        verify(predicate, times(1)).test(any());
    }

    @Test
    void testCreatePredicatedBag() {
        // Given
        Bag<String> bag = mock(Bag.class);

        // When
        Bag<String> result = PredicatedCollection.Builder.builder(predicate).createPredicatedBag(bag);

        // Then
        assertNotNull(result);
        verify(bag, times(1)).add(any(), anyInt());
    }

    @Test
    void testCreatePredicatedList() {
        // Given
        List<String> list = mock(List.class);

        // When
        List<String> result = PredicatedCollection.Builder.builder(predicate).createPredicatedList(list);

        // Then
        assertNotNull(result);
        verify(list, times(1)).add(any());
    }

    @Test
    void testCreatePredicatedMultiSet() {
        // Given
        HashMultiSet<String> multiset = mock(HashMultiSet.class);

        // When
        MultiSet<String> result = PredicatedCollection.Builder.builder(predicate).createPredicatedMultiSet(multiset);

        // Then
        assertNotNull(result);
        verify(multiset, times(1)).add(any(), anyInt());
    }

    @Test
    void testCreatePredicatedQueue() {
        // Given
        PredicatedQueue<String> queue = mock(PredicatedQueue.class);

        // When
        Queue<String> result = PredicatedCollection.Builder.builder(predicate).createPredicatedQueue(queue);

        // Then
        assertNotNull(result);
        verify(queue, times(1)).offer(any());
    }

    @Test
    void testCreatePredicatedSet() {
        // Given
        Set<String> set = mock(Set.class);

        // When
        Set<String> result = PredicatedCollection.Builder.builder(predicate).createPredicatedSet(set);

        // Then
        assertNotNull(result);
        verify(set, times(1)).add(any());
    }

    @Test
    void testRejectedElements() {
        // Given
        PredicatedCollection.Builder<String> builder = PredicatedCollection.Builder.builder(predicate);
        builder.add("item1");
        builder.add("item2");

        // When
        Collection<String> result = builder.rejectedElements();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testBuilder() {
        // Given
        Predicate<String> predicate = NotNullPredicate.notNullPredicate();

        // When
        PredicatedCollection.Builder<String> result = PredicatedCollection.Builder.builder(predicate);

        // Then
        assertNotNull(result);
    }

    @Test
    void testNotNullBuilder() {
        // Given

        // When
        PredicatedCollection.Builder<String> result = PredicatedCollection.Builder.notNullBuilder();

        // Then
        assertNotNull(result);
    }

    @Test
    void testPredicatedCollection() {
        // Given
        List<String> list = new ArrayList<>();
        Predicate<String> predicate = NotNullPredicate.notNullPredicate();

        // When
        PredicatedCollection<String> result = PredicatedCollection.predicatedCollection(list, predicate);

        // Then
        assertNotNull(result);
    }
}