package com.github.davidmoten.rtree;

import com.github.davidmoten.guavamini.annotations.VisibleForTesting;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.internal.util.ImmutableStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rx.Producer;
import rx.Subscriber;
import rx.functions.Func1;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OnSubscribeSearchTest {

    @Mock
    private com.github.davidmoten.rtree.Node<String, Geometry> node;

    @Mock
    private Func1<Geometry, Boolean> condition;

    @Mock
    private Subscriber<com.github.davidmoten.rtree.Entry<String, Geometry>> subscriber;

    private OnSubscribeSearch<String, Geometry> onSubscribeSearch;

    @BeforeEach
    void setup() {
        onSubscribeSearch = new OnSubscribeSearch<>(node, condition);
    }

    @Test
    void testCall() {
        // Given
        doNothing().when(subscriber).setProducer(any());

        // When
        onSubscribeSearch.call(subscriber);

        // Then
        verify(subscriber, times(1)).setProducer(any());
    }

    @Test
    void testRequest_LongMaxValue() {
        // Given
        OnSubscribeSearch.SearchProducer<String, Geometry> searchProducer = new OnSubscribeSearch.SearchProducer<>(node, condition, subscriber);
        doNothing().when(node).searchWithoutBackpressure(any(), any());

        // When
        searchProducer.request(Long.MAX_VALUE);

        // Then
        verify(node, times(1)).searchWithoutBackpressure(any(), any());
        verify(subscriber, times(1)).onCompleted();
    }

    @Test
    void testRequest_NonZeroValue() {
        // Given
        OnSubscribeSearch.SearchProducer<String, Geometry> searchProducer = new OnSubscribeSearch.SearchProducer<>(node, condition, subscriber);
        doNothing().when(node).searchWithoutBackpressure(any(), any());

        // When
        searchProducer.request(10);

        // Then
        verify(node, never()).searchWithoutBackpressure(any(), any());
    }

    @Test
    void testRequest_NegativeValue() {
        // Given
        OnSubscribeSearch.SearchProducer<String, Geometry> searchProducer = new OnSubscribeSearch.SearchProducer<>(node, condition, subscriber);
        doNothing().when(node).searchWithoutBackpressure(any(), any());

        // When
        searchProducer.request(-10);

        // Then
        verify(node, never()).searchWithoutBackpressure(any(), any());
    }

    @Test
    void testRequest_Overflow() {
        // Given
        OnSubscribeSearch.SearchProducer<String, Geometry> searchProducer = new OnSubscribeSearch.SearchProducer<>(node, condition, subscriber);
        doNothing().when(node).searchWithoutBackpressure(any(), any());

        // When
        searchProducer.request(Long.MAX_VALUE - 1);

        // Then
        verify(node, never()).searchWithoutBackpressure(any(), any());
    }

    @Test
    void testGetAndAddRequest_NoOverflow() {
        // Given
        AtomicLong requested = new AtomicLong(0);
        long n = 10;

        // When
        long previousCount = OnSubscribeSearch.SearchProducer.getAndAddRequest(requested, n);

        // Then
        assertEquals(0, previousCount);
        assertEquals(10, requested.get());
    }

    @Test
    void testGetAndAddRequest_Overflow() {
        // Given
        AtomicLong requested = new AtomicLong(Long.MAX_VALUE - 1);
        long n = 10;

        // When
        long previousCount = OnSubscribeSearch.SearchProducer.getAndAddRequest(requested, n);

        // Then
        assertEquals(Long.MAX_VALUE - 1, previousCount);
        assertEquals(Long.MAX_VALUE, requested.get());
    }
}