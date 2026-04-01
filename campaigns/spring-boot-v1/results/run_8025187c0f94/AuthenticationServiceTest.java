import com.alibou.security.auth.AuthenticationResponse;
import com.alibou.security.auth.AuthenticationRequest;
import com.alibou.security.auth.RegisterRequest;
import com.alibou.security.token.TokenRepository;
import com.alibou.security.user.UserRepository;
import com.alibou.security.config.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService service;

    @Test
    void register() {
        // Arrange
        var request = RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role("USER")
                .build();

        var user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();

        var jwtToken = "jwtToken";
        var refreshToken = "refreshToken";

        when(repository.save(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any())).thenReturn(refreshToken);

        // Act
        var response = service.register(request);

        // Assert
        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());

        verify(repository, times(1)).save(any());
        verify(jwtService, times(1)).generateToken(any());
        verify(jwtService, times(1)).generateRefreshToken(any());
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void authenticate() {
        // Arrange
        var request = AuthenticationRequest.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();

        var user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();

        var jwtToken = "jwtToken";
        var refreshToken = "refreshToken";

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(repository.findByEmail(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any())).thenReturn(refreshToken);

        // Act
        var response = service.authenticate(request);

        // Assert
        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(repository, times(1)).findByEmail(any());
        verify(jwtService, times(1)).generateToken(any());
        verify(jwtService, times(1)).generateRefreshToken(any());
        verify(tokenRepository, times(1)).save(any());
    }

    @Test
    void refreshToken() throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        var authHeader = "Bearer refreshToken";
        var userEmail = "john.doe@example.com";

        var user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();

        var jwtToken = "jwtToken";
        var refreshToken = "refreshToken";

        when(request.getHeader(any())).thenReturn(authHeader);
        when(jwtService.extractUsername(any())).thenReturn(userEmail);
        when(repository.findByEmail(any())).thenReturn(user);
        when(jwtService.isTokenValid(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn(jwtToken);

        // Act
        service.refreshToken(request, response);

        // Assert
        verify(jwtService, times(1)).extractUsername(any());
        verify(repository, times(1)).findByEmail(any());
        verify(jwtService, times(1)).isTokenValid(any(), any());
        verify(jwtService, times(1)).generateToken(any());
        verify(tokenRepository, times(1)).save(any());
    }
}