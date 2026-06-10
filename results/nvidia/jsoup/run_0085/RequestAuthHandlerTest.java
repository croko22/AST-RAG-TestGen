package org.jsoup.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.HttpURLConnection;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestAuthHandlerTest {

    @Mock
    private RequestAuthenticator auth;

    @Mock
    private HttpURLConnection httpURLConnection;

    @Mock
    private HttpClient.Builder httpClientBuilder;

    private RequestAuthHandler requestAuthHandler;

    @BeforeEach
    public void setup() {
        requestAuthHandler = new RequestAuthHandler();
    }

    @Test
    public void testEnable_WithHttpURLConnection() {
        // Given
        when(httpURLConnection.getClass()).thenReturn(HttpURLConnection.class);

        // When
        requestAuthHandler.enable(auth, httpURLConnection);

        // Then
        verify(httpURLConnection).setAuthenticator(any(AuthenticationHandler.class));
    }

    @Test
    public void testEnable_WithHttpClientBuilder() {
        // Given
        when(httpClientBuilder.getClass()).thenReturn(HttpClient.Builder.class);

        // When
        requestAuthHandler.enable(auth, httpClientBuilder);

        // Then
        verify(httpClientBuilder).authenticator(any(AuthenticationHandler.class));
    }

    @Test
    public void testEnable_WithUnsupportedExecutor() {
        // Given
        Object unsupportedExecutor = new Object();

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            requestAuthHandler.enable(auth, unsupportedExecutor);
        });
    }

    @Test
    public void testRemove() {
        // When
        requestAuthHandler.remove();

        // Then
        // No-op, no assertions needed
    }

    @Test
    public void testGet() {
        // Given
        AuthenticationHandler helper = mock(AuthenticationHandler.class);

        // When
        AuthenticationHandler result = requestAuthHandler.get(helper);

        // Then
        assertEquals(helper, result);
    }
}