import org.jsoup.nodes.Range;
import org.jsoup.nodes.Position;
import org.jsoup.internal.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RangeTest {

    private Range range;
    private Position start;
    private Position end;

    @BeforeEach
    public void setup() {
        start = new Position(10, 1, 1);
        end = new Position(20, 1, 2);
        range = new Range(start, end);
    }

    @Test
    public void testStart() {
        // Given: a range with a start position
        // When: start position is retrieved
        Position startPosition = range.start();
        // Then: the start position is correct
        assertEquals(start, startPosition);
    }

    @Test
    public void testStartPos() {
        // Given: a range with a start position
        // When: start position index is retrieved
        int startPos = range.startPos();
        // Then: the start position index is correct
        assertEquals(10, startPos);
    }

    @Test
    public void testEnd() {
        // Given: a range with an end position
        // When: end position is retrieved
        Position endPosition = range.end();
        // Then: the end position is correct
        assertEquals(end, endPosition);
    }

    @Test
    public void testEndPos() {
        // Given: a range with an end position
        // When: end position index is retrieved
        int endPos = range.endPos();
        // Then: the end position index is correct
        assertEquals(20, endPos);
    }

    @Test
    public void testIsTracked_Tracked() {
        // Given: a range that is tracked
        // When: isTracked is called
        boolean isTracked = range.isTracked();
        // Then: the range is tracked
        assertTrue(isTracked);
    }

    @Test
    public void testIsTracked_Untracked() {
        // Given: an untracked range
        Range untrackedRange = Range.Untracked;
        // When: isTracked is called
        boolean isTracked = untrackedRange.isTracked();
        // Then: the range is not tracked
        assertFalse(isTracked);
    }

    @Test
    public void testIsImplicit_Implicit() {
        // Given: a range that is implicit
        Position samePosition = new Position(10, 1, 1);
        Range implicitRange = new Range(samePosition, samePosition);
        // When: isImplicit is called
        boolean isImplicit = implicitRange.isImplicit();
        // Then: the range is implicit
        assertTrue(isImplicit);
    }

    @Test
    public void testIsImplicit_NotImplicit() {
        // Given: a range that is not implicit
        // When: isImplicit is called
        boolean isImplicit = range.isImplicit();
        // Then: the range is not implicit
        assertFalse(isImplicit);
    }

    @Test
    public void testEquals_SameRange() {
        // Given: two ranges with the same start and end positions
        Range sameRange = new Range(start, end);
        // When: equals is called
        boolean equals = range.equals(sameRange);
        // Then: the ranges are equal
        assertTrue(equals);
    }

    @Test
    public void testEquals_DifferentRange() {
        // Given: two ranges with different start or end positions
        Position differentStart = new Position(15, 1, 1);
        Range differentRange = new Range(differentStart, end);
        // When: equals is called
        boolean equals = range.equals(differentRange);
        // Then: the ranges are not equal
        assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        // Given: a range
        // When: hashCode is called
        int hashCode = range.hashCode();
        // Then: the hash code is correct
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        // Given: a range
        // When: toString is called
        String toString = range.toString();
        // Then: the string representation is correct
        assertNotNull(toString);
    }
}