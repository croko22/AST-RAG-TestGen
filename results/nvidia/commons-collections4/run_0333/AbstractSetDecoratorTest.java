import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
import org.apache.commons.collections4.set.AbstractSetDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractSetDecoratorTest {

    @Mock
    private Set<String> set;

    private AbstractSetDecorator<String> abstractSetDecorator;

    @BeforeEach
    public void setup() {
        abstractSetDecorator = new AbstractSetDecorator<>(set) {
        };
    }

    @Test
    public void testEquals_SameInstance() {
        // Given: same instance
        // When: equals is called
        boolean result = abstractSetDecorator.equals(abstractSetDecorator);
        // Then: true is returned
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_SameElements() {
        // Given: different instance with same elements
        Set<String> sameSet = new HashSet<>(set);
        AbstractSetDecorator<String> sameDecorator = new AbstractSetDecorator<>(sameSet) {
        };
        // When: equals is called
        boolean result = abstractSetDecorator.equals(sameDecorator);
        // Then: true is returned
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentInstance_DifferentElements() {
        // Given: different instance with different elements
        Set<String> differentSet = new HashSet<>();
        differentSet.add("different");
        AbstractSetDecorator<String> differentDecorator = new AbstractSetDecorator<>(differentSet) {
        };
        when(set.equals(any())).thenReturn(false);
        // When: equals is called
        boolean result = abstractSetDecorator.equals(differentDecorator);
        // Then: false is returned
        assertFalse(result);
    }

    @Test
    public void testEquals_Null() {
        // Given: null instance
        // When: equals is called
        boolean result = abstractSetDecorator.equals(null);
        // Then: false is returned
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Given: set with elements
        when(set.hashCode()).thenReturn(123);
        // When: hashCode is called
        int result = abstractSetDecorator.hashCode();
        // Then: same hash code is returned
        assertEquals(123, result);
    }

    @Test
    public void testHashCode_DifferentInstances_SameElements() {
        // Given: different instances with same elements
        Set<String> sameSet = new HashSet<>(set);
        AbstractSetDecorator<String> sameDecorator = new AbstractSetDecorator<>(sameSet) {
        };
        when(set.hashCode()).thenReturn(123);
        when(sameSet.hashCode()).thenReturn(123);
        // When: hashCode is called
        int result1 = abstractSetDecorator.hashCode();
        int result2 = sameDecorator.hashCode();
        // Then: same hash code is returned
        assertEquals(result1, result2);
    }

    @Test
    public void testHashCode_DifferentInstances_DifferentElements() {
        // Given: different instances with different elements
        Set<String> differentSet = new HashSet<>();
        differentSet.add("different");
        AbstractSetDecorator<String> differentDecorator = new AbstractSetDecorator<>(differentSet) {
        };
        when(set.hashCode()).thenReturn(123);
        when(differentSet.hashCode()).thenReturn(456);
        // When: hashCode is called
        int result1 = abstractSetDecorator.hashCode();
        int result2 = differentDecorator.hashCode();
        // Then: different hash code is returned
        assertNotEquals(result1, result2);
    }
}