import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.EntryDefault;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntriesTest {

    @Mock
    private Geometry geometry;

    @Mock
    private EntryDefault entryDefault;

    @Test
    public void testEntry() {
        // Given
        Object object = new Object();
        when(EntryDefault.entry(any(), any())).thenReturn(entryDefault);

        // When
        EntryDefault result = Entries.entry(object, geometry);

        // Then
        assertEquals(entryDefault, result);
        verifyStatic(EntryDefault.class);
        EntryDefault.entry(object, geometry);
    }

    @Test
    public void testEntry_NullObject() {
        // Given
        Object object = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> Entries.entry(object, geometry));
    }

    @Test
    public void testEntry_NullGeometry() {
        // Given
        Object object = new Object();
        Geometry geometry = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> Entries.entry(object, geometry));
    }
}