package fill;

import model.Point;
import model.Polygon;
import rasterize.Raster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLine {
    public static void fillPolygon(Raster raster, Polygon poly, int fillColor) {
        List<Point> pts = poly.getMyPoints();
        if (pts.size() < 3) return;

        int yMin = pts.get(0).getY();
        int yMax = pts.get(0).getY();
        for (Point p : pts) {
            yMin = Math.min(yMin, p.getY());
            yMax = Math.max(yMax, p.getY());
        }

        // pro každý scanline
        for (int y = yMin; y <= yMax; y++) {
            List<Double> xs = new ArrayList<>();

            // projdi všechny hrany (i -> i+1)
            for (int i = 0; i < pts.size(); i++) {
                Point a = pts.get(i);
                Point b = pts.get((i + 1) % pts.size());

                int y1 = a.getY();
                int y2 = b.getY();

                // ignoruj horizontální hrany
                if (y1 == y2) continue;

                // pravidlo [minY, maxY) — horní vrchol se nepočítá
                int minY = Math.min(y1, y2);
                int maxY = Math.max(y1, y2);
                if (y < minY || y >= maxY) continue;

                // průsečík x na přímce hrany
                double t = (y - y1) / (double)(y2 - y1);
                double x = a.getX() + t * (b.getX() - a.getX());
                xs.add(x);
            }

            if (xs.size() < 2) continue;
            Collections.sort(xs);

            // vyplň po dvojicích
            for (int i = 0; i + 1 < xs.size(); i += 2) {
                int xStart = (int)Math.ceil(xs.get(i));
                int xEnd   = (int)Math.floor(xs.get(i + 1));

                for (int x = xStart; x <= xEnd; x++) {
                    raster.setPixel(x, y, fillColor);
                }
            }
        }
    }
}
