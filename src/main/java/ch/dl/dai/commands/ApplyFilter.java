package ch.dl.dai.commands;

import ch.dl.dai.file.ImageFile;
import ch.dl.dai.file.bmp.ImageBMP;
import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.filters.adjustcolor.FilterAdjustColor;
import ch.dl.dai.filters.colorintensity.FilterColorIntensity;
import ch.dl.dai.filters.grayscale.FilterGrayscale;
import ch.dl.dai.filters.movingaverage.FilterMovingAverage;
import ch.dl.dai.filters.sepia.FilterSepia;
import ch.dl.dai.image.Image;
import java.util.Arrays;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "apply", description = "Apply a filter.")
public class ApplyFilter implements Callable<Integer> {
  @CommandLine.ParentCommand protected Root parent;

  public int getNumberOfArgumentsForCalledFilter() {
    switch (parent.getFilter()) {
      case GRAYSCALE, MOVING_AVERAGE, COLOR_INTENSITY -> {
        return 1;
      }
      case ADJUST_COLOR -> {
        return 4;
      }
      default -> { // Includes Sepia
        return 0;
      }
    }
  }

  @Override
  public Integer call() {
    final int[] pParameters = parent.getParameters();
    if (pParameters == null) throw new RuntimeException("No argument provided");

    final int nbArgs = getNumberOfArgumentsForCalledFilter();
    if (pParameters.length < nbArgs)
      throw new RuntimeException(
          "Too few arguments provided. (Got: " + pParameters.length + ", expected " + nbArgs + ")");

    ImageFilter filter =
        switch (parent.getFilter()) {
          case SEPIA -> new FilterSepia();
          case GRAYSCALE -> new FilterGrayscale();
          case MOVING_AVERAGE -> new FilterMovingAverage();
          case COLOR_INTENSITY -> new FilterColorIntensity();
          case ADJUST_COLOR -> new FilterAdjustColor();
        };

    ImageFile file =
        switch (parent.getImageType()) {
          case BMP -> new ImageBMP();
        };

    final int[] subParameters = Arrays.copyOfRange(pParameters, 0, nbArgs);
    if (pParameters.length > subParameters.length) {
      final int[] ignoredParameters = Arrays.copyOfRange(pParameters, nbArgs, pParameters.length);
      System.err.println(
          "\u001B[33m[⚠] "
              + ignoredParameters.length
              + " parameter"
              + (ignoredParameters.length > 1 ? "s were" : " was")
              + " ignored: "
              + Arrays.toString(ignoredParameters)
              + "\u001B[0m");
    }
    Image image = file.open(parent.getFilepath());
    filter.applyFilter(image, subParameters);
    file.write(parent.getOutputFilepath(), image);

    return 0;
  }
}
