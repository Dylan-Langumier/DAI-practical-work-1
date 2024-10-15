package ch.dl.dai.filters.sepia;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;
import java.awt.Color;

public class FilterSepia implements ImageFilter {
  @Override
  public void applyFilter(Image image, int... args) {
    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        Color pixel = image.getPixel(x, y);
        int originR = pixel.getRed();
        int originG = pixel.getGreen();
        int originB = pixel.getBlue();
        // Weighting values were gotten from
        // https://stackoverflow.com/questions/1061093/how-is-a-sepia-tone-created
        // Values are not cast into bytes as bytes are signed
        int newR = (int) Math.min((.393 * originR) + (.769 * originG) + (.189 * originB), 255.0);
        int newG = (int) Math.min((.349 * originR) + (.686 * originG) + (.168 * originB), 255.0);
        int newB = (int) Math.min((.272 * originR) + (.534 * originG) + (.131 * originB), 255.0);
        image.setPixel(x, y, new Color(newR, newG, newB));
      }
    }
  }
}
