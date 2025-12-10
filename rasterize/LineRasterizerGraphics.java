package rasterize;

import java.awt.*;

public class LineRasterizerGraphics extends LineRasterizer {


    public LineRasterizerGraphics(Raster raster) {
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        Graphics g = ((RasterBufferedImage)raster).getImage().getGraphics();
        g.setColor(this.color);
        g.drawLine(x1, y1, x2, y2);
    }

}
