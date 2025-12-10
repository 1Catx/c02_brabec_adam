package rasterize;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterBufferedImage implements Raster { //třída, která drží samotný obrázek a umožňuje do něj zapisovat pixely

    private BufferedImage image; //standardní Java třída pro práci s obrázky v paměti, pole pixelů (matice), které můžeme číst a měnit
    private int color;

    public RasterBufferedImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        int w = getWidth();
        int h = getHeight();

        if(x < 0 || x > w) return;
        if(y < 0 || y > h) return;
        image.setRGB(x, y, color);
    }

    @Override
    public int getPixel(int x, int y) {
        // TODO: druhá úloha
        return 0;
    }

    @Override
    public void setClearColor(int color) {
        // TODO: druhá úloha
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void clear() {
        Graphics g = image.getGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
    }

    public BufferedImage getImage() {
        return image;
    }
}
