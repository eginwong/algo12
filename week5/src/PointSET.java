import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;
import java.util.Stack;

public class PointSET {
    private final SET<Point2D> unitSquareSet;

    public PointSET() {
        unitSquareSet = new SET<Point2D>();
    } // construct an empty set of points 

    public boolean isEmpty() {
        return unitSquareSet.size() <= 0;
    } // is the set empty? 

    public int size() {
        return unitSquareSet.size();
    } // number of points in the set 

    public void insert(Point2D p) {
        checkInput(p);
        unitSquareSet.add(p);
    } // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        checkInput(p);
        return unitSquareSet.contains(p);
    } // does the set contain point p? 

    public void draw() {
        if (!isEmpty()) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            for (Point2D pt : unitSquareSet) {
                pt.draw();
            }
        }
    } // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        checkInput(rect);
        Stack<Point2D> output = new Stack<>();
        Iterator<Point2D> points = unitSquareSet.iterator();
        while (points.hasNext()) {
            Point2D nextPoint = points.next();
            if (rect.contains(nextPoint)) {
                output.push(nextPoint);
            }
        }
        return output;
    } // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        checkInput(p);
        Point2D nearestPoint = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        Iterator<Point2D> points = unitSquareSet.iterator();

        while (points.hasNext()) {
            Point2D nextPoint = points.next();
            double nextDistance = nextPoint.distanceSquaredTo(p);
            if (nextDistance < nearestDistance) {
                nearestDistance = nextDistance;
                nearestPoint = nextPoint;
            }
        }
        return nearestPoint;
    } // a nearest neighbor in the set to point p; null if the set is empty

    private void checkInput(Object input) {
        if (input == null) {
            throw new IllegalArgumentException("bad input");
        }
    }

    public static void main(String[] args) {
        PointSET test1 = new PointSET();
        assert (test1.isEmpty());
        Point2D testPoint = new Point2D(0.1, 0.1);
        assert (test1.size() == 0);
        assert (!test1.contains(testPoint));
        assert (test1.nearest(testPoint) == null);
        test1.insert(testPoint);
        assert (!test1.isEmpty());
        assert (test1.size() == 1);
        assert (test1.contains(testPoint));
        Point2D perfectPoint = new Point2D(0.9, 0.8);
        assert (test1.nearest(perfectPoint) == testPoint);
        assert (test1.nearest(testPoint) == testPoint);
        test1.insert(perfectPoint);
        Point2D projectedPoint = new Point2D(0.9, 0.1);
        assert (test1.nearest(projectedPoint) == perfectPoint);
        assert (test1.size() == 2);
        Point2D maxPoint = new Point2D(1, 1);
        test1.insert(maxPoint);

        RectHV testRect = new RectHV(0.1, 0.1, 0.9, 0.9);
        int expectedPointsInRange = 2;
        int actualPointsInRange = 0;
        Iterator<Point2D> rangeTestIterator = test1.range(testRect).iterator();
        while (rangeTestIterator.hasNext()) {
            rangeTestIterator.next();
            actualPointsInRange++;
        }
        assert (expectedPointsInRange == actualPointsInRange);

        // DRAWING TESTS
        Point2D testPoint2 = new Point2D(0.5, 0.4);
        Point2D testPoint3 = new Point2D(0.2, 0.3);
        Point2D testPoint4 = new Point2D(0.4, 0.7);
        Point2D testPoint5 = new Point2D(0.9, 0.6);
        test1.insert(testPoint2);
        test1.insert(testPoint3);
        test1.insert(testPoint4);
        test1.insert(testPoint5);
        test1.draw();
        // Exception Tests
    } // unit testing of the methods (optional)
}
