import org.jsoup.Connection;
import org.jsoup.helper.RequestAuthenticator;
import org.jsoup.helper.RequestAuthenticator.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestAuthenticatorTest {

    @Mock
    private RequestAuthenticator requestAuthenticator;

    @Mock
    private Connection connection;

    private Context context;

    @BeforeEach
    void setup() throws Exception {
        URL url = new URL("http://example.com");
        context = new Context(url, Authenticator.RequestorType.SERVER, "realm");
    }

    @Test
    public void testAuthenticate_Server() {
        // Given
        when(requestAuthenticator.authenticate(any(Context.class))).thenReturn(new PasswordAuthentication("username", "password".toCharArray()));

        // When
        PasswordAuthentication passwordAuthentication = requestAuthenticator.authenticate(context);

        // Then
        assertNotNull(passwordAuthentication);
        assertEquals("username", passwordAuthentication.getUserName());
        assertEquals("password", new String(passwordAuthentication.getPassword()));
    }

    @Test
    public void testAuthenticate_Proxy() {
        // Given
        Context proxyContext = new Context(new URL("http://proxy.example.com"), Authenticator.RequestorType.PROXY, "proxyRealm");
        when(requestAuthenticator.authenticate(any(Context.class))).thenReturn(new PasswordAuthentication("proxyUsername", "proxyPassword".toCharArray()));

        // When
        PasswordAuthentication passwordAuthentication = requestAuthenticator.authenticate(proxyContext);

        // Then
        assertNotNull(passwordAuthentication);
        assertEquals("proxyUsername", passwordAuthentication.getUserName());
        assertEquals("proxyPassword", new String(passwordAuthentication.getPassword()));
    }

    @Test
    public void testAuthenticate_Null() {
        // Given
        when(requestAuthenticator.authenticate(any(Context.class))).thenReturn(null);

        // When
        PasswordAuthentication passwordAuthentication = requestAuthenticator.authenticate(context);

        // Then
        assertNull(passwordAuthentication);
    }

    @Test
    public void testUrl() {
        // Given
        URL url = new URL("http://example.com");

        // When
        URL resultUrl = context.url();

        // Then
        assertEquals(url, resultUrl);
    }

    @Test
    public void testRealm() {
        // Given
        String realm = "realm";

        // When
        String resultRealm = context.realm();

        // Then
        assertEquals(realm, resultRealm);
    }

    @Test
    public void testIsProxy() {
        // Given
        Context proxyContext = new Context(new URL("http://proxy.example.com"), Authenticator.RequestorType.PROXY, "proxyRealm");

        // When
        boolean isProxy = proxyContext.isProxy();

        // Then
        assertTrue(isProxy);
    }

    @Test
    public void testIsServer() {
        // Given
        Context serverContext = new Context(new URL("http://example.com"), Authenticator.RequestorType.SERVER, "realm");

        // When
        boolean isServer = serverContext.isServer();

        // Then
        assertTrue(isServer);
    }

    @Test
    public void testCredentials() {
        // Given
        String username = "username";
        String password = "password";

        // When
        PasswordAuthentication passwordAuthentication = context.credentials(username, password);

        // Then
        assertNotNull(passwordAuthentication);
        assertEquals(username, passwordAuthentication.getUserName());
        assertEquals(password, new String(passwordAuthentication.getPassword()));
    }
}