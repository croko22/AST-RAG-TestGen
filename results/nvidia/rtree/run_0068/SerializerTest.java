package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.InternalStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SerializerTest {

    @Mock
    private RTree<String, Geometry> tree;

    @Mock
    private Geometry geometry;

    @Mock
    private InternalStructure structure;

    private Serializer<String, Geometry> serializer;

    @BeforeEach
    public void setup() {
        serializer = new Serializer<String, Geometry>() {
            @Override
            public void write(RTree<String, Geometry> tree, OutputStream os) throws IOException {
                // implementación por defecto para testing
            }

            @Override
            public RTree<String, Geometry> read(InputStream is, long sizeBytes, InternalStructure structure) throws IOException {
                // implementación por defecto para testing
                return tree;
            }
        };
    }

    @Test
    public void testWrite() throws IOException {
        // Given
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        doNothing().when(tree).write(any(OutputStream.class));

        // When
        serializer.write(tree, os);

        // Then
        verify(tree, times(1)).write(any(OutputStream.class));
    }

    @Test
    public void testWrite_IOException() throws IOException {
        // Given
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        doThrow(new IOException()).when(tree).write(any(OutputStream.class));

        // When / Then
        assertThrows(IOException.class, () -> serializer.write(tree, os));
        verify(tree, times(1)).write(any(OutputStream.class));
    }

    @Test
    public void testRead() throws IOException {
        // Given
        byte[] bytes = new byte[10];
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        when(tree.read(any(InputStream.class), anyLong(), any(InternalStructure.class))).thenReturn(tree);

        // When
        RTree<String, Geometry> result = serializer.read(is, 10, structure);

        // Then
        assertEquals(tree, result);
        verify(tree, times(1)).read(any(InputStream.class), anyLong(), any(InternalStructure.class));
    }

    @Test
    public void testRead_IOException() throws IOException {
        // Given
        byte[] bytes = new byte[10];
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        doThrow(new IOException()).when(tree).read(any(InputStream.class), anyLong(), any(InternalStructure.class));

        // When / Then
        assertThrows(IOException.class, () -> serializer.read(is, 10, structure));
        verify(tree, times(1)).read(any(InputStream.class), anyLong(), any(InternalStructure.class));
    }

    @Test
    public void testRead_NullInputStream() {
        // Given
        InputStream is = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> serializer.read(is, 10, structure));
    }

    @Test
    public void testRead_NullStructure() throws IOException {
        // Given
        byte[] bytes = new byte[10];
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        InternalStructure nullStructure = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> serializer.read(is, 10, nullStructure));
    }
}