import org.apache.commons.collections4.list.CursorableLinkedList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CursorableLinkedListTest {

    @Mock
    private CursorableLinkedList<String> cursorableLinkedList;

    @BeforeEach
    void setup() {
        cursorableLinkedList = new CursorableLinkedList<>();
    }

    @AfterEach
    void tearDown() {
        cursorableLinkedList = null;
    }

    @Test
    public void testAdd() {
        // Given
        String element = "Test Element";

        // When
        cursorableLinkedList.add(element);

        // Then
        assertEquals(1, cursorableLinkedList.size());
    }

    @Test
    public void testClose() {
        // Given
        CursorableLinkedList.Cursor<String> cursor = cursorableLinkedList.cursor();

        // When
        cursor.close();

        // Then
        assertFalse(cursor.valid);
    }

    @Test
    public void testNextIndex() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);

        // When
        CursorableLinkedList.Cursor<String> cursor = cursorableLinkedList.cursor();

        // Then
        assertEquals(0, cursor.nextIndex());
    }

    @Test
    public void testRemove() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);
        CursorableLinkedList.Cursor<String> cursor = cursorableLinkedList.cursor();
        cursor.next();

        // When
        cursor.remove();

        // Then
        assertEquals(0, cursorableLinkedList.size());
    }

    @Test
    public void testCursor() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);

        // When
        CursorableLinkedList.Cursor<String> cursor = cursorableLinkedList.cursor();

        // Then
        assertNotNull(cursor);
    }

    @Test
    public void testCursorWithIndex() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);

        // When
        CursorableLinkedList.Cursor<String> cursor = cursorableLinkedList.cursor(0);

        // Then
        assertNotNull(cursor);
    }

    @Test
    public void testIterator() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);

        // When
        Iterator<String> iterator = cursorableLinkedList.iterator();

        // Then
        assertNotNull(iterator);
    }

    @Test
    public void testListIterator() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);

        // When
        ListIterator<String> listIterator = cursorableLinkedList.listIterator();

        // Then
        assertNotNull(listIterator);
    }

    @Test
    public void testListIteratorWithIndex() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);

        // When
        ListIterator<String> listIterator = cursorableLinkedList.listIterator(0);

        // Then
        assertNotNull(listIterator);
    }

    @Test
    public void testCursorHasNext() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);
        CursorableLinkedList.Cursor<String> cursor = cursorableLinkedList.cursor();

        // When
        boolean hasNext = cursor.hasNext();

        // Then
        assertTrue(hasNext);
    }

    @Test
    public void testCursorHasPrevious() {
        // Given
        String element = "Test Element";
        cursorableLinkedList.add(element);
        CursorableLinkedList.Cursor<String> cursor = cursorableLinkedList.cursor();
        cursor.next();

        // When
        boolean hasPrevious = cursor.hasPrevious();

        // Then
        assertTrue(hasPrevious);
    }
}