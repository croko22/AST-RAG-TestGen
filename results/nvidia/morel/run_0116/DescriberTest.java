import net.hydromatic.morel.eval.Describer;
import net.hydromatic.morel.eval.Describer.Detail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DescriberTest {

    @Mock
    private Describer describer;

    @Mock
    private Detail detail;

    @Mock
    private Consumer<Detail> detailConsumer;

    @Mock
    private Describer.Describable describable;

    @BeforeEach
    void setup() {
        when(describer.start(any(), any())).thenReturn(describer);
        when(describer.register(any(), anyInt())).thenReturn(0);
        when(detail.arg(any(), any())).thenReturn(detail);
        when(detail.args(any(), any())).thenReturn(detail);
        when(detail.arg(any(), any(Describer.Describable.class))).thenReturn(detail);
    }

    @Test
    void testStart() {
        // Given: a describer and a detail consumer
        // When: start is called with a name and the detail consumer
        Describer result = describer.start("name", detailConsumer);

        // Then: the result is the same describer
        assertSame(describer, result);

        // Verify: the start method was called with the correct arguments
        verify(describer).start("name", detailConsumer);
    }

    @Test
    void testRegister() {
        // Given: a describer and a name and ordinal
        // When: register is called with the name and ordinal
        int result = describer.register("name", 0);

        // Then: the result is 0
        assertEquals(0, result);

        // Verify: the register method was called with the correct arguments
        verify(describer).register("name", 0);
    }

    @Test
    void testAddStartAction() {
        // Given: a describer and a runnable
        Runnable runnable = () -> {};

        // When: addStartAction is called with the runnable
        describer.addStartAction(runnable);

        // Then: no exception is thrown

        // Verify: the addStartAction method was called with the correct argument
        verify(describer).addStartAction(runnable);
    }

    @Test
    void testDetailArg() {
        // Given: a detail and a name and value
        // When: arg is called with the name and value
        Detail result = detail.arg("name", "value");

        // Then: the result is the same detail
        assertSame(detail, result);

        // Verify: the arg method was called with the correct arguments
        verify(detail).arg("name", "value");
    }

    @Test
    void testDetailArgs() {
        // Given: a detail and a name and iterable value
        // When: args is called with the name and iterable value
        Detail result = detail.args("name", java.util.Collections.emptyList());

        // Then: the result is the same detail
        assertSame(detail, result);

        // Verify: the args method was called with the correct arguments
        verify(detail).args("name", java.util.Collections.emptyList());
    }

    @Test
    void testDetailArgDescribable() {
        // Given: a detail and a name and describable value
        // When: arg is called with the name and describable value
        Detail result = detail.arg("name", describable);

        // Then: the result is the same detail
        assertSame(detail, result);

        // Verify: the arg method was called with the correct arguments
        verify(detail).arg("name", describable);
    }

    @Test
    void testDetailArgIf() {
        // Given: a detail and a name and describable value and condition
        // When: argIf is called with the name and describable value and condition
        Detail result = detail.argIf("name", describable, true);

        // Then: the result is the same detail
        assertSame(detail, result);

        // Verify: the arg method was called with the correct arguments
        verify(detail).arg("name", describable);
    }

    @Test
    void testDetailArgIfFalse() {
        // Given: a detail and a name and describable value and condition
        // When: argIf is called with the name and describable value and condition
        Detail result = detail.argIf("name", describable, false);

        // Then: the result is the same detail
        assertSame(detail, result);

        // Verify: the arg method was not called
        verify(detail, never()).arg("name", describable);
    }
}