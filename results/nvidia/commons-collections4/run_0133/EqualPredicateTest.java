import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EqualPredicateTest {

    @Mock
    private Equator<String> equatorMock;

    @Mock
    private Predicate<String> predicateMock;

    private EqualPredicate<String> equalPredicate;

    @BeforeEach
    void setup() {
        equalPredicate = new EqualPredicate<>("test");
    }

    @Test
    public void testEqualPredicate_NullObject() {
        // Given: null object
        // When: create equal predicate
        Predicate<Object> predicate = EqualPredicate.equalPredicate(null);
        // Then: verify null predicate
        assertNotNull(predicate);
    }

    @Test
    public void testEqualPredicate_Object() {
        // Given: non-null object
        String object = "test";
        // When: create equal predicate
        Predicate<String> predicate = EqualPredicate.equalPredicate(object);
        // Then: verify equal predicate
        assertNotNull(predicate);
        assertTrue(predicate.test(object));
    }

    @Test
    public void testEqualPredicate_ObjectAndEquator() {
        // Given: non-null object and equator
        String object = "test";
        // When: create equal predicate with equator
        Predicate<String> predicate = EqualPredicate.equalPredicate(object, equatorMock);
        // Then: verify equal predicate with equator
        assertNotNull(predicate);
        when(equatorMock.equate(any(), any())).thenReturn(true);
        assertTrue(predicate.test(object));
    }

    @Test
    public void testGetValue() {
        // Given: equal predicate
        // When: get value
        Object value = equalPredicate.getValue();
        // Then: verify value
        assertNotNull(value);
        assertEquals("test", value);
    }

    @Test
    public void testTest_Object() {
        // Given: equal predicate and object
        String object = "test";
        // When: test object
        boolean result = equalPredicate.test(object);
        // Then: verify result
        assertTrue(result);
    }

    @Test
    public void testTest_ObjectWithEquator() {
        // Given: equal predicate with equator and object
        EqualPredicate<String> predicateWithEquator = new EqualPredicate<>("test", equatorMock);
        String object = "test";
        // When: test object with equator
        when(equatorMock.equate(any(), any())).thenReturn(true);
        boolean result = predicateWithEquator.test(object);
        // Then: verify result
        assertTrue(result);
    }

    @Test
    public void testTest_NullObject() {
        // Given: equal predicate and null object
        // When: test null object
        boolean result = equalPredicate.test(null);
        // Then: verify result
        assertFalse(result);
    }

    @Test
    public void testTest_DifferentObject() {
        // Given: equal predicate and different object
        String differentObject = "different";
        // When: test different object
        boolean result = equalPredicate.test(differentObject);
        // Then: verify result
        assertFalse(result);
    }
}