package org.jsoup.parser;

import org.jsoup.internal.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenDataTest {

    @Mock
    private StringUtil stringUtil;

    private TokenData tokenData;

    @BeforeEach
    void setup() {
        tokenData = new TokenData();
    }

    @Test
    public void testToString_NoData() {
        // Given: no data in tokenData
        // When: toString is called
        String result = tokenData.toString();
        // Then: empty string is returned
        assertEquals("", result);
    }

    @Test
    public void testToString_WithValue() {
        // Given: value is set in tokenData
        tokenData.set("test");
        // When: toString is called
        String result = tokenData.toString();
        // Then: value is returned
        assertEquals("test", result);
    }

    @Test
    public void testToString_WithBuilder() {
        // Given: builder is set in tokenData
        tokenData.append("test");
        // When: toString is called
        String result = tokenData.toString();
        // Then: builder's string is returned
        assertEquals("test", result);
    }

    @Test
    public void testSet() {
        // Given: no data in tokenData
        // When: set is called
        tokenData.set("test");
        // Then: value is set
        assertEquals("test", tokenData.value());
    }

    @Test
    public void testAppend_String() {
        // Given: no data in tokenData
        // When: append is called with string
        tokenData.append("test");
        // Then: value is set
        assertEquals("test", tokenData.value());
    }

    @Test
    public void testAppend_String_WithValue() {
        // Given: value is set in tokenData
        tokenData.set("test");
        // When: append is called with string
        tokenData.append("ing");
        // Then: builder is used and value is appended
        assertEquals("testing", tokenData.value());
    }

    @Test
    public void testAppend_Char() {
        // Given: no data in tokenData
        // When: append is called with char
        tokenData.append('t');
        // Then: value is set
        assertEquals("t", tokenData.value());
    }

    @Test
    public void testAppend_Char_WithValue() {
        // Given: value is set in tokenData
        tokenData.set("te");
        // When: append is called with char
        tokenData.append('s');
        // Then: builder is used and value is appended
        assertEquals("tes", tokenData.value());
    }

    @Test
    public void testAppend_CodePoint() {
        // Given: no data in tokenData
        // When: append is called with code point
        tokenData.appendCodePoint(116); // 't'
        // Then: value is set
        assertEquals("t", tokenData.value());
    }

    @Test
    public void testAppend_CodePoint_WithValue() {
        // Given: value is set in tokenData
        tokenData.set("te");
        // When: append is called with code point
        tokenData.appendCodePoint(115); // 's'
        // Then: builder is used and value is appended
        assertEquals("tes", tokenData.value());
    }

    @Test
    public void testHasData_NoData() {
        // Given: no data in tokenData
        // When: hasData is called
        boolean result = tokenData.hasData();
        // Then: false is returned
        assertFalse(result);
    }

    @Test
    public void testHasData_WithValue() {
        // Given: value is set in tokenData
        tokenData.set("test");
        // When: hasData is called
        boolean result = tokenData.hasData();
        // Then: true is returned
        assertTrue(result);
    }

    @Test
    public void testReset() {
        // Given: value is set in tokenData
        tokenData.set("test");
        // When: reset is called
        tokenData.reset();
        // Then: value is reset
        assertEquals("", tokenData.value());
    }

    @Test
    public void testValue_NoData() {
        // Given: no data in tokenData
        // When: value is called
        String result = tokenData.value();
        // Then: empty string is returned
        assertEquals("", result);
    }

    @Test
    public void testValue_WithValue() {
        // Given: value is set in tokenData
        tokenData.set("test");
        // When: value is called
        String result = tokenData.value();
        // Then: value is returned
        assertEquals("test", result);
    }

    @Test
    public void testValue_WithBuilder() {
        // Given: builder is set in tokenData
        tokenData.append("test");
        // When: value is called
        String result = tokenData.value();
        // Then: builder's string is returned
        assertEquals("test", result);
    }
}