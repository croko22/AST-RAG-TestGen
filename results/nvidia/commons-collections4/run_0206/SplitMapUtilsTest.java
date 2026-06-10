import org.apache.commons.collections4.Get;
import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.Put;
import org.apache.commons.collections4.SplitMapUtils;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections4.map.EntrySetToMapIteratorAdapter;
import org.apache.commons.collections4.map.UnmodifiableEntrySet;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SplitMapUtilsTest {

    @Mock
    private Get<String, String> get;

    @Mock
    private Put<String, String> put;

    @BeforeEach
    void setup() {
        // Initialize mocks
    }

    @Test
    void testReadableMap_GetIsMap() {
        // Given
        when(get instanceof Map.class).thenReturn(true);
        when(get instanceof IterableMap.class).thenReturn(true);

        // When
        IterableMap<String, String> result = SplitMapUtils.readableMap(get);

        // Then
        assertSame(get, result);
    }

    @Test
    void testReadableMap_GetIsNotMap() {
        // Given
        when(get instanceof Map.class).thenReturn(false);

        // When
        IterableMap<String, String> result = SplitMapUtils.readableMap(get);

        // Then
        assertNotNull(result);
        assertNotSame(get, result);
    }

    @Test
    void testWritableMap_PutIsMap() {
        // Given
        when(put instanceof Map.class).thenReturn(true);

        // When
        Map<String, String> result = SplitMapUtils.writableMap(put);

        // Then
        assertSame(put, result);
    }

    @Test
    void testWritableMap_PutIsNotMap() {
        // Given
        when(put instanceof Map.class).thenReturn(false);

        // When
        Map<String, String> result = SplitMapUtils.writableMap(put);

        // Then
        assertNotNull(result);
        assertNotSame(put, result);
    }

    @Test
    void testReadableMap_NullGet() {
        // Given
        Get<String, String> nullGet = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> SplitMapUtils.readableMap(nullGet));
    }

    @Test
    void testWritableMap_NullPut() {
        // Given
        Put<String, String> nullPut = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> SplitMapUtils.writableMap(nullPut));
    }

    @Test
    void testWrappedGet_Clear() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);

        // When and Then
        assertThrows(UnsupportedOperationException.class, wrappedGet::clear);
    }

    @Test
    void testWrappedGet_ContainsKey() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);
        String key = "key";

        // When
        boolean result = wrappedGet.containsKey(key);

        // Then
        verify(get).containsKey(key);
        assertTrue(result);
    }

    @Test
    void testWrappedGet_ContainsValue() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);
        String value = "value";

        // When
        boolean result = wrappedGet.containsValue(value);

        // Then
        verify(get).containsValue(value);
        assertTrue(result);
    }

    @Test
    void testWrappedGet_EntrySet() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);

        // When
        Set<Map.Entry<String, String>> result = wrappedGet.entrySet();

        // Then
        assertNotNull(result);
        assertNotSame(get.entrySet(), result);
    }

    @Test
    void testWrappedGet_Equals() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);
        Object obj = wrappedGet;

        // When
        boolean result = wrappedGet.equals(obj);

        // Then
        assertTrue(result);
    }

    @Test
    void testWrappedGet_Get() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);
        String key = "key";

        // When
        String result = wrappedGet.get(key);

        // Then
        verify(get).get(key);
        assertNotNull(result);
    }

    @Test
    void testWrappedGet_HashCode() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);

        // When
        int result = wrappedGet.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    void testWrappedGet_IsEmpty() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);

        // When
        boolean result = wrappedGet.isEmpty();

        // Then
        verify(get).isEmpty();
        assertTrue(result);
    }

    @Test
    void testWrappedGet_KeySet() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);

        // When
        Set<String> result = wrappedGet.keySet();

        // Then
        assertNotNull(result);
        assertNotSame(get.keySet(), result);
    }

    @Test
    void testWrappedGet_MapIterator() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);

        // When
        MapIterator<String, String> result = wrappedGet.mapIterator();

        // Then
        assertNotNull(result);
    }

    @Test
    void testWrappedGet_Put() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);
        String key = "key";
        String value = "value";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> wrappedGet.put(key, value));
    }

    @Test
    void testWrappedGet_PutAll() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);
        Map<String, String> map = new HashMap<>();

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> wrappedGet.putAll(map));
    }

    @Test
    void testWrappedGet_Remove() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);
        String key = "key";

        // When
        String result = wrappedGet.remove(key);

        // Then
        verify(get).remove(key);
        assertNotNull(result);
    }

    @Test
    void testWrappedGet_Size() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);

        // When
        int result = wrappedGet.size();

        // Then
        verify(get).size();
        assertNotNull(result);
    }

    @Test
    void testWrappedGet_Values() {
        // Given
        WrappedGet<String, String> wrappedGet = new SplitMapUtils.WrappedGet<>(get);

        // When
        Collection<String> result = wrappedGet.values();

        // Then
        assertNotNull(result);
        assertNotSame(get.values(), result);
    }

    @Test
    void testWrappedPut_Clear() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);

        // When
        wrappedPut.clear();

        // Then
        verify(put).clear();
    }

    @Test
    void testWrappedPut_ContainsKey() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);
        String key = "key";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> wrappedPut.containsKey(key));
    }

    @Test
    void testWrappedPut_ContainsValue() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);
        String value = "value";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> wrappedPut.containsValue(value));
    }

    @Test
    void testWrappedPut_EntrySet() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);

        // When and Then
        assertThrows(UnsupportedOperationException.class, wrappedPut::entrySet);
    }

    @Test
    void testWrappedPut_Equals() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);
        Object obj = wrappedPut;

        // When
        boolean result = wrappedPut.equals(obj);

        // Then
        assertTrue(result);
    }

    @Test
    void testWrappedPut_Get() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);
        String key = "key";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> wrappedPut.get(key));
    }

    @Test
    void testWrappedPut_HashCode() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);

        // When
        int result = wrappedPut.hashCode();

        // Then
        assertNotNull(result);
    }

    @Test
    void testWrappedPut_IsEmpty() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);

        // When and Then
        assertThrows(UnsupportedOperationException.class, wrappedPut::isEmpty);
    }

    @Test
    void testWrappedPut_KeySet() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);

        // When and Then
        assertThrows(UnsupportedOperationException.class, wrappedPut::keySet);
    }

    @Test
    void testWrappedPut_Put() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);
        String key = "key";
        String value = "value";

        // When
        String result = wrappedPut.put(key, value);

        // Then
        verify(put).put(key, value);
        assertNotNull(result);
    }

    @Test
    void testWrappedPut_PutAll() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);
        Map<String, String> map = new HashMap<>();

        // When
        wrappedPut.putAll(map);

        // Then
        verify(put).putAll(map);
    }

    @Test
    void testWrappedPut_Remove() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);
        String key = "key";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> wrappedPut.remove(key));
    }

    @Test
    void testWrappedPut_Size() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);

        // When and Then
        assertThrows(UnsupportedOperationException.class, wrappedPut::size);
    }

    @Test
    void testWrappedPut_Values() {
        // Given
        WrappedPut<String, String> wrappedPut = new SplitMapUtils.WrappedPut<>(put);

        // When and Then
        assertThrows(UnsupportedOperationException.class, wrappedPut::values);
    }
}