import com.github.davidmoten.rtree.internal.Line2D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Line2DTest {

    @Test
    public void testGetX1() {
        Line2D line = new Line2D(1.0, 2.0, 3.0, 4.0);
        assertEquals(1.0, line.getX1());
    }

    @Test
    public void testGetX2() {
        Line2D line = new Line2D(1.0, 2.0, 3.0, 4.0);
        assertEquals(3.0, line.getX2());
    }

    @Test
    public void testGetY1() {
        Line2D line = new Line2D(1.0, 2.0, 3.0, 4.0);
        assertEquals(2.0, line.getY1());
    }

    @Test
    public void testGetY2() {
        Line2D line = new Line2D(1.0, 2.0, 3.0, 4.0);
        assertEquals(4.0, line.getY2());
    }

    @Test
    public void testPtSegDist() {
        Line2D line = new Line2D(1.0, 2.0, 3.0, 4.0);
        double distance = line.ptSegDist(2.0, 3.0);
        assertTrue(distance >= 0.0);
    }

    @Test
    public void testPtSegDistStatic() {
        double distance = Line2D.ptSegDist(1.0, 2.0, 3.0, 4.0, 2.0, 3.0);
        assertTrue(distance >= 0.0);
    }

    @Test
    public void testPtSegDistSq() {
        double distanceSq = Line2D.ptSegDistSq(1.0, 2.0, 3.0, 4.0, 2.0, 3.0);
        assertTrue(distanceSq >= 0.0);
    }

    @Test
    public void testIntersectsLine() {
        Line2D line1 = new Line2D(1.0, 2.0, 3.0, 4.0);
        Line2D line2 = new Line2D(2.0, 3.0, 4.0, 5.0);
        boolean intersects = line1.intersectsLine(line2);
        assertTrue(intersects);
    }

    @Test
    public void testLinesIntersect() {
        boolean intersects = Line2D.linesIntersect(1.0, 2.0, 3.0, 4.0, 2.0, 3.0, 4.0, 5.0);
        assertTrue(intersects);
    }

    @Test
    public void testPtSegDist_OnLine() {
        Line2D line = new Line2D(1.0, 2.0, 3.0, 4.0);
        double distance = line.ptSegDist(2.0, 3.0);
        assertEquals(0.0, distance, 1e-6);
    }

    @Test
    public void testPtSegDist_OnLineSegment() {
        Line2D line = new Line2D(1.0, 2.0, 3.0, 4.0);
        double distance = line.ptSegDist(2.5, 3.5);
        assertEquals(0.0, distance, 1e-6);
    }

    @Test
    public void testPtSegDist_BeyondLineSegment() {
        Line2D line = new Line2D(1.0, 2.0, 3.0, 4.0);
        double distance = line.ptSegDist(4.0, 5.0);
        assertTrue(distance > 0.0);
    }

    @Test
    public void testLinesIntersect_Perpendicular() {
        boolean intersects = Line2D.linesIntersect(0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0);
        assertTrue(intersects);
    }

    @Test
    public void testLinesIntersect_Parallel() {
        boolean intersects = Line2D.linesIntersect(0.0, 0.0, 1.0, 1.0, 2.0, 2.0, 3.0, 3.0);
        assertFalse(intersects);
    }

    @Test
    public void testLinesIntersect_Coincident() {
        boolean intersects = Line2D.linesIntersect(0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0);
        assertTrue(intersects);
    }
}