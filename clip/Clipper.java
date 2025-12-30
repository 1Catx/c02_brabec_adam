package clip;

import model.Point;
import model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Clipper {

    public static Polygon clipConvex(Polygon subject, Polygon clipper) {
        List<Point> out = new ArrayList<>(subject.getMyPoints());
        if (out.size() < 3) return new Polygon();

        List<Point> clip = clipper.getMyPoints();
        if (clip.size() < 3) return new Polygon();

        boolean ccw = signedArea(clip) > 0; // orientace clipperu

        for (int i = 0; i < clip.size(); i++) {
            Point A = clip.get(i);
            Point B = clip.get((i + 1) % clip.size());

            List<Point> input = out;
            out = new ArrayList<>();
            if (input.isEmpty()) break;

            Point S = input.get(input.size() - 1);
            for (Point E : input) {
                boolean Ein = inside(E, A, B, ccw);
                boolean Sin = inside(S, A, B, ccw);

                if (Ein) {
                    if (!Sin) out.add(intersect(S, E, A, B));
                    out.add(E);
                } else if (Sin) {
                    out.add(intersect(S, E, A, B));
                }
                S = E;
            }
        }

        Polygon result = new Polygon();
        for (Point p : out) result.addPoint(p);
        return result;
    }

    // inside = na "vnitřní" straně hrany AB (podle orientace clipperu)
    private static boolean inside(Point P, Point A, Point B, boolean ccw) {
        double cross = cross(B.getX() - A.getX(), B.getY() - A.getY(),
                             P.getX() - A.getX(), P.getY() - A.getY());
        return ccw ? cross >= 0 : cross <= 0;
    }

    private static double cross(double ax, double ay, double bx, double by) {
        return ax * by - ay * bx;
    }

    // průsečík úsečky S->E s přímkou A->B
    private static Point intersect(Point S, Point E, Point A, Point B) {
        double sx = S.getX(), sy = S.getY();
        double ex = E.getX(), ey = E.getY();

        double ax = A.getX(), ay = A.getY();
        double bx = B.getX(), by = B.getY();

        double dx1 = ex - sx, dy1 = ey - sy;  // směr SE
        double dx2 = bx - ax, dy2 = by - ay;  // směr AB

        double denom = cross(dx1, dy1, dx2, dy2);
        if (Math.abs(denom) < 1e-12) return E; // skoro rovnoběžné

        double t = cross(ax - sx, ay - sy, dx2, dy2) / denom;
        double ix = sx + t * dx1;
        double iy = sy + t * dy1;

        return new Point((int)Math.round(ix), (int)Math.round(iy));
    }

    // kladná plocha => CCW, záporná => CW
    private static double signedArea(List<Point> pts) {
        double a = 0;
        for (int i = 0; i < pts.size(); i++) {
            Point p = pts.get(i);
            Point q = pts.get((i + 1) % pts.size());
            a += p.getX() * (double) q.getY() - q.getX() * (double) p.getY();
        }
        return a / 2.0;
    }
}
