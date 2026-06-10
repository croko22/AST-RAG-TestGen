import org.apache.commons.collections4.set.AbstractSerializableSetDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbstractSerializableSetDecoratorTest {

    @Mock
    private Set<String> set;

    private AbstractSerializableSetDecorator<String> decorator;

    @BeforeEach
    public void setup() {
        decorator = new AbstractSerializableSetDecorator<>(set) {
        };
    }

    @Test
    public void testConstructor() {
        // Given
        Set<String> testSet = new HashSet<>();

        // When
        AbstractSerializableSetDecorator<String> decorator = new AbstractSerializableSetDecorator<>(testSet) {
        };

        // Then
        assertNotNull(decorator);
    }

    @Test
    public void testConstructor_NullSet() {
        // Given
        Set<String> testSet = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> new AbstractSerializableSetDecorator<>(testSet) {
        });
    }

    @Test
    public void testWriteObject() throws Exception {
        // Given
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        // When
        decorator.writeObject(out);

        // Then
        out.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        in.readObject();
        in.close();
    }

    @Test
    public void testReadObject() throws Exception {
        // Given
        Set<String> testSet = new HashSet<>();
        testSet.add("test");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.defaultWriteObject();
        out.writeObject(testSet);
        out.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);

        // When
        AbstractSerializableSetDecorator<String> decorator = new AbstractSerializableSetDecorator<>(new HashSet<>()) {
        };
        decorator.readObject(in);

        // Then
        in.close();
        verify(set, times(1)).addAll(any(Collection.class));
    }

    @Test
    public void testReadObject_IOException() throws Exception {
        // Given
        Set<String> testSet = new HashSet<>();
        testSet.add("test");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.defaultWriteObject();
        out.writeObject(testSet);
        out.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);

        // When and Then
        in.close();
        assertThrows(IOException.class, () -> {
            AbstractSerializableSetDecorator<String> decorator = new AbstractSerializableSetDecorator<>(new HashSet<>()) {
            };
            decorator.readObject(null);
        });
    }

    @Test
    public void testReadObject_ClassNotFoundException() throws Exception {
        // Given
        Set<String> testSet = new HashSet<>();
        testSet.add("test");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.defaultWriteObject();
        out.writeObject(testSet);
        out.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);

        // When and Then
        in.close();
        assertThrows(ClassNotFoundException.class, () -> {
            AbstractSerializableSetDecorator<String> decorator = new AbstractSerializableSetDecorator<>(new HashSet<>()) {
            };
            decorator.readObject(mock(ObjectInputStream.class));
        });
    }
}