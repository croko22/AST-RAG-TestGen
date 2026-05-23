import org.apache.commons.cli.TypeHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TypeHandlerTest {

    @Test
    public void testCreateClass_ClassNameExists() throws Exception {
        // Given
        String classname = "java.lang.String";

        // When
        Class<?> clazz = TypeHandler.createClass(classname);

        // Then
        assertNotNull(clazz);
        assertEquals("java.lang.String", clazz.getName());
    }

    @Test
    public void testCreateClass_ClassNameDoesNotExist() {
        // Given
        String classname = "NonExistentClass";

        // When and Then
        assertThrows(Exception.class, () -> TypeHandler.createClass(classname));
    }

    @Test
    public void testCreateDate() {
        // Given
        String str = "2022-01-01";

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(str));
    }

    @Test
    public void testCreateFile() {
        // Given
        String str = "/path/to/file";

        // When
        File file = TypeHandler.createFile(str);

        // Then
        assertNotNull(file);
        assertEquals(str, file.getPath());
    }

    @Test
    public void testCreateNumber_ValidNumber() throws Exception {
        // Given
        String str = "123";

        // When
        Number number = TypeHandler.createNumber(str);

        // Then
        assertNotNull(number);
        assertEquals(123L, number.longValue());
    }

    @Test
    public void testCreateNumber_InvalidNumber() {
        // Given
        String str = "abc";

        // When and Then
        assertThrows(Exception.class, () -> TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateNumber_ValidDecimalNumber() throws Exception {
        // Given
        String str = "123.45";

        // When
        Number number = TypeHandler.createNumber(str);

        // Then
        assertNotNull(number);
        assertEquals(123.45, number.doubleValue());
    }

    @Test
    public void testCreateObject_ClassNameExists() throws Exception {
        // Given
        String classname = "java.lang.String";

        // When
        Object obj = TypeHandler.createObject(classname);

        // Then
        assertNotNull(obj);
        assertTrue(obj instanceof String);
    }

    @Test
    public void testCreateObject_ClassNameDoesNotExist() {
        // Given
        String classname = "NonExistentClass";

        // When and Then
        assertThrows(Exception.class, () -> TypeHandler.createObject(classname));
    }

    @Test
    public void testCreateURL_ValidURL() throws Exception {
        // Given
        String str = "https://www.example.com";

        // When
        URL url = TypeHandler.createURL(str);

        // Then
        assertNotNull(url);
        assertEquals(str, url.toString());
    }

    @Test
    public void testCreateURL_InvalidURL() {
        // Given
        String str = "invalid url";

        // When and Then
        assertThrows(Exception.class, () -> TypeHandler.createURL(str));
    }

    @Test
    public void testCreateValue_StringValue() throws Exception {
        // Given
        String str = "hello";
        Class<String> clazz = String.class;

        // When
        String value = TypeHandler.createValue(str, clazz);

        // Then
        assertNotNull(value);
        assertEquals(str, value);
    }

    @Test
    public void testCreateValue_ObjectValue() throws Exception {
        // Given
        String str = "java.lang.String";
        Class<?> clazz = Class.forName("java.lang.Class");

        // When
        Object value = TypeHandler.createValue(str, clazz);

        // Then
        assertNotNull(value);
        assertTrue(value instanceof Class);
    }

    @Test
    public void testCreateValue_NumberValue() throws Exception {
        // Given
        String str = "123";
        Class<Number> clazz = Number.class;

        // When
        Number value = TypeHandler.createValue(str, clazz);

        // Then
        assertNotNull(value);
        assertEquals(123L, value.longValue());
    }

    @Test
    public void testCreateValue_DateValue() {
        // Given
        String str = "2022-01-01";
        Class<Date> clazz = Date.class;

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_ClassValue() throws Exception {
        // Given
        String str = "java.lang.String";
        Class<Class<?>> clazz = Class.class;

        // When
        Class<?> value = TypeHandler.createValue(str, clazz);

        // Then
        assertNotNull(value);
        assertEquals("java.lang.String", value.getName());
    }

    @Test
    public void testCreateValue_FileValue() throws Exception {
        // Given
        String str = "/path/to/file";
        Class<File> clazz = File.class;

        // When
        File value = TypeHandler.createValue(str, clazz);

        // Then
        assertNotNull(value);
        assertEquals(str, value.getPath());
    }

    @Test
    public void testCreateValue_ExistingFileValue() throws Exception {
        // Given
        String str = "/path/to/existing/file";
        Class<FileInputStream> clazz = FileInputStream.class;

        // When
        FileInputStream value = (FileInputStream) TypeHandler.createValue(str, clazz);

        // Then
        assertNotNull(value);
    }

    @Test
    public void testCreateValue_FilesValue() {
        // Given
        String str = "/path/to/files";
        Class<File[]> clazz = File[].class;

        // When and Then
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_URLValue() throws Exception {
        // Given
        String str = "https://www.example.com";
        Class<URL> clazz = URL.class;

        // When
        URL value = TypeHandler.createValue(str, clazz);

        // Then
        assertNotNull(value);
        assertEquals(str, value.toString());
    }

    @Test
    public void testOpenFile_ExistingFile() throws Exception {
        // Given
        String str = "/path/to/existing/file";

        // When
        FileInputStream fileInputStream = TypeHandler.openFile(str);

        // Then
        assertNotNull(fileInputStream);
    }

    @Test
    public void testOpenFile_NonExistingFile() {
        // Given
        String str = "/path/to/non/existing/file";

        // When and Then
        assertThrows(Exception.class, () -> TypeHandler.openFile(str));
    }
}