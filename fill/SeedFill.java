package fill;

import rasterize.Raster;

public class SeedFill implements Filler {
    private Raster raster;
    private int startX, startY;
    private int fillColor;
    private int backgroundColor;

    public SeedFill(Raster raster, int startX, int startY, int fillColor) {
        this.raster = raster;
        this.startX = startX;
        this.startY = startY;
        this.fillColor = fillColor;

        this.backgroundColor = raster.getPixel(startX, startY);
    }

    @Override
    public void fill() {
        seedFill(startX, startY);
    }

    private void seedFill(int x, int y) {
        int pixelColor = raster.getPixel(x, y);

        if (pixelColor != backgroundColor) return;

        raster.setPixel(x, y, fillColor);

        seedFill(x + 1, y);
        seedFill(x - 1, y);
        seedFill(x, y + 1);
        seedFill(x, y - 1);
    }
}

