import org.ice4j.ice.CandidateTcpType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CandidateTcpTypeTest {

    @Test
    public void testParse_Active() {
        // Given: a valid "active" candidate TCP type name
        String candidateTcpTypeName = "active";

        // When: parsing the candidate TCP type name
        CandidateTcpType result = CandidateTcpType.parse(candidateTcpTypeName);

        // Then: the expected CandidateTcpType instance is returned
        assertEquals(CandidateTcpType.ACTIVE, result);
    }

    @Test
    public void testParse_Passive() {
        // Given: a valid "passive" candidate TCP type name
        String candidateTcpTypeName = "passive";

        // When: parsing the candidate TCP type name
        CandidateTcpType result = CandidateTcpType.parse(candidateTcpTypeName);

        // Then: the expected CandidateTcpType instance is returned
        assertEquals(CandidateTcpType.PASSIVE, result);
    }

    @Test
    public void testParse_So() {
        // Given: a valid "so" candidate TCP type name
        String candidateTcpTypeName = "so";

        // When: parsing the candidate TCP type name
        CandidateTcpType result = CandidateTcpType.parse(candidateTcpTypeName);

        // Then: the expected CandidateTcpType instance is returned
        assertEquals(CandidateTcpType.SO, result);
    }

    @Test
    public void testParse_Invalid() {
        // Given: an invalid candidate TCP type name
        String candidateTcpTypeName = "invalid";

        // When / Then: an exception is thrown when parsing the candidate TCP type name
        assertThrows(IllegalArgumentException.class, () -> {
            CandidateTcpType.parse(candidateTcpTypeName);
        });
    }

    @Test
    public void testToString_Active() {
        // Given: an ACTIVE CandidateTcpType instance
        CandidateTcpType candidateTcpType = CandidateTcpType.ACTIVE;

        // When: getting the string representation of the CandidateTcpType instance
        String result = candidateTcpType.toString();

        // Then: the expected string representation is returned
        assertEquals("active", result);
    }

    @Test
    public void testToString_Passive() {
        // Given: a PASSIVE CandidateTcpType instance
        CandidateTcpType candidateTcpType = CandidateTcpType.PASSIVE;

        // When: getting the string representation of the CandidateTcpType instance
        String result = candidateTcpType.toString();

        // Then: the expected string representation is returned
        assertEquals("passive", result);
    }

    @Test
    public void testToString_So() {
        // Given: an SO CandidateTcpType instance
        CandidateTcpType candidateTcpType = CandidateTcpType.SO;

        // When: getting the string representation of the CandidateTcpType instance
        String result = candidateTcpType.toString();

        // Then: the expected string representation is returned
        assertEquals("so", result);
    }
}