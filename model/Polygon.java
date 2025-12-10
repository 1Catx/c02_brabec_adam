package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private final ArrayList<Point> points;

    public Polygon() {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point p) { points.add(p); }
    public int getSize() { return points.size(); }
    public Point first() { return points.get(0); }
    public Point last()  { return points.get(points.size()-1); }
    public List<Point> getMyPoints() { return points; }
    public Point getPoint(int index) {return points.get(index);} //points.get(index) je metoda ArrayListu
    public void clear() { points.clear(); }
}
