import org.ice4j.security.LongTermCredentialSession;
import org.ice4j.security.LongTermCredential;
import org.ice4j.message.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LongTermCredentialSessionTest {

    @Mock
    private LongTermCredential longTermCredential;

    @Mock
    private Request request;

    private LongTermCredentialSession longTermCredentialSession;

    @BeforeEach
    public void setup() {
        byte[] realm = "testRealm".getBytes();
        longTermCredentialSession = new LongTermCredentialSession(longTermCredential, realm);
    }

    @Test
    public void testAddAttributes() throws Exception {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());
        when(longTermCredential.getPassword()).thenReturn("testPassword".getBytes());

        // When
        longTermCredentialSession.addAttributes(request);

        // Then
        verify(request, times(1)).addAttribute(any());
    }

    @Test
    public void testCheckLocalUserName() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());

        // When
        boolean result = longTermCredentialSession.checkLocalUserName("testUsername");

        // Then
        assertTrue(result);
    }

    @Test
    public void testCheckLocalUserName_False() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());

        // When
        boolean result = longTermCredentialSession.checkLocalUserName("wrongUsername");

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetLocalKey() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());
        when(longTermCredential.getPassword()).thenReturn("testPassword".getBytes());

        // When
        byte[] result = longTermCredentialSession.getLocalKey("testUsername");

        // Then
        assertNotNull(result);
        assertNotEquals(0, result.length);
    }

    @Test
    public void testGetLocalKey_Null() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());
        when(longTermCredential.getPassword()).thenReturn("testPassword".getBytes());

        // When
        byte[] result = longTermCredentialSession.getLocalKey("wrongUsername");

        // Then
        assertNull(result);
    }

    @Test
    public void testGetNonce() {
        // Given
        byte[] nonce = "testNonce".getBytes();
        longTermCredentialSession.setNonce(nonce);

        // When
        byte[] result = longTermCredentialSession.getNonce();

        // Then
        assertNotNull(result);
        assertArrayEquals(nonce, result);
    }

    @Test
    public void testGetNonce_Null() {
        // When
        byte[] result = longTermCredentialSession.getNonce();

        // Then
        assertNull(result);
    }

    @Test
    public void testGetPassword() {
        // Given
        when(longTermCredential.getPassword()).thenReturn("testPassword".getBytes());

        // When
        byte[] result = longTermCredentialSession.getPassword();

        // Then
        assertNotNull(result);
        assertArrayEquals("testPassword".getBytes(), result);
    }

    @Test
    public void testGetRealm() {
        // Given
        byte[] realm = "testRealm".getBytes();
        LongTermCredentialSession session = new LongTermCredentialSession(longTermCredential, realm);

        // When
        byte[] result = session.getRealm();

        // Then
        assertNotNull(result);
        assertArrayEquals(realm, result);
    }

    @Test
    public void testGetRemoteKey() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());
        when(longTermCredential.getPassword()).thenReturn("testPassword".getBytes());

        // When
        byte[] result = longTermCredentialSession.getRemoteKey("testUsername", "testMedia");

        // Then
        assertNotNull(result);
        assertNotEquals(0, result.length);
    }

    @Test
    public void testGetRemoteKey_Null() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());
        when(longTermCredential.getPassword()).thenReturn("testPassword".getBytes());

        // When
        byte[] result = longTermCredentialSession.getRemoteKey("wrongUsername", "testMedia");

        // Then
        assertNull(result);
    }

    @Test
    public void testGetUsername() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());

        // When
        byte[] result = longTermCredentialSession.getUsername();

        // Then
        assertNotNull(result);
        assertArrayEquals("testUsername".getBytes(), result);
    }

    @Test
    public void testRealmEquals() {
        // Given
        byte[] realm = "testRealm".getBytes();
        LongTermCredentialSession session = new LongTermCredentialSession(longTermCredential, realm);

        // When
        boolean result = session.realmEquals(realm);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRealmEquals_False() {
        // Given
        byte[] realm = "testRealm".getBytes();
        LongTermCredentialSession session = new LongTermCredentialSession(longTermCredential, realm);

        // When
        boolean result = session.realmEquals("wrongRealm".getBytes());

        // Then
        assertFalse(result);
    }

    @Test
    public void testSetNonce() {
        // Given
        byte[] nonce = "testNonce".getBytes();

        // When
        longTermCredentialSession.setNonce(nonce);

        // Then
        byte[] result = longTermCredentialSession.getNonce();
        assertNotNull(result);
        assertArrayEquals(nonce, result);
    }

    @Test
    public void testUsernameEquals() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());

        // When
        boolean result = longTermCredentialSession.usernameEquals("testUsername".getBytes());

        // Then
        assertTrue(result);
    }

    @Test
    public void testUsernameEquals_False() {
        // Given
        when(longTermCredential.getUsername()).thenReturn("testUsername".getBytes());

        // When
        boolean result = longTermCredentialSession.usernameEquals("wrongUsername".getBytes());

        // Then
        assertFalse(result);
    }
}