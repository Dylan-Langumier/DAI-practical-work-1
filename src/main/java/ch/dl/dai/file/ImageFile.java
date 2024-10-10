package ch.dl.dai.file;

import ch.dl.dai.image.Image;

public interface ImageFile {
  Image open(String filePath);

  void write(String filePath, Image image);
}
