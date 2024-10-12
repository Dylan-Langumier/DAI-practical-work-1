package ch.dl.dai.filters.colorIntensity;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;
import java.awt.*;

public class ColorIntensity implements ImageFilter {
  @Override
  public void applyFilter(Image image, int... args) {
    int intensity = 100;
    if (args.length > 0) {
      if (args[0] >= 0 && args[0] <= 200) intensity = args[0];
    }

    double intensityFactor = (intensity - 100) / 100.;

    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        Color c = image.getPixel(x, y);
        int average = (int) ((c.getRed() + c.getGreen() + c.getBlue()) / (3 * intensity / 100.));
        int red = adjustIntensity(c.getRed(), average, intensityFactor),
            green = adjustIntensity(c.getGreen(), average, intensityFactor),
            blue = adjustIntensity(c.getBlue(), average, intensityFactor);
        image.setPixel(x, y, new Color(red, green, blue));
      }
    }
  }

  int adjustIntensity(int original, int average, double intensityFactor) {
    return (int) (original + (average - original) * intensityFactor);
  }
}
