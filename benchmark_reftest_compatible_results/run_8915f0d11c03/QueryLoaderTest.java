import org.apache.commons.dbutils.QueryLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
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
    private ClassLoader classLoader;

    @InjectMocks
    private QueryLoader queryLoader;

    @BeforeEach
    void setup() {
        // Mock the ClassLoader to return a mock InputStream
        when(classLoader.getResourceAsStream(anyString())).thenAnswer(invocation -> {
            String path = invocation.getArgument(0);
            if (path.endsWith(".properties")) {
                return new ByteArrayInputStream("query1=SELECT * FROM table1\nquery2=SELECT * FROM table2".getBytes());
            } else if (path.endsWith(".xml")) {
                return new ByteArrayInputStream("<!DOCTYPE properties><properties><entry key='query1'>SELECT * FROM table1</entry><entry key='query2'>SELECT * FROM table2</entry></properties>".getBytes());
            } else {
                return null;
            }
        });
    }

    @AfterEach
    void tearDown() {
        // Reset the mock ClassLoader
        reset(classLoader);
    }

    @Test
    public void testInstance() {
        // Given: QueryLoader instance
        QueryLoader instance = QueryLoader.instance();

        // Then: instance is not null
        assertNotNull(instance);
    }

    @Test
    public void testLoad_LineOrientedPropertiesFile() throws IOException {
        // Given: line-oriented properties file path
        String path = "/com/yourcorp/app/jdbc/Queries.properties";

        // When: load queries from the properties file
        Map<String, String> queries = queryLoader.load(path);

        // Then: queries are loaded correctly
        assertNotNull(queries);
        assertEquals(2, queries.size());
        assertEquals("SELECT * FROM table1", queries.get("query1"));
        assertEquals("SELECT * FROM table2", queries.get("query2"));
    }

    @Test
    public void testLoad_XmlPropertiesFile() throws IOException {
        // Given: XML properties file path
        String path = "/com/yourcorp/app/jdbc/Queries.xml";

        // When: load queries from the XML properties file
        Map<String, String> queries = queryLoader.load(path);

        // Then: queries are loaded correctly
        assertNotNull(queries);
        assertEquals(2, queries.size());
        assertEquals("SELECT * FROM table1", queries.get("query1"));
        assertEquals("SELECT * FROM table2", queries.get("query2"));
    }

    @Test
    public void testLoad_NonExistentFile() {
        // Given: non-existent file path
        String path = "/com/yourcorp/app/jdbc/NonExistentFile.properties";

        // When: load queries from the non-existent file
        assertThrows(IllegalArgumentException.class, () -> queryLoader.load(path));
    }

    @Test
    public void testUnload() {
        // Given: loaded queries
        String path = "/com/yourcorp/app/jdbc/Queries.properties";
        queryLoader.load(path);

        // When: unload queries
        queryLoader.unload(path);

        // Then: queries are unloaded
        try {
            queryLoader.load(path);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoad_IOException() {
        // Given: mock InputStream that throws IOException
        when(classLoader.getResourceAsStream(anyString())).thenThrow(new IOException("Mocked IOException"));

        // When: load queries
        assertThrows(IOException.class, () -> queryLoader.load("/com/yourcorp/app/jdbc/Queries.properties"));
    }
}