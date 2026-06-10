import net.hydromatic.morel.foreign.RelList;
import net.hydromatic.morel.compile.Environment;
import org.apache.calcite.DataContext;
import org.apache.calcite.interpreter.Interpreter;
import org.apache.calcite.rel.RelNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelListTest {

    @Mock
    private RelNode rel;

    @Mock
    private DataContext dataContext;

    @Mock
    private Function<Object[], Object> converter;

    @InjectMocks
    private RelList relList;

    @BeforeEach
    public void setup() {
        relList = new RelList(rel, dataContext, converter);
    }

    @Test
    public void testGet() {
        // Given
        List<Object> list = mock(List.class);
        when(list.get(0)).thenReturn("value");
        Supplier<List<Object>> supplier = () -> list;
        relList = new RelList(rel, dataContext, converter) {
            @Override
            Supplier<List<Object>> getSupplier() {
                return supplier;
            }
        };

        // When
        Object result = relList.get(0);

        // Then
        assertEquals("value", result);
        verify(list).get(0);
    }

    @Test
    public void testSize() {
        // Given
        List<Object> list = mock(List.class);
        when(list.size()).thenReturn(10);
        Supplier<List<Object>> supplier = () -> list;
        relList = new RelList(rel, dataContext, converter) {
            @Override
            Supplier<List<Object>> getSupplier() {
                return supplier;
            }
        };

        // When
        int result = relList.size();

        // Then
        assertEquals(10, result);
        verify(list).size();
    }

    @Test
    public void testToString() {
        // Given

        // When
        String result = relList.toString();

        // Then
        assertEquals("<list>", result);
    }

    @Test
    public void testAsString() {
        // Given
        List<Object> list = mock(List.class);
        when(list.toString()).thenReturn("listToString");
        Supplier<List<Object>> supplier = () -> list;
        relList = new RelList(rel, dataContext, converter) {
            @Override
            Supplier<List<Object>> getSupplier() {
                return supplier;
            }
        };

        // When
        String result = relList.asString();

        // Then
        assertEquals("listToString", result);
        verify(list).toString();
    }

    @Test
    public void testGet_IndexOutOfBoundsException() {
        // Given
        List<Object> list = mock(List.class);
        when(list.get(any())).thenThrow(IndexOutOfBoundsException.class);
        Supplier<List<Object>> supplier = () -> list;
        relList = new RelList(rel, dataContext, converter) {
            @Override
            Supplier<List<Object>> getSupplier() {
                return supplier;
            }
        };

        // When / Then
        assertThrows(IndexOutOfBoundsException.class, () -> relList.get(0));
    }

    @Test
    public void testSize_NullPointerException() {
        // Given
        Supplier<List<Object>> supplier = () -> null;
        relList = new RelList(rel, dataContext, converter) {
            @Override
            Supplier<List<Object>> getSupplier() {
                return supplier;
            }
        };

        // When / Then
        assertThrows(NullPointerException.class, () -> relList.size());
    }

    @Test
    public void testAsString_NullPointerException() {
        // Given
        Supplier<List<Object>> supplier = () -> null;
        relList = new RelList(rel, dataContext, converter) {
            @Override
            Supplier<List<Object>> getSupplier() {
                return supplier;
            }
        };

        // When / Then
        assertThrows(NullPointerException.class, () -> relList.asString());
    }

    // Helper method to get the supplier
    private Supplier<List<Object>> getSupplier(RelList relList) {
        try {
            java.lang.reflect.Field field = RelList.class.getDeclaredField("supplier");
            field.setAccessible(true);
            return (Supplier<List<Object>>) field.get(relList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}