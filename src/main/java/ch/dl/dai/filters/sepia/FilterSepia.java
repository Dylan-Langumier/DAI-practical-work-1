package ch.dl.dai.filters.sepia;

import static java.lang.Math.*;

import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.image.Image;
import java.awt.Color;

public class FilterSepia implements ImageFilter {
  @Override
  public void applyFilter(Image image, int... args) {
    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        Color pixel = image.getPixel(x, y);
        // Weighting values were gotten from https://stackoverflow.com/questions/1061093/how-is-a-sepia-tone-created
        // Values are not parsed into bytes as bytes are signed
        int red =
            (int)
                min(
                    (.393 * pixel.getRed())
                        + (.769 * pixel.getGreen())
                        + (.189 * (pixel.getBlue())),
                    255.0);
        int green =
            (int)
                min(
                    (.349 * pixel.getRed()) + (.686 * pixel.getGreen()) + (.168 * pixel.getBlue()),
                    255.0);
        int blue =
            (int)
                min(
                    (.272 * pixel.getRed()) + (.534 * pixel.getGreen()) + (.131 * pixel.getBlue()),
                    255.0);
        image.setPixel(x, y, new Color(red, green, blue));
      }
    }
  }
}
