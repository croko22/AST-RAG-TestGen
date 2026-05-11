import org.apache.commons.dbutils.QueryLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QueryLoaderTest {

    @Mock
    private InputStream mockInputStream;

    @Mock
    private Properties mockProperties;

    private QueryLoader queryLoader;

    @BeforeEach
    void setup() {
        queryLoader = QueryLoader.instance();
    }

    @AfterEach
    void tearDown() {
        queryLoader.unload("/test.properties");
    }

    @Test
    void testInstance() {
        // Given: QueryLoader instance
        QueryLoader instance1 = QueryLoader.instance();
        QueryLoader instance2 = QueryLoader.instance();

        // Then: instances are the same
        assertSame(instance1, instance2);
    }

    @Test
    void testLoad() throws IOException {
        // Given: mock input stream
        when(mockInputStream.available()).thenReturn(10);
        when(mockProperties.load(any(InputStream.class))).thenReturn(10);

        // When: load queries
        Map<String, String> queries = queryLoader.load("/test.properties");

        // Then: queries are loaded
        assertNotNull(queries);
        assertTrue(queries.isEmpty());
    }

    @Test
    void testLoadQueries() throws IOException {
        // Given: mock input stream and properties
        when(mockInputStream.available()).thenReturn(10);
        when(mockProperties.load(any(InputStream.class))).thenReturn(10);

        // When: load queries
        Map<String, String> queries = queryLoader.load("/test.properties");

        // Then: queries are loaded
        assertNotNull(queries);
        assertTrue(queries.isEmpty());
    }

    @Test
    void testUnload() {
        // Given: loaded queries
        queryLoader.load("/test.properties");

        // When: unload queries
        queryLoader.unload("/test.properties");

        // Then: queries are unloaded
        assertTrue(queryLoader.load("/test.properties").isEmpty());
    }

    @Test
    void testLoadIOException() throws IOException {
        // Given: mock input stream
        when(mockInputStream.available()).thenReturn(-1);

        // When / Then: load queries throws IOException
        assertThrows(IOException.class, () -> queryLoader.load("/test.properties"));
    }

    @Test
    void testLoadQueriesIOException() throws IOException {
        // Given: mock input stream and properties
        when(mockInputStream.available()).thenReturn(-1);

        // When / Then: load queries throws IOException
        assertThrows(IOException.class, () -> queryLoader.load("/test.properties"));
    }

    @Test
    void testUnloadNullPath() {
        // Given: null path

        // When / Then: unload queries does not throw exception
        assertDoesNotThrow(() -> queryLoader.unload(null));
    }

    @Test
    void testLoadNullPath() {
        // Given: null path

        // When / Then: load queries throws NullPointerException
        assertThrows(NullPointerException.class, () -> queryLoader.load(null));
    }
}