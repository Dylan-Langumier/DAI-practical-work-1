package ch.dl.dai.filters.colorintensity;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;
import java.awt.*;

/**
 * This filter allows to modify the color's intensity When augmenting the intensity, distortion
 * artefacts can appear Negative values will invert the colors
 */
public class FilterColorIntensity implements ImageFilter {
  @Override
  public void applyFilter(Image image, int... args) {
    int intensity = args[0];

    double intensityFactor = -(intensity - 100) / 100.;

    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        Color c = image.getPixel(x, y);
        int average = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
        int red = adjustIntensity(c.getRed(), average, intensityFactor),
            green = adjustIntensity(c.getGreen(), average, intensityFactor),
            blue = adjustIntensity(c.getBlue(), average, intensityFactor);
        image.setPixel(x, y, new Color(red, green, blue));
      }
    }
  }

  int adjustIntensity(int original, int average, double intensityFactor) {
    int result = original + (int) ((average - original) * intensityFactor);
    if (result > 255) result = 255;
    else if (result < 0) result = 0;
    return result;
  }
}
