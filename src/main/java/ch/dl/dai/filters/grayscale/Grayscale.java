package ch.dl.dai.filters.grayscale;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;
import java.awt.Color;

public class Grayscale implements ImageFilter {
  @Override
  public void applyFilter(Image image) {
    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        Color c = image.getPixel(x, y);
        int average = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
        Color gray = new Color(average, average, average);
        image.setPixel(x, y, gray);
      }
    }
  }
}
