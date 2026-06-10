import org.ice4j.message.Message;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransactionID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseStunMessageEventTest {

    @Mock
    private StunStack stunStack;

    @Mock
    private Message message;

    private BaseStunMessageEvent event;

    @BeforeEach
    public void setup() {
        event = new BaseStunMessageEvent(stunStack, new TransportAddress(), message);
    }

    @Test
    public void testGetMessage() {
        // Given
        when(message.getDataLength()).thenReturn((char) 10);

        // When
        Message result = event.getMessage();

        // Then
        assertEquals(message, result);
    }

    @Test
    public void testGetStunStack() {
        // Given
        when(stunStack.getCredentialsManager()).thenReturn(new CredentialsManager());

        // When
        StunStack result = event.getStunStack();

        // Then
        assertEquals(stunStack, result);
    }

    @Test
    public void testGetTransactionID() {
        // Given
        when(TransactionID.createTransactionID(stunStack, message.getTransactionID())).thenReturn(TransactionID.createNewTransactionID());

        // When
        TransactionID result = event.getTransactionID();

        // Then
        assertNotNull(result);
    }

    @Test
    public void testGetTransactionID_MultipleCalls() {
        // Given
        when(TransactionID.createTransactionID(stunStack, message.getTransactionID())).thenReturn(TransactionID.createNewTransactionID());

        // When
        TransactionID firstCall = event.getTransactionID();
        TransactionID secondCall = event.getTransactionID();

        // Then
        assertEquals(firstCall, secondCall);
    }

    @Test
    public void testSetTransactionID() {
        // Given
        TransactionID transactionID = TransactionID.createNewTransactionID();

        // When
        event.setTransactionID(transactionID);

        // Then
        assertEquals(transactionID, event.getTransactionID());
    }

    @Test
    public void testGetTransactionID_Null() {
        // Given
        when(TransactionID.createTransactionID(stunStack, message.getTransactionID())).thenThrow(new NullPointerException());

        // When
        assertThrows(NullPointerException.class, () -> event.getTransactionID());
    }
}