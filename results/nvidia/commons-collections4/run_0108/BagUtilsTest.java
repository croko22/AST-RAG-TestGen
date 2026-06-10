import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.CollectionBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.PredicatedBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
import org.apache.commons.collections4.bag.SynchronizedBag;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.commons.collections4.bag.TransformedBag;
import org.apache.commons.collections4.bag.TransformedSortedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.apache.commons.collections4.bag.UnmodifiableSortedBag;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Predicate;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BagUtilsTest {

    @Mock
    private Bag<String> bag;

    @Mock
    private org.apache.commons.collections4.bag.SortedBag<String> sortedBag;

    @BeforeEach
    public void setup() {
        // Initialize mocks
    }

    @Test
    public void testCollectionBag() {
        // Given
        Bag<String> collectionBag = BagUtils.collectionBag(bag);

        // Then
        assertNotNull(collectionBag);
        assertEquals(CollectionBag.class, collectionBag.getClass());
    }

    @Test
    public void testEmptyBag() {
        // Given
        Bag<String> emptyBag = BagUtils.emptyBag();

        // Then
        assertNotNull(emptyBag);
        assertTrue(emptyBag.isEmpty());
    }

    @Test
    public void testEmptySortedBag() {
        // Given
        org.apache.commons.collections4.bag.SortedBag<String> emptySortedBag = BagUtils.emptySortedBag();

        // Then
        assertNotNull(emptySortedBag);
        assertTrue(emptySortedBag.isEmpty());
    }

    @Test
    public void testPredicatedBag() {
        // Given
        Predicate<String> predicate = TruePredicate.truePredicate();
        Bag<String> predicatedBag = BagUtils.predicatedBag(bag, predicate);

        // Then
        assertNotNull(predicatedBag);
        assertEquals(PredicatedBag.class, predicatedBag.getClass());
    }

    @Test
    public void testPredicatedSortedBag() {
        // Given
        Predicate<String> predicate = TruePredicate.truePredicate();
        org.apache.commons.collections4.bag.SortedBag<String> predicatedSortedBag = BagUtils.predicatedSortedBag(sortedBag, predicate);

        // Then
        assertNotNull(predicatedSortedBag);
        assertEquals(PredicatedSortedBag.class, predicatedSortedBag.getClass());
    }

    @Test
    public void testSynchronizedBag() {
        // Given
        Bag<String> synchronizedBag = BagUtils.synchronizedBag(bag);

        // Then
        assertNotNull(synchronizedBag);
        assertEquals(SynchronizedBag.class, synchronizedBag.getClass());
    }

    @Test
    public void testSynchronizedSortedBag() {
        // Given
        org.apache.commons.collections4.bag.SortedBag<String> synchronizedSortedBag = BagUtils.synchronizedSortedBag(sortedBag);

        // Then
        assertNotNull(synchronizedSortedBag);
        assertEquals(SynchronizedSortedBag.class, synchronizedSortedBag.getClass());
    }

    @Test
    public void testTransformingBag() {
        // Given
        org.apache.commons.collections4.Transformer<String, String> transformer = mock(org.apache.commons.collections4.Transformer.class);
        Bag<String> transformingBag = BagUtils.transformingBag(bag, transformer);

        // Then
        assertNotNull(transformingBag);
        assertEquals(TransformedBag.class, transformingBag.getClass());
    }

    @Test
    public void testTransformingSortedBag() {
        // Given
        org.apache.commons.collections4.Transformer<String, String> transformer = mock(org.apache.commons.collections4.Transformer.class);
        org.apache.commons.collections4.bag.SortedBag<String> transformingSortedBag = BagUtils.transformingSortedBag(sortedBag, transformer);

        // Then
        assertNotNull(transformingSortedBag);
        assertEquals(TransformedSortedBag.class, transformingSortedBag.getClass());
    }

    @Test
    public void testUnmodifiableBag() {
        // Given
        Bag<String> unmodifiableBag = BagUtils.unmodifiableBag(bag);

        // Then
        assertNotNull(unmodifiableBag);
        assertEquals(UnmodifiableBag.class, unmodifiableBag.getClass());
    }

    @Test
    public void testUnmodifiableSortedBag() {
        // Given
        org.apache.commons.collections4.bag.SortedBag<String> unmodifiableSortedBag = BagUtils.unmodifiableSortedBag(sortedBag);

        // Then
        assertNotNull(unmodifiableSortedBag);
        assertEquals(UnmodifiableSortedBag.class, unmodifiableSortedBag.getClass());
    }

    @Test
    public void testCollectionBag_NullBag() {
        // Given
        Bag<String> nullBag = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> BagUtils.collectionBag(nullBag));
    }

    @Test
    public void testPredicatedBag_NullBag() {
        // Given
        Bag<String> nullBag = null;
        Predicate<String> predicate = TruePredicate.truePredicate();

        // When / Then
        assertThrows(NullPointerException.class, () -> BagUtils.predicatedBag(nullBag, predicate));
    }

    @Test
    public void testPredicatedBag_NullPredicate() {
        // Given
        Bag<String> bag = mock(Bag.class);
        Predicate<String> nullPredicate = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> BagUtils.predicatedBag(bag, nullPredicate));
    }

    @Test
    public void testSynchronizedBag_NullBag() {
        // Given
        Bag<String> nullBag = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> BagUtils.synchronizedBag(nullBag));
    }

    @Test
    public void testTransformingBag_NullBag() {
        // Given
        Bag<String> nullBag = null;
        org.apache.commons.collections4.Transformer<String, String> transformer = mock(org.apache.commons.collections4.Transformer.class);

        // When / Then
        assertThrows(NullPointerException.class, () -> BagUtils.transformingBag(nullBag, transformer));
    }

    @Test
    public void testTransformingBag_NullTransformer() {
        // Given
        Bag<String> bag = mock(Bag.class);
        org.apache.commons.collections4.Transformer<String, String> nullTransformer = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> BagUtils.transformingBag(bag, nullTransformer));
    }

    @Test
    public void testUnmodifiableBag_NullBag() {
        // Given
        Bag<String> nullBag = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> BagUtils.unmodifiableBag(nullBag));
    }
}