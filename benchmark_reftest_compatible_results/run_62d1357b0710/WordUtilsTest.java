package net.datafaker.internal.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WordUtilsTest {

    @Test
    public void testCapitalize_NullInput() {
        // Given: input es null
        String input = null;
        
        // When: se ejecuta el metodo capitalize
        String result = WordUtils.capitalize(input);
        
        // Then: se verifica el resultado
        assertNull(result);
    }

    @Test
    public void testCapitalize_EmptyString() {
        // Given: input es una cadena vacia
        String input = "";
        
        // When: se ejecuta el metodo capitalize
        String result = WordUtils.capitalize(input);
        
        // Then: se verifica el resultado
        assertEquals("", result);
    }

    @Test
    public void testCapitalize_AlreadyCapitalized() {
        // Given: input ya esta capitalizado
        String input = "Hola";
        
        // When: se ejecuta el metodo capitalize
        String result = WordUtils.capitalize(input);
        
        // Then: se verifica el resultado
        assertEquals("Hola", result);
    }

    @Test
    public void testCapitalize_NotCapitalized() {
        // Given: input no esta capitalizado
        String input = "hola";
        
        // When: se ejecuta el metodo capitalize
        String result = WordUtils.capitalize(input);
        
        // Then: se verifica el resultado
        assertEquals("Hola", result);
    }

    @Test
    public void testCapitalize_SingleCharacter() {
        // Given: input es un solo caracter en minuscula
        String input = "a";
        
        // When: se ejecuta el metodo capitalize
        String result = WordUtils.capitalize(input);
        
        // Then: se verifica el resultado
        assertEquals("A", result);
    }

    @Test
    public void testCapitalize_SingleCharacterAlreadyCapitalized() {
        // Given: input es un solo caracter ya capitalizado
        String input = "A";
        
        // When: se ejecuta el metodo capitalize
        String result = WordUtils.capitalize(input);
        
        // Then: se verifica el resultado
        assertEquals("A", result);
    }
}