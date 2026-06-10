import org.apache.commons.collections4.bag.HashBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HashBagTest {

    @Mock
    private Collection<String> collection;

    private HashBag<String> hashBag;

    @BeforeEach
    void setup() {
        hashBag = new HashBag<>();
    }

    @Test
    void testConstructor() {
        // Given: no elements
        // When: create a new HashBag
        HashBag<String> newHashBag = new HashBag<>();
        // Then: the bag is empty
        assertTrue(newHashBag.isEmpty());
    }

    @Test
    void testConstructorWithCollection() {
        // Given: a collection with elements
        List<String> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        elements.add("Element1");
        // When: create a new HashBag with the collection
        HashBag<String> newHashBag = new HashBag<>(elements);
        // Then: the bag contains the elements from the collection
        assertEquals(2, newHashBag.uniqueSet().size());
        assertEquals(3, newHashBag.size());
    }

    @Test
    void testConstructorWithIterable() {
        // Given: an iterable with elements
        List<String> elements = new ArrayList<>();
        elements.add("Element1");
        elements.add("Element2");
        elements.add("Element1");
        // When: create a new HashBag with the iterable
        HashBag<String> newHashBag = new HashBag<>(elements);
        // Then: the bag contains the elements from the iterable
        assertEquals(2, newHashBag.uniqueSet().size());
        assertEquals(3, newHashBag.size());
    }

    @Test
    void testSerialization() throws Exception {
        // Given: a HashBag with elements
        HashBag<String> originalHashBag = new HashBag<>();
        originalHashBag.add("Element1");
        originalHashBag.add("Element2");
        originalHashBag.add("Element1");
        // When: serialize and deserialize the HashBag
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(originalHashBag);
        out.flush();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        HashBag<String> deserializedHashBag = (HashBag<String>) in.readObject();
        // Then: the deserialized HashBag contains the same elements as the original
        assertEquals(2, deserializedHashBag.uniqueSet().size());
        assertEquals(3, deserializedHashBag.size());
    }
}