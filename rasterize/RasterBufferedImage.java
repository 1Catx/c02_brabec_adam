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
        int w = image.getWidth();
        int h = image.getHeight();

        if(x < 0 || x > w) return;
        if(y < 0 || y > h) return;
        image.setRGB(x, y, color);
    }

    public Graphics getGraphics(){
        return image.getGraphics();
    }

    public void draw(RasterBufferedImage raster) {
        Graphics graphics = getGraphics();
        graphics.setColor(new Color(color));
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(raster.image, 0, 0, null);
    }

    public void repaint(Graphics graphics) {
        graphics.drawImage(image, 0, 0, null);
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
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
    }

    public BufferedImage getImage() {
        return image;
    }
}
