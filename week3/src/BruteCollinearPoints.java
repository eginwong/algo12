import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> legalLineSegments;

    public BruteCollinearPoints(Point[] points) {
        checkNull(points);
        checkCornerCases(points);
        Point[] clone = deepClone(points);
        Arrays.sort(clone);

        legalLineSegments = new ArrayList<>();
        int totalLength = clone.length;
        for (int i = 0; i < totalLength; i++) {
            for (int j = i + 1; j < totalLength; j++) {
                for (int k = j + 1; k < totalLength; k++) {
                    final double idealSlope = clone[i].slopeTo(clone[j]);
                    if (idealSlope == clone[i].slopeTo(clone[k])) {
                        for (int m = k + 1; m < totalLength; m++) {
                            if (idealSlope == clone[i].slopeTo(clone[m])) {
                                legalLineSegments.add(new LineSegment(clone[i], clone[m]));
                            }
                        }
                    }

                }
            }
        }

    } // finds all line segments containing 4 points

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

    private static Point[] deepClone(Point[] array) {
        Point[] clone = new Point[array.length];
        for (int i = 0; i < array.length; i++) {
            clone[i] = array[i];
        }
        return clone;
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

    public int numberOfSegments() {
        return legalLineSegments.size();
    } // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] slave = new LineSegment[legalLineSegments.size()];
        return legalLineSegments.toArray(slave);
    } // the line segments
}