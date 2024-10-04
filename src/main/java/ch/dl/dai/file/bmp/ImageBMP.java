package ch.dl.dai.file.bmp;

import static java.lang.Math.*;

import ch.dl.dai.file.ImageFile;
import ch.dl.dai.image.Image;
import java.awt.*;
import java.io.*;

// main reference : https://en.wikipedia.org/wiki/BMP_file_format
public class ImageBMP implements ImageFile {

  @Override
  public Image open(String filePath) {

    try (RandomAccessFile in = new RandomAccessFile(filePath, "r")) {
      // Read BitMap header
      char fileHeader = readChar(in);
      in.skipBytes(4); // fileSize
      in.skipBytes(4); // reserved 1 & 2
      int dataOffset = readInt(in);

      // verifie signature of bitmap file
      if (fileHeader != 0x4D42) throw new IllegalArgumentException("Invalid file format");

      // read DIB BITMAPINFOHEADER (40 bytes)
      int dibSize = readInt(in);
      int width = readInt(in);
      int height = readInt(in);
      char nbPlanes = readChar(in);
      char colorDepth = readChar(in);
      int compressionType = readInt(in);
      in.skipBytes(5 * 4); // rawDataSize, h and v resolution, numberOfColor and number of important
      // colors, 5 * int

      // verify it and correct it if possible
      if (dibSize < 40)
        throw new UnsupportedEncodingException("only supports BITMAPINFOHEADER and greater");
      // skip trough rest of header if necessary
      if (dibSize > 40) in.skipBytes(dibSize - 40);
      if (nbPlanes != 1) throw new UnsupportedEncodingException("Supports only one color plane");
      if (compressionType != 0)
        throw new UnsupportedEncodingException("Doesn't support compression");

      // deal with inverted images and negative heights
      boolean isBottumUp = true;
      if (height < 0) {
        isBottumUp = false;
        height = abs(height);
      }

      // let's ignore color palettes for now and jump to the data offset
      in.seek(dataOffset);

      // read pixels
      int rowSize = (int) ceil((colorDepth * width) / 32.) * 4;
      int padding = (rowSize * 8 - colorDepth * width) / 8;
      int numberOfPixels = width * height;

      Color[] pixels = new Color[numberOfPixels];

      int index;
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (isBottumUp) index = (height - y - 1) * width + x;
          else index = y * width + x;
          pixels[index] =
              switch (colorDepth) {
                  /// TODO: read 1,2,8 color depth 1 - 8 need a color table
                case 16 -> readRGBA16(in);
                case 24 -> readRGB24(in);
                case 32 -> readRGBA32(in);
                default -> throw new UnsupportedEncodingException("Unsupported color depth");
              };
        }
        in.skipBytes(padding);
      }

      return new Image(pixels, width, height);

    } catch (IOException e) {
      System.out.println("Error opening file " + filePath + ": " + e);
    }

    return null;
  }

  private void writeInt(BufferedOutputStream out, int value) throws IOException {
    out.write(value & 0xFF);
    out.write((value >> 8) & 0xFF);
    out.write((value >> 16) & 0xFF);
    out.write((value >> 24) & 0xFF);
  }

  private void writeChar(BufferedOutputStream out, char value) throws IOException {
    out.write(value & 0xFF);
    out.write((value >> 8) & 0xFF);
  }

  /**
   * Writes image as RGBA on 32 bpp without any compression
   *
   * @param filePath path to save to
   * @param image image to save
   */
  @Override
  public void write(String filePath, Image image) {
    try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath))) {
      // compute important info
      final int bitMapHeaderSize = 14;
      final int dibHeaderSize = 40;
      final int colorTableSize = 0; // not using color tables
      final int pixelArraySize = image.getWidth() * image.getHeight() * 4; // int (4bytes) per pixel
      final int fileSize = bitMapHeaderSize + dibHeaderSize + colorTableSize + pixelArraySize;

      // write BitMap header
      writeChar(out, (char) 0x4D42);
      writeInt(out, fileSize);
      writeInt(out, 0); // reserved 1 and 2
      writeInt(out, bitMapHeaderSize + dibHeaderSize + colorTableSize); // data offset

      // write dib header
      writeInt(out, dibHeaderSize);
      writeInt(out, image.getWidth());
      writeInt(out, image.getHeight());
      writeChar(out, (char) 1); // planes
      writeChar(out, (char) 32); // color depth
      writeInt(out, 0); // compression
      writeInt(out, pixelArraySize);
      writeInt(out, 0); // hres
      writeInt(out, 0); // vres
      writeInt(out, 0); // not using color tables
      writeInt(out, 0); // nb of important colors

      // write color table here (optional)

      // and now to write the pixel array from bottom-up
      for (int y = image.getHeight() - 1; y >= 0; --y) {
        for (int x = 0; x < image.getWidth(); ++x) {
          Color pixel = image.getPixel(x, y);
          out.write((byte) pixel.getBlue());
          out.write((byte) pixel.getGreen());
          out.write((byte) pixel.getRed());
          out.write((byte) pixel.getAlpha());
        }
      }

    } catch (IOException e) {
      System.out.println("Error writing to file " + filePath + ": " + e);
    }
  }

  /** Reads little endian int from RandomAccessFile and return it as big endian (default for JVM) */
  private int readInt(RandomAccessFile in) throws IOException {
    int b0 = in.read();
    int b1 = in.read();
    int b2 = in.read();
    int b3 = in.read();
    return b0 & 0xFF | (b1 << 8) & 0xFF00 | (b2 << 16) & 0xFF0000 | (b3 << 24) & 0xFF000000;
  }

  /** Reads little endian char (2 bytes) and returns it as big endian */
  private char readChar(RandomAccessFile in) throws IOException {
    int b0 = in.read();
    int b1 = in.read();
    return (char) (b0 & 0xFF | (b1 << 8) & 0xFF00);
  }

  private Color readRGBA32(RandomAccessFile in) throws IOException {
    int blue = in.read();
    int green = in.read();
    int red = in.read();
    int alpha = in.read();
    return new Color(red, green, blue, alpha);
  }

  private Color readRGB24(RandomAccessFile in) throws IOException {
    int blue = in.read();
    int green = in.read();
    int red = in.read();
    return new Color(red, green, blue);
  }

  private Color readRGBA16(RandomAccessFile in) throws IOException {
    char tmp = readChar(in);
    int red = (tmp >> 12) & 0xF;
    int green = (tmp >> 8) & 0xF;
    int blue = (tmp >> 4) & 0xF;
    int alpha = tmp & 0xF;
    return new Color(red, green, blue, alpha);
  }
}
