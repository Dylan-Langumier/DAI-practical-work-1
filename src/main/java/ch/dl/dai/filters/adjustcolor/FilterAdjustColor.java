package ch.dl.dai.filters.adjustcolor;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;
import java.awt.*;

public class FilterAdjustColor implements ImageFilter {
  @Override
  public void applyFilter(Image image, int... args) {
    Color targetColor;
    int intensity;
    if (args.length < 4) {
      /// TODO : once we get values as arguments throw an error
      targetColor = new Color(70, 180, 50);
      intensity = -25;
    } else {
      targetColor = new Color(args[0], args[1], args[2]);
      intensity = args[3];
    }

    double intensityFactor = (intensity) / 100.;

    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        Color c = image.getPixel(x, y);
        int red = adjustIntensity(c.getRed(), targetColor.getRed(), intensityFactor),
            green = adjustIntensity(c.getGreen(), targetColor.getGreen(), intensityFactor),
            blue = adjustIntensity(c.getBlue(), targetColor.getBlue(), intensityFactor);
        image.setPixel(x, y, new Color(red, green, blue));
      }
    }
  }

  int adjustIntensity(int original, int desired, double intensityFactor) {
    int result = original + (int) ((desired - original) * intensityFactor);
    if (result > 255) result = 255;
    else if (result < 0) result = 0;
    return result;
  }
}
