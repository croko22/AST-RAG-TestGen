import org.jsoup.helper.AuthenticationHandler;
import org.jsoup.helper.RequestAuthenticator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.PasswordAuthentication;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationHandlerTest {

    @Mock
    private RequestAuthenticator requestAuthenticator;

    private AuthenticationHandler authenticationHandler;

    @BeforeEach
    void setup() {
        authenticationHandler = new AuthenticationHandler();
    }

    @AfterEach
    void tearDown() {
        authenticationHandler.remove();
    }

    @Test
    public void testGetPasswordAuthentication_NoAuthenticator() {
        // Given: no authenticator is set
        // When: getPasswordAuthentication is called
        PasswordAuthentication passwordAuthentication = authenticationHandler.getPasswordAuthentication();
        // Then: null is returned
        assertNull(passwordAuthentication);
    }

    @Test
    public void testGetPasswordAuthentication_WithAuthenticator() {
        // Given: an authenticator is set
        when(requestAuthenticator.authenticate(any())).thenReturn(new PasswordAuthentication("username", "password".toCharArray()));
        authenticationHandler.enable(requestAuthenticator, new Object());
        // When: getPasswordAuthentication is called
        PasswordAuthentication passwordAuthentication = authenticationHandler.getPasswordAuthentication();
        // Then: the authenticator's credentials are returned
        assertNotNull(passwordAuthentication);
        assertEquals("username", passwordAuthentication.getUserName());
        assertEquals("password", new String(passwordAuthentication.getPassword()));
    }

    @Test
    public void testGetPasswordAuthentication_MaxAttemptsExceeded() {
        // Given: an authenticator is set and max attempts are exceeded
        when(requestAuthenticator.authenticate(any())).thenReturn(null);
        authenticationHandler.enable(requestAuthenticator, new Object());
        for (int i = 0; i < AuthenticationHandler.MaxAttempts + 1; i++) {
            authenticationHandler.getPasswordAuthentication();
        }
        // When: getPasswordAuthentication is called again
        PasswordAuthentication passwordAuthentication = authenticationHandler.getPasswordAuthentication();
        // Then: null is returned
        assertNull(passwordAuthentication);
    }

    @Test
    public void testEnable() {
        // Given: an authenticator is set
        authenticationHandler.enable(requestAuthenticator, new Object());
        // When: getPasswordAuthentication is called
        PasswordAuthentication passwordAuthentication = authenticationHandler.getPasswordAuthentication();
        // Then: the authenticator's credentials are returned
        assertNotNull(passwordAuthentication);
    }

    @Test
    public void testRemove() {
        // Given: an authenticator is set
        authenticationHandler.enable(requestAuthenticator, new Object());
        // When: remove is called
        authenticationHandler.remove();
        // Then: getPasswordAuthentication returns null
        assertNull(authenticationHandler.getPasswordAuthentication());
    }

    @Test
    public void testGet() {
        // Given: an authenticator is set
        authenticationHandler.enable(requestAuthenticator, new Object());
        // When: get is called
        AuthenticationHandler handler = authenticationHandler.get(authenticationHandler);
        // Then: the handler is returned
        assertNotNull(handler);
    }
}