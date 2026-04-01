package com.alibou.security.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void changePassword_contrasenaActualIncorrecta_lanzaExcepcion() {
        // Arrange
        var request = new ChangePasswordRequest("actual", "nueva", "nueva");
        var connectedUser = new UsernamePasswordAuthenticationToken(new User("email", "password"), null);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // Act y Assert
        assertThrows(IllegalStateException.class, () -> userService.changePassword(request, connectedUser));
        verify(repository, never()).save(any());
    }

    @Test
    void changePassword_nuevasContrasenasNoCoinciden_lanzaExcepcion() {
        // Arrange
        var request = new ChangePasswordRequest("actual", "nueva1", "nueva2");
        var connectedUser = new UsernamePasswordAuthenticationToken(new User("email", "password"), null);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // Act y Assert
        assertThrows(IllegalStateException.class, () -> userService.changePassword(request, connectedUser));
        verify(repository, never()).save(any());
    }

    @Test
    void changePassword_contrasenaActualCorrecta_y_nuevasContrasenasCoinciden_actualizaContrasena() {
        // Arrange
        var request = new ChangePasswordRequest("actual", "nueva", "nueva");
        var connectedUser = new UsernamePasswordAuthenticationToken(new User("email", "password"), null);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("nueva");

        // Act
        userService.changePassword(request, connectedUser);

        // Assert
        verify(repository, times(1)).save(any());
    }
}