package control;

import view.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import model.Point;
import model.Polygon;
import model.RectanglePolygon;
import rasterize.LineRasterizerGraphics;

import javax.swing.SwingUtilities; 
import fill.SeedFill; // momentálně nepoužívaná ale připravená na přepnutí mezi SeedFillem a SeedFillBorder
import fill.SeedFillBorder; 


public class Controller2D {
    private final Panel myPanel;
    private final LineRasterizerGraphics lr;

    private final Polygon poly = new Polygon();
    private Point preview = null; //dočasná pozice při tažení
    private boolean dragging = false;

    private final int fillColor = 0xFF00FF00; 
    private final int borderColor = 0xFFFF0000;

    private boolean rectBaseDragging = false;
    private boolean rectWaitingHeight = false;
    private Point rectA = null;
    private Point rectB = null;

    private final java.util.List<RectanglePolygon> rectangles = new java.util.ArrayList<>();

    private final Polygon clipper = new Polygon();
    private boolean clipperEnabled = true; //kvůli mazání -> C

    private void initClipper() {
        clipper.addPoint(new Point(150, 120));
        clipper.addPoint(new Point(650, 130));
        clipper.addPoint(new Point(740, 260));
        clipper.addPoint(new Point(620, 520));
        clipper.addPoint(new Point(200, 500));
    }


    public Controller2D(Panel myPanel) {
        this.myPanel = myPanel;
        this.lr = new LineRasterizerGraphics(myPanel.getRaster());

        initClipper(); // vytvoří fixní polygon + clipperEnabled = true
        initListeners();

        drawScene();
    }

    private void initListeners() {
        myPanel.addMouseListener(new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {

            //SHIFT režim – OBDELNÍK
            if (e.isShiftDown() && SwingUtilities.isLeftMouseButton(e)) {
                handleRectangleMousePressed(e);
                return;
            }

            //Pravé tlačítko – FILL
            if (SwingUtilities.isRightMouseButton(e)) {
                //SeedFill filler = new SeedFill(myPanel.getRaster(), e.getX(), e.getY(), fillColor);
                SeedFillBorder filler = new SeedFillBorder(myPanel.getRaster(), e.getX(), e.getY(), fillColor, borderColor);
                filler.fill();
                myPanel.repaint();
                return;
            }

            // Levé tlačítko - Polygon
            if (SwingUtilities.isLeftMouseButton(e)) {
                dragging = true;
                preview = new Point(e.getX(), e.getY());
                drawScene();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //SHIFT režim – OBDELNÍK
            if (e.isShiftDown() && SwingUtilities.isLeftMouseButton(e)) {
                handleRectangleMouseReleased(e);
                return;
            }

            // Levé tlačítko - Polygon
            if (!SwingUtilities.isLeftMouseButton(e)) return;
            if (!dragging) return;

            dragging = false;
            preview = null;
            poly.addPoint(new Point(e.getX(), e.getY()));
            drawScene();
        }
    });

        myPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //SHIFT režim – táhnutí základny
                if (rectBaseDragging) {
                    rectB = new Point(e.getX(), e.getY());
                    drawScene();
                    return;
                }


                if (!dragging) return;
                preview = new Point(e.getX(), e.getY());
                drawScene();
            }
        });

        myPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_C) {
                    clearAll();
                }

                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_G) { 
                    drawScene();
                }
            }
        });
    }

    private void drawScene() {
        myPanel.getRaster().clear();

        drawUserPolygonWithPreview(); // uživatelský polygon + jeho náhled
        drawRectanglesWithPreview(); //  obdélníky (Shift) + náhled základny

        // fixní polygon + scanline fill výsledku
        if (clipperEnabled) {
            Polygon clipped = clip.Clipper.clipConvex(poly, clipper);
            fill.ScanLine.fillPolygon(myPanel.getRaster(), clipped, fillColor);

            drawPolygonOutline(clipper);
        }

        myPanel.repaint();
    }

    private void drawUserPolygonWithPreview() {
        List<Point> pts = poly.getMyPoints();

        // hotové hrany
        for (int i = 0; i < pts.size() - 1; i++) {
            drawLine(pts.get(i), pts.get(i + 1));
        }

        // uzavření polygonu (když netáhnu)
        if (!dragging && pts.size() >= 3) {
            drawLine(pts.get(pts.size() - 1), pts.get(0));
        }

        // náhled při tažení
        if (dragging && preview != null) {
            if (pts.size() >= 1) {
                drawLine(pts.get(pts.size() - 1), preview);
            }

            if (pts.size() >= 2) {
                drawLine(preview, pts.get(0));
            }
        }
    }

    private void drawRectanglesWithPreview() {
        // uložené obdélníky
        for (RectanglePolygon r : rectangles) {
            drawPolygonOutline(r);
        }

        // náhled základny obdélníku
        if (rectA != null && rectB != null && (rectBaseDragging || rectWaitingHeight)) {
            drawLine(rectA, rectB);
        }
    }

    private void drawLine(Point a, Point b) {
        lr.rasterize(a.getX(), a.getY(), b.getX(), b.getY());
    }

    private void clearAll() {
        poly.clear(); //vymazání polygonů
        rectangles.clear(); //vymazání obdelníků

        preview = null;
        dragging = false;
        clipperEnabled = false;

        myPanel.getRaster().clear();
        myPanel.repaint();
    }

    private void handleRectangleMousePressed(MouseEvent e) {
        // Pokud už máme základnu a čekáme na výšku, tak tento klik je bod C
        if (rectWaitingHeight && rectA != null && rectB != null) {
            Point c = new Point(e.getX(), e.getY());

            RectanglePolygon rect = new RectanglePolygon();
            rect.recompute(rectA, rectB, c);
            rectangles.add(rect);

            // reset režimu obdélníku
            rectWaitingHeight = false;
            rectBaseDragging = false;
            rectA = null;
            rectB = null;

            drawScene();
            return;
        }

        // Jinak začínáme zadávání základny
        rectBaseDragging = true;
        rectWaitingHeight = false;

        rectA = new Point(e.getX(), e.getY());
        rectB = new Point(e.getX(), e.getY());

        drawScene();
    }

    private void handleRectangleMouseReleased(MouseEvent e) {
        if (!rectBaseDragging) return;

        rectB = new Point(e.getX(), e.getY());
        rectBaseDragging = false;
        rectWaitingHeight = true; // teď čekáme na klik (bod C)
        drawScene();
    }

    private void drawPolygonOutline(Polygon p) {
        List<Point> pts = p.getMyPoints();

        for (int i = 0; i < pts.size(); i++) {
            Point a = pts.get(i); // aktuální bod polygonu
            Point b = pts.get((i + 1) % pts.size()); // vezme další bod a spojí ho s prvním (na konci modulo vrací zpátky na začátek)
            drawLine(a, b);
        }
    }

}