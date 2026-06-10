import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.EntryFactory;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryFactoryTest {

    @Mock
    private EntryFactory<String, Geometry> entryFactory;

    @Mock
    private Geometry geometry;

    @BeforeEach
    void setup() {
        // No setup required for this test class
    }

    @Test
    public void testCreateEntry_Success() {
        // Given: a valid value and geometry
        String value = "testValue";
        Geometry geometry = mock(Geometry.class);

        // When: createEntry is called
        Entry<String, Geometry> entry = entryFactory.createEntry(value, geometry);

        // Then: the entry is not null
        assertNotNull(entry);
    }

    @Test
    public void testCreateEntry_NullValue() {
        // Given: a null value
        String value = null;
        Geometry geometry = mock(Geometry.class);

        // When / Then: createEntry throws an exception
        assertThrows(NullPointerException.class, () -> entryFactory.createEntry(value, geometry));
    }

    @Test
    public void testCreateEntry_NullGeometry() {
        // Given: a null geometry
        String value = "testValue";
        Geometry geometry = null;

        // When / Then: createEntry throws an exception
        assertThrows(NullPointerException.class, () -> entryFactory.createEntry(value, geometry));
    }

    @Test
    public void testCreateEntry_ImplementationSpecific() {
        // Given: a custom implementation of EntryFactory
        EntryFactory<String, Geometry> customEntryFactory = new EntryFactory<String, Geometry>() {
            @Override
            public Entry<String, Geometry> createEntry(String value, Geometry geometry) {
                return new Entry<String, Geometry>() {
                    @Override
                    public String value() {
                        return value;
                    }

                    @Override
                    public Geometry geometry() {
                        return geometry;
                    }
                };
            }
        };

        // When: createEntry is called
        Entry<String, Geometry> entry = customEntryFactory.createEntry("testValue", mock(Geometry.class));

        // Then: the entry is not null and has the correct value and geometry
        assertNotNull(entry);
        assertEquals("testValue", entry.value());
        assertNotNull(entry.geometry());
    }
}