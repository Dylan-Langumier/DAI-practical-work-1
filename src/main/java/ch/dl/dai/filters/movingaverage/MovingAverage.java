package ch.dl.dai.filters.movingaverage;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;
import java.awt.Color;

public class MovingAverage implements ImageFilter {
  @Override
  public void applyFilter(Image image) {
    Image referenceCopy = new Image(image);
    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        image.setPixel(x, y, average(referenceCopy, x, y));
      }
    }
  }

  private static Color average(Image image, int x, int y) {
    final int DEFAULT = 3;
    return average(image, x, y, DEFAULT);
  }

  private static Color average(Image image, int x, int y, int squareSize) {
    int halfSize = squareSize / 2;
    int red = 0, green = 0, blue = 0, alpha = 0;
    int nbAveragedPixels = 0;
    for (int i = x - halfSize; i <= x + halfSize; ++i) {
      for (int j = y - halfSize; j <= y + halfSize; ++j) {
        if (i < 0 || i >= image.getWidth() || j < 0 || j >= image.getHeight()) continue;
        ++nbAveragedPixels;
        Color pixel = image.getPixel(i, j);
        red += pixel.getRed();
        green += pixel.getGreen();
        blue += pixel.getBlue();
        alpha += pixel.getAlpha();
      }
    }
    red /= nbAveragedPixels;
    green /= nbAveragedPixels;
    blue /= nbAveragedPixels;
    alpha /= nbAveragedPixels;
    return new Color(red, green, blue, alpha);
  }
}
