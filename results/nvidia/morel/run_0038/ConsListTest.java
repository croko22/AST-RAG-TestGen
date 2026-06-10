import net.hydromatic.morel.util.ConsList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class ConsListTest {

    private ConsList<String> consList;

    @BeforeEach
    void setup() {
        List<String> rest = new ArrayList<>(Arrays.asList("b", "c"));
        consList = ConsList.of("a", rest);
    }

    @Test
    void testOf() {
        // Given
        String first = "a";
        List<String> rest = new ArrayList<>(Arrays.asList("b", "c"));

        // When
        ConsList<String> result = ConsList.of(first, rest);

        // Then
        assertNotNull(result);
        assertEquals(first, result.get(0));
        assertEquals(rest.get(0), result.get(1));
        assertEquals(rest.get(1), result.get(2));
    }

    @Test
    void testGet() {
        // Given
        int index = 1;

        // When
        String result = consList.get(index);

        // Then
        assertEquals("b", result);
    }

    @Test
    void testGet_IndexOutOfBoundsException() {
        // Given
        int index = 5;

        // When and Then
        assertThrows(IndexOutOfBoundsException.class, () -> consList.get(index));
    }

    @Test
    void testSize() {
        // Given

        // When
        int result = consList.size();

        // Then
        assertEquals(3, result);
    }

    @Test
    void testHashCode() {
        // Given

        // When
        int result = consList.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    void testEquals() {
        // Given
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

        // When
        boolean result = consList.equals(list);

        // Then
        assertTrue(result);
    }

    @Test
    void testToString() {
        // Given

        // When
        String result = consList.toString();

        // Then
        assertEquals("[a, b, c]", result);
    }

    @Test
    void testSubList() {
        // Given
        int fromIndex = 1;
        int toIndex = 2;

        // When
        List<String> result = consList.subList(fromIndex, toIndex);

        // Then
        assertEquals(1, result.size());
        assertEquals("b", result.get(0));
    }

    @Test
    void testListIterator() {
        // Given

        // When
        ListIterator<String> result = consList.listIterator();

        // Then
        assertNotNull(result);
        assertTrue(result.hasNext());
        assertEquals("a", result.next());
    }

    @Test
    void testIterator() {
        // Given

        // When
        java.util.Iterator<String> result = consList.iterator();

        // Then
        assertNotNull(result);
        assertTrue(result.hasNext());
        assertEquals("a", result.next());
    }

    @Test
    void testListIterator_Index() {
        // Given
        int index = 1;

        // When
        ListIterator<String> result = consList.listIterator(index);

        // Then
        assertNotNull(result);
        assertTrue(result.hasNext());
        assertEquals("b", result.next());
    }

    @Test
    void testToArray() {
        // Given

        // When
        Object[] result = consList.toArray();

        // Then
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("c", result[2]);
    }

    @Test
    void testToArray_Type() {
        // Given
        String[] array = new String[3];

        // When
        String[] result = (String[]) consList.toArray(array);

        // Then
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals("c", result[2]);
    }

    @Test
    void testIndexOf() {
        // Given
        String element = "b";

        // When
        int result = consList.indexOf(element);

        // Then
        assertEquals(1, result);
    }

    @Test
    void testLastIndexOf() {
        // Given
        String element = "b";

        // When
        int result = consList.lastIndexOf(element);

        // Then
        assertEquals(1, result);
    }
}