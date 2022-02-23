/* *****************************************************************************
 *  Name:              Sanil Shah
 *  Last modified:     February 22, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final double XMIN = 0.0;
    private static final double YMIN = 0.0;
    private static final double XMAX = 1.0;
    private static final double YMAX = 1.0;

    private int size;

    private Node root;

    private class Node {
        private Point2D p;
        private RectHV rect;
        private Node left, right;

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            left = null;
            right = null;
        }
    }
    public KdTree() {             // construct an empty set of points
        size = 0;
    }

    public boolean isEmpty() {      // is the set empty?
        return size == 0;
    }

    public int size() {             // number of points in the set
        return size;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        root = insert(root, p, XMIN, YMIN, XMAX, YMAX, 0); // Level: 0
    }

    private Node insert(Node currentNode, Point2D p, double xmin, double ymin, double xmax, double ymax, int level) {
        if (currentNode == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }

        int compare = compare(p, currentNode.p, level);

        if (compare < 0) {
            if (level % 2 == 0) {
                currentNode.left = insert(currentNode.left, p, xmin, ymin, currentNode.p.x(), ymax, level + 1);
            }
            else {
                currentNode.left = insert(currentNode.left, p, xmin, ymin, xmax, currentNode.p.y(), level + 1);
            }
        }
        else if (compare > 0) {
            if (level % 2 == 0) {
                currentNode.right = insert(currentNode.right, p, currentNode.p.x(), ymin, xmax, ymax, level + 1);
            }
            else {
                currentNode.right = insert(currentNode.right, p, xmin, currentNode.p.y(), xmax, ymax, level + 1);
            }
        }
        /* System.out.println(currentNode.left);
        System.out.println(currentNode);
        System.out.println(currentNode.right);
        System.out.println("\n"); */
        return currentNode;
    }

    private int compare(Point2D p1, Point2D p2, int level) {
        // System.out.println("point: " + p1 + " nodepoint: " + p2 + " level: " + level);
        if ((level % 2) == 0) {
            // Compare x-coordinates
            int result = new Double(p1.x()).compareTo(new Double(p2.x()));
            // System.out.println(" x result: " + result);
            if (result == 0) {
                return new Double(p1.y()).compareTo(new Double(p2.y()));
            }
            else {
                return result;
            }
        }
        else {
            // Compare y-coordinates
            int result = new Double(p1.y()).compareTo(new Double(p2.y()));
            // System.out.println(" y result: " + result);
            if (result == 0) {
                return new Double(p1.x()).compareTo(new Double(p2.x()));
            }
            else {
                return result;
            }
        }
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        return (contains(root, p, 0) != null); // Level: 0
    }

    private Point2D contains(Node currentNode, Point2D p, int level) {
        while (currentNode != null) {

            int compare = compare(p, currentNode.p, level);

            if (compare < 0) {
                return contains(currentNode.left, p, level + 1);
            }
            else if (compare > 0) {
                return contains(currentNode.right, p, level + 1);
            }
            else {
                return currentNode.p;
            }
        }
        return null;
    }

    public void draw() {            // draw all points to standard draw
        StdDraw.clear();

        drawLine(root, 0);
    }

    private void drawLine(Node x, int level) {
        if (x != null) {
            drawLine(x.left, level + 1);

            StdDraw.setPenRadius();
            if (level % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            x.p.draw();

            drawLine(x.right, level + 1);
        }
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        Queue<Point2D> q = new Queue<Point2D>();

        rangeAdd(root, rect, q);

        return q;
    }

    private void rangeAdd(Node currentNode, RectHV rect, Queue<Point2D> q) {
        if (currentNode != null && rect.intersects(currentNode.rect)) {
            if (rect.contains(currentNode.p)) {
                q.enqueue(currentNode.p);
            }
            rangeAdd(currentNode.left, rect, q);
            rangeAdd(currentNode.right, rect, q);
        }
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (this.isEmpty()) {
            return null;
        }
        else {
            Point2D resultPoint = null; // Nearest Point
            resultPoint = nearest(root, p, resultPoint);

            return resultPoint;
        }
    }

    private Point2D nearest(Node currentNode, Point2D p, Point2D nearestPoint) {
        if (currentNode != null) {
            if (nearestPoint == null) {
                nearestPoint = currentNode.p;
            }

            if (nearestPoint.distanceSquaredTo(p) >= currentNode.rect.distanceSquaredTo(p)) {
                if (currentNode.p.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
                    nearestPoint = currentNode.p;
                }

                if (currentNode.right != null && currentNode.right.rect.contains(p)) {
                    nearestPoint = nearest(currentNode.right, p, nearestPoint);
                    nearestPoint = nearest(currentNode.left, p, nearestPoint);
                }
                else {
                    nearestPoint = nearest(currentNode.left, p, nearestPoint);
                    nearestPoint = nearest(currentNode.right, p, nearestPoint);
                }
            }
        }

        return nearestPoint;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        KdTree pSet = new KdTree();
        RectHV rect = new RectHV(0,0,1,1);
        Point2D p1 = new Point2D(0.7,0.2);
        Point2D p2 = new Point2D(0.5,0.4);
        Point2D p3 = new Point2D(0.2,0.3);
        Point2D p4 = new Point2D(0.4,0.7);
        Point2D p5 = new Point2D(0.9,0.6);
        pSet.insert(p1);
        pSet.insert(p2);
        pSet.insert(p3);
        pSet.insert(p4);
        pSet.insert(p5);
        pSet.draw();
        // System.out.println(pSet.isEmpty());
        // System.out.println(pSet.size());
        // System.out.println(pSet.contains(p5));
        // System.out.println(pSet.nearest(p2));
        System.out.println(pSet.range(rect));
    }
}