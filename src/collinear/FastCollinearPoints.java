package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FastCollinearPoints {
    private  int n;
    private List<LineSegment> lineSegments;

    public FastCollinearPoints(Point[] points) {  // finds all line segments containing 4 or more points
        if (points == null)
            throw new IllegalArgumentException();

        Point[] sortedPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
            sortedPoints[i] = points[i];
        }

        lineSegments = new ArrayList<>();
        for (Point point : points) {
            int lo = 1;
            int hi = 1;
            Arrays.sort(sortedPoints, point.slopeOrder());
            double slope = Double.NEGATIVE_INFINITY;
            for (int i = 1; i < sortedPoints.length; i++) {
                Point nextPoint = sortedPoints[i];
                if (point.slopeTo(nextPoint) == Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException();
                else if (point.slopeTo(nextPoint) == slope)
                    hi++;
                else {
                    slope = point.slopeTo(sortedPoints[i]);
                    if (hi - lo >= 2) {
                        Point[] sp = Arrays.copyOfRange(sortedPoints, lo - 1, hi + 1);
                        sp[0] = point;
                        Arrays.sort(sp);
                        if (sp[0].equals(point) && sp.length > 3) {
                            lineSegments.add(new LineSegment(sp[0], sp[sp.length - 1]));
                            n++;
                        }
                    }
                    lo = hi = i;
                }
            }
        }
    }

    public int numberOfSegments() {  // the number of line segments
        return n;
    }

    public LineSegment[] segments() {  // the line segments
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
