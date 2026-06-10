package com.github.davidmoten.rtree.internal;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryDefaultTest {

    @Mock
    private Geometry geometryMock;

    @Mock
    private Rectangle rectangleMock;

    private Object value;

    @BeforeEach
    void setup() {
        value = new Object();
        when(geometryMock.distance(any(Rectangle.class))).thenReturn(0.0);
        when(geometryMock.mbr()).thenReturn(rectangleMock);
        when(geometryMock.intersects(any(Rectangle.class))).thenReturn(true);
        when(geometryMock.isDoublePrecision()).thenReturn(true);
    }

    @Test
    public void testEntry() {
        // Given
        EntryDefault<Object, Geometry> entry = EntryDefault.entry(value, geometryMock);

        // When
        EntryDefault<Object, Geometry> entryResult = EntryDefault.entry(value, geometryMock);

        // Then
        assertEquals(entry, entryResult);
        assertEquals(value, entry.value());
        assertEquals(geometryMock, entry.geometry());
    }

    @Test
    public void testValue() {
        // Given
        EntryDefault<Object, Geometry> entry = EntryDefault.entry(value, geometryMock);

        // When
        Object result = entry.value();

        // Then
        assertEquals(value, result);
    }

    @Test
    public void testGeometry() {
        // Given
        EntryDefault<Object, Geometry> entry = EntryDefault.entry(value, geometryMock);

        // When
        Geometry result = entry.geometry();

        // Then
        assertEquals(geometryMock, result);
    }

    @Test
    public void testToString() {
        // Given
        EntryDefault<Object, Geometry> entry = EntryDefault.entry(value, geometryMock);

        // When
        String result = entry.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("Entry [value="));
        assertTrue(result.contains(", geometry="));
    }

    @Test
    public void testHashCode() {
        // Given
        EntryDefault<Object, Geometry> entry = EntryDefault.entry(value, geometryMock);

        // When
        int result = entry.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testEquals_SameObject() {
        // Given
        EntryDefault<Object, Geometry> entry = EntryDefault.entry(value, geometryMock);

        // When
        boolean result = entry.equals(entry);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_SameValues() {
        // Given
        EntryDefault<Object, Geometry> entry1 = EntryDefault.entry(value, geometryMock);
        EntryDefault<Object, Geometry> entry2 = EntryDefault.entry(value, geometryMock);

        // When
        boolean result = entry1.equals(entry2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals_DifferentObject_DifferentValues() {
        // Given
        Object differentValue = new Object();
        EntryDefault<Object, Geometry> entry1 = EntryDefault.entry(value, geometryMock);
        EntryDefault<Object, Geometry> entry2 = EntryDefault.entry(differentValue, geometryMock);

        // When
        boolean result = entry1.equals(entry2);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_Null() {
        // Given
        EntryDefault<Object, Geometry> entry = EntryDefault.entry(value, geometryMock);

        // When
        boolean result = entry.equals(null);

        // Then
        assertFalse(result);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given
        EntryDefault<Object, Geometry> entry = EntryDefault.entry(value, geometryMock);
        Object differentClass = new Object();

        // When
        boolean result = entry.equals(differentClass);

        // Then
        assertFalse(result);
    }
}