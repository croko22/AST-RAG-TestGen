import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.AbstractMultiValuedMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractMultiValuedMapDecoratorTest {

    @Mock
    private MultiValuedMap<String, String> multiValuedMap;

    private AbstractMultiValuedMapDecorator<String, String> decorator;

    @BeforeEach
    public void setup() {
        decorator = new AbstractMultiValuedMapDecorator<>(multiValuedMap) {
        };
    }

    @Test
    public void testAsMap() {
        // Given
        Map<String, Collection<String>> expectedMap = mock(Map.class);
        when(multiValuedMap.asMap()).thenReturn(expectedMap);

        // When
        Map<String, Collection<String>> actualMap = decorator.asMap();

        // Then
        assertEquals(expectedMap, actualMap);
        verify(multiValuedMap, times(1)).asMap();
    }

    @Test
    public void testClear() {
        // When
        decorator.clear();

        // Then
        verify(multiValuedMap, times(1)).clear();
    }

    @Test
    public void testContainsKey() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.containsKey(any())).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.containsKey("key");

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).containsKey("key");
    }

    @Test
    public void testContainsMapping() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.containsMapping(any(), any())).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.containsMapping("key", "value");

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).containsMapping("key", "value");
    }

    @Test
    public void testContainsValue() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.containsValue(any())).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.containsValue("value");

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).containsValue("value");
    }

    @Test
    public void testEntries() {
        // Given
        Collection<Map.Entry<String, String>> expectedEntries = mock(Collection.class);
        when(multiValuedMap.entries()).thenReturn(expectedEntries);

        // When
        Collection<Map.Entry<String, String>> actualEntries = decorator.entries();

        // Then
        assertEquals(expectedEntries, actualEntries);
        verify(multiValuedMap, times(1)).entries();
    }

    @Test
    public void testEquals() {
        // Given
        Object object = mock(Object.class);
        boolean expectedResult = true;
        when(multiValuedMap.equals(object)).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.equals(object);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).equals(object);
    }

    @Test
    public void testGet() {
        // Given
        Collection<String> expectedValues = mock(Collection.class);
        when(multiValuedMap.get(any())).thenReturn(expectedValues);

        // When
        Collection<String> actualValues = decorator.get("key");

        // Then
        assertEquals(expectedValues, actualValues);
        verify(multiValuedMap, times(1)).get("key");
    }

    @Test
    public void testHashCode() {
        // Given
        int expectedResult = 1;
        when(multiValuedMap.hashCode()).thenReturn(expectedResult);

        // When
        int actualResult = decorator.hashCode();

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).hashCode();
    }

    @Test
    public void testIsEmpty() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.isEmpty()).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.isEmpty();

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).isEmpty();
    }

    @Test
    public void testKeys() {
        // Given
        org.apache.commons.collections4.MultiSet<String> expectedKeys = mock(org.apache.commons.collections4.MultiSet.class);
        when(multiValuedMap.keys()).thenReturn(expectedKeys);

        // When
        org.apache.commons.collections4.MultiSet<String> actualKeys = decorator.keys();

        // Then
        assertEquals(expectedKeys, actualKeys);
        verify(multiValuedMap, times(1)).keys();
    }

    @Test
    public void testKeySet() {
        // Given
        Set<String> expectedKeySet = mock(Set.class);
        when(multiValuedMap.keySet()).thenReturn(expectedKeySet);

        // When
        Set<String> actualKeySet = decorator.keySet();

        // Then
        assertEquals(expectedKeySet, actualKeySet);
        verify(multiValuedMap, times(1)).keySet();
    }

    @Test
    public void testMapIterator() {
        // Given
        org.apache.commons.collections4.MapIterator<String, String> expectedMapIterator = mock(org.apache.commons.collections4.MapIterator.class);
        when(multiValuedMap.mapIterator()).thenReturn(expectedMapIterator);

        // When
        org.apache.commons.collections4.MapIterator<String, String> actualMapIterator = decorator.mapIterator();

        // Then
        assertEquals(expectedMapIterator, actualMapIterator);
        verify(multiValuedMap, times(1)).mapIterator();
    }

    @Test
    public void testPut() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.put(any(), any())).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.put("key", "value");

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).put("key", "value");
    }

    @Test
    public void testPutAll_K_Iterable() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.putAll(any(), any())).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.putAll("key", java.util.Collections.singleton("value"));

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).putAll("key", java.util.Collections.singleton("value"));
    }

    @Test
    public void testPutAll_Map() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.putAll(any())).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.putAll(java.util.Collections.singletonMap("key", "value"));

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).putAll(java.util.Collections.singletonMap("key", "value"));
    }

    @Test
    public void testPutAll_MultiValuedMap() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.putAll(any())).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.putAll(multiValuedMap);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).putAll(multiValuedMap);
    }

    @Test
    public void testRemove() {
        // Given
        Collection<String> expectedValues = mock(Collection.class);
        when(multiValuedMap.remove(any())).thenReturn(expectedValues);

        // When
        Collection<String> actualValues = decorator.remove("key");

        // Then
        assertEquals(expectedValues, actualValues);
        verify(multiValuedMap, times(1)).remove("key");
    }

    @Test
    public void testRemoveMapping() {
        // Given
        boolean expectedResult = true;
        when(multiValuedMap.removeMapping(any(), any())).thenReturn(expectedResult);

        // When
        boolean actualResult = decorator.removeMapping("key", "value");

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).removeMapping("key", "value");
    }

    @Test
    public void testSize() {
        // Given
        int expectedResult = 1;
        when(multiValuedMap.size()).thenReturn(expectedResult);

        // When
        int actualResult = decorator.size();

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).size();
    }

    @Test
    public void testToString() {
        // Given
        String expectedResult = "toString";
        when(multiValuedMap.toString()).thenReturn(expectedResult);

        // When
        String actualResult = decorator.toString();

        // Then
        assertEquals(expectedResult, actualResult);
        verify(multiValuedMap, times(1)).toString();
    }

    @Test
    public void testValues() {
        // Given
        Collection<String> expectedValues = mock(Collection.class);
        when(multiValuedMap.values()).thenReturn(expectedValues);

        // When
        Collection<String> actualValues = decorator.values();

        // Then
        assertEquals(expectedValues, actualValues);
        verify(multiValuedMap, times(1)).values();
    }
}