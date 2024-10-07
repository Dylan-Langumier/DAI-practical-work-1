package ch.dl.dai.filters.movingaverage;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;

import java.awt.Color;

public class MovingAverage implements ImageFilter {
    @Override
    public void applyFilter(Image image) {
        if (image.getHeight() < 3 || image.getWidth() < 3) {
            throw new IllegalArgumentException("Image must be at least 3 x 3 to apply this filter");
        }
        Image referenceCopy = new Image(image);
        for (int x = 1; x < image.getWidth() - 1; ++x) {
            for (int y = 1; y < image.getHeight() - 1; ++y) {
                image.setPixel(x, y, average(referenceCopy, x, y));
            }
        }
    }

    private static Color average(Image image, int x, int y) {
        int red = 0, green = 0, blue = 0, alpha = 0;
        for (int i = x - 1; i <= x + 1; ++i) {
            for (int j = y - 1; j <= y + 1; ++j) {
                Color pixel = image.getPixel(i, j);
                red += pixel.getRed();
                green += pixel.getGreen();
                blue += pixel.getBlue();
                alpha += pixel.getAlpha();
            }
        }
        red /= 9;
        green /= 9;
        blue /= 9;
        alpha /= 9;
        return new Color(red, green, blue, alpha);
    }

}
