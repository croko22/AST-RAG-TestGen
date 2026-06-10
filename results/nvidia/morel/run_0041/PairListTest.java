import net.hydromatic.morel.util.PairList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PairListTest {

    @Mock
    private BiConsumer<Object, Object> biConsumer;

    @Mock
    private BiFunction<Object, Object, Object> biFunction;

    @Mock
    private BiPredicate<Object, Object> biPredicate;

    @Mock
    private Supplier<Object> supplier;

    private PairList.Builder<Object, Object> builder;

    @BeforeEach
    void setup() {
        builder = new PairList.Builder<>();
    }

    @Test
    void testOf() {
        PairList<Object, Object> pairList = PairList.of();
        assertNotNull(pairList);
    }

    @Test
    void testOfSingleElement() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        assertNotNull(pairList);
    }

    @Test
    void testCopyOf() {
        PairList<Object, Object> pairList = PairList.copyOf("key", "value");
        assertNotNull(pairList);
    }

    @Test
    void testCopyOfIterable() {
        List<PairList.MapEntry<Object, Object>> list = new ArrayList<>();
        list.add(new PairList.MapEntry<>("key", "value"));
        PairList<Object, Object> pairList = PairList.copyOf(list);
        assertNotNull(pairList);
    }

    @Test
    void testFromTransformed() {
        List<Object> list = new ArrayList<>();
        list.add("key");
        PairList<Object, Object> pairList = PairList.fromTransformed(list, (t, consumer) -> consumer.accept("key", "value"));
        assertNotNull(pairList);
    }

    @Test
    void testViewOf() {
        PairList<Object, Object> pairList = PairList.viewOf(new java.util.HashMap<>());
        assertNotNull(pairList);
    }

    @Test
    void testWithCapacity() {
        PairList<Object, Object> pairList = PairList.withCapacity(10);
        assertNotNull(pairList);
    }

    @Test
    void testBackedBy() {
        List<Object> list = new ArrayList<>();
        PairList<Object, Object> pairList = PairList.backedBy(list);
        assertNotNull(pairList);
    }

    @Test
    void testBuilder() {
        PairList.Builder<Object, Object> builder = PairList.builder();
        assertNotNull(builder);
    }

    @Test
    void testAdd() {
        builder.add("key", "value");
        PairList<Object, Object> pairList = builder.build();
        assertNotNull(pairList);
    }

    @Test
    void testBuild() {
        PairList<Object, Object> pairList = builder.build();
        assertNotNull(pairList);
    }

    @Test
    void testForEach() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.forEach(biConsumer);
        verify(biConsumer).accept(any(), any());
    }

    @Test
    void testForEachIndexed() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.forEachIndexed((index, t, u) -> biConsumer.accept(t, u));
        verify(biConsumer).accept(any(), any());
    }

    @Test
    void testToImmutableMap() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.toImmutableMap();
    }

    @Test
    void testToImmutableSortedMap() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.toImmutableSortedMap();
    }

    @Test
    void testAsSortedMap() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.asSortedMap();
    }

    @Test
    void testImmutable() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.immutable();
    }

    @Test
    void testTransform() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.transform(biFunction);
    }

    @Test
    void testTransformEager() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.transformEager(biFunction);
    }

    @Test
    void testAnyMatch() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.anyMatch(biPredicate);
    }

    @Test
    void testAllMatch() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.allMatch(biPredicate);
    }

    @Test
    void testFirstMatch() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.firstMatch(biPredicate);
    }

    @Test
    void testNoneMatch() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.noneMatch(biPredicate);
    }

    @Test
    void testWithSortedKeys() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.withSortedKeys(null);
    }

    @Test
    void testAddIfLeftAbsent() {
        PairList<Object, Object> pairList = PairList.of("key", "value");
        pairList.addIfLeftAbsent("key", supplier);
    }
}