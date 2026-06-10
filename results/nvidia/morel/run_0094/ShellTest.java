Here is a complete test class for the provided `Shell` class:

```java
import net.hydromatic.morel.Config;
import net.hydromatic.morel.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShellTest {

    @Mock
    private Config config;

    @Mock
    private InputStream in;

    @Mock
    private OutputStream out;

    private Shell shell;

    @BeforeEach
    void setup() {
        shell = Shell.create(config, in, out);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(config, in, out);
    }

    @Test
    void testMain() {
        // Given
        String[] args = new String[0];

        // When
        Shell.main(args);

        // Then
        // No assertions, just verify that the method runs without exceptions
    }

    @Test
    void testCreate() {
        // Given
        List<String> args = new ArrayList<>();
        InputStream in = new ByteArrayInputStream("".getBytes());
        OutputStream out = new ByteArrayOutputStream();

        // When
        Shell.create(args, in, out);

        // Then
        // No assertions, just verify that the method runs without exceptions
    }

    @Test
    void testCreateWithConfig() {
        // Given
        Config config = mock(Config.class);
        InputStream in = new ByteArrayInputStream("".getBytes());
        OutputStream out = new ByteArrayOutputStream();

        // When
        Shell.create(config, in, out);

        // Then
        // No assertions, just verify that the method runs without exceptions
    }

    @Test
    void testParse() {
        // Given
        Config config = mock(Config.class);
        List<String> argList = new ArrayList<>();

        // When
        Config result = Shell.parse(config, argList);

        // Then
        assertNotNull(result);
    }

    @Test
    void testRun() {
        // Given
        doReturn(true).when(config).withHelp(true);

        // When
        shell.run();

        // Then
        verify(config, times(1)).withHelp(true);
    }

    @Test
    void testWithBanner() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);

        // When
        ConfigImpl result = config.withBanner(true);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithDumb() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);

        // When
        ConfigImpl result = config.withDumb(true);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithSystem() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);

        // When
        ConfigImpl result = config.withSystem(true);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithEcho() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);

        // When
        ConfigImpl result = config.withEcho(true);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithHelp() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);

        // When
        ConfigImpl result = config.withHelp(true);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithValueMap() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);
        Map<String, Object> valueMap = mock(Map.class);

        // When
        ConfigImpl result = config.withValueMap((Map<String, Object>) valueMap);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithDirectory() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);
        File directory = mock(File.class);

        // When
        ConfigImpl result = config.withDirectory(directory);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithPauseFn() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);
        Runnable pauseFn = mock(Runnable.class);

        // When
        Config result = config.withPauseFn(pauseFn);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithMaxUseDepth() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);

        // When
        ConfigImpl result = config.withMaxUseDepth(10);

        // Then
        assertNotNull(result);
    }

    @Test
    void testWithEval() {
        // Given
        ConfigImpl config = mock(ConfigImpl.class);
        String eval = "eval";

        // When
        ConfigImpl result = config.withEval(eval);

        // Then
        assertNotNull(result);
    }
}