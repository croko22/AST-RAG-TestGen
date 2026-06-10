import org.apache.commons.collections4.list.AbstractSerializableListDecorator;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractSerializableListDecoratorTest {

    @Mock
    private List<String> list;

    private AbstractSerializableListDecorator<String> decorator;

    @BeforeEach
    void setup() {
        decorator = new AbstractSerializableListDecorator<>(list) {
        };
    }

    @Test
    public void testConstructor() {
        // Given: a list
        List<String> testList = new ArrayList<>();
        testList.add("element1");
        testList.add("element2");

        // When: creating a new decorator
        AbstractSerializableListDecorator<String> decorator = new AbstractSerializableListDecorator<>(testList) {
        };

        // Then: the list is set correctly
        assertEquals(testList, decorator.decorated());
    }

    @Test
    public void testConstructor_NullList() {
        // Given: a null list
        List<String> testList = null;

        // When / Then: an exception is thrown
        assertThrows(NullPointerException.class, () -> new AbstractSerializableListDecorator<>(testList) {
        });
    }

    @Test
    public void testReadObject() throws Exception {
        // Given: a serialized decorator
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList<>());
        oos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);

        // When: deserializing the decorator
        AbstractSerializableListDecorator<String> decorator = new AbstractSerializableListDecorator<>(new ArrayList<>()) {
        };
        decorator.readObject(ois);

        // Then: the list is set correctly
        assertNotNull(decorator.decorated());
    }

    @Test
    public void testWriteObject() throws Exception {
        // Given: a decorator
        AbstractSerializableListDecorator<String> decorator = new AbstractSerializableListDecorator<>(new ArrayList<>()) {
        };

        // When: serializing the decorator
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        decorator.writeObject(oos);
        oos.close();

        // Then: the list is serialized correctly
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        ois.defaultReadObject();
        List<String> serializedList = (List<String>) ois.readObject();
        assertNotNull(serializedList);
    }
}