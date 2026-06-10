import com.github.davidmoten.rtree.internal.util.ImmutableStack;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ImmutableStackTest {

    @Test
    public void testCreate() {
        // Given: un elemento para crear la pila
        String elemento = "elemento";
        // When: se crea la pila
        ImmutableStack<String> pila = ImmutableStack.create(elemento);
        // Then: se verifica que la pila no esté vacía y que el elemento sea el esperado
        assertFalse(pila.isEmpty());
        assertEquals(elemento, pila.peek());
    }

    @Test
    public void testEmpty() {
        // Given: ninguna condición previa
        // When: se crea una pila vacía
        ImmutableStack<String> pila = ImmutableStack.empty();
        // Then: se verifica que la pila esté vacía
        assertTrue(pila.isEmpty());
    }

    @Test
    public void testIsEmpty_NoElement() {
        // Given: una pila vacía
        ImmutableStack<String> pila = ImmutableStack.empty();
        // When: se verifica si la pila está vacía
        // Then: se verifica que la pila esté vacía
        assertTrue(pila.isEmpty());
    }

    @Test
    public void testIsEmpty_WithElement() {
        // Given: una pila con un elemento
        ImmutableStack<String> pila = ImmutableStack.create("elemento");
        // When: se verifica si la pila está vacía
        // Then: se verifica que la pila no esté vacía
        assertFalse(pila.isEmpty());
    }

    @Test
    public void testPeek() {
        // Given: una pila con un elemento
        String elemento = "elemento";
        ImmutableStack<String> pila = ImmutableStack.create(elemento);
        // When: se obtiene el elemento superior de la pila
        String resultado = pila.peek();
        // Then: se verifica que el elemento sea el esperado
        assertEquals(elemento, resultado);
    }

    @Test
    public void testPeek_EmptyStack() {
        // Given: una pila vacía
        ImmutableStack<String> pila = ImmutableStack.empty();
        // When y Then: se espera una excepción al intentar obtener el elemento superior
        assertThrows(NoSuchElementException.class, pila::peek);
    }

    @Test
    public void testPop() {
        // Given: una pila con un elemento
        String elemento = "elemento";
        ImmutableStack<String> pila = ImmutableStack.create(elemento);
        // When: se elimina el elemento superior de la pila
        ImmutableStack<String> pilaNueva = pila.pop();
        // Then: se verifica que la nueva pila esté vacía
        assertTrue(pilaNueva.isEmpty());
    }

    @Test
    public void testPop_EmptyStack() {
        // Given: una pila vacía
        ImmutableStack<String> pila = ImmutableStack.empty();
        // When y Then: se espera una excepción al intentar eliminar el elemento superior
        assertThrows(NoSuchElementException.class, pila::pop);
    }

    @Test
    public void testPush() {
        // Given: una pila vacía y un elemento para agregar
        String elemento = "elemento";
        ImmutableStack<String> pila = ImmutableStack.empty();
        // When: se agrega el elemento a la pila
        ImmutableStack<String> pilaNueva = pila.push(elemento);
        // Then: se verifica que la nueva pila no esté vacía y que el elemento sea el esperado
        assertFalse(pilaNueva.isEmpty());
        assertEquals(elemento, pilaNueva.peek());
    }

    @Test
    public void testIterator() {
        // Given: una pila con elementos
        String elemento1 = "elemento1";
        String elemento2 = "elemento2";
        ImmutableStack<String> pila = ImmutableStack.create(elemento1);
        pila = pila.push(elemento2);
        // When: se crea un iterador para la pila
        Iterator<String> iterador = pila.iterator();
        // Then: se verifica que el iterador devuelva los elementos en el orden correcto
        assertTrue(iterador.hasNext());
        assertEquals(elemento2, iterador.next());
        assertTrue(iterador.hasNext());
        assertEquals(elemento1, iterador.next());
        assertFalse(iterador.hasNext());
    }

    @Test
    public void testIterator_EmptyStack() {
        // Given: una pila vacía
        ImmutableStack<String> pila = ImmutableStack.empty();
        // When: se crea un iterador para la pila
        Iterator<String> iterador = pila.iterator();
        // Then: se verifica que el iterador no tenga elementos
        assertFalse(iterador.hasNext());
    }

    @Test
    public void testRemove() {
        // Given: una pila con elementos y un iterador
        String elemento = "elemento";
        ImmutableStack<String> pila = ImmutableStack.create(elemento);
        Iterator<String> iterador = pila.iterator();
        // When y Then: se espera una excepción al intentar eliminar un elemento con el iterador
        assertThrows(RuntimeException.class, iterador::remove);
    }
}