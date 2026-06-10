import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionIDTest {

    @Mock
    private StunStack stunStack;

    @Test
    public void testCreateNewTransactionID() {
        // When: create a new transaction ID
        TransactionID transactionID = TransactionID.createNewTransactionID();

        // Then: verify the transaction ID
        assertNotNull(transactionID);
        assertEquals(12, transactionID.getBytes().length);
        assertFalse(transactionID.isRFC3489Compatible());
    }

    @Test
    public void testCreateNewRFC3489TransactionID() {
        // When: create a new RFC3489 transaction ID
        TransactionID transactionID = TransactionID.createNewRFC3489TransactionID();

        // Then: verify the transaction ID
        assertNotNull(transactionID);
        assertEquals(16, transactionID.getBytes().length);
        assertTrue(transactionID.isRFC3489Compatible());
    }

    @Test
    public void testCreateTransactionID() {
        // Given: a transaction ID byte array
        byte[] transactionIDBytes = new byte[12];

        // When: create a transaction ID
        TransactionID transactionID = TransactionID.createTransactionID(stunStack, transactionIDBytes);

        // Then: verify the transaction ID
        assertNotNull(transactionID);
        assertEquals(12, transactionID.getBytes().length);
        assertFalse(transactionID.isRFC3489Compatible());
    }

    @Test
    public void testIsRFC3489Compatible() {
        // Given: a transaction ID
        TransactionID transactionID = TransactionID.createNewTransactionID();

        // When: check if the transaction ID is RFC3489 compatible
        boolean isCompatible = transactionID.isRFC3489Compatible();

        // Then: verify the result
        assertFalse(isCompatible);
    }

    @Test
    public void testEquals() {
        // Given: two transaction IDs
        TransactionID transactionID1 = TransactionID.createNewTransactionID();
        TransactionID transactionID2 = TransactionID.createNewTransactionID();

        // When: check if the transaction IDs are equal
        boolean areEqual = transactionID1.equals(transactionID2);

        // Then: verify the result
        assertFalse(areEqual);
    }

    @Test
    public void testEquals_SameInstance() {
        // Given: a transaction ID
        TransactionID transactionID = TransactionID.createNewTransactionID();

        // When: check if the transaction ID is equal to itself
        boolean areEqual = transactionID.equals(transactionID);

        // Then: verify the result
        assertTrue(areEqual);
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given: a transaction ID and an object of a different class
        TransactionID transactionID = TransactionID.createNewTransactionID();
        Object differentObject = new Object();

        // When: check if the transaction ID is equal to the different object
        boolean areEqual = transactionID.equals(differentObject);

        // Then: verify the result
        assertFalse(areEqual);
    }

    @Test
    public void testHashCode() {
        // Given: a transaction ID
        TransactionID transactionID = TransactionID.createNewTransactionID();

        // When: get the hash code of the transaction ID
        int hashCode = transactionID.hashCode();

        // Then: verify the hash code
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        // Given: a transaction ID
        TransactionID transactionID = TransactionID.createNewTransactionID();

        // When: get the string representation of the transaction ID
        String toString = transactionID.toString();

        // Then: verify the string representation
        assertNotNull(toString);
    }

    @Test
    public void testSetApplicationData() {
        // Given: a transaction ID and an application data object
        TransactionID transactionID = TransactionID.createNewTransactionID();
        Object applicationData = new Object();

        // When: set the application data
        transactionID.setApplicationData(applicationData);

        // Then: verify the application data
        assertEquals(applicationData, transactionID.getApplicationData());
    }

    @Test
    public void testGetApplicationData() {
        // Given: a transaction ID
        TransactionID transactionID = TransactionID.createNewTransactionID();

        // When: get the application data
        Object applicationData = transactionID.getApplicationData();

        // Then: verify the application data
        assertNull(applicationData);
    }
}