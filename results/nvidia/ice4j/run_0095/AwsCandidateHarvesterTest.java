import org.ice4j.TransportAddress;
import org.ice4j.ice.harvest.AwsCandidateHarvester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AwsCandidateHarvesterTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private AwsCandidateHarvester awsCandidateHarvester;

    @BeforeEach
    void setUp() {
        awsCandidateHarvester = new AwsCandidateHarvester();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(httpClient, httpResponse);
    }

    @Test
    public void testGetMask_NotRunningOnEC2() {
        // Given
        when(AwsCandidateHarvester.smellsLikeAnEC2()).thenReturn(false);

        // When
        TransportAddress mask = awsCandidateHarvester.getMask();

        // Then
        assertNull(mask);
    }

    @Test
    public void testGetMask_RunningOnEC2() throws Exception {
        // Given
        when(AwsCandidateHarvester.smellsLikeAnEC2()).thenReturn(true);
        when(httpClient.send(any(HttpRequest.class), any())).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("192.168.1.100");
        when(httpResponse.statusCode()).thenReturn(200);

        // When
        TransportAddress mask = awsCandidateHarvester.getMask();

        // Then
        assertNotNull(mask);
        assertEquals("192.168.1.100", mask.getHostAddress());
    }

    @Test
    public void testGetMask_RunningOnEC2_FetchFails() throws Exception {
        // Given
        when(AwsCandidateHarvester.smellsLikeAnEC2()).thenReturn(true);
        when(httpClient.send(any(HttpRequest.class), any())).thenThrow(new IOException());

        // When
        TransportAddress mask = awsCandidateHarvester.getMask();

        // Then
        assertNull(mask);
    }

    @Test
    public void testGetFace_NotRunningOnEC2() {
        // Given
        when(AwsCandidateHarvester.smellsLikeAnEC2()).thenReturn(false);

        // When
        TransportAddress face = awsCandidateHarvester.getFace();

        // Then
        assertNull(face);
    }

    @Test
    public void testGetFace_RunningOnEC2() throws Exception {
        // Given
        when(AwsCandidateHarvester.smellsLikeAnEC2()).thenReturn(true);
        when(httpClient.send(any(HttpRequest.class), any())).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("192.168.1.100");
        when(httpResponse.statusCode()).thenReturn(200);

        // When
        TransportAddress face = awsCandidateHarvester.getFace();

        // Then
        assertNotNull(face);
        assertEquals("192.168.1.100", face.getHostAddress());
    }

    @Test
    public void testGetFace_RunningOnEC2_FetchFails() throws Exception {
        // Given
        when(AwsCandidateHarvester.smellsLikeAnEC2()).thenReturn(true);
        when(httpClient.send(any(HttpRequest.class), any())).thenThrow(new IOException());

        // When
        TransportAddress face = awsCandidateHarvester.getFace();

        // Then
        assertNull(face);
    }

    @Test
    public void testSmellsLikeAnEC2_True() throws Exception {
        // Given
        when(httpClient.send(any(HttpRequest.class), any())).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("token");
        when(httpResponse.statusCode()).thenReturn(200);

        // When
        boolean result = AwsCandidateHarvester.smellsLikeAnEC2();

        // Then
        assertTrue(result);
    }

    @Test
    public void testSmellsLikeAnEC2_False() throws Exception {
        // Given
        when(httpClient.send(any(HttpRequest.class), any())).thenThrow(new IOException());

        // When
        boolean result = AwsCandidateHarvester.smellsLikeAnEC2();

        // Then
        assertFalse(result);
    }
}