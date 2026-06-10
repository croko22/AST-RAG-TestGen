import org.ice4j.stunclient.StunDiscoveryReport;
import org.ice4j.TransportAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StunDiscoveryReportTest {

    @Mock
    private TransportAddress transportAddress;

    private StunDiscoveryReport report;

    @BeforeEach
    public void setup() {
        report = new StunDiscoveryReport();
    }

    @Test
    public void testGetNatType_Unknown() {
        // Given: default report
        // When: get nat type
        String natType = report.getNatType();
        // Then: assert unknown
        assertEquals(StunDiscoveryReport.UNKNOWN, natType);
    }

    @Test
    public void testGetNatType_SetNatType() {
        // Given: set nat type
        report.setNatType(StunDiscoveryReport.OPEN_INTERNET);
        // When: get nat type
        String natType = report.getNatType();
        // Then: assert open internet
        assertEquals(StunDiscoveryReport.OPEN_INTERNET, natType);
    }

    @Test
    public void testGetPublicAddress_Null() {
        // Given: default report
        // When: get public address
        TransportAddress publicAddress = report.getPublicAddress();
        // Then: assert null
        assertNull(publicAddress);
    }

    @Test
    public void testGetPublicAddress_SetPublicAddress() {
        // Given: set public address
        report.setPublicAddress(transportAddress);
        // When: get public address
        TransportAddress publicAddress = report.getPublicAddress();
        // Then: assert transport address
        assertEquals(transportAddress, publicAddress);
    }

    @Test
    public void testEquals_SameObject() {
        // Given: same object
        // When: equals
        boolean equals = report.equals(report);
        // Then: assert true
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentObject_SameValues() {
        // Given: different object with same values
        StunDiscoveryReport otherReport = new StunDiscoveryReport();
        otherReport.setNatType(StunDiscoveryReport.UNKNOWN);
        otherReport.setPublicAddress(null);
        // When: equals
        boolean equals = report.equals(otherReport);
        // Then: assert true
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentObject_DifferentValues() {
        // Given: different object with different values
        StunDiscoveryReport otherReport = new StunDiscoveryReport();
        otherReport.setNatType(StunDiscoveryReport.OPEN_INTERNET);
        otherReport.setPublicAddress(transportAddress);
        // When: equals
        boolean equals = report.equals(otherReport);
        // Then: assert false
        assertFalse(equals);
    }

    @Test
    public void testEquals_Null() {
        // Given: null object
        // When: equals
        boolean equals = report.equals(null);
        // Then: assert false
        assertFalse(equals);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: different class object
        // When: equals
        boolean equals = report.equals(new Object());
        // Then: assert false
        assertFalse(equals);
    }

    @Test
    public void testToString() {
        // Given: default report
        // When: to string
        String toString = report.toString();
        // Then: assert contains unknown and null
        assertTrue(toString.contains(StunDiscoveryReport.UNKNOWN));
        assertTrue(toString.contains("null"));
    }

    @Test
    public void testToString_SetNatTypeAndPublicAddress() {
        // Given: set nat type and public address
        report.setNatType(StunDiscoveryReport.OPEN_INTERNET);
        report.setPublicAddress(transportAddress);
        // When: to string
        String toString = report.toString();
        // Then: assert contains open internet and transport address
        assertTrue(toString.contains(StunDiscoveryReport.OPEN_INTERNET));
        assertTrue(toString.contains(transportAddress.toString()));
    }
}