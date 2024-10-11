package ch.dl.dai.commands;

import ch.dl.dai.file.ImageFile;
import ch.dl.dai.file.bmp.ImageBMP;
import ch.dl.dai.filters.ImageFilter;
import ch.dl.dai.filters.alpha.Alpha;
import ch.dl.dai.filters.grayscale.Grayscale;
import ch.dl.dai.filters.movingaverage.MovingAverage;
import ch.dl.dai.filters.sepia.Sepia;
import ch.dl.dai.image.Image;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "apply", description = "Apply a filter.")
public class ApplyFilter implements Callable<Integer> {
  @CommandLine.ParentCommand protected Root parent;

  @Override
  public Integer call() {
    ImageFilter filter =
        switch (parent.getFilter()) {
            // TODO: Refactor into xFilter instead of just x
          case SEPIA -> new Sepia();
          case GRAYSCALE -> new Grayscale();
          case MOVING_AVERAGE -> new MovingAverage();
          case ALPHA -> new Alpha();
        };

    ImageFile file =
        switch (parent.getImageType()) {
          case BMP -> new ImageBMP();
        };

    Image image = file.open(parent.getFilename());
    filter.applyFilter(image);
    file.write(parent.getOutputFilename(), image);

    return 0;
  }
}
