package view;

import rasterize.Raster;
import rasterize.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Panel extends JPanel {

    private RasterBufferedImage raster;
    private static final int FPS = 1000 / 20;

    public Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new RasterBufferedImage(width, height);
        setLoop();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(raster.getImage(), 0, 0, null); // zobrazí aktuální obraz z rasteru na panel
    }

    private void setLoop() {
        // časovač, který 30 krát za vteřinu obnoví obsah plátna aktuálním img
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, FPS);
    }

    public void clear() {
        raster.clear();
    }

    public Raster getRaster() {
        return raster;
    }
}
