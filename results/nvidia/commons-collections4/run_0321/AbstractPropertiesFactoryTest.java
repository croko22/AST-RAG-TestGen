import org.apache.commons.collections4.properties.AbstractPropertiesFactory;
import org.apache.commons.collections4.properties.PropertyFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractPropertiesFactoryTest {

    @Mock
    private ClassLoader classLoader;

    @Mock
    private File file;

    @Mock
    private InputStream inputStream;

    @Mock
    private Path path;

    @Mock
    private Reader reader;

    @Mock
    private URI uri;

    @Mock
    private URL url;

    @InjectMocks
    private AbstractPropertiesFactory<Properties> abstractPropertiesFactory;

    @BeforeEach
    void setup() {
        abstractPropertiesFactory = new AbstractPropertiesFactory<Properties>() {
            @Override
            protected Properties createProperties() {
                return new Properties();
            }
        };
    }

    @Test
    void testLoad_ClassLoader_Name() throws IOException {
        // Given
        when(classLoader.getResourceAsStream(any())).thenReturn(new ByteArrayInputStream("key=value".getBytes()));

        // When
        Properties properties = abstractPropertiesFactory.load(classLoader, "test.properties");

        // Then
        assertNotNull(properties);
        assertEquals("value", properties.getProperty("key"));
    }

    @Test
    void testLoad_ClassLoader_Name_IOException() throws IOException {
        // Given
        when(classLoader.getResourceAsStream(any())).thenThrow(new IOException("Test exception"));

        // When / Then
        assertThrows(IOException.class, () -> abstractPropertiesFactory.load(classLoader, "test.properties"));
    }

    @Test
    void testLoad_File() throws IOException {
        // Given
        when(file.toPath()).thenReturn(Paths.get("test.properties"));
        when(Files.newInputStream(any())).thenReturn(new FileInputStream("test.properties"));

        // When
        Properties properties = abstractPropertiesFactory.load(file);

        // Then
        assertNotNull(properties);
    }

    @Test
    void testLoad_File_IOException() throws IOException {
        // Given
        when(file.toPath()).thenReturn(Paths.get("test.properties"));
        when(Files.newInputStream(any())).thenThrow(new IOException("Test exception"));

        // When / Then
        assertThrows(IOException.class, () -> abstractPropertiesFactory.load(file));
    }

    @Test
    void testLoad_InputStream() throws IOException {
        // Given
        when(inputStream.read(any())).thenReturn("key=value".getBytes()[0]);

        // When
        Properties properties = abstractPropertiesFactory.load(inputStream);

        // Then
        assertNotNull(properties);
        assertEquals("value", properties.getProperty("key"));
    }

    @Test
    void testLoad_InputStream_Null() throws IOException {
        // Given
        when(inputStream).thenReturn(null);

        // When
        Properties properties = abstractPropertiesFactory.load(inputStream);

        // Then
        assertEquals(null, properties);
    }

    @Test
    void testLoad_InputStream_PropertyFormat() throws IOException {
        // Given
        when(inputStream.read(any())).thenReturn("key=value".getBytes()[0]);

        // When
        Properties properties = abstractPropertiesFactory.load(inputStream, PropertyFormat.PROPERTIES);

        // Then
        assertNotNull(properties);
        assertEquals("value", properties.getProperty("key"));
    }

    @Test
    void testLoad_InputStream_PropertyFormat_Null() throws IOException {
        // Given
        when(inputStream).thenReturn(null);

        // When
        Properties properties = abstractPropertiesFactory.load(inputStream, PropertyFormat.PROPERTIES);

        // Then
        assertEquals(null, properties);
    }

    @Test
    void testLoad_Path() throws IOException {
        // Given
        when(path.getFileName()).thenReturn(Paths.get("test.properties").getFileName());
        when(Files.newInputStream(any())).thenReturn(new FileInputStream("test.properties"));

        // When
        Properties properties = abstractPropertiesFactory.load(path);

        // Then
        assertNotNull(properties);
    }

    @Test
    void testLoad_Path_IOException() throws IOException {
        // Given
        when(path.getFileName()).thenReturn(Paths.get("test.properties").getFileName());
        when(Files.newInputStream(any())).thenThrow(new IOException("Test exception"));

        // When / Then
        assertThrows(IOException.class, () -> abstractPropertiesFactory.load(path));
    }

    @Test
    void testLoad_Reader() throws IOException {
        // Given
        when(reader.read(any())).thenReturn("key=value".getBytes()[0]);

        // When
        Properties properties = abstractPropertiesFactory.load(reader);

        // Then
        assertNotNull(properties);
        assertEquals("value", properties.getProperty("key"));
    }

    @Test
    void testLoad_Reader_Null() throws IOException {
        // Given
        when(reader).thenReturn(null);

        // When / Then
        assertThrows(NullPointerException.class, () -> abstractPropertiesFactory.load(reader));
    }

    @Test
    void testLoad_String() throws IOException {
        // Given
        when(Files.newInputStream(any())).thenReturn(new FileInputStream("test.properties"));

        // When
        Properties properties = abstractPropertiesFactory.load("test.properties");

        // Then
        assertNotNull(properties);
    }

    @Test
    void testLoad_String_IOException() throws IOException {
        // Given
        when(Files.newInputStream(any())).thenThrow(new IOException("Test exception"));

        // When / Then
        assertThrows(IOException.class, () -> abstractPropertiesFactory.load("test.properties"));
    }

    @Test
    void testLoad_URI() throws IOException {
        // Given
        when(uri.toURL()).thenReturn(new URL("file:test.properties"));
        when(Files.newInputStream(any())).thenReturn(new FileInputStream("test.properties"));

        // When
        Properties properties = abstractPropertiesFactory.load(uri);

        // Then
        assertNotNull(properties);
    }

    @Test
    void testLoad_URI_IOException() throws IOException {
        // Given
        when(uri.toURL()).thenReturn(new URL("file:test.properties"));
        when(Files.newInputStream(any())).thenThrow(new IOException("Test exception"));

        // When / Then
        assertThrows(IOException.class, () -> abstractPropertiesFactory.load(uri));
    }

    @Test
    void testLoad_URL() throws IOException {
        // Given
        when(url.openStream()).thenReturn(new FileInputStream("test.properties"));

        // When
        Properties properties = abstractPropertiesFactory.load(url);

        // Then
        assertNotNull(properties);
    }

    @Test
    void testLoad_URL_IOException() throws IOException {
        // Given
        when(url.openStream()).thenThrow(new IOException("Test exception"));

        // When / Then
        assertThrows(IOException.class, () -> abstractPropertiesFactory.load(url));
    }
}