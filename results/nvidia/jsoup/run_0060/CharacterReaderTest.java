import org.jsoup.parser.CharacterReader;
import org.jsoup.internal.SoftPool;
import org.jsoup.internal.StringUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterReaderTest {

    @Mock
    private SoftPool<String[]> stringPool;

    @Mock
    private SoftPool<char[]> bufferPool;

    private CharacterReader characterReader;

    @BeforeEach
    void setup() {
        // Mock SoftPool to return a new array
        when(stringPool.borrow()).thenReturn(new String[512]);
        when(bufferPool.borrow()).thenReturn(new char[2048]);

        characterReader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);
    }

    @AfterEach
    void tearDown() {
        characterReader.close();
    }

    @Test
    void testClose() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        reader.close();

        // Then
        verify(bufferPool).release(any());
        verify(stringPool).release(any());
    }

    @Test
    void testPos() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        int position = reader.pos();

        // Then
        assertEquals(0, position);
    }

    @Test
    void testTrackNewlines() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello\nWorld"), bufferPool, stringPool);

        // When
        reader.trackNewlines(true);

        // Then
        assertTrue(reader.isTrackNewlines());
    }

    @Test
    void testIsTrackNewlines() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        boolean trackNewlines = reader.isTrackNewlines();

        // Then
        assertFalse(trackNewlines);
    }

    @Test
    void testLineNumber() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello\nWorld"), bufferPool, stringPool);
        reader.trackNewlines(true);

        // When
        int lineNumber = reader.lineNumber();

        // Then
        assertEquals(1, lineNumber);
    }

    @Test
    void testColumnNumber() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        int columnNumber = reader.columnNumber();

        // Then
        assertEquals(1, columnNumber);
    }

    @Test
    void testIsEmpty() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader(""), bufferPool, stringPool);

        // When
        boolean isEmpty = reader.isEmpty();

        // Then
        assertTrue(isEmpty);
    }

    @Test
    void testCurrent() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        char current = reader.current();

        // Then
        assertEquals('H', current);
    }

    @Test
    void testConsume() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        char consumed = reader.consume();

        // Then
        assertEquals('H', consumed);
    }

    @Test
    void testAdvance() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        reader.advance();

        // Then
        assertEquals(1, reader.pos());
    }

    @Test
    void testConsumeTo() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        String consumed = reader.consumeTo(' ');

        // Then
        assertEquals("Hello", consumed);
    }

    @Test
    void testConsumeToAny() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        String consumed = reader.consumeToAny(' ', '\n');

        // Then
        assertEquals("Hello", consumed);
    }

    @Test
    void testToString() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"), bufferPool, stringPool);

        // When
        String string = reader.toString();

        // Then
        assertEquals("Hello World", string);
    }
}