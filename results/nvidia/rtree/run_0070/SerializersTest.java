import com.esotericsoftware.kryo.Kryo;
import com.github.davidmoten.guavamini.Preconditions;
import com.github.davidmoten.rtree.Serializers;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.internal.Functions;
import com.github.davidmoten.rtree.kryo.SerializerKryo;
import com.github.davidmoten.rtree.fbs.SerializerFlatBuffers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SerializersTest {

    @Mock
    private Function<String, byte[]> serializerMock;

    @Mock
    private Function<byte[], String> deserializerMock;

    @Mock
    private Kryo kryoMock;

    private Serializers.SerializerBuilder serializerBuilder;

    @BeforeEach
    void setup() {
        serializerBuilder = Serializers.flatBuffers();
    }

    @Test
    void testSerializer() {
        // Given
        String input = "input";
        byte[] expected = "expected".getBytes();

        when(serializerMock.apply(input)).thenReturn(expected);

        // When
        Serializers.SerializerTypedBuilder<String> serializerTypedBuilder = serializerBuilder.serializer(serializerMock);

        // Then
        assertNotNull(serializerTypedBuilder);
    }

    @Test
    void testDeserializer() {
        // Given
        byte[] input = "input".getBytes();
        String expected = "expected";

        when(deserializerMock.apply(input)).thenReturn(expected);

        // When
        Serializers.SerializerTypedBuilder<String> serializerTypedBuilder = serializerBuilder.deserializer(deserializerMock);

        // Then
        assertNotNull(serializerTypedBuilder);
    }

    @Test
    void testString() {
        // Given
        Charset charset = Charset.forName("UTF-8");

        // When
        Serializers.Serializer<String, Geometry> serializer = serializerBuilder.string(charset);

        // Then
        assertNotNull(serializer);
    }

    @Test
    void testJavaIo() {
        // Given
        Serializable serializable = new Serializable() {
        };

        // When
        Serializers.Serializer<Serializable, Geometry> serializer = serializerBuilder.javaIo();

        // Then
        assertNotNull(serializer);
    }

    @Test
    void testUtf8() {
        // Given

        // When
        Serializers.Serializer<String, Geometry> serializer = serializerBuilder.utf8();

        // Then
        assertNotNull(serializer);
    }

    @Test
    void testBytes() {
        // Given

        // When
        Serializers.Serializer<byte[], Geometry> serializer = serializerBuilder.bytes();

        // Then
        assertNotNull(serializer);
    }

    @Test
    void testMethod() {
        // Given
        Serializers.Method method = Serializers.Method.FLATBUFFERS;

        // When
        Serializers.SerializerBuilder serializerBuilder = this.serializerBuilder.method(method);

        // Then
        assertNotNull(serializerBuilder);
    }

    @Test
    void testCreate() {
        // Given
        Function<String, byte[]> serializer = input -> input.getBytes();
        Function<byte[], String> deserializer = input -> new String(input);

        // When
        Serializers.Serializer<String, Geometry> serializerTyped = serializerBuilder.serializer(serializer).deserializer(deserializer).create();

        // Then
        assertNotNull(serializerTyped);
    }

    @Test
    void testFlatBuffers() {
        // Given

        // When
        Serializers.SerializerBuilder serializerBuilder = Serializers.flatBuffers();

        // Then
        assertNotNull(serializerBuilder);
    }

    @Test
    void testJavaIoSerializer() {
        // Given
        Serializable serializable = new Serializable() {
        };

        // When
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }

        // Then
        assertNotNull(byteArrayOutputStream.toByteArray());
    }

    @Test
    void testJavaIoDeserializer() {
        // Given
        Serializable serializable = new Serializable() {
        };
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }

        // When
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Serializable deserialized = (Serializable) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            fail("IOException or ClassNotFoundException occurred");
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }

        // Then
        // no assertion, just testing that no exception is thrown
    }
}