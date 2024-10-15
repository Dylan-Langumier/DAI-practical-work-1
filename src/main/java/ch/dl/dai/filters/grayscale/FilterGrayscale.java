package ch.dl.dai.filters.grayscale;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;
import java.awt.Color;

public class FilterGrayscale implements ImageFilter {
  @Override
  public void applyFilter(Image image, int... args) {
    int intensity = Math.clamp(args[0], 0, 100);
    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        Color c = image.getPixel(x, y);
        float average = (c.getRed() + c.getGreen() + c.getBlue()) / (3f * 255f);
        average *= (intensity / 100f);
        Color gray = new Color(average, average, average);
        image.setPixel(x, y, gray);
      }
    }
  }
}
