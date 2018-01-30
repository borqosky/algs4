package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {
    private final int length;
    private  int n;
    private List<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) {  // finds all line segments containing 4 points
        if (points == null)
            throw new IllegalArgumentException();

        length = points.length;
        n = 0;
        lineSegments = new ArrayList<>();

        for (int p = 0; p < length - 3; p++)
            for (int q = p + 1; q < length - 2; q++)
                for (int r = q + 1; r < length - 1; r++)
                    for (int s = r + 1; s < length; s++) {
                        if (points[s] == null || points[r] == null || points[q] == null ||
                                points[p] == null) throw new IllegalArgumentException();
                        double pqSlope = points[p].slopeTo(points[q]);
                        double prSlope = points[p].slopeTo(points[r]);
                        double psSlope = points[p].slopeTo(points[s]);

                        if (pqSlope == Double.NEGATIVE_INFINITY || prSlope == Double.NEGATIVE_INFINITY
                                || psSlope == Double.NEGATIVE_INFINITY)
                            throw new IllegalArgumentException();

                        if (pqSlope == prSlope && pqSlope == psSlope) {
                            lineSegments.add(new LineSegment(points[p], points[s]));
                            n++;
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
