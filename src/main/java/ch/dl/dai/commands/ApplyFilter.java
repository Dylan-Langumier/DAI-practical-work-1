package ch.dl.dai.commands;

import ch.dl.dai.file.ImageFile;
import ch.dl.dai.file.bmp.ImageBMP;
import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.filters.alpha.FilterAlpha;
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
      case GRAYSCALE, MOVING_AVERAGE, ALPHA -> {
        return 1;
      }
      default -> { // Includes Sepia
        return 0;
      }
    }
  }

  @Override
  public Integer call() {
    ImageFilter filter =
        switch (parent.getFilter()) {
          case SEPIA -> new FilterSepia();
          case GRAYSCALE -> new FilterGrayscale();
          case MOVING_AVERAGE -> new FilterMovingAverage();
          case ALPHA -> new FilterAlpha();
        };

    ImageFile file =
        switch (parent.getImageType()) {
          case BMP -> new ImageBMP();
        };

    final int[] subParameters =
        Arrays.copyOfRange(parent.getParameters(), 0, getNumberOfArgumentsForCalledFilter());
    if (parent.getParameters().length > subParameters.length) {
      final int[] ignoredParameters =
          Arrays.copyOfRange(
              parent.getParameters(),
              getNumberOfArgumentsForCalledFilter(),
              parent.getParameters().length);
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
