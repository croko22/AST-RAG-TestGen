import org.apache.commons.dbutils.BeanProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeanProcessorTest {

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSetMetaData resultSetMetaData;

    private BeanProcessor beanProcessor;

    @BeforeEach
    void setup() {
        beanProcessor = new BeanProcessor();
    }

    @Test
    void testPopulateBean() throws SQLException {
        // Given
        Object bean = new Object();
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSet.getObject(any())).thenReturn("value");

        // When
        Object populatedBean = beanProcessor.populateBean(resultSet, bean);

        // Then
        assertSame(bean, populatedBean);
        verify(resultSetMetaData, times(1)).getColumnCount();
        verify(resultSet, times(1)).getObject(any());
    }

    @Test
    void testToBean() throws SQLException {
        // Given
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSet.getObject(any())).thenReturn("value");

        // When
        Object bean = beanProcessor.toBean(resultSet, Object.class);

        // Then
        assertNotNull(bean);
        verify(resultSetMetaData, times(1)).getColumnCount();
        verify(resultSet, times(1)).getObject(any());
    }

    @Test
    void testToBeanList() throws SQLException {
        // Given
        when(resultSet.next()).thenReturn(true, false);
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSet.getObject(any())).thenReturn("value");

        // When
        List<Object> beans = (List<Object>) beanProcessor.toBeanList(resultSet, Object.class);

        // Then
        assertNotNull(beans);
        assertEquals(1, beans.size());
        verify(resultSet, times(2)).next();
        verify(resultSetMetaData, times(1)).getColumnCount();
        verify(resultSet, times(1)).getObject(any());
    }

    @Test
    void testToBeanListEmptyResultSet() throws SQLException {
        // Given
        when(resultSet.next()).thenReturn(false);

        // When
        List<Object> beans = (List<Object>) beanProcessor.toBeanList(resultSet, Object.class);

        // Then
        assertNotNull(beans);
        assertTrue(beans.isEmpty());
        verify(resultSet, times(1)).next();
    }

    @Test
    void testToBeanListMultipleRows() throws SQLException {
        // Given
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSet.getObject(any())).thenReturn("value");

        // When
        List<Object> beans = (List<Object>) beanProcessor.toBeanList(resultSet, Object.class);

        // Then
        assertNotNull(beans);
        assertEquals(2, beans.size());
        verify(resultSet, times(3)).next();
        verify(resultSetMetaData, times(1)).getColumnCount();
        verify(resultSet, times(2)).getObject(any());
    }

    @Test
    void testToBeanListSQLException() throws SQLException {
        // Given
        when(resultSet.next()).thenThrow(SQLException.class);

        // When and Then
        assertThrows(SQLException.class, () -> beanProcessor.toBeanList(resultSet, Object.class));
        verify(resultSet, times(1)).next();
    }
}