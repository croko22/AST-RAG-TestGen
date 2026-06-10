import net.hydromatic.morel.util.ImmutablePairList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ImmutablePairListTest {

    private ImmutablePairList<String, Integer> pairList;

    @BeforeEach
    void setup() {
        pairList = ImmutablePairList.of("a", 1, "b", 2, "c", 3);
    }

    @Test
    void testOf() {
        // Given
        String t = "a";
        Integer u = 1;

        // When
        ImmutablePairList<String, Integer> pairList = ImmutablePairList.of(t, u);

        // Then
        assertNotNull(pairList);
    }

    @Test
    void testCopyOf() {
        // Given
        String t = "a";
        Integer u = 1;
        Object[] rest = new Object[]{ "b", 2, "c", 3 };

        // When
        ImmutablePairList<String, Integer> pairList = ImmutablePairList.copyOf(t, u, rest);

        // Then
        assertNotNull(pairList);
    }

    @Test
    void testCopyOf_Iterable() {
        // Given
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        // When
        ImmutablePairList<String, Integer> pairList = ImmutablePairList.copyOf(map.entrySet());

        // Then
        assertNotNull(pairList);
    }

    @Test
    void testFromTransformed() {
        // Given
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        BiFunction<String, Integer, Map.Entry<String, Integer>> transformer = (s, i) -> new HashMap.SimpleEntry<>(s, i);

        // When
        ImmutablePairList<String, Integer> pairList = ImmutablePairList.fromTransformed(list, (s, consumer) -> consumer.accept(s, 1));

        // Then
        assertNotNull(pairList);
    }

    @Test
    void testImmutable() {
        // Given
        ImmutablePairList<String, Integer> pairList = ImmutablePairList.of("a", 1);

        // When
        ImmutablePairList<String, Integer> immutablePairList = pairList.immutable();

        // Then
        assertNotNull(immutablePairList);
        assertSame(pairList, immutablePairList);
    }
}