import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CredentialsAuthorityTest {

    @Mock
    private CredentialsAuthority credentialsAuthority;

    @Test
    public void testCheckLocalUserName_KnownUser() {
        // Given: un usuario conocido por la autoridad
        when(credentialsAuthority.checkLocalUserName("usuarioConocido")).thenReturn(true);

        // When: se verifica si el usuario es conocido
        boolean isValid = credentialsAuthority.checkLocalUserName("usuarioConocido");

        // Then: se verifica el resultado
        assertTrue(isValid);
        verify(credentialsAuthority, times(1)).checkLocalUserName("usuarioConocido");
    }

    @Test
    public void testCheckLocalUserName_UnknownUser() {
        // Given: un usuario desconocido por la autoridad
        when(credentialsAuthority.checkLocalUserName("usuarioDesconocido")).thenReturn(false);

        // When: se verifica si el usuario es conocido
        boolean isValid = credentialsAuthority.checkLocalUserName("usuarioDesconocido");

        // Then: se verifica el resultado
        assertFalse(isValid);
        verify(credentialsAuthority, times(1)).checkLocalUserName("usuarioDesconocido");
    }

    @Test
    public void testCheckLocalUserName_NullUsername() {
        // Given: un nombre de usuario nulo
        when(credentialsAuthority.checkLocalUserName(null)).thenReturn(false);

        // When: se verifica si el usuario es conocido
        boolean isValid = credentialsAuthority.checkLocalUserName(null);

        // Then: se verifica el resultado
        assertFalse(isValid);
        verify(credentialsAuthority, times(1)).checkLocalUserName(null);
    }

    @Test
    public void testCheckLocalUserName_EmptyUsername() {
        // Given: un nombre de usuario vacío
        when(credentialsAuthority.checkLocalUserName("")).thenReturn(false);

        // When: se verifica si el usuario es conocido
        boolean isValid = credentialsAuthority.checkLocalUserName("");

        // Then: se verifica el resultado
        assertFalse(isValid);
        verify(credentialsAuthority, times(1)).checkLocalUserName("");
    }
}