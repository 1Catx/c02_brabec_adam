package rasterize;

import model.Line;

public class LineRasterizerGraphics extends LineRasterizer {
    
    public LineRasterizerGraphics(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(Line line) {
        rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) { //https://www.geeksforgeeks.org/computer-graphics/dda-line-generation-algorithm-computer-graphics
        int dx = x2 - x1; int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xInc = dx / (float) steps; float yInc = dy / (float) steps;
        float x = x1; float y = y1;

        for (int i = 0; i <= steps; i++) {
            raster.setPixel(Math.round(x), Math.round(y), 0xFFFF0000);
            x += xInc;
            y += yInc;
        }
    }

    /*
    DDA algoritmus
    Výhody: jednoduchý, univerzální pro všechny sklony, jednodušší implementace než Bresenham
    Nevýhody: používá float (zaokrouhlovací chyby a jejich kumulace), pomalejší než Bresenham 
        (Bresenham používá čistě celočíselné operace a jednoduché sčítání)
     */
}