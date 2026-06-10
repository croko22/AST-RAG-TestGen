import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class FunctionsTest {

    private Object key;

    @BeforeEach
    public void setup() {
        key = new Object();
    }

    @Test
    public void testListFunction() {
        // Given: listFunction is called
        Function<Object, List<Object>> listFunction = Functions.listFunction();

        // When: apply the listFunction to a key
        List<Object> result = listFunction.apply(key);

        // Then: the result is a new ArrayList instance
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertEquals(0, result.size());
    }

    @Test
    public void testSetFunction() {
        // Given: setFunction is called
        Function<Object, Set<Object>> setFunction = Functions.setFunction();

        // When: apply the setFunction to a key
        Set<Object> result = setFunction.apply(key);

        // Then: the result is a new HashSet instance
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertEquals(0, result.size());
    }

    @Test
    public void testMapFunction() {
        // Given: mapFunction is called
        Function<Object, Map<Object, Object>> mapFunction = Functions.mapFunction();

        // When: apply the mapFunction to a key
        Map<Object, Object> result = mapFunction.apply(key);

        // Then: the result is a new HashMap instance
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertEquals(0, result.size());
    }

    @Test
    public void testIdentityMapFunction() {
        // Given: identityMapFunction is called
        Function<Object, IdentityHashMap<Object, Object>> identityMapFunction = Functions.identityMapFunction();

        // When: apply the identityMapFunction to a key
        IdentityHashMap<Object, Object> result = identityMapFunction.apply(key);

        // Then: the result is a new IdentityHashMap instance
        assertNotNull(result);
        assertTrue(result instanceof IdentityHashMap);
        assertEquals(0, result.size());
    }

    @Test
    public void testListFunction_MultipleCalls() {
        // Given: listFunction is called multiple times
        Function<Object, List<Object>> listFunction = Functions.listFunction();

        // When: apply the listFunction to a key multiple times
        List<Object> result1 = listFunction.apply(key);
        List<Object> result2 = listFunction.apply(key);

        // Then: each result is a new ArrayList instance
        assertNotNull(result1);
        assertTrue(result1 instanceof ArrayList);
        assertEquals(0, result1.size());

        assertNotNull(result2);
        assertTrue(result2 instanceof ArrayList);
        assertEquals(0, result2.size());

        assertNotSame(result1, result2);
    }

    @Test
    public void testSetFunction_MultipleCalls() {
        // Given: setFunction is called multiple times
        Function<Object, Set<Object>> setFunction = Functions.setFunction();

        // When: apply the setFunction to a key multiple times
        Set<Object> result1 = setFunction.apply(key);
        Set<Object> result2 = setFunction.apply(key);

        // Then: each result is a new HashSet instance
        assertNotNull(result1);
        assertTrue(result1 instanceof HashSet);
        assertEquals(0, result1.size());

        assertNotNull(result2);
        assertTrue(result2 instanceof HashSet);
        assertEquals(0, result2.size());

        assertNotSame(result1, result2);
    }

    @Test
    public void testMapFunction_MultipleCalls() {
        // Given: mapFunction is called multiple times
        Function<Object, Map<Object, Object>> mapFunction = Functions.mapFunction();

        // When: apply the mapFunction to a key multiple times
        Map<Object, Object> result1 = mapFunction.apply(key);
        Map<Object, Object> result2 = mapFunction.apply(key);

        // Then: each result is a new HashMap instance
        assertNotNull(result1);
        assertTrue(result1 instanceof HashMap);
        assertEquals(0, result1.size());

        assertNotNull(result2);
        assertTrue(result2 instanceof HashMap);
        assertEquals(0, result2.size());

        assertNotSame(result1, result2);
    }

    @Test
    public void testIdentityMapFunction_MultipleCalls() {
        // Given: identityMapFunction is called multiple times
        Function<Object, IdentityHashMap<Object, Object>> identityMapFunction = Functions.identityMapFunction();

        // When: apply the identityMapFunction to a key multiple times
        IdentityHashMap<Object, Object> result1 = identityMapFunction.apply(key);
        IdentityHashMap<Object, Object> result2 = identityMapFunction.apply(key);

        // Then: each result is a new IdentityHashMap instance
        assertNotNull(result1);
        assertTrue(result1 instanceof IdentityHashMap);
        assertEquals(0, result1.size());

        assertNotNull(result2);
        assertTrue(result2 instanceof IdentityHashMap);
        assertEquals(0, result2.size());

        assertNotSame(result1, result2);
    }
}