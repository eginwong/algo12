import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> legalLineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        checkNull(points);
        checkCornerCases(points);

        for (int i = 0; i < points.length; i++) {
            Point[] clone = deepClone(points);
            // Sort according to slope order per Point as base.
            Arrays.sort(clone, clone[i].slopeOrder());

            int min = 0;
            // Will never start at zero because slope of oneself is negative infinity.
            double slope = 0.0;
            Point pMin = points[i];
            Point pMax = points[i];

            for (int pointer = 0; pointer < clone.length; pointer++) {
                double newSlope = points[i].slopeTo(clone[pointer]);
                if (newSlope != slope) {
                    if (pointer - min >= 3 && pMin == points[i]) {
                        legalLineSegments.add(new LineSegment(pMin, pMax));
                    }
                    slope = newSlope;
                    pMin = points[i].compareTo(clone[pointer]) < 0 ? points[i] : clone[pointer];
                    pMax = points[i].compareTo(clone[pointer]) > 0 ? points[i] : clone[pointer];
                    min = pointer;
                }
                else if ((pointer + 1) == clone.length) {
                    pMin = pMin.compareTo(clone[pointer]) < 0 ? pMin : clone[pointer];
                    pMax = pMax.compareTo(clone[pointer]) > 0 ? pMax : clone[pointer];
                    if (clone.length - min >= 3 && pMin == points[i]) {
                        legalLineSegments.add(new LineSegment(pMin, pMax));
                    }
                } else {
                    // what do here. Store pMin and pMax.
                    pMin = pMin.compareTo(clone[pointer]) < 0 ? pMin : clone[pointer];
                    pMax = pMax.compareTo(clone[pointer]) > 0 ? pMax : clone[pointer];
                }
            }
        }
    } // finds all line segments containing 4 or more points

    private static Point[] deepClone(Point[] array) {
        Point[] clone = new Point[array.length];
        for (int i = 0; i < array.length; i++) {
            clone[i] = array[i];
        }
        return clone;
    }

    public int numberOfSegments() {
        return legalLineSegments.size();
    } // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] slave = new LineSegment[legalLineSegments.size()];
        return legalLineSegments.toArray(slave);
    } // the line segments

    private void checkCornerCases(Point[] points) {
        // Corner cases. Throw a java.lang.IllegalArgumentException if the argument to the constructor is null, if any point in the array is null, or if the argument to the constructor contains a repeated point.
        Point[] clone = deepClone(points);
        Arrays.sort(clone);
        if (clone.length > 1) {
            for (int i = 1; i < clone.length; i++) {
                if (clone[i] != null && clone[i - 1] != null) {
                    if (clone[i].compareTo(clone[i - 1]) == 0) {
                        throw new IllegalArgumentException("identical points");
                    }
                } else {
                    throw new IllegalArgumentException("null point");
                }
            }
        }
    }

    private void checkNull(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("null constructor");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("null point");
            }
        }
    }
}