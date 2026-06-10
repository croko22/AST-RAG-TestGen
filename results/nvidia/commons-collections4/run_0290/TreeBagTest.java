import org.apache.commons.collections4.bag.TreeBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TreeBagTest {

    @Mock
    private Comparator<String> comparatorMock;

    private TreeBag<String> treeBag;

    @BeforeEach
    void setup() {
        treeBag = new TreeBag<>();
    }

    @Test
    void testAdd() {
        // Given
        String object = "Test Object";

        // When
        boolean result = treeBag.add(object);

        // Then
        assertTrue(result);
    }

    @Test
    void testAddNull() {
        // Given
        String object = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> treeBag.add(object));
    }

    @Test
    void testAddNonComparable() {
        // Given
        Object object = new Object();

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> treeBag.add((String) object));
    }

    @Test
    void testComparator() {
        // Given
        TreeBag<String> treeBagWithComparator = new TreeBag<>(comparatorMock);

        // When
        Comparator<? super String> result = treeBagWithComparator.comparator();

        // Then
        assertSame(comparatorMock, result);
    }

    @Test
    void testFirst() {
        // Given
        treeBag.add("First");
        treeBag.add("Second");

        // When
        String result = treeBag.first();

        // Then
        assertEquals("First", result);
    }

    @Test
    void testFirstEmpty() {
        // Given

        // When and Then
        assertNull(treeBag.first());
    }

    @Test
    void testLast() {
        // Given
        treeBag.add("First");
        treeBag.add("Second");

        // When
        String result = treeBag.last();

        // Then
        assertEquals("Second", result);
    }

    @Test
    void testLastEmpty() {
        // Given

        // When and Then
        assertNull(treeBag.last());
    }
}