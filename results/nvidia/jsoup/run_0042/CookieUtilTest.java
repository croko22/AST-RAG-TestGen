import org.jsoup.Connection;
import org.jsoup.helper.CookieUtil;
import org.jsoup.internal.StringUtil;
import org.jsoup.parser.CharacterReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CookieUtilTest {

    @Mock
    private Connection.Request request;

    @Mock
    private Connection.Response response;

    @Mock
    private CharacterReader characterReader;

    @Mock
    private BiConsumer<String, String> setter;

    @BeforeEach
    void setup() {
        // Initialize mocks
    }

    @Test
    void testApplyCookiesToRequest() throws IOException {
        // Given
        Map<String, List<String>> storedCookies = new HashMap<>();
        storedCookies.put("Cookie", Collections.singletonList("name=value"));
        when(request.cookieManager()).thenReturn(new CookieManager());
        when(request.cookieManager().get(any(URI.class), any())).thenReturn(storedCookies);

        // When
        CookieUtil.applyCookiesToRequest(request, setter);

        // Then
        verify(setter).accept("Cookie", "name=value");
    }

    @Test
    void testApplyCookiesToRequest_DuplicateCookies() throws IOException {
        // Given
        Map<String, List<String>> storedCookies = new HashMap<>();
        storedCookies.put("Cookie", Collections.singletonList("name=value"));
        when(request.cookieManager()).thenReturn(new CookieManager());
        when(request.cookieManager().get(any(URI.class), any())).thenReturn(storedCookies);
        when(request.cookies()).thenReturn(Map.of("name", "value"));

        // When
        CookieUtil.applyCookiesToRequest(request, setter);

        // Then
        verify(setter).accept("Cookie", "name=value");
    }

    @Test
    void testApplyCookiesToRequest_NoCookies() throws IOException {
        // Given
        Map<String, List<String>> storedCookies = new HashMap<>();
        when(request.cookieManager()).thenReturn(new CookieManager());
        when(request.cookieManager().get(any(URI.class), any())).thenReturn(storedCookies);

        // When
        CookieUtil.applyCookiesToRequest(request, setter);

        // Then
        verify(setter, never()).accept(any(), any());
    }

    @Test
    void testRequestCookieSet() {
        // Given
        when(request.cookies()).thenReturn(Map.of("name", "value"));

        // When
        LinkedHashSet<String> cookieSet = CookieUtil.requestCookieSet(request);

        // Then
        assertEquals(1, cookieSet.size());
        assertTrue(cookieSet.contains("name=value"));
    }

    @Test
    void testAsUri() throws IOException {
        // Given
        URL url = new URL("http://example.com");

        // When
        URI uri = CookieUtil.asUri(url);

        // Then
        assertEquals("http://example.com", uri.toString());
    }

    @Test
    void testAsUri_MalformedURLException() throws IOException {
        // Given
        URL url = new URL(" invalid url");

        // When and Then
        assertThrows(MalformedURLException.class, () -> CookieUtil.asUri(url));
    }

    @Test
    void testStoreCookies() throws IOException {
        // Given
        Map<String, List<String>> resHeaders = new HashMap<>();
        resHeaders.put("Set-Cookie", Collections.singletonList("name=value"));
        when(request.cookieManager()).thenReturn(new CookieManager());
        URL url = new URL("http://example.com");

        // When
        CookieUtil.storeCookies(request, response, url, resHeaders);

        // Then
        verify(request.cookieManager()).put(any(URI.class), eq(resHeaders));
    }

    @Test
    void testParseCookie() {
        // Given
        when(characterReader.consumeTo(any())).thenReturn("name");
        when(characterReader.consumeTo(any())).thenReturn("value");

        // When
        CookieUtil.parseCookie("name=value", response);

        // Then
        verify(response).cookie("name", "value");
    }

    @Test
    void testParseCookie_NullValue() {
        // Given
        when(characterReader.consumeTo(any())).thenReturn(null);

        // When
        CookieUtil.parseCookie("name=value", response);

        // Then
        verify(response, never()).cookie(any(), any());
    }

    @Test
    void testParseCookie_EmptyName() {
        // Given
        when(characterReader.consumeTo(any())).thenReturn("");

        // When
        CookieUtil.parseCookie("name=value", response);

        // Then
        verify(response, never()).cookie(any(), any());
    }
}