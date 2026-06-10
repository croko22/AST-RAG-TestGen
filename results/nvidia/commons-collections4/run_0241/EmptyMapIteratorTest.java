import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.iterators.EmptyMapIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmptyMapIteratorTest {

    private EmptyMapIterator<String, String> emptyMapIterator;

    @BeforeEach
    void setup() {
        emptyMapIterator = (EmptyMapIterator<String, String>) EmptyMapIterator.emptyMapIterator();
    }

    @Test
    void testEmptyMapIterator() {
        // Given: empty map iterator
        // When: get key
        assertThrows(UnsupportedOperationException.class, () -> emptyMapIterator.getKey());
        
        // When: get value
        assertThrows(UnsupportedOperationException.class, () -> emptyMapIterator.getValue());
        
        // When: check if has next
        assertFalse(emptyMapIterator.hasNext());
        
        // When: get next
        assertThrows(UnsupportedOperationException.class, () -> emptyMapIterator.next());
        
        // When: remove
        assertThrows(UnsupportedOperationException.class, () -> emptyMapIterator.remove());
        
        // When: set value
        assertThrows(UnsupportedOperationException.class, () -> emptyMapIterator.setValue("value"));
    }

    @Test
    void testEmptyMapIteratorSingleton() {
        // Given: two instances of empty map iterator
        MapIterator<String, String> instance1 = EmptyMapIterator.emptyMapIterator();
        MapIterator<String, String> instance2 = EmptyMapIterator.emptyMapIterator();
        
        // Then: both instances should be the same
        assertSame(instance1, instance2);
    }
}