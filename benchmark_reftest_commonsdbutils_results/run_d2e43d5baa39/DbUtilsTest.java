import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DbUtilsTest {

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private Driver driver;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private Properties properties;

    @BeforeEach
    void setup() {
        // Setup mock behavior
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(any())).thenReturn(resultSet);
        when(driver.acceptsURL(any())).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        // Reset mock behavior
        reset(connection, statement, resultSet, driver, printWriter, properties);
    }

    @Test
    void testClose_ConnectionNotNull_CloseCalled() throws SQLException {
        // Given: a non-null connection
        // When: close is called
        DbUtils.close(connection);
        // Then: close is called on the connection
        verify(connection, times(1)).close();
    }

    @Test
    void testClose_ConnectionNull_CloseNotCalled() throws SQLException {
        // Given: a null connection
        Connection nullConnection = null;
        // When: close is called
        DbUtils.close(nullConnection);
        // Then: close is not called
        verifyNoInteractions(connection);
    }

    @Test
    void testCloseQuietly_ConnectionNotNull_CloseCalled() {
        // Given: a non-null connection
        // When: closeQuietly is called
        DbUtils.closeQuietly(connection);
        // Then: close is called on the connection
        verify(connection, times(1)).close();
    }

    @Test
    void testCloseQuietly_ConnectionNull_CloseNotCalled() {
        // Given: a null connection
        Connection nullConnection = null;
        // When: closeQuietly is called
        DbUtils.closeQuietly(nullConnection);
        // Then: close is not called
        verifyNoInteractions(connection);
    }

    @Test
    void testCloseQuietly_StatementNotNull_CloseCalled() {
        // Given: a non-null statement
        // When: closeQuietly is called
        DbUtils.closeQuietly(statement);
        // Then: close is called on the statement
        verify(statement, times(1)).close();
    }

    @Test
    void testCloseQuietly_StatementNull_CloseNotCalled() {
        // Given: a null statement
        Statement nullStatement = null;
        // When: closeQuietly is called
        DbUtils.closeQuietly(nullStatement);
        // Then: close is not called
        verifyNoInteractions(statement);
    }

    @Test
    void testCloseQuietly_ResultSetNotNull_CloseCalled() {
        // Given: a non-null result set
        // When: closeQuietly is called
        DbUtils.closeQuietly(resultSet);
        // Then: close is called on the result set
        verify(resultSet, times(1)).close();
    }

    @Test
    void testCloseQuietly_ResultSetNull_CloseNotCalled() {
        // Given: a null result set
        ResultSet nullResultSet = null;
        // When: closeQuietly is called
        DbUtils.closeQuietly(nullResultSet);
        // Then: close is not called
        verifyNoInteractions(resultSet);
    }

    @Test
    void testCommitAndClose_ConnectionNotNull_CommitAndCloseCalled() throws SQLException {
        // Given: a non-null connection
        // When: commitAndClose is called
        DbUtils.commitAndClose(connection);
        // Then: commit and close are called on the connection
        verify(connection, times(1)).commit();
        verify(connection, times(1)).close();
    }

    @Test
    void testCommitAndClose_ConnectionNull_CommitAndCloseNotCalled() throws SQLException {
        // Given: a null connection
        Connection nullConnection = null;
        // When: commitAndClose is called
        DbUtils.commitAndClose(nullConnection);
        // Then: commit and close are not called
        verifyNoInteractions(connection);
    }

    @Test
    void testCommitAndCloseQuietly_ConnectionNotNull_CommitAndCloseCalled() {
        // Given: a non-null connection
        // When: commitAndCloseQuietly is called
        DbUtils.commitAndCloseQuietly(connection);
        // Then: commit and close are called on the connection
        verify(connection, times(1)).commit();
        verify(connection, times(1)).close();
    }

    @Test
    void testCommitAndCloseQuietly_ConnectionNull_CommitAndCloseNotCalled() {
        // Given: a null connection
        Connection nullConnection = null;
        // When: commitAndCloseQuietly is called
        DbUtils.commitAndCloseQuietly(nullConnection);
        // Then: commit and close are not called
        verifyNoInteractions(connection);
    }

    @Test
    void testLoadDriver_DriverFound_DriverLoaded() {
        // Given: a driver class name
        String driverClassName = "org.apache.commons.dbutils.Driver";
        // When: loadDriver is called
        boolean loaded = DbUtils.loadDriver(driverClassName);
        // Then: the driver is loaded
        assertTrue(loaded);
    }

    @Test
    void testLoadDriver_DriverNotFound_DriverNotLoaded() {
        // Given: a non-existent driver class name
        String driverClassName = "non.existent.Driver";
        // When: loadDriver is called
        boolean loaded = DbUtils.loadDriver(driverClassName);
        // Then: the driver is not loaded
        assertFalse(loaded);
    }

    @Test
    void testPrintStackTrace_StackTrace Printed() {
        // Given: a SQLException
        SQLException exception = new SQLException("Test exception");
        // When: printStackTrace is called
        DbUtils.printStackTrace(exception);
        // Then: the stack trace is printed
        verify(printWriter, times(1)).println(any());
    }

    @Test
    void testPrintWarnings_Warnings Printed() {
        // Given: a Connection
        // When: printWarnings is called
        DbUtils.printWarnings(connection);
        // Then: the warnings are printed
        verify(printWriter, times(1)).println(any());
    }

    @Test
    void testRollback_ConnectionNotNull_RollbackCalled() throws SQLException {
        // Given: a non-null connection
        // When: rollback is called
        DbUtils.rollback(connection);
        // Then: rollback is called on the connection
        verify(connection, times(1)).rollback();
    }

    @Test
    void testRollback_ConnectionNull_RollbackNotCalled() throws SQLException {
        // Given: a null connection
        Connection nullConnection = null;
        // When: rollback is called
        DbUtils.rollback(nullConnection);
        // Then: rollback is not called
        verifyNoInteractions(connection);
    }

    @Test
    void testRollbackAndClose_ConnectionNotNull_RollbackAndCloseCalled() throws SQLException {
        // Given: a non-null connection
        // When: rollbackAndClose is called
        DbUtils.rollbackAndClose(connection);
        // Then: rollback and close are called on the connection
        verify(connection, times(1)).rollback();
        verify(connection, times(1)).close();
    }

    @Test
    void testRollbackAndClose_ConnectionNull_RollbackAndCloseNotCalled() throws SQLException {
        // Given: a null connection
        Connection nullConnection = null;
        // When: rollbackAndClose is called
        DbUtils.rollbackAndClose(nullConnection);
        // Then: rollback and close are not called
        verifyNoInteractions(connection);
    }

    @Test
    void testRollbackAndCloseQuietly_ConnectionNotNull_RollbackAndCloseCalled() {
        // Given: a non-null connection
        // When: rollbackAndCloseQuietly is called
        DbUtils.rollbackAndCloseQuietly(connection);
        // Then: rollback and close are called on the connection
        verify(connection, times(1)).rollback();
        verify(connection, times(1)).close();
    }

    @Test
    void testRollbackAndCloseQuietly_ConnectionNull_RollbackAndCloseNotCalled() {
        // Given: a null connection
        Connection nullConnection = null;
        // When: rollbackAndCloseQuietly is called
        DbUtils.rollbackAndCloseQuietly(nullConnection);
        // Then: rollback and close are not called
        verifyNoInteractions(connection);
    }

    @Test
    void testRollbackQuietly_ConnectionNotNull_RollbackCalled() {
        // Given: a non-null connection
        // When: rollbackQuietly is called
        DbUtils.rollbackQuietly(connection);
        // Then: rollback is called on the connection
        verify(connection, times(1)).rollback();
    }

    @Test
    void testRollbackQuietly_ConnectionNull_RollbackNotCalled() {
        // Given: a null connection
        Connection nullConnection = null;
        // When: rollbackQuietly is called
        DbUtils.rollbackQuietly(nullConnection);
        // Then: rollback is not called
        verifyNoInteractions(connection);
    }
}