package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;


public class PointSET {
    private final SET<Point2D> point2DS;
    // construct an empty set of points
    public PointSET() {
        point2DS = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return point2DS.isEmpty();
    }

    // number of points in the set
    public int size() {
        return point2DS.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!contains(p))
            point2DS.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return point2DS.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.01);
        for (Point2D point2D : point2DS) {
            point2D.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> points = new ArrayList<>();
        for (Point2D point2D : point2DS) {
            if (rect.contains(point2D))
                points.add(point2D);
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        double distance = Double.POSITIVE_INFINITY;
        Point2D point = null;
        if (p == null)
            throw new IllegalArgumentException();
        for (Point2D point2D : point2DS) {
            double d = point2D.distanceSquaredTo(p);
            if (d < distance) {
                distance = d;
                point = point2D;
            }
        }
        return point;
    }

    public static void main(String[] args) {
        PointSET set = new PointSET();
        Point2D p1 = new Point2D(0.0, 0.0);
        Point2D p2 = new Point2D(0.0, 1.0);
        set.insert(p1);
        StdOut.print(set.nearest(p2));
    }
}
