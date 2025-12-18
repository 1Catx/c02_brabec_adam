package control;

import view.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import model.Point;
import model.Polygon;
import rasterize.LineRasterizerGraphics;

import javax.swing.SwingUtilities; 
import fill.SeedFill; 
import fill.SeedFillBorder; 


public class Controller2D {
    private final Panel myPanel;
    private final LineRasterizerGraphics lr;

    private final Polygon poly = new Polygon();
    private Point preview = null; //dočasná pozice při tažení
    private boolean dragging = false;

    private final int fillColor = 0xFF00FF00; 
    private final int borderColor = 0xFFFF0000;


    public Controller2D(Panel myPanel) {
        this.myPanel = myPanel;

        this.lr = new LineRasterizerGraphics(myPanel.getRaster());

        initListeners();
    }

    private void initListeners() {
        myPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    //SeedFill filler = new SeedFill(myPanel.getRaster(), e.getX(), e.getY(), fillColor);
                    SeedFillBorder filler = new SeedFillBorder(myPanel.getRaster(), e.getX(), e.getY(), fillColor, borderColor);
                    filler.fill();
                    myPanel.repaint();
                    return; // neprovádět logiku pro polygon
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragging = true;
                    preview = new Point(e.getX(), e.getY());
                    drawScene();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
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
                if (!dragging) return;

                Point a = poly.getSize() > 0 ? poly.getPoint(poly.getSize() - 1) : new Point(e.getX(), e.getY());

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
                    drawScene(); }
            }
        });
    }

    private void drawScene() {
        myPanel.getRaster().clear();

        List<Point> pts = poly.getMyPoints(); //hotové hrany
        
        for (int i = 0; i < pts.size() - 1; i++) {
            drawLine(pts.get(i), pts.get(i + 1));
        }

        if (!dragging && pts.size() >= 3) { //uzavření polygonu
            drawLine(pts.get(pts.size() - 1), pts.get(0));
        }

        if (dragging && preview != null) { //náhled při tažení
            if (pts.size() >= 1) {
                drawLine(pts.get(pts.size() - 1), preview);
            }

            if (pts.size() >= 2) {
                drawLine(preview, pts.get(0));
            }
        }

        myPanel.repaint();
    }

    private void drawLine(Point a, Point b) {
        lr.rasterize(a.getX(), a.getY(), b.getX(), b.getY());
    }

    private void clearAll() {
        poly.clear();
        preview = null;
        dragging = false;

        myPanel.getRaster().clear();
        myPanel.repaint();
    }
}

/*
    Pořád bude potřeba vykreslovat polygon (levé tlačítko)
    
    Záplavový algoritmus ohraničený barvou pozadí (prostřední tlačítko)
    Záplavový algoritmus ohraničený barvou hranice (pravé tlačítko)

    Funkce pro kreslení obdelníka (Shift mode)
*/