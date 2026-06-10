import com.github.davidmoten.rtree.internal.util.ObjectsHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ObjectsHelperTest {

    private Object object;
    private Class<?> cls;

    @BeforeEach
    public void setup() {
        object = "test";
        cls = String.class;
    }

    @Test
    public void testAsClass_NullObject() {
        // Given: object is null
        Object nullObject = null;
        // When: asClass is called with null object
        Optional<?> result = ObjectsHelper.asClass(nullObject, cls);
        // Then: result is empty
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAsClass_MatchingClass() {
        // Given: object is of matching class
        // When: asClass is called with matching object
        Optional<?> result = ObjectsHelper.asClass(object, cls);
        // Then: result is present and matches the object
        assertTrue(result.isPresent());
        assertEquals(object, result.get());
    }

    @Test
    public void testAsClass_NonMatchingClass() {
        // Given: object is of non-matching class
        Class<?> nonMatchingCls = Integer.class;
        // When: asClass is called with non-matching object
        Optional<?> result = ObjectsHelper.asClass(object, nonMatchingCls);
        // Then: result is empty
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAsClass_SameObjectDifferentClass() {
        // Given: object is of same type but different class
        Object sameObject = "test";
        Class<?> sameCls = String.class;
        // When: asClass is called with same object and same class
        Optional<?> result = ObjectsHelper.asClass(sameObject, sameCls);
        // Then: result is present and matches the object
        assertTrue(result.isPresent());
        assertEquals(sameObject, result.get());
    }

    @Test
    public void testAsClass_NullClass() {
        // Given: class is null
        Class<?> nullCls = null;
        // When: asClass is called with null class
        assertThrows(NullPointerException.class, () -> ObjectsHelper.asClass(object, nullCls));
    }
}