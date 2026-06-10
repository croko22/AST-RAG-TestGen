import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeriodicRunnableTest {

    @Mock
    private ScheduledExecutorService timer;

    @Mock
    private ExecutorService executor;

    @Mock
    private ScheduledFuture<?> scheduledSubmit;

    @Mock
    private Future<?> submittedExecute;

    private PeriodicRunnable periodicRunnable;

    @BeforeEach
    public void setup() {
        periodicRunnable = new PeriodicRunnable(timer, executor) {
            @Override
            protected Duration getDelayUntilNextRun() {
                return Duration.ZERO;
            }

            @Override
            protected void run() {
                // Do nothing
            }
        };
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(timer, executor, scheduledSubmit, submittedExecute);
    }

    @Test
    public void testSchedule() {
        // Given
        when(timer.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class))).thenReturn(scheduledSubmit);

        // When
        periodicRunnable.schedule();

        // Then
        assertTrue(periodicRunnable.running);
        verify(timer, times(1)).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testSchedule_AlreadyRunning() {
        // Given
        periodicRunnable.running = true;

        // When
        periodicRunnable.schedule();

        // Then
        assertTrue(periodicRunnable.running);
        verify(timer, never()).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testCancel() {
        // Given
        periodicRunnable.running = true;
        when(scheduledSubmit.cancel(true)).thenReturn(true);
        when(submittedExecute.cancel(true)).thenReturn(true);

        // When
        periodicRunnable.cancel();

        // Then
        assertFalse(periodicRunnable.running);
        verify(scheduledSubmit, times(1)).cancel(true);
        verify(submittedExecute, times(1)).cancel(true);
    }

    @Test
    public void testCancel_NotRunning() {
        // Given
        periodicRunnable.running = false;

        // When
        periodicRunnable.cancel();

        // Then
        assertFalse(periodicRunnable.running);
        verify(scheduledSubmit, never()).cancel(true);
        verify(submittedExecute, never()).cancel(true);
    }

    @Test
    public void testScheduleNextRun_DelayZero() {
        // Given
        Duration delay = Duration.ZERO;

        // When
        periodicRunnable.scheduleNextRun(delay);

        // Then
        assertTrue(periodicRunnable.running);
        verify(executor, times(1)).submit(any(Runnable.class));
    }

    @Test
    public void testScheduleNextRun_DelayNotZero() {
        // Given
        Duration delay = Duration.ofMillis(100);
        when(timer.schedule(any(Runnable.class), anyLong(), any(TimeUnit.class))).thenReturn(scheduledSubmit);

        // When
        periodicRunnable.scheduleNextRun(delay);

        // Then
        assertTrue(periodicRunnable.running);
        verify(timer, times(1)).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testScheduleNextRun_DelayNegative() {
        // Given
        Duration delay = Duration.ofMillis(-100);

        // When
        periodicRunnable.scheduleNextRun(delay);

        // Then
        assertFalse(periodicRunnable.running);
        verify(timer, never()).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
        verify(executor, never()).submit(any(Runnable.class));
    }

    @Test
    public void testSubmitExecuteRun() {
        // Given
        periodicRunnable.running = true;

        // When
        periodicRunnable.submitExecuteRun();

        // Then
        verify(executor, times(1)).submit(any(Runnable.class));
    }

    @Test
    public void testSubmitExecuteRun_NotRunning() {
        // Given
        periodicRunnable.running = false;

        // When
        periodicRunnable.submitExecuteRun();

        // Then
        verify(executor, never()).submit(any(Runnable.class));
    }

    @Test
    public void testExecuteRun() {
        // Given
        periodicRunnable.running = true;

        // When
        periodicRunnable.executeRun();

        // Then
        verify(periodicRunnable, times(1)).run();
        verify(periodicRunnable, times(1)).getDelayUntilNextRun();
        verify(periodicRunnable, times(1)).scheduleNextRun(any(Duration.class));
    }

    @Test
    public void testExecuteRun_NotRunning() {
        // Given
        periodicRunnable.running = false;

        // When
        periodicRunnable.executeRun();

        // Then
        verify(periodicRunnable, never()).run();
        verify(periodicRunnable, never()).getDelayUntilNextRun();
        verify(periodicRunnable, never()).scheduleNextRun(any(Duration.class));
    }

    @Test
    public void testCreate() {
        // Given
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Duration period = Duration.ofMillis(100);
        Runnable r = () -> {
            // Do nothing
        };

        // When
        PeriodicRunnable periodicRunnable = PeriodicRunnable.create(timer, executor, period, r);

        // Then
        assertNotNull(periodicRunnable);
        assertTrue(periodicRunnable instanceof PeriodicRunnable);
    }
}