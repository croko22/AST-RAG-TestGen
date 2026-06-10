import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SynchronizedSortedBagTest {

    @Mock
    private SortedBag<String> sortedBagMock;

    private SynchronizedSortedBag<String> synchronizedSortedBag;

    @BeforeEach
    public void setup() {
        synchronizedSortedBag = SynchronizedSortedBag.synchronizedSortedBag(sortedBagMock);
    }

    @Test
    public void testSynchronizedSortedBag() {
        // Given
        when(sortedBagMock.comparator()).thenReturn(Comparator.naturalOrder());
        when(sortedBagMock.first()).thenReturn("first");
        when(sortedBagMock.last()).thenReturn("last");

        // When
        Comparator<? super String> comparator = synchronizedSortedBag.comparator();
        String first = synchronizedSortedBag.first();
        String last = synchronizedSortedBag.last();

        // Then
        assertNotNull(comparator);
        assertEquals("first", first);
        assertEquals("last", last);
        verify(sortedBagMock, times(1)).comparator();
        verify(sortedBagMock, times(1)).first();
        verify(sortedBagMock, times(1)).last();
    }

    @Test
    public void testSynchronizedSortedBagNull() {
        // Given
        when(sortedBagMock.comparator()).thenReturn(null);
        when(sortedBagMock.first()).thenReturn(null);
        when(sortedBagMock.last()).thenReturn(null);

        // When
        Comparator<? super String> comparator = synchronizedSortedBag.comparator();
        String first = synchronizedSortedBag.first();
        String last = synchronizedSortedBag.last();

        // Then
        assertNull(comparator);
        assertNull(first);
        assertNull(last);
        verify(sortedBagMock, times(1)).comparator();
        verify(sortedBagMock, times(1)).first();
        verify(sortedBagMock, times(1)).last();
    }

    @Test
    public void testSynchronizedSortedBagFactoryMethod() {
        // Given
        SortedBag<String> sortedBag = mock(SortedBag.class);

        // When
        SynchronizedSortedBag<String> synchronizedSortedBag = SynchronizedSortedBag.synchronizedSortedBag(sortedBag);

        // Then
        assertNotNull(synchronizedSortedBag);
        assertNotSame(sortedBag, synchronizedSortedBag);
    }

    @Test
    public void testSynchronizedSortedBagFactoryMethodNull() {
        // Given
        SortedBag<String> sortedBag = null;

        // When and Then
        assertThrows(NullPointerException.class, () -> SynchronizedSortedBag.synchronizedSortedBag(sortedBag));
    }
}