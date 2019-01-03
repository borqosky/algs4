package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private int size;
    private Node root;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!contains(p)) {
            root = insert(root, p, VERTICAL, new RectHV(0, 0, 1, 1));
            size++;
        }
    }

    private Node insert(Node x, Point2D p, boolean orientation, RectHV rectHV) {
        if (x == null)
            return new Node(p, rectHV);

        if (orientation == VERTICAL) {
            double cmpX = p.x() - x.p.x();
            if (cmpX < 0) {
                x.lb = insert(x.lb, p, HORIZONTAL, new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax()));
            } else {
                x.rt = insert(x.rt, p, HORIZONTAL, new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax()));
            }
        } else if (orientation == HORIZONTAL) {
            double cmpY = p.y() - x.p.y();
            if (cmpY < 0) {
                x.lb = insert(x.lb, p, VERTICAL, new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y()));
            } else {
                x.rt = insert(x.rt, p, VERTICAL, new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax()));
            }
        }
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return contains(root, p, VERTICAL);

    }

    private boolean contains(Node x, Point2D p, boolean orientation) {
        if (x == null) return false;
        if (!x.rect.contains(p)) return false;
        if (x.p.equals(p)) return true;

        double cmp;
        if (orientation == VERTICAL)
            cmp = p.x() - x.p.x();
        else
            cmp = p.y() - x.p.y();

        if (cmp < 0)
            return contains(x.lb, p, !orientation);
        else
            return contains(x.rt, p, !orientation);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, VERTICAL);
    }

    private void draw(Node x, boolean orientation) {
        if (orientation == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }

        if (x.lb != null)
            draw(x.lb, !orientation);
        if (x.rt != null)
            draw(x.rt, !orientation);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x.p.x(), x.p.y());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        return range(root, rect, new Queue<>());
    }

    private Iterable<Point2D> range(Node x, RectHV rect, Queue<Point2D> ds) {
        if (x == null || !x.rect.intersects(rect))
            return ds;
        else if (rect.contains(x.p))
            ds.enqueue(x.p);

        range(x.lb, rect, ds);
        range(x.rt, rect, ds);

        return ds;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return nearest(root, p, Double.POSITIVE_INFINITY, null);
    }

    private Point2D nearest(Node x, Point2D p, double distance, Point2D nearest) {
        if (x == null)
            return nearest;
        if (x.rect.distanceSquaredTo(p) >= distance)
            return nearest;

        double d = x.p.distanceSquaredTo(p);
        Point2D n;
        if (d < distance) {
            distance = d;
            n = x.p;
        } else {
            n = nearest;
        }

        Node firstNode = x.lb;
        Node secondNode = x.rt;
        if (firstNode != null && secondNode != null &&
                firstNode.rect.distanceSquaredTo(p) > secondNode.rect.distanceSquaredTo(p)) {
            firstNode = x.rt;
            secondNode = x.lb;
        }

        Point2D firstPoint = nearest(firstNode, p, distance, n);
        double firstDistance = p.distanceSquaredTo(firstPoint);
        if (firstDistance < distance) {
            n = firstPoint;
            distance = firstDistance;
        }

        Point2D secondPoint = nearest(secondNode, p, distance, n);
        double secondDistance = p.distanceSquaredTo(secondPoint);
        if (secondDistance < distance) {
            n = secondPoint;
            distance = secondDistance;
        }

        return n;
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3)); // correct
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        // StdOut.println("Find existing point: " + tree.contains(new Point2D(0.25, 0.25)));
        StdOut.println("Find non-existant p: " + tree.nearest(new Point2D(0.79, 0.896)));
        // StdOut.println("Count of nodes = : " + tree.size());
    }
}