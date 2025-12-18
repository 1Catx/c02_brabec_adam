package fill;

import rasterize.Raster;

public class SeedFillBorder implements Filler {

    private final Raster raster;
    private final int startX, startY;
    private final int fillColor;
    private final int borderColor;

    public SeedFillBorder(Raster raster, int startX, int startY, int fillColor, int borderColor) {
        this.raster = raster;
        this.startX = startX;
        this.startY = startY;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }

    @Override
    public void fill() {
        seedFill(startX, startY);
    }

    private void seedFill(int x, int y) {
        int c = raster.getPixel(x, y);

        if (c == borderColor || c == fillColor) return;

        raster.setPixel(x, y, fillColor);

        seedFill(x + 1, y);
        seedFill(x - 1, y);
        seedFill(x, y + 1);
        seedFill(x, y - 1);
    }
}
