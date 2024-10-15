package ch.dl.dai.commands;

import ch.dl.dai.file.ImageFile;
import ch.dl.dai.file.bmp.ImageBMP;
import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.filters.alpha.FilterAlpha;
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
      case GRAYSCALE, MOVING_AVERAGE, ALPHA, COLOR_INTENSITY -> {
        return 1;
      }
      default -> { // Includes Sepia
        return 0;
      }
    }
  }

  @Override
  public Integer call() {
    final int[] pParameters = parent.getParameters();
    final int nbArgs = getNumberOfArgumentsForCalledFilter();

    ImageFilter filter =
        switch (parent.getFilter()) {
          case SEPIA -> new FilterSepia();
          case GRAYSCALE -> new FilterGrayscale();
          case MOVING_AVERAGE -> new FilterMovingAverage();
          case ALPHA -> new FilterAlpha();
          case COLOR_INTENSITY -> new FilterColorIntensity();
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
              + Arrays.toString(ignoredParameters));
    }
    Image image = file.open(parent.getFilename());
    filter.applyFilter(image, subParameters);
    file.write(parent.getOutputFilename(), image);

    return 0;
  }
}
