import org.jsoup.helper.RequestDispatch;
import org.jsoup.internal.SharedConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.net.Proxy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestDispatchTest {

    @Mock
    private RequestDispatch.RequestExecutor requestExecutor;

    @Mock
    private RequestDispatch.Request request;

    @Mock
    private RequestDispatch.Response response;

    @BeforeEach
    void setup() {
        // Reset the clientConstructor to null before each test
        RequestDispatch.clientConstructor = null;
    }

    @Test
    public void testGet_RequestExecutor_WithHttpClient() throws Exception {
        // Given: HttpClient is available and system property is set to true
        System.setProperty(SharedConstants.UseHttpClient, "true");
        RequestDispatch.clientConstructor = mock(Constructor.class);

        // When: get method is called
        RequestDispatch.RequestExecutor executor = RequestDispatch.get(request, response);

        // Then: HttpClientExecutor is used
        assertTrue(executor instanceof RequestDispatch.UrlConnectionExecutor);
    }

    @Test
    public void testGet_RequestExecutor_WithoutHttpClient() throws Exception {
        // Given: HttpClient is not available
        RequestDispatch.clientConstructor = null;

        // When: get method is called
        RequestDispatch.RequestExecutor executor = RequestDispatch.get(request, response);

        // Then: UrlConnectionExecutor is used
        assertTrue(executor instanceof RequestDispatch.UrlConnectionExecutor);
    }

    @Test
    public void testGet_RequestExecutor_WithSslSocketFactory() throws Exception {
        // Given: SSL socket factory is set
        when(request.sslSocketFactory()).thenReturn(mock(javax.net.ssl.SSLSocketFactory.class));

        // When: get method is called
        RequestDispatch.RequestExecutor executor = RequestDispatch.get(request, response);

        // Then: UrlConnectionExecutor is used
        assertTrue(executor instanceof RequestDispatch.UrlConnectionExecutor);
    }

    @Test
    public void testGet_RequestExecutor_WithSocksProxy() throws Exception {
        // Given: SOCKS proxy is set
        Proxy proxy = mock(Proxy.class);
        when(proxy.type()).thenReturn(Proxy.Type.SOCKS);
        when(request.proxy()).thenReturn(proxy);

        // When: get method is called
        RequestDispatch.RequestExecutor executor = RequestDispatch.get(request, response);

        // Then: UrlConnectionExecutor is used
        assertTrue(executor instanceof RequestDispatch.UrlConnectionExecutor);
    }

    @Test
    public void testGet_RequestExecutor_WithException() throws Exception {
        // Given: Exception occurs while creating HttpClientExecutor
        RequestDispatch.clientConstructor = mock(Constructor.class);
        when(RequestDispatch.clientConstructor.newInstance(any(), any())).thenThrow(mock(Exception.class));

        // When: get method is called
        RequestDispatch.RequestExecutor executor = RequestDispatch.get(request, response);

        // Then: UrlConnectionExecutor is used
        assertTrue(executor instanceof RequestDispatch.UrlConnectionExecutor);
    }
}