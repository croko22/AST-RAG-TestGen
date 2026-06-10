import net.hydromatic.morel.util.PairLists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PairListsTest {

    @Mock
    private BiConsumer<Integer, Integer> consumer;

    @Mock
    private BiFunction<Integer, Integer, Integer> function;

    @Mock
    private BiPredicate<Integer, Integer> predicate;

    private PairLists.MutablePairList<Integer, Integer> pairList;

    @BeforeEach
    void setup() {
        pairList = new PairLists.MutablePairList<>(new ArrayList<>());
    }

    @Test
    void testLeft() {
        pairList.add(1, 2);
        assertEquals(1, pairList.left(0));
    }

    @Test
    void testRight() {
        pairList.add(1, 2);
        assertEquals(2, pairList.right(0));
    }

    @Test
    void testSubList() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        PairLists.PairList<Integer, Integer> subList = pairList.subList(0, 1);
        assertEquals(1, subList.size());
    }

    @Test
    void testForEachIndexed() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        pairList.forEachIndexed((index, left, right) -> {
            if (index == 0) {
                assertEquals(1, left);
                assertEquals(2, right);
            } else if (index == 1) {
                assertEquals(3, left);
                assertEquals(4, right);
            }
        });
    }

    @Test
    void testTransform() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        List<Integer> result = pairList.transform((left, right) -> left + right);
        assertEquals(2, result.get(0));
        assertEquals(7, result.get(1));
    }

    @Test
    void testTransformEager() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        List<Integer> result = pairList.transformEager((left, right) -> left + right);
        assertEquals(2, result.get(0));
        assertEquals(7, result.get(1));
    }

    @Test
    void testNoneMatch() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        assertTrue(pairList.noneMatch((left, right) -> left > 10));
    }

    @Test
    void testClear() {
        pairList.add(1, 2);
        pairList.clear();
        assertTrue(pairList.isEmpty());
    }

    @Test
    void testSize() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        assertEquals(2, pairList.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(pairList.isEmpty());
        pairList.add(1, 2);
        assertFalse(pairList.isEmpty());
    }

    @Test
    void testAsSortedMap() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        SortedMap<Integer, Integer> sortedMap = pairList.asSortedMap();
        assertEquals(2, sortedMap.get(1));
        assertEquals(4, sortedMap.get(3));
    }

    @Test
    void testWithSortedKeys() {
        pairList.add(3, 4);
        pairList.add(1, 2);
        PairLists.ImmutablePairList<Integer, Integer> sortedPairList = pairList.withSortedKeys(Comparator.naturalOrder());
        assertEquals(1, sortedPairList.left(0));
        assertEquals(2, sortedPairList.right(0));
        assertEquals(3, sortedPairList.left(1));
        assertEquals(4, sortedPairList.right(1));
    }

    @Test
    void testGet() {
        pairList.add(1, 2);
        assertEquals(1, pairList.get(0).getKey());
        assertEquals(2, pairList.get(0).getValue());
    }

    @Test
    void testSet() {
        pairList.add(1, 2);
        pairList.set(0, 3, 4);
        assertEquals(3, pairList.get(0).getKey());
        assertEquals(4, pairList.get(0).getValue());
    }

    @Test
    void testRemove() {
        pairList.add(1, 2);
        pairList.remove(0);
        assertTrue(pairList.isEmpty());
    }

    @Test
    void testAdd() {
        pairList.add(1, 2);
        assertEquals(1, pairList.size());
    }

    @Test
    void testAddAll() {
        pairList.add(1, 2);
        PairLists.PairList<Integer, Integer> otherList = new PairLists.MutablePairList<>(new ArrayList<>());
        otherList.add(3, 4);
        pairList.addAll(otherList);
        assertEquals(2, pairList.size());
    }

    @Test
    void testLeftList() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        List<Integer> leftList = pairList.leftList();
        assertEquals(1, leftList.get(0));
        assertEquals(3, leftList.get(1));
    }

    @Test
    void testRightList() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        List<Integer> rightList = pairList.rightList();
        assertEquals(2, rightList.get(0));
        assertEquals(4, rightList.get(1));
    }

    @Test
    void testForEach() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        pairList.forEach((left, right) -> {
            if (left == 1) {
                assertEquals(2, right);
            } else if (left == 3) {
                assertEquals(4, right);
            }
        });
    }

    @Test
    void testAnyMatch() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        assertTrue(pairList.anyMatch((left, right) -> left > 0));
    }

    @Test
    void testAllMatch() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        assertTrue(pairList.allMatch((left, right) -> left > 0));
    }

    @Test
    void testNoneMatch() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        assertFalse(pairList.noneMatch((left, right) -> left > 0));
    }

    @Test
    void testFirstMatch() {
        pairList.add(1, 2);
        pairList.add(3, 4);
        assertEquals(0, pairList.firstMatch((left, right) -> left > 0));
    }
}