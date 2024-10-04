package ch.dl.dai.commands;

import picocli.CommandLine;

@CommandLine.Command(
    description = "CLI to apply basic filters on images",
    version = "1.0.0",
    subcommands = {
      ApplyFilter.class,
    },
    scope = CommandLine.ScopeType.INHERIT,
    mixinStandardHelpOptions = true)
public class Root {
  public enum AvailableFilter {
    SEPIA,
    GRAYSCALE,
    MOVING_AVERAGE,
    ALPHA,
  }

  public enum SupportedImageType {
    BMP,
  }

  @CommandLine.Parameters(index = "0", description = "The name of the image file.")
  protected String filename;

  @CommandLine.Parameters(index = "1", description = "The name of the output image file.")
  protected String outputFilename;

  @CommandLine.Option(
      names = {"-f", "--filter"},
      description = "The filter to apply on the image (possible values: ${COMPLETION-CANDIDATES}).",
      required = true)
  protected AvailableFilter filter;

  public SupportedImageType getImageType() throws RuntimeException {
    String fileExtension = filename.substring(filename.lastIndexOf('.') + 1);
    if (fileExtension.equalsIgnoreCase("bmp")) return SupportedImageType.BMP;
    else throw new RuntimeException("Unsupported file (" + fileExtension.toUpperCase() + ").");
  }

  public String getOutputFilename() {
    return outputFilename;
  }

  public AvailableFilter getFilter() {
    return filter;
  }

  public String getFilename() {
    return filename;
  }
}
