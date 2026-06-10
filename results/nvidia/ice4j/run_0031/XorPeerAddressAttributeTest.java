import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class XorPeerAddressAttributeTest {

    private XorPeerAddressAttribute xorPeerAddressAttribute;

    @BeforeEach
    public void setup() {
        xorPeerAddressAttribute = new XorPeerAddressAttribute();
    }

    @Test
    public void testConstructor() {
        // When: se crea una instancia de XorPeerAddressAttribute
        XorPeerAddressAttribute attribute = new XorPeerAddressAttribute();

        // Then: se verifica que la instancia no sea null
        assertNotNull(attribute);
    }

    @Test
    public void testGetName() {
        // Given: la clase tiene un atributo NAME con el valor "XOR-PEER-ADDRESS"
        String expectedName = "XOR-PEER-ADDRESS";

        // When: se obtiene el nombre del atributo
        String attributeName = XorPeerAddressAttribute.NAME;

        // Then: se verifica que el nombre del atributo sea el esperado
        assertEquals(expectedName, attributeName);
    }

    @Test
    public void testGetClass() {
        // When: se obtiene la clase del atributo
        Class<?> attributeClass = xorPeerAddressAttribute.getClass();

        // Then: se verifica que la clase del atributo sea XorPeerAddressAttribute
        assertEquals(XorPeerAddressAttribute.class, attributeClass);
    }

    @Test
    public void testGetSuperClass() {
        // When: se obtiene la superclase del atributo
        Class<?> superClass = xorPeerAddressAttribute.getClass().getSuperclass();

        // Then: se verifica que la superclase del atributo sea XorMappedAddressAttribute
        assertEquals(XorMappedAddressAttribute.class, superClass);
    }
}