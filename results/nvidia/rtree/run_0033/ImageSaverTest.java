package com.github.davidmoten.rtree;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageSaverTest {

    @Mock
    private Callable<Void> callableMock;

    @Mock
    private BufferedImage imageMock;

    @Mock
    private File fileMock;

    @BeforeEach
    void setup() {
        // No setup required
    }

    @AfterEach
    void tearDown() {
        // No tear down required
    }

    @Test
    public void testSave_ImageIOWriteSuccess() throws Exception {
        // Given: a callable that writes the image successfully
        when(callableMock.call()).thenReturn(null);

        // When: save is called with the image, file, and image format
        ImageSaver.save(imageMock, fileMock, "png");

        // Then: the callable is executed and no exception is thrown
        verifyStatic(ImageIO.class);
        ImageIO.write(imageMock, "png", fileMock);
    }

    @Test
    public void testSave_ImageIOWriteFailure() {
        // Given: a callable that throws an exception when writing the image
        when(callableMock.call()).thenThrow(new IOException("Mocked exception"));

        // When: save is called with the image, file, and image format
        assertThrows(RuntimeException.class, () -> ImageSaver.save(imageMock, fileMock, "png"));

        // Then: the callable is executed and an exception is thrown
        verifyStatic(ImageIO.class);
        ImageIO.write(imageMock, "png", fileMock);
    }

    @Test
    public void testRun_CallableSuccess() throws Exception {
        // Given: a callable that executes successfully
        when(callableMock.call()).thenReturn(null);

        // When: run is called with the callable
        ImageSaver.run(callableMock);

        // Then: the callable is executed and no exception is thrown
        verify(callableMock, times(1)).call();
    }

    @Test
    public void testRun_CallableFailure() {
        // Given: a callable that throws an exception when executed
        when(callableMock.call()).thenThrow(new IOException("Mocked exception"));

        // When: run is called with the callable
        assertThrows(RuntimeException.class, () -> ImageSaver.run(callableMock));

        // Then: the callable is executed and an exception is thrown
        verify(callableMock, times(1)).call();
    }
}