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

        n = 0;
        lineSegments = new ArrayList<>();

        Arrays.sort(points, points[0].slopeOrder());

        int c = 1;
        for (int p = 0; p < points.length - 3; p++) {
            while (points[p] == points[c])
                c++;
            if (c >= 3)
                lineSegments.add(new LineSegment(points[p], points[c]));
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
