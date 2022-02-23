/* *****************************************************************************
 *  Name:              Sanil Shah
 *  Last modified:     February 22, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet;

    public PointSET() {             // construct an empty set of points
        pointSet = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {      // is the set empty?
        return pointSet.isEmpty();
    }

    public int size() {             // number of points in the set
        return pointSet.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException("Argument is Null.");
        pointSet.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        return pointSet.contains(p);
    }

    public void draw() {            // draw all points to standard draw
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D point : pointSet) {
            if (rect.contains(point))
                q.enqueue(point);
        }
        return q;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (pointSet.isEmpty())
            return null;
        Point2D nearestPoint = null;
        for (Point2D point : pointSet) {
            if (nearestPoint == null || (p.distanceSquaredTo(point) < p.distanceSquaredTo(nearestPoint) && !point.equals(p))) {
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        //PointSET pSet = new PointSET();
        // RectHV rect = new RectHV(0,0,1,1);
        // Point2D p1 = new Point2D(0.1,0.1);
        // Point2D p2 = new Point2D(0.5,0.5);
        // Point2D p3 = new Point2D(0.7,0.8);
        // Point2D p4 = new Point2D(0.6,0.4);
        // Point2D p5 = new Point2D(0.2,0.9);
        // pSet.insert(p1);
        // pSet.insert(p2);
        // pSet.insert(p3);
        // pSet.insert(p4);
        //
        // pSet.draw();
        // System.out.println(pSet.isEmpty());
        // System.out.println(pSet.size());
        // System.out.println(pSet.contains(p5));
        // System.out.println(pSet.nearest(p2));
        // System.out.println(pSet.range(rect));
    }
}