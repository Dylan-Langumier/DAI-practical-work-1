package ch.dl.dai.image;

import java.awt.Color;

/**
 A simple array of colors representing an image
 */
public class Image {
    private Color[] pixels;
    private int width, height;

    public Image(Color[] pixels, int width, int height) {
        if(width * height != pixels.length){
            throw new IllegalArgumentException("Dimensions don't match");
        }
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public Color getPixel(int x, int y) {
        return pixels[y * width + x];
    }

    public void setPixel(int x, int y, Color c) {
        pixels[y * width + x] = c;
    }
}
