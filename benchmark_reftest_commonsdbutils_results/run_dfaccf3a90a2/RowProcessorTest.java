import org.apache.commons.dbutils.RowProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RowProcessorTest {

    @Mock
    private ResultSet resultSet;

    @Mock
    private RowProcessor rowProcessor;

    @BeforeEach
    void setup() {
        // Initialize mocks
        rowProcessor = new BasicRowProcessor();
    }

    @Test
    void testToArray() throws SQLException {
        // Given: Mock ResultSet with data
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");

        // When: Call toArray method
        Object[] array = rowProcessor.toArray(resultSet);

        // Then: Verify result and interactions
        assertNotNull(array);
        assertEquals(2, array.length);
        assertEquals("value1", array[0]);
        assertEquals("value2", array[1]);
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getString(1);
        verify(resultSet, times(1)).getString(2);
    }

    @Test
    void testToArray_NoData() throws SQLException {
        // Given: Mock ResultSet with no data
        when(resultSet.next()).thenReturn(false);

        // When: Call toArray method
        Object[] array = rowProcessor.toArray(resultSet);

        // Then: Verify result and interactions
        assertNotNull(array);
        assertEquals(0, array.length);
        verify(resultSet, times(1)).next();
    }

    @Test
    void testToArray_ThrowsSQLException() throws SQLException {
        // Given: Mock ResultSet that throws SQLException
        when(resultSet.next()).thenThrow(SQLException.class);

        // When / Then: Verify exception is thrown
        assertThrows(SQLException.class, () -> rowProcessor.toArray(resultSet));
        verify(resultSet, times(1)).next();
    }

    @Test
    void testToBean() throws SQLException {
        // Given: Mock ResultSet with data
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");

        // When: Call toBean method
        Object bean = rowProcessor.toBean(resultSet, Object.class);

        // Then: Verify result and interactions
        assertNotNull(bean);
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getString(1);
        verify(resultSet, times(1)).getString(2);
    }

    @Test
    void testToBean_NoData() throws SQLException {
        // Given: Mock ResultSet with no data
        when(resultSet.next()).thenReturn(false);

        // When: Call toBean method
        Object bean = rowProcessor.toBean(resultSet, Object.class);

        // Then: Verify result and interactions
        assertNull(bean);
        verify(resultSet, times(1)).next();
    }

    @Test
    void testToBean_ThrowsSQLException() throws SQLException {
        // Given: Mock ResultSet that throws SQLException
        when(resultSet.next()).thenThrow(SQLException.class);

        // When / Then: Verify exception is thrown
        assertThrows(SQLException.class, () -> rowProcessor.toBean(resultSet, Object.class));
        verify(resultSet, times(1)).next();
    }

    @Test
    void testToBeanList() throws SQLException {
        // Given: Mock ResultSet with data
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("value1").thenReturn("value2");
        when(resultSet.getString(2)).thenReturn("value3").thenReturn("value4");

        // When: Call toBeanList method
        List<Object> beanList = rowProcessor.toBeanList(resultSet, Object.class);

        // Then: Verify result and interactions
        assertNotNull(beanList);
        assertEquals(2, beanList.size());
        verify(resultSet, times(3)).next();
        verify(resultSet, times(2)).getString(1);
        verify(resultSet, times(2)).getString(2);
    }

    @Test
    void testToBeanList_NoData() throws SQLException {
        // Given: Mock ResultSet with no data
        when(resultSet.next()).thenReturn(false);

        // When: Call toBeanList method
        List<Object> beanList = rowProcessor.toBeanList(resultSet, Object.class);

        // Then: Verify result and interactions
        assertNotNull(beanList);
        assertEquals(0, beanList.size());
        verify(resultSet, times(1)).next();
    }

    @Test
    void testToBeanList_ThrowsSQLException() throws SQLException {
        // Given: Mock ResultSet that throws SQLException
        when(resultSet.next()).thenThrow(SQLException.class);

        // When / Then: Verify exception is thrown
        assertThrows(SQLException.class, () -> rowProcessor.toBeanList(resultSet, Object.class));
        verify(resultSet, times(1)).next();
    }

    @Test
    void testToMap() throws SQLException {
        // Given: Mock ResultSet with data
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");

        // When: Call toMap method
        Map<String, Object> map = rowProcessor.toMap(resultSet);

        // Then: Verify result and interactions
        assertNotNull(map);
        assertEquals(2, map.size());
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getString(1);
        verify(resultSet, times(1)).getString(2);
    }

    @Test
    void testToMap_NoData() throws SQLException {
        // Given: Mock ResultSet with no data
        when(resultSet.next()).thenReturn(false);

        // When: Call toMap method
        Map<String, Object> map = rowProcessor.toMap(resultSet);

        // Then: Verify result and interactions
        assertNotNull(map);
        assertEquals(0, map.size());
        verify(resultSet, times(1)).next();
    }

    @Test
    void testToMap_ThrowsSQLException() throws SQLException {
        // Given: Mock ResultSet that throws SQLException
        when(resultSet.next()).thenThrow(SQLException.class);

        // When / Then: Verify exception is thrown
        assertThrows(SQLException.class, () -> rowProcessor.toMap(resultSet));
        verify(resultSet, times(1)).next();
    }
}