package ch.dl.dai.file.bmp;

import ch.dl.dai.file.ImageFile;
import ch.dl.dai.image.Image;

import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import static java.lang.Math.*;

public class ImageBMP implements ImageFile {
    private Color readRGBA32(RandomAccessFile in) throws IOException {
        int RGBA32Value = in.readInt();
        int red = (RGBA32Value >> 24) & 0xFF;
        int green = (RGBA32Value >> 16) & 0xFF;
        int blue = (RGBA32Value >> 8) & 0xFF;
        int alpha = RGBA32Value & 0xFF;
        return new Color(red, green, blue, alpha);
    }

    private Color readRGB24(RandomAccessFile in) throws IOException {
        int RGB24Value = in.readInt();
        int red = (RGB24Value >> 16) & 0xFF;
        int green = (RGB24Value >> 8) & 0xFF;
        int blue = RGB24Value & 0xFF;
        return new Color(red, green, blue);
    }

    private Color readRGBA16(RandomAccessFile in) throws IOException {
        int RGBA16Value = in.readInt();
        int red = (RGBA16Value >> 12) & 0xF;
        int green = (RGBA16Value >> 8) & 0xF;
        int blue = (RGBA16Value >> 4) & 0xF;
        int alpha = RGBA16Value & 0xF;
        return new Color(red, green, blue, alpha);
    }

    @Override
    public Image open(String filePath) {

        try(RandomAccessFile in = new RandomAccessFile(filePath, "r"))
        {
            //Read BitMap header
            char fileHeader = in.readChar();
            int fileSize = in.readInt();
            in.skipBytes(4);
            int dataOffset = in.readInt();

            //verifie signature of bitmap file
            if (fileHeader != 0x4D && fileHeader != 0x4F)
                throw new IllegalArgumentException("Invalid file format");

            //read DIB header
            int dibSize = in.readInt();
            int width = in.readInt();
            int height = in.readInt();
            char nbPlanes = in.readChar();
            char colorDepth = in.readChar();
            int compressionType = in.readInt();
            int rawDataSize = in.readInt();
            int hResolution = in.readInt();
            int vResolution = in.readInt();
            int numberOfColors = in.readInt();
            int numberOfImportantColors = in.readInt();

            //verify it and correct it if possible
            if(dibSize != 40)
                throw new UnsupportedEncodingException("only supports BITMAPINFOHEADER");
            if(nbPlanes != 1)
                throw new UnsupportedEncodingException("Supports only one color plane");
            if(compressionType != 0)
                throw new UnsupportedEncodingException("Doesn't support compression");
            if(numberOfColors == 0)
                numberOfColors = (int)pow(2,colorDepth);

            //let's ignore color palettes for now
            //since we are allowing only the most basic color tables, we consider 32bpp
            in.skipBytes(4 * numberOfColors);

            //read pixels (fucking finally...)
            int rowSize = (int)ceil((colorDepth * width) / 32.) * 4;
            int pixelArraySize = rowSize * abs(height);
            int numberOfPixels = width * height;

            Color[] pixels = new Color[numberOfPixels];

            for(int i = 0; i < numberOfPixels; i++) {
                ///TODO: read 1
                ///TODO: read 4
                pixels[i] = switch (colorDepth){
                    //color depth 1 - 8 need a color table
                    case 16 -> readRGBA16(in);
                    case 24 -> readRGB24(in);
                    case 32 -> readRGBA32(in);
                    default -> throw new UnsupportedEncodingException("Unsupported color depth");
                };
            }

            return new Image(pixels, width, height);

        }
        catch (IOException e) {
            System.out.println("Error opening file " + filePath + ": " + e);
        }

        return null;
    }

    @Override
    public void write(String filePath, Image image) {

    }
}
