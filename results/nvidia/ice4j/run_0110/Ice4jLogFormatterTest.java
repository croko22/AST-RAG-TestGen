import org.ice4j.util.Ice4jLogFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Ice4jLogFormatterTest {

    private Ice4jLogFormatter ice4jLogFormatter;

    @BeforeEach
    public void setup() {
        ice4jLogFormatter = new Ice4jLogFormatter();
    }

    @Test
    public void testFormat_LogRecordWithMessage() {
        // Given
        LogRecord logRecord = new LogRecord(Level.INFO, "Test message");
        logRecord.setLoggerName("TestLogger");
        logRecord.setSourceClassName("TestClass");
        logRecord.setSourceMethodName("testMethod");

        // When
        String formattedLog = ice4jLogFormatter.format(logRecord);

        // Then
        assertNotNull(formattedLog);
        assertTrue(formattedLog.contains("Test message"));
        assertTrue(formattedLog.contains("TestLogger"));
        assertTrue(formattedLog.contains("TestClass"));
        assertTrue(formattedLog.contains("testMethod"));
    }

    @Test
    public void testFormat_LogRecordWithThrowable() {
        // Given
        LogRecord logRecord = new LogRecord(Level.SEVERE, "Test message");
        logRecord.setLoggerName("TestLogger");
        logRecord.setSourceClassName("TestClass");
        logRecord.setSourceMethodName("testMethod");
        logRecord.setThrown(new Exception("Test exception"));

        // When
        String formattedLog = ice4jLogFormatter.format(logRecord);

        // Then
        assertNotNull(formattedLog);
        assertTrue(formattedLog.contains("Test message"));
        assertTrue(formattedLog.contains("TestLogger"));
        assertTrue(formattedLog.contains("TestClass"));
        assertTrue(formattedLog.contains("testMethod"));
        assertTrue(formattedLog.contains("Test exception"));
    }

    @Test
    public void testFormat_LogRecordWithNullLoggerName() {
        // Given
        LogRecord logRecord = new LogRecord(Level.INFO, "Test message");
        logRecord.setSourceClassName("TestClass");
        logRecord.setSourceMethodName("testMethod");

        // When
        String formattedLog = ice4jLogFormatter.format(logRecord);

        // Then
        assertNotNull(formattedLog);
        assertTrue(formattedLog.contains("Test message"));
        assertTrue(formattedLog.contains("TestClass"));
        assertTrue(formattedLog.contains("testMethod"));
    }

    @Test
    public void testInferCaller_LogRecord() {
        // Given
        LogRecord logRecord = new LogRecord(Level.INFO, "Test message");

        // When
        int lineNumber = ice4jLogFormatter.inferCaller(logRecord);

        // Then
        assertTrue(lineNumber >= 0);
    }

    @Test
    public void testFormat_LogRecordWithNullSourceClassName() {
        // Given
        LogRecord logRecord = new LogRecord(Level.INFO, "Test message");
        logRecord.setLoggerName("TestLogger");

        // When
        String formattedLog = ice4jLogFormatter.format(logRecord);

        // Then
        assertNotNull(formattedLog);
        assertTrue(formattedLog.contains("Test message"));
        assertTrue(formattedLog.contains("TestLogger"));
    }
}