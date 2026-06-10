import net.hydromatic.morel.type.ProgressiveRecordType;
import net.hydromatic.morel.type.RecordType;
import net.hydromatic.morel.type.Type;
import net.hydromatic.morel.type.Keys;
import net.hydromatic.morel.type.Key;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgressiveRecordTypeTest {

    @Mock
    private SortedMap<String, Type> argNameTypes;

    private ProgressiveRecordType progressiveRecordType;

    @BeforeEach
    void setup() {
        progressiveRecordType = new ProgressiveRecordType(argNameTypes);
    }

    @Test
    public void testIsProgressive() {
        // When: se ejecuta el metodo isProgressive
        boolean result = progressiveRecordType.isProgressive();

        // Then: se verifica el resultado
        assertTrue(result);
    }

    @Test
    public void testKey() {
        // Given: se configura el comportamiento del mock
        when(argNameTypes.isEmpty()).thenReturn(false);

        // When: se ejecuta el metodo key
        Key result = progressiveRecordType.key();

        // Then: se verifica el resultado
        assertNotNull(result);
        verifyStatic(Keys.class);
        Keys.progressiveRecord(Keys.toKeys(argNameTypes));
    }

    @Test
    public void testKey_EmptyArgNameTypes() {
        // Given: se configura el comportamiento del mock
        when(argNameTypes.isEmpty()).thenReturn(true);

        // When: se ejecuta el metodo key
        Key result = progressiveRecordType.key();

        // Then: se verifica el resultado
        assertNotNull(result);
        verifyStatic(Keys.class);
        Keys.progressiveRecord(Keys.toKeys(argNameTypes));
    }

    @Test
    public void testConstructor() {
        // Given: se crea un mapa de nombre de tipo
        SortedMap<String, Type> nameTypes = new TreeMap<>();
        nameTypes.put("field1", mock(Type.class));
        nameTypes.put("field2", mock(Type.class));

        // When: se crea una instancia de ProgressiveRecordType
        ProgressiveRecordType instance = new ProgressiveRecordType(nameTypes);

        // Then: se verifica la instancia
        assertNotNull(instance);
        assertSame(nameTypes, instance.argNameTypes);
    }
}