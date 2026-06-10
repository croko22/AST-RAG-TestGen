import org.ice4j.attribute.AddressAttribute;
import org.ice4j.attribute.ChangedAddressAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ChangedAddressAttributeTest {

    @InjectMocks
    private ChangedAddressAttribute changedAddressAttribute;

    @Test
    public void testChangedAddressAttributeConstructor() {
        // Given: default constructor
        ChangedAddressAttribute attribute = new ChangedAddressAttribute();

        // Then: verify attribute name
        assertEquals(ChangedAddressAttribute.NAME, attribute.getName());
    }

    @Test
    public void testGetName() {
        // Given: default constructor
        ChangedAddressAttribute attribute = new ChangedAddressAttribute();

        // When: get attribute name
        String attributeName = attribute.getName();

        // Then: verify attribute name
        assertEquals(ChangedAddressAttribute.NAME, attributeName);
    }
}