package ch.dl.dai.filters;

import ch.dl.dai.image.Image;

public interface ImageFilter {
  void applyFilter(Image image, int... args);
}
