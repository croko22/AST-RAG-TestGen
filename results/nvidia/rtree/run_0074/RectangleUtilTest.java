import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.github.davidmoten.rtree.internal.RectangleUtil;

public class RectangleUtilTest {

    @Test
    public void testRectangleIntersectsLine_RectangleIntersectsLine() {
        // Given
        double rectX = 0;
        double rectY = 0;
        double rectWidth = 10;
        double rectHeight = 10;
        double x1 = 5;
        double y1 = 0;
        double x2 = 5;
        double y2 = 10;

        // When
        boolean result = RectangleUtil.rectangleIntersectsLine(rectX, rectY, rectWidth, rectHeight, x1, y1, x2, y2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRectangleIntersectsLine_RectangleDoesNotIntersectLine() {
        // Given
        double rectX = 0;
        double rectY = 0;
        double rectWidth = 10;
        double rectHeight = 10;
        double x1 = 15;
        double y1 = 15;
        double x2 = 20;
        double y2 = 20;

        // When
        boolean result = RectangleUtil.rectangleIntersectsLine(rectX, rectY, rectWidth, rectHeight, x1, y1, x2, y2);

        // Then
        assertFalse(result);
    }

    @Test
    public void testRectangleIntersectsLine_LineIntersectsRectangleCorner() {
        // Given
        double rectX = 0;
        double rectY = 0;
        double rectWidth = 10;
        double rectHeight = 10;
        double x1 = 10;
        double y1 = 0;
        double x2 = 10;
        double y2 = 10;

        // When
        boolean result = RectangleUtil.rectangleIntersectsLine(rectX, rectY, rectWidth, rectHeight, x1, y1, x2, y2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRectangleIntersectsLine_LineIsParallelToRectangleSide() {
        // Given
        double rectX = 0;
        double rectY = 0;
        double rectWidth = 10;
        double rectHeight = 10;
        double x1 = 0;
        double y1 = 15;
        double x2 = 10;
        double y2 = 15;

        // When
        boolean result = RectangleUtil.rectangleIntersectsLine(rectX, rectY, rectWidth, rectHeight, x1, y1, x2, y2);

        // Then
        assertFalse(result);
    }

    @Test
    public void testRectangleIntersectsLine_LineIsVerticalAndIntersectsRectangle() {
        // Given
        double rectX = 0;
        double rectY = 0;
        double rectWidth = 10;
        double rectHeight = 10;
        double x1 = 5;
        double y1 = -10;
        double x2 = 5;
        double y2 = 20;

        // When
        boolean result = RectangleUtil.rectangleIntersectsLine(rectX, rectY, rectWidth, rectHeight, x1, y1, x2, y2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRectangleIntersectsLine_LineIsHorizontalAndIntersectsRectangle() {
        // Given
        double rectX = 0;
        double rectY = 0;
        double rectWidth = 10;
        double rectHeight = 10;
        double x1 = -10;
        double y1 = 5;
        double x2 = 20;
        double y2 = 5;

        // When
        boolean result = RectangleUtil.rectangleIntersectsLine(rectX, rectY, rectWidth, rectHeight, x1, y1, x2, y2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRectangleIntersectsLine_RectangleHasZeroWidth() {
        // Given
        double rectX = 0;
        double rectY = 0;
        double rectWidth = 0;
        double rectHeight = 10;
        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 10;

        // When
        boolean result = RectangleUtil.rectangleIntersectsLine(rectX, rectY, rectWidth, rectHeight, x1, y1, x2, y2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRectangleIntersectsLine_RectangleHasZeroHeight() {
        // Given
        double rectX = 0;
        double rectY = 0;
        double rectWidth = 10;
        double rectHeight = 0;
        double x1 = 0;
        double y1 = 0;
        double x2 = 10;
        double y2 = 0;

        // When
        boolean result = RectangleUtil.rectangleIntersectsLine(rectX, rectY, rectWidth, rectHeight, x1, y1, x2, y2);

        // Then
        assertTrue(result);
    }
}