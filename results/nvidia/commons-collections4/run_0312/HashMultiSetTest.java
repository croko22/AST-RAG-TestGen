import org.apache.commons.collections4.multiset.HashMultiSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HashMultiSetTest {

    private HashMultiSet<String> hashMultiSet;

    @BeforeEach
    public void setup() {
        hashMultiSet = new HashMultiSet<>();
    }

    @Test
    public void testConstructor() {
        // Given: no elements
        // When: create a new HashMultiSet
        HashMultiSet<String> newHashMultiSet = new HashMultiSet<>();
        // Then: the multiset is empty
        assertTrue(newHashMultiSet.isEmpty());
    }

    @Test
    public void testConstructorWithCollection() {
        // Given: a collection of elements
        List<String> elements = new ArrayList<>();
        elements.add("element1");
        elements.add("element2");
        elements.add("element1");
        // When: create a new HashMultiSet with the collection
        HashMultiSet<String> newHashMultiSet = new HashMultiSet<>(elements);
        // Then: the multiset contains the elements
        assertEquals(2, newHashMultiSet.uniqueSet().size());
        assertEquals(3, newHashMultiSet.size());
    }

    @Test
    public void testSerialization() throws Exception {
        // Given: a HashMultiSet with elements
        hashMultiSet.add("element1");
        hashMultiSet.add("element2");
        hashMultiSet.add("element1");
        // When: serialize and deserialize the multiset
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(hashMultiSet);
        out.flush();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        HashMultiSet<String> deserializedHashMultiSet = (HashMultiSet<String>) in.readObject();
        // Then: the deserialized multiset is equal to the original
        assertEquals(hashMultiSet.uniqueSet().size(), deserializedHashMultiSet.uniqueSet().size());
        assertEquals(hashMultiSet.size(), deserializedHashMultiSet.size());
    }
}