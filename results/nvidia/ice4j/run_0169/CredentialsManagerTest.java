import org.ice4j.security.CredentialsAuthority;
import org.ice4j.security.CredentialsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CredentialsManagerTest {

    @Mock
    private CredentialsAuthority credentialsAuthority;

    private CredentialsManager credentialsManager;

    @BeforeEach
    public void setup() {
        credentialsManager = new CredentialsManager();
    }

    @Test
    public void testCheckLocalUserName_UnknownUser() {
        // Given: no authorities registered
        // When: check local user name
        boolean result = credentialsManager.checkLocalUserName("unknownUser");
        // Then: should return false
        assertFalse(result);
    }

    @Test
    public void testCheckLocalUserName_KnownUser() {
        // Given: authority registered that knows the user
        when(credentialsAuthority.checkLocalUserName(anyString())).thenReturn(true);
        credentialsManager.registerAuthority(credentialsAuthority);
        // When: check local user name
        boolean result = credentialsManager.checkLocalUserName("knownUser");
        // Then: should return true
        assertTrue(result);
    }

    @Test
    public void testCheckLocalUserName_MultipleAuthorities() {
        // Given: multiple authorities registered, one of which knows the user
        CredentialsAuthority authority1 = mock(CredentialsAuthority.class);
        when(authority1.checkLocalUserName(anyString())).thenReturn(false);
        CredentialsAuthority authority2 = mock(CredentialsAuthority.class);
        when(authority2.checkLocalUserName(anyString())).thenReturn(true);
        credentialsManager.registerAuthority(authority1);
        credentialsManager.registerAuthority(authority2);
        // When: check local user name
        boolean result = credentialsManager.checkLocalUserName("knownUser");
        // Then: should return true
        assertTrue(result);
    }

    @Test
    public void testRegisterAuthority() {
        // Given: no authorities registered
        // When: register authority
        credentialsManager.registerAuthority(credentialsAuthority);
        // Then: should be able to check local user name
        when(credentialsAuthority.checkLocalUserName(anyString())).thenReturn(true);
        assertTrue(credentialsManager.checkLocalUserName("knownUser"));
    }

    @Test
    public void testUnregisterAuthority() {
        // Given: authority registered
        credentialsManager.registerAuthority(credentialsAuthority);
        // When: unregister authority
        credentialsManager.unregisterAuthority(credentialsAuthority);
        // Then: should not be able to check local user name
        when(credentialsAuthority.checkLocalUserName(anyString())).thenReturn(true);
        assertFalse(credentialsManager.checkLocalUserName("knownUser"));
    }

    @Test
    public void testRegisterAndUnregisterMultipleAuthorities() {
        // Given: multiple authorities registered
        CredentialsAuthority authority1 = mock(CredentialsAuthority.class);
        CredentialsAuthority authority2 = mock(CredentialsAuthority.class);
        credentialsManager.registerAuthority(authority1);
        credentialsManager.registerAuthority(authority2);
        // When: unregister one authority
        credentialsManager.unregisterAuthority(authority1);
        // Then: should still be able to check local user name with remaining authority
        when(authority2.checkLocalUserName(anyString())).thenReturn(true);
        assertTrue(credentialsManager.checkLocalUserName("knownUser"));
    }
}