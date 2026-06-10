import org.jsoup.helper.HttpConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestExecutorTest {

    @Mock
    private HttpConnection.Request request;

    @Mock
    private HttpConnection.Response previousResponse;

    private RequestExecutor requestExecutor;

    @BeforeEach
    public void setup() {
        requestExecutor = new RequestExecutor(request, previousResponse) {
            @Override
            public HttpConnection.Response execute() throws IOException {
                return null;
            }

            @Override
            public InputStream responseBody() throws IOException {
                return null;
            }

            @Override
            public void safeClose() {

            }
        };
    }

    @Test
    public void testConstructor_RequestAndPreviousResponse() {
        // Given
        RequestExecutor executor = new RequestExecutor(request, previousResponse);

        // Then
        assertEquals(request, executor.req);
        assertEquals(previousResponse, executor.prevRes);
    }

    @Test
    public void testConstructor_RequestAndNullPreviousResponse() {
        // Given
        RequestExecutor executor = new RequestExecutor(request, null);

        // Then
        assertEquals(request, executor.req);
        assertNull(executor.prevRes);
    }

    @Test
    public void testExecute() throws IOException {
        // Given
        HttpConnection.Response response = mock(HttpConnection.Response.class);
        doReturn(response).when(requestExecutor).execute();

        // When
        HttpConnection.Response result = requestExecutor.execute();

        // Then
        assertEquals(response, result);
        verify(requestExecutor, times(1)).execute();
    }

    @Test
    public void testExecute_ThrowsIOException() throws IOException {
        // Given
        doThrow(new IOException()).when(requestExecutor).execute();

        // When / Then
        assertThrows(IOException.class, () -> requestExecutor.execute());
        verify(requestExecutor, times(1)).execute();
    }

    @Test
    public void testResponseBody() throws IOException {
        // Given
        InputStream inputStream = mock(InputStream.class);
        doReturn(inputStream).when(requestExecutor).responseBody();

        // When
        InputStream result = requestExecutor.responseBody();

        // Then
        assertEquals(inputStream, result);
        verify(requestExecutor, times(1)).responseBody();
    }

    @Test
    public void testResponseBody_ThrowsIOException() throws IOException {
        // Given
        doThrow(new IOException()).when(requestExecutor).responseBody();

        // When / Then
        assertThrows(IOException.class, () -> requestExecutor.responseBody());
        verify(requestExecutor, times(1)).responseBody();
    }

    @Test
    public void testSafeClose() {
        // Given / When
        requestExecutor.safeClose();

        // Then
        verify(requestExecutor, times(1)).safeClose();
    }
}