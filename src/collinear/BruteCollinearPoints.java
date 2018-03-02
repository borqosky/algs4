package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final int length;
    private  int n;
    private List<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) {  // finds all line segments containing 4 points
        if (points == null)
            throw new IllegalArgumentException();

        Point[] sortedPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
            sortedPoints[i] = points[i];
        }
        Arrays.sort(sortedPoints);

        length = sortedPoints.length;
        n = 0;
        lineSegments = new ArrayList<>();

        for (int p = 0; p < length; p++)
            for (int q = p + 1; q < length; q++) {
                if (sortedPoints[p].compareTo(sortedPoints[q]) == 0)
                    throw new IllegalArgumentException();
                for (int r = q + 1; r < length; r++)
                    for (int s = r + 1; s < length; s++) {
                        double pqSlope = sortedPoints[p].slopeTo(sortedPoints[q]);
                        double prSlope = sortedPoints[p].slopeTo(sortedPoints[r]);
                        double psSlope = sortedPoints[p].slopeTo(sortedPoints[s]);

                        if (pqSlope == Double.NEGATIVE_INFINITY || prSlope == Double.NEGATIVE_INFINITY
                                || psSlope == Double.NEGATIVE_INFINITY)
                            throw new IllegalArgumentException();

                        if (pqSlope == prSlope && pqSlope == psSlope) {
                            lineSegments.add(new LineSegment(sortedPoints[p], sortedPoints[s]));
                            n++;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
