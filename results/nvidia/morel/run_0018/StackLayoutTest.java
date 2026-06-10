import com.google.common.collect.ImmutableMap;
import net.hydromatic.morel.ast.Core;
import net.hydromatic.morel.compile.StackLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StackLayoutTest {

    private StackLayout stackLayout;

    @BeforeEach
    void setup() {
        stackLayout = StackLayout.EMPTY;
    }

    @Test
    void testGet_EmptyLayout() {
        // Given: an empty layout
        // When: get is called with a named pattern
        int result = stackLayout.get(new Core.NamedPat("name", 0));
        // Then: the result is -1
        assertEquals(-1, result);
    }

    @Test
    void testGet_NonEmptyLayout() {
        // Given: a non-empty layout
        Core.NamedPat namedPat = new Core.NamedPat("name", 0);
        stackLayout = stackLayout.with(namedPat, 0);
        // When: get is called with the named pattern
        int result = stackLayout.get(namedPat);
        // Then: the result is the slot index
        assertEquals(0, result);
    }

    @Test
    void testWith_NewNamedPat() {
        // Given: an empty layout and a new named pattern
        Core.NamedPat namedPat = new Core.NamedPat("name", 0);
        // When: with is called with the named pattern and a slot index
        StackLayout newLayout = stackLayout.with(namedPat, 0);
        // Then: the new layout contains the named pattern and slot index
        assertEquals(0, newLayout.get(namedPat));
    }

    @Test
    void testWith_ExistingNamedPat() {
        // Given: a non-empty layout and an existing named pattern
        Core.NamedPat namedPat = new Core.NamedPat("name", 0);
        stackLayout = stackLayout.with(namedPat, 0);
        // When: with is called with the existing named pattern and a new slot index
        StackLayout newLayout = stackLayout.with(namedPat, 1);
        // Then: the new layout contains the existing named pattern and new slot index
        assertEquals(1, newLayout.get(namedPat));
    }

    @Test
    void testWithout_NoNames() {
        // Given: a non-empty layout and no names to remove
        Core.NamedPat namedPat = new Core.NamedPat("name", 0);
        stackLayout = stackLayout.with(namedPat, 0);
        // When: without is called with an empty collection of names
        StackLayout newLayout = stackLayout.without(ImmutableMap.of());
        // Then: the new layout is the same as the original layout
        assertEquals(stackLayout, newLayout);
    }

    @Test
    void testWithout_NameToRemove() {
        // Given: a non-empty layout and a name to remove
        Core.NamedPat namedPat = new Core.NamedPat("name", 0);
        stackLayout = stackLayout.with(namedPat, 0);
        // When: without is called with a collection containing the name to remove
        StackLayout newLayout = stackLayout.without(ImmutableMap.of("name"));
        // Then: the new layout does not contain the named pattern
        assertEquals(-1, newLayout.get(namedPat));
    }

    @Test
    void testSize_EmptyLayout() {
        // Given: an empty layout
        // When: size is called
        int result = stackLayout.size();
        // Then: the result is 0
        assertEquals(0, result);
    }

    @Test
    void testSize_NonEmptyLayout() {
        // Given: a non-empty layout
        Core.NamedPat namedPat = new Core.NamedPat("name", 0);
        stackLayout = stackLayout.with(namedPat, 0);
        // When: size is called
        int result = stackLayout.size();
        // Then: the result is 1
        assertEquals(1, result);
    }

    @Test
    void testNameToOffsetMap_EmptyLayout() {
        // Given: an empty layout
        // When: nameToOffsetMap is called with a local depth
        ImmutableMap<String, Integer> result = stackLayout.nameToOffsetMap(0);
        // Then: the result is an empty map
        assertTrue(result.isEmpty());
    }

    @Test
    void testNameToOffsetMap_NonEmptyLayout() {
        // Given: a non-empty layout
        Core.NamedPat namedPat = new Core.NamedPat("name", 0);
        stackLayout = stackLayout.with(namedPat, 0);
        // When: nameToOffsetMap is called with a local depth
        ImmutableMap<String, Integer> result = stackLayout.nameToOffsetMap(1);
        // Then: the result contains the named pattern and its offset
        assertEquals(1, (int) result.get("name"));
    }
}