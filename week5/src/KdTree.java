import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class KdTree {
    private Node root;

    public KdTree() {
    } // construct an empty set of points

    public boolean isEmpty() {
        return size() == 0;
    } // is the set empty?

    public int size() {
        return size(root);
    } // number of points in the set

    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.size;
    }

    public void insert(Point2D p) {
        if (!contains(p)) {
            root = insert(root, p, true, new RectHV(0, 0, 1, 1));
        }
    } // add the point to the set (if it is not already in the set)

    // compare on x if orientation is true, y if orientation is false
    private Node insert(final Node x, final Point2D p, final boolean orientation, final RectHV rect) {
        if (x == null)
            return new Node(p, rect, null, null, 1);

        // determine which side the rectangle should be on.
        RectHV newRect = null;

        // reduce the number of calls
        double xCoordP = p.x();
        double yCoordP = p.y();
        double xCoord = x.p.x();
        double yCoord = x.p.y();
        // if true, go left or bottom.
        boolean cmp = x.compareTo(xCoordP, yCoordP, xCoord, yCoord, orientation);

        if (cmp && x.lb == null) {
            if (orientation) {
                // it will be left of the point, so x left to the point.
                newRect = new RectHV(x.rect.xmin(), x.rect.ymin(), xCoord, x.rect.ymax());
            } else {
                // it will be bottom of the point, so y up to the point.
                newRect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), yCoord);
            }
        } else if (!cmp && x.rt == null) {
            if (orientation) {
                // it will be right of the point, so x right to the point.
                newRect = new RectHV(xCoord, x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
            } else {
                // it will be top of the point, so y down to the point.
                newRect = new RectHV(x.rect.xmin(), yCoord, x.rect.xmax(), x.rect.ymax());
            }
        }

        if (cmp)
            x.lb = insert(x.lb, p, !orientation, newRect);
        else
            x.rt = insert(x.rt, p, !orientation, newRect);
        x.size = 1 + size(x.lb) + size(x.rt);
        return x;
    }

    public boolean contains(Point2D p) {
        checkInput(p);
        return get(root, p, true) != null;
    } // does the set contain point p?

    private Point2D get(Node x, Point2D p, boolean orientation) {
        if (x == null)
            return null;
        if (p.equals(x.p)) return x.p;
        if (orientation) {
            // left
            if (p.x() < (x.p.x()))
                return get(x.lb, p, !orientation);
            // right
            else if (p.x() >= (x.p.x()))
                return get(x.rt, p, !orientation);
        } else {
            if (p.y() < (x.p.y()))
                return get(x.lb, p, !orientation);
            else if (p.y() >= (x.p.y()))
                return get(x.rt, p, !orientation);
        }
        return null;
    }

    public void draw() {
        draw(root, true);
    } // draw all points to standard draw

    private void draw(Node node, boolean orientation) {
        if (node == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        StdDraw.setPenRadius();
        if (orientation) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        draw(node.lb, !orientation);
        draw(node.rt, !orientation);
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkInput(rect);
        Stack<Point2D> output = new Stack<>();
        range(root, rect, output);
        return output;
    } // all points that are inside the rectangle (or on the boundary)

    private Stack<Point2D> range(Node node, RectHV rect, Stack<Point2D> output) {
        if (node == null) {
            return output;
        }
        if (node.rect.intersects(rect)) {
            // only check if the rectangles intersect
            if (rect.contains(node.p)) {
                output.push(node.p);
            }
            range(node.lb, rect, output);
            range(node.rt, rect, output);
        }

        return output;
    }

    public Point2D nearest(Point2D p) {
        checkInput(p);
        // reset values
        // put nearest distance here
        if (root != null) {
            return nearest(root, root.p, p, true);
        } else {
            return null;
        }
    } // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D nearest(Node node, Point2D champion, Point2D p, boolean orientation) {
        if (node == null) {
            return champion;
        }

        // check node if point is closer to rectangle
        if (node.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
            // replace champion if new node point is closer only if rectangles intersect
            if (p.distanceSquaredTo(node.p) < p.distanceSquaredTo(champion)) {
                champion = node.p;
            }
            // if this node is bigger, go left
            if (node.compareTo(p, orientation)) {
                champion = nearest(node.lb, champion, p, !orientation);
                champion = nearest(node.rt, champion, p, !orientation);
            } else {
                champion = nearest(node.rt, champion, p, !orientation);
                champion = nearest(node.lb, champion, p, !orientation);
            }
        }

        return champion;
    }

    private void checkInput(Object input) {
        if (input == null) {
            throw new IllegalArgumentException("bad input");
        }
    }

    private static class Node {
        private final Point2D p; // the point
        private final RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private int size;

        public Node(Point2D p, RectHV rect, Node lb, Node rt, int size) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.size = size;
        }

        public boolean compareTo(Point2D b, boolean orientation) {
            if (orientation) {
                if (b.x() < (this.p.x()))
                    return true;
                return false;
            } else {
                if (b.y() < (this.p.y()))
                    return true;
                return false;
            }
        }

        public boolean compareTo(double x, double y, double nodeX, double nodeY, boolean orientation) {
            if (orientation) {
                if (x < nodeX)
                    return true;
                return false;
            } else {
                if (y < nodeY)
                    return true;
                return false;
            }
        }
    }

    public static void main(String[] args) {
        //    Test things
        KdTree test1 = new KdTree();
        assert (test1.isEmpty());
        Point2D testPoint = new Point2D(0.7, 0.2);
        assert (test1.size() == 0);
        assert (!test1.contains(testPoint));
        assert (test1.nearest(testPoint) == null);
        test1.insert(testPoint);
        assert (test1.size() == 1);
        assert (test1.contains(testPoint));
        Point2D perfectPoint = new Point2D(0.9, 0.8);
        assert (test1.nearest(perfectPoint) == testPoint);
        assert (test1.nearest(testPoint) == testPoint);
        test1.insert(perfectPoint);
        Point2D projectedPoint = new Point2D(0.9, 0.1);
        assert (test1.nearest(projectedPoint) == testPoint);
        assert (test1.size() == 2);
        Point2D maxPoint = new Point2D(1, 1);
        test1.insert(maxPoint);
        test1.insert(maxPoint);
        test1.insert(maxPoint);
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

        Point2D p1 = new Point2D(0.75, 0.25);
        Point2D p2 = new Point2D(0, 0.25);
        Point2D p3 = new Point2D(0.5, 1);
        Point2D p4 = new Point2D(0.5, 0);
        Point2D p5 = new Point2D(0, 1);
        Point2D p6 = new Point2D(0.75, 0.75);
        Point2D p7 = new Point2D(0.75, 0.75);
        KdTree test2 = new KdTree();
        test2.insert(p1);
        test2.insert(p2);
        test2.insert(p3);
        test2.insert(p4);
        test2.insert(p5);
        test2.insert(p6);
        test2.insert(p7);
        assert (test2.size() == 6);


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

// it should support nearest() and range() in time proportional to the number of points in the set.