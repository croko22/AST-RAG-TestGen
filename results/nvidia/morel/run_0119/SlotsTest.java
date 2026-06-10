import net.hydromatic.morel.eval.Code;
import net.hydromatic.morel.eval.Slots;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SlotsTest {

    @Mock
    private Code code1;

    @Mock
    private Code code2;

    @Mock
    private Code code3;

    @BeforeEach
    void setup() {
        when(code1.maxSlots()).thenReturn(5);
        when(code2.maxSlots()).thenReturn(10);
        when(code3.maxSlots()).thenReturn(3);
    }

    @Test
    public void testMaxOf_NoCodes() {
        // Given: no codes
        // When: maxOf is called with no codes
        int result = Slots.maxOf();
        // Then: result should be 0
        assertEquals(0, result);
    }

    @Test
    public void testMaxOf_SingleCode() {
        // Given: a single code with max slots 5
        // When: maxOf is called with the single code
        int result = Slots.maxOf(code1);
        // Then: result should be 5
        assertEquals(5, result);
    }

    @Test
    public void testMaxOf_MultipleCodes() {
        // Given: multiple codes with max slots 5, 10, and 3
        // When: maxOf is called with the multiple codes
        int result = Slots.maxOf(code1, code2, code3);
        // Then: result should be 10
        assertEquals(10, result);
    }

    @Test
    public void testMaxOf_CollectionOfCodes() {
        // Given: a collection of codes with max slots 5, 10, and 3
        Collection<Code> codes = new ArrayList<>();
        codes.add(code1);
        codes.add(code2);
        codes.add(code3);
        // When: maxOf is called with the collection of codes
        int result = Slots.maxOf(codes);
        // Then: result should be 10
        assertEquals(10, result);
    }

    @Test
    public void testMaxOf_BaseAndCollectionOfCodes() {
        // Given: a base of 8 and a collection of codes with max slots 5, 10, and 3
        Collection<Code> codes = new ArrayList<>();
        codes.add(code1);
        codes.add(code2);
        codes.add(code3);
        // When: maxOf is called with the base and collection of codes
        int result = Slots.maxOf(8, codes);
        // Then: result should be 10
        assertEquals(10, result);
    }

    @Test
    public void testMaxOf_BaseAndNoCodes() {
        // Given: a base of 8 and no codes
        // When: maxOf is called with the base and no codes
        int result = Slots.maxOf(8, new ArrayList<>());
        // Then: result should be 8
        assertEquals(8, result);
    }

    @Test
    public void testMaxOf_BaseAndSingleCode() {
        // Given: a base of 8 and a single code with max slots 5
        // When: maxOf is called with the base and single code
        int result = Slots.maxOf(8, java.util.Collections.singleton(code1));
        // Then: result should be 8
        assertEquals(8, result);
    }

    @Test
    public void testMaxOf_BaseAndMultipleCodes() {
        // Given: a base of 8 and multiple codes with max slots 5, 10, and 3
        Collection<Code> codes = new ArrayList<>();
        codes.add(code1);
        codes.add(code2);
        codes.add(code3);
        // When: maxOf is called with the base and multiple codes
        int result = Slots.maxOf(8, codes);
        // Then: result should be 10
        assertEquals(10, result);
    }
}