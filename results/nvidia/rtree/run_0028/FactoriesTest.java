package com.github.davidmoten.rtree;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.internal.FactoryDefault;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FactoriesTest {

    @Test
    public void testDefaultFactory() {
        // When: se ejecuta el metodo defaultFactory
        Factory<Object, Geometry> factory = Factories.defaultFactory();

        // Then: se verifica el resultado
        assertNotNull(factory);
        assertSame(FactoryDefault.instance(), factory);
    }

    @Test
    public void testDefaultFactory_MultipleCalls() {
        // When: se ejecuta el metodo defaultFactory multiple veces
        Factory<Object, Geometry> factory1 = Factories.defaultFactory();
        Factory<Object, Geometry> factory2 = Factories.defaultFactory();

        // Then: se verifica el resultado
        assertNotNull(factory1);
        assertNotNull(factory2);
        assertSame(factory1, factory2);
    }
}