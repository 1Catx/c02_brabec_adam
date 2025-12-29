package model;

public class RectanglePolygon extends Polygon {

    // Přepočítá 4 vrcholy obdélníka z bodů A,B (základna) a C (určuje výšku)
    public void recompute(Point a, Point b, Point c) {
        clear();

        double ax = a.getX(), ay = a.getY();
        double bx = b.getX(), by = b.getY();
        double cx = c.getX(), cy = c.getY();

        double dx = bx - ax;
        double dy = by - ay;

        double len = Math.hypot(dx, dy);

        double nx = -dy / len;
        double ny =  dx / len;

        double hx = cx - ax;
        double hy = cy - ay;
        double h = hx * nx + hy * ny; // skalární součin

        double vx = nx * h;
        double vy = ny * h;

        // A, B, B+v, A+v
        Point p0 = new Point((int)Math.round(ax), (int)Math.round(ay));
        Point p1 = new Point((int)Math.round(bx), (int)Math.round(by));
        Point p2 = new Point((int)Math.round(bx + vx), (int)Math.round(by + vy));
        Point p3 = new Point((int)Math.round(ax + vx), (int)Math.round(ay + vy));

        addPoint(p0);
        addPoint(p1);
        addPoint(p2);
        addPoint(p3);
    }
}
