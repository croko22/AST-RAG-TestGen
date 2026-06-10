import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransformedCollectionTest {

    @Mock
    private Transformer transformer;

    private Collection<String> collection;

    @BeforeEach
    void setup() {
        collection = new ArrayList<>();
    }

    @Test
    public void testTransformedCollection() {
        // Given
        when(transformer.apply(any())).thenReturn("transformed");

        // When
        TransformedCollection<String> transformedCollection = TransformedCollection.transformedCollection(collection, transformer);

        // Then
        assertNotNull(transformedCollection);
    }

    @Test
    public void testTransformingCollection() {
        // Given
        when(transformer.apply(any())).thenReturn("transformed");

        // When
        TransformedCollection<String> transformingCollection = TransformedCollection.transformingCollection(collection, transformer);

        // Then
        assertNotNull(transformingCollection);
    }

    @Test
    public void testAdd() {
        // Given
        when(transformer.apply(any())).thenReturn("transformed");
        TransformedCollection<String> transformedCollection = TransformedCollection.transformingCollection(collection, transformer);

        // When
        boolean result = transformedCollection.add("object");

        // Then
        assertTrue(result);
        verify(transformer, times(1)).apply("object");
    }

    @Test
    public void testAddAll() {
        // Given
        when(transformer.apply(any())).thenReturn("transformed");
        TransformedCollection<String> transformedCollection = TransformedCollection.transformingCollection(collection, transformer);
        Collection<String> coll = new ArrayList<>();
        coll.add("object1");
        coll.add("object2");

        // When
        boolean result = transformedCollection.addAll(coll);

        // Then
        assertTrue(result);
        verify(transformer, times(1)).apply("object1");
        verify(transformer, times(1)).apply("object2");
    }

    @Test
    public void testTransformedCollectionNullCollection() {
        // Given
        collection = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedCollection.transformedCollection(collection, transformer));
    }

    @Test
    public void testTransformingCollectionNullCollection() {
        // Given
        collection = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedCollection.transformingCollection(collection, transformer));
    }

    @Test
    public void testTransformedCollectionNullTransformer() {
        // Given
        transformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedCollection.transformedCollection(collection, transformer));
    }

    @Test
    public void testTransformingCollectionNullTransformer() {
        // Given
        transformer = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> TransformedCollection.transformingCollection(collection, transformer));
    }
}