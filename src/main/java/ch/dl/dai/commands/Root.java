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

  static final int MAX_ARG = 4;

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
  String filename;

  @CommandLine.Parameters(index = "1", description = "The name of the output image file.")
  String outputFilename;

  @CommandLine.Parameters(
      index = "2",
      description = "Parameters of the filter (the required amount depends on the chosen filter)",
      arity = "0.." + MAX_ARG,
      defaultValue = "100",
      type = int.class)
  int[] p = new int[MAX_ARG]; // Short name chosen for CLI legibility

  @CommandLine.Option(
      names = {"-f", "--filter"},
      description = "The filter to apply on the image (possible values: ${COMPLETION-CANDIDATES}).",
      required = true)
  AvailableFilter filter;

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

  public int[] getParameters() {
    return p;
  }
}
